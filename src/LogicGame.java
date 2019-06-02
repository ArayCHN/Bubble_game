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

public class LogicGame {
    int[][] map;
    JLabel[][] balls;
    int numRow, numCol, numTyp, totalRows;
    JPanel panelGame;
    ImageIcon[] textures = new ImageIcon[7];
    ImageIcon cannonTexture;
    BufferedImage cannonImage;
    Random rand = new Random();
    int r = 29; // size of each ball
    int currBall, nextBall;
    int cannonCenterX, cannonCenterY;
    JLabel labelCurrBall, labelNextBall, cannon;

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

    public void moveToClosest(int x, int y) {
        double existingX, existingY;
        for (int i = 0; i < numRow + 1; i++)
                for (int j = 0; j < numCol; j++) {
                    existingX = ((i % 2) + 2.1 * j + 1) * r - r;
                    existingY = (1 + 1.8 * i) * r - r;
                    if (distance(existingX, existingY, x, y) <= r) {
                        // move from here to existingX, existingY
                        labelCurrBall.setBounds((int)existingX, (int)existingY, 2 * r, 2 * r);
                        break;
                    }
                }
    }

    public void rotateCannon(double theta) {
        RotatedIcon rotatedCannon = new RotatedIcon(cannonTexture, theta);
        int newWidth = rotatedCannon.getIconWidth();
        int newHeight = rotatedCannon.getIconHeight();
        cannon.setBounds(cannonCenterX - newWidth / 2, cannonCenterY - newHeight / 2, newWidth, newHeight);
        cannon.setIcon(rotatedCannon);
    }

    public void initializeGame(int level, JPanel panelGameIn) {
        panelGame = panelGameIn;
        panelGame.setLayout(null); // set absolute layout
        switch (level) {
            case 1: numRow = 3 + 1; numCol = 7; numTyp = 3; break; // the additional 1 row for buffer
            case 2: numRow = 4 + 1; numCol = 7; numTyp = 4; break;
            case 3: numRow = 5 + 1; numCol = 7; numTyp = 5; break;
            default: numRow = 3 + 1; numCol = 7; numTyp = 3;
        }
        totalRows = 8;
        // 8 rows in total! but we need 9 rows for temporary placement for the additional ball when
        // num of balls exceeds the lifeline and the game is over
        map = new int[totalRows + 1][numCol]; // initialized to 0
        balls = new JLabel[totalRows + 1][numCol];
        for (int i = 0; i < numRow; i ++)
            for (int j = 0; j < numCol; j ++) {
                map[i][j] = rand.nextInt(numTyp) + 1;
                double xCenter = (i % 2) + 2.1 * j + 1, yCenter = 1 + 1.8 * i;
                balls[i][j] = renderBalls(xCenter, yCenter, map[i][j]); // graphics
                // System.out.println(String.valueOf(i) + ", " + String.valueOf(j));
            }

        cannon = new JLabel();
        cannon.setIcon(cannonTexture);
        // cannon rotates around: 225, 500
        cannonCenterX = 225; cannonCenterY = 500;
        cannon.setBounds(cannonCenterX - 50, cannonCenterY - 50, 100, 100);
        panelGame.add(cannon);

        currBall = rand.nextInt(numTyp);
        nextBall = rand.nextInt(numTyp);
        labelCurrBall = new JLabel();
        labelNextBall = new JLabel();
        labelCurrBall.setIcon(textures[currBall]);
        labelNextBall.setIcon(textures[nextBall]);
        labelCurrBall.setBounds(200, 500, 2 * r, 2 * r);
        labelNextBall.setBounds(100, 500, 2 * r, 2 * r);
        panelGame.add(labelCurrBall);
        panelGame.add(labelNextBall);

        GameMouseListener listener = new GameMouseListener();
        panelGame.addMouseListener(listener);
        panelGame.addMouseMotionListener(listener);
    }

    public LogicGame(int level, JPanel panelGameIn) {
        textures[0] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/1.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[1] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/2.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[2] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/3.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[3] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/4.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[4] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/5.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[5] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/6.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));
        textures[6] = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/7.png")).getImage().getScaledInstance(2*r, 2*r, Image.SCALE_DEFAULT));

        cannonTexture = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/cannon.png")).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        try {
            cannonImage = ImageIO.read(getClass().getResource("../img/cannon.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initializeGame(level, panelGameIn);
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
            
        }

        public void mouseReleased(MouseEvent e) {
            // launch the ball
            //System.out.println("Mouse released!");
            int v = 10;
            int step = 0;
            double dx = deltaX / (double)Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            double dy = deltaY / (double)Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            Thread runBall = new Thread(new RunningBallRenderer(dx, dy, v));
            runBall.start();
        }
    }

    class RunningBallRenderer implements Runnable {
        double dx, dy;
        int v, newX, newY;

        private boolean stopped(int newX, int newY) {
            int centerX = newX + r;
            int centerY = newY + r;
            double existingX, existingY;
            for (int i = 0; i < numRow; i++)
                for (int j = 0; j < numCol; j++) {
                    existingX = ((i % 2) + 2.1 * j + 1) * r;
                    existingY = (1 + 1.8 * i) * r;
                    if (map[i][j] != 0 && distance(existingX, existingY, centerX, centerY) <= 2.2 * r) {
                        // stop!
                        return true;
                    }
                }
            return false;
        }
        
        public RunningBallRenderer(double dxIn, double dyIn, int vIn) {
            dx = dxIn;
            dy = dyIn;
            v = vIn;
        }

        public void run() {
            int step = 0;
            newX = 200;
            newY = 500;
            while (!stopped(newX, newY)) { // find stop condition!
                newX += (int)(v * dx);
                newY += (int)(v * dy);
                if (newX <= 10 || newX >= 450) { // reflected
                    dx = -dx;
                }
                step ++;
                labelCurrBall.setBounds(newX, newY, 2 * r, 2 * r);
                try {
                    Thread.sleep(10);
                } catch(Exception e1) {}
            }
            // stopped, need to move to correct location!
            moveToClosest(newX, newY);
            // determine if there are 
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
