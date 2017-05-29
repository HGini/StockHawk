package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.widget.RemoteViewsService.*;

/**
 * Created by Hemangini on 5/21/17.
 */

public class CustomRemoteViewsFactory implements RemoteViewsFactory {

    private DecimalFormat dollarFormat;
    private List<WidgetItem> mWidgetItems = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;

    public CustomRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
    }
    public void onCreate() {
        if (mContext != null) {
            Cursor cursor = mContext.getContentResolver().query(
                    Contract.Quote.URI,
                    Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                    null, null, Contract.Quote.COLUMN_SYMBOL);

            if (cursor != null) {
                while (cursor.getCount() > 0 && cursor.moveToNext()) {
                    WidgetItem widgetItem = new WidgetItem();
                    widgetItem.setStockSymbol(cursor.getString(Contract.Quote.POSITION_SYMBOL));
                    widgetItem.setStockPrice(dollarFormat.format(cursor.getFloat(Contract.Quote.POSITION_PRICE)));

                    mWidgetItems.add(widgetItem);
                }

                cursor.close();
            }
        }
    }
    public void onDestroy() {
        mWidgetItems.clear();
    }
    public int getCount() {
        return mWidgetItems.size();
    }
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.tv_stock_name, mWidgetItems.get(position).getStockSymbol());
        rv.setTextViewText(R.id.tv_stock_price, mWidgetItems.get(position).getStockPrice());

        Bundle extras = new Bundle();
        extras.putInt(CustomAppWidgetProvider.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

        return rv;
    }
    public RemoteViews getLoadingView() {
        return null;
    }
    public int getViewTypeCount() {
        return 1;
    }
    public long getItemId(int position) {
        return position;
    }
    public boolean hasStableIds() {
        return true;
    }
    public void onDataSetChanged() {

    }
}
