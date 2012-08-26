/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comparator;

import java.util.Comparator;
import system.Order;

/**
 *
 * @author jonathanrainer
 */
public class OrderComparator implements Comparator<Order>{
    
    public OrderComparator()
    {
        
    }
    
    @Override
    public int compare(Order or1, Order or2)
    {
        return or1.getWhenFulfillable().compareTo(or1.getWhenFulfillable());
    }
    
}
