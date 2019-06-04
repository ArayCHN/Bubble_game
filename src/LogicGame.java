package bubblegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.lang.Math;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.awt.geom.*;
import java.util.concurrent.*;
import sun.audio.*;

class MutableInteger {
    int value;
    public void setValue(int valueIn) {
        value = valueIn;
    }
    public int getValue() {
        return value;
    }
    public MutableInteger(int valueIn) {
        setValue(valueIn);
    }
}

public class LogicGame {
    int[][] map, visited;
    JLabel[][] balls;
    int numRow, numCol, numTyp, numInitRow, numBalls = 0; // initial num of rows is not total num of rows
    JPanel panelGame;
    ImageIcon[] textures = new ImageIcon[7];
    ImageIcon cannonTexture;
    BufferedImage cannonImage;
    Random rand = new Random();
    int r = 29; // size of each ball
    int currBall, nextBall;
    int cannonCenterX, cannonCenterY;
    JLabel labelCurrBall, labelNextBall, cannon;
    int[] nextI = {0, 0, 1, -1, 1, -1};
    int[][] nextJ = {{1, -1, 0, 0, -1, -1}, {1, -1, 0, 0, 1, 1}}; // nextJ[i % 2][k]
    // GameInterface gameInterface;
    Index index;

    public JLabel renderBalls(double xCenter, double yCenter, int which) {
        JLabel ball = new JLabel();
        ball.setIcon(textures[which - 1]);
        ball.setBounds((int)(xCenter * r - r), (int)(yCenter * r - r), 2 * r, 2 * r);
        panelGame.add(ball);
        return ball;
    }

    public double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public void moveToClosest(int x, int y, MutableInteger newI, MutableInteger newJ) { // (x, y) is the upper-left corner
        double existingX, existingY;
        boolean moved = false;
        // double xCenter = (i % 2) + 2.1 * j + 1, yCenter = 1 + 1.8 * i;
        int movedI = (int)Math.round(y / 1.8 / r);
        int movedJ = (int)Math.round(x / 2.1 / r) - (movedI % 2);
        double min = 10.0 * r;
        for (int i = 0; i < numRow + 1; i++)
            for (int j = 0; j < numCol; j++) {
                existingX = ((i % 2) + 2.1 * j + 1) * r - r;
                existingY = (1 + 1.8 * i) * r - r;
                double dist = distance(existingX, existingY, x, y);
                if (dist < min && map[i][j] == 0) {
                    // move from here to existingX, existingY
                    min = dist;
                    movedI = i;
                    movedJ = j;
                }
            }
        existingX = ((movedI % 2) + 2.1 * movedJ + 1) * r - r;
        existingY = (1 + 1.8 * movedI) * r - r;
        labelCurrBall.setBounds((int)existingX, (int)existingY, 2 * r, 2 * r);
        newI.setValue(movedI);
        newJ.setValue(movedJ);
    }

    public void rotateCannon(double theta) {
        RotatedIcon rotatedCannon = new RotatedIcon(cannonTexture, theta);
        int newWidth = rotatedCannon.getIconWidth();
        int newHeight = rotatedCannon.getIconHeight();
        cannon.setBounds(cannonCenterX - newWidth / 2, cannonCenterY - newHeight / 2, newWidth, newHeight);
        cannon.setIcon(rotatedCannon);
    }

    public void clearVisited() {
        visited = new int[numRow + 1][numCol]; // default to zero
    }

    public int eliminatable(int i, int j) { // return >= 3, eliminatable, otherwise, non-eliminatable
        int color = map[i][j];
        visited[i][j] = 1;
        int num = 1;
        for (int k = 0; k < 6; k ++) {
            int i1 = i + nextI[k];
            int j1 = j + nextJ[i % 2][k];
            if (i1 >= 0 && i1 < numRow + 1 && j1 >= 0 && j1 < numCol
                && visited[i1][j1] != 1 && map[i1][j1] == color) {
                num += eliminatable(i1, j1);
                if (num >= 3)
                    return num; // trimming, no need to find more
            }
        }
        return num;
    }

    public void eliminate(int i, int j) {
        int color = map[i][j];
        visited[i][j] = 1;
        map[i][j] = 0;
        balls[i][j].setIcon(null); // clear icon
        //balls[i][j].revalidate();
        for (int k = 0; k < 6; k ++) {
            int i1 = i + nextI[k];
            int j1 = j + nextJ[i % 2][k];
            if (i1 >= 0 && i1 < numRow + 1 && j1 >= 0 && j1 < numCol
                && visited[i1][j1] != 1 && map[i1][j1] == color) {
                eliminate(i1, j1);
            }
        }
    }

    public void visit(int i, int j) {
        visited[i][j] = 1;
        for (int k = 0; k < 6; k ++) {
            int i1 = i + nextI[k];
            int j1 = j + nextJ[i % 2][k];
            if (i1 >= 0 && i1 < numRow + 1 && j1 >= 0 && j1 < numCol
                && visited[i1][j1] != 1 && map[i1][j1] != 0) {
                visit(i1, j1);
            }
        }
    }

    public void fall() {
        clearVisited(); // find eligible balls
        for (int j = 0; j < numCol; j ++) {
            if (visited[0][j] == 0 && map[0][j] != 0) {
                visit(0, j);
            }
        }
        fallEffect();
    }

    public void fallEffect() { // actually falls eligible balls!
        for (int i = 1; i < numRow + 1; i ++) 
            for (int j = 0; j < numCol; j ++ ) {
                if (map[i][j] != 0 && visited[i][j] == 0) { // this ball should fall!
                    map[i][j] = 0;
                    JLabel fallingBall = balls[i][j];
                    int x = (int)(((i % 2) + 2.1 * j) * r), y = (int)((1.8 * i) * r);
                    FallingBallRenderer renderer = new FallingBallRenderer(x, y, fallingBall);
                    Thread tFall = new Thread(renderer);
                    tFall.start();
            }
        }
    }

    public void postProcess(int i, int j) {
        if (map[i][j] < 6) {
            // determine if there are eliminations
            clearVisited();
            if (eliminatable(i, j) >= 3) {
                clearVisited();
                try {
                    AudioStream audioFall = new AudioStream(BubbleGame.class.getResourceAsStream("../sound/eliminate.wav"));
                    AudioPlayer.player.start(audioFall);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                eliminate(i, j);
            }
            // determine if there are isolated balls falling
            fall();
        } else if (map[i][j] == 6) { // bomb
            try {
                AudioStream audioFall = new AudioStream(BubbleGame.class.getResourceAsStream("../sound/bomb.wav"));
                AudioPlayer.player.start(audioFall);
            } catch(Exception e) {
                e.printStackTrace();
            }
            map[i][j] = 0;
            balls[i][j].setIcon(null); // clear icon
            for (int k = 0; k < 6; k ++) {
                int i1 = i + nextI[k];
                int j1 = j + nextJ[i % 2][k];
                if (i1 >= 0 && i1 < numRow + 1 && j1 >= 0 && j1 < numCol && map[i1][j1] != 0) {
                    map[i1][j1] = 0;
                    balls[i1][j1].setIcon(null); // clear icon
                }
            }
            fall();
        } else if (map[i][j] == 7) {
            try {
                AudioStream audioFall = new AudioStream(BubbleGame.class.getResourceAsStream("../sound/transform.wav"));
                AudioPlayer.player.start(audioFall);
            } catch(Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(50);
            } catch(Exception e) {}
            for (int k = 0; k < 6; k ++) {
                int i1 = i + nextI[k];
                int j1 = j + nextJ[i % 2][k];
                if (i1 >= 0 && i1 < numRow + 1 && j1 >= 0 && j1 < numCol && map[i1][j1] != 0) {
                    map[i][j] = map[i1][j1];
                    balls[i][j].setIcon(textures[map[i1][j1] - 1]); // clear icon
                    break;
                }
            }
            try {
                Thread.sleep(500);
            } catch(Exception e) {}
            // determine if there are eliminations
            clearVisited();
            if (eliminatable(i, j) >= 3) {
                clearVisited();
                try {
                    AudioStream audioFall = new AudioStream(BubbleGame.class.getResourceAsStream("../sound/eliminate.wav"));
                    AudioPlayer.player.start(audioFall);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                eliminate(i, j);
            }
            // determine if there are isolated balls falling
            fall();
        }
    }

    public boolean fails() {
        for (int j = 0; j < numCol; j++) {
            if (map[numRow][j] != 0)
                return true;
        }
        return false;
    }

    public boolean wins() {
        int numBalls = 0;
        for (int i = 0; i < numRow; i ++)
            for (int j = 0; j < numCol; j ++) {
                if (map[i][j] != 0)
                    numBalls ++;
            }
        if (numBalls < 5)
            return true;
        else
            return false;
    }

    public void generateNewBall() {
        currBall = nextBall;
        labelNextBall.setBounds(cannonCenterX - r, cannonCenterY - r, 2 * r, 2 * r); // move next ball to curr ball location
        labelCurrBall = labelNextBall;
        JLabel newLabelNextBall = new JLabel();
        nextBall = rand.nextInt(numTyp) + 1; // should not be 0, 0 is empty ball
        newLabelNextBall.setIcon(textures[nextBall - 1]);
        newLabelNextBall.setBounds(cannonCenterX - 4 * r, cannonCenterY - r, 2 * r, 2 * r);
        labelNextBall = newLabelNextBall;
        panelGame.add(newLabelNextBall);
        index.frame.revalidate();
        index.frame.repaint(); // repaint to clean up!
        // panelGame.add(labelNextBall);
    }

    public boolean stopped(int newX, int newY) {
        int centerX = newX + r;
        int centerY = newY + r;
        double existingX, existingY;
        if (centerY <= 1.1 * r) // reaches the ceiling, also stops!
            return true;
        for (int i = 0; i < numRow + 1; i++)
            for (int j = 0; j < numCol; j++) {
                existingX = ((i % 2) + 2.1 * j + 1) * r;
                existingY = (1 + 1.8 * i) * r;
                if (map[i][j] != 0 && distance(existingX, existingY, centerX, centerY) <= 2.2 * r) {
                    // stop!
                    // System.out.println("3, 0: "+String.valueOf(map[3][0]));
                    return true;
                }
            }
        return false;
    }

    public void initializeGame(int level, JPanel panelGameIn) {
        panelGame = panelGameIn;
        panelGame.setLayout(null); // set absolute layout
        switch (level) {
            case 1: numInitRow = 3; numCol = 7; numTyp = 3; break;
            case 2: numInitRow = 4; numCol = 7; numTyp = 4; break;
            case 3: numInitRow = 5; numCol = 7; numTyp = 5; break;
            default: numInitRow = 3; numCol = 7; numTyp = 3;
        }
        numRow = 8;
        // 8 rows in total! but we need 9 rows for temporary placement for the additional ball when
        // num of balls exceeds the lifeline and the game is over
        map = new int[numRow + 1][numCol]; // initialized to 0
        balls = new JLabel[numRow + 1][numCol];
        for (int i = 0; i < numInitRow; i ++)
            for (int j = 0; j < numCol; j ++) {
                map[i][j] = rand.nextInt(numTyp) + 1;
                double xCenter = (i % 2) + 2.1 * j + 1, yCenter = 1 + 1.8 * i;
                balls[i][j] = renderBalls(xCenter, yCenter, map[i][j]); // graphics
                // System.out.println(String.valueOf(i) + ", " + String.valueOf(j));
            }
        numBalls = numInitRow * numCol; // initial num of balls

        cannon = new JLabel();
        cannon.setIcon(cannonTexture);
        // cannon rotates around: 225, 500
        cannonCenterX = GlobalSettings.cannonCenterX; cannonCenterY = GlobalSettings.cannonCenterY;
        cannon.setBounds(cannonCenterX - 50, cannonCenterY - 50, 100, 100);
        panelGame.add(cannon);

        currBall = rand.nextInt(numTyp) + 1;
        nextBall = rand.nextInt(numTyp) + 1;
        labelCurrBall = new JLabel();
        labelNextBall = new JLabel();
        labelCurrBall.setIcon(textures[currBall - 1]);
        labelNextBall.setIcon(textures[nextBall - 1]);
        labelCurrBall.setBounds(cannonCenterX - r, cannonCenterY - r, 2 * r, 2 * r);
        labelNextBall.setBounds(cannonCenterX - 4 * r, cannonCenterY - r, 2 * r, 2 * r);
        panelGame.add(labelCurrBall);
        panelGame.add(labelNextBall);

        // tools: bomb, rainbow, laser, gold
        ImageIcon iconBomb = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/bomb.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        JButton buttonBomb = new JButton(iconBomb);
        buttonBomb.setBounds(cannonCenterX + 3 * r, cannonCenterY - r, 2 * r, 2 * r);
        panelGame.add(buttonBomb);
        buttonBomb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currBall = 6;
                labelCurrBall.setIcon(textures[5]);
                index.frame.revalidate();
                index.frame.repaint();
            }
        });

        ImageIcon iconRainbow = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/rainbow.png")).getImage().getScaledInstance((int)(1.8*r), (int)(1.8*r), Image.SCALE_DEFAULT));
        JButton buttonRainbow = new JButton(iconRainbow);
        buttonRainbow.setBounds(cannonCenterX + 6 * r, cannonCenterY - r, 2 * r, 2 * r);
        panelGame.add(buttonRainbow);
        buttonRainbow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currBall = 7;
                labelCurrBall.setIcon(textures[6]);
                index.frame.revalidate();
                index.frame.repaint();
            }
        });

        ImageIcon iconLaser = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/laser.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        JButton buttonLaser = new JButton(iconLaser);
        buttonLaser.setBounds(cannonCenterX + 3 * r, cannonCenterY - 3 * r, 2 * r, 2 * r);
        panelGame.add(buttonLaser);
        buttonLaser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearVisited();
                for (int j = 0; j < numCol; j ++) {
                    visited[0][j] = 1;
                }
                fallEffect();
                if (fails()) {
                    index.gamePage.gameOver("You lose!");
                } else if (wins()) {
                    index.gamePage.gameOver("You win!");
                }
            }
        });

        ImageIcon iconGold = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/gold.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        JButton buttonGold = new JButton(iconGold);
        buttonGold.setBounds(cannonCenterX + 6 * r, cannonCenterY - 3 * r, 2 * r, 2 * r);
        panelGame.add(buttonGold);
        buttonGold.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                index.gamePage.timer.add(10);
            }
        });

        GameMouseListener listener = new GameMouseListener();
        panelGame.addMouseListener(listener);
        panelGame.addMouseMotionListener(listener);
    }

    public LogicGame(int level, JPanel panelGameIn, Index indexIn) {
        textures[0] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/1.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[1] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/2.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[2] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/3.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[3] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/4.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[4] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/5.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[5] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/6.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT)); // bomb
        textures[6] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/7.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT)); // rainbow

        cannonTexture = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/cannon.png")).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        try {
            cannonImage = ImageIO.read(getClass().getResource("../img/cannon.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeGame(level, panelGameIn);
        index = indexIn;
    }

    private class GameMouseListener extends MouseAdapter {
        int deltaX, deltaY;

        public void mousePressed(MouseEvent e) {
            //System.out.println("Mouse pressed!");
        }

        public void mouseMoved(MouseEvent e) { // 
            //System.out.println("Mouse moved!");
            int tmpDeltaX = e.getX() - cannonCenterX;
            int tmpDeltaY = e.getY() - cannonCenterY;
            // only allow the direction of launching upwards!
            if (- (tmpDeltaY + 0.0) / Math.sqrt(deltaX * deltaX + deltaY * deltaY) > 0.2) {
                deltaX = tmpDeltaX;
                deltaY = tmpDeltaY;
            } else if (tmpDeltaX < 0) {
                        deltaX = -10;
                        deltaY = -2;
                    } else {
                        deltaX = 10;
                        deltaY = -2;
                    }
            // rotate cannon
            double theta = -Math.atan((deltaX + 0.0) / deltaY) / Math.PI * 180.0;
            rotateCannon(theta);
            // draw cannon direction line
            // not implemented
        }

        public void mouseReleased(MouseEvent e) {
            // launch the ball
            // System.out.println("Mouse released!");
            int v = 10;
            int step = 0;
            double dx = deltaX / (double)Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double dy = deltaY / (double)Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            Thread runBall = new Thread(new RunningBallRenderer(dx, dy, v));
            runBall.start();
        }
    }

    class FallingBallRenderer implements Runnable {
        int v = 10, initX, initY, newX, newY;
        JLabel fallingBall;

        public FallingBallRenderer(int initXIn, int initYIn, JLabel fallingBallIn) {
            newX = initXIn;
            newY = initYIn;
            fallingBall = fallingBallIn;
        }

        public void run() {
            try {
                AudioStream audioFall = new AudioStream(BubbleGame.class.getResourceAsStream("../sound/fall.wav"));
                AudioPlayer.player.start(audioFall);
            } catch(Exception e) {
                e.printStackTrace();
            }
            while (newY < GlobalSettings.deathLineHeight) { // find stop condition!
                newY += (int)v;
                fallingBall.setBounds(newX, newY, 2 * r, 2 * r);
                try {
                    Thread.sleep(10);
                } catch(Exception e1) {}
            }
            fallingBall.setIcon(null);
        }
    }

    class RunningBallRenderer implements Runnable {
        double dx, dy;
        int v, newX, newY;
        
        public RunningBallRenderer(double dxIn, double dyIn, int vIn) {
            dx = dxIn;
            dy = dyIn;
            v = vIn;
        }

        public void run() {
            synchronized(map) {
                try {
                    AudioStream audioLaunch = new AudioStream(BubbleGame.class.getResourceAsStream("../sound/launch.wav"));
                    AudioPlayer.player.start(audioLaunch);
                } catch(Exception e) {
                    e.printStackTrace();
                }
                int step = 0;
                newX = cannonCenterX - r;
                newY = cannonCenterY - r;
                while (!stopped(newX, newY)) { // find stop condition!
                    newX += (int)(v * dx);
                    newY += (int)(v * dy);
                    if (newX <= 10 || newX >= GlobalSettings.windowWidth - 50) { // reflected
                        try {
                            AudioStream audioFall = new AudioStream(BubbleGame.class.getResourceAsStream("../sound/reflect.wav"));
                            AudioPlayer.player.start(audioFall);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        dx = -dx;
                    }
                    step ++;
                    labelCurrBall.setBounds(newX, newY, 2 * r, 2 * r);
                    try {
                        Thread.sleep(10);
                    } catch(Exception e1) {}
                }
                // stopped, need to move to correct location!
                // System.out.println("Stopped!");
                MutableInteger newII = new MutableInteger(0), newJI = new MutableInteger(0);
                moveToClosest(newX, newY, newII, newJI);
                int newI = newII.getValue(), newJ = newJI.getValue();
                map[newI][newJ] = currBall;
                balls[newI][newJ] = labelCurrBall;
                postProcess(newI, newJ); // functions of bomb or rainbow; eliminate or fall
                // reset labelCurrBall and labelNextBall
                generateNewBall();
                // determine if wins or fails!
                if (fails()) {
                    try {
                        AudioStream audioLose = new AudioStream(BubbleGame.class.getResourceAsStream("../sound/lose.wav"));
                        AudioPlayer.player.start(audioLose);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    index.gamePage.gameOver("You lose!");
                } else if (wins()) {
                    try {
                        AudioStream audioWin = new AudioStream(BubbleGame.class.getResourceAsStream("../sound/win.wav"));
                        AudioPlayer.player.start(audioWin);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    index.gamePage.gameOver("You win!");
                }
            }
        }
    }

}

abstract class MouseAdapter implements MouseListener, MouseWheelListener, MouseMotionListener {
    public void mouseClicked(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseWheelMoved(MouseWheelEvent e){}
    public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){}
}
