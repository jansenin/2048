package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Button {
    //Текстура кнопки
    public Texture texture1;

    //Текстура кнопки при нажатии
    public Texture texture2;

    //Позиция кнопки
    public Vector2 position;

    //Вектор движения кнопки
    public Vector2 velosity = new Vector2(0,0);

    ///Увеличиваеться ли кнопка в размерах
    protected boolean is_grow = false;

    //Движеться ли кнопка
    protected boolean is_move = false;

    //Активна ли кнопка
    public boolean active = true;

    //Видно ли кнопку
    public boolean visible = true;

    private boolean f = true;

    //нажата ли кнопка
    public boolean is_button_pressed = false;

    //Ширина кнопки
    public float width;

    //Высота кнопки
    public float height;

    //Увеличение кнопки по горизонтали
    public float width_grow = 0;

    //Увеличение кнопки по вертикали
    public float height_grow = 0;

    //какую картинку сейчас рисовать
    private boolean now_is_texture1 = true;

    private boolean is_last_just_pressed_were_on_the_button = false;

    public Button(String texture1, String texture2, Vector2 position) {
        this.texture1 = new Texture(texture1);
        this.texture2 = new Texture(texture2);
        this.width = this.texture1.getWidth();
        this.height = this.texture2.getHeight();
        this.position = new Vector2(position);
    }

    public Button(String texture1, String texture2, int x,int y) {
        this.texture1 = new Texture(texture1);
        this.texture2 = new Texture(texture2);
        this.width = this.texture1.getWidth();
        this.height = this.texture2.getHeight();
        this.position = new Vector2(x,y);
    }

    public Button(Vector2 position, float width, float height) {
        this.position = new Vector2(position);
        this.width = width;
        this.height = height;
    }

    public void setImages(String texture1,String texture2){
        this.texture1 = new Texture(texture1);
        this.texture2 = new Texture(texture2);
    }

    public void setPosition(float x,float y){
        this.position.x = x;
        this.position.y = y;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setVelosity(Vector2 velosity) {
        this.velosity = velosity;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setVelosity(float x,float y){
        this.velosity.x = x;
        this.velosity.y = y;
    }

    public void move(){
        is_move = true;
    }

    public void stopMove(){
        is_move = false;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
    }

    public void show(){
        visible = true;
    }

    public void hide(){
        visible = false;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public void activate(){
        active = true;
    }

    public void deactivate(){
        active = false;
    }

    public void setSize(float width,float height){
        this.width = width;
        this.height = height;
    }

    public void setSizeGrow(float width_grow,float height_grow){
        this.width_grow = width_grow;
        this.height_grow = height_grow;
    }

    public void setWidthGrow(float width_grow){
        this.width_grow = width_grow;
    }

    public void setHeightGrow(float height_grow){
        this.height_grow = height_grow;
    }

    public void grow(){
        is_grow = true;
    }

    public void stopGrow(){
        is_grow = false;
    }

    public boolean is_pressed(){
        return is_button_pressed;
    }

    public void update(){
        if (active) {

            if (is_button_pressed) is_button_pressed = false;
            if(is_move) {
                position.add(velosity);
            }

            if(is_grow){
                width += width_grow;
                height += height_grow;
                position.x -= width_grow / 2;
                position.y -= height_grow / 2;
            }

            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && Gdx.input.justTouched() && position.x < Gdx.input.getX() && position.x + width > Gdx.input.getX() && position.y < (Gdx.graphics.getHeight()-Gdx.input.getY()) && position.y + height > (Gdx.graphics.getHeight()-Gdx.input.getY()))
                is_last_just_pressed_were_on_the_button = true;
            //********************************************

            if (now_is_texture1 && !Gdx.input.isTouched() && !f){
                is_button_pressed = true;
            }
            else {
                is_button_pressed = false;
                f = false;
            }

            //********************************************

            if (is_last_just_pressed_were_on_the_button && position.x < Gdx.input.getX() && position.x + width > Gdx.input.getX() && position.y < (Gdx.graphics.getHeight()-Gdx.input.getY()) && position.y + height > (Gdx.graphics.getHeight()-Gdx.input.getY()))
                now_is_texture1 = true;
            else now_is_texture1 = false;

            if (!Gdx.input.isTouched()) is_last_just_pressed_were_on_the_button = false;
        }
    }

    public void render(SpriteBatch batch){
        update();
        if (visible){
            if (now_is_texture1){
                batch.draw(texture2,position.x,position.y,width,height);
            }
            else{
                batch.draw(texture1,position.x,position.y,width,height);
            }
        }
    }
}
