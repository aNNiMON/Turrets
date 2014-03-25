package com.annimon.turrets;

import static com.annimon.turrets.Constants.MENU_FONT_NAME;
import static com.annimon.turrets.Constants.SCREEN_DIMENSION;
import com.annimon.turrets.util.ExceptionHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author aNNiMON
 */
public final class HelpPanel extends JPanel {
    
    public HelpPanel() {
        setPreferredSize(SCREEN_DIMENSION);
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        
        final JLabel titleLabel = new JLabel("Turrets Online");
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font(MENU_FONT_NAME, 1, 38));
        titleLabel.setBorder(new EmptyBorder(100, 0, 50, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        final JEditorPane infoPane = new JEditorPane();
        infoPane.setContentType("text/html");
        infoPane.setEditable(false);
        infoPane.setOpaque(false);
        infoPane.setText("<html>" +
                "<head><style type=\"text/css\">p {text-align: center; color: #FFFFFF; font-size: 24pt;}</style></head>" +
                "<p>Turrets Online is peer-to-peer network game.<br/>" +
                "The goal of the game is to hit enemy with turret's missile.</p>" +
                "<br/><p>Author: Victor aNNiMON Melnik<br/>"+
                "Source code available at <a href=\"https://bitbucket.org/annimon/turrets\">BitBucket</a> or " + 
                "<a href=\"https://github.com/aNNiMON/turrets\">GitHub</a></p>" +
                "<br/><p>Ukraine, Donetsk 2014</p>" +
                "</html>");
        infoPane.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent hle) {
                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
                    openBrowser(hle.getURL().toString());
                }
            }
        });
        add(infoPane, BorderLayout.CENTER);
        
        final JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Main.getInstance().switchToMainMenu();
            }
        });
        backButton.setForeground(Color.GRAY);
        backButton.setFont(new Font(MENU_FONT_NAME, 1, 30));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(new EmptyBorder(0, 0, 75, 0));
        add(backButton, BorderLayout.SOUTH);
    }
    
    private void openBrowser(String url) {
        final String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Windows")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else {
                final String[] browsers = {"firefox", "opera", "konqueror",
                    "epiphany", "mozilla", "netscape", "chrome"};
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(
                            new String[]{"which", browsers[count]})
                            .waitFor() == 0)
                        browser = browsers[count];
                }
                Runtime.getRuntime().exec(new String[]{browser, url});
            }
        } catch (IOException | InterruptedException ex) {
            ExceptionHandler.handle(ex);
        }
    }
};
