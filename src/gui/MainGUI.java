package gui;

/**
 * The main graphical user interface for the system
 * @author jonathanrainer
 */
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.*;



public class MainGUI {
    private JFrame mainFrame;
    private GridLayout oneColumnThreeRows;
    private GridLayout oneColumnTwoRows;
    private GridLayout twoColumnsFiveRows;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem preferences;
    private JMenuItem exit;
    private JPanel topPanel;
    private JLabel logo;
    private JPanel middlePanel;
    private JPanel middlePanelColumnSplit;
    private JLabel title;
    private JComboBox orderList;
    private JLabel orderLabel;
    private JComboBox destinationList;
    private JLabel destinationLabel;
    private JLabel status;
    private JLabel statusLabel;
    private JLabel progressLabel;
    private JProgressBar progressBar;
    private JPanel bottomPanel;
    private JButton fulfillOrder;
    private JButton refresh;
    private JTextArea orderTextBox;
    private JScrollPane orderTextBoxScrollPane;
    
    // Load in the current Greenbelt Logo
    ClassLoader cldr = this.getClass().getClassLoader();
    
    java.net.URL gb_logo   = cldr.getResource("gui/gb_logo.png");
    ImageIcon greenbeltLogo = new ImageIcon(gb_logo);

    /**
     * Creates the Graphical User Interface to allow users to interact with
     * the system.
     */
    public MainGUI()
    {
        // Create the main frame to store all other items within
        mainFrame = new JFrame("Greenbelt Automatic Order Fulfillment System");
        
        // Create the top menu bar and its two sub menus
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        preferences = new JMenuItem("Preferences");
        exit = new JMenuItem("Exit");
        menuBar.add(fileMenu);
        fileMenu.add(preferences);
        fileMenu.add(exit);
        mainFrame.setJMenuBar(menuBar);
        
        // Create and set up the layouts for each of the grid sections within 
        // the layout as a whole.
        oneColumnThreeRows = new GridLayout(3,1);
        oneColumnThreeRows.setHgap(30);
        oneColumnThreeRows.setVgap(5);
        oneColumnTwoRows = new GridLayout(2,1);
        oneColumnTwoRows.setHgap(30);
        oneColumnTwoRows.setVgap(5);
        twoColumnsFiveRows = new GridLayout(5,2);
        twoColumnsFiveRows.setHgap(0);
        twoColumnsFiveRows.setVgap(5);
        
        // Set the layout and size on the main window
        mainFrame.setLayout(oneColumnThreeRows);
        mainFrame.setSize(700, 850);
        
        // Add the logo to the top panel in the frame
        topPanel = new JPanel();
        logo = new JLabel(greenbeltLogo);
        topPanel.add(logo);
        topPanel.setVisible(true);
        
        // Add the title text to the middle Panel
        middlePanel = new JPanel();
        middlePanel.setLayout(oneColumnTwoRows);
        String titleText = "<html><h2><b>Greenbelt Automatic Order Fulfillment"
                + " System</h2></b></html>";
        title = new JLabel(titleText, JLabel.CENTER);
        middlePanel.add(title);
        
        // Split the middle pane into a 2 Column, 5 Row table to add in further
        // elements
        middlePanelColumnSplit = new JPanel(twoColumnsFiveRows);
        middlePanel.add(middlePanelColumnSplit);
        
        // Create and add in the order label and the associated combo box
        String orderLabelText = "<html><h3><b>Order to be Fulfilled</b>"
                + "</h3></html>";
        orderLabel = new JLabel(orderLabelText, JLabel.CENTER);
        middlePanelColumnSplit.add(orderLabel);
        orderList = new JComboBox();
        middlePanelColumnSplit.add(orderList);
        
        // Create and add in the destination label and associated combo box
        String destinationLabelText = "<html><h3><b>Destination"
                + "</b></h3></html>";
        destinationLabel = new JLabel(destinationLabelText, JLabel.CENTER);
        middlePanelColumnSplit.add(destinationLabel);
        destinationList = new JComboBox();
        middlePanelColumnSplit.add(destinationList);
        
        // Create and add in the status label and progress bar
        String statusLabelText = "<html><h3><b>Status"
                + "</b></h3></html>";
        statusLabel = new JLabel(statusLabelText, JLabel.CENTER);
        middlePanelColumnSplit.add(statusLabel);
        String statusText = "Ready";
        status = new JLabel(statusText, JLabel.CENTER);
        middlePanelColumnSplit.add(status);
        String progressText = "<html><h3><b>Progress"
                + "</b></h3></html>";
        progressLabel = new JLabel(progressText, JLabel.CENTER);
        progressLabel.setVisible(true);
        middlePanelColumnSplit.add(progressLabel);
        progressBar = new JProgressBar();
        progressBar.setVisible(true);
        Dimension progBarDim = new Dimension(100,100);
        progressBar.setPreferredSize(progBarDim);
        middlePanelColumnSplit.add(progressBar);
        middlePanel.setVisible(true);
        
        // Set the layout on the bottom panel
        bottomPanel = new JPanel(oneColumnThreeRows);
        fulfillOrder = new JButton("<html><h1>Fulfill Order</h1></html>");
        
        // Add in the refresh and fulfill order buttons
        refresh = new JButton("<html><h1>Refresh</h1></html>");
        orderTextBox = new JTextArea("This is where the list of talks related to"
                + " a particular order will be displayed");
        orderTextBoxScrollPane = new JScrollPane(orderTextBox);
        orderTextBoxScrollPane.setVerticalScrollBarPolicy
                (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        bottomPanel.add(orderTextBoxScrollPane);
        bottomPanel.add(fulfillOrder);
        bottomPanel.add(refresh);
        bottomPanel.setVisible(true);
        
        //Add the three panels to the panel to create the final layout
        mainFrame.add(topPanel);
        mainFrame.add(middlePanel);
        mainFrame.add(bottomPanel);
        mainFrame.setVisible(true);
    }
    
    /**
     * Sets the text in the box in the lower part of the GUI.
     * 
     * @param orderText The new text to be displayed in the text box at the
     * bottom of the GUI.
     */
    public void setOrderTextBox(String orderText) 
    {
        orderTextBox.setText(orderText);
    }
    
    /**
     * Returns the ComboBox containing all the orders it's possible to fulfill.
     * @return The ComboBox containing all the order it's possible to fulfill.
     */
    public JComboBox getOrderList()
    {
       return orderList;
    }
    
   /**
    * Returns the list of destinations
    * @return The ComboBox containing all the possible destinations
    */
    public JComboBox getDestinationList() 
    {
        return destinationList;
    }
    
    /**
     * Return the Fulfill Order button
     * @return The fulfill order JButton
     */
    public JButton getFulfillOrder() 
    {
        return fulfillOrder;
    }

    /**
     * Return the Refresh Button
     * @return The Refresh JButton
     */
    public JButton getRefresh() 
    {
        return refresh;
    }
    
    /**
     * Return the current label in the "Status" field
     * @return The JLabel that explains the status.
     */
    public JLabel getStatus()
    {
        return status;
    }
   
    /**
     * Return the whole of the main frame
     * @return The JFrame that contains all other content
     */
    public JFrame getMainFrame()
    {
        return mainFrame;
    }

    /**
     * Return the Progress Bar 
     * @return The JProgressBar that illustrates how much progress has been made
     */
    public JProgressBar getProgressBar() 
    {
        return progressBar;
    }
    
    /**
     * Update the text in the status field so other methods can easily change 
     * it.
     * @param updatedText The new text to display.
     */
    public void updateStatus(String updatedText)
    {
        status.setText(updatedText);
    }
    

    public JMenuItem getExit() {
        return exit;
    }

    public JMenu getFileMenu() {
        return fileMenu;
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    public JMenuItem getPreferences() {
        return preferences;
    }
}
