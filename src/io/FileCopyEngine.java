/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.SwingWorker;

    
/**
 *
 * @author jonathanrainer
 */
public class FileCopyEngine extends SwingWorker<Integer,Void> {
    
    private String talksLocation;
    private String fileName;
    private String destination;
    private File originalFile;
    private File fileDestination;
    private int filesComplete;
    
    public FileCopyEngine(String talksLocation, String fileName, String
            destination, int filesComplete)
    {
        this.talksLocation = talksLocation;
        this.fileName = fileName;
        this.destination = destination;
        originalFile = new File(talksLocation + File.separator + fileName);
        fileDestination = new File(destination + File.separator + fileName);
        this.filesComplete = filesComplete - 1;
        
    }
    
    @Override
    protected Integer doInBackground() throws IOException
    {      
        URL originalFileURL = originalFile.toURL();
        URLConnection connection = originalFileURL.openConnection();
        InputStream inputStream = connection.getInputStream();
        
        float length = connection.getContentLength();
        float current = 0f;
        
        int progress = calculateProgress(length, current);
        setProgress(progress);
        
        FileOutputStream out = new FileOutputStream(fileDestination);

        byte[] buffer = new byte[2048];
        int bytesRead;
        bytesRead = inputStream.read(buffer);

        while(bytesRead != -1)
        {   
            out.write(buffer, 0, bytesRead);
            current = current + bytesRead; 
            progress = calculateProgress(length, current);
            setProgress(progress);
            bytesRead = inputStream.read(buffer);
        }
        
        inputStream.close();
        
        filesComplete += 1;
        return filesComplete;
    }

    public int calculateProgress(float originalFileLength, float current)
    {
        float progress = (current/originalFileLength) * 100;
        return (int) progress;
    }
    
    public int getFilesComplete()
    {
        return filesComplete;
    }
   
}
