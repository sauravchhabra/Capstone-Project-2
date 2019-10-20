package com.sauravchhabra.foodme.widget;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.sauravchhabra.foodme.R;
import com.sauravchhabra.foodme.data.local.FoodMeDatabase;
import com.sauravchhabra.foodme.data.local.dao.FavRestaurantDao;
import com.sauravchhabra.foodme.data.local.entity.CommonRestaurant;
import com.sauravchhabra.foodme.util.AppExecutorsUtils;

import java.util.ArrayList;
import java.util.List;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new FoodMeRemoteViewFactory(getApplicationContext());
    }

    public class FoodMeRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

        FavRestaurantDao dao;
        AppExecutorsUtils executors = new AppExecutorsUtils();

        private Context context;
        private List<CommonRestaurant> favoritesList = new ArrayList();

        public FoodMeRemoteViewFactory(Context context) {
            this.context = context;
            dao = Room.databaseBuilder(context, FoodMeDatabase.class, "foodme.db").build().favRestaurantDao();
        }

        @Override
        public void onCreate() {
            executors.diskIO().execute(() -> favoritesList = dao.getAllFavsForWidget());
        }

        @Override
        public void onDataSetChanged() {
            executors.diskIO().execute(() -> favoritesList = dao.getAllFavsForWidget());
        }

        @Override
        public void onDestroy() {
            dao = null;
        }

        @Override
        public int getCount() {
            return (favoritesList != null) ? favoritesList.size() : 0;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            String restaurantName = favoritesList.get(i).getName();
            String address = favoritesList.get(i).getAddress();
            String imgUrl = favoritesList.get(i).getFeaturedImage();

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.foodme_app_widget_item);

            remoteViews.setTextViewText(R.id.widget_name, restaurantName);
            remoteViews.setTextViewText(R.id.widget_short_adress, address);

            Intent fillIntent = new Intent();
            remoteViews.setOnClickFillInIntent(R.id.widget_wrapper, fillIntent);

            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
