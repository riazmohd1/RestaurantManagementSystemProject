package src.main.java.com.rms.main;

import src.main.java.com.rms.salesnexpenses.TotalExpenses;
import src.main.java.com.rms.salesnexpenses.TotalSales;
import src.main.java.com.rms.accountsmgmt.Accounts;
import src.main.java.com.rms.inventorymgmt.Inventory;
import src.main.java.com.rms.ordermgmt.Orders;

import java.util.Formatter;
import java.util.Scanner;

public class RestaurantManagementApplication {
    TotalExpenses totalExpenses;
    TotalSales totalSales;
    Accounts accounts;
    String availableBalance;
    Inventory inventory;
    String inventoryList;
    Orders orders;
    String data2;
    public void initialize(){
        totalExpenses = new TotalExpenses();
        totalSales = new TotalSales();
        accounts  = new Accounts();
        double availableBalance = Double.parseDouble((accounts.readData(accounts.getFileName())));
        System.out.println("availableBalance: "+availableBalance);
        inventory = new Inventory();
        String inventoryList = inventory.readData(inventory.getFileName());
        orders = new Orders();
        String data2 = orders.readData(orders.getFileName());
    }
    public void readInputs(){
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
           executeApplication(inputVal);
        }
    }
    public void executeApplication(int inputVal){
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
                    boolean status= inventory.orderSpecificIngredients(totalExpenses, accounts);
                    if(status)
                        System.out.println("Ingredients order placed successfully.");
                    else if(!status)
                        System.out.println("Insufficient Account Balance");
                    else
                        System.out.println("Ingredients order did not placed");

                    break;
                case 3:
                    totalSales.viewTotalSales();
                    break;
                case 4:
                    totalExpenses.viewTotalExpenses();
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
    public static void main(String[] args) {
        RestaurantManagementApplication restaurantManagementApplication = new RestaurantManagementApplication();
        restaurantManagementApplication.initialize();
        restaurantManagementApplication.readInputs();
    }
}
