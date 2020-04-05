package com.example.flappy_bird_reverse_game;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

/*
 * Главная и единственнная активити
 */
public class MainActivity extends AppCompatActivity {

    static float main_width; // мы считаем это высоту только один раз и потом она не меняеться
    static float main_height; // мы считаем это высоту только один раз и потом она не меняеться
    static int bird_width;
    static float bird_height;
    static float bird_y;
    static float bird_x;
    static boolean bird_up = false;
    static int bird_rotation;
    // Отвечает за отключение жеста тап на начальном экране и при крушении птицы
    static boolean tapOnScreen = false;
    static int speed = 15; // скорость движения
    static ImageView[] obstacles;
    // эти параметры будет изменяться в Thread, а отображаться в UI отоке
    static float[] obstacles_x;
    static float[] obstacles_y;
    static float[] obstacles_rotation;
    static float[] obstacles_width;
    static float[] obstacles_height;
    ImageView background1;
    ImageView background2;
    ImageView cloud1;
    ImageView cloud2;
    RelativeLayout black_layout;
    TextView text_start;
    TextView tap_count_1;
    TextView tap_count_2;
    ImageView gameOver;
    static int score = 0;
    static int best_score;
    static SharedPreferences sPref;
    TextView best_score_view;
    Vibrator vibrator;
    MediaPlayer mPlayer1;
    MediaPlayer mPlayer2;

    // птица
    ImageView bird;

    // если true, то крылья вверх
    static boolean wings_up = false;

    Context context;

    RelativeLayout relativeLayout;

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
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

        sPref = getPreferences(MODE_PRIVATE);
        best_score = sPref.getInt("best_score", 0);

        context = getApplicationContext();

        // узнаём ширину и высоту экрана
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        main_width = size.x;
        main_height = size.y;

        if (main_width <= 320) {
            speed = 4;
        }

        if(main_width >= 500) {
            speed = 30;
        }

        // вытаскиваем из layout все необходимые нам элементы
        relativeLayout = findViewById(R.id.relative_layout);
        bird = findViewById(R.id.bird);
        cloud1 = findViewById(R.id.cloud1);
        cloud2 = findViewById(R.id.cloud2);
        background1 = findViewById(R.id.background1);
        background2 = findViewById(R.id.background2);
        black_layout = findViewById(R.id.black_layout);
        text_start = findViewById(R.id.text_start);
        tap_count_1 = findViewById(R.id.count1);
        tap_count_2 = findViewById(R.id.count2);
        gameOver = findViewById(R.id.gameOver);
        best_score_view = findViewById(R.id.best_score);
        best_score_view.setText("Best: " + best_score);
        mPlayer1=MediaPlayer.create(this, R.raw.die);
        mPlayer2=MediaPlayer.create(this, R.raw.wing);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        // размещаем птицу по середине и облака
        bird.measure(0,0);
        bird_width = bird.getMeasuredWidth();
        bird_height = bird.getMeasuredHeight();
        bird_y = main_height;
        bird_x = main_width/4*3;
        bird.setTranslationX(bird_x);
        cloud1.measure(0,0);
        float cloud1_y = cloud1.getMeasuredHeight();
        cloud1.setTranslationY(main_height/2 - cloud1_y/2);
        cloud2.setTranslationY(main_height/2 - cloud1_y/2);

        new ObstacleThread(this,0, 0, -1, 0, 0, 1, -1 , 1).start();
        // BirdThread вызывается в ObstacleThread
        new WingsThread().start();
        new BackgroundThread(this).start();

        // делаем реакцию на нажатие
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                bird_up = tapOnScreen;
                soundPlay(mPlayer2);
                return true;
            }
        });

        text_start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(tapOnScreen)
                    return false;
                tapOnScreen = true;
                score = 0;
                tap_count_1.setText("" + score);
                tap_count_2.setText("" + score);
                bird_y = main_height/2 - bird_height/2;
                bird_x = main_width/4*3;
                return true;
            }
        });

        background2.setTranslationX(-main_width + 1);
        cloud2.setTranslationX(-main_width);
    }

    public void timerMethod(){
        this.runOnUiThread(Timer_Tick);
    }

    /*
     * Создаем поток, который вызывает методы для изменения изображения
     */
    private Runnable Timer_Tick = new Runnable() {
        @SuppressLint("SetTextI18n")
        public void run() {
            draw_bird();
            draw_black_layout();
            for(int i = 0; i<ObstacleThread.numberOfObstacles; i++){
                draw_obstacle(i);
            }

            if(tapOnScreen) {
                score++;
                tap_count_1.setText("" + score);
                tap_count_2.setText("" + score);
            }

        }
    };

    @SuppressLint("SetTextI18n")
    void draw_black_layout(){
        if(tapOnScreen){
            black_layout.setAlpha(0);
            gameOver.setAlpha(1f);
        }
        else {
            black_layout.setAlpha(1);
            if(score > best_score) {
                SharedPreferences.Editor ed = sPref.edit();
                best_score = score;
                ed.putInt("best_score", score);
                ed.apply();
                best_score_view.setText("Best: " + best_score);
            }
        }
    }

    /*
     * Устанавливает изображение птицы и её положение и угол поворота
     */
    void draw_bird(){
        // изменение картинки
        if(wings_up)
            bird.setImageResource(R.drawable.bird3);
        else
            bird.setImageResource(R.drawable.bird4);

        bird.setTranslationY(bird_y);
        bird.setRotation(bird_rotation);
    }

    /*
     * Устанавливает расположение препятствия и ее наклон
     */
    static void draw_obstacle(int number_of_obstacle){
        obstacles[number_of_obstacle].setTranslationX(obstacles_x[number_of_obstacle]);
        obstacles[number_of_obstacle].setTranslationY(obstacles_y[number_of_obstacle]);
        obstacles[number_of_obstacle].setRotation(obstacles_rotation[number_of_obstacle]);
    }

    /*
     * Запускает звук при нажатии и при крушении птицы
     */
    public static void soundPlay(MediaPlayer sound) {
        sound.start();
    }
}
