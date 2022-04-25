/*
Ye' Bay
ShoppingApp Build Version 1.1.2
Includes:
 - Inventory class to serve as template for every inventory item added
 - Main menu that leads to each method handling inventory list changes/views
 - Improved exception handling so the app won't exit/glitch if the user makes a mistake
 - New item file that will autofill the inventory with some of Davy Jones belongings
*/

package com.finalProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        //Instantiates Main Menu Scanner and inventory lists
        Scanner input = new Scanner(System.in);
        ArrayList<Object> inventoryList = new ArrayList<>();
        ArrayList<Object> soldInventoryList = new ArrayList<>();

        //Reads text lines from a .txt file and then instantiates an ArrayList of
        //Inventory objects items in the file
        try {
            File file = new File("src\\com\\finalProject\\items.txt");
            Scanner fileIn = new Scanner(file);
            while (fileIn.hasNextLine()) {
                boolean cnt2 = true;
                while (cnt2) {
                    String seller = "Davy Jones";
                    String productName = fileIn.nextLine();
                    String description = fileIn.nextLine();
                    double price = fileIn.nextDouble();
                    fileIn.nextLine();

                    Inventory fileInventory = new Inventory(productName, price, description, seller);
                    inventoryList.add(fileInventory);
                    cnt2 = false;
                }
            }
            fileIn.close();
        } catch (NoSuchElementException e) {System.out.println("Inventory Loaded\n");
        }

        //Username entry
        System.out.println("Greetings matey. Enter ye' username:");
        String username = input.next();

        //Main Menu Loop
        boolean cnt = true;
        menu();
        do {
            int userIn = 0;
            boolean cnt1 = true;

            while (cnt1) {
                try {
                    Scanner in = new Scanner(System.in);
                    userIn = in.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Sorry matey, ye' need to enter a number.\n");
                } finally {
                    cnt1 = false;
                }
            }

            switch (userIn) {
                case 1:
                    displayInventory(inventoryList);
                    break;
                case 2:
                    addInventory(inventoryList, username);
                    break;
                case 3:
                    sellInventory(inventoryList, soldInventoryList, username);
                    break;
                case 4:
                    displaySoldInventory(soldInventoryList);
                    break;
                case 5:
                    deleteInventory(inventoryList, username);
                    break;
                case 6:
                    System.out.println("Exiting Ye' Bay, see ya later matey.");
                    cnt = false;
                    break;
                default:
                    menu();
                    break;
            }

        } while (cnt);
    }

    /*Menu Methods*/

    //Prints out main menu
    public static void menu() {
        System.out.println("\t\t\t\tWelcome to Ye'Bay!\t\t\t\t\n\nPlease enter one of the follow options to continue:\n\nBrowse for Treasure\t\t'1'\nSell Ye' Treasure\t\t'2'\n" +
                "Buy some Booty\t\t\t'3'\nBrowse Past Plunders\t'4'\nDelete an Item\t\t\t'5'\nExit Ye'Bay\t\t\t\t'6'\n");
    }

    //Prints out the current available inventory
    public static void displayInventory(ArrayList<Object> inventoryList) {
        System.out.printf("%90s%n%n", "= Inventory =");
        System.out.printf("%-20s%-80s%10s%11s%25s%35s%n%n", "Product Name", "Description", "Price", "Serial ID", "Seller", "Date Posted");
        for (Object i : inventoryList) {
            Inventory inventory = (Inventory) i;
            inventory.showInventoryDetails();
        }
        System.out.println("\nEnter '0' to return to the menu.\n");
    }

    //Prints out sold inventory
    public static void displaySoldInventory(ArrayList<Object> soldInventoryList) {
        System.out.printf("%90s%n%n", "= Inventory =");
        System.out.printf("%-20s%-80s%10s%10s%25s%35s%n%n", "Product Name", "Description", "Price", "Serial ID", "Buyer", "Date Posted");
        for (Object i : soldInventoryList) {
            Inventory inventory = (Inventory) i;
            inventory.showSoldInventoryDetails();
        }
        System.out.println("\nEnter '0' to return to the menu.\n");
    }

    //Adds inventory to the inventory list
    public static void addInventory(ArrayList<Object> inventoryList, String username) {
        Scanner input = new Scanner(System.in);
        System.out.println("So... ye' would like to sell some of thy booty!\n");

        try {
            System.out.println("Item Name?");
            String newProductName = input.nextLine();

            System.out.println("Item Description?");
            String newDescription = input.nextLine();

            System.out.println("Item Price?");
            double newPrice = input.nextDouble();

            inventoryList.add(new Inventory(newProductName, newPrice, newDescription, username));

            System.out.println("Congrats matey. Ye' treasure has been posted!\n");
            System.out.println("Enter '0' to return to the menu.\n");
        } catch (InputMismatchException e) {
            System.out.println("Sorry matey. Make sure your inputs are correct.\n");
            menu();
        }
    }

    //Maps the list to a hashmap so the serial ID of the inventory items can be used to search
    //Returns the object with the serialID matching the ID the user enters
    public static Inventory searchInventory(ArrayList<Object> inventoryList, int userSerialID) {
        Map<Integer, Inventory> inventoryMap = new HashMap<>();
        for (Object i : inventoryList) {
            Inventory inventory = (Inventory) i;
            int serialID = inventory.getSerialID();
            inventoryMap.put(serialID, inventory);
        }

        Inventory inventory = inventoryMap.get(userSerialID);
        if (inventory == null) {
            System.out.println("Sorry Matey... it seems X didn't mark the spot.\n");
            return null;
        } else {
            System.out.println("Grab your grog, ye treasure has been found!\n");
            return inventory;
        }

    }

    //Removes the user selected item from the current inventory list
    public static void deleteInventory(ArrayList<Object> inventoryList, String username) {
        Scanner input = new Scanner(System.in);
        int userSerialID;

        try {
            System.out.println("Please enter the Serial ID for the item ye' want to delete:");
            userSerialID = input.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Sorry matey. It seems something went wrong.");
            return;
        }

        Inventory inventory = searchInventory(inventoryList, userSerialID);
        if (!username.equals(Inventory.getSeller())) {
            System.out.println("It looks like you don't own this item.\n\nPlease enter '0' to go back to the menu.\n");
            return;
        } else if (inventory == null) {
            System.out.println("It looks like we couldn't find your item.\n\nPlease enter '0' to go back to the menu.\n");
        } else {
            System.out.printf("%-20s%-80s%10s%11s%25s%35s%n%n", "Product Name", "Description", "Price", "Serial ID", "Seller", "Date Posted");
            inventory.showInventoryDetails();
            System.out.println("\nAre ye' sure ye' would like to delete this item? <Y/N>\n");
            String cnd = input.next();
            if (cnd.equalsIgnoreCase("Y")) {
                inventoryList.remove(inventory);
                System.out.println("Ye' item has been deleted\n");
            }
        }
        System.out.println("Please enter '0' to go back to the menu.\n");
    }

    //Removes the user selected item from the current inventory list
    //Sets the date and buyer then adds the inventory object to the sold inventory
    public static void sellInventory(ArrayList<Object> inventoryList, ArrayList<Object> soldInventoryList, String username) {
        Scanner input = new Scanner(System.in);
        int userSerialID;

        try {
            System.out.println("Please enter the Serial ID for the item ye' seek to purchase:");
            userSerialID = input.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Sorry matey. Try entering the number again.");
            return;
        }

        Inventory inventory = searchInventory(inventoryList, userSerialID);
        if (inventory == null) {
            System.out.println("It seems we couldn't find your item. Please enter '0' to go back to the menu\n");
            return;
        } else {
            System.out.printf("%-20s%-80s%10s%11s%25s%35s%n%n", "Product Name", "Description", "Price", "Serial ID", "Seller", "Date Posted");
            inventory.showInventoryDetails();
            System.out.println("\nWould ye' like to buy this item? <Y,N>\n");
            String cnd = input.next();
            if (!cnd.equalsIgnoreCase("Y")) {
                System.out.println("I respect ye' decision. Please enter '0' to go back to the menu.\n");
                return;
            } else {
                inventory.setDateBought();
                inventory.setBuyer(username);
                inventoryList.remove(inventory);
                soldInventoryList.add(inventory);
                System.out.println("Land Ho!! This item now belongs to ye'.\n");
            }
        }
        System.out.println("Please enter '0' to go back to the menu.\n");
    }
}



















