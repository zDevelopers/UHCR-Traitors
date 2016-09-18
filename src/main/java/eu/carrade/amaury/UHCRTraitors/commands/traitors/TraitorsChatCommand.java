package eu.carrade.amaury.UHCRTraitors.commands.traitors;

import eu.carrade.amaury.UHCRTraitors.Config;
import eu.carrade.amaury.UHCRTraitors.Permissions;
import eu.carrade.amaury.UHCRTraitors.traitors.Traitor;
import eu.carrade.amaury.UHCRTraitors.UHCRTraitors;
import eu.carrade.amaury.UHCReloaded.UHCReloaded;
import eu.carrade.amaury.UHCReloaded.teams.UHTeam;
import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


@CommandInfo (name = "chat", usageParameters = "")
public final class TraitorsChatCommand extends Command
{
    @Override
    protected void run() throws CommandException
    {
        if (!UHCRTraitors.get().getTraitorsManager().areTraitorsNotified())
            error(I.t("You cannot use the traitors chat because traitors were not notified yet."));

        final Traitor traitor = UHCRTraitors.get().getTraitorsManager().getTraitor(playerSender().getUniqueId());
        if (traitor == null)
            error(I.t("You cannot use the traitors chat because you are not a traitor."));

        final StringBuilder b = new StringBuilder();
        for (String word : args) b.append(word).append(" ");

        final String nickname = traitor.isRevealed() ? playerSender().getDisplayName() : traitor.getFakeName();
        final String message = ChatColor.translateAlternateColorCodes('&', b.toString().trim());
        final UHTeam team = Config.TRAITORS.DISPLAY_TEAM.get() || traitor.isRevealed() ? UHCReloaded.get().getTeamManager().getTeamForPlayer(playerSender()) : null;

        UHCRTraitors.get().getTraitorsManager().sendToTraitorsChat(nickname, message, team, traitor.isRevealed());
    }

    @Override
    public boolean canExecute(CommandSender sender)
    {
        return Permissions.CHAT.grantedTo(sender);
    }
}
