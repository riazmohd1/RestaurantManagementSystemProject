package src.main.java.com.rms.accountsmgmt;

import src.main.java.com.rms.readandwrite.ReadandWriteFile;
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
    public double getBalance() {
        return balance;
    }
    public String readData(String fileName){
        String data = super.readData(fileName);
        this.balance = Double.parseDouble(data);
        return data;
    }
    public boolean writeData(String fileName,double Balance){
        return super.writeData(fileName,String.valueOf(Balance));
    }
}
