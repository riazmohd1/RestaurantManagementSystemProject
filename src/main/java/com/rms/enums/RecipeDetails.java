package src.main.java.com.rms.enums;

import java.util.HashMap;

public enum RecipeDetails {
    Sandwich(new HashMap<>(),250),
    MasalaDosa(new HashMap<>(), 200),
    Coffee(new HashMap<>(), 25),
    Tea(new HashMap<>(), 35),
    FilterCoffee(new HashMap<>(), 30);

    private HashMap<String, Double> recipeMap;
    private int price;
    RecipeDetails(HashMap<String,Double> recipeMap, int price){
        this.recipeMap = recipeMap;
        this.price = price;
    }
    public HashMap<String, Double> getRecipeMap() {
        return recipeMap;
    }

    public int getPrice() {
        return price;
    }

    public void updaterecipeInformation(HashMap<String,Double> recipeMap, int price){
        this.recipeMap = recipeMap;
        this.price = price;

    }
    public static boolean isDishAvailable(String recipeName) throws IllegalArgumentException{
       try {
           RecipeDetails recipeEnum = RecipeDetails.valueOf(recipeName);
            for (RecipeDetails name : RecipeDetails.values()) {
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
    public static RecipeDetails[] getRecipeEnum(){
        return RecipeDetails.values();
    }
}