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
import sun.audio.*;

class JFrameWithBg extends JFrame {
    public JFrameWithBg(String name) {
        setTitle(name);
        int windowWidth = GlobalSettings.windowWidth, windowHeight = GlobalSettings.windowHeight;
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        ImageIcon background = 
            new ImageIcon(new ImageIcon(BubbleGame.class.getResource("img/background.png"))
                        .getImage().getScaledInstance(windowWidth, windowHeight, Image.SCALE_DEFAULT)); // background
        JLabel labelBg = new JLabel(background); // put background image in a label
        labelBg.setBounds(0, 0, windowWidth, windowHeight);
        this.getLayeredPane().setLayout(null);
        this.getLayeredPane().add(labelBg, new Integer(Integer.MIN_VALUE));
    }
}

public class Index{
    JFrameWithBg frame;
    JPanel panelIndex;
    JButton buttonStart1, buttonStart2, buttonStart3, buttonStart4, buttonQuit;
    GameInterface gamePage;
    Audios audios;
    // public static Index indexPage;

    public Index(Audios audiosIn) {
        frame = new JFrameWithBg("Bubble Game");
        createNew();
        frame.setBounds(100, 100, GlobalSettings.windowWidth, GlobalSettings.windowHeight);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        audios = audiosIn;
    }

    public void createNew() {
        Index myIndex = this;
        panelIndex = new JPanel();
        panelIndex.setLayout(new FlowLayout());

        ImageIcon buttonIcon = new ImageIcon(new ImageIcon(BubbleGame.class.getResource("img/button.png")).getImage().getScaledInstance(250, 100, Image.SCALE_DEFAULT));
        buttonStart1 = new JButton("LEVEL 1 ");
        buttonStart1.setBorder(BorderFactory.createEmptyBorder());
        buttonStart1.setHorizontalTextPosition(JButton.CENTER);
        buttonStart1.setVerticalTextPosition(JButton.CENTER);
        buttonStart1.setIcon(buttonIcon);

        buttonStart2 = new JButton("LEVEL 2 ");
        buttonStart2.setBorder(BorderFactory.createEmptyBorder());
        buttonStart2.setHorizontalTextPosition(JButton.CENTER);
        buttonStart2.setVerticalTextPosition(JButton.CENTER);
        buttonStart2.setIcon(buttonIcon);

        buttonStart3 = new JButton("LEVEL 3 ");
        buttonStart3.setBorder(BorderFactory.createEmptyBorder());
        buttonStart3.setHorizontalTextPosition(JButton.CENTER);
        buttonStart3.setVerticalTextPosition(JButton.CENTER);
        buttonStart3.setIcon(buttonIcon);

        buttonStart4 = new JButton(" CUSTOM ");
        buttonStart4.setBorder(BorderFactory.createEmptyBorder());
        buttonStart4.setHorizontalTextPosition(JButton.CENTER);
        buttonStart4.setVerticalTextPosition(JButton.CENTER);
        buttonStart4.setIcon(buttonIcon);

        buttonQuit = new JButton("  QUIT  ");
        buttonQuit.setBorder(BorderFactory.createEmptyBorder());
        buttonQuit.setHorizontalTextPosition(JButton.CENTER);
        buttonQuit.setVerticalTextPosition(JButton.CENTER);
        buttonQuit.setIcon(buttonIcon);

        buttonStart1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                panelIndex.setVisible(false);
                try {
                    AudioStream audioBegin = new AudioStream(BubbleGame.class.getResourceAsStream("sound/begin.wav"));
                    AudioPlayer.player.start(audioBegin);
                } catch(Exception e0) {
                    e0.printStackTrace();
                }
                gamePage = new GameInterface(1, frame, panelIndex, myIndex, "");
            }
        });

        buttonStart2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                panelIndex.setVisible(false);
                try {
                    AudioStream audioBegin = new AudioStream(BubbleGame.class.getResourceAsStream("sound/begin.wav"));
                    AudioPlayer.player.start(audioBegin);
                } catch(Exception e1) {
                    e1.printStackTrace();
                }
                gamePage = new GameInterface(2, frame, panelIndex, myIndex, "");
            }
        });

        buttonStart3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                panelIndex.setVisible(false);
                try {
                    AudioStream audioBegin = new AudioStream(BubbleGame.class.getResourceAsStream("sound/begin.wav"));
                    AudioPlayer.player.start(audioBegin);
                } catch(Exception e2) {
                    e2.printStackTrace();
                }
                gamePage = new GameInterface(3, frame, panelIndex, myIndex, "");
            }
        });

        buttonStart4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.getContentPane().removeAll();
                // open file dialog
                FileDialog fd = new FileDialog(frame, "Choose a customs file", FileDialog.LOAD);
                fd.setFile("*.txt");
                fd.setVisible(true);
                String filename = fd.getFile();
                if (filename != null) {
                    try {
                        AudioStream audioBegin = new AudioStream(BubbleGame.class.getResourceAsStream("sound/begin.wav"));
                        AudioPlayer.player.start(audioBegin);
                    } catch(Exception e2) {
                        e2.printStackTrace();
                    }
                    panelIndex.setVisible(false);
                    // read: time, row, column, color
                    gamePage = new GameInterface(3, frame, panelIndex, myIndex, filename);
                }
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
        buttonStart4.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonQuit.setAlignmentX(Component.CENTER_ALIGNMENT);

        int buttonSize = 38;
        Font buttonFont = new Font("System", Font.BOLD, buttonSize);
        buttonStart1.setFont(buttonFont);
        buttonStart2.setFont(buttonFont);
        buttonStart3.setFont(buttonFont);
        buttonStart4.setFont(buttonFont);
        buttonQuit.setFont(buttonFont);


        int buttonSpace = 20;
        panelIndex.add(Box.createRigidArea(new Dimension(0, 50)));
        panelIndex.add(buttonStart1);
        panelIndex.add(Box.createRigidArea(new Dimension(0, buttonSpace)));
        panelIndex.add(buttonStart2);
        panelIndex.add(Box.createRigidArea(new Dimension(0, buttonSpace)));
        panelIndex.add(buttonStart3);
        panelIndex.add(Box.createRigidArea(new Dimension(0, buttonSpace)));
        panelIndex.add(buttonStart4);
        panelIndex.add(Box.createRigidArea(new Dimension(0, buttonSpace)));
        panelIndex.add(buttonQuit);

        //panelIndex.setVisible(true);
        panelIndex.setOpaque(false);
        frame.add(panelIndex);
        // frame.pack();

        JPanel contentPanel = (JPanel)frame.getContentPane();
        contentPanel.setOpaque(false);
    }

    public void reset() {
        frame.removeAll();
        createNew();
        frame.revalidate();
        frame.repaint();
    }
}