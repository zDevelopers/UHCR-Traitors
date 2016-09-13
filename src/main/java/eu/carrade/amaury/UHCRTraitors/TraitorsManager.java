/*
 * Copyright or © or Copr. AmauryCarrade (2015)
 * 
 * http://amaury.carrade.eu
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package eu.carrade.amaury.UHCRTraitors;

import eu.carrade.amaury.UHCReloaded.UHCReloaded;
import eu.carrade.amaury.UHCReloaded.events.UHGameStartsEvent;
import eu.carrade.amaury.UHCReloaded.teams.UHTeam;
import fr.zcraft.zlib.core.ZLibComponent;
import fr.zcraft.zlib.tools.PluginLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.UUID;


public class TraitorsManager extends ZLibComponent implements Listener
{
    private final Map<UUID, Traitor> traitors = new HashMap<>();

    private int traitorsCount;
    private final List<String> fakeNames = new ArrayList<>();
    private final Queue<String> availableFakeNames = new ArrayDeque<>();
    private int fakeNameNumber = 0;

    @Override
    protected void onEnable()
    {
        traitorsCount = Config.TRAITORS.COUNT.get();

        fakeNames.addAll(Config.TRAITORS.FAKE_NAMES);
        for (String name : new ArrayList<>(fakeNames))
            if (name.trim().isEmpty())
                fakeNames.remove(name);

        if (!fakeNames.isEmpty())
            availableFakeNames.addAll(fakeNames);
    }

    public int getTraitorsCount()
    {
        return traitorsCount;
    }

    public void setTraitorsCount(int traitorsCount)
    {
        if (traitorsGenerated())
            throw new IllegalStateException("Cannot update the traitors count after the traitors generation.");

        this.traitorsCount = traitorsCount;
    }

    public String getFakeName()
    {
        if (!fakeNames.isEmpty())
        {
            // If empty, we reuse the names
            if (availableFakeNames.isEmpty())
                availableFakeNames.addAll(fakeNames);

            return availableFakeNames.poll();
        }
        else
        {
            return String.valueOf(++fakeNameNumber);
        }
    }

    public boolean isTraitor(UUID id)
    {
        return traitors.containsKey(id);
    }

    public Traitor getTraitor(UUID id)
    {
        return traitors.get(id);
    }

    public boolean traitorsGenerated()
    {
        return !traitors.isEmpty();
    }

    public void generateTraitors()
    {
        generateTraitors(new Random());
    }

    public void generateTraitors(final Random random)
    {
        if (!traitorsGenerated())
        {
            final Set<UHTeam> teams = UHCReloaded.get().getTeamManager().getTeams();

            if (traitorsCount <= 0)
            {
                traitorsCount = teams.size();
            }

            // We first check if there is enough players to generate all traitors (else, this will result into an infinite loop)
            final Integer playersCount = UHCReloaded.get().getGameManager().getAlivePlayersCount();
            if (playersCount < traitorsCount)
                throw new IllegalArgumentException("Cannot generate traitors: not enough players! No traitor will be added.");
            else if (playersCount == traitorsCount)
                throw new IllegalArgumentException("Cannot generate traitors: all players would be traitors!  No traitor will be added.");

            for (int traitorsNeeded = traitorsCount ; traitorsNeeded > 0 ; )
            {
                final List<UHTeam> teamsRound = new LinkedList<>(teams);
                Collections.shuffle(teamsRound, random);

                for (final UHTeam team : teamsRound)
                {
                    // No other traitor needed
                    if (traitorsNeeded <= 0) break;

                    // We pick a random non-traitor-yet player in this team.
                    UUID traitor = null;

                    final List<UUID> players = new ArrayList<>(team.getPlayersUUID());
                    Collections.shuffle(players, random);

                    for (UUID player : players)
                    {
                        if (!isTraitor(player))
                        {
                            traitor = player;
                            break;
                        }
                    }

                    // If there where a non-traitor player left
                    if (traitor != null)
                    {
                        traitors.put(traitor, new Traitor(traitor));
                        traitorsNeeded--;
                    }
                }
            }
        }
        else
        {
            throw new IllegalStateException("Traitors are already generated");
        }
    }

    @EventHandler
    public void onGameStarts(UHGameStartsEvent ev)
    {
        try
        {
            generateTraitors();
            PluginLogger.info("{0} traitors generated.", traitors.size());
        }
        catch (IllegalArgumentException e)
        {
            PluginLogger.error("Unable to generate traitors. " + e.getMessage());
        }
    }
}
