package dev.arnaldo.mission.command.adapter;

import org.jetbrains.annotations.NotNull;
import revxrsal.commands.bukkit.exception.BukkitExceptionAdapter;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.MissingArgumentException;

public class CustomExceptionAdapter extends BukkitExceptionAdapter {

    @Override
    public void missingArgument(@NotNull CommandActor actor, @NotNull MissingArgumentException exception) {
        actor.errorLocalized("missing-argument", exception.getParameter().getName(), exception.getCommand().getUsage());
    }

}