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

import eu.carrade.amaury.UHCRTraitors.UHCRTraitors;
import fr.zcraft.zlib.components.i18n.I;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;


public class Traitor
{
    private final UUID traitorID;
    private final String fakeName;

    private boolean isRevealed = false;

    public Traitor(UUID traitorID)
    {
        this.traitorID = traitorID;
        this.fakeName = UHCRTraitors.get().getTraitorsManager().getFakeName();
    }

    public UUID getUniqueId()
    {
        return traitorID;
    }

    public Player getPlayer()
    {
        return Bukkit.getPlayer(traitorID);
    }

    public String getFakeName()
    {
        return fakeName;
    }

    public boolean isRevealed()
    {
        return isRevealed;
    }

    public void reveal()
    {
       if (!isRevealed)
       {
           isRevealed = true;

           final Player player = getPlayer();
           if (player != null)
           {
               UHCRTraitors.get().getTraitorsManager().addRevealedTraitorAttributes(player);
           }

           Bukkit.broadcastMessage("");
           UHCRTraitors.get().separator();

           Bukkit.broadcastMessage(I.t("{red}{bold}{0} is a traitor!", Bukkit.getOfflinePlayer(traitorID).getName()));

           UHCRTraitors.get().separator();
           Bukkit.broadcastMessage("");
       }
    }
}
