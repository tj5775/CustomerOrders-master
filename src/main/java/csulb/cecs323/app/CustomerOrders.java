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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

   /**
    * Main method. 
    * Controls the EntityManager and allows the user to make more transactions if they want to.
    * @param args
    */
   public static void main(String[] args) {
      LOGGER.fine("Creating EntityManagerFactory and EntityManager");
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("CustomerOrders");
      EntityManager manager = factory.createEntityManager();
      boolean again = true;
      List<String> orderDetails = new ArrayList<>();
      while (again) {
         // Create an instance of CustomerOrders and store our new EntityManager as an instance variable.
         CustomerOrders customerOrders = new CustomerOrders(manager);
         //List<String> customerDetails = new ArrayList<>();
         // List<String> productDetails = new ArrayList<>();
         List<Orders> order = new ArrayList<>();
         List<Order_lines> order_lines = new ArrayList<>();
         List<Products> product = new ArrayList<>();
         boolean proceedToComplete = false;


         // Any changes to the database need to be done within a transaction.
         // See: https://en.wikibooks.org/wiki/Java_Persistence/Transactions

         LOGGER.fine("Begin of Transaction");
         EntityTransaction tx = manager.getTransaction();
         tx.begin();
         customerOrders.displayAllCustomers(orderDetails);
         customerOrders.promptCustomer(orderDetails);
         while (!proceedToComplete) {

            customerOrders.displayAllProducts(orderDetails);
            customerOrders.promptProduct(orderDetails);
            //customerOrders.placeOrder(orderDetails);


            order.add(customerOrders.placeOrder(orderDetails));
            proceedToComplete = customerOrders.proceedCheckout(orderDetails);
            if (!proceedToComplete) {
               System.out.println("Welcome back");
               //orderDetails.clear();
            } else {
               order_lines.add(customerOrders.checkout(orderDetails, order));

            }
         }

         customerOrders.createEntity(order);
         customerOrders.createEntity(order_lines);

         product.add(customerOrders.getProduct(orderDetails.get(1)));
         int updatedQuantity = product.get(0).getUnits_in_stock() - Integer.parseInt(orderDetails.get(2));
         product.get(0).setUnits_in_stock(updatedQuantity);
         customerOrders.createEntity(product);
         // Commit the changes so that the new data persists and is visible to other users.
         tx.commit();
         LOGGER.fine("End of Transaction");

         Scanner scanner = new Scanner(System.in);
         boolean loop1 = true;
         while (loop1) {
            String userInput = " ";
            System.out.println("Do you want to place another order? y/Y or n/N");
            userInput = scanner.nextLine().toLowerCase();
            if (userInput.equals("y")) {
               again = true;
               loop1 = false;
            }
            else if(userInput.equals("n")){
               again = false;
               loop1 = false;
               customerOrders.displayAllOrderDetails(orderDetails);
            }
            else{
               System.out.println("Invalid input. Please enter 'y/Y or n/N");
            }
         }
      }

   } // End of the main method

   /**
    * Checkout method. Creates a new Order Line.
    * 
    * @param orderDetails     Details associated with the order being checked out.
    * @param order            All current orders.
    * @return                 The order line associated with both the order and orderDetails.
    */
   public Order_lines checkout(List<String> orderDetails, List<Orders> order){
      Products product = getProduct(orderDetails.get(1));
      return new Order_lines(order.get(0), product, Integer.parseInt(orderDetails.get(2)), Double.parseDouble(orderDetails.get(3)));
   }

   /**
    * Allows customer to choose whether or not they are completing their order.
    * If so, then they can proceed to checkout.
    * 
    * @param orderDetails     Details associated with the order being checked out.
    * @return boolean         Whether or not the order has been completed. (True = complete, False = incomplete)
    */
   public boolean proceedCheckout(List<String> orderDetails){
      Scanner scanner = new Scanner(System.in);
      String completeOrder = "";
      while(!completeOrder.equals("y") && !completeOrder.equals("n")) {
         System.out.println("Type y/Y to complete order");
         System.out.println("Or type n/N to abort transaction");
         completeOrder = scanner.nextLine().toLowerCase();
         if(!completeOrder.equals("y") && !completeOrder.equals("n")){
            System.out.println("\n\nInvalid input. Try again.");
         }
         //completeOrder = completeOrder.toLowerCase();
      }
      if(completeOrder.equals("y")){
         return true;
      }
      else{
         cls();
         return false;
      }
   }

   /**
    * Method to create an order using specific details.
    * 
    * @param orderDetails     Details associated with the order being placed.
    * @return                 The order being placed using the passed details.    
    */
   public Orders placeOrder(List<String> orderDetails){
      LocalDateTime dateTime = LocalDateTime.now();
      Customers customer = getCustomer(Long.valueOf(orderDetails.get(0)));
      Orders orders = new Orders(customer, dateTime, "Al");

      return orders;
   }

   /**
    * Method for user to find the specific product(s) they are looking for.
    * 
    * @param orderDetails     Details associated with the order being placed.   
    */
   public void promptProduct(List<String> orderDetails){

      List<Products> products =
              this.entityManager.createNamedQuery("ReturnAllProducts", Products.class).getResultList();
      if(products.size() > 0) {
         Scanner scanner = new Scanner(System.in);
         String userInput;
         boolean validUserInput = false;
         boolean isValidProductNumb = false;
         int productNumber = -1;

         while (!validUserInput || !isValidProductNumb) {
            System.out.print("Enter the number of the product: ");
            userInput = scanner.nextLine();
            validUserInput = isInteger(userInput);
            if(!validUserInput){
               System.out.println("Invalid input. Please enter an integer.");
            }
            else{
               productNumber = Integer.parseInt(userInput);

               if(productNumber > 0 && productNumber < products.size() + 1){
                  orderDetails.add(1, products.get(productNumber - 1).getUPC());
                  isValidProductNumb = true;
               }
               else{
                  System.out.println(productNumber + " is not a valid product number. Try again.");
                  isValidProductNumb = false;
               }
            }

         }

         validUserInput = false;
         while(!validUserInput) {
            //cls();
            System.out.println("PRODUCT #" + productNumber + "\tMFGR: \t\t\t  " + products.get(productNumber - 1).getMfgr());
            System.out.println("\t\t\tProduct name: \t  " + products.get(productNumber - 1).getProd_name());
            System.out.println("\t\t\tProduct price: \t  $" + products.get(productNumber - 1).getUnit_list_price());
            System.out.println("\t\t\tUnits available:  " + products.get(productNumber - 1).getUnits_in_stock());
            System.out.println("How many units to order?");
            userInput = scanner.nextLine();
            if(isInteger(userInput)){
               int unitsToOrder = Integer.parseInt(userInput);
               int availableUnits = products.get(productNumber - 1).getUnits_in_stock();
               cls();
               if(unitsToOrder > 0 && unitsToOrder <= availableUnits){
                  // Display order details
                  orderDetails.add(2, String.valueOf(unitsToOrder));
                  orderDetails.add(3, String.valueOf(products.get(productNumber - 1).getUnit_list_price()));

                  displayOrderDetails(orderDetails);
                  validUserInput = true;
               }
               else{
                  //cls();
                  System.out.println(unitsToOrder + " units exceeds the number of available units");
                  System.out.println("Please, enter a number of units less than or equal to " + availableUnits);
               }
            }
            else{
               System.out.println("Invalid input. Please enter an integer.");
            }
         }
      }

   }

   /**
    * Method to display all the details of a specific order being placed.
    * 
    * @param orderDetails     Details associated with the order being placed.
    */
   public void displayOrderDetails(List<String> orderDetails){
      Customers customer = getCustomer(Integer.parseInt(orderDetails.get(0)));
      Products product = getProduct(orderDetails.get(1));
      cls();
      System.out.println("\t\t\t\tOrder Details");
      System.out.println("Customer info");
      System.out.println("Customer name: " + customer.getFirst_name() + " " + customer.getLast_name() +
              "\tPhone number: " + customer.getPhone() + "\t Street: " + customer.getStreet() +
              "\tZip: " + customer.getZip());
      System.out.println("\nProduct info");
      System.out.println("Product name: " + product.getProd_name() + "\tManufacturer: " + product.getMfgr() +
              "\tUnit price: $" + product.getUnit_list_price() + "\tUnits ordered: " + orderDetails.get(2));
      System.out.println("Order total: $" + (Integer.parseInt(orderDetails.get(2)) * product.getUnit_list_price()));
   }

   public void displayAllOrderDetails(List<String> orderDetails) {
      Customers customer = getCustomer(Integer.parseInt(orderDetails.get(0)));
      Products product = new Products();
      double total = 0;
      /*
      System.out.println(orderDetails.size());
      for (int x = 0; x < orderDetails.size(); x++) {
         System.out.println(orderDetails.get(x));
      }

       */
      for (int x = 0; x < orderDetails.size(); x++) {
         /*
         if (x % 3 == 0) {
            customer = getCustomer(Integer.parseInt(orderDetails.get(x)));
            }
         */
         if (x % 4 == 1) {
            product = getProduct(orderDetails.get(x));
         }
         if (x % 4 == 2) {
            System.out.println("\t\t\t\tOrder Details");
            System.out.println("Customer info");
            System.out.println("Customer name: " + customer.getFirst_name() + " " + customer.getLast_name() +
                    "\tPhone number: " + customer.getPhone() + "\t Street: " + customer.getStreet() +
                    "\tZip: " + customer.getZip());
            System.out.println("\nProduct info");
            System.out.println("Product name: " + product.getProd_name() + "\tManufacturer: " + product.getMfgr() +
                    "\tUnit price: $" + product.getUnit_list_price() + "\tUnits ordered: " + orderDetails.get(x));
            System.out.println("Order total: $" + (Integer.parseInt(orderDetails.get(x)) * product.getUnit_list_price()));
            total += Integer.parseInt(orderDetails.get(x)) * product.getUnit_list_price();
         }
      }
      System.out.println("\nThe Total Cost: $" + total);
   }

   /**
    * Method to display all the products.
    * 
    * @param orderDetails     Details associated with the order being placed.   
    */
   public void displayAllProducts(List<String> orderDetails){
      Customers customer = getCustomer(Integer.parseInt(orderDetails.get(0)));
      List<Products> allProducts =
              this.entityManager.createNamedQuery("ReturnAllProducts", Products.class).getResultList();
      if (allProducts.size() == 0) {
         // Database has no products
         System.out.println("There are no customers in the database.");

      } else {
         cls();
         // Displays all products
         System.out.println("List of products for "+ customer.getFirst_name() + " " + customer.getLast_name() + ":");
         System.out.println("\tUPC\t\t\t\tMFGR\t\t\t\tMODEL\tPRODUCT NAME\t\t\tUNIT LIST PRICE\t\tUNITS IN STOCK");
         for(int index = 0; index < allProducts.size(); index++){
            System.out.println((index + 1) + ".\t" + allProducts.get(index).getUPC() + "\t" +
                    allProducts.get(index).getMfgr() + "     \t" + allProducts.get(index).getModel() + "    \t" +
                    allProducts.get(index).getProd_name() + "  \t$" + allProducts.get(index).getUnit_list_price() + " \t\t\t\t" +
                    allProducts.get(index).getUnits_in_stock() + " units");
         }

      }
   }

   /**
    * Method to get and validate customers.
    * 
    * @param orderDetails     Details associated with the order being placed.
    */
   public void promptCustomer (List<String> orderDetails){
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
               if(customerNumber > 0 && customerNumber < customers.size() + 1){
                  orderDetails.add(0, String.valueOf(customers.get(customerNumber - 1).getCustomer_id()));

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

   /**
    * Method to check if a certain input is an integer.
    * 
    * @param input            The value to be checked for integer status.
    * @return                 Whether or not the input is an integer. (True = integer, False = non-integer)    
    */
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

   /**
    * Method to display a list of all customers.
    * 
    * @param orderDetails     Details associated with the order being placed.
    */
   public void displayAllCustomers(List<String> orderDetails){
      List<Customers> allCustomers =
              this.entityManager.createNamedQuery("ReturnAllNames", Customers.class).getResultList();
      if (allCustomers.size() == 0) {
         // Database has no customers
         System.out.println("There are no customers in the database.");
      } else {
         //cls(); // clears the screen

         // Displays all customers' first and last names
         System.out.println("List of customers:");
         System.out.println("\tNAME\t\t\t\tPHONE\t\t\tSTREET\t\t\t\t\tZIPCODE");
         for(int index = 0; index < allCustomers.size(); index++){
            System.out.println((index + 1) + ". " + allCustomers.get(index).getFirst_name() +
                    " " + allCustomers.get(index).getLast_name() + "\t\t" +
                    allCustomers.get(index).getPhone() + "\t" + allCustomers.get(index).getStreet() +
                    " \t\t" + allCustomers.get(index).getZip());
         }

      }
   }

   /**
    * Method to get the details of a specific customer
    * 
    * @param customer_id     The ID of the customer whose details are needed.
    * @return                The customer associated with the ID passed.
    */
   public Customers getCustomer(long customer_id){
      Customers customer = this.entityManager.createNamedQuery("ReturnCustomer", Customers.class)
              .setParameter(1, customer_id).getSingleResult();
      return customer;
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

   public void cls(){
      for(int i = 0; i < 25; i ++){
         System.out.println();
      }
   }


} // End of CustomerOrders class
