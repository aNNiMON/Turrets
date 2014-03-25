package com.annimon.turrets;

import static com.annimon.turrets.Constants.MENU_FONT_NAME;
import static com.annimon.turrets.Constants.SCREEN_DIMENSION;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author aNNiMON
 */
public final class HelpPanel extends JPanel {
    
    public HelpPanel() {
        setPreferredSize(SCREEN_DIMENSION);
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Main.getInstance().switchToMainMenu();
            }
        });
        
        final JLabel titleLabel = new JLabel("Turrets Online");
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font(MENU_FONT_NAME, 1, 38));
        titleLabel.setBorder(new EmptyBorder(50, 0, 50, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        final JLabel infoLabel = new JLabel();
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(infoLabel, BorderLayout.CENTER);
    }
};
