package dmanzur.xplaces.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dmanzur.xplaces.MainActivity;
import dmanzur.xplaces.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AuthenticationFragment extends Fragment {


    public AuthenticationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_authentication, container, false);

        Button registrationBtn = fragmentView.findViewById(R.id.authRegisterBtn);
        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrationFragment rf = new RegistrationFragment();
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,rf).addToBackStack(null).commit();
            }
        });

        Button loginBtn = fragmentView.findViewById(R.id.authLoginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginFragment lf = new LoginFragment();
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container,lf).addToBackStack(null).commit();
            }
        });

        return fragmentView;
    }

}
