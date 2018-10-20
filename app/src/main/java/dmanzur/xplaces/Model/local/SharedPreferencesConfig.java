package dmanzur.xplaces.Model.local;


import android.content.Context;
import android.content.SharedPreferences;

import dmanzur.xplaces.MyApplication;

public class SharedPreferencesConfig {

    private SharedPreferences sharedPreferences;

    public SharedPreferencesConfig(){
        this.sharedPreferences = MyApplication.getAppContext().getSharedPreferences("userDetails",Context.MODE_PRIVATE);
    }

    public String getUserEmail(){
        return this.sharedPreferences.getString("email",null);
    }

    public String getUserPassword(){
        return this.sharedPreferences.getString("password",null);
    }

    public void setUserEmailAndPassword(String email,String password){
        SharedPreferences.Editor editor= this.sharedPreferences.edit();
        editor.putString("email",email);
        editor.putString("password",password);
        editor.apply();
    }

    public void removeUserEmailAndPassword(){
        this.sharedPreferences.edit().clear().apply();
    }
}
