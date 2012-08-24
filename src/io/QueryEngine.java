/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import system.Order;
import system.Talk;

/**
 *
 * @author jonathanrainer
 */
public class QueryEngine {
    
    // JDBC driver name and database URL
   private String JDBC_DRIVER;
   private String DB_URL;

   //  Database credentials
   private String USER;
   private String PASS;
   
   public QueryEngine(String mysqlhost, String mysqldatabase, String mysqluser
           ,  String mysqlpassword)
   {
       JDBC_DRIVER = "com.mysql.jdbc.Driver"; 
       DB_URL = "jdbc:mysql://" + mysqlhost + "/" + mysqldatabase;
       USER = mysqluser;
       PASS = mysqlpassword;
   }
   
   
   public ArrayList<Order> generateOrderList() throws SQLException, Exception
   {
       Connection conn = null;
       Statement stmt1 = null;
       Statement stmt2 = null;
       Statement stmt3 = null;
       Statement stmt4 = null;
       ArrayList<Order> results = new ArrayList<Order>();
       try
       {
           //STEP 2: Register JDBC driver
           Class.forName("com.mysql.jdbc.Driver");
           
           //STEP 3: Open a connection
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
           
           //STEP 4: Execute a query
           //In this case the query will extract all of the current orders in
           //the order table regardless of whether they can be fulfilled or 
           //not.
           stmt1 = conn.createStatement();
           ResultSet rs1 = stmt1.executeQuery("SELECT id FROM orders WHERE "
                   + "complete = '0'");
           
           //STEP 5: Extract data from result set
           //This will then extract the data from the previous query whilst
           //running a second query to add the talk_id's associated with that
           //order to a new order object and adding these to a collection of
           //orders. These can then be iterated through to discover which can
           //be fulfilled.
           int i = 0;
           while(rs1.next())
           {
               int id = rs1.getInt("id");
               stmt2 = conn.createStatement();
               ResultSet rs2 = stmt2.executeQuery("SELECT talk_id FROM "
                       + "order_items WHERE order_id = '" + id + "'");
               ArrayList<Talk> talks = new ArrayList<Talk>();
               while(rs2.next())
               {
                   stmt3 = conn.createStatement();
                   ResultSet rs3 = stmt3.executeQuery("SELECT id, year, speaker"
                       + ", title FROM talks WHERE  id = " + rs2.getInt
                           ("talk_id"));
                   while(rs3.next())
                   {
                       Talk talk = new Talk(rs3.getInt("id"), rs3.getInt
                               ("year"), rs3.getString("speaker"),
                               rs3.getString("title"));
                       talks.add(talk);
                   }
                   rs3.close();
                   stmt3.close();
               }
               rs2.close();
               stmt2.close();
               Order order = new Order(id, false, talks, true);
               results.add(order);
               i++;
           }
           if (i == 0)
           {
               return null;
           }
           //Now we have a new order object that relates to the order we need to
           //check if it's actually possible to fulfill that order at all so we
           //iterate through the orders until we find one that is unavailable
           //and then stop or if none are found the order is marked fulfillable
           Iterator it1 = results.iterator();
           while(it1.hasNext())
           {
               Order order = (Order) it1.next();
               Iterator it2 = order.getTalks().iterator();
               while(it2.hasNext())
               {
                   Talk talk = (Talk) it2.next();
                   stmt4 = conn.createStatement();
                   ResultSet rs4 = stmt4.executeQuery("SELECT available "
                           + "FROM talks WHERE id = '" + talk.getTalkID() + "'");
                   while(rs4.next() && order.getFulfillable())
                   {
                       boolean available = rs4.getBoolean("available");
                       if(!available)
                       {
                           order.setFulfillable(false);
                       }
                       if(rs4.next() && !(order.getFulfillable()))
                       {
                           it1.remove();
                       }
                       if(order.getTalks().isEmpty())
                       {
                           order.setFulfillable(false);
                       }
                   }
                   rs4.close();
                   stmt4.close();
               }
               
           }       
           //STEP 6: Clean-up environment
           rs1.close();
           stmt1.close();
           conn.close();
       }
//       catch(SQLException se)
//       {
//           //Handle errors for JDBC
//           se.printStackTrace();
//       }
//       catch(Exception e)
//       {
//           //Handle errors for Class.forName
//           e.printStackTrace();
//       }
       finally
       {
           //finally block used to close resources
           try
           {
               if(stmt1!=null)
               {
                   stmt1.close();
               }
           }
           catch(SQLException se2)
           {
               // nothing we can do
           }
      try
      {
         if(conn!=null)
         {
             conn.close();
         }
      }
      catch(SQLException se)
      {
         se.printStackTrace();
      }//end finally try
      
   }//end try
       return results;
   }
   
   public void setOrderFulfilled(int orderID)
   {
        Connection conn = null;
        Statement stmt = null;
        String output = "";
        try {
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 4: Execute a query
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE `gb_talks`.`orders` SET `complete` = 1 "
                    + "WHERE `id` = " + orderID);

            //STEP 6: Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
    }
}
