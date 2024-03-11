package com.example.rainbow_six_battles;

//  Enter package name here

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Sprite {
    private Engine p_engine;
    private Canvas p_canvas;
    private Texture p_texture;
    private Paint p_paint;
    public Point position;

    public Sprite(Engine engine) {
        p_engine = engine;
        p_canvas = null;
        p_texture = new Texture(engine);
        p_paint = new Paint();
        p_paint.setColor(Color.WHITE);
        position = new Point(0,0);
    }

    public void draw() {
        p_canvas = p_engine.getCanvas();
        p_canvas.drawBitmap(p_texture.getBitmap(), position.x,
                position.y, p_paint);
    }

    public void draw(Rect dst){
        p_canvas = p_engine.getCanvas();
        p_canvas.drawBitmap(p_texture.getBitmap(), null,
                dst, p_paint);
    }

    /**
     * Color manipulation methods
     */
    public void setColor(int color) {
        p_paint.setColor(color);
    }

    public void setPaint(Paint paint) {
        p_paint = paint;
    }

    /**
     * common get/set methods
     */
    public void setTexture(Texture texture) {
        p_texture = texture;
    }

    public Texture getTexture() {
        return p_texture;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

}
