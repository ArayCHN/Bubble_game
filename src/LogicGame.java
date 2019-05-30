package bubblegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class LogicGame {
    int[][] map;
    int numRow, numCol, numTyp;
    JPanel panelGame;
    ImageIcon[] textures = new ImageIcon[7];
    Random rand = new Random();
    int r = 30; // size of each ball

    public void render(double xCenter, double yCenter, int which) {
        JLabel ball = new JLabel();
        ball.setIcon(textures[which]);
        ball.setBounds((int)(xCenter * r - r), (int)(yCenter * r - r), 2 * r, 2 * r);
        panelGame.add(ball);
    }

    public void playGame() {

    }

    public void initializeGame(int level, JPanel panelGameIn) {
        panelGame = panelGameIn;
        panelGame.setLayout(null); // set absolute layout
        switch (level) {
            case 1: numRow = 3; numCol = 7; numTyp = 3; break;
            case 2: numRow = 4; numCol = 7; numTyp = 4; break;
            case 3: numRow = 5; numCol = 7; numTyp = 5; break;
            default: numRow = 3; numCol = 7; numTyp = 3;
        }
        map = new int[numRow][numCol]; // initialized to 0
        for (int i = 0; i < numRow; i ++)
            for (int j = 0; j < numCol; j ++) {
                map[i][j] = rand.nextInt(numTyp);
                double xCenter = (i % 2) + 2.1 * j + 1, yCenter = 1 + 1.8 * i;
                render(xCenter, yCenter, map[i][j]); // graphics
                // System.out.println(String.valueOf(i) + ", " + String.valueOf(j));
            }
    }

    public LogicGame(int level, JPanel panelGameIn) {
        textures[0] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/1.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[1] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/2.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[2] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/3.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[3] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/4.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[4] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/5.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[5] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/6.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[6] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/7.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));

        initializeGame(level, panelGameIn);
    }

}