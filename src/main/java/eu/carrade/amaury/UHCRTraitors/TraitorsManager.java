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
import eu.carrade.amaury.UHCReloaded.utils.UHSound;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zlib.components.rawtext.RawText;
import fr.zcraft.zlib.core.ZLibComponent;
import fr.zcraft.zlib.tools.PluginLogger;
import fr.zcraft.zlib.tools.items.ItemStackBuilder;
import fr.zcraft.zlib.tools.runners.RunTask;
import fr.zcraft.zlib.tools.text.MessageSender;
import fr.zcraft.zlib.tools.text.Titles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

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

    private boolean traitorsNotified = false;

    private final List<String> fakeNames = new ArrayList<>();
    private final Queue<String> availableFakeNames = new ArrayDeque<>();
    private int fakeNameNumber = 0;

    private String traitorsTeamInternalName = "";

    @Override
    protected void onEnable()
    {
        traitorsCount = Config.TRAITORS.COUNT.get();

        fakeNames.addAll(Config.TRAITORS.FAKE_NAMES);
        for (String name : new ArrayList<>(fakeNames))
            if (name.trim().isEmpty())
                fakeNames.remove(name);
    }



    /* **  TRAITORS MANAGEMENT  ** */


    /**
     * @return The amount of traitors to be generated
     */
    public int getTraitorsCount()
    {
        return traitorsCount;
    }

    /**
     * Updates the amount of traitors to generate.
     *
     * @param traitorsCount The amount. 0 to use the teams count.
     * @throws IllegalStateException if the traitors are generated.
     */
    public void setTraitorsCount(int traitorsCount)
    {
        if (traitorsGenerated())
            throw new IllegalStateException("Cannot update the traitors count after the traitors generation.");

        this.traitorsCount = traitorsCount;
    }

    /**
     * Generates a random fake name. It will be unique, except if all the names were used.
     *
     * @return A random fake name.
     */
    public String getFakeName()
    {
        if (!fakeNames.isEmpty())
        {
            // If empty, we reuse the names. Each times the order is different.
            if (availableFakeNames.isEmpty())
            {
                Collections.shuffle(fakeNames);
                availableFakeNames.addAll(fakeNames);
            }

            return availableFakeNames.poll();
        }
        else
        {
            return String.valueOf(++fakeNameNumber);
        }
    }

    /**
     * Updates the display name and team of the given player to appear like a traitor (all dark red).
     *
     * @param player The player to update.
     */
    public void addRevealedTraitorAttributes(Player player)
    {
        player.setDisplayName(ChatColor.DARK_RED + player.getName() + ChatColor.RESET);
        UHCReloaded.get().getScoreboardManager().getScoreboard().getTeam(traitorsTeamInternalName).addPlayer(player);
    }

    /**
     * @param id A player UUID
     * @return {@code true} if that player is a traitor.
     */
    public boolean isTraitor(UUID id)
    {
        return traitors.containsKey(id);
    }

    /**
     * @param id A player UUID
     * @return A {@link Traitor} object, or {@code null} if this player is not a traitor.
     */
    public Traitor getTraitor(UUID id)
    {
        return traitors.get(id);
    }

    /**
     * @return {@code true} if the traitors were notified.
     */
    public boolean areTraitorsNotified()
    {
        return traitorsNotified;
    }

    /**
     * @return {@code true} if the traitors were generated.
     */
    public boolean traitorsGenerated()
    {
        return !traitors.isEmpty();
    }



    /* **  TRAITORS GENERATION  ** */


    /**
     * Generates the traitors using a random seed.
     */
    public void generateTraitors()
    {
        generateTraitors(new Random());
    }

    /**
     * Generates the traitors using a custom {@link Random} object.
     *
     * @param random The {@link Random} object to use to generate the traitors.
     */
    private void generateTraitors(final Random random)
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



    /* **  TRAITORS INITIALIZATION  ** */


    /**
     * Schedules the traitors notifications. To be executed when the game starts.
     */
    private void scheduleTraitorsNotification()
    {
        Integer notifyAfter = Config.TRAITORS.NOTIFY_AFTER.get();
        if (notifyAfter < 1) notifyAfter = 1;

        RunTask.timer(new TraitorsNotificationTask(), notifyAfter * 60 * 20l, 20l);
    }

    /**
     * Setups the traitors team. To be executed before any reveal (e.g. when the game starts).
     */
    private void setupScoreboard()
    {
        final Scoreboard sb = UHCReloaded.get().getScoreboardManager().getScoreboard();
        final Random rand = new Random();

        traitorsTeamInternalName = String.valueOf(rand.nextInt(99999999)) + String.valueOf(rand.nextInt(99999999));

        final Team team = sb.registerNewTeam(traitorsTeamInternalName);

        team.setDisplayName(I.t("Traitors"));
        team.setPrefix(ChatColor.DARK_RED.toString());
        team.setSuffix(ChatColor.RESET.toString());
    }

    /**
     * Schedules all the things.
     */
    @EventHandler (priority = EventPriority.MONITOR)
    public void onGameStarts(UHGameStartsEvent ev)
    {
        try
        {
            // TODO move after when tests done, to execute only if traitors are generated
            setupScoreboard();
            scheduleTraitorsNotification();

            generateTraitors();
            PluginLogger.info("{0} traitors generated.", traitors.size());
        }
        catch (IllegalArgumentException e)
        {
            PluginLogger.error("Unable to generate traitors. " + e.getMessage());
        }
    }

    /**
     * Ensures revealed traitors are in the good scoreboard team and with the good color.
     */
    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent ev)
    {
        final Traitor traitor = getTraitor(ev.getPlayer().getUniqueId());
        if (traitor != null && areTraitorsNotified() && traitor.isRevealed())
        {
            RunTask.later(new Runnable() {
                @Override
                public void run()
                {
                    addRevealedTraitorAttributes(ev.getPlayer());
                }
            }, 10l);
        }
    }



    /* **  TRAITORS CHAT  ** */


    /**
     * Sends a message to the traitors chat
     *
     * @param nickname The nickname to use
     * @param message The message
     * @param team The team, to be displayed, or null to hide the team.
     * @param revealed {@code true} to mark the traitor as revealed in the tooltip,
     *                 {@code false} to mark it as hidden, {@code null} if this doesn't apply (e.g. external message).
     */
    public void sendToTraitorsChat(final String nickname, final String message, final UHTeam team, final Boolean revealed)
    {
        final ChatColor color = team != null ? team.getColor().toChatColor() : ChatColor.RED;
        final ItemStackBuilder tooltip = new ItemStackBuilder(Material.POTATO_ITEM)
                .title(color, ChatColor.BOLD + nickname);

        if (team != null) tooltip.lore(I.t("{gray}Team: {0}", team.getDisplayName()));
        if (revealed != null)
        {
            if (revealed) tooltip.lore(I.t("{gray}Traitor identity revealed"));
            else          tooltip.lore(I.t("{gray}Traitor identity hidden"));
        }

        RawText chatMessage = new RawText("")
                .then("[").color(ChatColor.GOLD)
                .then(nickname)
                    .color(color)
                    .hover(tooltip.item())
                .then(" -> ").color(ChatColor.GOLD)
                .then(I.t("traitors")).color(ChatColor.RED)
                .then("] ").color(ChatColor.GOLD)
                .then(message)
                .build();

        for (Traitor traitor : traitors.values())
        {
            MessageSender.sendChatMessage(traitor.getUniqueId(), chatMessage);
        }

        Bukkit.getConsoleSender().sendMessage(chatMessage.toFormattedText());
    }



    /* **  TRAITORS TASKS  ** */


    /**
     * The task sending the countdown and then notifying the players about their traitor status.
     */
    private class TraitorsNotificationTask extends BukkitRunnable
    {
        private int step = 10;

        @Override
        public void run()
        {
            if (step > 0)
            {
                ChatColor color = step > 5 ? ChatColor.GREEN : step > 3 ? ChatColor.YELLOW : ChatColor.RED;
                Titles.broadcastTitle(0, 30, 0, color + String.valueOf(step), "");

                if (step == 10 || step <= 5)
                {
                    Bukkit.broadcastMessage(I.tn("{yellow}Traitors will be notified in {gold}{0}{yellow} second", "{yellow}Traitors will be notified in {gold}{0}{yellow} seconds", step));
                }

                if (step <= 5)
                {
                    new UHSound(Sound.BLOCK_NOTE_HAT, 1.2f, 0.6f).broadcast();
                }
            }
            else
            {
                try
                {
                    Titles.broadcastTitle(0, 60, 20, ChatColor.RED + "0", "");

                    new UHSound(Sound.ENTITY_HORSE_SADDLE, 2f, 0.65f).broadcast();


                    // Chat notifications

                    for (Player player : Bukkit.getOnlinePlayers())
                    {
                        final Traitor traitor = getTraitor(player.getUniqueId());

                        player.sendMessage("");
                        UHCRTraitors.get().separator(player);

                        if (traitor != null)
                        {
                            MessageSender.sendActionBarMessage(player, I.t("{red}YOU ARE A TRAITOR. {yellow}Open chat for details."));

                            I.sendT(player, "{red}{bold}You are a traitor.");
                            I.sendT(player, "{yellow}{bold}Your objective is to win with the other traitors.");
                            I.sendT(player, "{yellow}You are now against your team, but they don't know this! Use the secret to your advantage.");

                            player.sendMessage("");

                            I.sendT(player, "{yellow}The traitors only know themselves through a fake name.");
                            I.sendT(player, "{yellow}Your traitor name is {gold}{0}{yellow}.", traitor.getFakeName());
                        }
                        else
                        {
                            I.sendT(player, "{darkgreen}{bold}You are not a traitor.");
                            I.sendT(player, "{green}Your objective is to win with your team. But beware, it's not the case of all your teammates...");
                        }

                        UHCRTraitors.get().separator(player);
                        player.sendMessage("");
                    }


                    // Public ProTips

                    RunTask.later(new Runnable() {
                        @Override
                        public void run()
                        {
                            TraitorsProTips.AM_I_A_TRAIOR.broadcast();
                        }
                    }, 60 * 20l);


                    // Traitors ProTips

                    RunTask.later(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for (Traitor traitor : traitors.values())
                            {
                                TraitorsProTips.TRAITORS_CHAT.sendTo(traitor.getUniqueId());
                            }
                        }
                    }, 15 * 20l);

                    RunTask.later(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            for (Traitor traitor : traitors.values())
                            {
                                TraitorsProTips.TRAITORS_REVEAL.sendTo(traitor.getUniqueId());
                            }
                        }
                    }, 60 * 5 * 20l);
                }
                finally
                {
                    traitorsNotified = true;
                    cancel();
                }
            }

            step--;
        }
    }
}
