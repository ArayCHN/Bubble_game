package bubblegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class Index{
    JFrame frame;
    JPanel panelIndex;
    JButton buttonStart, buttonQuit;

    public Index() {
        frame = new JFrame("Bubble Game");

        panelIndex = new JPanel();
        panelIndex.setLayout(new FlowLayout());

        buttonStart = new JButton("START");
        buttonQuit = new JButton("QUIT");

        buttonStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Starting Game!");
            }
        });

        buttonQuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panelIndex.add(buttonStart);
        panelIndex.add(buttonQuit);

        frame.add(panelIndex);

        frame.setBounds(100, 100, 460, 640);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}