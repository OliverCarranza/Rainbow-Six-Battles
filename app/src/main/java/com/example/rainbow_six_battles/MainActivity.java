package com.example.rainbow_six_battles;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Random;

public class MainActivity extends Engine {
    // member (class) variables

    ImageView img;


    public static final int imageW = 800;
    public static final int imageH = 600;
    int screenWidth;
    int screenHeight;

    //monday
    float scaleW;
    float scaleH;
    Random random;

    Texture titleImage;
    Texture backgroundImage;
    Sprite title;

    Sprite background;
    Canvas canvas;
    Paint paint;
    Rect rect;

    boolean titleMode = true;
    Point touch;

    Texture images;

    Rect[] molePos;

    SoundPool soundPool = null; //wednesday
    HashMap<Integer, Integer> soundPoolMap;


    public MainActivity() {
        title = null;
        background = null;
        backgroundImage = null;
        canvas = null;
        paint = new Paint();
        touch = new Point(0, 0);
        images = null;
        random = new Random();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        rect = new Rect();
        rect.left = 0;
        rect.top = 0;
        rect.right = getScreenWidth(); // Engine methods
        rect.bottom = getScreenHeight();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Wednesday

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(0, soundPool.load(this, R.raw.thememusic, 1));


        //setContentView(R.layout.activity_main);
    }

    @Override
    public void init() {
        screenWidth = getScreenWidth();
        screenHeight = getScreenHeight();
    }

    @Override
    public void load() {
        title = new Sprite(this);
        Log.d("Game", "Game.load");

        titleImage = new Texture(this);

        if (!titleImage.loadFromAsset("GamePics/LoadingScreens/tapToStart.png")) {
            fatalError("Error Loading Title Image");
        }

        title.setTexture(titleImage);
        title.position = new Point(0, 0);

        background = new Sprite(this);
        backgroundImage = new Texture(this);

        //Directory for images inside of Assets folder
        if (!backgroundImage.loadFromAsset("GamePics/LoadingScreens/pickCharacter.png")) {
            fatalError("Error Loading Background Image");
        }

        background.setTexture(backgroundImage);
        background.position = new Point(0, 0);
    }

    @Override
    public void draw() {
        canvas = getCanvas();

        if (!titleMode) {
            background.draw(rect);

        } else {
            title.draw(rect);
        }
    }

    @Override
    public void update() {

        if (getTouchInputs() > 0) {
            touch = getTouchPoint(0);
            if (!titleMode) {

            } else {
                titleMode = false;
            }
        }
    }


    private void playSound(int soundId) {
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
    }

}