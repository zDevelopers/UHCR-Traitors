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
package eu.carrade.amaury.UHCRTraitors.commands.traitors;

import eu.carrade.amaury.UHCRTraitors.Config;
import eu.carrade.amaury.UHCRTraitors.Permissions;
import eu.carrade.amaury.UHCRTraitors.UHCRTraitors;
import eu.carrade.amaury.UHCRTraitors.traitors.Traitor;
import eu.carrade.amaury.UHCReloaded.UHCReloaded;
import eu.carrade.amaury.UHCReloaded.teams.UHTeam;
import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zlib.components.rawtext.RawText;
import fr.zcraft.zlib.components.rawtext.RawTextPart;
import fr.zcraft.zlib.tools.items.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


@CommandInfo (name = "list")
public class TraitorsListCommand extends Command
{
    @Override
    protected void run() throws CommandException
    {
        final boolean showNames = args.length >0 && args[0].equalsIgnoreCase("full");

        if (showNames && !Permissions.LIST_FULL.grantedTo(sender))
            throwNotAuthorized();

        if (!UHCRTraitors.get().getTraitorsManager().areTraitorsNotified())
            error(I.t("Traitors are not public yet!"));


        final List<Traitor> traitors = UHCRTraitors.get().getTraitorsManager().getTraitors();


        UHCRTraitors.get().separator(sender);

        final RawTextPart title = new RawText("")
                .then(I.tn("{0} traitor", "{0} traitors", traitors.size()))
                .color(ChatColor.RED)
                .style(ChatColor.BOLD);

        if (sender instanceof Player && !showNames && Permissions.LIST_FULL.grantedTo(sender))
        {
            title
                    .then(" ")
                    .then(I.t("(display identities)"))
                        .color(ChatColor.GRAY)
                        .hover(new ItemStackBuilder(Material.POTATO_ITEM)
                                        .title(I.t("{red}{bold}Displays the traitors identities"))
                                        .lore(I.t("{darkred}Warning! {red}This can be a game breaker!"))
                                        .item()
                        )
                        .command(TraitorsListCommand.class, "full");
        }

        send(title.build());


        final boolean displayTeams = showNames ? true : Config.TRAITORS.DISPLAY_TEAM.get();

        for (final Traitor traitor : traitors)
        {
            final OfflinePlayer player = Bukkit.getOfflinePlayer(traitor.getUniqueId());
            final String displayName = traitor.isRevealed() ? player.getName() : traitor.getFakeName();
            final UHTeam team = displayTeams ? UHCReloaded.get().getTeamManager().getTeamForPlayer(player) : null;

            final RawText desc = new RawText("");
            desc.then("- ").color(ChatColor.GRAY);
            desc.then(displayName).color(team != null ? team.getColor().toChatColor() : ChatColor.GOLD);

            if (team != null)
            {
                desc
                    .then("(").color(ChatColor.GRAY)
                    .then(team.getDisplayName())
                        .hover(new ItemStackBuilder(Material.POTATO_ITEM)
                                        .title(I.t("{white}Team {0}", team.getDisplayName()))
                                        .lore(I.tn("{gray}{0} player", "{gray}{0} players", team.getSize()))
                                        .item()
                        )
                    .then(") ").color(ChatColor.GRAY);
            }

            if (traitor.isRevealed())
            {
                desc.then(I.t("(was {0})", traitor.getFakeName())).color(ChatColor.GRAY);
            }
            else if (showNames)
            {
                desc.then(I.t("(name: {0})", player.getName())).color(ChatColor.GRAY);
            }

            if (UHCReloaded.get().getGameManager().isPlayerDead(traitor.getUniqueId()))
            {
                desc.then(I.t("{gray}({darkred}DEAD{gray})"));
            }

            send(desc);
        }


        UHCRTraitors.get().separator(sender);
    }

    @Override
    public boolean canExecute(CommandSender sender)
    {
        return Permissions.LIST.grantedTo(sender);
    }
}
