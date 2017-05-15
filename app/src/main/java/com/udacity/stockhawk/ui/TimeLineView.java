package com.udacity.stockhawk.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

import yahoofinance.histquotes.HistoricalQuote;


/**
 * Created by Hemangini on 5/8/17.
 */

public class TimeLineView extends View {

    float width = 0;
    float height = 0;
    float margin = 50;
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
            canvas.drawLine(0, height - margin, width, height - margin, paint);

            // Draw markers on X-axis
            int numMarks = 5;
            float prevMarkX = 0;
            float markXDiv = width/numMarks;
            int markValDiv = history.size()/numMarks;

            for (int i = 0; i < numMarks; i++) {
                // Marker
                canvas.drawLine(prevMarkX, height - 20, prevMarkX, height - 10, paint);

                // Marker value
                Calendar calendar = history.get(i + markValDiv).getDate();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String date = day + "/" + month + "/" + year;
                paint.setTextSize(26);
                canvas.drawText(date, prevMarkX, height, paint);

                prevMarkX += markXDiv;
            }

            // Draw Y-axis
            canvas.drawLine(0, height - margin, 0, height - (1.5f * getMaxCloseTime()) - margin, paint);

            // Plot the graph
            float prevX = 0;
            float prevY = height - margin;
            float xDiv = width/history.size();
            for (HistoricalQuote histQuote : history) {
                float startX = prevX;
                float startY = prevY;
                float stopX = (prevX + xDiv);
                float stopY = (height - histQuote.getClose().floatValue() - margin);
                canvas.drawLine(startX, startY, stopX, stopY, paint);

                prevX += xDiv;
                prevY = (height - histQuote.getClose().floatValue() - margin);
            }
        } else {
            super.onDraw(canvas);
        }
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
