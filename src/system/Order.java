package system;

import java.util.ArrayList;

/**
 * This class defines all the functionality for Objects that represent orders in
 * this system, at a basic level they consist of an ID and a collection of talks
 * with a set of markers to easily decide whether they are included in certain
 * lists or not
 * 
 * @author jonathanrainer
 */
public class Order {
    /**
     * An integer to store the ID of this particular order
     */
    private int order_id;
    /**
     * A collection of talks associated with this particular order
     */
    private ArrayList<Talk> talks;
    /**
     * A boolean to mark whether this order has already been completed
     */
    private boolean complete;
    /**
     * Another boolean to mark whether it's possible for this order to be 
     * fulfilled
     */
    private boolean fulfillable;
    
    /**
     * This creates a new order object to store information about one particular
     * order
     * @param order_id The identifier for this paticular order
     * @param complete A boolean to mark whether the order is completed or not
     * @param talks A collection of the talks associated with this order
     * @param fulfillable A boolean to mark whether the order is fulfillable or
     * not
     */
    public Order(int order_id, boolean complete, ArrayList<Talk> talks,
            boolean fulfillable)
    {
        this.order_id = order_id;
        this.complete = complete;
        this.talks = talks;
        this.fulfillable = fulfillable;
    }
    
    /**
     * Returns the ID for the order this object represents
     * @return The ID associated with this order
     */
    public int getOrderID()
    {
        return order_id;
    }
    
    /**
     * Return whether this order is fulfillable based on the current state of 
     * the database.
     * @return Whether this order is able to be fulfilled or not.
     */
    public boolean getFulfillable()
    {
        return fulfillable;
    }
    
    /**
     * Set whether the order can be fulfilled or not
     * 
     * @param fulfillable A boolean indicating whether the order can be 
     * fulfilled or not.
     */
    public void setFulfillable(boolean fulfillable)
    {
        this.fulfillable = fulfillable;
    }
    
    /**
     * Return the collection of talks associated with this order
     * @return The ArrayList that stores the talks defined by the order
     */
    public ArrayList<Talk> getTalks()
    {
        return talks;
    }
    
    /**
     * Return a string representation of the order such that it doesn't return
     * its memory location when placed in print statements etc.
     * @return A string representation of the order itself in the form "Order: 
     * 123" for example.
     */
    @Override
    public String toString()
    {
        return "Order: " + order_id;
    }
}
