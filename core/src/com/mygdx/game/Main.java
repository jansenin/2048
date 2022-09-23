package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends ApplicationAdapter {
	SpriteBatch batch;
	Texture header,header_2048,texture,win,lose_img,field_img;
	Field field;
	Button restsrt_button,volume_button;
	BitmapFont font27,font40;
	int high_store = 0;
	boolean click_on_win = false;
	static boolean is_volume_on = true;
	static Sound move,add;
	int timer = 0;
	boolean lose = false;
	GameData gameData;
	@Override
	public void create () {
		try {
			gameData = new GameData();
			move = Gdx.audio.newSound(Gdx.files.internal("move-2.mp3"));
			add = Gdx.audio.newSound(Gdx.files.internal("ad2.mp3"));
			restsrt_button = new Button("restart button_1.png","restart button_2.png",Gdx.graphics.getWidth() - (10 + 80),Gdx.graphics.getHeight() - 95 + 8);
			volume_button = new Button("volume button_1.png","volume button_2.png",10,750 - ( 95 - (95 - 49) / 2));
			win = new Texture("win.png");
			lose_img = new Texture("lose.png");
			field_img = new Texture("field.png");
			batch = new SpriteBatch();
			header = new Texture("header.png");
			header_2048 = new Texture("2048_header.png");
			field = new Field(Gdx.graphics.getWidth() / 2 - (Field.cell_size * 4 + Field.pixels_beetwen_cells * 3) / 2,
					          20);//Gdx.graphics.getHeight() / 2 - (Field.cell_size * 4 + Field.pixels_beetwen_cells * 3) / 2);
			font27 = new BitmapFont(Gdx.files.internal("font27.fnt"));
			font40 = new BitmapFont(Gdx.files.internal("font40.fnt"));
			if (!gameData.load_data(this)){
				field.addNumberToField();
			}
			if (!is_volume_on) {
				texture = volume_button.texture1;
				volume_button.texture1 = volume_button.texture2;
				volume_button.texture2 = texture;
			}
			//font20 = new BitmapFont(Gdx.files.external("font20.fnt"));
		/*
		width = 154 * 4 + 5*3 + 10 * 2
		height = (restart_button + 10 + 2048 * 2) * 2 + 154 * 4 + 5 * 3
		 */
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void render () {
		try {
			boolean b = false;
			if (!field.is_there_2048() || (click_on_win && field.is_there_2048())) {
				field.update();
				restsrt_button.update();
				volume_button.update();
				if (volume_button.is_button_pressed) {
					is_volume_on = !is_volume_on;
					texture = volume_button.texture1;
					volume_button.texture1 = volume_button.texture2;
					volume_button.texture2 = texture;
				}
				if (field.getScore() > high_store) high_store = field.getScore();
				if (restsrt_button.is_button_pressed || Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.R)) {
					field.restart();
					click_on_win = false;
					b = false;
					lose = false;
				}
			}
			else {
				b = true;
				if (timer == 0) timer = 120;
			}
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();
			batch.draw(field_img,0,0);
			field.render(batch);
			batch.draw(header, 0, Gdx.graphics.getHeight() - header.getHeight());
			batch.draw(header_2048,126,Gdx.graphics.getHeight() - (header.getHeight() + header_2048.getHeight() + 40));
			restsrt_button.render(batch);
			volume_button.render(batch);
			int k = 1;
			if (high_store > 9) k = 2;
			if (high_store > 99) k = 3;
			if (high_store > 999) k = 4;
			if (high_store > 9999) k = 5;
			if (high_store > 99999) k = 5;
			font27.draw(batch,"High Store - " + high_store,161 - k * 6,730);
			k = 1;
			if (field.getScore() > 9) k = 2;
			if (field.getScore() > 99) k = 3;
			if (field.getScore() > 999) k = 4;
			if (field.getScore() > 9999) k = 5;
			if (field.getScore() > 99999) k = 5;
			font40.draw(batch,"Store - " + field.getScore(),(int)(172 - k * 8.6),700);
			if (lose) batch.draw(lose_img,50,325);
			if (b) batch.draw(win,50,325);
			batch.end();
			if (b){
				if (timer > 1) timer -= 1;
				if ((Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT) || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.ANY_KEY)) && (timer == 1)){
					click_on_win = true;
					timer = 0;
				}
			}
			else if (field.is_game_over()){
				lose = true;
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void dispose () {
		gameData.save_data(this);
		batch.dispose();
		header.dispose();
	}
}
