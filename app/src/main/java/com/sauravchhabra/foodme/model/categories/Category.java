package com.sauravchhabra.foodme.model.categories;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("categories")
    private Categories categories;

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }
}
