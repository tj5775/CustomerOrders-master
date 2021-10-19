package csulb.cecs323.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
/*
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

@Entity
@NamedNativeQuery(
        name = "PlaceOrder",
        query = "INSERT INTO ORDERS" +
                "(order_date, sold_by, customer_id) " +
                "VALUE(timeStamp = ?, seller = ?, custID = ?)",
        resultClass = Orders.class
)
@IdClass(Orders_pk.class)
/**
A request by a Customer for a collection of one or more
Products.  The Order includes a quantity of each Product
within the order.
 */
public class Orders {
    @Id
    @ManyToOne
    // I could easily have left the @JoinColumn annotation out
    @JoinColumn(name="customer_id",
    referencedColumnName = "customer_id")
    /** The individual placing the order */
    private Customers customer;
    @Id
    @Column(nullable=false)
    /** When they placed it.  This allows us to distinguish
    one order from another by the same customer.
     */
    private LocalDateTime order_date;
    // make this just a string for now.  Perhaps recast Customer to "Person" and make soldby
    // a relationship from Person instead of just a String.  Or a lookup table is fine too.
    @Column(nullable=false, length=128)
    /** The name of the sales person who sold the goods. */
    private String sold_by;

   /**
    * Default constructor for Orders objects.
    */
    public Orders () {}

    /**
    * The constructor for the Orders class. Takes in details about the Order for later use in the application.
    * @param customer       The customer who is placing the order
    * @param order_date     The date which the order is being placed
    * @param sold_by        The entity selling the order
    */
    public Orders (Customers customer, LocalDateTime order_date,
                   String sold_by) {
        this.customer = customer;
        this.order_date = order_date;
        this.sold_by = sold_by;
    }

    /**
    * The getter method for customer objects
    * @return               The requested customer object
    */
    public Customers getCustomer() {
        return customer;
    }

    /**
    * The setter method for customer objects
    * @param customer       The customer placing the order
    */
    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    /**
    * The getter method for order dates
    * @return               The requested order date
    */
    public LocalDateTime getOrder_date() {
        return order_date;
    }

    /**
    * The setter method for order dates
    * @param order_date       When the customer is placing the order
    */
    public void setOrder_date(LocalDateTime order_date) {
        this.order_date = order_date;
    }

    /**
    * The getter method for sold by info
    * @return               The entity selling the order
    */
    public String getSold_by() {
        return sold_by;
    }

    /**
    * The setter method for order dates
    * @param sold_by        The entity selling the order
    */
    public void setSold_by(String sold_by) {
        this.sold_by = sold_by;
    }

    /**
     * A toString method for Orders displaying the customer, order date, and sold by
     * @return
     */
    @Override
    public String toString () {
        return "Order: Placed by: " + this.getCustomer() + ", On: " + this.getOrder_date() +
                ", Sold by: " + this.getSold_by();
    }

    /**
     * A method that determines whether an object equals the current Order object
     *
     * @param o
     * @return boolean          returns true or false dependent or whether the object equals the current Order object
     */
    @Override
    public boolean equals (Object o) {
        Orders order = (Orders) o;
        return (this.getCustomer().equals(order.getCustomer()) &&
                this.getOrder_date() == order.getOrder_date());
    }

    /**
     * A method that produces a hashCode of the order date and customer from the Orders object
     * @return int          hashcode of the 2 private variables in Orders
     */
    @Override
    public int hashCode () {
        return Objects.hash(this.getOrder_date(), this.getCustomer());
    }
}
