package eecs285.proj4.prabd_sanilgbudgettracker;

import java.io.Serializable;

public class Category implements Serializable, Comparable<Category> {
    private String name;
    private Double sum;

    /**
     *
     * @param name
     */
    Category(String name, Double val) {
        this.name = name;
        sum = val;
    }

    /**
     *
     * @return name of cat
     */
    String getName() {
        return name;
    }


    /**
     *
     * @return sum
     */
    Double getSum() {
        return sum;
    }

    /**
     * Adds value to sum
     * @param num number to be added
     */
    void addToSum(Double num){
        sum += num;
    }


    /**
     * Overrides compareTo for sorting purposes
     * @param other the second object of the same type
     * @return value of string comparison
     */
    @Override
    public int compareTo(Category other) {
        if(other == null){
            throw new NullPointerException("Null value");
        }
        return -1* other.getName().compareTo(name);
    }
}
