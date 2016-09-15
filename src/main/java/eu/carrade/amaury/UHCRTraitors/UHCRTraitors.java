package eu.carrade.amaury.UHCRTraitors;

import eu.carrade.amaury.UHCRTraitors.commands.traitors.TraitorsChatCommand;
import eu.carrade.amaury.UHCRTraitors.commands.traitors.TraitorsCheckCommand;
import eu.carrade.amaury.UHCRTraitors.commands.traitors.TraitorsCountCommand;
import eu.carrade.amaury.UHCRTraitors.commands.traitors.TraitorsRevealCommand;
import eu.carrade.amaury.UHCRTraitors.dependencies.UHCReloadedDependency;
import fr.zcraft.zlib.components.commands.Commands;
import fr.zcraft.zlib.components.i18n.I18n;
import fr.zcraft.zlib.core.ZPlugin;
import fr.zcraft.zlib.tools.PluginLogger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public final class UHCRTraitors extends ZPlugin
{
    private static UHCRTraitors instance;

    private TraitorsManager traitorsManager;


    @Override
    public void onEnable()
    {
        instance = this;

        UHCReloadedDependency uhcReloadedDependency = loadComponent(UHCReloadedDependency.class);
        if (!uhcReloadedDependency.isEnabled())
        {
            PluginLogger.error("Cannot run without UHCReloaded ('UHPlugin') loaded. Aborting.");
            setEnabled(false);
            return;
        }

        saveDefaultConfig();

        loadComponents(Config.class, Commands.class, I18n.class);

        I18n.useDefaultPrimaryLocale();

        traitorsManager = loadComponent(TraitorsManager.class);

        Commands.register("traitors", TraitorsCountCommand.class, TraitorsChatCommand.class, TraitorsCheckCommand.class, TraitorsRevealCommand.class);
        Commands.registerShortcut("traitors", TraitorsChatCommand.class, "f");
        Commands.registerShortcut("traitors", TraitorsCheckCommand.class, "amiatraitor");
        Commands.registerShortcut("traitors", TraitorsRevealCommand.class, "reveal");
    }

    public static UHCRTraitors get()
    {
        return instance;
    }

    public TraitorsManager getTraitorsManager()
    {
        return traitorsManager;
    }

    public void separator(Player player)
    {
        final String separator = ChatColor.GRAY + "⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅ ⋅";

        if (player != null)
            player.sendMessage(separator);
        else
            Bukkit.broadcastMessage(separator);
    }

    public void separator()
    {
        separator(null);
    }
}
