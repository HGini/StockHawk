package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.DBManager;

/**
 * Created by Hemangini on 5/6/17.
 */

public class StockDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = LayoutInflater.from(this).inflate(R.layout.activity_stock_detail, null);
        setContentView(view);

        Bundle args = null;
        String stockSymbol = null;
        if (savedInstanceState != null)
            args = savedInstanceState;
        else if (getIntent().hasExtra(getString(R.string.intent_stock_bundle)))
            args = getIntent().getBundleExtra(getString(R.string.intent_stock_bundle));

        if (args != null && args.containsKey(getString(R.string.intent_stock_symbol))) {
            stockSymbol = args.getString(getString(R.string.intent_stock_symbol));
        }
        drawPriceTimeSeries(stockSymbol);
    }

    private void drawPriceTimeSeries(String stockSymbol) {
        if (!TextUtils.isEmpty(stockSymbol)) {
            DBManager.getHistory(this, stockSymbol);
        }
    }
}
