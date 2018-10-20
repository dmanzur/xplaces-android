package dmanzur.xplaces.Model.services;


import dmanzur.xplaces.MainActivity;
import dmanzur.xplaces.Model.local.LocalManager;
import dmanzur.xplaces.Model.network.NetworkManager;
import dmanzur.xplaces.view.LogoutAndAboutFragment;

public class UserService {

    private static UserService userServiceInstance;

    private UserService(){}

    public static UserService getInstance(){
        if (userServiceInstance == null){
            userServiceInstance = new UserService();
        }

        return userServiceInstance;
    }

    public void loginUser(String email, String password, MainActivity.ILoginListener callback){
        NetworkManager.getInstance().loginUser(email,password,callback);
    }

    public void registerUser(String name, String email, String password, MainActivity.IRegistrationListener callback){
        NetworkManager.getInstance().registerUser(name,email,password,callback);
    }

    public String getUserEmail(){
        return LocalManager.getInstance().getUserEmail();
    }

    public String getUserPassword(){
        return LocalManager.getInstance().getUserPassword();
    }

    public void setUserEmailAndPassword(String email,String password){
        LocalManager.getInstance().setUserEmailAndPassword(email,password);
    }

    public void logoutUser(LogoutAndAboutFragment.ILogout callback){
        NetworkManager.getInstance().logoutUser(callback);
    }

    public void removeUserEmailAndPassword(){
        LocalManager.getInstance().removeUserEmailAndPassword();
    }
}
