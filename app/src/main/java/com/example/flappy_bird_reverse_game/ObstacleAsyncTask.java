package com.example.flappy_bird_reverse_game;

import android.os.AsyncTask;

public class ObstacleAsyncTask extends AsyncTask<Void, Void, Void> {

    int[] numbers_of_obstacle;


    // номера препятствий передаються следующим образом: нижнее, верхнее, нижнее, верхнее...
    // если препядствие отсутствует - ставит -1
    public ObstacleAsyncTask(int... numbers_of_obstacle){
        this.numbers_of_obstacle = numbers_of_obstacle;
    }

    // препядствия должны двигаться с лева на право , т.е. параметр transtateX должен
    //  постепенно меняться от < -obstacle_width > до < main_width + obstacle_width >

    // поток будет заниматься движение одного препядствия
    @Override
    protected Void doInBackground(Void... _) {

        int main_width = MainActivity.main_width;
        int main_height = MainActivity.main_height;

        // самая минимальная начальная позиция
        int min = 0;

        // ставим все препятствия в исходное положение
        // TODO проверить
        for(int i = 0; i < numbers_of_obstacle.length; i++){
            if(numbers_of_obstacle[i] == -1)
                continue;
            if(i % 2 == 1){
                MainActivity.obstacles_rotation[numbers_of_obstacle[i]] = 180;
                MainActivity.obstacles_y[numbers_of_obstacle[i]] = 0;
            } else {
                MainActivity.obstacles_rotation[numbers_of_obstacle[i]] = 0;
                MainActivity.obstacles_y[numbers_of_obstacle[i]] = main_height -
                        MainActivity.obstacles_height[numbers_of_obstacle[i]];
            }
            MainActivity.obstacles_x[numbers_of_obstacle[i]] =
                    -MainActivity.obstacles_width[numbers_of_obstacle[i]] - (main_width) * (i/2);
            if(MainActivity.obstacles_x[numbers_of_obstacle[i]] < min)
                min = MainActivity.obstacles_x[numbers_of_obstacle[i]];
        }

        // нимимальная позиция переноситься чуть правее
        min += main_width/2;

        while(true){
            for (int i = 0; i < numbers_of_obstacle.length; i++) {
                if(numbers_of_obstacle[i] == -1)
                    continue;
                if(MainActivity.obstacles_x[numbers_of_obstacle[i]] >= main_width)
                    MainActivity.obstacles_x[numbers_of_obstacle[i]] = min;
                MainActivity.draw_obstacle(numbers_of_obstacle[i]);
                MainActivity.obstacles_x[numbers_of_obstacle[i]] += MainActivity.speed;
            }
            try {

                Thread.sleep(20);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }



}
