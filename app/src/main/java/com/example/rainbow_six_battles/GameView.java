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
import android.util.Log;
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

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView {
    public static int globalxSpeed = 5; //changes speed of enemy sprite
    int xx = 0;
    private GameLoopThread gameLoopThread; //changes framerate
    private SurfaceHolder holder;
    private Timer times;

    Bitmap enemybpm; //how fast the enemy moves
    Bitmap level1;
    Bitmap endImage;

    int screenWidth;
    int screenHeight;
    private List < Enemy > enemyList = new ArrayList < Enemy > (); // any enemies in the screen that has to be spawned or is spawned

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
        try {
            level1.setWidth(screenWidth);
            level1.setHeight(screenHeight);
        } catch(Exception a){
            Log.d("imgB", "Error Setting Width and Height for background Image");
        }
    }

    public void update() { // updates the screen after event
        deleteEnemy();
    }

    public void addGround() { // adds enemy to map
        // will continuously add an enemy to the map
        while (xx < this.getWidth() + Enemy.width) {
            enemyList.add(new Enemy(this, enemybpm, xx, 0));
            xx += enemybpm.getWidth();
        }
    }

    public void deleteEnemy() {
        int i = -1;
        Random rand = new Random();
        int r = rand.nextInt(2000 - 1000);
        try {
            for (i = enemyList.size(); i >= 0; i--) {
                int coinX = enemyList.get(i - 1).getX();

                if (coinX < -Enemy.width) {
                    enemyList.remove(i - 1);
                    enemyList.add(new Enemy(this, enemybpm,coinX + this.getWidth() + Enemy.width, r));
                }
            }
        } catch (Exception e) {
            Log.d("d", "Error found-" + i + ".  " + e.toString() +
                    ".  enemy Size = " + enemyList.size());
        }
    }



    public void draw(Canvas canvas) {
        super.draw(canvas);
        update(); // updates
        if(times.getElapsed() > 100) {
            canvas.drawBitmap(level1, 0, 0, new Paint());
        }
        addGround(); // adds enemy to ground
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
        if (times.getElapsed() >= 10000) { // change time to desired length, 1,000 milli is 1 second
            Log.d("cl", "Close Game, time  is up : " + times.getElapsed());
            //System.exit(0);
            return true; // reached the time limit
        }
        return false;//means that it still has not reached the time limit
    }

    private void endGame(){
        // sledge remove by setting array to 0;
    }

}