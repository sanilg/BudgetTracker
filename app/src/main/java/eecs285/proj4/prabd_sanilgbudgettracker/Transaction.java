package eecs285.proj4.prabd_sanilgbudgettracker;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String time;
    private String date;
    private String item;
    private String category;
    private Double cost;

    public Transaction(String time, String date, String item, String category, Double cost) {
        this.time = time;
        this.date = date;
        this.item = item;
        this.category = category;
        this.cost = cost;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getItem() {
        return item;
    }

    public String getCategory() {
        return category;
    }

    public Double getCost() {
        return cost;
    }
}
