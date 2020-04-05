package com.example.flappy_bird_reverse_game;

/*
 * Поток, который отвечает за скорость птицы и за ее крушение
*/

public class BirdThread extends Thread {

    private int ups = 0;
    private int smoothness = 0;
    private MainActivity mainActivity;

    BirdThread(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        while (true) {

            if (MainActivity.bird_up) {
                if (MainActivity.bird_y > 15) {
                    MainActivity.bird_y -= 15;

                    // установливаем плавность плавность
                    smoothness = 5;

                    MainActivity.bird_rotation = 10;

                }
                ups++;
                smoothness++;
                if (MainActivity.main_width <= 320) {
                    if (ups >= 2) {
                        ups = 0;
                        MainActivity.bird_up = false;
                    }
                }
                if (ups >= 3) {
                    ups = 0;
                    MainActivity.bird_up = false;
                }
            } else {

                if (MainActivity.main_width <= 320) {
                    MainActivity.bird_y += 1;
                }
                MainActivity.bird_y += 6;
                if (!MainActivity.tapOnScreen)
                    MainActivity.bird_y += 14;

                // установить плавность
                if (smoothness >= -10) {
                    MainActivity.bird_rotation = -10;
                } else if (smoothness >= -20) {
                    MainActivity.bird_rotation = -20;
                } else if (smoothness >= -35) {
                    MainActivity.bird_rotation = -35;
                } else if (smoothness >= -45) {
                    MainActivity.bird_rotation = -45;

                } else if (smoothness >= -55) {
                    MainActivity.bird_rotation = -55;

                } else if (smoothness >= -65) {
                    MainActivity.bird_rotation = -65;

                } else if (smoothness >= -70) {
                    MainActivity.bird_rotation = -70;

                }
                smoothness--;
            }

            for (int i = 0; i < ObstacleThread.numberOfObstacles; i++) {
                float a = MainActivity.bird_x;
                float b = MainActivity.bird_y;
                float x1 = MainActivity.obstacles_x[i];
                float y1 = MainActivity.obstacles_y[i];
                float h = MainActivity.obstacles_height[i];
                float w = MainActivity.obstacles_width[i];
                float x2 = x1 + w;
                float y2 = y1 + h;
                float k = MainActivity.bird_height;
                if ((x1 < a && a < x2 && y1 < b && b < y2) || (y1 < b + k && b + k < y2 && x1 < a && a < x2) || (b + k > MainActivity.main_height)) {

                    // при ударе должен отключаться тап
                    MainActivity.tapOnScreen = false;

                    if (b + k <= MainActivity.main_height) {
                        MainActivity.soundPlay(mainActivity.mPlayer1);
                        mainActivity.vibrator.vibrate(20);
                    }
                }
            }

            if(MainActivity.bird_x < 0){
                break;
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
