package com.example.flappy_bird_reverse_game;

import android.content.Context;
import android.util.Log;

public class BirdThread extends Thread {

    int ups = 0;
    int smoothness = 0;


    @Override
    public void run() {
        while(true){
             //MainActivity.draw_dird();
            if(MainActivity.bird_up) {
                if(MainActivity.bird_y > 15) {
                    MainActivity.bird_y -= 15;

                    // установить плавность
                    smoothness = 5;
                    if(smoothness <= 5) {
                        MainActivity.bird_rotation = 5;
                    }
                   else if(smoothness <=10){
                        MainActivity.bird_rotation = 10;
                    }
                    else if(smoothness <=15){
                        MainActivity.bird_rotation = 15;
                    }
                    else if(smoothness <=20){
                        MainActivity.bird_rotation = 20;

                    }
                    else if(smoothness <=25){
                        MainActivity.bird_rotation = 25;
                        smoothness = 0;
                    }
                    else if(smoothness <=30){
                        MainActivity.bird_rotation = 30;

                    }else if(smoothness <=35){
                        MainActivity.bird_rotation = 35;
                        smoothness = 0;

                    }

                }
                ups++;
                smoothness++;
                if (ups >= 3) {
                    ups = 0;
                    MainActivity.bird_up = false;
                }
            } else {
                MainActivity.bird_y += 7;
                if(MainActivity.tapOnScreen == false)
                    MainActivity.bird_y += 14;

                // установить плавность
                if(smoothness >= -10) {
                    MainActivity.bird_rotation = -10;
                }
               else if(smoothness >= -20) {
                    MainActivity.bird_rotation = -20;
                }
               else if(smoothness >= -35) {
                    MainActivity.bird_rotation = -35;
                }
               else if(smoothness >= -45) {
                    MainActivity.bird_rotation = -45;

                }
                else if(smoothness >= -55) {
                    MainActivity.bird_rotation = -55;

                }
                else if(smoothness >= -65) {
                    MainActivity.bird_rotation = -65;

                }
                else if(smoothness >= -70) {
                    MainActivity.bird_rotation = -70;

                }
                    smoothness--;
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

                    // при ударе должен отключаться тап
                    MainActivity.tapOnScreen = false;

                    if(b+k <= MainActivity.main_height) {
                        MainActivity.vibrator.vibrate(20);
                    }

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
