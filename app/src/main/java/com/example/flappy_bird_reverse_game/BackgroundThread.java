package com.example.flappy_bird_reverse_game;

/*
 * Поток, который отвечает за движение фона и облаков
 */

public class BackgroundThread extends Thread {
    private MainActivity mainActivity;

    BackgroundThread(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        float getTranslationX1;
        float getTranslationX2;
        float getTranslationX3;
        float getTranslationX4;

        getTranslationX1 = mainActivity.background1.getTranslationX();
        getTranslationX2 = mainActivity.background2.getTranslationX();

        if (getTranslationX1 >= MainActivity.main_width)
            mainActivity.background1.setTranslationX(getTranslationX2 - MainActivity.main_width + 1);
        else
            mainActivity.background1.setTranslationX(getTranslationX1 + 1);

        if (getTranslationX2 >= MainActivity.main_width)
            mainActivity.background2.setTranslationX(getTranslationX1 - MainActivity.main_width + 1);
        else
            mainActivity.background2.setTranslationX(getTranslationX2 + 1);

        getTranslationX3 = mainActivity.cloud1.getTranslationX();
        if (getTranslationX3 >= MainActivity.main_width)
            mainActivity.cloud1.setTranslationX(-MainActivity.main_width);
        else
            mainActivity.cloud1.setTranslationX(getTranslationX3 + 2);
        getTranslationX4 = mainActivity.cloud2.getTranslationX();
        if (getTranslationX4 >= MainActivity.main_width)
            mainActivity.cloud2.setTranslationX(-MainActivity.main_width);
        else
            mainActivity.cloud2.setTranslationX(getTranslationX4 + 2);

        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (!(MainActivity.bird_y < -10)) {

            getTranslationX1 = mainActivity.background1.getTranslationX();
            getTranslationX2 = mainActivity.background2.getTranslationX();

            if (getTranslationX1 >= MainActivity.main_width)
                mainActivity.background1.setTranslationX(getTranslationX2 - MainActivity.main_width + 1);
            else
                mainActivity.background1.setTranslationX(getTranslationX1 + 1);

            if (getTranslationX2 >= MainActivity.main_width)
                mainActivity.background2.setTranslationX(getTranslationX1 - MainActivity.main_width + 1);
            else
                mainActivity.background2.setTranslationX(getTranslationX2 + 1);

            getTranslationX3 = mainActivity.cloud1.getTranslationX();
            if (getTranslationX3 >= MainActivity.main_width)
                mainActivity.cloud1.setTranslationX(-MainActivity.main_width);
            else
                mainActivity.cloud1.setTranslationX(getTranslationX3 + 2);
            getTranslationX4 = mainActivity.cloud2.getTranslationX();
            if (getTranslationX4 >= MainActivity.main_width)
                mainActivity.cloud2.setTranslationX(-MainActivity.main_width);
            else
                mainActivity.cloud2.setTranslationX(getTranslationX4 + 2);

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
