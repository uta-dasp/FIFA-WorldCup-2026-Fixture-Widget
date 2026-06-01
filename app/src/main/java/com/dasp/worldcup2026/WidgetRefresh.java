package com.dasp.worldcup2026;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;

final class WidgetRefresh {
    private WidgetRefresh() {
    }

    static void updateAll(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);

        int[] scheduleIds = manager.getAppWidgetIds(new ComponentName(context, ScheduleWidgetProvider.class));
        manager.notifyAppWidgetViewDataChanged(scheduleIds, R.id.widget_schedule_list);
        new ScheduleWidgetProvider().onUpdate(context, manager, scheduleIds);

        int[] scoreIds = manager.getAppWidgetIds(new ComponentName(context, ScoreWidgetProvider.class));
        new ScoreWidgetProvider().onUpdate(context, manager, scoreIds);
    }
}
