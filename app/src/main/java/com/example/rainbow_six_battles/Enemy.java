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

    private void update() {
        //Move the ground
        x -= gameView.globalxSpeed;
    }

    public int getX() {
        return x;
    }

    public void onDraw(Canvas c) {
        update();
        ls = randomNumber.nextInt(601);
        c.drawBitmap(bmp, x, y + gameView.getHeight() - 800, null);
    }

}