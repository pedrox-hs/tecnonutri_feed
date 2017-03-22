package br.com.pedrosilva.tecnonutri.data.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psilva on 3/16/17.
 */

public class Food {

    @SerializedName("description")
    private String description;

    @SerializedName("measure")
    private String measure;

    @SerializedName("amount")
    private Double amount;

    @SerializedName("weight")
    private Double weight;

    @SerializedName("energy")
    private Double energy;

    @SerializedName("carbohydrate")
    private Double carbohydrate;

    @SerializedName("fat")
    private Double fat;

    @SerializedName("protein")
    private Double protein;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getEnergy() {
        return energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public Double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(Double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }
}
