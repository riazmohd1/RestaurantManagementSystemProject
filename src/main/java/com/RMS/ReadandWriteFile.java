package src.main.java.com.RMS;

import java.io.*;
import java.util.Scanner;

public class ReadandWriteFile implements RestaurantManagementSystem{
    private String[] arrayofData;
    private String data = "";

    public String[] getArrayofData() {
        return arrayofData;
    }
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setArrayofData(String[] arrayofData) {
        this.arrayofData = arrayofData;
    }
    public String readData(String fileName){

        try {
            File myObj = new File(".");
            String basePath = myObj.getCanonicalPath();;
            //System.out.println(basePath);
            myObj = new File(basePath+"\\resource\\"+fileName);
            // File myObj = new File("accounts.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                if(!data.equals(""))
                    data +="-";
                data += myReader.nextLine();
               // System.out.println(data);
            }
            myReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }

    public boolean writeData(String fileName, String data) {
        Boolean status = false;
        try {
            FileWriter myWriter = new FileWriter("C:\\Users\\moriaz\\Desktop\\Riaz\\learning\\prep4t\\workspace\\RestaurantManagementSystem\\resource\\" + fileName);
            myWriter.write(data);
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
            status = true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return status;
    }
    public void dataArray(){
        this.arrayofData = this.data.split("-");
    }
}
