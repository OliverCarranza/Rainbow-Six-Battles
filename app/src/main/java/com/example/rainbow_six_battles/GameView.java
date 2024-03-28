package com.example.rainbow_six_battles;
/*
Author: Oliver Carranza
Date: 3/4/2024
Purpose: This file will have a backenemyList image where you have a (defenders) sprite that will
        only move up or down. Enemy Sprites will also be moving from right to left.
        If the enemy sprites reach the end, then game ends.
        The defenders only have 2 minutes to survive till they win.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Callback, View.OnTouchListener {
    public static int globalxSpeed = 7; //changes speed of enemy sprite
    int xx = 0;
    private GameLoopThread gameLoopThread; //changes framerate
    private SurfaceHolder holder;
    private Timer times;

    Bitmap enemybpm; //how fast the enemy moves
    private boolean sledgeClicked = false; // Flag to check if sledge is clicked
    Bitmap level1;
    Bitmap endImage;
    Rect rect;
    Enemy en; // enemy class

    int screenWidth;
    int screenHeight;
    private List < Enemy > enemyList = new ArrayList < Enemy > (); // any enemies in the screen that has to be spawned or is spawned
    private List < Rect > rectList = new ArrayList< Rect >(); // array of rectangle objects that will go over the enemy

    public GameView(Context context, int screenWidth, int screenHeight) {
        super(context); // calling parent
        //drawBackground();
        gameLoopThread = new GameLoopThread(this); //new gameloop passing in Gameview

        times = new Timer();
        Log.d("new_time", "TIME BEGGING is " + times.getElapsed());
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        holder = getHolder();
        holder.addCallback(new Callback() {
            // called to start game
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }
            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {}
            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {}
        });
        //drawing of sledge in hammer position
        enemybpm = BitmapFactory.decodeResource(getResources(), R.drawable.sledge1);
        level1 = BitmapFactory.decodeResource(getResources(), R.drawable.level1);
        endImage = BitmapFactory.decodeResource(getResources(), R.drawable.winning_screen);
       // try {
           // level1.setWidth(screenWidth);
       //     level1.setHeight(screenHeight);
      //  } catch(Exception a){
       //     Log.d("imgB", "Error Setting Width and Height for background Image");
        //}

        // add line here

    }

    public void update() { // updates the screen after event
        deleteEnemy();
    }

    public void addGround() { // adds enemy to map
        Random rand = new Random();
        int r = rand.nextInt(2000 - 1350);
        // will continuously add an enemy to the map
        while (xx < this.getWidth() + Enemy.width) {
            enemyList.add(new Enemy(this, enemybpm, xx, r));
            en = enemyList.get(enemyList.size()-1);
            rectList.add(new Rect(en.getX() + 15, en.getY() + 5, Enemy.width, 200));
            xx += enemybpm.getWidth();
        };
        setOnTouchListener(this); // Set onTouchListener for handling touch events
    }

    public void deleteEnemy() {
        int i = -1;
        Random rand = new Random();
        int r = rand.nextInt(2000 - 1350);
        try {
            for (i = enemyList.size(); i >= 0; i--) {
                int coinX = enemyList.get(i - 1).getX();

                if (coinX < - Enemy.width) {
                    enemyList.remove(i - 1);
                    rectList.remove(i - 1); // removes the second to last rectangle object
                    enemyList.add(new Enemy(this, enemybpm,coinX + this.getWidth() + Enemy.width, r));
                    en = enemyList.get(enemyList.size()-1);
                    rectList.add(new Rect(en.getX() + 15, en.getY() + 5, Enemy.width, 200));
                }
            }
        } catch (Exception e) {
            Log.d("d", "Error found-" + i + ".  " + e.toString() +
                    ".  enemy Size = " + enemyList.size());
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(times.getElapsed() > 100) {
            canvas.drawBitmap(level1, 0, 0, new Paint());
        }
        addGround(); // adds enemy to ground

        update(); // updates
        Log.d("t", "Current Time elapsed " + times.getElapsed());
        if(checkTime()){ //Checks time if it is more than alloted, will display the end winning screen.
            try {
                endImage.setWidth(screenWidth);
                endImage.setHeight(screenHeight);
            } catch(Exception a){
                Log.d("imgB", "Error Setting Width and Height for End Game Image");
            }
            endGame();
            canvas.drawBitmap(endImage, 0, 0, new Paint());
        }
        Paint textPaint = new Paint();
        textPaint.setTextSize(32);

        for (Enemy genemyList: enemyList) { // draws enemy to canvas
            genemyList.onDraw(canvas);
        }

    }
    // Checks if time is greater than or equal to certain milliseconds and if so,
    // then closes game.
    private boolean checkTime() {
        if (times.getElapsed() >= 40000) { // change time to desired length, 1,000 milli is 1 second
            Log.d("cl", "Close Game, time  is up : " + times.getElapsed());
            //System.exit(0);
            return true; // reached the time limit
        }
        return false;//means that it still has not reached the time limit
    }

    private void endGame(){
        // sledge removed from array
        for(int i = 0; i < enemyList.size(); i++) {
            //if(enemyList.get(i).getX() < 50){
                enemyList.remove(i);
                //deleteEnemy();
                //enemyList.set(i,)
            //}
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Get touch coordinates
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();

        // Check if the touch event occurred on the sledge image
        if (event.getAction() == MotionEvent.ACTION_DOWN && !sledgeClicked) {
            if (1 == 1) {
                deleteSledge();
                Log.d("touchSle", "Touched rect!!");
            }
        }
        Log.d("noTouSle", "Did NOT Touch rect  X: " + touchX + " | Y: " + touchY);
        return true;
    }

    private void deleteSledge() {
        // Iterate over the enemyList to find and remove the sledge
        for (int i = 0; i < enemyList.size(); i++) {
            Enemy enemy = enemyList.get(i);
            if (enemy.getBitmap() == enemybpm) {
                enemyList.remove(i);
                break; // Exit loop once sledge is removed
            }
        }
        Log.d("delSle", "Deleted Sledge!!");
    }

    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameLoopThread.setRunning(true);
        gameLoopThread.start();
    }
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {}
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {}
}