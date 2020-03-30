package com.example.flappy_bird_reverse_game;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    static int main_width; // мы считаем это высоту только один раз и потом она не меняеться
    static int main_height; // мы считаем это высоту только один раз и потом она не меняеться
    static int bird_width;
    static int bird_height;
    static int bird_y;
    static int bird_x;
    static boolean bird_up = false;
    static int bird_rotation;
    // Отвечает за отключение жеста тап на начальном экране и при крушении птицы
    static boolean tapOnScreen = true;

    static int speed = 15; // скорость движения

    static ImageView[] obstacles;

    // эти параметры будет изменяться в AcyncTask, а отображаться в UI отоке
    static int[] obstacles_x;
    static int[] obstacles_y;
    static int[] obstacles_rotation;

    static int[] obstacles_width;
    static int[] obstacles_height;

    static ImageView background1;
    static ImageView background2;
    static ImageView cloud1;
    static ImageView cloud2;
    static RelativeLayout black_layout;
    TextView text_start;

    // птица
    static ImageView bird;

    // если true, то крылья вверх
    static boolean wings_up = false;

    static Context context;

    static RelativeLayout relativeLayout;

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
        relativeLayout = findViewById(R.id.relative_layout);
        bird = findViewById(R.id.bird);
        cloud1 = findViewById(R.id.cloud1);
        cloud2 = findViewById(R.id.cloud2);
        background1 = findViewById(R.id.background1);
        background2 = findViewById(R.id.background2);
        black_layout = findViewById(R.id.black_layout);
        text_start = findViewById(R.id.text_start);

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
                bird_up = tapOnScreen;
                return true;
            }
        });

        text_start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(tapOnScreen)
                    return false;
                tapOnScreen = true;
                bird_y = main_height/2 - bird_height/2;
                bird_x = main_width/4*3;
                return true;
            }
        });

        background2.setTranslationX(-main_width + 1);
        cloud2.setTranslationX(-main_width);



        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }

        }, 0, 30);

    }

    private void TimerMethod(){

        this.runOnUiThread(Timer_Tick);

    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            draw_dird();
            draw_black_layout();
            for(int i = 0; i<ObstacleThread.numberOfObstacles; i++){
                draw_obstacle(i);
            }

        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        // запускаем потоки
        new ObstacleThread(0, 0, -1, 0, 0, 1, -1 , 1).start();
        // BirdThread вызывается в ObstacleThread
        new WingsThread().start();
        new BackgroundThread().start();



    }

    static void draw_black_layout(){
        if(tapOnScreen){
            black_layout.setAlpha(0);
        }
        else
            black_layout.setAlpha(1);
    }

    static void draw_dird(){


        // изменение картинки
        if(wings_up)
            bird.setImageResource(R.drawable.bird3);
        else
            bird.setImageResource(R.drawable.bird4);

        bird.setTranslationY(bird_y);
        bird.setRotation(bird_rotation);

    }

    static void draw_obstacle(int number_of_obstacle){
        obstacles[number_of_obstacle].setTranslationX(obstacles_x[number_of_obstacle]);
        obstacles[number_of_obstacle].setTranslationY(obstacles_y[number_of_obstacle]);
        obstacles[number_of_obstacle].setRotation(obstacles_rotation[number_of_obstacle]);
    }


}
