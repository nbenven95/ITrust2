package edu.ncsu.csc.itrust2.forms.patient;

import org.hibernate.validator.constraints.NotEmpty;

public class FoodDiaryEntryForm {

    /**
     * Empty constructor to make a DiaryEntryForm for the user to fill out
     */
    public FoodDiaryEntryForm () {
    }

    /**
     * The date as milliseconds since epoch for the entry
     */
    private String date;

    /**
     * The type of meal
     */
    private String mealType;

    /**
     * The food eaten
     */
    @NotEmpty
    private String food;

    /**
     * The amount of servings
     */
    @NotEmpty
    private String servings;

    /**
     * The amount of calories
     */
    @NotEmpty
    private String calories;

    /**
     * The amount of fat
     */
    @NotEmpty
    private String fat;

    /**
     * The amount of sodium
     */
    @NotEmpty
    private String sodium;

    /**
     * The amount of carbs
     */
    @NotEmpty
    private String carbs;

    /**
     * The amount of sugars
     */
    @NotEmpty
    private String sugars;

    /**
     * The amount of fiber
     */
    @NotEmpty
    private String fiber;

    /**
     * The amount of protein
     */
    @NotEmpty
    private String protein;

    /**
     * Gets the date
     *
     * @return the date as milliseconds since epoch
     */
    public String getDate () {
        return date;
    }

    /**
     * Sets the date
     *
     * @param date
     *            the date as milliseconds since epoch to set
     */
    public void setDate ( final String date ) {
        this.date = date;
    }

    /**
     * Gets the meal type
     *
     * @return the mealType
     */
    public String getMealType () {
        return mealType;
    }

    /**
     * Sets the mealType
     *
     * @param mealType
     *            the mealType to set
     */
    public void setMealType ( final String mealType ) {
        this.mealType = mealType;
    }

    /**
     * Gets the food eaten
     *
     * @return the food
     */
    public String getFood () {
        return food;
    }

    /**
     * Sets the food eaten
     *
     * @param food
     *            the food to set
     */
    public void setFood ( final String food ) {
        this.food = food;
    }

    /**
     * Gets the servings
     *
     * @return the servings
     */
    public String getServings () {
        return servings;
    }

    /**
     * Sets the servings
     *
     * @param servings
     *            the servings to set
     */
    public void setServings ( final String servings ) {
        this.servings = servings;
    }

    /**
     * Gets the calories
     *
     * @return the calories
     */
    public String getCalories () {
        return calories;
    }

    /**
     * Sets the calories
     *
     * @param calories
     *            the calories to set
     */
    public void setCalories ( final String calories ) {
        this.calories = calories;
    }

    /**
     * Gets the fat
     *
     * @return the fat
     */
    public String getFat () {
        return fat;
    }

    /**
     * Sets the fat
     *
     * @param fat
     *            the fat to set
     */
    public void setFat ( final String fat ) {
        this.fat = fat;
    }

    /**
     * Gets the sodium
     *
     * @return the sodium
     */
    public String getSodium () {
        return sodium;
    }

    /**
     * Sets the sodium
     *
     * @param sodium
     *            the sodium to set
     */
    public void setSodium ( final String sodium ) {
        this.sodium = sodium;
    }

    /**
     * Gets the carbs
     *
     * @return the carbs
     */
    public String getCarbs () {
        return carbs;
    }

    /**
     * Sets the carbs
     *
     * @param carbs
     *            the carbs to set
     */
    public void setCarbs ( final String carbs ) {
        this.carbs = carbs;
    }

    /**
     * Gets the sugars
     *
     * @return the sugars
     */
    public String getSugars () {
        return sugars;
    }

    /**
     * Sets the sugars
     *
     * @param sugars
     *            the sugars to set
     */
    public void setSugars ( final String sugars ) {
        this.sugars = sugars;
    }

    /**
     * Gets the fiber
     *
     * @return the fiber
     */
    public String getFiber () {
        return fiber;
    }

    /**
     * Sets the fiber
     *
     * @param fiber
     *            the fiber to set
     */
    public void setFiber ( final String fiber ) {
        this.fiber = fiber;
    }

    /**
     * Gets the protein
     *
     * @return the protein
     */
    public String getProtein () {
        return protein;
    }

    /**
     * Sets the protein
     *
     * @param protein
     *            the protein to set
     */
    public void setProtein ( final String protein ) {
        this.protein = protein;
    }
}
