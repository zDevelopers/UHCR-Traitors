package eu.carrade.amaury.UHCRTraitors.commands.traitors;

import eu.carrade.amaury.UHCRTraitors.Permissions;
import eu.carrade.amaury.UHCRTraitors.traitors.Traitor;
import eu.carrade.amaury.UHCRTraitors.UHCRTraitors;
import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zlib.components.rawtext.RawText;
import fr.zcraft.zlib.tools.text.MessageSender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


@CommandInfo (name = "reveal", usageParameters = "[confirm]")
public final class TraitorsRevealCommand extends Command
{
    @Override
    protected void run() throws CommandException
    {
        final Traitor traitor = UHCRTraitors.get().getTraitorsManager().getTraitor(playerSender().getUniqueId());

        if (traitor == null)
            error(I.t("You are not a traitor."));

        else if (traitor.isRevealed())
            error(I.t("You already revealed yourself as a traitor."));


        if (args.length == 0 || !args[0].equalsIgnoreCase("confirm"))
        {
            UHCRTraitors.get().separator(playerSender());

            I.sendT(playerSender(), "{darkred}{bold}Warning! {red}Revealing yourself as a traitor will have the following consequences:");
            I.sendT(playerSender(), "{gray}- {red}all players will be notified you're a traitor;");
            I.sendT(playerSender(), "{gray}- {red}your nickname color will turn to red;");
            I.sendT(playerSender(), "{gray}- {red}you'll be {0} instead of {1} in the traitors chat.", playerSender().getName(), traitor.getFakeName());

            playerSender().sendMessage("");

            RawText confirm = new RawText("» ").color(ChatColor.GRAY)
                    .then(I.t("Click here to confirm"))
                        .color(ChatColor.GOLD)
                        .command(TraitorsRevealCommand.class, "confirm")
                        .hover(new RawText(I.t("Click here to reveal yourself as a traitor")).color(ChatColor.WHITE))
                    .then(" «").color(ChatColor.GRAY)
                    .build();

            MessageSender.sendSystemMessage(playerSender(), confirm);

            UHCRTraitors.get().separator(playerSender());
        }
        else
        {
            traitor.reveal();
        }
    }

    @Override
    public boolean canExecute(CommandSender sender)
    {
        return Permissions.REVEAL.grantedTo(sender);
    }
}
