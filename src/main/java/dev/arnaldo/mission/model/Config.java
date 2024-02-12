package dev.arnaldo.mission.model;

import lombok.*;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Config {

    PLAYER_NOT_FOUND_MESSAGE("messages.player-not-found"),

    INVALID_ITEM_MESSAGE("messages.invalid-item"),

    ITEM_ALREADY_COLLECTED_MESSAGE("messages.item-already-collected"),
    ITEM_COLLECTED_MESSAGE("messages.item-collected"),
    ITEM_SENT_MESSAGE("messages.item-sent"),
    ITEMS_REDEFINED_MESSAGE("messages.items-redefined"),
    QUEST_COMPLETED_MESSAGE("messages.quest-completed"),

    QUEST_AMOUNT_SETTING("quest-amount"),

    ITEM_COLLECTED_SETTING("items-placeholders.collected"),
    ITEM_COLLECT_SETTING("items-placeholders.collect"),

    USER_RANKING_NAME_SETTING("ranking-inventory.format.name"),
    USER_RANKING_LORE_SETTING("ranking-inventory.format.lore"),

    ;

    private final String path;

    @Setter
    private Object object;

    public String asString() {
        return object.toString();
    }

    public int asInt() {
        return object instanceof Integer ? (int) object : (int) (object = Integer.parseInt(object.toString()));
    }

    public double asDouble() {
        return object instanceof Double ? (double) object : (double) (object = Double.parseDouble(object.toString()));
    }

    @SuppressWarnings("unchecked")
    public List<String> asList() {
        return (List<String>) object;
    }

}
