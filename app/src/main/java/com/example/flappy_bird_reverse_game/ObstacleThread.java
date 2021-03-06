package com.example.flappy_bird_reverse_game;

import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.List;

/*
 * Поток, который отвечает за движение препятствий
 */

public class ObstacleThread extends Thread {

    private static Integer[] numbers_of_obstacle;
    static int numberOfObstacles;
    private float main_width = MainActivity.main_width;
    private float main_height = MainActivity.main_height;
    private MainActivity mainActivity;

    // самая минимальная начальная позиция
    private float min = 0;

    // номера препятствий передаются следующим образом: нижнее, верхнее, нижнее, верхнее...
    // передаються именно номера картинок препятствий
    // если препятствие отсутствует - ставим -1
    ObstacleThread(MainActivity mainActivity, int... numbers_of_images){
        this.mainActivity = mainActivity;
        int obstacle_number = 0;
        List<Integer> list_numbers_of_obstacle = new LinkedList<>();
        List<ImageView> list_obstacles = new LinkedList<>();
        for (int numbers_of_image : numbers_of_images) {
            if (numbers_of_image == -1) {
                list_numbers_of_obstacle.add(-1);
                continue;
            }
            ImageView obstacle = new ImageView(mainActivity.context);
            obstacle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            if (numbers_of_image == 0)
                obstacle.setImageResource(R.drawable.obstacle_0);
            else
                obstacle.setImageResource(R.drawable.obstacle_1);
            list_obstacles.add(obstacle);
            list_numbers_of_obstacle.add(obstacle_number++);
            mainActivity.relativeLayout.addView(obstacle, 4);
        }

        MainActivity.obstacles = new ImageView[obstacle_number];
        MainActivity.obstacles_width = new float[obstacle_number];
        MainActivity.obstacles_height = new float[obstacle_number];
        MainActivity.obstacles_rotation = new float[obstacle_number];
        MainActivity.obstacles_x = new float[obstacle_number];
        MainActivity.obstacles_y = new float[obstacle_number];

        for(int i = 0 ; i < obstacle_number; i++)
            MainActivity.obstacles[i] = list_obstacles.get(i);

        numbers_of_obstacle = new Integer[numbers_of_images.length];
        for(int i = 0 ; i < numbers_of_images.length; i++)
            numbers_of_obstacle[i] = list_numbers_of_obstacle.get(i);

        numberOfObstacles = MainActivity.obstacles.length;

        // узнатём высоты и ширины всех препятствий
        for(int i = 0; i < MainActivity.obstacles.length; i++){
            MainActivity.obstacles[i].measure(0,0);
            MainActivity.obstacles_width[i] = MainActivity.obstacles[i].getMeasuredWidth();
            MainActivity.obstacles_height[i] = MainActivity.obstacles[i].getMeasuredHeight();
        }

        // выносим все препятствия за экран
        for(int i=0; i < numberOfObstacles; i++)
            MainActivity.obstacles[i].setTranslationX(-MainActivity.obstacles_width[i]);

        new BirdThread(mainActivity).start();

    }

    //препятствия должны двигаться с лева на право , т.е. параметр translateX должен
    //постепенно меняться от < -obstacle_width > до < main_width >

    @Override
    public void run() {

        // ставим все препятствия в исходное положение
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
                    -MainActivity.obstacles_width[numbers_of_obstacle[i]] - (main_width) * (i >> 1);
            if(MainActivity.obstacles_x[numbers_of_obstacle[i]] < min)
                min = MainActivity.obstacles_x[numbers_of_obstacle[i]];
        }

        //минимальная позиция переносится чуть правее
        min += main_width/2;

        do {
            for (Integer integer : numbers_of_obstacle) {
                if (integer == -1)
                    continue;
                if (MainActivity.obstacles_x[integer] >= main_width)
                    MainActivity.obstacles_x[integer] = min;
                MainActivity.obstacles_x[integer] += MainActivity.speed;
            }
            mainActivity.timerMethod();
            try {

                Thread.sleep(20);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } while (!(MainActivity.bird_x < 0));
    }
}
