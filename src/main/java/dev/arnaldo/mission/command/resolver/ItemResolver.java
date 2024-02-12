package dev.arnaldo.mission.command.resolver;

import dev.arnaldo.mission.Main;
import dev.arnaldo.mission.model.Config;
import dev.arnaldo.mission.model.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import revxrsal.commands.command.CommandParameter;
import revxrsal.commands.exception.CommandErrorException;
import revxrsal.commands.process.ValueResolver;
import revxrsal.commands.process.ValueResolverFactory;

public class ItemResolver implements ValueResolverFactory {

    @Override
    public @Nullable ValueResolver<?> create(@NotNull CommandParameter parameter) {
        if (!Item.class.isAssignableFrom(parameter.getType())) return null;
        return context -> Main.getInstance().getItemManager()
                .find(context.pop())
                .orElseThrow(() -> new CommandErrorException(Config.INVALID_ITEM_MESSAGE.asString()));
    }


}
