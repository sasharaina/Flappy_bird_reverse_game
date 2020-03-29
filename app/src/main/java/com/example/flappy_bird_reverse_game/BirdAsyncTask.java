package com.example.flappy_bird_reverse_game;

import android.os.AsyncTask;

public class BirdAsyncTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {

        while(true){
            MainActivity.draw_dird();
            MainActivity.bird_y += 10;
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }

    }

}
