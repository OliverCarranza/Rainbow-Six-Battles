package com.example.rainbow_six_battles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class Map extends View implements View.OnTouchListener {


    Paint paintBg = new Paint();

    Paint paintBomb = new Paint();

    Paint paintLine = new Paint();

    Paint paintOpen = new Paint();

    public Map(Context context) {
        super(context);

        paintBg.setColor(Color.GRAY);
        paintBg.setStyle(Paint.Style.FILL);
        paintLine.setColor(Color.WHITE);
        paintOpen.setARGB(0,0,0,0);
        drawGameArea(new Canvas());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    private void drawGameArea(Canvas canvas) {
        // border
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);

        // four horizontal lines
        canvas.drawLine(0, getHeight() / 5, getWidth(), getHeight() / 5,
                paintLine);
        canvas.drawLine(0, 2 * getHeight() / 5, getWidth(),
                2 * getHeight() / 5, paintLine);
        canvas.drawLine(0, 3 * getHeight() / 5, getWidth(),
                3 * getHeight() / 5, paintLine);
        canvas.drawLine(0, 4 * getHeight() / 5, getWidth(),
                4 * getHeight() / 5, paintLine);

        // four vertical lines
        canvas.drawLine(getWidth() / 5, 0, getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(2 * getWidth() / 5, 0, 2 * getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(3 * getWidth() / 5, 0, 3 * getWidth() / 5, getHeight(),
                paintLine);
        canvas.drawLine(4 * getWidth() / 5, 0, 4 * getWidth() / 5, getHeight(),
                paintLine);
    }
}
