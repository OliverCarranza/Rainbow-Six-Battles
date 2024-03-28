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

// class
public class Enemy {
    public static int width;
    private GameView gameView;
    private GameView2 gameView2;

    private GameView3 gameView3;
    private Bitmap bmp;
    private int x;
    private int y;
    Random randomNumber;
    private int ls;


    //constructors for the enemy Class
    public Enemy(GameView gv, Bitmap bp, int x, int y) {
        this.gameView = gv;
        this.bmp = bp;
        this.x = x;
        this.y = y;
        this.randomNumber = new Random();
    }
    public Enemy(GameView2 gv, Bitmap bp, int x, int y) {
        this.gameView2 = gv;
        this.bmp = bp;
        this.x = x;
        this.y = y;
        this.randomNumber = new Random();
    }

    public Enemy(GameView3 gv, Bitmap bp, int x, int y) {
        this.gameView3 = gv;
        this.bmp = bp;
        this.x = x;
        this.y = y;
        this.randomNumber = new Random();
    }

    public Bitmap getBitmap(){
        return this.bmp;
    }

    // Sets X var to globalSpeed for the enemies
    private void update() {
        //Move the ground
        if(gameView != null) {
            x -= gameView.globalxSpeed;
        } else if(gameView2 != null) {
            x -= gameView2.globalxSpeed;
        } else if (gameView3 != null){
            x -= gameView3.globalxSpeed;
        }
    }

    public int getX() {
        return x;
    }

    // To try to remove sledge(s) - Josiah is testing
    public int getY() {return y;}
    public void setY(int y){this.y = y;}
    public void setX(int x){this.x = x;}


    // calls update method from Enemy Class which sets speed.
    // Then draws the images at the certain x, y, image, height and paint method
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