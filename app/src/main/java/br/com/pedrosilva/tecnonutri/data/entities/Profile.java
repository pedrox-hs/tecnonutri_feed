package br.com.pedrosilva.tecnonutri.data.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by psilva on 3/16/17.
 */

public class Profile {

    @SerializedName("id")
    private int id;

    @SerializedName("image")
    private String imageUrl;

    @SerializedName("name")
    private String name;

    @SerializedName("general_goal")
    private String generalGoal;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGeneralGoal() {
        return generalGoal;
    }

    public void setGeneralGoal(String generalGoal) {
        this.generalGoal = generalGoal;
    }
}
