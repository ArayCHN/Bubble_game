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
    MyTimer timer;
    Index index;

    public GameInterface(int level, JFrame frameIndexIn, JPanel panelIndexIn, Index indexIn) {
        // level1: 3*7, 3; level2: 4*7, 4; level3: 5*7, 5 colors
        index = indexIn;
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
        timer = new MyTimer(60, panelTimer, this); // 60 seconds
        timer.paused = false;

        // add balls
        LogicGame logicGame = new LogicGame(level, panelGame, index);

        // add tools

        // add various settings buttons
        JButton menu = new JButton("MENU");
        JButton pause = new JButton("PAUSE"); // functions the same as menu button!
        panelGameSettings.setLayout(new BoxLayout(panelGameSettings, BoxLayout.X_AXIS));
        panelGameSettings.add(menu);
        panelGameSettings.add(pause);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(panelGameSettings);
        //frame.add(Box.createRigidArea(new Dimension(0, 50)));
        frame.add(panelTimer);
        //frame.add(Box.createRigidArea(new Dimension(0, 50)));
        frame.add(panelGame);

        menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                panelGame.setVisible(false);
                panelGameSettings.setVisible(false);
                panelTimer.setVisible(false);
                createMenu();
                timer.paused = true;
            }
        });

        pause.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                panelGame.setVisible(false);
                panelGameSettings.setVisible(false);
                panelTimer.setVisible(false);
                paused = false;
                createMenu();
                timer.paused = true;
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
                timer.paused = false;
            }
        });

        buttonRestart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                panelMenu.setVisible(false);
                panelIndex.setVisible(true);
                timer.paused = false;
                timer.reset();
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

    public void gameOver(String endGameMessage) {
        JOptionPane.showMessageDialog(frame, endGameMessage); // display message
        // clear game page and restart everything
        timer.reset();
        panelGame.setVisible(false);
        panelGameSettings.setVisible(false);
        panelTimer.setVisible(false);
        panelIndex.setVisible(true);
    }

}