/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.io.filefilter.NameFileFilter;

/**
 *
 * @author jonathanrainer
 */
public class DestinationEngine {
    private String os;

    public DestinationEngine(String os)
    {
        this.os = os;
    }
    
    public ArrayList<File> generateDestinationList()
    {
        if(os.equals("Windows"))
        {
            ArrayList<File> destinations = new ArrayList<File>();
            File[] fileList = File.listRoots();
            destinations.addAll(Arrays.asList(fileList));
            return destinations;
            
        }
        else if(os.equals("Mac"))
        {
            ArrayList<File> destinations = new ArrayList<File>();
            File[] fileList = File.listRoots()[0].
                    listFiles((java.io.FilenameFilter) 
                    new NameFileFilter("Volumes"))[0].listFiles();
            destinations.addAll(Arrays.asList(fileList));
            return destinations;
        }
        else if(os.equals("Unix"))
        {
            return null;
        }
        else
        {
            return null;
        }
    }
   
}
