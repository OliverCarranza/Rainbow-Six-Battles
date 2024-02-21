package com.example.rainbow_six_battles;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

public class Map extends View implements View.OnTouchListener {


    public Map(Context context) {
        super(context);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
