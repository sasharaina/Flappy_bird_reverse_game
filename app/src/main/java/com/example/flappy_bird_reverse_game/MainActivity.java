package com.example.flappy_bird_reverse_game;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    static int main_width; // мы считаем это высоту только один раз и потом она не меняеться
    static int main_height; // мы считаем это высоту только один раз и потом она не меняеться
    static int bird_width;
    static int bird_height;
    static int bird_y;
    static int bird_x;
    static boolean bird_up = false;

    static int speed = 15; // скорость движения

    static int numberOfObstacles = 6;
    static ImageView[] obstacles = new ImageView[numberOfObstacles];

    // эти параметры будет изменяться в AcyncTask, а отображаться в UI отоке
    static int[] obstacles_x = new int[numberOfObstacles];
    static int[] obstacles_y = new int[numberOfObstacles];
    static int[] obstacles_rotation = new int[numberOfObstacles];

    static int[] obstacles_width = new int[numberOfObstacles];
    static int[] obstacles_height = new int[numberOfObstacles];

    static ImageView background1;
    static ImageView background2;
    static ImageView cloud1;
    static ImageView cloud2;

    // птица
    static ImageView bird;

    // если true, то крылья вверх
    static boolean wings_up = false;

    static Context context;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // открываем приложение на весь экран
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        // узнаём ширину и высоту экрана
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        main_width = size.x;
        main_height = size.y;

        // вытаскиваем из layout все необходимые нам элементы
        RelativeLayout relativeLayout = findViewById(R.id.relative_layout);
        bird = findViewById(R.id.bird);
        obstacles[0] = findViewById(R.id.obstacle_0);
        obstacles[1] = findViewById(R.id.obstacle_1);
        obstacles[2] = findViewById(R.id.obstacle_2);
        obstacles[3] = findViewById(R.id.obstacle_3);
        obstacles[4] = findViewById(R.id.obstacle_4);
        obstacles[5] = findViewById(R.id.obstacle_5);
        cloud1 = findViewById(R.id.cloud1);
        cloud2 = findViewById(R.id.cloud2);
        background1 = findViewById(R.id.background1);
        background2 = findViewById(R.id.background2);

        // узнатём высоты и ширины всех препятствий
        for(int i = 0; i < obstacles.length; i++){
            obstacles[i].measure(0,0);
            obstacles_width[i] = obstacles[i].getMeasuredWidth();
            obstacles_height[i] = obstacles[i].getMeasuredHeight();
        }

        // выносим все препядствия за экран
        for(int i=0; i<numberOfObstacles; i++)
            obstacles[i].setTranslationX(-obstacles_width[i]);

        // размещаем птицу по середине и облака
        bird.measure(0,0);
        bird_width = bird.getMeasuredWidth();
        bird_height = bird.getMeasuredHeight();
        bird_y = main_height/2 - bird_height/2;
        bird_x = main_width/4*3;
        bird.setTranslationX(bird_x);
        cloud1.measure(0,0);
        int cloud1_y = cloud1.getMeasuredHeight();
        cloud1.setTranslationY(main_height/2 - cloud1_y/2);
        cloud2.setTranslationY(main_height/2 - cloud1_y/2);


        // делаем реакцию на нажатие
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bird_up = true;
                return true;
            }
        });

        background2.setTranslationX(-main_width + 1);
        cloud2.setTranslationX(-main_width);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // запускаем потоки
        new WingsThread().start();
        new BirdThread().start();
        new BackgroundThread().start();
        new ObstacleAsyncTask(0, 1, -1, 2, 3, 4, -1 , 5).execute();

    }

    static void draw_dird(){

        // изменение картинки
        if(wings_up)
            bird.setImageResource(R.drawable.bird1);
        else
            bird.setImageResource(R.drawable.bird2);

        bird.setTranslationY(bird_y);

    }

    static void draw_obstacle(int number_of_obstacle){
        obstacles[number_of_obstacle].setTranslationX(obstacles_x[number_of_obstacle]);
        obstacles[number_of_obstacle].setTranslationY(obstacles_y[number_of_obstacle]);
        obstacles[number_of_obstacle].setRotation(obstacles_rotation[number_of_obstacle]);
    }


}
