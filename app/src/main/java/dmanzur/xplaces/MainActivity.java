package dmanzur.xplaces;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import dmanzur.xplaces.Model.services.PlacesService;
import dmanzur.xplaces.Model.services.UserService;
import dmanzur.xplaces.view.AuthenticationFragment;
import dmanzur.xplaces.view.FavoritesListFragment;
import dmanzur.xplaces.view.FindFragment;
import dmanzur.xplaces.view.LoginFragment;
import dmanzur.xplaces.view.LogoutAndAboutFragment;
import dmanzur.xplaces.view.MapFragment;
import dmanzur.xplaces.view.PlaceDetailsFragment;
import dmanzur.xplaces.view.PlacesListFragment;
import dmanzur.xplaces.view.RegistrationFragment;

public class MainActivity extends AppCompatActivity implements
        LoginFragment.OnUserLoggedInListener,
        LogoutAndAboutFragment.onUserLoggedOutListener,
        FindFragment.OnImagePlaceFoundListener,
        PlacesListFragment.OnPlacePickedListener,
        PlaceDetailsFragment.OnMapClickListener,
        FavoritesListFragment.OnFavoritePlacePickedListener,
        RegistrationFragment.OnRegistrationSucceededListener {

    public static FragmentManager fragmentManager;


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                //go home
                PlacesListFragment plf = new PlacesListFragment();
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,plf).commit();
                return true;
            case R.id.action_favorites:
                //go favorites
                FavoritesListFragment flf = new FavoritesListFragment();
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,flf).commit();
                return true;
            case R.id.action_find:
                //go find
                FindFragment ff = new FindFragment();
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,ff).commit();
                return true;
            case R.id.action_settings:
                //go settings
                LogoutAndAboutFragment laa = new LogoutAndAboutFragment();
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,laa).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState!=null){
            return;
        }


        fragmentManager = getSupportFragmentManager();

        final String email = UserService.getInstance().getUserEmail();

        if (email != null){
            Log.d("tag","email is not null");
            final String password = UserService.getInstance().getUserPassword();
            UserService.getInstance().loginUser(email, password, new ILoginListener() {
                @Override
                public void onComplete(boolean isLogged) {
                    if (isLogged){
                        Log.d("tag", "user is logged in");
                        onUserLoggedIn(false);
                    } else {
                        //add authentication fragment
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("tag", "user is not logged in");
                                AuthenticationFragment af = new AuthenticationFragment();
                                MainActivity.fragmentManager.beginTransaction().add(R.id.fragment_container, af).commit();
                                getSupportActionBar().hide();
                            }
                        });
                    }
                }
            });

        } else {
            //add authentication fragment
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("tag", "email is null");
                    AuthenticationFragment af = new AuthenticationFragment();
                    MainActivity.fragmentManager.beginTransaction().add(R.id.fragment_container, af).commit();
                    getSupportActionBar().hide();
                }
            });
        }
    }

    @Override
    public void onUserLoggedIn(final boolean isFromFragment) {

        PlacesService.getInstance().getPlacesList(new IInitPlacesListListener(){
            @Override
            public void onComplete() {
                Log.d("tag","completed prepare places");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PlacesService.getInstance().releasePlaces();
                        PlacesListFragment plf = new PlacesListFragment();
                        if(isFromFragment) {
                            MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, plf).commit();
                            getSupportActionBar().show();
                        }
                        else {
                            MainActivity.fragmentManager.beginTransaction().add(R.id.fragment_container,plf).commit();
                        }
                    }
                });
            }

            @Override
            public void onError(String msg) {
                Log.d("tag",msg);
            }
        });
    }



    @Override
    public void onUserLoggedOut() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UserService.getInstance().removeUserEmailAndPassword();
                AuthenticationFragment af = new AuthenticationFragment();
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,af).commit();
                getSupportActionBar().hide();
            }
        });
    }

    @Override
    public void onImagePlaceFoundListener(final String placeId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PlaceDetailsFragment af = new PlaceDetailsFragment();
                Bundle args = new Bundle();
                args.putString("placeId", placeId);
                af.setArguments(args);
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,af).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public void onPlacePicked(final String placeId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PlaceDetailsFragment af = new PlaceDetailsFragment();
                Bundle args = new Bundle();
                args.putString("placeId", placeId);
                af.setArguments(args);
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,af).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onMapClick(final String placeId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MapFragment mf = new MapFragment();
                Bundle args = new Bundle();
                args.putString("placeId", placeId);
                mf.setArguments(args);
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,mf).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onFavoritePlacePicked(final String placeId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PlaceDetailsFragment af = new PlaceDetailsFragment();
                Bundle args = new Bundle();
                args.putString("placeId", placeId);
                af.setArguments(args);
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,af).commit();
            }
        });
    }

    @Override
    public void onRegistrationSucceeded() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginFragment lf = new LoginFragment();
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container, lf).commit();
                getSupportActionBar().hide();
            }
        });
    }


    public interface IRegistrationListener {
        void onComplete(boolean isRegistered);
    }



    public interface IInitPlacesListListener {
        void onComplete();
        void onError(String msg);
    }

    public interface ILoginListener {
        void onComplete(boolean isLogged);
    }

}
