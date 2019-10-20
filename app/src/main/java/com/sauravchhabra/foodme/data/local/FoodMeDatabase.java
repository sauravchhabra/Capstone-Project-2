package com.sauravchhabra.foodme.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.sauravchhabra.foodme.data.local.dao.FavRestaurantDao;
import com.sauravchhabra.foodme.data.local.entity.CommonRestaurant;

@Database(entities = {CommonRestaurant.class}, version = 1, exportSchema = false)
public abstract class FoodMeDatabase extends RoomDatabase {

    public abstract FavRestaurantDao favRestaurantDao();
}
