package eu.carrade.amaury.UHCRTraitors.commands.traitors;

import eu.carrade.amaury.UHCRTraitors.Permissions;
import eu.carrade.amaury.UHCRTraitors.UHCRTraitors;
import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import org.bukkit.command.CommandSender;


@CommandInfo (name = "count", usageParameters = "[new count]")
public final class TraitorsCountCommand extends Command
{
    @Override
    protected void run() throws CommandException
    {
        if (args.length == 0)
        {
            final int traitorsCount = UHCRTraitors.get().getTraitorsManager().getTraitorsCount();
            success(I.t("{green}Current traitors count: {darkgreen}{0}{green}.", traitorsCount <= 0 ? I.t("teams count") : traitorsCount));
        }
        else
        {
            if (!Permissions.COUNT.grantedTo(sender)) throwNotAuthorized();

            try
            {
                UHCRTraitors.get().getTraitorsManager().setTraitorsCount(Integer.parseInt(args[0]));

                final int traitorsCount = UHCRTraitors.get().getTraitorsManager().getTraitorsCount();
                success(I.t("{green}Traitors count updated to {darkgreen}{0}{green}.", traitorsCount <= 0 ? I.t("the teams count") : traitorsCount));
            }
            catch (NumberFormatException e)
            {
                error(I.t("{0} is not a valid number.", args[0]));
            }
            catch (IllegalStateException e)
            {
                error(I.t("You cannot update the traitors count as the game is started and traitors are already generated."));
            }
        }
    }

    @Override
    public boolean canExecute(CommandSender sender)
    {
        return Permissions.COUNT.grantedTo(sender);
    }
}
