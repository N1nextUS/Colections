package dev.arnaldo.mission.task;

import dev.arnaldo.mission.Main;
import dev.arnaldo.mission.manager.UserManager;

public class AutoSaveTask implements Runnable {

    @Override
    public void run() {
        UserManager userManager = Main.getInstance().getUserManager();

        userManager.save();
        userManager.updateRanking();
    }

}