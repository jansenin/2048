package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.Serializable;
import java.util.HashMap;

public class Number {
    public int value,time_value;
    private static HashMap<Integer,Texture> textures = new HashMap<>();
    private float x,y,size_persent = 200;
    private int create_process,destruct_process,move_process,addition_process,v;
    private static final int PROCESS_TIME = 5;
    private boolean right,left,up,down,added;
    private boolean destroed;

    static {
        for (int i = 1;i <= 13;i++){
            textures.put((int)Math.pow(2,i),new Texture(((int)Math.pow(2,i)) + ".png"));
        }
    }

    public Number(int value,int x,int y){
        move_process = destruct_process = addition_process = -1;
        this.value = value;
        this.time_value = value;
        create_process = PROCESS_TIME;
        this.x = x;
        this.y = y;
        update();
    }

    public void setPosition(int x,int y){
        this.x = x;
        this.y = y;
    }

    public boolean isProcessesFinished(){
        return (create_process == -1) && (destruct_process == -1) && (move_process == -1) && (addition_process == -1);
    }

    public void render(SpriteBatch batch){
        batch.draw(textures.get(new Integer(value)),x + Field.cell_size / 2 - (Field.cell_size * size_persent / 100) / 2,y + Field.cell_size / 2 - (Field.cell_size * size_persent / 100) / 2,Field.cell_size * size_persent / 100,Field.cell_size * size_persent / 100);
        //System.out.println("x : " + (x + 154 / 2 - (154 * size_persent / 100) / 2) + "        y  : " + (y + 154 / 2 - (154 * size_persent / 100) / 2) + "       value : " + value + "      size_percent : " + size_persent);
    }

    public void update(){
        if (create_process > -1){
            size_persent = (PROCESS_TIME - create_process) * 100 / PROCESS_TIME;
        }
        /*else if (destruct_process > -1){
            size_persent = destruct_process * 100 / PROCESS_TIME;
        }*/
        if (addition_process > -1){
            size_persent = (PROCESS_TIME + (addition_process)) * 100 / PROCESS_TIME;
        }


        //System.out.println("create_process : " + create_process + "     destryction_process : " + destruct_process + "      addition_process : " + addition_process);

        if (move_process > 0){
            if (right){
                x += ((Field.cell_size + Field.pixels_beetwen_cells) / PROCESS_TIME) * v;
            }
            else if (left){
                x -= ((Field.cell_size + Field.pixels_beetwen_cells) / PROCESS_TIME) * v;
            }
            else if (up){
                y += ((Field.cell_size + Field.pixels_beetwen_cells) / PROCESS_TIME) * v;
            }
            else if (down){
                y -= ((Field.cell_size + Field.pixels_beetwen_cells) / PROCESS_TIME) * v;
            }
        }


        if (destruct_process == 0) destroed = true;
        if (create_process > -1) create_process--;
        if (destruct_process > -1) destruct_process--;
        if (move_process > -1) move_process--;
        if (addition_process > -1) addition_process--;
        if (move_process == -1) v = 0;
        if (addition_process == -1) added = false;
        //size_persent = 100;
    }

    public int getValue(){
        return value;
    }

    public int getMove_process(){ return move_process; }

    public int getTime_value(){ return time_value; }

    public void destroy(){
        destruct_process = PROCESS_TIME * 2;
    }

    private void move(){
        move_process = PROCESS_TIME;
        left = right = up = down = false;
    }

    public void moveright(int v){
        move();
        right = true;
        this.v += v;
    }

    public void moveleft(int v){
        move();
        left = true;
        this.v += v;
    }

    public void movedown(int v){
        move();
        down = true;
        this.v += v;
    }

    public void moveup(int v){
        move();
        up = true;
        this.v += v;
    }

    public boolean isItDestroid(){
        return destroed;
    }

    public void add(){
        addition_process = PROCESS_TIME;
        time_value *= 2;
        value *= 2;
        added = true;
    }

    public boolean isItAdded(){
        return added;
    }

    public boolean isItDestroing(){
        return destruct_process != 0;
    }
}
