package dev.arnaldo.mission.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class User {

    @NonNull
    private final String name;

    @NonNull
    private Map<String, UserItem> items = new HashMap<>();

    public User(@NonNull String name, @NonNull Map<String, UserItem> items) {
        this.name = name;
        this.items = items;
    }

    public boolean hasItem(@NonNull String id) {
        return this.items.containsKey(id);
    }

    public void addItem(@NonNull UserItem item) {
        this.items.put(item.getId(), item);
    }

    public void removeItem(@NonNull String id) {
        this.items.remove(id);
    }

    public void setItems(@NonNull List<UserItem> items) {
        this.items = items.stream().collect(Collectors.toMap(UserItem::getId, Function.identity()));
    }

    public boolean isDirty() {
        return this.items.values().stream().anyMatch(UserItem::isDirty);
    }

    public void setDirty(boolean value) {
        this.items.values().forEach(item -> item.setDirty(value));
    }

    @Unmodifiable
    public Collection<UserItem> getItems() {
        return Collections.unmodifiableCollection(items.values());
    }

}
