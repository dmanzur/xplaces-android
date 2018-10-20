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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dmanzur.xplaces.MainActivity;
import dmanzur.xplaces.Model.services.UserService;
import dmanzur.xplaces.MyApplication;
import dmanzur.xplaces.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {


    private OnRegistrationSucceededListener onRegistrationSucceededListener;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(R.layout.fragment_registration, container, false);

        Button registrationBtn = fragmentView.findViewById(R.id.registrationRegisterBtn);
        registrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameEditText = fragmentView.findViewById(R.id.registerNameEditText);
                String name = nameEditText.getText().toString();

                EditText emailEditText = fragmentView.findViewById(R.id.registerEmailEditText);
                String email = emailEditText.getText().toString();

                EditText firstPasswordEditText = fragmentView.findViewById(R.id.registerFirstPasswordEditText);
                String firstPassword = firstPasswordEditText.getText().toString();

                EditText secondPasswordEditText = fragmentView.findViewById(R.id.registerSecondPasswordEditText);
                String secondPassword = secondPasswordEditText.getText().toString();

                if (isEmailValid(email) && firstPassword.equals(secondPassword)){
                    registerUser(name,email,firstPassword);
                } else {
                    Log.d("tag","register fragment One of the inputs are incorrect");
                    Toast.makeText(MyApplication.getAppContext(), "One of the inputs are incorrect",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        return fragmentView;
    }

    private void registerUser(String name, String email, String password){

        UserService.getInstance().registerUser(name, email, password, new MainActivity.IRegistrationListener() {
            @Override
            public void onComplete(boolean isRegistered) {
                if (isRegistered){
                    onRegistrationSucceededListener.onRegistrationSucceeded();
                } else {
                    Log.d("tag","register fragment One of the inputs are incorrect");
                    Toast.makeText(MyApplication.getAppContext(), "One of the inputs are incorrect",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            onRegistrationSucceededListener = (OnRegistrationSucceededListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement onMapClickListener");
        }
    }

    public interface OnRegistrationSucceededListener {
        void onRegistrationSucceeded();
    }

}
