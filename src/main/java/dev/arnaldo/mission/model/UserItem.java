package dev.arnaldo.mission.model;

import br.com.blecaute.inventory.property.InventoryProperty;
import br.com.blecaute.inventory.type.InventoryItem;
import dev.arnaldo.mission.manager.ItemManager;
import dev.arnaldo.mission.util.StringUtil;
import dev.arnaldo.mission.util.item.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserItem implements InventoryItem {

    private final String id;
    private Instant collectedAt = Instant.now();
    private transient boolean dirty = false;

    public void setCollectedAt(@NonNull Instant collectedAt) {
        this.collectedAt = collectedAt;
        this.dirty = true;
    }

    @Override
    public @Nullable ItemStack getItem(@NotNull Inventory inventory, @NotNull InventoryProperty property) {
        ItemManager manager = Objects.requireNonNull(property.get("itemManager"));

        Item item = manager.find(this.id).orElse(null);
        if (item == null) return null;

        String lore = StringUtil.replaceUserItem(this, Config.ITEM_COLLECTED_MESSAGE.asString())[0];
        return new ItemBuilder(item.getBaseItem().clone()).addLoreLine(lore).toItemStack();
    }

}