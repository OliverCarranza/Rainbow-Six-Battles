package com.example.rainbow_six_battles;
/*
 - Names: Major Andrews, Oliver Carranza, Josiah Mathews
 - Using GitHub Repository to work collaboratively and share code, images, media, and files with each other.
 - GameView2 Java file that starts the game and guides the user into the start of gameplay levels.
 - This code and documentation meet the requirements for this Mobile Final Project.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.view.View;
import java.util.Random;

public class GameView2 extends SurfaceView implements Callback, View.OnTouchListener {
    public static int globalxSpeed = 15; //changes speed of enemy sprite
    private int xx = 0;
    private GameLoopThread gameLoopThread; //changes framerate
    private SurfaceHolder holder;
    private Timer times;
    private Timer spawnTime;
    private Timestamp lastClick;
    private Canvas canv;
    private Bitmap enemybpm; //how fast the enemy moves
    private Bitmap defeatImg;
    private Bitmap level2;
    private Bitmap endImage;
    private Paint paint;
    private int screenWidth;
    private int screenHeight;
    private boolean endGameBool = false;
    private int score = 0;
    private List < Enemy > enemyList = new ArrayList < Enemy > (); // any enemies in the screen that has to be spawned or is spawned

    public GameView2(Context context, int screenWidth, int screenHeight) {
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
        level2 = BitmapFactory.decodeResource(getResources(), R.drawable.level2); //change image for levels
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

    // when called, this function calls another function called deleteEnemy()
    public void update() { // updates the screen after event
        deleteEnemy();
    }

    //  Adds enemy to the enemyList and randomizes its position in the screen.
    public void addGround() { // adds enemy to map
        Log.d("addEnmeny", "ADDED ENEMY TO THE MAP!!!!");
        Random rand = new Random();
        Random rand2 = new Random();
        int r2 = rand2.nextInt(300);
        int r = screenWidth - rand.nextInt(200);
        enemyList.add(new Enemy(this, enemybpm, r + Enemy.width, r2));
    }
    // removes enemy from the enemyList and adds more after removing one.
    public void deleteEnemy() {
        int i = -1;
        Random rand = new Random();
        int r = screenWidth - rand.nextInt(200);
        if (enemyList.size() == 0 && !endGameBool) { //supposed to be like this, a surprise enemy
            enemyList.add(new Enemy(this, enemybpm, r + Enemy.width, r));
            enemyList.add(new Enemy(this, enemybpm, r + Enemy.width, r));
            enemyList.add(new Enemy(this, enemybpm, r + Enemy.width, r));
        } else {
            try {
                for (i = enemyList.size(); i > 0; i--) {
                    int coinX = enemyList.get(i - 1).getX();
                    if (coinX < -Enemy.width) {
                        endGameBool = true; // call for defeat
                        enemyList.remove(i - 1);
                        enemyList.add(new Enemy(this, enemybpm, coinX + this.getWidth() + Enemy.width, r));
                        //add the code for switching screen HERE

                    }
                }
            } catch (Exception e) {
                Log.d("DELETE_ENEMY", "Error found-" + i + ".  " + e.toString() +
                        ".  enemy Size = " + enemyList.size());
            }
        }
    }
    //drawing objects and updating screen with new items
    // checks for time limit reached
    // enemy reached ending
    // also draws and logs other small details.
    public void draw(Canvas canvas) {
        super.draw(canvas);

        update(); // updates
        if (times.getElapsed() > 100) {
            canvas.drawBitmap(level2, 0, 0, new Paint());
        }

        if (endGameBool == true) {
            canvas.drawBitmap(defeatImg, 0, 0, new Paint());
            deleteSledge();
            Log.d("endGame", "END GAME GAME ENDED YOU LOST");
        }

        if (checkEnemyTime()) {
            addGround(); // adds enemy to ground
        }

        Log.d("t", "Current Time elapsed " + times.getElapsed());
        if (checkTime() && !endGameBool) { //Checks time if it is more than alloted, will display the end winning screen.
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
        if (spawnTime.getElapsed() >= 1500) {
            spawnTime = new Timer();
            return true;
        }
        return false;
    }

    // Checks if time is greater than or equal to certain milliseconds and if so,
    // then closes game.
    private boolean checkTime() {
        if (times.getElapsed() >= 45000) { // change time to desired length, 1,000 milli is 1 second
            Log.d("cl", "Close Game, time  is up : " + times.getElapsed());
            //System.exit(0);
            return true; // reached the time limit
        }
        return false; //means that it still has not reached the time limit
    }

    private void endGame() {
        // sledge removed from array
//        for (int i = 0; i < enemyList.size(); i++) {
//            enemyList.remove(i);
        deleteSledge();
//        }
    }
    // Checks for user input and if so then raise score and check which enemy is
    //  close to the wall to remove them.
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
            for (int i = 0; i < enemyList.size(); i++) {
                if (enemyList.get(a).getX() > enemyList.get(i).getX()) {
                    a = i;
                    score++;
                }
            }
            enemyList.get(a).setX(screenWidth);
            spawnTime = new Timer();
        } // end if

        return true;
    }
// goes through the enemyList and removes every single enemy
    private void deleteSledge() {
        // Iterate over the enemyList to find and remove the sledge
        for (int i = 0; i < enemyList.size(); i++) {
            Enemy enemy = enemyList.get(i);
            if (enemy.getBitmap() == enemybpm) {
                enemyList.remove(i);
                //break; // Exit loop once sledge is removed
            }
        }
        Log.d("delSle", "Deleted Sledge!!");
    }
    // functions for the setTouchInputs required down here.
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        gameLoopThread.setRunning(true);
        gameLoopThread.start();
    }
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {}
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {}
}