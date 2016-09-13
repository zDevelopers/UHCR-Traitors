package eu.carrade.amaury.UHCRTraitors.commands.traitors;

import eu.carrade.amaury.UHCRTraitors.Permissions;
import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import org.bukkit.command.CommandSender;


@CommandInfo (name = "chat", usageParameters = "")
public final class TraitorsChatCommand extends Command
{
    @Override
    protected void run() throws CommandException
    {
        // TODO implement command /traitors chat
    }

    @Override
    public boolean canExecute(CommandSender sender)
    {
        return Permissions.CHAT.grantedTo(sender);
    }
}
