package eu.carrade.amaury.UHCRTraitors;

import eu.carrade.amaury.UHCRTraitors.commands.traitors.TraitorsChatCommand;
import eu.carrade.amaury.UHCRTraitors.commands.traitors.TraitorsCountCommand;
import eu.carrade.amaury.UHCRTraitors.commands.traitors.TraitorsRevealCommand;
import eu.carrade.amaury.UHCRTraitors.dependencies.UHCReloadedDependency;
import fr.zcraft.zlib.components.commands.Commands;
import fr.zcraft.zlib.core.ZPlugin;
import fr.zcraft.zlib.tools.PluginLogger;


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

        loadComponents(Config.class, Commands.class);

        traitorsManager = loadComponent(TraitorsManager.class);

        Commands.register("traitors", TraitorsCountCommand.class, TraitorsChatCommand.class, TraitorsRevealCommand.class);
        Commands.registerShortcut("traitors", TraitorsChatCommand.class, "f");
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
}
