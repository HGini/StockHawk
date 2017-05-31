package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Hemangini on 5/30/17.
 */

public class WidgetUtils {

    public static final String ACTION_APPWIDGET_DATA_CHANGED = "ACTION_APPWIDGET_DATA_CHANGED";

    public static void sendWidgetUpdate(final Context context) {
        Intent intent = new Intent(context, CustomAppWidgetProvider.class);
        intent.setAction(ACTION_APPWIDGET_DATA_CHANGED);
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, getAppWidgetIDs(context));
        context.sendBroadcast(intent);
    }

    private static int[] getAppWidgetIDs(final Context context) {
        ComponentName name = new ComponentName(context, CustomAppWidgetProvider.class);
        return AppWidgetManager.getInstance(context).getAppWidgetIds(name);
    }
}
