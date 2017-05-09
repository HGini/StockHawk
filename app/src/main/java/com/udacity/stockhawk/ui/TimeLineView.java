package com.udacity.stockhawk.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import yahoofinance.histquotes.HistoricalQuote;


/**
 * Created by Hemangini on 5/8/17.
 */

public class TimeLineView extends View {

    float width = 0;
    float height = 0;
    float lastPointX = 0;
    float lastPointY = 0;
    float xDivLength = 0;
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
            xDivLength = width/history.size();
            lastPointY = - height;

            for (HistoricalQuote histQuote : history) {
                canvas.drawLine(lastPointX, lastPointY, (lastPointX + xDivLength),
                        (lastPointY + histQuote.getClose().floatValue()), paint);
                lastPointX += xDivLength;
            }
        } else {
            super.onDraw(canvas);
        }
    }
}
