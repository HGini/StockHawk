package com.udacity.stockhawk.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import yahoofinance.histquotes.HistoricalQuote;


/**
 * Created by Hemangini on 5/8/17.
 */

public class TimeLineView extends View {

    float width = 0;
    float height = 0;
    float margin = 80;
    Paint paint = new Paint();
    ArrayList<HistoricalQuote> history;

    public TimeLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.WHITE);
    }

    public void setHistory(ArrayList<HistoricalQuote> history) {
        this.history = history;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (history != null && history.size() > 0) {

            // Draw X-axis
            canvas.drawLine(margin, height - margin, width - margin, height - margin, paint);

            // Draw markers on X-axis
            int numMarks = 5;
            float prevMarkX = margin;
            float markXDiv = (width - 2 * margin)/numMarks;
            int markValDiv = history.size()/numMarks;

            for (int i = 0; i < numMarks; i++) {
                // Marker
                canvas.drawLine(prevMarkX, height - margin, prevMarkX, height - margin + 10, paint);

                // Marker value
                Calendar calendar = history.get(history.size() - 1 - (i * markValDiv)).getDate();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String date = day + "/" + month + "/" + year;
                paint.setTextSize(26);
                canvas.drawText(date, prevMarkX - margin - 10, height, paint);

                prevMarkX += markXDiv;
            }
            canvas.drawLine(prevMarkX - 5, height - margin, prevMarkX - 5, height - margin + 10, paint);
            canvas.drawText("Today", prevMarkX - margin, height - 5, paint);

            // Draw Y-axis
            canvas.drawLine(margin, height - margin, margin, 40, paint);

            // Plot highest y value
            paint.setColor(ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark));
            canvas.drawLine(margin, height - margin - getMaxCloseTimeYCoord(), width - margin,
                    height - margin - getMaxCloseTimeYCoord(), paint);
            paint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
            canvas.drawText(String.valueOf(getMaxCloseTime()), 0, height - margin - getMaxCloseTimeYCoord() + 5, paint);

            // Normalise close times
            ArrayList<HistoricalQuote> normHistory = normalizeQuoteValues();

            // Plot the graph
            float prevX = margin;
            float prevY = (height - margin - normHistory.get(normHistory.size() - 1).getClose().floatValue());
            float xDiv = (width - 2 * margin)/normHistory.size();
            for (int i = normHistory.size() - 1; i >= 0; i--) {
                HistoricalQuote histQuote = normHistory.get(i);
                float startX = prevX;
                float startY = prevY;
                float stopX = (prevX + xDiv);
                float stopY = (height - margin - histQuote.getClose().floatValue());
                canvas.drawLine(startX, startY, stopX, stopY, paint);

                prevX += xDiv;
                prevY = (height - margin - histQuote.getClose().floatValue());
            }
        } else {
            super.onDraw(canvas);
        }
    }

    private ArrayList<HistoricalQuote> normalizeQuoteValues() {
        ArrayList<HistoricalQuote> normHistory = new ArrayList<>();
        float maxVal = getMaxCloseTime();
        for (HistoricalQuote histQuote : history) {
            float val = histQuote.getClose().floatValue();
            float normVal = (val/maxVal) * getMaxCloseTimeYCoord();
            HistoricalQuote normQuote = new HistoricalQuote();
            normQuote.setClose(BigDecimal.valueOf(normVal));
            normHistory.add(normQuote);
        }
        return normHistory;
    }

    private float getMaxCloseTimeYCoord() {
        return 0.75f * (height - margin);
    }

    private float getMaxCloseTime() {
        float maxCloseTime = 0;
        for (HistoricalQuote histQuote : history) {
            if (maxCloseTime < histQuote.getClose().floatValue()) {
                maxCloseTime = histQuote.getClose().floatValue();
            }
        }
        return maxCloseTime;
    }
}
