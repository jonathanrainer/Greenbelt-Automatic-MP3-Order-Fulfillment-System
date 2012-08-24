package system;

import config.Config;
import gui.MainGUI;
import gui.PreferencesWindow;
import io.DestinationEngine;
import io.FileCopyEngine;
import io.QueryEngine;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.SizeFileComparator;

/**
 * A class to encompass the entire system.
 * 
 * @author jonathanrainer
 */

public class MainSystem {
    private MainGUI gui;
    private QueryEngine queryEngine;
    private ArrayList<Order> orderList;
    private ArrayList<File> destinationList;
    private Config configuration;
    private DestinationEngine destinationEngine;
    private FileCopyEngine fileCopyEngine;
    
/**
 * The constructor for the entire system, built according to the inputted
 * configuration file.
 * 
 * @param configFileName The location of the configuration file which details 
 * implementation specific details that couldn't be set in stone beforehand.
 */
    public MainSystem(File configurationFile)
    {
        gui = new MainGUI();
        try
        {
            configuration = new Config(configurationFile);
            queryEngine = new QueryEngine(configuration.getConfigs()
                .get("mysqlhost"), configuration.getConfigs()
                .get("mysqldatabase"), configuration.getConfigs()
                .get("mysqluser"), configuration.getConfigs()
                .get("mysqlpassword"));
            destinationEngine = new DestinationEngine(configuration.getConfigs().
                get("os"));
            destinationList = destinationEngine.generateDestinationList();
        }
        catch (IOException ioe)
        {
            gui.updateStatus("Cannot find configuration file");
            gui.setOrderTextBox("The configuration file either cannot be "
                    + "accessed or cannot be found make sure its location is"
                    + " correct. \n None of the menus will be accessible till"
                    + " this is rectified.");
        }
        catch (NullPointerException npe)
        {
            
        }
        try
        {
            orderList = queryEngine.generateOrderList(); 
        }
        catch(Exception e)
        {
            orderList = new ArrayList<Order>();
            gui.updateStatus("Cannot establish MYSQL connection");
        }
        
    }
    
    
    public static void main(String[] args) {
        try 
        {
            File configurationFile = null;
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION)
            {
                configurationFile = fc.getSelectedFile();
            }
            MainSystem mainSystem = new MainSystem(configurationFile);
            mainSystem.updateOrdersToFulfillComboBox(mainSystem.getGUI(), 
                    mainSystem.getOrderList());
            mainSystem.updateDestinationsComboBox(mainSystem.getGUI(), 
                    mainSystem.getDestinationList());
            mainSystem.setUpActionListeners();
        }
        catch (Exception e)
        {
            
        }
    }
    
    public MainGUI getGUI()
    {
        return gui;
    }
    
    public QueryEngine getQueryEngine()
    {
        return queryEngine;
    }
    
    public ArrayList<Order> getOrderList()
    {
        return orderList;
    }
    
    public ArrayList<File> getDestinationList() 
    {
        return destinationList;
    }
    
    public void updateOrdersToFulfillComboBox(MainGUI gui, ArrayList<Order> orders)
    {
        gui.getOrderList().removeAllItems();
        Iterator it1 = orders.iterator();
        int i = 0;
        while(it1.hasNext())
        {
            Order order = (Order) it1.next();
            if(order.getFulfillable())
            {
                gui.getOrderList().insertItemAt(order, i);
                i++;
            }
        }
    }
    
    public void updateOrderList() throws Exception
    {
        orderList.clear();
        try
        {
            orderList = queryEngine.generateOrderList();
        }
        catch(Exception e)
        {
            gui.updateStatus("Problem Updating Order List");
            throw new Exception();
        }
    }
    
    public void populateDestinationList()
    {
        destinationList = destinationEngine.generateDestinationList();
    }
    
    public void updateDestinationsComboBox(MainGUI gui, ArrayList<File> 
            destinations)
    {
        gui.getDestinationList().removeAllItems();
        Iterator it1 = destinations.iterator();
        int i = 0;
        while(it1.hasNext())
        {
            File file = (File) it1.next();
            gui.getDestinationList().insertItemAt(file, i);
            i++;
        }
    }
    
    public void updateDestinationList()
    {
        destinationList.clear();
        destinationList = destinationEngine.generateDestinationList();
    }
    
    public void setUpActionListeners()
    {
        gui.getRefresh().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    queryEngine = new QueryEngine(configuration.getConfigs()
                    .get("mysqlhost"), configuration.getConfigs()
                    .get("mysqldatabase"), configuration.getConfigs()
                    .get("mysqluser"), configuration.getConfigs()
                    .get("mysqlpassword"));
                    updateOrderList();
                    updateOrdersToFulfillComboBox(gui, orderList);
                    updateDestinationList();
                    updateDestinationsComboBox(gui,destinationList);
                    gui.updateStatus("Ready");
                }
                catch(Exception ex)
                {
                    gui.updateStatus("MYSQL Error: Problem Updating Order List");
                }
                
                gui.setOrderTextBox("This is where the list of talks related to"
                + " a particular order will be displayed");
                gui.getProgressBar().setValue(0);
            }
        });
        
        
        gui.getOrderList().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    JComboBox orderList = gui.getOrderList();
                    Order currentItem = (Order) orderList.getSelectedItem();
                    ArrayList<Talk> talks = currentItem.getTalks();
                    String textBoxContents = "Order ID: " + currentItem.
                            getOrderID() + " \n \nTalks: \n";
                    int i = 1;
                    Iterator it1 = talks.iterator();
                    while(it1.hasNext())
                    {
                        Talk talk = (Talk) it1.next();
                        textBoxContents = textBoxContents + 
                                "\n#" + i + ": " + 
                                talk.getTalkID() + ", " + talk.getTitle() + 
                                ", " + talk.getSpeaker() + ", " + talk.getYear(); 
                        i++;
                    }
                    gui.setOrderTextBox(textBoxContents);
                }
                catch (NullPointerException npe)
                {
                            
                }
            }
        });
        gui.getFulfillOrder().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(gui.getDestinationList().getSelectedItem() == null || 
                        gui.getOrderList().getSelectedItem() == null)
                {
                    return;
                }
                gui.getRefresh().setEnabled(false);
                gui.getFulfillOrder().setEnabled(false);

                final Order order = (Order) gui.getOrderList().getSelectedItem();
                final String talksLocation = configuration.getConfigs().
                        get("talkslocation");
                final String destination = (String) gui.getDestinationList().
                        getSelectedItem().toString();
                String fileName = "";
                
                try
                {
                    // Create an ArrayList of the talks required for this order
                    final ArrayList<Talk> talksComplete = order.getTalks();
                    final ArrayList<Talk> talks = preCopyingSpaceCheck(order,
                            talksLocation, destination);
                    Iterator it2 = talks.iterator();
                    int j = 0;
                    while(it2.hasNext())
                    {
                            j++;
                            gui.getMainFrame().setCursor(Cursor.WAIT_CURSOR);
                            gui.getStatus().setText("Copying Files");
                            Talk talk = (Talk) it2.next();
                            fileCopyEngine = new FileCopyEngine(talksLocation, 
                                    talk.getFileName(), destination, j);
                            fileCopyEngine.addPropertyChangeListener(new 
                                    PropertyChangeListener()
                            {
                            @Override
                                public void propertyChange(PropertyChangeEvent 
                                        evt) 

                                {
                                    try
                                    {
                                        if ("progress".equals(evt.
                                                getPropertyName()))
                                        {
                                            int progress = (Integer) evt.
                                                    getNewValue();
                                            gui.getProgressBar().
                                                    setValue(progress);
                                        }
                                        else if ("state".equals(evt.
                                                getPropertyName())
                                                && fileCopyEngine.isDone()
                                                && fileCopyEngine.get()
                                                == talksComplete.size())
                                        {
                                            if(postCopyingFileCheck(order, 
                                                    talksLocation, 
                                                    destination ))
                                            {
                                                gui.getMainFrame().setCursor
                                                        (Cursor.DEFAULT_CURSOR);
                                                gui.getRefresh().setEnabled(true);
                                                gui.getFulfillOrder().setEnabled
                                                        (true);
                                                queryEngine.setOrderFulfilled
                                                        (order.getOrderID());
                                                updateOrderList();
                                                updateOrdersToFulfillComboBox
                                                        (gui, orderList);
                                                gui.setOrderTextBox(null);
                                                gui.updateStatus("<html><h4>"
                                                        + "<b><font color ="
                                                        + " \"green\"> Order "
                                                        + "Fulfilled! </b></h4>"
                                                        + "</font></html>");
                                            }
                                            else
                                            {
                                                gui.updateStatus("Copying "
                                                        + "Failed");
                                            }
                                            
                                        }
                                        else if ("state".equals(evt.
                                                getPropertyName())
                                                && fileCopyEngine.isDone()
                                                && fileCopyEngine.get()
                                                == talks.size())
                                        {
                                            System.out.println("Error! "
                                                    + "fix me :)");
                                        }
                                    }
                                    catch(Exception ie)
                                            {
                                                
                                            }
                                }
                            });
                            fileCopyEngine.execute();
                        }
                        
                }
                catch (Exception ex)
                        {
                            
                        }
            }
        });
        gui.getPreferences().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                PreferencesWindow prefWindow = new PreferencesWindow
                        (configuration);
            }
         });
        
        gui.getExit().addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
             System.exit(0);   
            }
        });
    }
        
    public boolean postCopyingFileCheck(Order order, String sourceDir,String 
            destinationDir)
    {
        ArrayList<Talk> talks = order.getTalks();
        Iterator it1 = talks.iterator();
        boolean checkPasses = true;
        while (it1.hasNext())
        {
            Talk talk = (Talk) it1.next();
            File destinationFile = new File(destinationDir + File.separator + 
                    talk.getFileName());
            File sourceFile = new File(sourceDir + File.separator + 
                    talk.getFileName());
            SizeFileComparator compare = new SizeFileComparator();
            if(compare.compare(sourceFile,destinationFile) != 0)
            {
                checkPasses = false;
            }
        }
        if (checkPasses)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public ArrayList<Talk> preCopyingSpaceCheck(Order order, String sourceDir, 
            String destinationDir)
    { 
        ArrayList<Talk> talksComplete = order.getTalks();
        Iterator it1 = talksComplete.iterator();
        long talksOverallSize = 0;
        int i = 0;
        boolean exceeded = false;
        long destinationSize = new File(destinationDir).getFreeSpace();
        while(it1.hasNext() && !exceeded)
        {
            Talk talk = (Talk) it1.next();
            File talkFile = new File(sourceDir + File.separator
                    + talk.getFileName());
            talksOverallSize = talksOverallSize + FileUtils.
                    sizeOf(talkFile);
            i++;
            if(talksOverallSize >= destinationSize)
            {
                exceeded = true;
                i = i - 1;
            }
            else if (talksOverallSize == destinationSize || i == 
                    talksComplete.size())
            {
                exceeded = true;
            }
        }
        ArrayList<Talk> talksToReturn = new ArrayList<Talk>();
        int j = 0;
        while (j < i)
        {
            talksToReturn.add(talksComplete.get(j));
            j++;
        }
        return talksToReturn;
    }
}
                


    

    
    

