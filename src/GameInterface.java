// the game interface
package bubblegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import sun.audio.*;

public class GameInterface {
    JFrame frame;
    JPanel panel, panelMenu, panelIndex, panelGame, panelGameSettings, panelTimer;
    boolean menuCreated = false, paused = false;
    int[][] map;
    Random rand = new Random();
    MyTimer timer;
    Index index;

    public GameInterface(int level, JFrame frameIndexIn, JPanel panelIndexIn, Index indexIn, String filename) {
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
        int timeLength = 60;
        if (filename != "") {// there is a file, read time!
            File file = new File(filename);
            Scanner scanner;
            try {
                scanner = new Scanner(file);
            } catch(Exception e) {
                e.printStackTrace();
                return;
            }
            scanner.next();
            String timeString = scanner.next();
            timeString = timeString.substring(0, timeString.length() - 1);
            try {
                timeLength = Integer.parseInt(timeString);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }
        timer = new MyTimer(timeLength, panelTimer, this); // 60 seconds
        timer.paused = false;

        // add balls
        LogicGame logicGame = new LogicGame(level, panelGame, index, filename);

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

        try {
            Thread.sleep(500);
        } catch(Exception e) {
            e.printStackTrace();
        }

        timer.begin();
    }

    public void createMenu() {
        if (menuCreated) {
            panelMenu.setVisible(true);
            return;
        }
        JButton buttonResume, buttonRestart, buttonQuit;
        panelMenu = new JPanel();
        panelMenu.setOpaque(false);

        ImageIcon buttonIcon = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("img/button.png")).getImage().getScaledInstance(250, 100, Image.SCALE_DEFAULT));
        
        buttonResume = new JButton("  RESUME  ");
        buttonResume.setBorder(BorderFactory.createEmptyBorder());
        buttonResume.setHorizontalTextPosition(JButton.CENTER);
        buttonResume.setVerticalTextPosition(JButton.CENTER);
        buttonResume.setIcon(buttonIcon);

        buttonRestart = new JButton(" RESTART  ");
        buttonRestart.setBorder(BorderFactory.createEmptyBorder());
        buttonRestart.setHorizontalTextPosition(JButton.CENTER);
        buttonRestart.setVerticalTextPosition(JButton.CENTER);
        buttonRestart.setIcon(buttonIcon);

        buttonQuit = new JButton("   QUIT   ");
        buttonQuit.setBorder(BorderFactory.createEmptyBorder());
        buttonQuit.setHorizontalTextPosition(JButton.CENTER);
        buttonQuit.setVerticalTextPosition(JButton.CENTER);
        buttonQuit.setIcon(buttonIcon);

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

        int buttonSize = 38;
        Font buttonFont = new Font("System", Font.BOLD, buttonSize);
        buttonRestart.setFont(buttonFont);
        buttonResume.setFont(buttonFont);
        buttonQuit.setFont(buttonFont);
        buttonResume.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonRestart.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonQuit.setAlignmentX(Component.CENTER_ALIGNMENT);
        int buttonSpace = 20;
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.add(Box.createRigidArea(new Dimension(0, 100)));
        panelMenu.add(buttonResume);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 30)));
        panelMenu.add(buttonRestart);
        panelMenu.add(Box.createRigidArea(new Dimension(0, 30)));
        panelMenu.add(buttonQuit);
        frame.add(panelMenu);
        menuCreated = true;
    }

    public void gameOver(String endGameMessage) {
        timer.reset();
        JOptionPane.showMessageDialog(frame, endGameMessage); // display message
        // clear game page and restart everything
        panelGame.setVisible(false);
        panelGameSettings.setVisible(false);
        panelTimer.setVisible(false);
        panelIndex.setVisible(true);
    }

}