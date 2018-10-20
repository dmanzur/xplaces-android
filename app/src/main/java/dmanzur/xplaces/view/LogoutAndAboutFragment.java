package dmanzur.xplaces.view;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dmanzur.xplaces.Model.services.UserService;
import dmanzur.xplaces.MyApplication;
import dmanzur.xplaces.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogoutAndAboutFragment extends Fragment {



    onUserLoggedOutListener userLoggedOutListener;
    private String aboutText =
            "won the match for Vader by using a cattle prod on Cactus, " +
                    "knocking him out. The level of violence involved in this feud caused WCW to refuse to book " +
                    "Cactus Jack against Vader on a pay-per-view again. On March 16, 1994, during a WCW European tour," +
                    " Foley and Big Van Vader had one of" +
                    " the most infamous matches in wrestling history in Munich," +
                    " Germany. Foley began a hangman—a planned move where a wrestler's head is tangled between" +
                    " the top two ring ropes. Neither wrestler was aware that the ring ropes had been drawn extra " +
                    "tight before the event, and Foley was barely able to move. When Foley finally freed himself from the " +
                    "ropes and fell out of the ring, his ears were badly split at the back. When Foley re-entered the ring " +
                    " Foley and Big Van Vader had one of" +
                    " the most infamous matches in wrestling history in Munich," +
                    " Germany. Foley began a hangman—a planned move where a wrestler's head is tangled between" +
                    " the top two ring ropes. Neither wrestler was aware that the ring ropes had been drawn extra " +
                    "tight before the event, and Foley was barely able to move. When Foley finally freed himself from the " +
                    "ropes and fell out of the ring, his ears were badly split at the back. When Foley re-entered the ring " +
                    "the two wrestlers began trading blows. During this time, Vader reached up and grabbed Foley's ear, and " +
                    "ripped it off. The two men continued wrestling as the referee picked up the ear and gave it to the ring announcer." +
                    " Vader claimed for years after that the ear had come off during " +
                    "the botched Hangman maneuver, however in a WWE Network video, Vader admits that after seeing " ;


    public LogoutAndAboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_logout_and_about, container, false);


        TextView aboutTextView = fragmentView.findViewById(R.id.aboutTextView);
        aboutTextView.setText(this.aboutText);

        Button logoutBtn = fragmentView.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserService.getInstance().logoutUser(new ILogout() {
                    @Override
                    public void onComplete(boolean value) {
                        if (value){
                            userLoggedOutListener.onUserLoggedOut();
                        } else {
                            Toast.makeText(MyApplication.getAppContext(), "Cant Logout",
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
            userLoggedOutListener = (onUserLoggedOutListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement onUserLoggedOutListener");
        }
    }


    public interface ILogout {
        void onComplete(boolean value);
    }

    public interface onUserLoggedOutListener {
        void onUserLoggedOut();
    }

}
