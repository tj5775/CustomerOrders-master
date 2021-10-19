package csulb.cecs323.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
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

/**
 * The primary key class for the Order_lines class.
 */
public class Order_lines_pk implements Serializable {
    @Id
    @Column(nullable = false)
    /*
    This makes perfect sense when you think about it, but it
    takes getting used to.  The parent Orders class has an
    Orders_pk class as its primary key, so that's what migrates
    in to this PK class.  We do not have to worry about the
    customer_id that comes down from Customers since that is
    encapsulated in the Orders_pk object.
     */
    /** The order containing the given product */
    private Orders_pk order;
    @Id
    @Column(nullable = false, length = 30)
    /** One product within this one order */
    private String product;
    /**
     * A get method for order
     *
     * @return          current order
     */
    public Orders_pk getOrder() {
        return order;
    }

    /**
     * A set method for order
     *
     * @param order
     */
    public void setOrder(Orders_pk order) {
        this.order = order;
    }

    /**
     * A get method for unit_sale_price
     *
     * @return          current product
     */
    public String getProduct() {
        return product;
    }

    /**
     * A set method for product
     *
     * @param product
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * A method that compares an object to the Order_lines object and sees whether they are equal or not
     *
     * @param o
     * @return boolean          Whether or not they are equal
     */
    @Override
    public boolean equals (Object o) {
        boolean results = false;
        if (this == o) {
            results = true;
        } else if (o == null || getClass() != o.getClass()) {
            results = false;
        } else {
            Order_lines_pk olpk = (Order_lines_pk) o;
            results = this.getOrder().equals (olpk.getOrder()) &&
                    this.getProduct() == olpk.getProduct();
        }
        return results;
    }
    /**
     * A method that generates a hash code using the order value and the product value
     *
     * @return int      the hashcode of all input values
     */
    @Override
    public int hashCode () {
        return Objects.hash (this.getOrder(), this.getProduct());
    }
}
