/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.Comparator;
import system.Talk;

/**
 *
 * @author jonathanrainer
 */
public class TalksComparator implements Comparator<Talk>{
    
    public TalksComparator()
    {
        
    }
    
    @Override
    public int compare(Talk t1, Talk t2)
    {
        return t1.getTalkTime().compareTo(t2.getTalkTime());
    }
    
}
