package com.example.rainbow_six_battles;

import android.graphics.Canvas;
import android.graphics.Color;
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

    int screenWidth;
    int screenHeight;

    Random random;

    Sprite title;
    Texture titleImage;
    Sprite characters;
    Texture charactersImg;
    Sprite level;
    Texture levelImg;

    Canvas canvas;
    Paint paint;
    Rect rect;

    boolean titleMode = true;   //default screen
    private boolean characterMode;
    private boolean levelMode;

    Point touch;

    //Touch point rects
    Rect ashSelect;
    Rect buckSelect;
    Rect oryxSelect;

    //Sound stuff
    SoundPool soundPool = null;
    HashMap<Integer, Integer> soundPoolMap;

    public MainActivity() {
        title = null;
        characters = null;
        charactersImg = null;
        canvas = null;
        paint = new Paint();
        touch = new Point(0, 0);
        //images = null;
        random = new Random();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new GameView(this));

        //SETS UP FRAMING for images
        rect = new Rect();
        rect.left = 0;
        rect.top = 0;
        rect.right = getScreenWidth();
        rect.bottom = getScreenHeight();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundPoolMap = new HashMap<Integer, Integer>();
        //soundPoolMap.put(0, soundPool.load(this, R.raw.thememusic, 1));

    }

    @Override
    public void init() {
        screenWidth = getScreenWidth();
        screenHeight = getScreenHeight();
    }

    @Override
    public void load() {
        Log.d("Game", "Game.load");

        //TITLE
        title = new Sprite(this);
        titleImage = new Texture(this);

        if (!titleImage.loadFromAsset("GamePics/LoadingScreens/tapToStart.png")) {
            fatalError("Error Loading Title Image");
        }

        title.setTexture(titleImage);
        title.position = new Point(0, 0);


        //old background
        characters = new Sprite(this);
        charactersImg = new Texture(this);

        if (!charactersImg.loadFromAsset("GamePics/LoadingScreens/pickCharacter.png")) {
            fatalError("Error Loading Background Image");
        }

        characters.setTexture(charactersImg);
        characters.position = new Point(0, 0);


        //CHARACTER
        level = new Sprite(this);
        levelImg = new Texture(this);

        //Directory for images inside of Assets folder
        if (!levelImg.loadFromAsset("GamePics/GameLevels/level1.png")) {
            fatalError("Error Loading Background Image");
        }

        level.setTexture(levelImg);
        level.position = new Point(0, 0);
    }

    @Override
    public void draw() {
        canvas = getCanvas();
        paint = new Paint(Color.RED);

        if (titleMode) {
            title.draw(rect);
        } else if (characterMode) {
            characters.draw(rect);  //insert new var that will do character select mode

            ashSelect = new Rect(400,260,800, 900);  //ash touch-zone rect
            buckSelect = new Rect(950,260,1350, 900);  //buck touch-zone rect
            oryxSelect = new Rect(1450,260,1900, 900);  //Oryx touch-zone rect
        } else if (levelMode){
            level.draw(rect);
        }
    }

    @Override
    public void update() {
        if (getTouchInputs() > 0) {
            touch = getTouchPoint(0);
            if (titleMode) {
                titleMode = false;
                characterMode = true;

            } else if(characterMode) {
                int x = touch.x;
                int y = touch.y;

                if (ashSelect.contains(x, y)) {  // if else if

                    /* Trigger your action here */

                    //setContentView(new GameView(this));
                    Log.d("Character mode", "Selected ash char");
                } else if (buckSelect.contains(x,y)){
                    //setContentView(new GameView(this));
                    Log.d("Character mode", "Selected buck char");
                } else if (oryxSelect.contains(x,y)){
                    //setContentView(new GameView(this));
                    Log.d("Character mode", "Selected ORYX CHAR");
                }
                //add in click code then new GameView
            }



        }

    }


    private void playSound(int soundId) {
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
    }

}