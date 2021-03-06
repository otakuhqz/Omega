/*
 *     Copyright (C) 2019 Lawnchair Team.
 *
 *     This file is part of Lawnchair Launcher.
 *
 *     Lawnchair Launcher is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Lawnchair Launcher is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Lawnchair Launcher.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.saggitt.omega.preferences

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import androidx.preference.Preference
import com.android.launcher3.BuildConfig
import com.android.launcher3.Launcher
import com.android.launcher3.LauncherAppState
import com.android.launcher3.util.ActivityTracker
import com.android.launcher3.widget.WidgetsFullSheet

class SmartspaceAddToHomePreference(context: Context, attrs: AttributeSet?) :
        Preference(context, attrs), ResumablePreference,
        ControlledPreference by ControlledPreference.Delegate(context, attrs) {

    override fun onResume() {
        isVisible = hasSmartspaceWidget(context) != true
    }

    override fun onClick() {
        val homeIntent =
                Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_HOME)
                        .setPackage(BuildConfig.APPLICATION_ID)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        openWidgetsActivityTracker.addToIntent(homeIntent)

        context.startActivity(homeIntent)
    }

    companion object {
        private val openWidgetsActivityTracker = ActivityTracker.SchedulerCallback<Launcher> { activity, _ ->
            WidgetsFullSheet.show(activity, true)
            true
        }

        fun hasSmartspaceWidget(context: Context): Boolean? {
            return LauncherAppState.getInstance(context).model.loadedWidgets?.any { it.isCustomWidget }
        }

        @JvmStatic
        fun showSmartspaceInPreview(context: Context): Boolean {
            return hasSmartspaceWidget(context) != false
        }
    }
}
