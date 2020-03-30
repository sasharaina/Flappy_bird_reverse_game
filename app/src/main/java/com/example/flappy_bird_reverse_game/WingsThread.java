package com.example.flappy_bird_reverse_game;

import android.os.AsyncTask;

public class WingsThread extends Thread {

    @Override
    public void run() {
        while(true){
           // MainActivity.draw_dird();
            MainActivity.wings_up = !MainActivity.wings_up;
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }
}
