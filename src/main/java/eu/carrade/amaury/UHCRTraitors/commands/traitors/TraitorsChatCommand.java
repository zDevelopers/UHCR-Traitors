package eu.carrade.amaury.UHCRTraitors.commands.traitors;

import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;

import java.util.List;


@CommandInfo (name = "chat", usageParameters = "")
public final class TraitorsChatCommand extends Command
{
    @Override
    protected void run() throws CommandException
    {
        // TODO implement command /traitors chat
    }

    @Override
    protected List<String> complete() throws CommandException
    {
        // TODO implement auto-completion for /traitors chat
        return null;
    }
}
