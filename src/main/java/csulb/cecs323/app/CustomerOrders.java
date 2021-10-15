/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 *
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 *
 *  2018 Alvaro Monge <alvaro.monge@csulb.edu>
 *
 */

package csulb.cecs323.app;

// Import all of the entity classes that we have written for this application.
import com.mysql.cj.Session;
import com.mysql.cj.xdevapi.SessionFactory;
import csulb.cecs323.model.*;
import org.eclipse.persistence.jpa.jpql.tools.spi.IQuery;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * A simple application to demonstrate how to persist an object in JPA.
 * <p>
 * This is for demonstration and educational purposes only.
 * </p>
 * <p>
 *     Originally provided by Dr. Alvaro Monge of CSULB, and subsequently modified by Dave Brown.
 * </p>
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 *
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 *
 *  2021 David Brown <david.brown@csulb.edu>
 *
 */
public class CustomerOrders {
   /**
    * You will likely need the entityManager in a great many functions throughout your application.
    * Rather than make this a global variable, we will make it an instance variable within the CustomerOrders
    * class, and create an instance of CustomerOrders in the main.
    */
   private EntityManager entityManager;

   /**
    * The Logger can easily be configured to log to a file, rather than, or in addition to, the console.
    * We use it because it is easy to control how much or how little logging gets done without having to
    * go through the application and comment out/uncomment code and run the risk of introducing a bug.
    * Here also, we want to make sure that the one Logger instance is readily available throughout the
    * application, without resorting to creating a global variable.
    */
   private static final Logger LOGGER = Logger.getLogger(CustomerOrders.class.getName());

   /**
    * The constructor for the CustomerOrders class.  All that it does is stash the provided EntityManager
    * for use later in the application.
    * @param manager    The EntityManager that we will use.
    */
   public CustomerOrders(EntityManager manager) {
      this.entityManager = manager;
   }

   public static void main(String[] args) {
      LOGGER.fine("Creating EntityManagerFactory and EntityManager");
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("CustomerOrders");
      EntityManager manager = factory.createEntityManager();
      // Create an instance of CustomerOrders and store our new EntityManager as an instance variable.
      CustomerOrders customerOrders = new CustomerOrders(manager);
      List<String> customerDetails = new ArrayList<>();
      List<String> productDetails = new ArrayList<>();


      // Any changes to the database need to be done within a transaction.
      // See: https://en.wikibooks.org/wiki/Java_Persistence/Transactions

      LOGGER.fine("Begin of Transaction");
      EntityTransaction tx = manager.getTransaction();

      tx.begin();

      customerOrders.displayAllCustomers();
      customerOrders.promptCustomer(customerDetails);
      customerOrders.displayAllProducts();
      customerOrders.promptProduct(productDetails);
      System.out.println("customerDetails: " + customerDetails);
      System.out.println("productDetails: " + productDetails);

      // Commit the changes so that the new data persists and is visible to other users.
      tx.commit();
      LOGGER.fine("End of Transaction");

   } // End of the main method

   public void promptProduct(List<String> productUPC){
      Scanner scanner = new Scanner(System.in);
      List<Products> products =
              this.entityManager.createNamedQuery("ReturnAllProducts", Products.class).getResultList();
      if(products.size() > 0) {
         boolean validUserInput = false;
         boolean isValidProductNumb = false;
         int productNumber = -1;

         while (!validUserInput || !isValidProductNumb) {
            System.out.print("Enter the number of the product: ");
            String userInput = scanner.nextLine();
            validUserInput = isInteger(userInput);
            if(!validUserInput){
               System.out.println("Invalid input. Please enter an integer.");
            }
            else{
               productNumber = Integer.parseInt(userInput);

               productUPC.add(products.get(productNumber - 1).getUPC());
               productUPC.add(products.get(productNumber - 1).getMfgr());
               productUPC.add(products.get(productNumber - 1).getModel());
               productUPC.add(products.get(productNumber - 1).getProd_name());
               productUPC.add(String.valueOf(products.get(productNumber - 1).getUnit_list_price()));
               productUPC.add(String.valueOf(products.get(productNumber - 1).getUnits_in_stock()));

               if(productNumber > 0 && productNumber < products.size() + 1){
                  System.out.println("PRODUCT #" + productNumber + "\tUPC " +
                          products.get(productNumber - 1).getUPC() + "\t product name: " +
                          products.get(productNumber - 1).getProd_name());
                  isValidProductNumb = true;
               }
               else{
                  System.out.println(productNumber + " is not a valid product number. Try again.");
                  isValidProductNumb = false;
               }
            }

         }

      }

   }

   public void displayAllProducts(){
      List<Products> allProducts =
              this.entityManager.createNamedQuery("ReturnAllProducts", Products.class).getResultList();
      if (allProducts.size() == 0) {
         // Database has no products
         System.out.println("There are no customers in the database.");

      } else {
         // Displays all products
         System.out.println("List of products:");
         System.out.println("\tUPC\t\t\t\tMFGR\t\t\t\tMODEL\tPRODUCT NAME\t\t\tUNIT LIST PRICE\t\tUNITS IN STOCK");
         for(int index = 0; index < allProducts.size(); index++){
            System.out.println((index + 1) + ".\t" + allProducts.get(index).getUPC() + "\t" +
                    allProducts.get(index).getMfgr() + "     \t" + allProducts.get(index).getModel() + "    \t" +
                    allProducts.get(index).getProd_name() + "  \t$" + allProducts.get(index).getUnit_list_price() + " \t\t\t\t" +
                    allProducts.get(index).getUnits_in_stock() + " units");
         }

      }
   }



   public void promptCustomer (List<String> customerInfo){
      Scanner scanner = new Scanner(System.in);
      List<Customers> customers =
              this.entityManager.createNamedQuery("ReturnAllNames", Customers.class).getResultList();
      if(customers.size() > 0) {
         boolean validUserInput = false;
         boolean isValidCustomerNumb = false;
         int customerNumber = -1;

         while (!validUserInput || !isValidCustomerNumb) {
            System.out.print("Enter the number of the customer: ");
            String userInput = scanner.nextLine();
            validUserInput = isInteger(userInput);
            if(!validUserInput){
               System.out.println("Invalid input. Please enter an integer.");
            }
            else{
               customerNumber = Integer.parseInt(userInput);

               customerInfo.add(String.valueOf(customers.get(customerNumber - 1).getCustomer_id()));
               customerInfo.add(customers.get(customerNumber - 1).getFirst_name());
               customerInfo.add(customers.get(customerNumber - 1).getLast_name());
               customerInfo.add(customers.get(customerNumber - 1).getPhone());
               customerInfo.add(customers.get(customerNumber - 1).getStreet());
               customerInfo.add(customers.get(customerNumber - 1).getZip());

               if(customerNumber > 0 && customerNumber < customers.size() + 1){
                  System.out.println("customer: " + customerNumber + ". " +
                          customers.get(customerNumber - 1).getFirst_name() + " " +
                          customers.get(customerNumber - 1).getLast_name());
                  isValidCustomerNumb = true;
               }
               else{
                  System.out.println(customerNumber + " is not a valid customer number. Try again.");
                  isValidCustomerNumb = false;
               }
            }

         }

      }
   }

   public boolean isInteger(String input){
      //int number;
      try{
         Integer.parseInt(input);
      }
      catch (NumberFormatException e){
         return false;
      }
      catch (NullPointerException e) {
         return false;
      }
      return true;
   }

   public void displayAllCustomers(){
      List<Customers> allCustomers =
              this.entityManager.createNamedQuery("ReturnAllNames", Customers.class).getResultList();
      if (allCustomers.size() == 0) {
         // Database has no customers
         System.out.println("There are no customers in the database.");
      } else {
         // Displays all customers' first and last names
         System.out.println("List of customers:");
         for(int index = 0; index < allCustomers.size(); index++){
            System.out.println((index + 1) + ". " + allCustomers.get(index).getFirst_name() +
                    " " + allCustomers.get(index).getLast_name());
         }

      }
   }

   /**
    * Create and persist a list of objects to the database.
    * @param entities   The list of entities to persist.  These can be any object that has been
    *                   properly annotated in JPA and marked as "persistable."  I specifically
    *                   used a Java generic so that I did not have to write this over and over.
    */
   public <E> void createEntity(List <E> entities) {
      for (E next : entities) {
         LOGGER.info("Persisting: " + next);
         // Use the CustomerOrders entityManager instance variable to get our EntityManager.
         this.entityManager.persist(next);
      }

      // The auto generated ID (if present) is not passed in to the constructor since JPA will
      // generate a value.  So the previous for loop will not show a value for the ID.  But
      // now that the Entity has been persisted, JPA has generated the ID and filled that in.
      for (E next : entities) {
         LOGGER.info("Persisted object after flush (non-null id): " + next);
      }
   } // End of createEntity member method

   /**
    * Think of this as a simple map from a String to an instance of Products that has the
    * same name, as the string that you pass in.  To create a new Cars instance, you need to pass
    * in an instance of Products to satisfy the foreign key constraint, not just a string
    * representing the name of the style.
    * @param UPC        The name of the product that you are looking for.
    * @return           The Products instance corresponding to that UPC.
    */
   public Products getProduct (String UPC) {
      // Run the native query that we defined in the Products entity to find the right style.
      List<Products> products = this.entityManager.createNamedQuery("ReturnProduct",
              Products.class).setParameter(1, UPC).getResultList();
      if (products.size() == 0) {
         // Invalid style name passed in.
         return null;
      } else {
         // Return the style object that they asked for.
         return products.get(0);
      }
   }// End of the getStyle method


} // End of CustomerOrders class
