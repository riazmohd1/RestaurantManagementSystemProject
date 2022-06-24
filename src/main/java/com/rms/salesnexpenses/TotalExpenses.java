package src.main.java.com.rms.salesnexpenses;

import java.util.Formatter;

public class TotalExpenses {
    public String[] totalExpensesList;
    int totalExpense;

    public int getTotalExpenses() {
        return totalExpense;
    }

    public void setTotalExpenses(int totalExpense) {
        this.totalExpense = totalExpense;
    }

    public String[] getTotalExpensesList() {
        return totalExpensesList;
    }

    public void setTotalExpensesList(String[] totalExpensesList) {
        this.totalExpensesList = totalExpensesList;
    }

    public void viewTotalExpenses(){
        try {
            Formatter fmt = new Formatter();
            System.out.println("Total expenses are :");
            String[] totalExpensesList = getTotalExpensesList();
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
    }
}
