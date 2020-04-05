package com.example.flappy_bird_reverse_game;

/*
 * Поток, который отвечает за движение крыльев
 */
public class WingsThread extends Thread {

    @Override
    public void run() {
        do {
            MainActivity.wings_up = !MainActivity.wings_up;
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } while (!(MainActivity.bird_x < 0));
    }
}