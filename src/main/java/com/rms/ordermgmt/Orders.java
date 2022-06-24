package src.main.java.com.rms.ordermgmt;

import src.main.java.com.rms.enums.IngredientsType;
import src.main.java.com.rms.enums.RecipeDetails;
import src.main.java.com.rms.readandwrite.ReadandWriteFile;
import src.main.java.com.rms.salesnexpenses.TotalExpenses;
import src.main.java.com.rms.salesnexpenses.TotalSales;
import src.main.java.com.rms.accountsmgmt.Accounts;
import src.main.java.com.rms.inventorymgmt.Inventory;
import java.util.HashMap;
import java.util.Scanner;
public class Orders extends ReadandWriteFile {
    private String fileName = "receipe.txt";
    private String data;
    private String dishName;
    private String quantity;
    private int price;
    private String[] Ingredients;
    private String[] arrayofData;
    public String[] getArrayofData() {
        return arrayofData;
    }
    public Orders(){

    }
   public String getFileName() {
        return fileName;
    }
    public String readData(String fileName){
        this.data = super.readData(fileName);
        dataArray();
        updateRecipeEnum();
        return data;
    }
   public boolean writeData(String fileName,String data){
        return super.writeData(fileName,data);
    }
    public void dataArray(){
        this.arrayofData = this.data.split("-");
    }
    private void updateRecipeEnum(){
        for (String str : this.arrayofData) {
            HashMap<String,Double> hashMap = new HashMap<>();
            String[] str1 = str.split(" ");
            RecipeDetails recipeEnum = RecipeDetails.valueOf(str1[0]);
            int price = Integer.parseInt(str1[str1.length-1]);
            for(int i=1;i<str1.length-1;i=i+2){
                hashMap.put(str1[i],Double.parseDouble(str1[i+1]));
           }
           recipeEnum.updaterecipeInformation(hashMap, price);
        }
    }
    private void displayMenu(){
        System.out.println("Place an order from the below Menu Card: ");
        RecipeDetails[] recipeArray = RecipeDetails.getRecipeEnum();
        System.out.println("Ingredients(No of units available) ");
        for(RecipeDetails name : recipeArray){
            HashMap<String,Double> recipeMap = name.getRecipeMap();
            System.out.println(name);
        }
    }
    private boolean readInputs(Scanner scan){
        boolean statusVal = false;
        System.out.println("Please enter dish name");
        dishName = scan.nextLine().trim();
        if(!RecipeDetails.isDishAvailable(dishName)){
            System.out.println("Improper Dish name");
        }
        else {
            System.out.println("Please enter quantity of the dish: ");
            quantity = scan.nextLine();
            statusVal = true;
        }
        return statusVal;
    }
    private boolean placingOrder(Inventory inventory, TotalSales totalSales, Accounts accounts){
        boolean status = false;
        //need to update inventory.txt with ingredients used
        for(String str : Ingredients){
            String[] str1 = str.split(" ");
            IngredientsType ingredientsEnum1 = IngredientsType.valueOf(str1[0]);
            ingredientsEnum1.setAvailableUnits(Double.parseDouble(str1[1]));
        }
        boolean status1 = writeData(inventory.getFileName(), Inventory.convertStringArrayToString(Ingredients, "\n"));
        //update account with available+price
        double availableBalance = accounts.getBalance();
        boolean status2 = accounts.writeData(accounts.getFileName(), (availableBalance + price));
        totalSales.setSalesAmount(totalSales.getSalesAmount()+(price*Integer.parseInt(quantity)));
        String sales = dishName+" "+quantity+" "+price*Integer.parseInt(quantity);
        String[] totalAvailableSalesList = totalSales.getTotalSales();
        if(totalAvailableSalesList!=null && totalAvailableSalesList.length>=1){
            System.out.println("totalAvailableSalesList: "+totalAvailableSalesList.length);
            for (int k = 0; k < totalAvailableSalesList.length; k++) {
                if (totalAvailableSalesList[k].contains(dishName)) {
                    String[] str1 = totalAvailableSalesList[k].split(" ");
                    str1[1] = String.valueOf(Integer.parseInt(str1[1])+Integer.parseInt(quantity));
                    str1[2] = String.valueOf(Integer.parseInt(str1[2])+price*Integer.parseInt(quantity));
                    StringBuffer sb = new StringBuffer();
                    sb.append(str1[0] + " " + str1[1] + " " + str1[2]);
                    totalAvailableSalesList[k] = String.valueOf(sb);
                    totalSales.setTotalSales(totalAvailableSalesList);
                    System.out.println("Order for "+dishName+" with quantity: "+quantity+" placed successfully");
                    status = true;
                }
                else{
                    String[] totalSalesList = new String[totalAvailableSalesList.length+1];
                    for (int  j= 0; j < totalAvailableSalesList.length; j++) {
                        totalSalesList[j] = totalAvailableSalesList[j];
                    }
                    totalSalesList[totalAvailableSalesList.length] = sales;
                    totalSales.setTotalSales(totalSalesList);
                    System.out.println("Order for "+dishName+" with quantity: "+quantity+" placed successfully");
                    status = true;
                }
            }
        }
        else{
            String[] totalSalesList = new String[1];
            totalSalesList[0] = sales;
            totalSales.setTotalSales(totalSalesList);
            System.out.println("Order for "+dishName+" with quantity: "+quantity+" placed successfully");
            status = true;
        }
        return status;
    }
    private boolean orderUnAvailableIngredients(Inventory inventory, TotalExpenses totalExpenses, Accounts accounts, TotalSales totalSales){
        boolean status = false;
        boolean statusVal = inventory.orderSpecificIngredients(totalExpenses, accounts);
        if(statusVal) {
            status = placeOrder(inventory, totalExpenses, accounts, totalSales);
        }
        return status;
    }
    public boolean placeOrder(Inventory inventory, TotalExpenses totalExpenses, Accounts accounts, TotalSales totalSales){
        boolean status = false;
        double requiredQuantity = 0.0;
        double availableQuantity = 0.0;
        String unAvailableIngredients = "";
        boolean notAvailableIngredQuantity = false;
        try{
            displayMenu();
            Scanner scan = new Scanner(System.in);
            status = readInputs(scan);
            if(!status){
                return status;
            }
            else{
                Ingredients =  inventory.getArrayofData();
                RecipeDetails recipeEnum = RecipeDetails.valueOf(dishName);
                int price = recipeEnum.getPrice();
                HashMap<String,Double> recipeMap = recipeEnum.getRecipeMap();
                for (String name : recipeMap.keySet()) {
                    requiredQuantity = recipeMap.get(name) * Double.parseDouble(quantity);
                    IngredientsType ingredientsEnum = IngredientsType.valueOf(name);
                    availableQuantity = ingredientsEnum.getAvailableUnits();
                    if (availableQuantity < requiredQuantity) {
                        if (unAvailableIngredients.length() > 1) {
                            unAvailableIngredients += ",";
                        }
                        unAvailableIngredients += ingredientsEnum;
                        notAvailableIngredQuantity = true;
                    }
                    else {
                        for (int j = 0; j < Ingredients.length; j++) {
                            if (Ingredients[j].contains(name)) {
                                String[] str1 = Ingredients[j].split(" ");
                                str1[1] = String.valueOf(Double.parseDouble(str1[1]) - requiredQuantity);
                                StringBuffer sb = new StringBuffer();
                                sb.append(str1[0] + " " + str1[1] + " " + str1[2]);
                                Ingredients[j] = String.valueOf(sb);
                            }
                        }
                    }
                }
            }
            if(notAvailableIngredQuantity) {
                System.out.println("availableQuantity for Ingredient: "+unAvailableIngredients+" is less");
                System.out.println("Do you want to continue with order? Yes/No");
                String response = scan.nextLine();
                if(response.equals("Yes")) {
                    status = orderUnAvailableIngredients(inventory, totalExpenses, accounts, totalSales);
                }
                else {
                    status = false;
                    return status;
                }
            }
            else {
                status = placingOrder(inventory, totalSales, accounts);
            }
        }
       catch(RuntimeException ex){
        ex.printStackTrace();
        }
    return status;
    }
}