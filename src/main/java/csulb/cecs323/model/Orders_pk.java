package csulb.cecs323.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
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

/** The primary key class for Orders */
public class Orders_pk implements Serializable {
    @Id
    @Column(nullable = false)
    /*
    Note carefully the name of this attribute.  It MUST be the
    same as the name of the object in the child that has the
    migrating foreign key from the parent.  Even though it's
    just a surrogate.
     */
    private long customer;
    @Id
    @Column(nullable = false)
    private LocalDateTime order_date;

   /**
    * Default constructor for Orders_pk objects.
    */
    public Orders_pk () {}

    /**
    * The constructor for the Orders_pk class. Takes in details about the Order for later use in the application.
    * @param customer_id    The customer who is placing the order
    * @param order_date     The date which the order is being placed
    */
    public Orders_pk (long customer_id, LocalDateTime order_date) {
        this.customer = customer_id;
        this.order_date = order_date;
    }

    /**
    * The getter method for customer objects
    * @return               The requested customer object
    */
    public long getCustomer() {
        return customer;
    }

    /**
    * The setter method for customer objects
    * @param customer       The customer placing the order
    */
    public void setCustomer(long customer) {
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
     * A method that determines whether an object equals the current Order object
     *
     * @param o
     * @return boolean          returns true or false dependent or whether the object equals the current Order object
     */
    @Override
    public boolean equals (Object o) {
        boolean results = false;
        if (this == o) {
            results = true;
        } else if (o == null || getClass() != o.getClass()) {
            results = false;
        } else {
            Orders_pk orders_pk = (Orders_pk) o;
            results =   this.getCustomer() == orders_pk.getCustomer() &&
                        this.getOrder_date() == orders_pk.getOrder_date();
        }
        return results;
    }

    /**
     * A method that produces a hashCode of the order date and customer from the Orders object
     * @return int          hashcode of the 2 private variables in Orders
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getCustomer(), this.getOrder_date());
    }
}
