/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import config.Config;
import io.CSVEngine;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import javax.swing.*;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author jonathanrainer
 */
public class PreferencesWindow {
    
    private JFrame preferencesWindow;
    private JPanel contentPane;
    private GridLayout mainLayout;
    private Config configuration;
    private ArrayList<JLabel> labels;
    private ArrayList<JTextField> textFields;
    private JButton saveButton;
    private JButton closeButton;
    private JButton exportButton;
    private JButton importButton;
    private JFileChooser fc;
    
    public PreferencesWindow(Config configuration)
    {
        this.configuration = configuration;
        
        // Initialise a few other variables
        labels = new ArrayList<JLabel>();
        textFields = new ArrayList<JTextField>();
        fc = new JFileChooser();
        
        refreshPreferencesWindow(configuration.getConfigs());
    }
    
    public JFrame getPrefrencesWindow()
    {
        return preferencesWindow;
    }
    
    private void updateConfigurationForSaving()
    {
        HashMap<String, String> buffer = configuration.getConfigs();
        Iterator it1 = labels.iterator();
        Iterator it2 = textFields.iterator();
        while(it1.hasNext() && it2.hasNext())
        {
            JLabel labelBuffer = (JLabel) it1.next();
            String parameter = labelBuffer.getText();
            JTextField textFieldBuffer = (JTextField) it2.next();
            String value = textFieldBuffer.getText();

            buffer.put(parameter, value);
        }
       configuration.setConfiguration(buffer);
    }
    
    private void refreshPreferencesWindow(HashMap<String, String> configs)
    {
        configuration.setConfiguration(configs);
        
        labels.removeAll(labels);
        textFields.removeAll(textFields);
        
        
        // Create the preferences window
        preferencesWindow = new JFrame("Preferences");
        contentPane = new JPanel();
        preferencesWindow.setSize(550, 500);
        
        // Set up the layout
        mainLayout = new GridLayout((configuration.getConfigs().size() + 6),2);
        contentPane.setLayout(mainLayout);
        
        // Add in the title label
        String titleLabelText = "<html><h2> Preferences - "
        + "Configuration </h2></html>";
        JLabel titleLabel = new JLabel(titleLabelText, JLabel.CENTER);
        JLabel blank3 = new JLabel();
        contentPane.add(titleLabel);
        contentPane.add(blank3);
        
        // Add in a row of blanks
        JLabel blank1 = new JLabel();
        JLabel blank2 = new JLabel();
        contentPane.add(blank1);
        contentPane.add(blank2);
        
        // Extract the set of keys from the Hash Map
        Set<String> keys = configuration.getConfigs().keySet();
        TreeSet<String> treeSet = new TreeSet<String>(keys); 
        
        // Iterate over the keys and place them into the GUI so they can be 
        // easily altered
        Iterator it1 = treeSet.iterator();
        
        while (it1.hasNext())
        {
            String labelText = (String) it1.next();
            JLabel label = new JLabel(labelText, JLabel.CENTER);
            JTextField textField = new JTextField();
            textField.setText(configuration.getConfigs().get(labelText));
            labels.add(label);
            contentPane.add(label);
            textFields.add(textField);
            contentPane.add(textField);
        }
        
        // Add in a row of blank JLabels
        
        JLabel blank4 = new JLabel();
        JLabel blank5 = new JLabel();
        contentPane.add(blank4);
        contentPane.add(blank5);
        
        // Create a button to save all these once they've been changed
        
        saveButton = new JButton("Save & Close");
        
        // Add an ActionListener to trip the altering the feature to write out
        // the changes to the csv file and then reload the configuration file.
        
        saveButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                updateConfigurationForSaving();
                preferencesWindow.dispose();
            }
        });
        contentPane.add(saveButton);
        
        // Create a button to simply close the window
        
        closeButton = new JButton("Close Without Saving");
        
        // Add an ActionListener to affect this closing
        
        closeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                preferencesWindow.dispose();
            }
        });
        contentPane.add(closeButton);
        
        // Create a button to export the current settings to an external file
        
        exportButton = new JButton("Export Settings");
        
        // Add an ActionListener to perform the process of exporting
        
        exportButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int returnVal = fc.showSaveDialog(preferencesWindow);
                if(returnVal == JFileChooser.APPROVE_OPTION)
                {
                    File selectedFile = fc.getSelectedFile();
                    if(!(selectedFile.getName().endsWith(".csv")))
                    {
                        String fileName = FilenameUtils.
                                removeExtension(selectedFile.getName());
                        fileName = fileName + ".csv";
                        selectedFile = new File(selectedFile.getParent() 
                                + File.separator + fileName);
                    }
                    CSVEngine csvEngine = new CSVEngine();
                    csvEngine.exportToCSVFile(selectedFile, 
                            labels, textFields);
                    JOptionPane.showMessageDialog(preferencesWindow,
                            "Settings Successfully Exported to CSV File.");
                }
                
            }
        });
        contentPane.add(exportButton);
        
        // Create a button to import settings from a remote file
        
        importButton = new JButton("Import Settings");
        
        // Add an ActionListener to perform the process of importing from an
        // external file
        
        importButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int returnVal = fc.showOpenDialog(preferencesWindow);
                preferencesWindow.dispose();
                if(returnVal == JFileChooser.APPROVE_OPTION)
                {
                    try
                    {
                        CSVEngine csvEngine = new CSVEngine();
                        HashMap<String, String> details = 
                                csvEngine.importCSVFile(fc.getSelectedFile());
                        refreshPreferencesWindow(details);
                        JOptionPane.showMessageDialog(preferencesWindow,
                            "Settings Successfully Imported From CSV File.");
                        
                    }
                    catch (Exception ie)
                    {
                        
                    }
                }
            }
        });
        contentPane.add(importButton);
        
        preferencesWindow.add(contentPane);
        preferencesWindow.setVisible(true);
    }
}
