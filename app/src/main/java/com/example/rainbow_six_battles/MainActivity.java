/*
 - Names: Major Andrews, Oliver Carranza, Major Andrews
 - Using GitHub Repository to work collaboratively and share code, images, media, and files with each other.
 - Main Activity Java file that starts the game and guides the user into the start of gameplay levels.
 - This code and documentation meet the requirements for this Mobile Final Project.
 */


package com.example.rainbow_six_battles;

// Imports
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
    // Global (class) variables

    //Width of the phone screen that auto adjusts below
    int screenWidth;
    //Height of the phone screen that auto adjusts below
    int screenHeight;

    //random number generator
    Random random;

    //Sprite title created to be used as a Sprite for the title screen
    Sprite title;
    //Sprite title image for starting screen
    Texture titleImage;

    //Character sprite created to be used as a Sprite for the character level select screen
    Sprite characters;
    //Character screen
    Texture charactersImg;

    //Sprite level to be temporary use for level debug
    Sprite level;
    Texture levelImg;

    //Canvas for rect and paint for color
    Canvas canvas;
    Paint paint;

    //Rect for framing image and sizing of the phone screen
    Rect rect;

    //Booleans
    //title mode to start as default loading screen
    boolean titleMode = true;   //default screen
    //character mode to come into as the character/level selection screen
    private boolean characterMode;
    //debug level mode that may not be utilized
    private boolean levelMode;

    // touch point to grab X and Y location of a touch/click input
    Point touch;

    //Touch point Rects (transparent touch zones)
    // Each have their own framing below
    Rect ashSelect;
    Rect buckSelect;
    Rect oryxSelect;


    //Sound stuff amy not be utilzed due to bugs in the end
    SoundPool soundPool = null;
    HashMap<Integer, Integer> soundPoolMap;

    //Main Activity Constructor method that initializes most of the variables created above
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

    //On Create method that overrides the super class
    //creates the rect framing of the screen
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(new GameView(this));

        //SETS UP FRAMING for images
        rect = new Rect();

        rect.left = 0;
        rect.top = 0;
        //adjustable
        rect.right = getScreenWidth();  //width of the phone screen
        rect.bottom = getScreenHeight();    //height of the phone screen

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //sound pool audio portal to play music
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        soundPoolMap = new HashMap<Integer, Integer>();
        //soundPoolMap.put(0, soundPool.load(this, R.raw.thememusic, 1));

    }

    //Init function that overrides the super class to initialize variable and parameters of the program
    @Override
    public void init() {
        //variables are initialized
        screenWidth = getScreenWidth();
        screenHeight = getScreenHeight();
    }

    //Load function that overides the super class call to loa din objects, variables, and media into the game.
    @Override
    public void load() {
        Log.d("Game", "Game.load"); //any log.d is like a console.log statement

        //TITLE screen/ loading screen sprite init
        title = new Sprite(this);
        //image creation for titleImage as a Texture object
        titleImage = new Texture(this);

        //load in image from assets folder style
        if (!titleImage.loadFromAsset("GamePics/LoadingScreens/tapToStart.png")) {
            fatalError("Error Loading Title Image");
        }

        //set sprite image for ease of object programming
        title.setTexture(titleImage);
        title.position = new Point(0, 0);


        //same process for the character select screen
        //sprite init
        characters = new Sprite(this);
        charactersImg = new Texture(this);

        if (!charactersImg.loadFromAsset("GamePics/LoadingScreens/pickCharacter.png")) {
            fatalError("Error Loading Background Image");
        }

        //characters sprite gains its texture image background like other sprites above
        characters.setTexture(charactersImg);
        characters.position = new Point(0, 0);

        //level1 sprtie for debug purposes
        level = new Sprite(this);
        levelImg = new Texture(this);

        //Directory for images inside of Assets folder
        if (!levelImg.loadFromAsset("GamePics/GameLevels/level1.png")) {
            fatalError("Error Loading Background Image");
        }

        level.setTexture(levelImg);
        level.position = new Point(0, 0);
    }

    //Draw function to override super class call to draw things on the screen output
    @Override
    public void draw() {
        canvas = getCanvas();   //canvas to draw or template objects with paint
        paint = new Paint(Color.RED);

        //logic if-statement
        //default true is for this to start the tap to start loading screen
        if (titleMode) {
            //draw's loading screen
            title.draw(rect);
        } else if (characterMode) { //the on click will update below and the bool's will change for this to activate
            //sets selction level screen
            characters.draw(rect);  //new var that will do character select mode

            //transparent touch-zone Rects that have individualized framing after testing
            ashSelect = new Rect(400,260,800, 900);  //ash touch-zone rect
            buckSelect = new Rect(950,260,1350, 900);  //buck touch-zone rect
            oryxSelect = new Rect(1450,260,1900, 900);  //Oryx touch-zone rect
        } else if (levelMode){
            //debug purposes
            level.draw(rect);
        }
    }

    //update method to override the super class call
    //mainly prioritized to organize the touch inputs and to update th game based on touch
    @Override
    public void update() {
        //grabbing all touch inputs on the screen via the Engine
        if (getTouchInputs() > 0) {
            touch = getTouchPoint(0);

            //after the first touch, the screen will switch here to the level select screen
            if (titleMode) {
                titleMode = false;
                characterMode = true;

                // in the level character select screen touch is pinpointed in order to find what level the user selected
            } else if(characterMode) {
                //specific touch point
                int x = touch.x;
                int y = touch.y;

                //if statement below to check all rectangles to see what level the user touched
                if (ashSelect.contains(x, y)) {  // if else if
                    //the thread will only properly activate with this runnable code in each statement below
                    runOnUiThread(new Runnable() {
                        public void run() {
                            setContentView(new GameView(MainActivity.this));    //call to new gameview1
                        }
                    });
                    Log.d("Character mode", "Not CALLING GAME VIEW ash char");
                } else if (buckSelect.contains(x, y)) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            setContentView(new GameView(MainActivity.this));    //call to new gameview2
                        }
                    });
                    Log.d("Character mode", "Selected buck character");
                } else if (oryxSelect.contains(x, y)) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            setContentView(new GameView(MainActivity.this));    //call to new gameview3
                        }
                    });
                    Log.d("Character mode", "Selected oryx CHARACTER");
                }
                //add in click code then new GameView
            }



        }
        //Log.d("clickTag", "CLICKED STATUS 2: " + clicked);
    }

    //Sound function not utilized as of right now
    private void playSound(int soundId) {
        soundPool.play(soundId, 1, 1, 1, 0, 1);
    }

}