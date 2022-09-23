package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class Field {
    public Number[][] numbers = new Number[4][4];
    private Number[] destroyed_numbers = new Number[16];
    private static Random random = new Random();
    private int lastJustPressX = -10000,lastJustPressY = - 10000,score,destroyed_index;
    private int x,y;
    private boolean need_to_add_number = true;
    public static final int cell_size = 100;
    public static final int pixels_beetwen_cells = 20;



    public void setNumbers(Number[][] numbers) {
        this.numbers = numbers;
    }

    public void setDestroyed_numbers(Number[] destroyed_numbers) {
        this.destroyed_numbers = destroyed_numbers;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setDestroyed_index(int destroyed_index) {
        this.destroyed_index = destroyed_index;
    }

    public void setNeed_to_add_number(boolean need_to_add_number) {
        this.need_to_add_number = need_to_add_number;
    }

    public Number[][] getNumbers() {
        return numbers;
    }

    public Number[] getDestroyed_numbers() {
        return destroyed_numbers;
    }

    public int getDestroyed_index() {
        return destroyed_index;
    }

    public boolean isNeed_to_add_number() {
        return need_to_add_number;
    }

    public void restart(){
        for (int i = 0;i <= 3;i++){
            for (int j = 0;j <= 3;j++){
                numbers[i][j] = null;
            }
        }
        for (int i = 0;i <= 15;i++){
            destroyed_numbers[i] = null;
        }
        lastJustPressY = lastJustPressX = -10000;
        need_to_add_number = true;
        score = 0;
        destroyed_index = 0;
        addNumberToField();
    }

    public boolean is_there_2048(){
        for (int i = 0;i <= 3;i++){
            for (int j = 0;j <= 3;j++){
                if (!(numbers[i][j] == null)) {
                    if (numbers[i][j].getValue() == 2048) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Field(int x,int y){
        this.x = x;
        this.y = y;
    }

    public int getScore(){
        return score;
    }

    public boolean is_game_over(){
        for (int i = 0; i <= 3;i++){
            for (int j = 0;j <= 3;j++){
                if (numbers[i][j] == null) return false;
            }
        }
        for (int i = 0;i <= 3;i++){
            for (int j = 0;j <= 3;j++){
                if (i - 1 >= 0) if ((numbers[i][j].getTime_value() == numbers[i - 1][j].getTime_value()) && !(numbers[i][j].getTime_value() == 8192)) return false;
                if (i + 1 <= 3) if ((numbers[i][j].getTime_value() == numbers[i + 1][j].getTime_value()) && !(numbers[i][j].getTime_value() == 8192)) return false;
                if (j - 1 >= 0) if ((numbers[i][j].getTime_value() == numbers[i][j - 1].getTime_value()) && !(numbers[i][j].getTime_value() == 8192)) return false;
                if (j + 1 <= 3) if ((numbers[i][j].getTime_value() == numbers[i][j + 1].getTime_value()) && !(numbers[i][j].getTime_value() == 8192)) return false;
            }
        }
        return true;
    }

    public void addNumberToField(){
        if (need_to_add_number) {
            int x, y;
            boolean fl = true;
            while (fl) {
                x = random.nextInt(4);
                y = random.nextInt(4);
                if (numbers[y][x] == null) {
                    fl = false;
                    if (random.nextInt(4) == 0) {
                        numbers[y][x] = new Number(1024, this.x + (cell_size + pixels_beetwen_cells) * x, this.y + (cell_size + pixels_beetwen_cells) * y);
                    } else {
                        numbers[y][x] = new Number(512, this.x + (cell_size + pixels_beetwen_cells) * x, this.y + (cell_size + pixels_beetwen_cells) * y);
                    }
                }
            }
            need_to_add_number = false;
        }
    }

    public void addNumberToField(int x,int y,int value){
        numbers[y][x] = new Number(value, this.x + (cell_size + pixels_beetwen_cells) * x, this.y + (cell_size + pixels_beetwen_cells) * y);
    }

    public void render(SpriteBatch batch){
        for (int i = 0;i <= 15;i++){
            if (!(destroyed_numbers[i] == null)){
                destroyed_numbers[i].render(batch);
            }
        }
        for (int i = 0;i <= 3;i++){
            for (int j = 0;j <= 3;j++){
                if (numbers[i][j] != null){
                    numbers[i][j].render(batch);
                }
            }
        }
    }

    public void update(){
        if (Gdx.input.justTouched() && Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            lastJustPressX = Gdx.input.getX();
            lastJustPressY = Gdx.graphics.getHeight() - Gdx.input.getY();
        }

        for (int i = 0;i <= 3;i++){
            for (int j = 0;j <= 3;j++){
                if (numbers[i][j] != null){
                    numbers[i][j].update();
                    if (numbers[i][j].getMove_process() == -1){
                        numbers[i][j].setPosition(this.x + (cell_size + pixels_beetwen_cells) * j,this.y + (cell_size + pixels_beetwen_cells) * i);
                    }
                }
            }
        }

        for (int i = 0;i <= 15;i++){
            if (!(destroyed_numbers[i] == null)){
                destroyed_numbers[i].update();
                if (destroyed_numbers[i].isItDestroid()) destroyed_numbers[i] = null;
            }
        }



        boolean processesFinished = true;

        for (int i = 0;i <= 3;i++){
            for (int j = 0;j <= 3;j++){
                if (numbers[i][j] != null) {
                    if (!numbers[i][j].isProcessesFinished()) processesFinished = false;
                }
            }
        }

        if (!Gdx.input.isTouched() && processesFinished){
            if (!(lastJustPressX == -10000) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN)
                    || Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)
                    || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.A)
                    || Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.D)
                    || Gdx.input.isKeyJustPressed(Input.Keys.S)){
                int deltax,deltay;
                destroyed_index = 0;
                deltax = Gdx.input.getX() - lastJustPressX;
                deltay = (Gdx.graphics.getHeight() - Gdx.input.getY()) - lastJustPressY;

                if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)){
                    deltax = 50;
                    deltay = 0;
                }else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)){
                    deltax = -50;
                    deltay = 0;
                }else if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)){
                    deltax = 0;
                    deltay = 50;
                }else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)){
                    deltax = 0;
                    deltay = -50;
                }

                if (Math.abs(deltax) > 30 || Math.abs(deltay) > 30){
                    boolean is_move = false;
                    boolean is_add = false;
                    if (Math.abs(deltax) > Math.abs(deltay)){
                        if (deltax > 0){
                            //right
                            for (int i = 0;i <= 3;i++) {
                                for (int ij = 0; ij <= 3; ij++) {
                                    for (int j = 2; j >= 0; j--) {
                                        if (!(numbers[i][j] == null)) {
                                            if (numbers[i][j + 1] == null) {
                                                numbers[i][j].moveright(1);
                                                numbers[i][j + 1] = numbers[i][j];
                                                numbers[i][j] = null;
                                                need_to_add_number = true;
                                                is_move = true;
                                            } else {
                                                if ((numbers[i][j].getTime_value() == numbers[i][j + 1].getTime_value()) && !(numbers[i][j].isItAdded()) && !(numbers[i][j + 1].isItAdded()) && !(numbers[i][j].getTime_value() == 8192)) {
                                                    numbers[i][j].destroy();
                                                    numbers[i][j].moveright(1);
                                                    destroyed_numbers[destroyed_index] = numbers[i][j];
                                                    destroyed_index += 1;
                                                    numbers[i][j] = null;
                                                    numbers[i][j + 1].add();
                                                    score += numbers[i][j + 1].getTime_value();
                                                    need_to_add_number = true;
                                                    is_move = true;
                                                    is_add = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            //left
                            for (int i = 0;i <= 3;i++) {
                                for (int ij = 0; ij <= 3; ij++) {
                                    for (int j = 1; j <= 3; j++) {
                                        if (!(numbers[i][j] == null)) {
                                            if (numbers[i][j - 1] == null) {
                                                numbers[i][j - 1] = numbers[i][j];
                                                numbers[i][j - 1].moveleft(1);
                                                numbers[i][j] = null;
                                                need_to_add_number = true;
                                                is_move = true;
                                            } else {
                                                if ((numbers[i][j].getTime_value() == numbers[i][j - 1].getTime_value()) && !(numbers[i][j].isItAdded()) && !(numbers[i][j - 1].isItAdded()) && !(numbers[i][j].getTime_value() == 8192)) {
                                                    numbers[i][j].destroy();
                                                    numbers[i][j].moveleft(1);
                                                    destroyed_numbers[destroyed_index] = numbers[i][j];
                                                    destroyed_index += 1;
                                                    numbers[i][j] = null;
                                                    numbers[i][j - 1].add();
                                                    score += numbers[i][j - 1].getTime_value();
                                                    need_to_add_number = true;
                                                    is_move = true;
                                                    is_add = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else {
                        if (deltay < 0){
                            //up
                            for (int i = 0;i <= 3;i++) {
                                for (int ij = 0; ij <= 3; ij++) {
                                    for (int j = 1; j <= 3; j++) {
                                        if (!(numbers[j][i] == null)) {
                                            if (numbers[j - 1][i] == null) {
                                                numbers[j - 1][i] = numbers[j][i];
                                                numbers[j - 1][i].movedown(1);
                                                numbers[j][i] = null;
                                                need_to_add_number = true;
                                                is_move = true;
                                            } else {
                                                if ((numbers[j][i].getTime_value() == numbers[j - 1][i].getTime_value()) && !(numbers[j][i].isItAdded()) && !(numbers[j - 1][i].isItAdded()) && !(numbers[j][i].getTime_value() == 8192)) {
                                                    numbers[j][i].destroy();
                                                    numbers[j][i].movedown(1);
                                                    destroyed_numbers[destroyed_index] = numbers[j][i];
                                                    destroyed_index += 1;
                                                    numbers[j][i] = null;
                                                    numbers[j - 1][i].add();
                                                    score += numbers[j - 1][i].getTime_value();
                                                    need_to_add_number = true;
                                                    is_move = true;
                                                    is_add = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            //down
                            for (int i = 0;i <= 3;i++) {
                                for (int ij = 0; ij <= 3; ij++) {
                                    for (int j = 2; j >= 0; j--) {
                                        if (!(numbers[j][i] == null)) {
                                            if (numbers[j + 1][i] == null) {
                                                numbers[j + 1][i] = numbers[j][i];
                                                numbers[j + 1][i].moveup(1);
                                                numbers[j][i] = null;
                                                need_to_add_number = true;
                                                is_move = true;
                                            } else {
                                                if ((numbers[j][i].getTime_value() == numbers[j + 1][i].getTime_value()) && !(numbers[j][i].isItAdded()) && !(numbers[j + 1][i].isItAdded()) && !(numbers[j][i].getTime_value() == 8192)) {
                                                    numbers[j][i].destroy();
                                                    numbers[j][i].moveup(1);
                                                    destroyed_numbers[destroyed_index] = numbers[j][i];
                                                    destroyed_index += 1;
                                                    numbers[j][i] = null;
                                                    numbers[j + 1][i].add();
                                                    score += numbers[j + 1][i].getTime_value();
                                                    need_to_add_number = true;
                                                    is_move = true;
                                                    is_add = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    addNumberToField();
                    if (Main.is_volume_on) {
                        if (is_move) {
                            Main.move.play(0.25f);
                        }
                        if (is_add) {
                            Main.add.play();
                        }
                    }
                }
                lastJustPressX = -10000;
                //System.out.println(score);
                /*System.out.println(' ');
                System.out.println(' ');
                for (int i = 3;i >= 0;i--){
                    System.out.println(' ');
                    for (int j = 0;j <= 3;j++){
                        if (!(numbers[i][j] == null)){
                            System.out.print(numbers[i][j].getTime_value());
                        }
                        else{
                            System.out.print('*');
                        }
                    }
                }*/
            }
        }
    }
}

