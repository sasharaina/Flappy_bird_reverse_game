package com.example.flappy_bird_reverse_game;

public class BackgroundThread extends Thread {
    float getTranslationX1;
    float getTranslationX2;
    float getTranslationX3;
    float getTranslationX4;

    @Override
    public void run() {
        while(true) {


            getTranslationX1 = MainActivity.background1.getTranslationX();
            getTranslationX2 = MainActivity.background2.getTranslationX();

            if(getTranslationX1 >= MainActivity.main_width)
                MainActivity.background1.setTranslationX(getTranslationX2 - MainActivity.main_width + 1);
            else
                MainActivity.background1.setTranslationX(getTranslationX1 + 1);

            if(getTranslationX2 >= MainActivity.main_width)
                MainActivity.background2.setTranslationX(getTranslationX1 - MainActivity.main_width + 1);
            else
                MainActivity.background2.setTranslationX(getTranslationX2 + 1);

            getTranslationX3 = MainActivity.cloud1.getTranslationX();
            if(getTranslationX3 >= MainActivity.main_width)
                MainActivity.cloud1.setTranslationX(-MainActivity.main_width);
            else
                MainActivity.cloud1.setTranslationX(getTranslationX3 + 2);
            getTranslationX4 = MainActivity.cloud2.getTranslationX();
            if(getTranslationX4 >= MainActivity.main_width)
                MainActivity.cloud2.setTranslationX(-MainActivity.main_width);
            else
                MainActivity.cloud2.setTranslationX(getTranslationX4 + 2);

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }

    }
}
