package dev.arnaldo.mission.inventory.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.button.Button;
import br.com.blecaute.inventory.button.ButtonType;
import br.com.blecaute.inventory.configuration.InventoryConfiguration;
import br.com.blecaute.inventory.configuration.PaginatedConfiguration;
import dev.arnaldo.mission.Main;
import dev.arnaldo.mission.inventory.Inventory;
import dev.arnaldo.mission.inventory.InventoryType;
import dev.arnaldo.mission.model.User;
import dev.arnaldo.mission.model.UserItem;
import dev.arnaldo.mission.util.item.ItemUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CollectInventoryImpl implements Inventory {

    private InventoryConfiguration inventoryConfiguration;
    private PaginatedConfiguration paginatedConfiguration;
    private Pair<Integer, ItemStack> rankingItem;

    @Override
    public void open(@NotNull Player player, @NotNull User user) {
        new InventoryBuilder<UserItem>(inventoryConfiguration)
                .withProperty("itemManager", Main.getInstance().getItemManager())
                .withObjects(paginatedConfiguration, user.getItems(), click -> {})
                .withItem(rankingItem.getKey(), rankingItem.getValue(), click -> InventoryType.RANKING.getInventory().open(player, user))
                .build(player);
    }

    @Override
    public void load(@NotNull Main main) {
        ConfigurationSection section = main.getConfig().getConfigurationSection("collect-inventory");

        ConfigurationSection rankingItemSection = section.getConfigurationSection("ranking-item");
        ConfigurationSection previousItemSection = section.getConfigurationSection("previous-item");
        ConfigurationSection nextItemSection = section.getConfigurationSection("next-item");

        this.inventoryConfiguration = InventoryConfiguration.builder(section.getString("title"), 6).build();
        this.paginatedConfiguration = PaginatedConfiguration.builder("#items")
                .start(11).end(34).size(15)
                .validator(slot -> (slot > 15 && slot < 20) || (slot > 24 && slot < 29))
                .button(Button.of(ButtonType.PREVIOUS_PAGE, previousItemSection.getInt("slot"), ItemUtil.getItem(previousItemSection)))
                .button(Button.of(ButtonType.NEXT_PAGE, nextItemSection.getInt("slot"), ItemUtil.getItem(nextItemSection)))
                .build();

        this.rankingItem = Pair.of(rankingItemSection.getInt("slot"), ItemUtil.getItem(rankingItemSection));
    }

}
