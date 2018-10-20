package dmanzur.xplaces.view;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dmanzur.xplaces.Model.services.PlacesService;
import dmanzur.xplaces.MyApplication;
import dmanzur.xplaces.R;
import dmanzur.xplaces.entities.AppPlace;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesListFragment extends Fragment {

    private static View fragmentView;
    private RecyclerView recyclerView;
   private FavoritesListAdapter favoritesListAdapter;
    private OnFavoritePlacePickedListener onFavoritePlacePickedListener;

    public FavoritesListFragment() {
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
            fragmentView = inflater.inflate(R.layout.fragment_favorites_list, container, false);

        this.recyclerView = fragmentView.findViewById(R.id.favoritesListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(MyApplication.getAppContext());
        this.recyclerView.setLayoutManager(mLayoutManager);
            FavoritesListRecyclerViewCellClickListener recyclerViewCellClick = new FavoritesListRecyclerViewCellClickListener();
        this.favoritesListAdapter = new FavoritesListAdapter(recyclerViewCellClick);
        this.recyclerView.setAdapter(favoritesListAdapter);
        this.recyclerView.setHasFixedSize(true);
        return fragmentView;
        } catch (InflateException e) {
            return fragmentView;
        }
    }

    public class FavoritesListRecyclerViewCellClickListener implements View.OnClickListener{

        @Override
        public void onClick(final View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
           final AppPlace place = favoritesListAdapter.getItemClicked(itemPosition);
            Log.d("tag","item clicked: " + place.getName());
            onFavoritePlacePickedListener.onFavoritePlacePicked(place.getPlaceId());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            onFavoritePlacePickedListener = (OnFavoritePlacePickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement onUserLoggedInListener");
        }
    }

    public interface OnFavoritePlacePickedListener {
        void onFavoritePlacePicked(String placeId);
    }



}
