package src.main.java.com.RMS.accountsmgmt;

import src.main.java.com.RMS.ReadandWriteFile;
public class Accounts extends ReadandWriteFile{
    private double balance;
    private String fileName = "accounts.txt";
    public Accounts(){

    }
    public Accounts(String fileName, double balance){
        this.fileName = fileName;
        this.balance = balance;

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

   public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }



    public String readData(){
        String data = super.readData(this.fileName);
        this.balance = Double.parseDouble(data);
        return data;

    }
    public String readData(String fileName){
        String data = super.readData(fileName);
        this.balance = Double.parseDouble(data);
        return data;
    }
    public boolean writeData(){
        boolean status = super.writeData(this.fileName,String.valueOf(this.balance));
        return status;
    }
    public boolean writeData(String fileName,double Balance){
        boolean status = super.writeData(fileName,String.valueOf(Balance));
        return status;
    }
}
