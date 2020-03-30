package com.example.flappy_bird_reverse_game;

import android.util.Log;

public class BirdThread extends Thread {

    int ups = 0;

    @Override
    public void run() {
        while(true){
             //MainActivity.draw_dird();
            if(MainActivity.bird_up) {
                if(MainActivity.bird_y > 15) {
                    MainActivity.bird_y -= 15;

                    // TODO установить сначала 10, потом 20, потом уже 30
                    MainActivity.bird_rotation = 30;
                }
                ups++;
                if (ups >= 3) {
                    ups = 0;
                    MainActivity.bird_up = false;
                }
            } else {
                MainActivity.bird_y += 7;
                if(MainActivity.tapOnScreen == false)
                    MainActivity.bird_y += 14;

                // TODO установить сначала -20, потом -40, потом -55 и потом -70,
                MainActivity.bird_rotation = -70;
            }


            for(int i = 0; i < ObstacleThread.numberOfObstacles; i++){
                int a = MainActivity.bird_x;
                int b = MainActivity.bird_y;
                int x1 = MainActivity.obstacles_x[i];
                int y1 = MainActivity.obstacles_y[i];
                int h =  MainActivity.obstacles_height[i];
                int w = MainActivity.obstacles_width[i];
                int x2 = x1 + w;
                int y2 = y1 + h;
                int k = MainActivity.bird_height;
                if((x1 < a && a<x2 && y1 < b && b < y2) || (y1 < b + k && b + k < y2 && x1 < a && a<x2) || (b+k > MainActivity.main_height)){

                    // TODO при ударе должен отключаться тап
                    MainActivity.tapOnScreen = false;


                }


            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }

}
