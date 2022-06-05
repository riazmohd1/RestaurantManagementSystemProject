package src.main.java.com.RMS;

import src.main.java.com.RMS.accountsmgmt.Accounts;
import src.main.java.com.RMS.inventorymgmt.Inventory;
import src.main.java.com.RMS.ordermgmt.Orders;

import java.util.Formatter;
import java.util.Scanner;

public class RestaurantManagementApplication {
    public static void main(String[] args) {
        TotalExpenses totalExpenses = new TotalExpenses();
        TotalSales totalSales = new TotalSales();
        Accounts accounts  = new Accounts();
        String availableBalance = accounts.readData(accounts.getFileName());
        System.out.println("availableBalance: "+availableBalance);
        Inventory inventory = new Inventory();
        String inventoryList = inventory.readData(inventory.getFileName());
        Orders orders = new Orders();
        String data2 = orders.readData(orders.getFileName());
        int inputVal = 0;
        while(inputVal<7) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter the number of command as an input:\n" +
                                "Command #1 : View Available Ingredients\n" +
                                "Command #2 : Order Specific Ingredients\n" +
                                "Command #3 : View Total Sales\n" +
                                "Command #4 : View Total Expenses\n" +
                                "Command #5 : View Net Profit\n" +
                                "Command #6 : Place Order\n" +
                                "Command #7 :  Exit From the Program\n");
            String input = scanner.nextLine();
            if (!input.matches("[0-9]+")) {
                System.out.println("Please enter a valid number between from 1-7");
                continue;
            }
            inputVal = Integer.parseInt(input);
            if (inputVal < 0 && inputVal > 7) {
                System.out.println("Please enter a valid number between from 1-7");
                continue;
            }
            if (inputVal == 7) {
                System.out.println("EXit from the program");
                System.exit(0);
            }
            else{
                switch(inputVal){
                    case 1:
                        inventory.viewAvailableIngredients();
                        break;
                    case 2:
                        String status= inventory.orderSpecificIngredients(totalExpenses, accounts);
                        if(status.equals("true"))
                            System.out.println("Ingredients order placed successfully.");
                        else if(status.equals("false"))
                            System.out.println("Insufficient Account Balance");
                        else
                            System.out.println("Ingredients order did not placed");

                        break;
                    case 3:
                        try {
                            Formatter fmt = new Formatter();
                            System.out.println("Total sales are :");
                            String[] totalSalesList = totalSales.getTotalSales();
                            if (totalSalesList != null) {
                                fmt.format("%15s %15s %15s\n", "Dishes", "quantity", "totalPrice");
                                for (String str : totalSalesList) {
                                    String[] str1 = str.split(" ");
                                    if (str1.length > 1) {
                                        fmt.format("%14s %14s %14s\n", str1[0], str1[1], str1[2]);
                                    }

                                }
                                System.out.println(fmt);
                            } else {
                                System.out.println("No sales details available");
                            }
                        }
                        catch(NullPointerException ex){
                            ex.printStackTrace();
                        }
                        break;
                    case 4:
                        try {
                            Formatter fmt = new Formatter();
                            System.out.println("Total expenses are :");
                            String[] totalExpensesList = totalExpenses.getTotalExpensesList();
                            if (totalExpensesList != null) {
                                //int expenses = totalExpenses.getTotalExpenses();
                                fmt.format("%15s %15s %15s\n", "Items", "quantity", "totalPrice");
                                for (String str : totalExpensesList) {
                                    if(str!=null) {
                                        String[] str1 = str.split(" ");
                                        if (str1.length > 1) {
                                            fmt.format("%14s %14s %14s\n", str1[0], str1[1], str1[2]);
                                        }
                                    }
                                }
                                System.out.println(fmt);
                            } else {
                                System.out.println("No expense details available");
                            }
                        }
                        catch(NullPointerException ex){
                            ex.printStackTrace();
                        }
                        break;
                    case 5:
                        System.out.println("Net Profit is: :"+(totalSales.getSalesAmount() -totalExpenses.getTotalExpenses()));
                        break;
                    case 6:
                        orders.placeOrder(inventory,totalExpenses,accounts,totalSales);
                        break;
                }
            }

        }
    }
}
