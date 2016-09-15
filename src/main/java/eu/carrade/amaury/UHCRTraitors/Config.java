package eu.carrade.amaury.UHCRTraitors;

import fr.zcraft.zlib.components.configuration.Configuration;
import fr.zcraft.zlib.components.configuration.ConfigurationItem;
import fr.zcraft.zlib.components.configuration.ConfigurationList;
import fr.zcraft.zlib.components.configuration.ConfigurationSection;

import static fr.zcraft.zlib.components.configuration.ConfigurationItem.section;


public class Config extends Configuration
{
    static public final TraitorsSection TRAITORS = section("traitors", TraitorsSection.class);
    static public class TraitorsSection extends ConfigurationSection
    {
        public final ConfigurationItem<Integer> COUNT = item("count", 0);
        public final ConfigurationItem<Integer> NOTIFY_AFTER = item("notifyAfter", 30);
        public final ConfigurationList<String> FAKE_NAMES = list("fakeNames", String.class);
        public final ConfigurationItem<Boolean> DISPLAY_TEAM = item("displayTeam", true);
    }
}
