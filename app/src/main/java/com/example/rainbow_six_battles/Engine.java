package com.example.rainbow_six_battles;

//  Add your package name here

import java.math.BigDecimal;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.Float2;
import android.renderscript.Float3;
import android.content.pm.ActivityInfo;
import android.graphics.*;
import android.util.Log;
import android.view.*;
import android.view.View.OnTouchListener;
import android.util.DisplayMetrics;

/**
 * Engine Core Class
 */
public abstract class Engine extends Activity implements Runnable,
        OnTouchListener {
    private SurfaceView p_view;
    private Canvas p_canvas;
    private Thread p_thread;
    private boolean p_running, p_paused;
    private int p_pauseCount;
    private Paint p_paintDraw, p_paintFont;
    private Typeface p_typeface;
    private Point[] p_touchPoints;
    private int p_numPoints;
    int screenWidth;
    int screenHeight;

    private long p_preferredFrameRate, p_sleepTime;

    /**
     * Engine constructor
     */
    public Engine() {
        Log.d("Engine","Engine constructor");
        p_view = null;
        p_canvas = null;
        p_thread = null;
        p_running = false;
        p_paused = false;
        p_paintDraw = null;
        p_paintFont = null;
        p_numPoints = 0;
        p_typeface = null;
        p_preferredFrameRate = 40;
        p_sleepTime = 1000 / p_preferredFrameRate;
        p_pauseCount = 0;
    }

    /**
     * Abstract methods that must be implemented in the sub-class!
     */
    public abstract void init();
    public abstract void load();
    public abstract void draw();
    public abstract void update();


    /**
     * Activity.onCreate event method
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Engine","Engine.onCreate start");

        //disable the title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set default screen orientation
        setScreenOrientation(ScreenModes.LANDSCAPE);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        /**
         * Call abstract init method in sub-class!
         */
        init();

        //create the view object
        p_view = new SurfaceView(this);
        p_view.setKeepScreenOn(true);

        setContentView(p_view);

        //turn on touch listening
        p_view.setOnTouchListener(this);

        //create the points array
        p_touchPoints = new Point[5];
        for (int n=0; n<5; n++) {
            p_touchPoints[n] = new Point(0,0);
        }

        //create Paint object for drawing styles
        p_paintDraw = new Paint();
        p_paintDraw.setColor(Color.WHITE);

        //create Paint object for font settings
        p_paintFont = new Paint();
        p_paintFont.setColor(Color.WHITE);
        p_paintFont.setTextSize(24);

        /**
         * Call abstract load method in sub-class!
         */
        load();

        //launch the thread
        p_running = true;
        p_thread = new Thread(this);
        p_thread.start();

        Log.d("Engine","Engine.onCreate end");
    }

    /**
     * Runnable.run thread method (MAIN LOOP)
     */
    @Override
    public void run() {
        Log.d("Engine","Engine.run start");

        Timer frameTimer = new Timer();
        int frameCount=0;
        int frameRate=0;
        long startTime=0;
        long timeDiff=0;

        while (p_running) {
            /**
             * Process frame only if not paused.
             */
            if (p_paused) continue;

            /**
             * Calculate frame rate
             */
            frameCount++;
            startTime = frameTimer.getElapsed();
            if (frameTimer.stopwatch(1000)) {
                frameRate = frameCount;
                frameCount = 0;

                //reset touch input count
                p_numPoints = 0;
            }


            /**
             * Call abstract update method in sub-class!
             */
            update();

            /**
             * Rendering section, lock the canvas.
             * Only proceed if the SurfaceView is valid.
             */
            if (beginDrawing()) {

                p_canvas.drawColor(Color.BLUE);

                /**
                 * Call abstract draw method in sub-class!
                 */
                draw();


                int x = p_canvas.getWidth()-150;
                //p_canvas.drawText("ENGINE", x, 20, p_paintFont);
                //p_canvas.drawText(toString(frameRate) + " FPS", x, 40,
                //        p_paintFont);
                //p_canvas.drawText("Pauses: " + toString(p_pauseCount),
                //        x, 60, p_paintFont);

                /**
                 * Complete the rendering process by
                 * unlocking the canvas.
                 */
                endDrawing();
            }

            /**
             * Calculate frame update time and sleep if necessary.
             */
            timeDiff = frameTimer.getElapsed() - startTime;
            long updatePeriod = p_sleepTime - timeDiff;
            if (updatePeriod > 0) {
                try {
                    Thread.sleep( updatePeriod );
                }
                catch(InterruptedException e) {}
            }

        }
        Log.d("Engine","Engine.run end");

        System.exit(RESULT_OK);
    }

    /**
     * BEGIN RENDERING
     * Verify that the surface is valid and then lock the canvas.
     */
    private boolean beginDrawing() {
        if (!p_view.getHolder().getSurface().isValid()) {
            return false;
        }
        p_canvas = p_view.getHolder().lockCanvas();
        return true;
    }

    /**
     * END RENDERING
     * Unlock the canvas to free it for future use.
     */
    private void endDrawing() {
        p_view.getHolder().unlockCanvasAndPost(p_canvas);
    }

    /**
     * Activity.onResume event method
     */
    @Override
    public void onResume() {
        Log.d("Engine","Engine.onResume");
        super.onResume();
        p_paused = false;
        /*p_running = true;
        p_thread = new Thread(this);
        p_thread.start();*/
    }

    /**
     * Activity.onPause event method
     */
    @Override
    public void onPause() {
        Log.d("Engine","Engine.onPause");
        super.onPause();
        p_paused = true;
        p_pauseCount++;
        /*p_running = false;
        while (true) {
            try {
                p_thread.join();
            }
            catch (InterruptedException e) { }
        }*/
    }

    /**
     * OnTouchListener.onTouch event method
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //count the touch inputs
        p_numPoints = event.getPointerCount();
        if (p_numPoints > 5) p_numPoints = 5;

        //store the input values
        for (int n=0; n<p_numPoints; n++) {
            p_touchPoints[n].x = (int)event.getX(n);
            p_touchPoints[n].y = (int)event.getY(n);
        }
        return true;
    }

    /**
     * Shortcut methods to duplicate existing Android methods.
     */
    public void fatalError(String msg) {
        Log.e("FATAL ERROR", msg);
        System.exit(0);
    }

    /**
     * Drawing helpers
     */
    public void drawText(String text, int x, int y) {
        p_canvas.drawText(text, x, y, p_paintFont);
    }

    /**
     * Engine helper get/set methods for private properties.
     */
    public SurfaceView getView() {
        return p_view;
    }

    public Canvas getCanvas() {
        return p_canvas;
    }

    public void setFrameRate(int rate) {
        p_preferredFrameRate = rate;
        p_sleepTime = 1000 / p_preferredFrameRate;
    }

    public int getTouchInputs() {
        return p_numPoints;
    }

    public Point getTouchPoint(int index) {
        if (index > p_numPoints)
            index = p_numPoints;
        return p_touchPoints[index];
    }

    public void setDrawColor(int color) {
        p_paintDraw.setColor(color);
    }

    public void setTextColor(int color) {
        p_paintFont.setColor(color);
    }

    public void setTextSize(int size) {
        p_paintFont.setTextSize((float)size);
    }

    public void setTextSize(float size) {
        p_paintFont.setTextSize(size);
    }

    /**
     * Font style helper
     */
    public enum FontStyles {
        NORMAL (Typeface.NORMAL),
        BOLD (Typeface.BOLD),
        ITALIC (Typeface.ITALIC),
        BOLD_ITALIC (Typeface.BOLD_ITALIC);
        int value;
        FontStyles(int type) {
            this.value = type;
        }
    }

    public void setTextStyle(FontStyles style) {

        p_typeface = Typeface.create(Typeface.DEFAULT, style.value);
        p_paintFont.setTypeface(p_typeface);
    }

    /**
     * Screen mode helper
     */
    public enum ScreenModes {
        LANDSCAPE (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE),
        PORTRAIT (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        int value;
        ScreenModes(int mode) {
            this.value = mode;
        }
    }
    public void setScreenOrientation(ScreenModes mode) {
        setRequestedOrientation(mode.value);
    }

    /**
     * Round to a default 2 decimal places
     */
    public double round(double value) {
        return round(value,2);
    }

    /**
     * Round to any number of decimal places
     */
    public double round(double value, int precision) {
        try {
            BigDecimal bd = new BigDecimal(value);
            BigDecimal rounded = bd.setScale(precision, BigDecimal.
                    ROUND_HALF_UP);
            return rounded.doubleValue();
        }
        catch (Exception e) {
            Log.e("Engine","round: error rounding number");
        }
        return 0;
    }

    /**
     * String conversion helpers
     */
    public String toString(int value) {
        return Integer.toString(value);
    }

    public String toString(float value) {
        return Float.toString(value);
    }

    public String toString(double value) {
        return Double.toString(value);
    }

    public String toString(Float2 value) {
        String s = "X:" + round(value.x) + "," +
                "Y:" + round(value.y);
        return s;
    }

    public String toString(Float3 value) {
        String s = "X:" + round(value.x) + "," +
                "Y:" + round(value.y) + "," +
                "Z:" + round(value.z);
        return s;
    }

    public String toString(Point value) {
        Float2 f = new Float2(value.x,value.y);
        return toString((Float2)f);
    }

    public int getScreenWidth(){
        return screenWidth;
    }

    public int getScreenHeight(){
        return screenHeight;
    }

}

