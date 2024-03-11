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

public class MainActivity extends Engine{
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
        moleImage = null;
        maskImage = null;
        mask = null;
        mole = null;
        molePos = new Rect[7];
        maskPos = new Rect[7];
        random = new Random(); //monday

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


        //setContentView(R.layout.activity_main);
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

        molePos[0] = new Rect();
        molePos[0].top = (int)(466 * scaleH);
        molePos[0].left = (int)(70 * scaleW);
        molePos[0].bottom = molePos[0].top + MOLE_HEIGHT;
        molePos[0].right = molePos[0].left + MOLE_WIDTH;

        molePos[1] = new Rect();
        molePos[1].top = (int)(417 * scaleH);
        molePos[1].left = (int)(170 * scaleW);
        molePos[1].bottom = molePos[1].top + MOLE_HEIGHT;
        molePos[1].right = molePos[1].left + MOLE_WIDTH;

        molePos[2] = new Rect();
        molePos[2].top = (int)(466 * scaleH);
        molePos[2].left = (int)(270 * scaleW);
        molePos[2].bottom = molePos[2].top + MOLE_HEIGHT;
        molePos[2].right = molePos[2].left + MOLE_WIDTH;

        molePos[3] = new Rect();
        molePos[3].top = (int)(417 * scaleH);
        molePos[3].left = (int)(370 * scaleW);
        molePos[3].bottom = molePos[3].top + MOLE_HEIGHT;
        molePos[3].right = molePos[3].left + MOLE_WIDTH;

        molePos[4] = new Rect();
        molePos[4].top = (int)(466 * scaleH);
        molePos[4].left = (int)(475 * scaleW);
        molePos[4].bottom = molePos[4].top + MOLE_HEIGHT;
        molePos[4].right = molePos[4].left + MOLE_WIDTH;

        molePos[5] = new Rect();
        molePos[5].top = (int)(417 * scaleH);
        molePos[5].left = (int)(575 * scaleW);
        molePos[5].bottom = molePos[5].top + MOLE_HEIGHT;
        molePos[5].right = molePos[5].left + MOLE_WIDTH;

        molePos[6] = new Rect();
        molePos[6].top = (int)(466 * scaleH);
        molePos[6].left = (int)(475 * scaleW);
        molePos[6].bottom = molePos[6].top + MOLE_HEIGHT;
        molePos[6].right = molePos[6].left + MOLE_WIDTH;



        // positions for each of the maskpos
        createMaskPos(0, 0, 0);
        createMaskPos(1, 150, 403);
        createMaskPos(2, 250, 453);
        createMaskPos(3, 350, 403);
        createMaskPos(4, 455, 453);
        createMaskPos(5, 555, 403);
        createMaskPos(6, 655, 453);
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

        if (!backgroundImage.loadFromAsset("images/background.png")) {
            fatalError("Error Loading Background Image");
        }

        background.setTexture(backgroundImage);
        background.position = new Point(0, 0);

        mole = new Sprite(this);
        moleImage = new Texture(this);

        if (!moleImage.loadFromAsset("images/mole.png")) {
            fatalError("Error Loading Mole Image");
        }

        mole.setTexture(moleImage);
        mole.position = new Point(0, 0);

        mask = new Sprite(this);
        maskImage = new Texture(this);


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

            molePos[curMole].top += moleSpeed;
            molePos[curMole].bottom += moleSpeed;

            /*mole.draw(molePos[0]);
            mole.draw(molePos[1]);
            mole.draw(molePos[2]);
            mole.draw(molePos[3]);
            mole.draw(molePos[4]);
            mole.draw(molePos[5]);
            mole.draw(molePos[6]);
            */

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

                        playSound(1);
                        moleHit = true;
                        break;
                    }

                }

            } else {
                titleMode = false;
            }
        }
    }

    private void playSound(int soundId) {
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
    }

    public void createMaskPos(int index, int x, int y) {
        float scaleW = (float) screenWidth / imageW;
        float scaleH = (float) screenHeight / imageH;

        maskPos[index] = new Rect();
        maskPos[index].top = (int)(y * scaleH);
        maskPos[index].left = (int)(x * scaleW);
        maskPos[index].bottom = maskPos[index].top + MASK_HEIGHT;
        maskPos[index].right = maskPos[index].left + MASK_WIDTH;

    }

}