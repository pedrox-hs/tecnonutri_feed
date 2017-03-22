package br.com.pedrosilva.tecnonutri.domain.entities;

import android.support.annotation.Nullable;

/**
 * Created by psilva on 3/16/17.
 */

public class Profile {

    private int id;
    @Nullable
    private String imageUrl;
    @Nullable
    private String name;
    @Nullable
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
