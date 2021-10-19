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

/*
This is the junction table between Orders and Products.  We would
normally use the @ManyToMany mapping if we did not have the
additional columns of quantity and unit_sale_price to contend
with.  In that case, we could have let JPA generate the junction
table for us and left it at that.  As it is, however, we have
to build it ourselves.

One question that I have is if we had a model in which the
association class was the parent to some other class, would
that mean that you would have to explicitly declare the
association class the way that we are doing here.  It seems to
me that would be the case.
 */
@Entity
@IdClass(Order_lines_pk.class)
/** The occurrence of a single Product within a single Order */
public class Order_lines {
    @Id
    @ManyToOne
    /** The order that this line belongs to. */
    private Orders order;
    @Id
    @ManyToOne
    /** The product ordered in this line item of the order. */
    private Products product;
    @Column(nullable=false)
    /** The number of this item in this order.  If the customer
    changes their mind and wants more of this item, we come
    back to this row and update the quantity.
     */
    private int quantity;
    @Column(nullable = false)
    /** The price of this item FOR THIS ORDER.  The customer might
    have scored a discount from the unit_list_price for this
    product that is only in effect for this sale.
     */
    private double unit_sale_price;

    /**
     * Default Constructor for Order_lines
     */
    public Order_lines(){};

    /**
     * Constructor for all private variables in Order_lines
     *
     * @param order
     * @param product
     * @param quantity
     * @param unit_sale_price
     */
    public Order_lines(Orders order, Products product, int quantity, double unit_sale_price){
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unit_sale_price = unit_sale_price;
    }


    /**
     * A get method for order
     *
     * @return          current order
     */
    public Orders getOrder() {
        return order;
    }

    /**
     * A set method for order
     *
     * @param order
     */
    public void setOrder(Orders order) {
        this.order = order;
    }

    /**
     * A get method for product
     *
     * @return          current product
     */
    public Products getProduct() {
        return product;
    }

    /**
     * A set method for product
     *
     * @param product
     */
    public void setProduct(Products product) {
        this.product = product;
    }

    /**
     * A get method for quantity
     *
     * @return          current quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * A set method for quantity
     *
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * A get method for unit_sale_price
     *
     * @return          current unit_sale_price
     */
    public double getUnit_sale_price() {
        return unit_sale_price;
    }

    /**
     * A set method for unit_sale_price
     *
     * @param unit_sale_price
     */
    public void setUnit_sale_price(double unit_sale_price) {
        this.unit_sale_price = unit_sale_price;
    }

    /**
     * A method that compares an object to the Order_lines object and sees whether they are equal or not
     *
     * @param o
     * @return boolean          Whether or not they are equal
     */
    public boolean equals (Object o) {
        boolean results = false;
        if (this == o) {
            results = true;
        } else if (o == null || getClass() != o.getClass()) {
            results = false;
        } else {
            Order_lines ol = (Order_lines) o;
            results = this.getOrder().equals (ol.getOrder()) &&
                    this.getProduct() == ol.getProduct();
        }
        return results;
    }

    /**
     * A method that generates a hash code using the order value and the product value
     *
     * @return int      the hashcode of all input values
     */
    public int hasCode () {
        return Objects.hash(this.getOrder(), this.getProduct());
    }
}
