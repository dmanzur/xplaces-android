package dmanzur.xplaces.Model.local;


import android.graphics.Bitmap;

import java.util.List;

import dmanzur.xplaces.Model.local.sqlite.SQLiteManager;
import dmanzur.xplaces.entities.AppPlace;

public class LocalManager {


    private static LocalManager localManagerInstance;

    private SQLiteManager sqLiteManager = new SQLiteManager();
    private InternalStorage internalStorage = new InternalStorage();
    private SharedPreferencesConfig sharedPreferencesConfig = new SharedPreferencesConfig();


    private LocalManager() { }

    public static LocalManager getInstance() {
        if (localManagerInstance == null) {
            localManagerInstance = new LocalManager();
        }
        return localManagerInstance;
    }

    public void addImageToPlaceInternalStorage(String placeId, Bitmap img, String imgId) {
        this.internalStorage.savePlaceImageToInternalStorage(img, placeId, imgId);
    }

    public void deletePlaceFromInternalStorage(String placeId) {
        this.internalStorage.deletePlaceImages(placeId);
    }

    public Bitmap getPlaceImageFromInternalStorage(String placeId, String imageId) {
        return this.internalStorage.readImageFromFile(imageId, placeId);
    }


    public void addPlaceToDatabase(AppPlace place) {
        this.sqLiteManager.addPlaceToDatabase(place);
    }


    public AppPlace getPlaceFromDatabase(String placeId) {
        return this.sqLiteManager.getPlaceFromDatabase(placeId);
    }

    public List<AppPlace> getPlaces() {
        return this.sqLiteManager.getPlacesFromDatabase();
    }

    public String getUserEmail() {
        return this.sharedPreferencesConfig.getUserEmail();
    }

    public String getUserPassword() {
        return this.sharedPreferencesConfig.getUserPassword();
    }

    public void setUserEmailAndPassword(String email, String password) {
        this.sharedPreferencesConfig.setUserEmailAndPassword(email, password);
    }

    public void removeUserEmailAndPassword(){
        this.sharedPreferencesConfig.removeUserEmailAndPassword();
    }

      public List<AppPlace> getFavoritesPlaces() {
        return this.sqLiteManager.getFavoritesPlaces();
    }

    public void markPlaceAsFavorite(String placeId){
        this.sqLiteManager.markPlaceAsFavorite(placeId);
    }

    public void unMarkPlaceAsFavorite(String placeId){
        this.sqLiteManager.unMarkPlaceAsFavorite(placeId);
    }

}
