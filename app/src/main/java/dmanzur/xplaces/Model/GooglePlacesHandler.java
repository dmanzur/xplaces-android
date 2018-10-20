package dmanzur.xplaces.Model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import dmanzur.xplaces.Model.services.PlacesService;
import dmanzur.xplaces.MyApplication;
import dmanzur.xplaces.entities.AppPlace;

public class GooglePlacesHandler {

    private final GeoDataClient mGeoDataClient = Places.getGeoDataClient(MyApplication.getAppContext());
    private List<PlaceBufferResponse> placesToRealese = new ArrayList<>();

    public void getPlaceById(String placeId, final PlacesService.IGooglePlace callback){
        this.mGeoDataClient.getPlaceById(placeId).addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                if (task.isSuccessful()) {
                    final PlaceBufferResponse places = task.getResult();
                    placesToRealese.add(places);
                    final Place myPlace = places.get(0);
                    Log.d("tag", "Place found: " + myPlace.getName());
                    callback.onComplete(myPlace);
                } else {
                    Log.d("tag", "Place not found.");
                    callback.onError("Place not found.");
                }
            }
        });
    }

    public void releasePlaces(){
        for (PlaceBufferResponse places : placesToRealese){
            places.release();
        }
        placesToRealese.clear();
    }

    public void getPlacePhotosMetadataBuffer(String placeId, final PlacesService.IPlacePhotos callback) {

        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = this.mGeoDataClient.getPlacePhotos(placeId);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                callback.onComplete(photoMetadataBuffer);
            }
        });
    }

    public void getPlacePhotoByMethadata(PlacePhotoMetadata photoMetadata,final int index, final PlacesService.IPlacePhoto callback){

        // Get a full-size bitmap for the photo.
        Task<PlacePhotoResponse> photoResponse = this.mGeoDataClient.getPhoto(photoMetadata);
        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                PlacePhotoResponse photo = task.getResult();
                Bitmap bitmap = photo.getBitmap();
                callback.onComplete(bitmap,index);
            }
        });
    }

    public static AppPlace toAppPlace(Place place, String placeDescription){

        String placeId = place.getId();
        Double latitude =  place.getLatLng().latitude;
        Double longitude =  place.getLatLng().longitude;
        String name  = place.getName().toString();
        String address = place.getAddress().toString();
        String country = address.substring(address.lastIndexOf(" ")+1);
        return new AppPlace(placeId,name,placeDescription,longitude,latitude,country,0);

    }
}
