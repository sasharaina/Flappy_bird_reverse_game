package com.example.flappy_bird_reverse_game;

import android.os.AsyncTask;

public class WingsAsyncTask extends AsyncTask<Void, Void, Void> {



    @Override
    protected Void doInBackground(Void... voids) {

        while(true){
            MainActivity.draw_dird();
            MainActivity.wings_up = !MainActivity.wings_up;
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }

    }

}
