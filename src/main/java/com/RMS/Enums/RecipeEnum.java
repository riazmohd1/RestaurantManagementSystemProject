package src.main.java.com.RMS.Enums;

import java.util.HashMap;

public enum RecipeEnum {
    Sandwich(new HashMap<>(),250),
    MasalaDosa(new HashMap<>(), 200),
    Coffee(new HashMap<>(), 25),
    Tea(new HashMap<>(), 35),
    FilterCoffee(new HashMap<>(), 30);

    private HashMap<String, Double> recipeMap;

    private int price;
    RecipeEnum(HashMap<String,Double> recipeMap, int price){
        recipeMap = recipeMap;
        this.price = price;
    }
    //private HashMap<String,Integer> recipeMap;

    public HashMap<String, Double> getRecipeMap() {
        return recipeMap;
    }

    public void setRecipeMap(HashMap<String, Double> recipeMap) {
        this.recipeMap = recipeMap;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void updaterecipeInformation(HashMap<String,Double> recipeMap, int price){
        this.recipeMap = recipeMap;
        this.price = price;

    }

    public static boolean isDishAvailable(String recipeName) throws IllegalArgumentException{
       try {
           RecipeEnum recipeEnum = RecipeEnum.valueOf(recipeName);
            for (RecipeEnum name : RecipeEnum.values()) {
                if (name.equals(recipeEnum)) {
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
    public static RecipeEnum[] getRecipeEnum(){
        RecipeEnum[] recipeArray = RecipeEnum.values();
        return recipeArray;
    }
}
