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
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CollectInventoryImpl implements Inventory {

    private InventoryConfiguration inventoryConfiguration;
    private PaginatedConfiguration paginatedConfiguration;

    private Pair<Integer, ItemStack> topItem;
    private Pair<Integer, ItemStack> nextItem;
    private Pair<Integer, ItemStack> backItem;

    @Override
    public void open(@NotNull Player player, @NotNull User user) {
        new InventoryBuilder<UserItem>(inventoryConfiguration)
                .withProperty("itemManager", Main.getInstance().getItemManager())
                .withButton(Button.of(ButtonType.NEXT_PAGE, nextItem.getKey(), nextItem.getValue()))
                .withButton(Button.of(ButtonType.PREVIOUS_PAGE, backItem.getKey(), backItem.getValue()))
                .withObjects(paginatedConfiguration, user.getItems(), click -> {})
                .withItem(topItem.getKey(), topItem.getValue(), click ->
                        InventoryType.TOP.getInventory().open(player, user))
                .build(player);
    }

    @Override
    public void load(@NotNull Main main) {

    }

}
