package com.udacity.stockhawk.data;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by Hemangini on 5/8/17.
 */

public class DBManager {

    public static ArrayList<HistoricalQuote> getHistory(final Context context, final String stockSymbol) {
        ArrayList<HistoricalQuote> history = new ArrayList<>();
        if (context != null && !TextUtils.isEmpty(stockSymbol)) {
            Cursor cursor = context.getContentResolver()
                    .query(Contract.Quote.makeUriForStock(stockSymbol), null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                populateHistory(context, cursor, history);
                cursor.close();
            }
        }
        return history;
    }

    private static void populateHistory(Context context, Cursor cursor, ArrayList<HistoricalQuote> history) {
        if (context != null && cursor != null && history != null) {
            String historyString = cursor.getString(cursor.getColumnIndex(Contract.Quote.COLUMN_HISTORY));
            String[] quotes = historyString.split("\\n");
            for (String quote : quotes) {
                String[] quoteInfo = quote.split(",");

                // Get quote's time
                long quoteTimeLong = Long.valueOf(quoteInfo[0].trim());
                Calendar quoteTime = Calendar.getInstance();
                quoteTime.setTimeInMillis(quoteTimeLong);

                // Get quote's close price
                float quotePriceFloat = Float.valueOf(quoteInfo[1].trim());
                BigDecimal quoteClosePrice = BigDecimal.valueOf(quotePriceFloat);

                // Create historical quote
                HistoricalQuote historicalQuote = new HistoricalQuote();
                historicalQuote.setDate(quoteTime);
                historicalQuote.setClose(quoteClosePrice);
                history.add(historicalQuote);
            }
        }
    }
}
