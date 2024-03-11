package com.example.rainbow_six_battles;

import static android.media.AudioManager.STREAM_MUSIC;

import  androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Random;

public class MainActivity extends Engine {
    // member (class) variables
    public static final int imageW = 800;
    public static final int imageH = 600;
    public static final int MOLE_WIDTH = 88;
    public static final int MOLE_HEIGHT = 212;
    int screenWidth;
    int screenHeight;

    int MASK_WIDTH;
    int MASK_HEIGHT;

    Rect[] maskPos;
    //monday
    float scaleW;
    float scaleH;
    Random random;
    int curMole;
    int moleSpeed;
    boolean newMole = true;
    int initMoleHeight;
    //monday end

    boolean moleHit = false;

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
    Texture moleImage;
    Texture maskImage;
    Sprite mask;
    Sprite mole;

    Rect[] molePos;

    HashMap<Integer, Integer> soundPoolMap;


    public MainActivity() {
        title = null;
        background = null;
        backgroundImage = null;
        canvas = null;
        paint = new Paint();
        touch = new Point(0, 0);
        images = null;
        moleImage = null;
        maskImage = null;
        mask = null;
        mole = null;
        molePos = new Rect[7];
        maskPos = new Rect[7];
        random = new Random();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rect = new Rect();
        rect.left = 0;
        rect.top = 0;
        rect.right = getScreenWidth();
        rect.bottom = getScreenHeight();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @Override
    public void init() {
        screenWidth = getScreenWidth();
        screenHeight = getScreenHeight();

        //made global
        scaleW = (float) screenWidth / imageW;
        scaleH = (float) screenHeight / imageH;

        MASK_WIDTH = (122 * (int) scaleW);
        MASK_HEIGHT = (248 * (int) scaleH);

        moleSpeed = -20;
        initMoleHeight = 0;

    }

    @Override
    public void load() {
        Log.d("Game", "Game.load");

        title = new Sprite(this);
        titleImage = new Texture(this);

        if (!titleImage.loadFromAsset("GamePics/Start.jpg")) {
            fatalError("Error Loading Title Image");
        }

        title.setTexture(titleImage);
        title.position = new Point(0, 0);

        background = new Sprite(this);
        backgroundImage = new Texture(this);

        if (!backgroundImage.loadFromAsset("GamePics/Start.png")) {
            fatalError("Error Loading Background Image");
        }

        background.setTexture(backgroundImage);
        background.position = new Point(0, 0);

        mole = new Sprite(this);
        moleImage = new Texture(this);

        if (!moleImage.loadFromAsset("GamePics/Background.jpg")) {
            fatalError("Error Loading Mole Image");
        }

        mole.setTexture(moleImage);
        mole.position = new Point(0, 0);

        mask = new Sprite(this);
        maskImage = new Texture(this);

        if (!maskImage.loadFromAsset("GamePics/mask.png")) {
            fatalError("Error Loading Title Image");
        }

        mask.setTexture(maskImage);
        mask.position = new Point(0,0);


    }

    @Override
    public void draw() {
        canvas = getCanvas();

        if (!titleMode) {
            background.draw(rect);

            if (newMole) {
                curMole = random.nextInt(7);
                initMoleHeight = molePos[curMole].top;
                newMole = false;
            }
            if (molePos[curMole].top < initMoleHeight - MOLE_HEIGHT) {
                moleSpeed = -moleSpeed;
            }

            if (molePos[curMole].top > initMoleHeight){
                moleSpeed = -moleSpeed;
                newMole = true;
            }


            for (int i = 0; i < maskPos.length; i++) {
                mole.draw(molePos[i]);
                mask.draw(maskPos[i]);
            }
            if (moleHit) {
                Log.d("HIT", "Somewhere");
                moleHit = false;
            }

        } else {
            title.draw(rect);
        }
    }

    @Override
    public void update() {

        if (getTouchInputs() > 0) {
            touch = getTouchPoint(0);
            if (!titleMode) {

                //tuesday
                for (int i = 0; i < molePos.length; i++) {
                    if (molePos[i].contains(touch.x, touch.y) &&
                            !maskPos[i].contains(touch.x, touch.y)) {


                        moleHit = true;
                        break;
                    }

                }

            } else {
                titleMode = false;
            }
        }
    }

    }





