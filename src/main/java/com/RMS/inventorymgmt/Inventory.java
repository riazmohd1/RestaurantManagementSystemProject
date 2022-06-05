package src.main.java.com.RMS.inventorymgmt;

import src.main.java.com.RMS.Enums.IngredientsEnum;
import src.main.java.com.RMS.ReadandWriteFile;
import src.main.java.com.RMS.RestaurantManagementSystem;
import src.main.java.com.RMS.TotalExpenses;
import src.main.java.com.RMS.accountsmgmt.Accounts;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;

public class Inventory extends ReadandWriteFile {
    private String fileName = "ingredients.txt";
    private String data;

    private String[] arrayofData;
    public String[] totalExpensesList;
    double totalExpenses;
    IngredientsEnum ingredientsEnum;
    public double getTotalExpenses() {
        return totalExpenses;
    }

    public Inventory(){

    }
    public Inventory(String fileName, double balance){
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
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

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public String[] getTotalExpensesList() {
        return totalExpensesList;
    }

    public void setTotalExpensesList(String[] totalExpensesList) {
        this.totalExpensesList = totalExpensesList;
    }

    public String readData(){
        this.data = super.readData(this.fileName);
        dataArray();
       return data;
    }
    public String readData(String fileName){
        this.data = super.readData(fileName);
        dataArray();
        updateIngredientEnum();
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
    public void updateIngredientEnum(){
        for (String str : this.arrayofData) {
                String[] str1 = str.split(" ");
            IngredientsEnum ingredientsEnum = IngredientsEnum.valueOf(str1[0]);
            ingredientsEnum.updateUnitInformation(Double.parseDouble(str1[1]),Double.parseDouble(str1[2]));;
        }
    }
    public static String convertStringArrayToString(String[] strArr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr)
            sb.append(str).append(delimiter);
        return sb.substring(0, sb.length() - 1);
    }
    public void viewAvailableIngredients(){
        IngredientsEnum[] ingredientsArray = IngredientsEnum.getIngredientsEnum();
        System.out.println("Ingredients(No of units available) ");
        for(IngredientsEnum name : ingredientsArray){
            System.out.println(name+"("+name.getAvailableUnits()+")");
        }
    }
    public String orderSpecificIngredients(TotalExpenses totalExpenses, Accounts accounts){
        String statusVal = "";
        String[] Ingredients =  getArrayofData();
        int price = 0;
        try {
            System.out.println("Order Specific Ingredients from below list: ");
            IngredientsEnum[] ingredientsArray = IngredientsEnum.getIngredientsEnum();
            System.out.println("Ingredients");
            for(IngredientsEnum name : ingredientsArray){
                System.out.println(name);
            }
            Scanner scan = new Scanner(System.in);
            String[] ingredientstoBuy = new String[Ingredients.length];
            /*for (int j = 0; j < Ingredients.length; j++) {
                ingredientstoBuy[j] = "";
            }*/
            int i = 0;

            while (i < Ingredients.length) {
                double pricePerQuantity = 0.0;
                System.out.println("Please enter Ingredient name: ");
                String item = scan.nextLine().trim();

                if(!IngredientsEnum.isIngredientAvailable(item)){
                    System.out.println("Improper Ingredient name");
                    return statusVal="false";
                }
                IngredientsEnum ingredientsEnum = IngredientsEnum.valueOf(item);
                pricePerQuantity = ingredientsEnum.getPricePerUnit();

                System.out.println("Please enter quantity: ");
                String quantity = scan.nextLine();
                double priceVal = Double.parseDouble(quantity) * pricePerQuantity;
                ingredientstoBuy[i] = item + " " + quantity + " " + String.valueOf(priceVal);
                for (int j = 0; j < Ingredients.length; j++) {
                    if (Ingredients[j].contains(item)) {
                        String[] str1 = Ingredients[j].split(" ");
                        str1[1] = String.valueOf(Double.parseDouble(str1[1]) + Double.parseDouble(quantity));
                        StringBuffer sb = new StringBuffer();
                        sb.append(str1[0] + " " + str1[1] + " " + str1[2]);
                        Ingredients[j] = String.valueOf(sb);
                    }
                }
                price += priceVal;

                System.out.println("Do you want to add more items? Yes/No");
                String decision = scan.nextLine();
                if (decision.equalsIgnoreCase("Yes"))
                    i++;
                else
                    break;
            }
            if (price > 0) {
                double availableBalance = accounts.getBalance();
                if (availableBalance < price) {
                    statusVal = "false";
                } else {
                    boolean status = accounts.writeData(accounts.getFileName(), (availableBalance - price));
                    for(String str : ingredientstoBuy){
                        if(str!=null){
                            String[] str1 = str.split(" ");
                            if(!str1[0].equals("")){
                                IngredientsEnum ingredientsEnum = IngredientsEnum.valueOf(str1[0].trim());
                                ingredientsEnum.setAvailableUnits(Double.parseDouble(str1[1]));;
                            }
                        }

                    }
                   boolean status1 = writeData(getFileName(), convertStringArrayToString(Ingredients, "\n"));
                   totalExpenses.setTotalExpensesList(ingredientstoBuy);
                   totalExpenses.setTotalExpenses(price);

                    statusVal = "true";
                }
            }
        }
        catch(NullPointerException | ArrayIndexOutOfBoundsException | IllegalArgumentException ex){
            ex.printStackTrace();
        }

        return statusVal;
    }
}