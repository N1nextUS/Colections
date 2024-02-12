package dev.arnaldo.mission.repository;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Repository<K, V> {

    void createTable();

    Optional<V> findById(@NotNull K k);

    @NotNull
    Set<V> findAll();

    void save(@NotNull V v);

    void save(@NotNull List<V> v);

    void delete(@NotNull V v);

    void deleteById(@NotNull K k);

}