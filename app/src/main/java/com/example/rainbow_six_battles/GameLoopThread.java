package com.example.rainbow_six_battles;

import android.graphics.Canvas;

public class GameLoopThread extends Thread{

    private GameView view;
    static final long FPS = 30;
    boolean running;

    //Constructor
    public GameLoopThread(GameView v){
        this.view = v;
    }

    public void setRunning(boolean b) {
        running = b;
    }

    public void run(){
        long ticksPS = 1000 / FPS;
        long startTime = 0;
        long sleepTime;

        while (running) {
            Canvas c = null;

            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.draw(c); //onDraw if errors
                }
            } finally {
                if (c != null){
                    view.getHolder().unlockCanvasAndPost(c);
                }
                sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
                try {
                    if (sleepTime > 0){
                        sleep(sleepTime);
                    } else {
                        sleep(10);
                    }
                } catch (Exception e){
                    //TODO debug
                }
            }
        }
    }
}
