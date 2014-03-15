package com.annimon.turrets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Main menu
 * @author aNNiMON
 */
public class MenuPanel extends JPanel {
    
    private static final String[] MENU_ITEMS = {
        "Create Game", "Join", "Options", "Help", "Exit"
    };
    
    private static final Color TITLE_COLOR = new Color(0x007700);
    
    private final JList<String> menuList;
    
    public MenuPanel() {
        // Init menu list
        menuList = new JList<>(MENU_ITEMS);
        menuList.setOpaque(false);
        menuList.setFont(new Font("Stencil", Font.BOLD, 42));
        menuList.setForeground(Color.GRAY);
        menuList.setBackground(Color.BLACK);
        menuList.setSelectionBackground(Color.BLACK);
        menuList.setSelectionForeground(TITLE_COLOR);
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) menuList.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        menuList.setBorder(new EmptyBorder(0, 100, 50, 100));
        // Remove border for selected item
        UIManager.put("List.focusCellHighlightBorder", BorderFactory.createEmptyBorder());
        // Listeners
        menuList.addListSelectionListener(listSelectionListener);
        
        // Init title
        final JLabel titleLabel = new JLabel("Turrets Online");
        titleLabel.setForeground(TITLE_COLOR);
        titleLabel.setFont(new Font("Stencil", Font.BOLD, 70));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Init panel
        setPreferredSize(Constants.SCREEN_DIMENSION);
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        add(menuList, BorderLayout.SOUTH);
        add(titleLabel, BorderLayout.CENTER);
    }
    
    private final ListSelectionListener listSelectionListener = new ListSelectionListener() {

        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (event.getValueIsAdjusting()) return;
            
            final int index = menuList.getSelectedIndex();
            switch (index) {
                case 0:
                    GameCanvas canvas = new GameCanvas();
                    Main.getInstance().switchLayout(canvas);
                    canvas.start();
                    break;
                case 4:
                    System.exit(0);
                    break;
            }
        }
    };
}
