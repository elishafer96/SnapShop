/*
 * TCSS 305 - Winter 2017
 * Assignment 4 - SnapShot
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import filters.EdgeDetectFilter;
import filters.EdgeHighlightFilter;
import filters.Filter;
import filters.FlipHorizontalFilter;
import filters.FlipVerticalFilter;
import filters.GrayscaleFilter;
import filters.SharpenFilter;
import filters.SoftenFilter;
import image.PixelImage;

/**
 * The graphical user interface for the SnapShot program.
 * 
 * @author Eli Shafer
 * @version Winter 2017
 */
public class SnapShopGUI extends JFrame {
    
    private static final String NAME_OF_JFRAME = "SnapShot Winter 2017";
        
    private final Map<Filter, JButton> myFilterButtonMap;
    
    private final List<Filter> myFilters;
    
    private final JLabel myImageDisplay;
    
    private final List<JButton> myOptionButtons;
    
    private PixelImage myImage;
    
    SnapShopGUI() {
        super(NAME_OF_JFRAME);
        
        myOptionButtons = new ArrayList<>();
        myFilterButtonMap = new HashMap<>();
        myImageDisplay = new JLabel();
        
        myOptionButtons.add(new JButton("Open..."));
        myOptionButtons.add(new JButton("Save As..."));
        myOptionButtons.add(new JButton("Close image"));
       
        myFilters = new ArrayList<>();
        myFilters.add(new EdgeDetectFilter());
        myFilters.add(new EdgeHighlightFilter());
        myFilters.add(new FlipHorizontalFilter());
        myFilters.add(new FlipVerticalFilter());
        myFilters.add(new GrayscaleFilter());
        myFilters.add(new SharpenFilter());
        myFilters.add(new SoftenFilter());
        
        

        for (final Filter filter : myFilters) {
            myFilterButtonMap.put(filter, new JButton(filter.getDescription()));
            myFilterButtonMap.get(filter).addActionListener(theEvent -> {
                filter.filter(myImage);
                myImageDisplay.setIcon(new ImageIcon(myImage));
            });
        }
    }

    /**
     * Sets the SnapShopGUI to its initial state.
     */
    public void start() {
        setUpImagePanel();
        
        final JPanel buttonPanel = new JPanel(new BorderLayout());
        
        buttonPanel.add(createFilterPanel(), BorderLayout.NORTH);
        buttonPanel.add(createOptionPanel(), BorderLayout.SOUTH);
        
        this.add(buttonPanel, BorderLayout.WEST);
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setMinimumSize(getSize());
        setVisible(true);
    }

    /**
     * Sets up the image panel.
     */
    private void setUpImagePanel() {
        final JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(myImageDisplay);
    }

    /**
     * Creates and returns the JButton filter panel.
     *
     * @return the JButton filter panel
     */
    private JPanel createFilterPanel() {
        final JPanel filterPanel = new JPanel(new GridLayout(7, 1));
        for (Filter myFilter : myFilters) {
            filterPanel.add(myFilterButtonMap.get(myFilter));
            myFilterButtonMap.get(myFilter).setEnabled(false);
        }
        return filterPanel;
    }
    
    /**
     * Creates and returns the JButton option panel.
     * 
     * @return the JButton option panel
     */
    private JPanel createOptionPanel() {
        final JPanel optionPanel = new JPanel(new GridLayout(3, 1));
        final ActionListener optionListener = new OptionActionListener();
        
        for (final JButton button : myOptionButtons) {
            optionPanel.add(button);
            if (myOptionButtons.indexOf(button) != 0) {
                button.setEnabled(false);
            }
            button.addActionListener(optionListener);
        }
        
        return optionPanel;
    }
    
    /**
     * The ActionListener used by the option buttons in SnapShopGUI.
     * 
     * @author Eli Shafer
     * @version Winter 2017
     */
    private class OptionActionListener implements ActionListener {

        /** The JFileChooser that will be utilized by this MenuActionListener. */
        private final JFileChooser myFileChooser;
        
        /**
         * Private constructor to inhibit external instantiation.
         */
        OptionActionListener() {
            super();
            myFileChooser = new JFileChooser();
            
        }
        
        /**
         * Performs the actions for the "Open...", "Save As", and "Close Image" 
         * JButtons.
         */
        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            if (theEvent.getSource() == myOptionButtons.get(0)) {
                openImage();
            } else if (theEvent.getSource() == myOptionButtons.get(1)) {
                saveImage();
            } else if (theEvent.getSource() == myOptionButtons.get(2)) {
                closeImage();
            }
        }
        
        /**
         * Closes the image that is currently open.
         */
        private void closeImage() {
            myImageDisplay.setIcon(null);
            myImage.flush();
            for (int i = 1; i < myOptionButtons.size(); i++) {
                myOptionButtons.get(i).setEnabled(false);
            }
            for (final JButton button : myFilterButtonMap.values()) {
                button.setEnabled(false);
            }
            setMinimumSize(new Dimension());
            pack();
            setMinimumSize(getSize());
        }
        
        /**
         * Opens a file chooser to choose an image file.
         */
        private void openImage() {
            final int returnVal = myFileChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                final File file = myFileChooser.getSelectedFile();
                try {
                    myImage = PixelImage.load(file);
                    myImageDisplay.setIcon(new ImageIcon(myImage));
                    for (final JButton button : myOptionButtons) {
                        button.setEnabled(true);
                    }
                    for (final JButton button : myFilterButtonMap.values()) {
                        button.setEnabled(true);
                    }
                    pack();
                    setMinimumSize(getSize());
                } catch (final IOException e) {
                    JOptionPane.showMessageDialog(null, "The selected file did not "
                                    + "contain an image!", "Error!",
                                    JOptionPane.ERROR_MESSAGE);
                    myOptionButtons.get(0).doClick();
                }
            }
            
        }
        
        /**
         * Writes the image that is currently opened.
         */
        private void saveImage() {
            final int returnVal = myFileChooser.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                final File file = myFileChooser.getSelectedFile();
                try {
                    myImage.save(file);
                } catch (final IOException e) {
                    
                }
            }
        }
    }
}