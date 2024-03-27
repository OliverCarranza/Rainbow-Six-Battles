/*
 - Names: Major Andrews, Oliver Carranza, Major Andrews
 - Using GitHub Repository to work collaboratively and share code, images, media, and files with each other.
 - Main Activity Java file that starts the game and guides the user into the start of gameplay levels.
 - This code and documentation meet the requirements for this Mobile Final Project.
 */

package com.example.rainbow_six_battles;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Enemy {
    private int health;
    private int bps; //bullets per second
    private int cost;
    private int recharge;
    private String name;
    public static int width;
    private GameView gameView;
    private GameView2 gameView2;

    private GameView3 gameView3;
    private Bitmap bmp;
    private int x;
    private int y;
    Random randomNumber;
    private int ls;

    public Enemy(GameView gv, Bitmap bp, int x, int y) {
        this.health = 3;
        this.gameView = gv;
        this.bmp = bp;
        this.x = x;
        this.y = y;
        this.randomNumber = new Random();
    }
    public Enemy(GameView2 gv, Bitmap bp, int x, int y) {
        this.health = 3;
        this.gameView2 = gv;
        this.bmp = bp;
        this.x = x;
        this.y = y;
        this.randomNumber = new Random();
    }

    public Enemy(GameView3 gv, Bitmap bp, int x, int y) {
        this.health = 3;
        this.gameView3 = gv;
        this.bmp = bp;
        this.x = x;
        this.y = y;
        this.randomNumber = new Random();
    }

    public Bitmap getBitmap(){
        return this.bmp;
    }

    private void update() {
        //Move the ground
        if(gameView != null) {
            x -= gameView.globalxSpeed;
        } else if(gameView2 != null) {
            x -= gameView2.globalxSpeed;
        } else if (gameView2 != null){
            x -= gameView3.globalxSpeed;
        }
    }

    public int getX() {
        return x;
    }

    // To try to remove sledge(s) - Josiah is testing
    public int getY() {return y;}

    public void onDraw(Canvas c) {
        update();
        ls = randomNumber.nextInt(601);
        if(gameView != null) {
            c.drawBitmap(bmp, x, y + gameView.getHeight() - 800, null);
        } else if(gameView2 != null) {
            c.drawBitmap(bmp, x, y + gameView2.getHeight() - 800, null);
        } else if (gameView3 != null){
            c.drawBitmap(bmp, x, y + gameView3.getHeight() - 800, null);
        }
    }

}