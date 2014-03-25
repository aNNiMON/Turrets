package com.annimon.turrets;

import static com.annimon.turrets.Constants.MENU_FONT_NAME;
import static com.annimon.turrets.Constants.SCREEN_DIMENSION;
import com.annimon.turrets.util.ExceptionHandler;
import com.annimon.turrets.util.Prefs;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author aNNiMON
 */
public final class OptionsPanel extends JPanel {
    
    private static final Font FONT = new Font(MENU_FONT_NAME, 1, 30);
    private static final int VERT_INSET = 80, HOR_INSET = 50;
    
    private final JCheckBox soundCheckBox;
    private final JFormattedTextField ipTextField;
    private final JButton saveButton;

    public OptionsPanel() {
        setPreferredSize(SCREEN_DIMENSION);
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        
        final JLabel soundLabel = new JLabel("Sound");
        soundLabel.setForeground(Color.GRAY);
        soundLabel.setFont(FONT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 0.4;
        gbc.insets = new Insets(0, 0, VERT_INSET, HOR_INSET);
        add(soundLabel, gbc);
        
        soundCheckBox = new JCheckBox();
        soundCheckBox.setBorder(null);
        soundCheckBox.setSelected(Prefs.getInstance().soundEnabled());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 0, VERT_INSET, 0);
        add(soundCheckBox, gbc);
        
        final JLabel ipLabel = new JLabel("Server IP");
        ipLabel.setForeground(Color.GRAY);
        ipLabel.setFont(FONT);
        gbc = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        gbc.insets = new Insets(0, 0, 0, HOR_INSET);
        add(ipLabel, gbc);
        
        ipTextField = new JFormattedTextField();
        final String pattern = "###.###.###.###";
        try {
            ipTextField.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter(pattern)));
        } catch (ParseException ex) {
            ExceptionHandler.handle(ex);
        }
        ipTextField.setBackground(Color.BLACK);
        ipTextField.setForeground(Color.GRAY);
        ipTextField.setBorder(null);
        ipTextField.setFont(FONT);
        ipTextField.setText(Prefs.getInstance().lastIp());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.6;
        add(ipTextField, gbc);
        
        saveButton = new JButton("Save");
        saveButton.setForeground(Color.GRAY);
        saveButton.setFont(FONT);
        saveButton.setBorderPainted(false);
        saveButton.setFocusPainted(false);
        saveButton.setContentAreaFilled(false);
        saveButton.addActionListener(saveActionListener);
        gbc = new GridBagConstraints();
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(VERT_INSET, 0, 0, 0);
        add(saveButton, gbc);
    }
    
    private final ActionListener saveActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            final boolean soundEnabled = soundCheckBox.isSelected();
            Sound.setEnabled(soundEnabled);
            Sound.CLICK.play();
            Prefs.getInstance().setSoundEnabled(soundEnabled);
            
            final String ip = ipTextField.getText();
            if (validateIp(ip)) Prefs.getInstance().setLastIp(ip);
            Main.getInstance().switchToMainMenu();
        }
    };
    
    private boolean validateIp(String ip) {
        try {
            InetAddress.getByName(ip);
            return true;
        } catch (UnknownHostException ex) {
            return false;
        }
    }

};
