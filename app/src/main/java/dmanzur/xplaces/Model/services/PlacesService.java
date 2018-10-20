package dmanzur.xplaces.Model.services;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;


import java.util.ArrayList;
import java.util.List;

import dmanzur.xplaces.MainActivity;
import dmanzur.xplaces.Model.GooglePlacesHandler;
import dmanzur.xplaces.Model.local.LocalManager;
import dmanzur.xplaces.Model.network.NetworkManager;
import dmanzur.xplaces.entities.AppPlace;
import dmanzur.xplaces.view.FindFragment;
import dmanzur.xplaces.view.PlaceDetailsFragment;
import dmanzur.xplaces.view.PlacesListFragment;

public class PlacesService {

    private GooglePlacesHandler googlePlacesHandler = new GooglePlacesHandler();
    private static PlacesService placesServiceInstance;

    private PlacesService() {
    }

    public static PlacesService getInstance() {
        if (placesServiceInstance == null) {
            placesServiceInstance = new PlacesService();
        }

        return placesServiceInstance;
    }


    public List<AppPlace> getPlaces() {
        return LocalManager.getInstance().getPlaces();
    }

    public void getPlacePhoto(final String placeId, final int imageId, AppPlace.IPlacePhotoListener callback) {
        Bitmap photo = LocalManager.getInstance().getPlaceImageFromInternalStorage(placeId, String.valueOf(imageId));
        if (photo != null)
            callback.onComplete(photo);
        else {
            callback.onError("error get image");
        }
    }

    public AppPlace getPlace(String placeId) {
        return LocalManager.getInstance().getPlaceFromDatabase(placeId);
    }

    public void findImage(String imageUriPath, final FindFragment.IPlaceSearch callback) {

        NetworkManager.getInstance().findImage(imageUriPath, new IPlacesServiceSearch() {
            @Override
            public void onComplete(String placeId) {
                callback.complete(placeId);
            }

            @Override
            public void onError(String msg) {
                callback.onError();
            }
        });
    }

    public void initPlace(final Place googlePlace,final PlacesListFragment.InitPlaceDetails callback){

        this.initPlacePhotos(googlePlace.getId(), new IDownloadPlacePhotos() {
            @Override
            public void onComplete() {
                NetworkManager.getInstance().getPlaceDescription(googlePlace.getName().toString(), new IPlaceDescription() {
                    @Override
                    public void onComplete(String placeDescription) {
                        AppPlace appPlace = GooglePlacesHandler.toAppPlace(googlePlace, placeDescription);
                        LocalManager.getInstance().addPlaceToDatabase(appPlace);
                        callback.onComplete();
                    }

                    @Override
                    public void onError(String msg) {
                        callback.onError(msg);
                    }
                });
            }
        });

    }

    private void getPlaceById(String placeId,final int index, final IAppPlace callback) {

        AppPlace localPlace = LocalManager.getInstance().getPlaceFromDatabase(placeId);
        if (localPlace != null) {
            callback.onComplete(index);
        } else {
            this.googlePlacesHandler.getPlaceById(placeId, new IGooglePlace() {
                @Override
                public void onComplete(final Place googlePlace) {
                    initPlace(googlePlace, new PlacesListFragment.InitPlaceDetails() {
                        @Override
                        public void onComplete() {
                            callback.onComplete(index);
                        }

                        @Override
                        public void onError(String msg) {
                            callback.onError(msg);
                        }
                    });
                }

                @Override
                public void onError(String msg) {
                    callback.onError(msg);
                }
            });
        }
    }

    private void initAppPlaces(List<String> placesListIds, final MainActivity.IInitPlacesListListener callback) {

        final int placesIdsListSize = placesListIds.size();

        for (final String placeId : placesListIds) {
            this.getPlaceById(placeId,placesListIds.indexOf(placeId), new IAppPlace() {
                @Override
                public void onComplete(final int index) {
                    if (placesIdsListSize == index+1) {
                        callback.onComplete();
                    }
                }

                @Override
                public void onError(String msg) {
                    callback.onError(msg);
                }
            });
        }
    }

    public void releasePlaces() {
        googlePlacesHandler.releasePlaces();
    }

    public void getPlacesList(final MainActivity.IInitPlacesListListener callback) {

        NetworkManager.getInstance().getPlacesListIDs(new IPlacesListIdsListener() {
            @Override
            public void onComplete(List<String> placesListIds) {
                initAppPlaces(placesListIds, callback);
            }

            @Override
            public void onError(String msg) {
                Log.d("tag", msg);
                callback.onError(msg);
            }
        });
    }

    public void initPlacePhotos(final String placeId, final IDownloadPlacePhotos callback) {
        this.googlePlacesHandler.getPlacePhotosMetadataBuffer(placeId, new IPlacePhotos() {
            @Override
            public void onComplete(PlacePhotoMetadataBuffer placePhotoMetadataBuffer) {

                final int photosCount = placePhotoMetadataBuffer.getCount();
                for (int i = 0; i < photosCount; i++) {
                    final String imageId = String.valueOf(i);
                    final Bitmap img = LocalManager.getInstance().getPlaceImageFromInternalStorage(placeId, imageId);
                    if (img == null) {
                        final PlacePhotoMetadata placePhotoMetadata = placePhotoMetadataBuffer.get(i);
                        googlePlacesHandler.getPlacePhotoByMethadata(placePhotoMetadata, i, new IPlacePhoto() {
                            @Override
                            public void onComplete(Bitmap photo, int index) {
                                LocalManager.getInstance().addImageToPlaceInternalStorage(placeId, photo, imageId);
                                if (index + 1 == photosCount) {
                                    callback.onComplete();
                                }
                            }
                        });
                    } else {
                        if (i + 1 == photosCount) {
                            callback.onComplete();
                        }
                    }
                }
            }
        });
    }

    public void getPlacePhotos(final String placeId, final PlaceDetailsFragment.IPlacePhotos callback) {

        final List<Bitmap> placePhotos = new ArrayList<>();
        int imageId = 0;
        Bitmap img = LocalManager.getInstance().getPlaceImageFromInternalStorage(placeId, String.valueOf(imageId));
        while (img !=null){
            placePhotos.add(img);
            imageId++;
            img = LocalManager.getInstance().getPlaceImageFromInternalStorage(placeId, String.valueOf(imageId));
        }
        callback.onComplete(placePhotos);

    }

    public List<AppPlace> getFavoritesPlaces() {
        return LocalManager.getInstance().getFavoritesPlaces();
    }

    public void markPlaceAsFavorite(String placeId){
        LocalManager.getInstance().markPlaceAsFavorite(placeId);
    }

    public void unMarkPlaceAsFavorite(String placeId){
        LocalManager.getInstance().unMarkPlaceAsFavorite(placeId);
    }



    public interface IPlacePhotos {
        void onComplete(PlacePhotoMetadataBuffer placePhotoMetadataBuffer);
    }

    public interface IPlacePhoto {
        void onComplete(Bitmap photo, int index);
    }

    public interface IAppPlace {
        void onComplete(final int index);

        void onError(String msg);
    }

    public interface IGooglePlace {
        void onComplete(Place googlePlace);

        void onError(String msg);
    }

    public interface IPlacesListIdsListener {
        void onComplete(List<String> placesListIds);

        void onError(String msg);
    }

    public interface IPlaceDescription {
        void onComplete(String placeDescription);

        void onError(String msg);
    }


    public interface IPlacesServiceSearch {
        void onComplete(String placeId);

        void onError(String msg);
    }

    public interface IDownloadPlacePhotos {
        void onComplete();
    }
}
