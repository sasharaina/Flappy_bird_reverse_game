package com.example.flappy_bird_reverse_game;

import android.os.AsyncTask;

public class BirdThread extends Thread {

    @Override
    public void run() {
        while(true){
            MainActivity.draw_dird();
            MainActivity.bird_y += 5;
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

}
