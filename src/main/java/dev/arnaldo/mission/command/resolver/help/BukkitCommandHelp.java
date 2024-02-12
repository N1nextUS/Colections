package dev.arnaldo.mission.command.resolver.help;

import org.jetbrains.annotations.Range;
import revxrsal.commands.exception.InvalidHelpPageException;
import revxrsal.commands.help.CommandHelp;

import java.util.ArrayList;
import java.util.Collection;

public class BukkitCommandHelp<E> extends ArrayList<E> implements CommandHelp<E> {

    public BukkitCommandHelp() {
        super();
    }

    public BukkitCommandHelp(Collection<E> collection) {
        super(collection);
    }

    @Override
    public CommandHelp<E> paginate(int page, int elements) throws InvalidHelpPageException {
        BukkitCommandHelp<E> list = new BukkitCommandHelp<E>();
        if (this.isEmpty()) return list;

        int size = this.getPageSize(elements);
        if (page > size) {
            throw new InvalidHelpPageException(this, page, elements);
        }

        int pages = Math.min(page * elements, this.size());
        for (int i = (page - 1) * elements; i < pages; ++i) {
            list.add(this.get(i));
        }

        return list;
    }

    @Override
    @Range(from = 1L, to = Integer.MAX_VALUE)
    public int getPageSize(int elements) {
        if (elements < 1) {
            throw new IllegalArgumentException("Elements per page cannot be less than 1! (Found " + elements + ")");
        } else {
            return this.size() / elements + (this.size() % elements == 0 ? 0 : 1);
        }
    }

}