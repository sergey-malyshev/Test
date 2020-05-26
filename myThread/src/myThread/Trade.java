package myThread;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Trade {

   static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/market";
   static final String USER = "master";
   static final String PASSWORD = "123";
   
   public static void main(String[] args) throws IOException{
      Store store = new Store();
      Producer producer = new Producer(store);
      Consumer consumer = new Consumer(store);
      Thread producerThread = new Thread(producer);
      Thread consumerThread = new Thread(consumer);
//      producerThread.start();
//      consumerThread.start();
      
      /* Create Vector */
      Vector<DataClass> vector = new Vector<DataClass>();
      
      /* Add data to Vector */
      vector.add(new DataClass("Shape","Square"));
      vector.add(new DataClass("Area", "2433Sqft")); 
      vector.add(new DataClass("Radius", "25m")); 
      
      // print result 
      System.out.println("list of Objects:");
      for(DataClass n : vector) {
        print(n);
      }
//      vector.forEach(n -> print(n));
      
      
      
      /* Data Base */ 
      System.out.println("Connection to database...");
      try {
         Class.forName("org.postgresql.Driver");
      }catch (ClassNotFoundException e) {
         System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
         return;
      }
      
      System.out.println("PostgreSQL JDBC Driver successfully connected");
      Connection connection = null;
      try {
         connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
      } catch (SQLException e) {
         System.out.println("Connection Failed");
         return;
      }
      
      if (connection != null) {
         System.out.println("You successfully connected to database now");
      } else {
         System.out.println("Failed to make connection to database");
      }
      
      Statement stmt = null;
      try {
         stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery( "SELECT * FROM product;" );
         FileWriter outputString = new FileWriter("D:/settings.txt");
         while ( rs.next() ) {
            int id = rs.getInt("product_id");
            String  product_name = rs.getString("product_name");
            String  product_description = rs.getString("product_description");
            String   product_price  = rs.getString("product_price");
            System.out.println( "ID = " + id );
            System.out.println( "NAME = " + product_name );
            System.out.println( "DESC = " + product_description );
            System.out.println( "PRICE = " + product_price );
            System.out.println();
            outputString.append((char)id);
            outputString.append(' ');
            outputString.append(product_name);
            outputString.append(' ');
            outputString.append(product_description);
            outputString.append(' ');
            outputString.append(product_price);     
            outputString.append(' ');
         }
         rs.close();
         stmt.close();
         connection.close();
         outputString.flush();
         outputString.close();
      } catch (SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      
      /* поток ввода */
      InputStreamReader inputString = null;
      
      /* поток вывода */
           
      String[] fileList;
      try {
         inputString = new InputStreamReader(System.in);
         System.out.println("Enter symbol q for exit");
         String nameDir = "D:/test";
         File dir = new File(nameDir);
        if (dir.mkdir() == true) {
           System.out.println("Directory create is OK");
        }
        else {
           System.out.println("Directory is exist");
        }
        
        fileList = dir.list();
        for(String path:fileList) {
           System.out.println(path);
        }
         char a;
         do {
            a = (char) inputString.read();
         } while (a != 'q');
      }finally {
         if (inputString != null) {
           inputString.close();
           System.out.println("Programm is complete");
         }
      }
      
   }
   
// printing object data 
   public static void print(DataClass n) 
   { 
       System.out.println("****************"); 
       System.out.println("Object Details"); 
       System.out.println("key : " + n.key); 
       System.out.println("value : " + n.value); 
   } 
}
