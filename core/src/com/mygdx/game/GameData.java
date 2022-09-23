package com.mygdx.game;
import com.badlogic.gdx.Gdx;

import java.io.*;

public class GameData implements Serializable {
    private int[][] numbers = new int[4][4];
    private int score,high_store;
    private boolean click_on_win,is_volume_on,lose;

    public GameData(){
        numbers = new int[4][4];
    }

    public void save_data(Main main){
        GameData gameData = new GameData();
        for (int i = 0;i <= 3;i++) {
            for (int j = 0;j <= 3;j++) {
                if (!(main.field.numbers[i][j] == null)) gameData.numbers[i][j] = main.field.numbers[i][j].getTime_value();
                else gameData.numbers[i][j] = -1;
            }
        }
        gameData.score = main.field.getScore();
        gameData.high_store = main.high_store;
        gameData.click_on_win = main.click_on_win;
        gameData.is_volume_on = Main.is_volume_on;
        gameData.lose = main.lose;
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("data.dat"));
            objectOutputStream.writeObject(gameData);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean load_data(Main main){
        if (Gdx.files.internal("data.dat").exists()) {
            GameData gameData;
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("data.dat"));
                gameData = (GameData) objectInputStream.readObject();
                objectInputStream.close();
                for (int i = 0;i <= 3;i++) {
                    for (int j = 0;j <= 3;j++) {
                        if (gameData.numbers[i][j] != -1){
                            main.field.addNumberToField(j,i,gameData.numbers[i][j]);
                        }
                    }
                }
                main.field.setScore(gameData.score);
                main.high_store = gameData.high_store;
                main.click_on_win = gameData.click_on_win;
                Main.is_volume_on = gameData.is_volume_on;
                main.lose = gameData.lose;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
