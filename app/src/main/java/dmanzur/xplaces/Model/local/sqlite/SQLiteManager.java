package dmanzur.xplaces.Model.local.sqlite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dmanzur.xplaces.MyApplication;
import dmanzur.xplaces.entities.AppPlace;

public class SQLiteManager {

    private PlaceDBHelper placeDBHelper = new PlaceDBHelper(MyApplication.getAppContext());



    public void addPlaceToDatabase(AppPlace place) {
        SQLiteDatabase database = placeDBHelper.getWritableDatabase();
        this.placeDBHelper.addPlace(place, database);
        database.close();
    }

    public AppPlace getPlaceFromDatabase(String placeId) {

        SQLiteDatabase database = placeDBHelper.getReadableDatabase();
        Cursor cursor = this.placeDBHelper.getPlace(placeId, database);
        AppPlace appPlace = null;
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            String id = cursor.getString(cursor.getColumnIndex(PlaceContract.PlaceEntry.PLACE_ID));
            String name = cursor.getString(cursor.getColumnIndex(PlaceContract.PlaceEntry.NAME));
            String country = cursor.getString(cursor.getColumnIndex(PlaceContract.PlaceEntry.COUNTRY));
            double longitude = cursor.getDouble(cursor.getColumnIndex(PlaceContract.PlaceEntry.LONGITUDE));
            double latitude = cursor.getDouble(cursor.getColumnIndex(PlaceContract.PlaceEntry.LATITUDE));
            int favorite = cursor.getInt(cursor.getColumnIndex(PlaceContract.PlaceEntry.FAVORITE));
            String description = cursor.getString(cursor.getColumnIndex(PlaceContract.PlaceEntry.DESCRIPTION));

            appPlace = new AppPlace(id, name, description, longitude, latitude, country,favorite);
            Log.d("tag","got place from database...");
        }
        database.close();
        return appPlace;

    }

    public List<AppPlace> getPlacesFromDatabase(){

        List<AppPlace> places = new ArrayList<>();
        SQLiteDatabase database = placeDBHelper.getReadableDatabase();
        Cursor cursor = this.placeDBHelper.getPlaces(database);

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(PlaceContract.PlaceEntry.PLACE_ID));
            String name = cursor.getString(cursor.getColumnIndex(PlaceContract.PlaceEntry.NAME));
            String country = cursor.getString(cursor.getColumnIndex(PlaceContract.PlaceEntry.COUNTRY));
            double longitude = cursor.getDouble(cursor.getColumnIndex(PlaceContract.PlaceEntry.LONGITUDE));
            double latitude = cursor.getDouble(cursor.getColumnIndex(PlaceContract.PlaceEntry.LATITUDE));
            int favorite = cursor.getInt(cursor.getColumnIndex(PlaceContract.PlaceEntry.FAVORITE));
            String description = cursor.getString(cursor.getColumnIndex(PlaceContract.PlaceEntry.DESCRIPTION));

            AppPlace appPlace = new AppPlace(id, name, description, longitude, latitude, country,favorite);
            places.add(appPlace);
            Log.d("tag","got place from database...");
        }
        database.close();
        Log.d("tag","got all places from database...");
        return places;
    }


    public List<AppPlace> getFavoritesPlaces() {

        List<AppPlace> places = new ArrayList<>();
        SQLiteDatabase database = placeDBHelper.getReadableDatabase();
        Cursor cursor = this.placeDBHelper.getFavoritesPlaces(database);

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(PlaceContract.PlaceEntry.PLACE_ID));
            String name = cursor.getString(cursor.getColumnIndex(PlaceContract.PlaceEntry.NAME));
            String country = cursor.getString(cursor.getColumnIndex(PlaceContract.PlaceEntry.COUNTRY));
            double longitude = cursor.getDouble(cursor.getColumnIndex(PlaceContract.PlaceEntry.LONGITUDE));
            double latitude = cursor.getDouble(cursor.getColumnIndex(PlaceContract.PlaceEntry.LATITUDE));
            int favorite = cursor.getInt(cursor.getColumnIndex(PlaceContract.PlaceEntry.FAVORITE));
            String description = cursor.getString(cursor.getColumnIndex(PlaceContract.PlaceEntry.DESCRIPTION));

            AppPlace appPlace = new AppPlace(id, name, description, longitude, latitude, country,favorite);
            places.add(appPlace);
            Log.d("tag","got place from database...");
        }
        database.close();
        Log.d("tag","got all favorites places from database...");
        return places;
    }


    public void markPlaceAsFavorite(String placeId){
        SQLiteDatabase database = placeDBHelper.getWritableDatabase();
        this.placeDBHelper.markPlaceAsFavorite(database,placeId);
        database.close();
    }

    public void unMarkPlaceAsFavorite(String placeId){
        SQLiteDatabase database = placeDBHelper.getWritableDatabase();
        this.placeDBHelper.unMarkPlaceAsFavorite(database,placeId);
        database.close();
    }
}
