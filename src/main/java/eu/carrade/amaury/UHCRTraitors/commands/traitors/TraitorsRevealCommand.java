package eu.carrade.amaury.UHCRTraitors.commands.traitors;

import eu.carrade.amaury.UHCRTraitors.Permissions;
import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import org.bukkit.command.CommandSender;


@CommandInfo (name = "reveal", usageParameters = "")
public final class TraitorsRevealCommand extends Command
{
    @Override
    protected void run() throws CommandException
    {
        // TODO implement command /traitors reveal
    }

    @Override
    public boolean canExecute(CommandSender sender)
    {
        return Permissions.REVEAL.grantedTo(sender);
    }
}
