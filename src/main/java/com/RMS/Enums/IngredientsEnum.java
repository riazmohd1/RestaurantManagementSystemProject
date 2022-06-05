package src.main.java.com.RMS.Enums;

import java.util.ArrayList;
import java.util.HashMap;

public enum IngredientsEnum {
    Tomato(10, 50),
    Potato(5,30),
    Onions(4,45),
    Batter(2,90),
    Spices(3,100),
    Milk(2,50),
    CoffeePowder (7,30),
    TeaLeaves(6,40),
    Oregano(3,10),
    Cheese(4,150),
    Yoghurt(2,30),
    Bread(30,3),
    Capsicum(2,10);

    IngredientsEnum(double availableUnits, double pricePerUnit){
        this.availableUnits = availableUnits;
        this.pricePerUnit = pricePerUnit;

    }
   private double availableUnits;
   private double pricePerUnit;

    public double getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(double availableUnits) {
        this.availableUnits = availableUnits;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
    public void updateUnitInformation(double availableUnits,double pricePerUnit){
        this.availableUnits = availableUnits;
        this.pricePerUnit = pricePerUnit;

    }

    public static boolean isIngredientAvailable(String ingredientName) throws IllegalArgumentException{
       try{
           IngredientsEnum ingredientsEnum = IngredientsEnum.valueOf(ingredientName);
            for(IngredientsEnum name : IngredientsEnum.values()){
                if(name.equals(ingredientsEnum)) {
                    return true;
                }
            }
        }
        catch(IllegalArgumentException ia){
            ia.printStackTrace();
            return false;
        }
        return false;
    }
    public static IngredientsEnum[] getIngredientsEnum(){
        IngredientsEnum[] ingredientsArray = IngredientsEnum.values();
        return ingredientsArray;
    }
}
