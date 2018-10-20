package dmanzur.xplaces.view;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.util.List;

import dmanzur.xplaces.MainActivity;
import dmanzur.xplaces.Model.services.PlacesService;
import dmanzur.xplaces.MyApplication;
import dmanzur.xplaces.R;
import dmanzur.xplaces.entities.AppPlace;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesListFragment extends Fragment {

    private static View fragmentView;
    private RecyclerView recyclerView;
    private PlacesListRecyclerAdapter placesListRecyclerAdapter;
    private OnPlacePickedListener onPlacePickedListener;

    public PlacesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        if (fragmentView != null) {
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null)
                parent.removeView(fragmentView);
        }
        try {
            fragmentView = inflater.inflate(R.layout.fragment_places_list, container, false);

            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(final Place place) {
                    // TODO: Get info about the selected place.
                    Log.d("tag", "Place: " + place.getName());
                    PlacesService.getInstance().initPlace(place, new InitPlaceDetails() {
                        @Override
                        public void onComplete() {
                            onPlacePickedListener.onPlacePicked(place.getId());
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(MyApplication.getAppContext(), msg,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.d("tag", "An error occurred: " + status);
                }
            });

            this.recyclerView = fragmentView.findViewById(R.id.placesListRecyclerView);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MyApplication.getAppContext());
            this.recyclerView.setLayoutManager(mLayoutManager);
            PlacesListRecyclerViewCellClickListener recyclerViewCellClick = new PlacesListRecyclerViewCellClickListener();
            this.placesListRecyclerAdapter = new PlacesListRecyclerAdapter(recyclerViewCellClick);
            this.recyclerView.setAdapter(placesListRecyclerAdapter);
            this.recyclerView.setHasFixedSize(true);
            return fragmentView;
        } catch (InflateException e) {
            return fragmentView;
        }
    }

    public class PlacesListRecyclerViewCellClickListener implements View.OnClickListener {

        @Override
        public void onClick(final View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            final AppPlace place = placesListRecyclerAdapter.getItemClicked(itemPosition);
            onPlacePickedListener.onPlacePicked(place.getPlaceId());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            onPlacePickedListener = (OnPlacePickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement onUserLoggedInListener");
        }
    }

    public interface OnPlacePickedListener {
        void onPlacePicked(String placeId);
    }


    public interface InitPlaceDetails {
        void onComplete();
        void onError(String msg);
    }

}
