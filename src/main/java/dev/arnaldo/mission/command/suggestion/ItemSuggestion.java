package dev.arnaldo.mission.command.suggestion;

import dev.arnaldo.mission.Main;
import dev.arnaldo.mission.model.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.autocomplete.SuggestionProviderFactory;
import revxrsal.commands.command.CommandParameter;

import java.util.stream.Collectors;

public class ItemSuggestion implements SuggestionProviderFactory {

    @Override
    public @Nullable SuggestionProvider createSuggestionProvider(@NotNull CommandParameter parameter) {
        if (!Item.class.isAssignableFrom(parameter.getType())) return null;
        return (args, sender, command) -> Main.getInstance().getItemManager().findAll().stream()
                .map(Item::getId)
                .collect(Collectors.toList());
    }


}
