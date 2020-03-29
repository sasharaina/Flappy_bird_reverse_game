package com.example.flappy_bird_reverse_game;

import android.os.AsyncTask;

public class BirdThread extends Thread {

    int ups = 0;

    @Override
    public void run() {
        while(true){
            MainActivity.draw_dird();
            if(MainActivity.bird_up) {
                MainActivity.bird_y -= 15;
                ups++;
                if (ups >= 3) {
                    ups = 0;
                    MainActivity.bird_up = false;
                }
            } else
                MainActivity.bird_y += 5;
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

}
