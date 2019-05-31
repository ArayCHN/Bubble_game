package bubblegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.util.Timer;

public class MyTimer {
    int timeLeft, totalTime;
    JPanel panelTimer;
    JLabel labelTimer;
    CheckTimer checker;
    long startTime, elapsedTime;
    Timer timer;
    JProgressBar bar;

    public MyTimer(int timeLeftIn, JPanel panelTimerIn) {
        timeLeft = timeLeftIn + 1;
        timer = new Timer();
        panelTimer = panelTimerIn;
        panelTimer.setLayout(new FlowLayout());
        labelTimer = new JLabel(String.valueOf(timeLeft));
        bar = new JProgressBar(SwingConstants.HORIZONTAL);
        bar.setMaximum(timeLeftIn);
        bar.setMinimum(0);
        bar.setValue(timeLeftIn);
        bar.setStringPainted(true);
        panelTimer.add(bar);
        //panelTimer.add(labelTimer);
        setTimer();
        // startTime = System.currentTimeMillis();
        // elapsedTime = 0;
    }

    public void setTimer() {
        long interval = 1000;
        timer.schedule(new TimerTask() {
            public void run() {
                timeLeft --;
                render();
            }
        }, 0, interval);
    }

    public void render() {
        bar.setValue(timeLeft);
        bar.setString(String.valueOf(timeLeft));
        labelTimer.setText(String.valueOf(timeLeft));
    }

    public void reset(int time) {
        timeLeft = time;
        timer.cancel();
        setTimer();
    }

    public void add(int time) {
        timeLeft += time;
    }

    public void run() {
        long elapsedTime = (new Date()).getTime() - startTime;
        timeLeft = totalTime - (int)(elapsedTime / 1000);
        render();
    }
}