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
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import java.util.concurrent.TimeUnit;

public class GameView extends SurfaceView implements Callback, View.OnTouchListener {
    public static int globalxSpeed = 7; //changes speed of enemy sprite
    private int xx = 0;
    private GameLoopThread gameLoopThread; //changes framerate
    private SurfaceHolder holder;
    private Timer times;
    private Timer spawnTime;
    private Timestamp lastClick;
    private Canvas canv;
    private Bitmap enemybpm; //how fast the enemy moves
    private Bitmap defeatImg;
    private Bitmap level1;
    private Bitmap endImage;
    private Paint paint;
    private int screenWidth;
    private int screenHeight;
    private boolean endGameBool = false;
    private int score = 0;
    private List < Enemy > enemyList = new ArrayList < Enemy > (); // any enemies in the screen that has to be spawned or is spawned

    public GameView(Context context, int screenWidth, int screenHeight) {
        super(context); // calling parent
        //drawBackground();
        canv = new Canvas();
        gameLoopThread = new GameLoopThread(this); //new gameloop passing in Gameviev
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
        level1 = BitmapFactory.decodeResource(getResources(), R.drawable.level1); //change image for levels
        endImage = BitmapFactory.decodeResource(getResources(), R.drawable.winning_screen);
        defeatImg = BitmapFactory.decodeResource(getResources(), R.drawable.defeat);
        //timers for spawning and figuring spawnage
        times = new Timer();
        spawnTime = new Timer();
        lastClick = new Timestamp(new Date().getTime());
        //paint
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(80);
        //touch listener
        setOnTouchListener(this);
    }

    public void update() { // updates the screen after event
        deleteEnemy();
    }

    public void addGround() { // adds enemy to map
        Log.d("addEnmeny", "ADDED ENEMY TO THE MAP!!!!");
        Random rand = new Random();
        Random rand2 = new Random();
        int r2 = rand2.nextInt(300);
        int r = screenWidth - rand.nextInt(200);
        enemyList.add(new Enemy(this, enemybpm,r + Enemy.width, r2));
    }

    public void deleteEnemy() {
        int i = -1;
        Random rand = new Random();
        int r = screenWidth - rand.nextInt(200) ;
        if(enemyList.size() == 0 && !endGameBool){ //supposed to be like this, a surprise enemy
            enemyList.add(new Enemy(this, enemybpm,r + Enemy.width, r));
            enemyList.add(new Enemy(this, enemybpm,r + Enemy.width, r));
            enemyList.add(new Enemy(this, enemybpm,r + Enemy.width, r));
        } else {
            try {
                for (i = enemyList.size(); i > 0; i--) {
                    int coinX = enemyList.get(i - 1).getX();
                    if (coinX < -Enemy.width) {
                        enemyList.remove(i - 1);
                        enemyList.add(new Enemy(this, enemybpm, coinX + this.getWidth() + Enemy.width, r));
                        //add the code for switching screen HERE
                        endGameBool = true;
                    }
                }
            } catch (Exception e) {
                Log.d("DELETE_ENEMY", "Error found-" + i + ".  " + e.toString() +
                        ".  enemy Size = " + enemyList.size());
            }
        }
    }
    //drawing objects and updating screen with new items
    public void draw(Canvas canvas) {
        super.draw(canvas);
        update(); // updates
        if(times.getElapsed() > 100) {
            canvas.drawBitmap(level1, 0, 0, new Paint());
        }
        if(endGameBool){
            deleteSledge();
            canv.drawBitmap(defeatImg, 0, 0, new Paint());
            Log.d("endGame", "END GAME GAME ENDED YOU LOST");
        }
        if(checkEnemyTime()){
            addGround(); // adds enemy to ground
        }

        Log.d("t", "Current Time elapsed " + times.getElapsed());
        if(checkTime()){ //Checks time if it is more than alloted, will display the end winning screen.
            endGame();
            canvas.drawBitmap(endImage, 0, 0, new Paint());
        }

        for (Enemy genemyList: enemyList) { // draws enemy to canvas
            genemyList.onDraw(canvas);
        }
        // drawing score
        canvas.drawText("Score: " + score, 200, 55, paint);
    }

    private boolean checkEnemyTime() {
        if(spawnTime.getElapsed() >= 1500) {
            spawnTime = new Timer();
            return true;
        }

        return false;
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
                enemyList.remove(i);
                endGameBool = true;
                deleteEnemy();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //checking if time stamp from begging is less than 250 mm than new click
            // if smaller, does not allow code to run


//            Timestamp temp = new Timestamp(new Date().getTime());
//            long lmg = lastClick.getTime() - temp.getTime();
//            if(lmg <= 250){
//                return false;
//            }
//            lastClick = temp;

            int a = 0;
            for(int i = 0; i < enemyList.size(); i++) {
                if(enemyList.get(a).getX() > enemyList.get(i).getX()) {
                    a = i;
                    score++;
                }
            }
            enemyList.get(a).setX(screenWidth);
            spawnTime = new Timer();
        } // end if

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