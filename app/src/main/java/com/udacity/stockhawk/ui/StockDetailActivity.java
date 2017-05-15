package com.udacity.stockhawk.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.DBManager;
import com.udacity.stockhawk.data.PrefUtils;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by Hemangini on 5/6/17.
 */

public class StockDetailActivity extends AppCompatActivity {

    private String stockSymbol = null;

    private TextView symbolView;
    private TextView priceView;
    private TextView changeView;
    private TimeLineView timeLineView;

    private DecimalFormat dollarFormatWithPlus;
    private DecimalFormat dollarFormat;
    private DecimalFormat percentageFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(this).inflate(R.layout.activity_stock_detail, null);
        setContentView(view);
        initView(view);

        Bundle args = null;
        if (savedInstanceState != null)
            args = savedInstanceState;
        else if (getIntent().hasExtra(getString(R.string.intent_stock_bundle)))
            args = getIntent().getBundleExtra(getString(R.string.intent_stock_bundle));

        if (args != null && args.containsKey(getString(R.string.intent_stock_symbol))) {
            stockSymbol = args.getString(getString(R.string.intent_stock_symbol));
        }

        bindData();
    }

    private void initView(View view) {
        symbolView = (TextView) view.findViewById(R.id.symbol);
        priceView = (TextView) view.findViewById(R.id.price);
        changeView = (TextView) view.findViewById(R.id.change);
        timeLineView = (TimeLineView) view.findViewById(R.id.timeline_view);
        timeLineView.setHeight(getResources().getDimension(R.dimen.timeline_height));
        timeLineView.setWidth(getScreenWidth());
    }

    private float getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private void bindData() {
        if (stockSymbol != null) {
            dollarFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
            dollarFormatWithPlus = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
            dollarFormatWithPlus.setPositivePrefix("+$");
            percentageFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.getDefault());
            percentageFormat.setMaximumFractionDigits(2);
            percentageFormat.setMinimumFractionDigits(2);
            percentageFormat.setPositivePrefix("+");

            Cursor cursor = getContentResolver().query(Contract.Quote.makeUriForStock(stockSymbol),
                    null, null, null, null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    symbolView.setText(cursor.getString(Contract.Quote.POSITION_SYMBOL));
                    priceView.setText(dollarFormat.format(cursor.getFloat(Contract.Quote.POSITION_PRICE)));

                    float rawAbsoluteChange = cursor.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
                    float percentageChange = cursor.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);

                    if (rawAbsoluteChange > 0) {
                        changeView.setBackgroundResource(R.drawable.percent_change_pill_green);
                    } else {
                        changeView.setBackgroundResource(R.drawable.percent_change_pill_red);
                    }

                    String change = dollarFormatWithPlus.format(rawAbsoluteChange);
                    String percentage = percentageFormat.format(percentageChange / 100);

                    if (PrefUtils.getDisplayMode(this)
                            .equals(getString(R.string.pref_display_mode_absolute_key))) {
                        changeView.setText(change);
                    } else {
                        changeView.setText(percentage);
                    }
                }
                cursor.close();
            }

            drawPriceTimeSeries();
        }
    }

    private void drawPriceTimeSeries() {
        if (!TextUtils.isEmpty(stockSymbol)) {
            ArrayList<HistoricalQuote> history = DBManager.getHistory(this, stockSymbol);
            timeLineView.setHistory(history);
            timeLineView.invalidate();
        }
    }
}
