package dmanzur.xplaces.entities;

import android.graphics.Bitmap;

public class AppPlace {

    private  String placeId;
    private  String name;
    private  String description;
    private  double longitude;
    private  double latitude;
    private  String country;
    private  int isFavorite;

    public AppPlace(String placeId,String name,String description,double longitude,double latitude,String country,int favorite){
        this.placeId = placeId;
        this.name = name;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
        this.country = country;
        this.isFavorite = favorite;
    }

    public AppPlace(){

    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public String getPlaceId(){
        return this.placeId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getCountry() {
        return country;
    }


    public interface IPlacePhotoListener {
        void onComplete(Bitmap photo);
        void onError(String msg);
    }

}
