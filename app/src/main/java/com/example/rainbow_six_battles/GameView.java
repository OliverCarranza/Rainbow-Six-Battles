package com.example.rainbow_six_battles;

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
    public static int globalxSpeed = 8;
    int xx = 0;

    int coinxx = 50;
    private GameLoopThread gameLoopThread;
    private SurfaceHolder holder;

    Bitmap groundBmp;
    Bitmap coinBmp;
    private List<Ground> ground = new ArrayList<Ground>();
    private List<Coin> coin = new ArrayList<Coin>();

    public GameView(Context context, ) {
        super(context);


        gameLoopThread = new GameLoopThread(this);

        holder = getHolder();
        holder.addCallback(new Callback() {


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

        groundBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        coinBmp = BitmapFactory.decodeResource(getResources(), R.drawable.coin);

    }

    public void update() {
        deleteGround();
        deleteCoin();
    }
    public void addGround(){

        while (xx < this.getWidth() + Ground.width){
            ground.add(new Ground(this, groundBmp, xx, 0));

            xx += groundBmp.getWidth();
        }
    }

    public void deleteGround(){
        int i = -1;
        try{
            for(i = ground.size(); i >= 0; i--){
                int groundX = ground.get(i-1).getX();

                if(groundX < -Ground.width){
                    ground.remove(i-1);
                    ground.add(new Ground(this, groundBmp,
                            groundX + this.getWidth() + Ground.width, 0));
                }
            }
        } catch (Exception ex){
            Log.d("deleteGround", "Error found-" + i + ".  " + ex.toString()
                    + ".  Ground Size = " + ground.size());
        }
    }

    public void addCoin(){
        while(coinxx < this.getWidth()+  Coin.width){
            coin.add(new Coin(this, coinBmp, coinxx, 800));
            coinxx += coinBmp.getWidth();
        }
    }
    public void deleteCoin(){
        int i = -1;
        Random rand = new Random();
        int r = rand.nextInt(2000-1000);
        try{
            for(i = coin.size(); i >= 0; i--){
                int coinX = coin.get(i-1).getX();

                if(coinX < -Coin.width){
                    coin.remove(i-1);
                    coin.add(new Coin(this, coinBmp,
                            coinX + this.getWidth() + Coin.width, r ));
                }
            }
        } catch (Exception e){
            Log.d("d", "Error found-" + i + ".  " + e.toString()
                    + ".  coin Size = " + coin.size());
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);

        update();
        canvas.drawColor(Color.CYAN);
        addGround();
        addCoin();

        Paint textPaint = new Paint();
        textPaint.setTextSize(32);

        for (Ground gground : ground) {
            gground.onDraw(canvas);
        }


        for (Coin c : coin) {
            c.onDraw(canvas);
        }

    }

}