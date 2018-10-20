package dmanzur.xplaces.Model.local.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import dmanzur.xplaces.entities.AppPlace;

public class PlaceDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "place_db";
    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_TABLE = "create table " +
            PlaceContract.PlaceEntry.TABLE_NAME + "(" +
            PlaceContract.PlaceEntry.PLACE_ID + " TEXT," +
            PlaceContract.PlaceEntry.NAME + " TEXT," +
            PlaceContract.PlaceEntry.COUNTRY + " TEXT," +
            PlaceContract.PlaceEntry.LONGITUDE + " NUMBER," +
            PlaceContract.PlaceEntry.LATITUDE + " NUMBER," +
            PlaceContract.PlaceEntry.FAVORITE + " INTEGER," +
            PlaceContract.PlaceEntry.DESCRIPTION + " TEXT);";

    public static final String DROP_TABLE = "drop table if exists " + PlaceContract.PlaceEntry.TABLE_NAME;

    public PlaceDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        Log.d("tag","database created...");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        Log.d("tag","Table created...");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        Log.d("tag","Table dropped...");
        onCreate(sqLiteDatabase);
    }

    public void addPlace(AppPlace place,SQLiteDatabase database){

        ContentValues contentValues= new ContentValues();
        contentValues.put(PlaceContract.PlaceEntry.PLACE_ID,place.getPlaceId());
        contentValues.put(PlaceContract.PlaceEntry.NAME,place.getName());
        contentValues.put(PlaceContract.PlaceEntry.COUNTRY,place.getCountry());
        contentValues.put(PlaceContract.PlaceEntry.LONGITUDE,place.getLongitude());
        contentValues.put(PlaceContract.PlaceEntry.LATITUDE,place.getLatitude());
        contentValues.put(PlaceContract.PlaceEntry.FAVORITE,0);
        contentValues.put(PlaceContract.PlaceEntry.DESCRIPTION,place.getDescription());

        database.insert(PlaceContract.PlaceEntry.TABLE_NAME,null,contentValues);

        Log.d("tag","Place inserted to database...");

    }

    public void addPlacesList(List<AppPlace> places, SQLiteDatabase database){

        for (AppPlace place:places) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PlaceContract.PlaceEntry.PLACE_ID, place.getPlaceId());
            contentValues.put(PlaceContract.PlaceEntry.NAME, place.getName());
            contentValues.put(PlaceContract.PlaceEntry.COUNTRY, place.getCountry());
            contentValues.put(PlaceContract.PlaceEntry.LONGITUDE, place.getLongitude());
            contentValues.put(PlaceContract.PlaceEntry.LATITUDE, place.getLatitude());
            contentValues.put(PlaceContract.PlaceEntry.DESCRIPTION, place.getDescription());
            contentValues.put(PlaceContract.PlaceEntry.FAVORITE,0);
            database.insert(PlaceContract.PlaceEntry.TABLE_NAME, null, contentValues);
            Log.d("tag","Place inserted to database...");
        }


    }

    public Cursor getPlace(String placeId,SQLiteDatabase database){
        String[] projections = {
                PlaceContract.PlaceEntry.PLACE_ID,
                PlaceContract.PlaceEntry.NAME,
                PlaceContract.PlaceEntry.COUNTRY,
                PlaceContract.PlaceEntry.LONGITUDE,
                PlaceContract.PlaceEntry.LATITUDE,
                PlaceContract.PlaceEntry.DESCRIPTION,
                PlaceContract.PlaceEntry.FAVORITE

        };

        String selection = PlaceContract.PlaceEntry.PLACE_ID + " = ?";
        String[] selectionArgs = {placeId};
        Cursor cursor = null;
        try {
            cursor = database.query(PlaceContract.PlaceEntry.TABLE_NAME, projections, selection, selectionArgs, null, null, null);

        } catch (Exception e){
            Log.d("tag","placeId not found in database");
            Log.d("tag",e.getMessage());
        }
       return cursor;
    }

    public Cursor getPlaces(SQLiteDatabase database){
        String[] projections = {
                PlaceContract.PlaceEntry.PLACE_ID,
                PlaceContract.PlaceEntry.NAME,
                PlaceContract.PlaceEntry.COUNTRY,
                PlaceContract.PlaceEntry.LONGITUDE,
                PlaceContract.PlaceEntry.LATITUDE,
                PlaceContract.PlaceEntry.FAVORITE,
                PlaceContract.PlaceEntry.DESCRIPTION};


        return database.query(PlaceContract.PlaceEntry.TABLE_NAME,projections,null,null,null,null,null);

    }

    public Cursor getFavoritesPlaces(SQLiteDatabase database){
        String[] projections = {
                PlaceContract.PlaceEntry.PLACE_ID,
                PlaceContract.PlaceEntry.NAME,
                PlaceContract.PlaceEntry.COUNTRY,
                PlaceContract.PlaceEntry.LONGITUDE,
                PlaceContract.PlaceEntry.LATITUDE,
                PlaceContract.PlaceEntry.FAVORITE,
                PlaceContract.PlaceEntry.DESCRIPTION};

        String selection = PlaceContract.PlaceEntry.FAVORITE + " = ?";
        String[] selectionArgs = {"1"};
        Cursor cursor = null;
        try {
            cursor = database.query(PlaceContract.PlaceEntry.TABLE_NAME, projections, selection, selectionArgs, null, null, null);

        } catch (Exception e){
            Log.d("tag","FAVORITES places not found in database");
            Log.d("tag",e.getMessage());
        }

        return cursor;
    }


    public void markPlaceAsFavorite(SQLiteDatabase database,String placeId){
        ContentValues contentValues= new ContentValues();
        contentValues.put(PlaceContract.PlaceEntry.FAVORITE,1);

        String selection = PlaceContract.PlaceEntry.PLACE_ID + " = ?";
        String[] selectionArgs = {placeId};

        database.update(PlaceContract.PlaceEntry.TABLE_NAME,contentValues,selection,selectionArgs);

        Log.d("tag","Place marked as favorite in database...");
    }

    public void unMarkPlaceAsFavorite(SQLiteDatabase database,String placeId){
        ContentValues contentValues= new ContentValues();
        contentValues.put(PlaceContract.PlaceEntry.FAVORITE,0);

        String selection = PlaceContract.PlaceEntry.PLACE_ID + " = ?";
        String[] selectionArgs = {placeId};

        database.update(PlaceContract.PlaceEntry.TABLE_NAME,contentValues,selection,selectionArgs);

        Log.d("tag","Place unmarked as favorite in database...");
    }
}
