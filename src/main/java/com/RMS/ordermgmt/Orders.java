package src.main.java.com.RMS.ordermgmt;

import src.main.java.com.RMS.Enums.IngredientsEnum;
import src.main.java.com.RMS.Enums.RecipeEnum;
import src.main.java.com.RMS.ReadandWriteFile;
import src.main.java.com.RMS.TotalExpenses;
import src.main.java.com.RMS.TotalSales;
import src.main.java.com.RMS.accountsmgmt.Accounts;
import src.main.java.com.RMS.inventorymgmt.Inventory;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Orders extends ReadandWriteFile {
    private String fileName = "receipe.txt";
    private String data;
    private String[] arrayofData;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String[] getArrayofData() {
        return arrayofData;
    }

    public void setArrayofData(String[] arrayofData) {
        this.arrayofData = arrayofData;
    }

    public Orders(){

    }
    public Orders(String fileName, double balance){
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String readData(){
        this.data = super.readData(this.fileName);
        dataArray();
        return data;
    }
    public String readData(String fileName){
        this.data = super.readData(fileName);
        dataArray();
        updateRecipeEnum();
        return data;
    }
    public boolean writeData(){
        return super.writeData(this.fileName,this.data);
    }
    public boolean writeData(String fileName,String data){
        return super.writeData(fileName,data);


    }
    public void dataArray(){
        this.arrayofData = this.data.split("-");
    }
    public void updateRecipeEnum(){
        for (String str : this.arrayofData) {
            HashMap<String,Double> hashMap = new HashMap<>();
            String[] str1 = str.split(" ");
            RecipeEnum recipeEnum = RecipeEnum.valueOf(str1[0]);
            int price = Integer.parseInt(str1[str1.length-1]);
            for(int i=1;i<str1.length-1;i=i+2){
                hashMap.put(str1[i],Double.parseDouble(str1[i+1]));
           }
           recipeEnum.updaterecipeInformation(hashMap, price);
        }
    }
    public String placeOrder(Inventory inventory, TotalExpenses totalExpenses, Accounts accounts, TotalSales totalSales){
        String status = "";
        double requiredQuantity = 0.0;
        double availableQuantity = 0.0;
        String unAvailableIngredients = "";
        boolean notAvailableIngredQuantity = false;
       try{
            System.out.println("Place an order from the below Menu Card: ");
            RecipeEnum[] recipeArray = RecipeEnum.getRecipeEnum();
            System.out.println("Ingredients(No of units available) ");
            for(RecipeEnum name : recipeArray){
                HashMap<String,Double> recipeMap = name.getRecipeMap();
                System.out.println(name);
            }
            System.out.println("Please enter dish name");
            Scanner scan = new Scanner(System.in);
            String dishName = scan.nextLine().trim();
            if(!RecipeEnum.isDishAvailable(dishName)){
                System.out.println("Improper Dish name");
                return status="false";
            }
            System.out.println("Please enter quantity of the dish: ");
            String quantity = scan.nextLine();
            String[] Ingredients =  inventory.getArrayofData();
            RecipeEnum recipeEnum = RecipeEnum.valueOf(dishName);
            int price = recipeEnum.getPrice();
            HashMap<String,Double> recipeMap = recipeEnum.getRecipeMap();
           for (String name : recipeMap.keySet()) {
               String ingredientName1 = name;
               requiredQuantity = recipeMap.get(ingredientName1) * Double.parseDouble(quantity);
               IngredientsEnum ingredientsEnum = IngredientsEnum.valueOf(ingredientName1);
               availableQuantity = ingredientsEnum.getAvailableUnits();
               if (availableQuantity < requiredQuantity) {
                   if (unAvailableIngredients.length() > 1) {
                       unAvailableIngredients += ",";
                   }
                   unAvailableIngredients += ingredientsEnum;
                   notAvailableIngredQuantity = true;

               } else {
                   for (int j = 0; j < Ingredients.length; j++) {
                       if (Ingredients[j].contains(ingredientName1)) {
                           String[] str1 = Ingredients[j].split(" ");
                           str1[1] = String.valueOf(Double.parseDouble(str1[1]) - requiredQuantity);
                           StringBuffer sb = new StringBuffer();
                           sb.append(str1[0] + " " + str1[1] + " " + str1[2]);
                           Ingredients[j] = String.valueOf(sb);
                       }
                   }
               }
           }
                if(notAvailableIngredQuantity == true){
                    System.out.println("availableQuantity for Ingredient: "+unAvailableIngredients+" is less");
                    System.out.println("Do you want to continue with order? Yes/No");
                    String response = scan.nextLine();
                    if(response.equals("Yes")){
                        String statusVal = inventory.orderSpecificIngredients(totalExpenses, accounts);
                        if(statusVal.equals("true")) {
                            String statusVal1 = placeOrder(inventory, totalExpenses, accounts, totalSales);
                            return statusVal1;
                        }
                        else {
                            status = "false";
                            return status;
                        }
                    }
                    else {
                        status = "false";
                        return status;
                    }
                }
                else{
                    //need to update inventory.txt with ingredients used
                    for(String str : Ingredients){
                        //System.out.println("Ingredients: "+str);
                        String[] str1 = str.split(" ");
                        IngredientsEnum ingredientsEnum1 = IngredientsEnum.valueOf(str1[0]);
                        ingredientsEnum1.setAvailableUnits(Double.parseDouble(str1[1]));;
                    }

                    boolean status1 = writeData(inventory.getFileName(), inventory.convertStringArrayToString(Ingredients, "\n"));
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
                                status = "true";
                                return status;
                            }
                            else{
                                String[] totalSalesList = new String[totalAvailableSalesList.length+1];
                                for (int  j= 0; j < totalAvailableSalesList.length; j++) {
                                    totalSalesList[j] = totalAvailableSalesList[j];
                                }
                                totalSalesList[totalAvailableSalesList.length] = sales;
                                totalSales.setTotalSales(totalSalesList);
                                System.out.println("Order for "+dishName+" with quantity: "+quantity+" placed successfully");
                                status = "true";
                                return status;
                            }
                        }
                    }
                    else{
                        String[] totalSalesList = new String[1];
                        totalSalesList[0] = sales;
                        totalSales.setTotalSales(totalSalesList);
                        System.out.println("Order for "+dishName+" with quantity: "+quantity+" placed successfully");
                        status = "true";
                        return status;
                    }

                }


        }
       catch(RuntimeException ex){
        ex.printStackTrace();
        }
    return status;
    }
}
