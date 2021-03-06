package com.android.launcher3.logging;

import android.view.View;
import android.view.ViewParent;

import androidx.annotation.Nullable;

import com.android.launcher3.model.data.ItemInfo;
import com.android.launcher3.userevent.nano.LauncherLogProto.Target;

import java.util.ArrayList;

public class StatsLogUtils {
    // Defined in android.stats.launcher.nano
    // As they cannot be linked in this file, defining again.
    public final static int LAUNCHER_STATE_BACKGROUND = 0;
    public final static int LAUNCHER_STATE_HOME = 1;
    public final static int LAUNCHER_STATE_OVERVIEW = 2;
    public final static int LAUNCHER_STATE_ALLAPPS = 3;
    private final static int MAXIMUM_VIEW_HIERARCHY_LEVEL = 5;

    public interface LogStateProvider {
        int getCurrentState();
    }

    /**
     * Implemented by containers to provide a container source for a given child.
     */
    public interface LogContainerProvider {

        /**
         * Populates parent container targets for an item
         */
        void fillInLogContainerData(ItemInfo childInfo, Target child, ArrayList<Target> parents);
    }

    /**
     * Recursively finds the parent of the given child which implements IconLogInfoProvider
     */
    public static LogContainerProvider getLaunchProviderRecursive(@Nullable View v) {
        ViewParent parent;
        if (v != null) {
            parent = v.getParent();
        } else {
            return null;
        }

        // Optimization to only check up to 5 parents.
        int count = MAXIMUM_VIEW_HIERARCHY_LEVEL;
        while (parent != null && count-- > 0) {
            if (parent instanceof LogContainerProvider) {
                return (LogContainerProvider) parent;
            } else {
                parent = parent.getParent();
            }
        }
        return null;
    }
}
