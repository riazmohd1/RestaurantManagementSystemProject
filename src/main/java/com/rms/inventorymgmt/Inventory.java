package src.main.java.com.rms.inventorymgmt;

import src.main.java.com.rms.enums.IngredientsType;
import src.main.java.com.rms.readandwrite.ReadandWriteFile;
import src.main.java.com.rms.salesnexpenses.TotalExpenses;
import src.main.java.com.rms.accountsmgmt.Accounts;

import java.util.ArrayList;
import java.util.Scanner;
public class Inventory extends ReadandWriteFile {
    private String fileName = "ingredients.txt";
    private String data;
    private String[] arrayofData;
    private String[] totalExpensesList;
    private double totalExpenses;
    IngredientsType ingredientsEnum;
    String[] Ingredients;
    double availableBalance;
    int price;
    String[] ingredientstoBuy;
    String item;
    String quantity;
    public Inventory(){

    }
    public String getFileName() {
        return fileName;
    }
    public String getData() {
        return data;
    }
    public String[] getArrayofData() {
        return arrayofData;
    }
    public String readData(String fileName){
        this.data = super.readData(fileName);
        dataArray();
        updateIngredientEnum();
        return data;
    }
    public boolean writeData(String fileName,String data){
        return super.writeData(fileName,data);
    }
    public void dataArray(){
        this.arrayofData = this.data.split("-");
    }
    private void updateIngredientEnum(){
        for (String str : this.arrayofData) {
                String[] str1 = str.split(" ");
            IngredientsType ingredientsEnum = IngredientsType.valueOf(str1[0]);
            ingredientsEnum.updateUnitInformation(Double.parseDouble(str1[1]),Double.parseDouble(str1[2]));
        }
    }
    public static String convertStringArrayToString(String[] strArr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr)
            sb.append(str).append(delimiter);
        return sb.substring(0, sb.length() - 1);
    }
    public void viewAvailableIngredients(){
        IngredientsType[] ingredientsArray = IngredientsType.getIngredientsEnum();
        System.out.println("Ingredients(No of units available) ");
        for(IngredientsType name : ingredientsArray){
            System.out.println(name+"("+name.getAvailableUnits()+")");
        }
    }
    private void displayIngredients(){
        System.out.println("Order Specific Ingredients from below list: ");
        IngredientsType[] ingredientsArray = IngredientsType.getIngredientsEnum();
        System.out.println("Ingredients");
        for(IngredientsType name : ingredientsArray){
            System.out.println(name);
        }
    }
    private boolean readInputs(Scanner scan){
        boolean statusVal = false;
        System.out.println("Please enter Ingredient name: ");
        item = scan.nextLine().trim();
        if(!IngredientsType.isIngredientAvailable(item)){
            System.out.println("Improper Ingredient name");
        }
        else{
            System.out.println("Please enter quantity: ");
            quantity = scan.nextLine();
            statusVal = true;
        }
        return statusVal;
    }
    private void updateIngredients(TotalExpenses totalExpenses, Accounts accounts){
        boolean status = accounts.writeData(accounts.getFileName(), (availableBalance - price));
        for(String str : ingredientstoBuy){
            if(str!=null){
                String[] str1 = str.split(" ");
                if(!str1[0].trim().equals(" ")){
                    IngredientsType ingredientsEnum = IngredientsType.valueOf(str1[0].trim());
                    double availableUnits = ingredientsEnum.getAvailableUnits();
                    ingredientsEnum.setAvailableUnits(availableUnits+Double.parseDouble(str1[1]));
                }
            }

        }
        boolean status1 = writeData(getFileName(), convertStringArrayToString(Ingredients, "\n"));
        totalExpenses.setTotalExpensesList(ingredientstoBuy);
        totalExpenses.setTotalExpenses(price);

    }
    public boolean orderSpecificIngredients(TotalExpenses totalExpenses, Accounts accounts){
        boolean statusVal = false;
        try {
            Scanner scan = new Scanner(System.in);
            displayIngredients();
            Ingredients =  getArrayofData();;
            ingredientstoBuy = new String[Ingredients.length];
            String[] currentTotalExpensesList = totalExpenses.getTotalExpensesList();
            int ctelLength = 0;
            if(currentTotalExpensesList!=null && currentTotalExpensesList.length>0){
                ctelLength = currentTotalExpensesList.length-1;
                System.arraycopy(currentTotalExpensesList,0,ingredientstoBuy,0,currentTotalExpensesList.length);
            }

            int i = 0;
            while (i < Ingredients.length) {
                double pricePerQuantity = 0.0;
                statusVal = readInputs(scan);
                if(!statusVal){
                    return statusVal;
                }
                else{
                    IngredientsType ingredientsEnum = IngredientsType.valueOf(item);
                    pricePerQuantity = ingredientsEnum.getPricePerUnit();
                    double priceVal = Double.parseDouble(quantity) * pricePerQuantity;
                    ingredientstoBuy[i+ctelLength] = item + " " + quantity + " " + priceVal;
                    for (int j = 0; j < Ingredients.length; j++) {
                        if (Ingredients[j].contains(item)) {
                            String[] str1 = Ingredients[j].split(" ");
                            str1[1] = String.valueOf(Double.parseDouble(str1[1]) + Double.parseDouble(quantity));
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append(str1[0] + " " + str1[1] + " " + str1[2]);
                            Ingredients[j] = String.valueOf(stringBuffer);
                            price += priceVal;
                        }
                    }

                }
                System.out.println("Do you want to add more items? Yes/No");
                String decision = scan.nextLine();
                if (decision.equalsIgnoreCase("Yes"))
                    i++;
                else
                    break;
            }
            if (price > 0) {
                availableBalance = accounts.getBalance();
                if (availableBalance < price) {
                    statusVal = false;
                } else {
                    updateIngredients(totalExpenses, accounts);
                    statusVal = true;

                }
            }
        }
        catch(NullPointerException | ArrayIndexOutOfBoundsException | IllegalArgumentException ex){
            ex.printStackTrace();
        }
        return statusVal;
    }
}