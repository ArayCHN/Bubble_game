// the index page of the game
package bubblegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

class JFrameWithBg extends JFrame {
    public JFrameWithBg(String name) {
        setTitle(name);
        int windowWidth = 460, windowHeight = 640;
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        ImageIcon background = 
            new ImageIcon(new ImageIcon(BubbleGame.class.getResource("../img/background.png"))
                        .getImage().getScaledInstance(windowWidth, windowHeight, Image.SCALE_DEFAULT));// 背景图片
        JLabel labelBg = new JLabel(background);// 把背景图片显示在一个标签里面
        labelBg.setBounds(0, 0, windowWidth, windowHeight);
        this.getLayeredPane().setLayout(null);
        this.getLayeredPane().add(labelBg, new Integer(Integer.MIN_VALUE));
    }
}

public class Index{
    JFrameWithBg frame;
    JPanel panelIndex;
    JButton buttonStart1, buttonStart2, buttonStart3, buttonQuit;
    GameInterface gamePage;
    // public static Index indexPage;

    public Index() {
        frame = new JFrameWithBg("Bubble Game");

        panelIndex = new JPanel();
        panelIndex.setLayout(new FlowLayout());

        buttonStart1 = new JButton("LEVEL 1");
        buttonStart2 = new JButton("LEVEL 2");
        buttonStart3 = new JButton("LEVEL 3");
        buttonQuit = new JButton("QUIT");

        buttonStart1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                panelIndex.setVisible(false);
                gamePage = new GameInterface(1, frame, panelIndex);
            }
        });

        buttonStart2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                panelIndex.setVisible(false);
                gamePage = new GameInterface(2, frame, panelIndex);
            }
        });

        buttonStart3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                panelIndex.setVisible(false);
                gamePage = new GameInterface(3, frame, panelIndex);
            }
        });

        buttonQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panelIndex.setLayout(new BoxLayout(panelIndex, BoxLayout.Y_AXIS));
        buttonStart1.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonStart2.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonStart3.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonQuit.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelIndex.add(buttonStart1);
        panelIndex.add(buttonStart2);
        panelIndex.add(buttonStart3);
        panelIndex.add(buttonQuit);

        //panelIndex.setVisible(true);
        panelIndex.setOpaque(false);
        frame.add(panelIndex);
        // frame.pack();

        JPanel contentPanel = (JPanel)frame.getContentPane();
        contentPanel.setOpaque(false);

        frame.setBounds(100, 100, 460, 640);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void reset() {
        panelIndex.setVisible(true);
    }
}