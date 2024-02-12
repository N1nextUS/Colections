package dev.arnaldo.mission.model;

import dev.arnaldo.mission.util.item.ItemBuilder;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@Data
public class Item {

    public static final String NBT_KEY = "sunrise.mission.nbt";

    @NonNull
    private String id;

    @NonNull
    private String name;

    @NonNull
    private ItemStack baseItem;

    @Nullable
    private ItemStack item;

    public Item(@NonNull String id, @NonNull String name, @NonNull ItemStack baseItem) {
        this.id = id;
        this.name = name;
        this.baseItem = baseItem;
    }

    public ItemStack getItem() {
        return item != null ? item : (item = new ItemBuilder(this.baseItem.clone())
                .addLoreLine(Config.ITEM_COLLECT_SETTING.asString())
                .toItemStack());
    }

}