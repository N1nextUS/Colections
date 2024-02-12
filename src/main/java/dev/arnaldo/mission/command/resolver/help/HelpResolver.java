package dev.arnaldo.mission.command.resolver.help;

import dev.arnaldo.mission.command.annotation.HelpPosition;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.CommandHandler;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.command.CommandCategory;
import revxrsal.commands.command.ExecutableCommand;
import revxrsal.commands.core.CommandPath;
import revxrsal.commands.help.CommandHelp;
import revxrsal.commands.help.CommandHelpWriter;
import revxrsal.commands.process.ContextResolver;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public class HelpResolver implements ContextResolver<CommandHelp> {

    @Override
    public CommandHelp<?> resolve(@NotNull ContextResolverContext context) {
        CommandHandler handler = Objects.requireNonNull(context.commandHandler(), "No help writer is registered!");
        CommandHelpWriter<?> writer = handler.getHelpWriter();
        ExecutableCommand command = context.command();
        CommandCategory parent = command.getParent();

        CommandPath path = parent == null ? null : parent.getPath();
        Set<ExecutableCommand> entries = new HashSet<>();
        CommandActor actor = context.actor();

        if (parent != null && parent.getDefaultAction() != null) {
            entries.add(parent.getDefaultAction());
        }

        for (ExecutableCommand children : handler.getCommands().values()) {
            if (path == null || command == children || path.isParentOf(children.getPath()) ) {
                if (actor instanceof BukkitCommandActor) {
                    BukkitCommandActor bukkitActor = (BukkitCommandActor) actor;
                    CommandPermission annotation = children.getAnnotation(CommandPermission.class);
                    if (annotation != null) {
                        Permission permission = new Permission(annotation.value(), annotation.defaultAccess());
                        if (!bukkitActor.getSender().hasPermission(permission)) continue;
                    }
                }

                entries.add(children);
            }
        }

        List<Object> values = entries.stream()
                .sorted(Comparator.comparingInt(value -> {
                    HelpPosition position = value.getAnnotation(HelpPosition.class);
                    return position == null ? 0 : position.value();
                }))
                .map(value -> writer.generate(value, actor))
                .filter(Objects::nonNull).distinct()
                .collect(Collectors.toList());

        return new BukkitCommandHelp<>(values);
    }

}