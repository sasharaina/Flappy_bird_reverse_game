package com.example.flappy_bird_reverse_game;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    static int main_width;
    static int main_height;
    static int bird_width;
    static int bird_height;
    static int bird_y;

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
        Log.d("main_width = ", main_width + "");
        Log.d("main_height = ", main_height + "");

        bird = findViewById(R.id.bird);

        // TODO разместить прицу по середине

        bird.measure(0,0);
        bird_width = bird.getMeasuredWidth();
        bird_height = bird.getMeasuredHeight();
        Log.d("bird_width = ", bird_width + "");
        Log.d("bird_height = ", bird_height + "");

        bird_y = main_height/2 - bird_height/2;
        bird.setTranslationX(main_width/4*3);

    }

    @Override
    protected void onStart() {
        super.onStart();

        // запускаем потоки
        new WingsThread().start();
        new BirdThread().start();

    }

    static void draw_dird(){

        // изменение картинки
        if(wings_up)
            bird.setImageResource(R.drawable.bird1);
        else
            bird.setImageResource(R.drawable.bird2);

        bird.setTranslationY(bird_y);

    }

}
