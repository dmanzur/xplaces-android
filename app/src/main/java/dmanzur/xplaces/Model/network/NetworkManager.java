package dmanzur.xplaces.Model.network;


import android.graphics.Bitmap;

import dmanzur.xplaces.MainActivity;
import dmanzur.xplaces.Model.services.PlacesService;
import dmanzur.xplaces.view.LogoutAndAboutFragment;

public class NetworkManager {

    private static NetworkManager networkManagerInstance;

    private NetworkManager(){}
    private UserHandler userHandler = new UserHandler();
    private PlacesHandler placesHandler = new PlacesHandler();

    public static NetworkManager getInstance(){
        if (networkManagerInstance == null){
            networkManagerInstance = new NetworkManager();
        }

        return networkManagerInstance;
    }


    public void loginUser(String email, String password, MainActivity.ILoginListener callback){
        this.userHandler.loginUser(email,password,callback);
    }

    public void registerUser(String name, String email, String password, MainActivity.IRegistrationListener callback){
        this.userHandler.registerUser(name,email,password,callback);
    }

    public void getPlacesListIDs(PlacesService.IPlacesListIdsListener callback){
        this.placesHandler.getPlacesListIDs(callback);
    }

    public void getPlaceDescription(String name, PlacesService.IPlaceDescription callback){
        this.placesHandler.getPlaceDescription(name,callback);
    }

    public void logoutUser(LogoutAndAboutFragment.ILogout callback){
        this.userHandler.logoutUser(callback);
    }

    public void findImage(String imageUriPath, PlacesService.IPlacesServiceSearch callback){

        this.placesHandler.findImage(imageUriPath,callback);
    }

}
