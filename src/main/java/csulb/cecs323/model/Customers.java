package csulb.cecs323.model;

import javax.persistence.*;
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
        name="ReturnAllNames",
        query = "SELECT  *" +
                " FROM CUSTOMERS ",
        resultClass = Customers.class
)
@NamedNativeQuery(
        name="ReturnCustomer",
        query = "SELECT  *" +
                " FROM CUSTOMERS " +
                "WHERE customer_id = ?",
        resultClass = Customers.class
)
// I could have avoided uniqueConstraints and just done
// one constraint, but this was more fun.
@Table(uniqueConstraints = {@UniqueConstraint(columnNames =
        {"first_name", "last_name", "phone"})})
/** A person, who has, or might, order products from us. */
public class Customers {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    /** Surrogate key for customer.  We don't want to
    migrate last name, first name, & phone all over the place.
     */
    private long customer_id;
    @Column(nullable=false, length = 64)
    /** Customer surname */
    private String last_name;
    @Column(nullable=false, length = 64)
    /** Customer given name */
    private String first_name;
    @Column(nullable = false, length = 64)
    /** Street address, minus the zipcode */
    private String street;
    @Column(nullable = false, length = 10)
    /** Zip code for the customer */
    private String zip;
    @Column(nullable = false, length = 20)
    /** Their phone number, with no particular validation */
    private String phone;

    /**
     * Default Constructor
     */
    public Customers() {}

    /**
     * Constructor for all private variables in Customers
     *
     * @param last_name
     * @param first_name
     * @param street
     * @param zip
     * @param phone
     */
    public Customers (String last_name, String first_name, String street,
                      String zip, String phone) {
        this.last_name = last_name;
        this.first_name = first_name;
        this.street = street;
        this.zip = zip;
        this.phone = phone;
    }

    /**
     * A get method for customer_id
     *
     * @return          current customer_id
     */
    public long getCustomer_id() {
        return customer_id;
    }

    /**
     * A set method for customer_id
     *
     * @param customer_id
     */
    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    /**
     * A get method for last_name
     *
     * @return          current last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * A set method for last_name
     *
     * @param last_name
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * A get method for first_name
     *
     * @return          current first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * A set method for first_name
     *
     * @param first_name
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * A get method for street
     *
     * @return          current street
     */
    public String getStreet() {
        return street;
    }

    /**
     * A set method for street
     *
     * @param street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * A get method for zip
     *
     * @return          current zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * A set method for zip
     *
     * @param zip
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * A get method for phone
     *
     * @return          current phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * A set method for phone
     *
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * A toString method for Customers displaying the customer_id, first_name, and last_name of the customer
     * @return
     */
    @Override
    public String toString () {
        return "Customer- ID: " + this.customer_id + ", Name: " + this.last_name +
                ", " + this.first_name;
    }

    /**
     * A method that determines whether an object equals the current Customer object
     *
     * @param o
     * @return boolean          returns true or false dependent or whether the object equals the current Customer object
     */
    @Override
    public boolean equals (Object o) {
        Customers customer = (Customers) o;
        return this.getCustomer_id() == customer.getCustomer_id();
    }

    /**
     * A method that produces a hashCode of the customer_id, last_name, and first_name from the Customer object
     * @return int          hashcode of the 3 private variables in Customers
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.getCustomer_id(), this.getLast_name(), this.getFirst_name());
    }
}
