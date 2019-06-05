package bubblegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.util.Timer;
import sun.audio.*;

public class MyTimer {
    int timeLeft, totalTime;
    JPanel panelTimer;
    JLabel labelTimer;
    CheckTimer checker;
    long startTime, elapsedTime;
    Timer timer;
    JProgressBar bar;
    boolean paused = false, timesUp = false;
    GameInterface gameInterface;

    public MyTimer(int timeLeftIn, JPanel panelTimerIn, GameInterface gameInterfaceIn) {
        gameInterface = gameInterfaceIn;
        timeLeft = timeLeftIn + 1;
        totalTime = timeLeftIn;
        timer = new Timer();
        panelTimer = panelTimerIn;
        panelTimer.setLayout(new BoxLayout(panelTimer, BoxLayout.Y_AXIS));
        labelTimer = new JLabel(String.valueOf(timeLeft));
        bar = new JProgressBar(SwingConstants.HORIZONTAL);
        bar.setMaximum(timeLeftIn);
        bar.setMinimum(0);
        bar.setValue(timeLeftIn);
        bar.setStringPainted(true);
        panelTimer.add(bar);
        //panelTimer.add(labelTimer);
        // startTime = System.currentTimeMillis();
        // elapsedTime = 0;
    }

    public void begin() {
        setTimer();
    }

    public void setTimer() {
        long interval = 1000;
        timesUp = false;
        paused = false;
        timer.schedule(new TimerTask() {
            public void run() {
                if (!paused) {
                    timeLeft --;
                    try {
                        AudioStream audioTick = new AudioStream(BubbleGame.class.getResourceAsStream("../sound/tick.wav"));
                        AudioPlayer.player.start(audioTick);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    if (timeLeft == 0) {
                        if (!timesUp) {
                            timesUp = true;
                            // go to GameOver page
                            paused = true;
                            try {
                                AudioStream audioLose = new AudioStream(BubbleGame.class.getResourceAsStream("../sound/timesup.wav"));
                                AudioPlayer.player.start(audioLose);
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                            gameInterface.gameOver("Time's up!");
                        }
                        return;
                    }
                    render();
                }
            }
        }, 0, interval);
    }

    public void render() {
        bar.setValue(timeLeft);
        bar.setString(String.valueOf(timeLeft));
        labelTimer.setText(String.valueOf(timeLeft));
    }

    public void reset() {
        timer.cancel();
        timeLeft = totalTime;
    }

    public void add(int time) {
        try {
            AudioStream audioAdd = new AudioStream(BubbleGame.class.getResourceAsStream("../sound/add.wav"));
            AudioPlayer.player.start(audioAdd);
        } catch(Exception e) {
            e.printStackTrace();
        }
        timeLeft += time;
        if (timeLeft > 60)
            timeLeft = 60;
    }
}