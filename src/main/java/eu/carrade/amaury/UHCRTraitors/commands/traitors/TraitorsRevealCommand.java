package eu.carrade.amaury.UHCRTraitors.commands.traitors;

import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;

import java.util.List;


@CommandInfo (name = "reveal", usageParameters = "")
public final class TraitorsRevealCommand extends Command
{
    @Override
    protected void run() throws CommandException
    {
        // TODO implement command /traitors reveal
    }

    @Override
    protected List<String> complete() throws CommandException
    {
        // TODO implement auto-completion for /traitors reveal
        return null;
    }
}
