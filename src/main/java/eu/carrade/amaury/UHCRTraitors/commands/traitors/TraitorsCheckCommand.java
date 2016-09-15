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

import eu.carrade.amaury.UHCRTraitors.Permissions;
import eu.carrade.amaury.UHCRTraitors.Traitor;
import eu.carrade.amaury.UHCRTraitors.UHCRTraitors;
import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import org.bukkit.command.CommandSender;


@CommandInfo (name = "check")
public class TraitorsCheckCommand extends Command
{
    @Override
    protected void run() throws CommandException
    {
        if (!UHCRTraitors.get().getTraitorsManager().areTraitorsNotified())
            error(I.t("Traitors are not revealed yet!"));

        Traitor traitor = UHCRTraitors.get().getTraitorsManager().getTraitor(playerSender().getUniqueId());

        UHCRTraitors.get().separator(playerSender());

        if (traitor != null)
        {
            I.sendT(playerSender(), "{red}{bold}You are a traitor.");
            I.sendT(playerSender(), "{yellow}{bold}Your objective is to win with the other traitors.");

            playerSender().sendMessage("");

            I.sendT(playerSender(), "{yellow}Your traitor name is {gold}{0}{yellow}.", traitor.getFakeName());
            I.sendT(playerSender(), "{yellow}Use {cc}/f <message>{yellow} to speak with the other traitors.");
        }
        else
        {
            I.sendT(playerSender(), "{darkgreen}{bold}You are not a traitor.");
            I.sendT(playerSender(), "{green}Your objective is to win with your team. But beware, it's not the case of all your teammates...");
        }

        UHCRTraitors.get().separator(playerSender());
    }

    @Override
    public boolean canExecute(CommandSender sender)
    {
        return Permissions.CHECK.grantedTo(sender);
    }
}
