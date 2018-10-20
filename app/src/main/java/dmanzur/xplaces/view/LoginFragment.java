package dmanzur.xplaces.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dmanzur.xplaces.MainActivity;
import dmanzur.xplaces.Model.services.UserService;
import dmanzur.xplaces.MyApplication;
import dmanzur.xplaces.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    OnUserLoggedInListener userLoggedInListener;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentView = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginBtn = fragmentView.findViewById(R.id.loginLoginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText emailEditText = fragmentView.findViewById(R.id.loginEmailEditText);
                final String email = emailEditText.getText().toString();
                EditText passwordEditText = fragmentView.findViewById(R.id.loginPasswordEditText);
                final String password = passwordEditText.getText().toString();
                UserService.getInstance().loginUser(email, password, new MainActivity.ILoginListener() {
                    @Override
                    public void onComplete(boolean isLogged) {
                        if (isLogged) {
                            UserService.getInstance().setUserEmailAndPassword(email, password);
                            userLoggedInListener.onUserLoggedIn(true);
                        } else {
                            Toast.makeText(MyApplication.getAppContext(), "Wrong email or password",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            userLoggedInListener = (OnUserLoggedInListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement onUserLoggedInListener");
        }
    }

    public interface OnUserLoggedInListener {
        void onUserLoggedIn(boolean isFromFragment);
    }
}
