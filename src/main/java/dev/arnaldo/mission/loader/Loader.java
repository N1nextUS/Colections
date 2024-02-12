package dev.arnaldo.mission.loader;

import dev.arnaldo.mission.Main;
import lombok.NonNull;

public interface Loader {

    void load(@NonNull Main main);

}