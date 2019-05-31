// the game interface
package bubblegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class GameInterface {
    JFrame frame;
    JPanel panel, panelMenu, panelIndex, panelGame, panelGameSettings, panelTimer;
    boolean menuCreated = false, paused = false;
    int[][] map;
    Random rand = new Random();

    public GameInterface(int level, JFrame frameIndexIn, JPanel panelIndexIn) {
        // level1: 3*7, 3; level2: 4*7, 4; level3: 5*7, 5 colors
        frame = frameIndexIn;
        panelIndex = panelIndexIn;
        panelGame = new JPanel();
        panelGame.setOpaque(false);
        panelGameSettings = new JPanel();
        panelGameSettings.setOpaque(false);
        panelTimer = new JPanel();
        panelTimer.setOpaque(false);
        panelTimer.setPreferredSize(new Dimension(200, 20));

        // add timer
        MyTimer timer = new MyTimer(60, panelTimer); // 60 seconds
        //Thread timerThread = new Thread(timer);
        //timerThread.start();

        // add the main game interface
        LogicGame logicGame = new LogicGame(level, panelGame);

        // add cannon

        // add tools

        // add various settings buttons
        JButton menu = new JButton("MENU");
        JButton pause = new JButton("PAUSE"); // functions the same as menu button!
        panelGameSettings.add(menu);
        panelGameSettings.add(pause);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(panelGameSettings);
        frame.add(panelTimer);
        frame.add(panelGame);

        menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                panelGame.setVisible(false);
                panelGameSettings.setVisible(false);
                panelTimer.setVisible(false);
                createMenu();
            }
        });

        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                panelGame.setVisible(false);
                panelGameSettings.setVisible(false);
                panelTimer.setVisible(false);
                createMenu();
            }
        });
    }

    public void createMenu() {
        if (menuCreated) {
            panelMenu.setVisible(true);
            return;
        }
        JButton buttonResume, buttonRestart, buttonQuit;
        panelMenu = new JPanel();
        panelMenu.setOpaque(false);
        buttonResume = new JButton("RESUME");
        buttonRestart = new JButton("RESTART");
        buttonQuit = new JButton("QUIT");

        buttonResume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                panelMenu.setVisible(false);
                panelGame.setVisible(true);
                panelGameSettings.setVisible(true);
                panelTimer.setVisible(true);
            }
        });

        buttonRestart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                panelMenu.setVisible(false);
                panelIndex.setVisible(true);
            }
        });

        buttonQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                System.exit(0);
            }
        });

        panelMenu.add(buttonResume);
        panelMenu.add(buttonRestart);
        panelMenu.add(buttonQuit);
        frame.add(panelMenu);
        menuCreated = true;
    }

}