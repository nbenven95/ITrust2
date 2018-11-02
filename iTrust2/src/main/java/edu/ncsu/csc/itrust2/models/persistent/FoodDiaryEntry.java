package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.criterion.Criterion;
import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.itrust2.forms.patient.FoodDiaryEntryForm;
import edu.ncsu.csc.itrust2.models.enums.MealType;

/**
 * Class representing a DiaryEntry obejct. This deals with any information that
 * will be stored in the DB to describe a patient's food diary entry.
 *
 * @author Brendan Boss (blboss)
 * @author Kai Presler-Marshall
 *
 */
@Entity
@Table ( name = "FoodDiaryEntry" )
public class FoodDiaryEntry extends DomainObject<FoodDiaryEntry> implements Serializable {

    /**
     * Randomly generated ID.
     */
    private static final long serialVersionUID = 424094627895115508L;

    /**
     * Get a specific diary entry by the database ID
     *
     * @param id
     *            the database ID
     * @return the specific diary entry with the desired ID
     */
    public static FoodDiaryEntry getById ( final Long id ) {
        try {
            return getWhere( eqList( ID, id ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * Get a list of DiaryEntries by patient.
     *
     * @param patient
     *            the username of the patient whose entries are being searched
     *            for
     * @return a list of DiaryEntries for the given patient
     */
    public static List<FoodDiaryEntry> getByPatient ( final String patient ) {
        return getWhere( eqList( "patient", patient ) );
    }

    /**
     * Retrieves all matching FoodDiaryEntries from the database that meet the
     * restrictions provided
     *
     * @param where
     *            Restrictions on what to retrieve
     * @return All matching entries
     */
    @SuppressWarnings ( "unchecked" )
    private static List<FoodDiaryEntry> getWhere ( final List<Criterion> where ) {
        return (List<FoodDiaryEntry>) getWhere( FoodDiaryEntry.class, where );
    }

    /**
     * Default constructor for making a DiaryEntry that will have its values set
     * without a form.
     */
    public FoodDiaryEntry () {
    }

    /**
     * Creates a Diary Entry based on the FoodDiaryEntryForm
     *
     * @param def
     *            DiaryEntry to create
     */
    public FoodDiaryEntry ( final FoodDiaryEntryForm def ) {
        if ( null != def.getDate() ) {
            setDate( Long.valueOf( def.getDate() ) );
        }

        if ( null != def.getMealType() ) {
            setMealType( MealType.valueOf( def.getMealType() ) );
        }
        setFood( def.getFood() );
        if ( null != def.getServings() ) {
            setServings( Integer.valueOf( def.getServings() ) );
        }
        if ( null != def.getCalories() ) {
            setCalories( Integer.valueOf( def.getCalories() ) );
        }
        if ( null != def.getFat() ) {
            setFat( Integer.valueOf( def.getFat() ) );
        }
        if ( null != def.getSodium() ) {
            setSodium( Integer.valueOf( def.getSodium() ) );
        }
        if ( null != def.getCarbs() ) {
            setCarbs( Integer.valueOf( def.getCarbs() ) );
        }
        if ( null != def.getSugars() ) {
            setSugars( Integer.valueOf( def.getSugars() ) );
        }
        if ( null != def.getFiber() ) {
            setFiber( Integer.valueOf( def.getFiber() ) );
        }
        if ( null != def.getProtein() ) {
            setProtein( Integer.valueOf( def.getProtein() ) );
        }

    }

    /**
     * The date as milliseconds since epoch of this DiaryEntry
     */
    @NotNull
    private Long     date;

    /**
     * The type of the meal for this DiaryEntry
     */
    @NotNull
    @Enumerated ( EnumType.STRING )
    private MealType mealType;

    /**
     * The food for this DiaryEntry
     */
    @Length ( max = 50 )
    private String   food;

    /**
     * The amount of servings for this DiaryEntry
     */
    @NotNull
    private Integer  servings;

    /**
     * The amount of calories for this DiaryEntry
     */
    @NotNull
    private Integer  calories;

    /**
     * The amount of fat for this DiaryEntry
     */
    @NotNull
    private Integer  fat;

    /**
     * The amount of sodium for this DiaryEntry
     */
    @NotNull
    private Integer  sodium;

    /**
     * The amount of carbs for this DiaryEntry
     */
    @NotNull
    private Integer  carbs;

    /**
     * The amount of sugars for this DiaryEntry
     */
    @NotNull
    private Integer  sugars;

    /**
     * The amount of fiber for this DiaryEntry
     */
    @NotNull
    private Integer  fiber;

    /**
     * The amount of protein for this DiaryEntry
     */
    @NotNull
    private Integer  protein;

    /**
     * The username of the patient for this DiaryEntry
     */
    @Length ( max = 20 )
    private String   patient;

    /**
     * The id of this DiaryEntry
     */
    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long     id;

    /**
     * Gets the date for this DiaryEntry
     *
     * @return the date as milliseconds since epoch
     */
    public Long getDate () {
        return date;
    }

    /**
     * Sets the date for this DiaryEntry
     *
     * @param date2
     *            .getTimeInMillis() the date as milliseconds since epoch to set
     */
    public void setDate ( final Long date2 ) {
        if ( date2 > Calendar.getInstance().getTimeInMillis() ) {
            throw new IllegalArgumentException( "Date must be before current date" );
        }
        this.date = date2;
    }

    /**
     * Gets the meal type for this DiaryEntry
     *
     * @return the mealType
     */
    public MealType getMealType () {
        return mealType;
    }

    /**
     * Sets the meal type for this DiaryEntry
     *
     * @param mealType
     *            the mealType to set
     */
    public void setMealType ( final MealType mealType ) {
        this.mealType = mealType;
    }

    /**
     * Gets the food for this DiaryEntry
     *
     * @return the food
     */
    public String getFood () {
        return food;
    }

    /**
     * Sets the food for this DiaryEntry
     *
     * @param food
     *            the food to set
     */
    public void setFood ( final String food ) {
        this.food = food;
    }

    /**
     * Gets the servings for this DiaryEntry
     *
     * @return the servings
     */
    public Integer getServings () {
        return servings;
    }

    /**
     * Sets the servings for this DiaryEntry
     *
     * @param servings
     *            the servings to set
     */
    public void setServings ( final Integer servings ) {
        if ( servings < 1 ) {
            throw new IllegalArgumentException( "Servings must be a positive integer!" );
        }
        this.servings = servings;
    }

    /**
     * Gets the calories for this DiaryEntry
     *
     * @return the calories
     */
    public Integer getCalories () {
        return calories;
    }

    /**
     * Sets the calories for this DiaryEntry
     *
     * @param calories
     *            the calories to set
     */
    public void setCalories ( final Integer calories ) {
        if ( calories < 0 ) {
            throw new IllegalArgumentException( "Calories must be a non-negative integer!" );
        }
        this.calories = calories;
    }

    /**
     * Gets the fat for this DiaryEntry
     *
     * @return the fat
     */
    public Integer getFat () {
        return fat;
    }

    /**
     * Sets the fat for this DiaryEntry
     *
     * @param fat
     *            the fat to set
     */
    public void setFat ( final Integer fat ) {
        if ( fat < 0 ) {
            throw new IllegalArgumentException( "Fat must be a non-negative integer!" );
        }
        this.fat = fat;
    }

    /**
     * Gets the sodium for this DiaryEntry
     *
     * @return the sodium
     */
    public Integer getSodium () {
        return sodium;
    }

    /**
     * Sets the sodium for this DiaryEntry
     *
     * @param sodium
     *            the sodium to set
     */
    public void setSodium ( final Integer sodium ) {
        if ( sodium < 0 ) {
            throw new IllegalArgumentException( "Sodium must be a non-negative integer!" );
        }
        this.sodium = sodium;
    }

    /**
     * Gets the carbs for this DiaryEntry
     *
     * @return the carbs
     */
    public Integer getCarbs () {
        return carbs;
    }

    /**
     * Sets the carbs for this DiaryEntry
     *
     * @param carbs
     *            the carbs to set
     */
    public void setCarbs ( final Integer carbs ) {
        if ( carbs < 0 ) {
            throw new IllegalArgumentException( "Carbs must be a non-negative integer!" );
        }
        this.carbs = carbs;
    }

    /**
     * Gets the sugars for this DiaryEntry
     *
     * @return the sugars
     */
    public Integer getSugars () {
        return sugars;
    }

    /**
     * Sets the sugars for this DiaryEntry
     *
     * @param sugars
     *            the sugars to set
     */
    public void setSugars ( final Integer sugars ) {
        if ( sugars < 0 ) {
            throw new IllegalArgumentException( "Sugars must be a non-negative integer!" );
        }
        this.sugars = sugars;
    }

    /**
     * Gets the fiber for this DiaryEntry
     *
     * @return the fiber
     */
    public Integer getFiber () {
        return fiber;
    }

    /**
     * Sets the fiber for this DiaryEntry
     *
     * @param fiber
     *            the fiber to set
     */
    public void setFiber ( final Integer fiber ) {
        if ( fiber < 0 ) {
            throw new IllegalArgumentException( "Fiber must be a non-negative integer!" );
        }
        this.fiber = fiber;
    }

    /**
     * Gets the protein for this DiaryEntry
     *
     * @return the protein
     */
    public Integer getProtein () {
        return protein;
    }

    /**
     * Sets the protein for this DiaryEntry
     *
     * @param protein
     *            the protein to set
     */
    public void setProtein ( final Integer protein ) {
        if ( protein < 0 ) {
            throw new IllegalArgumentException( "Protein must be a non-negative integer!" );
        }
        this.protein = protein;
    }

    /**
     * Gets the patient for this DiaryEntry
     *
     * @return the patient
     */
    public String getPatient () {
        return patient;
    }

    /**
     * Sets the patient for this DiaryEntry
     *
     * @param patient
     *            the patient to set
     */
    public void setPatient ( final String patient ) {
        this.patient = patient;
    }

    /**
     * Get the ID of this DiaryEntry
     *
     * @return the ID of this DiaryEntry
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the id of this DiaryEntry
     *
     * @param id
     *            the id to set
     */
    public void setId ( final Long id ) {
        this.id = id;
    }
}
