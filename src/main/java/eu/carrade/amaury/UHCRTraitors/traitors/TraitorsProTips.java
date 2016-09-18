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
package eu.carrade.amaury.UHCRTraitors.traitors;

import eu.carrade.amaury.UHCReloaded.protips.ProTip;
import fr.zcraft.zlib.components.i18n.I;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;


public enum TraitorsProTips
{
    TRAITORS_CHAT(new ProTip(true, I.t("{gray}Speak anonymously with the other traitors using {cc}/f <message>{gray}."))),
    TRAITORS_REVEAL(new ProTip(true, I.t("{gray}You can tell the whole server you're a traitor using {cc}/reveal{gray}. Beware of the consequences!"))),
    AM_I_A_TRAIOR(new ProTip(true, I.t("{gray}If someone wasn't online when the traitors were notified, you can tell them to use {cc}/amiatraitor{gray}.")))

    ;

    private final ProTip proTip;

    TraitorsProTips(ProTip proTip)
    {
        this.proTip = proTip;
    }

    public ProTip get()
    {
        return proTip;
    }


    /**
     * Sends this ProTip, if it wasn't sent before to this player.
     *
     * @param player The receiver of this ProTip.
     */
    public void sendTo(Player player)
    {
        proTip.sendTo(player);
    }

    /**
     * Sends this ProTip, if it wasn't sent before to this player and this player is online.
     *
     * @param id The receiver of this ProTip.
     */
    public void sendTo(UUID id)
    {
        proTip.sendTo(id);
    }

    public void broadcast()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            proTip.sendTo(player);
        }
    }
}
