package csulb.cecs323.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
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
        name="ReturnProduct",
        query = "SELECT * " +
                "FROM   PRODUCTS " +
                "WHERE  UPC = ? ",
        resultClass = Products.class
)
@NamedNativeQuery(
        name="ReturnAllProducts",
        query = "SELECT * " +
                "FROM   PRODUCTS ",
        resultClass = Products.class
)
@NamedNativeQuery(
        name="UpdateQuantity",
        query = "UPDATE units_in_stock" +
                "FROM   PRODUCTS " +
                "SET ?" +
                "WHERE UPC = ?",
        resultClass = Products.class
)
/** Something that we stock, that the customer can order. */
public class Products {
    @Id
    @Column(nullable = false, length = 30)
    /** The Product Universal Product Code */
    private String UPC;

    @Column(nullable = false, length = 128)
    /** Descriptive name for the product */
    private String prod_name;

    @Column(nullable = false, length = 40)
    /** The name of the manufacturer. */
    private String mfgr;

    @Column(nullable = false, length = 20)
    /** The manufacturer's model number for this product. */
    private String model;

    @Column(nullable = false)
    /** Price in US $ */
    private double unit_list_price;

    @Column(nullable = false)
    /** The quantity of this item that we have on hand. */
    private int units_in_stock;

    public Products(String UPC, String prod_name, String mfgr, String model, double unit_list_price, int units_in_stock) {
        this.UPC = UPC;
        this.prod_name = prod_name;
        this.mfgr = mfgr;
        this.model = model;
        this.unit_list_price = unit_list_price;
        this.units_in_stock = units_in_stock;
    }

    /**
     * Default Constructor
     */
    public Products() {}

    public String getUPC() {
        return UPC;
    }

    /**
     * A set method for UPC
     *
     * @param UPC
     */
    public void setUPC(String UPC) {
        this.UPC = UPC;
    }

    /**
     * A get method for prod_name
     *
     * @return          current prod_name
     */
    public String getProd_name() {
        return prod_name;
    }

    /**
     * A set method for prod_name
     *
     * @param prod_name
     */
    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    /**
     * A get method for mfgr
     *
     * @return          current mfgr
     */
    public String getMfgr() {
        return mfgr;
    }

    /**
     * A set method for mfgr
     *
     * @param mfgr
     */
    public void setMfgr(String mfgr) {
        this.mfgr = mfgr;
    }

    /**
     * A get method for model
     *
     * @return          current model
     */
    public String getModel() {
        return model;
    }

    /**
     * A set method for model
     *
     * @param model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * A get method unit_list_price
     *
     * @return          current unit_list_price
     */
    public double getUnit_list_price() {
        return unit_list_price;
    }

    /**
     * A set method for unit_list_price
     *
     * @param unit_list_price
     */
    public void setUnit_list_price(double unit_list_price) {
        this.unit_list_price = unit_list_price;
    }

    /**
     * A get method for units_in_stock
     *
     * @return          current units_in_stock
     */
    public int getUnits_in_stock() {
        return units_in_stock;
    }

    /**
     * A set method for units_in_stock
     *
     * @param units_in_stock
     */
    public void setUnits_in_stock(int units_in_stock) {
        this.units_in_stock = units_in_stock;
    }

    /**
     * A toString method for Products that displays the UPC, prod_name, unit_list_price, and units_in_stock
     * return String            Data of product
     */
    @Override
    public String toString () {
        return "Product- UPC: " + this.UPC + ", Name: " + this.prod_name + ", Price: " + this.unit_list_price
                + " QTY on hand: " + this.units_in_stock;
    }
}
