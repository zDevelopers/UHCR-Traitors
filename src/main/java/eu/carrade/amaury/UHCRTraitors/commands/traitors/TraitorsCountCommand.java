package eu.carrade.amaury.UHCRTraitors.commands.traitors;

import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;

import java.util.List;


@CommandInfo (name = "count", usageParameters = "")
public final class TraitorsCountCommand extends Command
{
    @Override
    protected void run() throws CommandException
    {
        // TODO implement command /traitors count
    }

    @Override
    protected List<String> complete() throws CommandException
    {
        // TODO implement auto-completion for /traitors count
        return null;
    }
}
