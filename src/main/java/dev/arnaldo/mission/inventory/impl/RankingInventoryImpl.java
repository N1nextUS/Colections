package dev.arnaldo.mission.inventory.impl;

import br.com.blecaute.inventory.InventoryBuilder;
import br.com.blecaute.inventory.button.Button;
import br.com.blecaute.inventory.button.ButtonType;
import br.com.blecaute.inventory.configuration.InventoryConfiguration;
import br.com.blecaute.inventory.configuration.PaginatedConfiguration;
import dev.arnaldo.mission.Main;
import dev.arnaldo.mission.inventory.Inventory;
import dev.arnaldo.mission.inventory.InventoryType;
import dev.arnaldo.mission.manager.UserManager;
import dev.arnaldo.mission.model.User;
import dev.arnaldo.mission.model.UserRanking;
import dev.arnaldo.mission.util.item.ItemUtil;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RankingInventoryImpl implements Inventory {

    private UserManager userManager;

    private InventoryConfiguration inventoryConfiguration;
    private PaginatedConfiguration paginatedConfiguration;

    private Pair<Integer, ItemStack> menuItem;
    private Pair<Integer, ItemStack> nextItem;
    private Pair<Integer, ItemStack> backItem;

    @Override
    public void open(@NotNull Player player, @NotNull User user) {
        new InventoryBuilder<UserRanking>(inventoryConfiguration)
                .withButton(Button.of(ButtonType.NEXT_PAGE, nextItem.getKey(), nextItem.getValue()))
                .withButton(Button.of(ButtonType.PREVIOUS_PAGE, backItem.getKey(), backItem.getValue()))
                .withObjects(paginatedConfiguration, userManager.findRankings(), click -> {})
                .withItem(menuItem.getKey(), menuItem.getValue(), click ->
                        InventoryType.COLLECT.getInventory().open(player, user))
                .build(player);
    }

    @Override
    public void load(@NotNull Main main) {
        ConfigurationSection section = main.getConfig().getConfigurationSection("ranking-inventory");

        ConfigurationSection menuItemSection = section.getConfigurationSection("menu-item");
        ConfigurationSection previousItemSection = section.getConfigurationSection("previous-item");
        ConfigurationSection nextItemSection = section.getConfigurationSection("next-item");

        this.inventoryConfiguration = InventoryConfiguration.builder(section.getString("title"), 5).build();
        this.paginatedConfiguration = PaginatedConfiguration.builder("#ranking")
                .start(11).end(25).size(10)
                .validator(slot -> slot > 15 && slot < 20)
                .button(Button.of(ButtonType.PREVIOUS_PAGE, previousItemSection.getInt("slot"), ItemUtil.getItem(previousItemSection)))
                .button(Button.of(ButtonType.NEXT_PAGE, nextItemSection.getInt("slot"), ItemUtil.getItem(nextItemSection)))
                .build();

        this.menuItem = Pair.of(menuItemSection.getInt("slot"), ItemUtil.getItem(menuItemSection));
    }

}
