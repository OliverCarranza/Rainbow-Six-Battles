package com.example.rainbow_six_battles;
/*
Author: Oliver Carranza
Date: 3/20/2024
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
    public static int globalxSpeed = 8; //changes speed of enemy sprite
    int xx = 0;
    private GameLoopThread gameLoopThread; //changes framerate
    private SurfaceHolder holder;

    Bitmap enemybpm; //how fast the enemy moves
    private List<Enemy> enemyList = new ArrayList<Enemy>(); // any enemies in the screen that has to be spawned or is spawned

    public GameView(Context context) {
        super(context); // calling parent
        gameLoopThread = new GameLoopThread(this); //new gameloop passing in Gameview
        holder = getHolder();
        holder.addCallback(new Callback() {
            // called to start game
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

            }
        });
        //drawing of ash in resting position
        enemybpm = BitmapFactory.decodeResource(getResources(), R.drawable.sledge1);

    }

    public void update() { // updates the screen after event
        deleteEnemy();
    }
    public void addGround(){
        while(xx < this.getWidth() + Enemy.width){
            enemyList.add(new Enemy(this, enemybpm, xx, 500));

            xx += enemybpm.getWidth();
        }
    }
    public void deleteEnemy(){
        int i = -1;
        Random rand = new Random();
        int r = rand.nextInt();
        try{
            for(i = enemyList.size(); i >= 0; i--){
                int coinX = enemyList.get(i-1).getX();

                if(coinX < - Enemy.width){
                    enemyList.remove(i-1);
                    enemyList.add(new Enemy(this, enemybpm,
                            coinX + this.getWidth() + Enemy.width, -rand.nextInt(600) - 10 )); //changes range and spots that the enemy appears
                }
            }
        }catch (Exception ex){
            Log.d("deleteGround", "Error found-" + i + ".  " + ex.toString()
                    + ".  Ground Size = " + enemyList.size());
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        update(); // updates
        addGround(); // adds enemy to ground

        Paint textPaint = new Paint();
        textPaint.setTextSize(32);

        for (Enemy genemyList : enemyList) { // draws enemy to canvas
            genemyList.onDraw(canvas);
        }

    }

}