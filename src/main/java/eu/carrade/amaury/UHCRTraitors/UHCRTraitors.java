package eu.carrade.amaury.UHCRTraitors;

import fr.zcraft.zlib.core.ZPlugin;
import fr.zcraft.zlib.components.commands.Commands;
import eu.carrade.amaury.UHCRTraitors.listeners.GameListener;
import eu.carrade.amaury.UHCRTraitors.commands.traitors.TraitorsCountCommand;
import eu.carrade.amaury.UHCRTraitors.commands.traitors.TraitorsChatCommand;
import eu.carrade.amaury.UHCRTraitors.commands.traitors.TraitorsRevealCommand;


public final class UHCRTraitors extends ZPlugin
{
    private static UHCRTraitors instance;

    @Override
    public void onEnable()
    {
        instance = this;

        loadComponents(Commands.class, GameListener.class);
        
        Commands.register("traitors", TraitorsCountCommand.class, TraitorsChatCommand.class, TraitorsRevealCommand.class);

    }

    public static UHCRTraitors get()
    {
        return instance;
    }
}
