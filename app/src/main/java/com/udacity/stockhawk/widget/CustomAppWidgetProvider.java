package com.udacity.stockhawk.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.ui.MainActivity;

/**
 * Created by Hemangini on 5/2/17.
 */

public class CustomAppWidgetProvider extends android.appwidget.AppWidgetProvider {

    public static final String ITEM_POSITION = "ITEM_POSITION";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; i++) {
            Intent intent = new Intent(context, CustomRemoteViewsService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            rv.setRemoteAdapter(R.id.view_flipper, intent);

            rv.setEmptyView(R.id.view_flipper, R.id.empty_view);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
