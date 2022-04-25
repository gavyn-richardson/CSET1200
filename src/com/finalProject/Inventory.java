package com.finalProject;

import java.io.PrintStream;
import java.util.Date;

public class Inventory
{
    //Data Fields
    public String productName;
    private final int serialID;
    private final double price;
    private final String description;
    private static String seller;
    private String buyer;
    private Date date = new Date();

    /* Constructors */

    public Inventory(String newProductName, double newPrice, String newDescription, String newSeller)
    {
        this.serialID = (int)Math.round((Math.random()*Math.pow(10,7)));
        this.productName = newProductName;
        this.price = newPrice;
        this.description = newDescription;
        seller = newSeller;
    }

    /* Accessors */
    public void showInventoryDetails()
    {
        System.out.printf("%-20s%-80s%10.2f%10d%30s%40s%n", productName, description, price,serialID,seller,date);
    }
    public void showSoldInventoryDetails()
    {
        System.out.printf("%-20s%-80s%10.2f%10d%30s%40s%n", productName, description, price,serialID,buyer,date);
    }

    public int getSerialID() {return serialID;}

    public static String getSeller() {return seller;}

    /* Mutators */
    public void setBuyer(String newBuyer) {buyer = newBuyer;}

    public void setDateBought() {date = new Date();}
}
