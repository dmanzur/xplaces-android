package dmanzur.xplaces.view;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dmanzur.xplaces.MainActivity;
import dmanzur.xplaces.Model.services.PlacesService;
import dmanzur.xplaces.MyApplication;
import dmanzur.xplaces.R;
import dmanzur.xplaces.entities.AppPlace;

public class PlacesListRecyclerAdapter extends RecyclerView.Adapter<PlacesListRecyclerAdapter.PlacesListCellHolder> {

    private List<AppPlace> places;
    private PlacesListFragment.PlacesListRecyclerViewCellClickListener recyclerViewCellClickListener;

    public PlacesListRecyclerAdapter(PlacesListFragment.PlacesListRecyclerViewCellClickListener recyclerViewCellClick) {
        this.recyclerViewCellClickListener = recyclerViewCellClick;
        this.places = PlacesService.getInstance().getPlaces();
    }

    @NonNull
    @Override
    public PlacesListCellHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_list_cell, parent, false);

        view.setOnClickListener(recyclerViewCellClickListener);

        return new PlacesListCellHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final PlacesListCellHolder holder, int position) {

        AppPlace place = this.places.get(position);
        String fullName = place.getName() + ", " + place.getCountry();
        holder.placeName.setText(fullName);
        holder.placeDescription.setText(place.getDescription());

        PlacesService.getInstance().getPlacePhoto(place.getPlaceId(), 0, new AppPlace.IPlacePhotoListener() {
            @Override
            public void onComplete(Bitmap photo) {
                holder.placeImage.setImageBitmap(photo);
            }

            @Override
            public void onError(String msg) {
                holder.placeImage.setImageDrawable(MyApplication.getAppContext().getResources().getDrawable(R.drawable.app_icon));

            }
        });

    }

    @Override
    public int getItemCount() {
        return this.places.size();
    }

    public AppPlace getItemClicked(int position) {
        return this.places.get(position);
    }

    public static class PlacesListCellHolder extends RecyclerView.ViewHolder {

        TextView placeName;
        ImageView placeImage;
        TextView placeDescription;

        public PlacesListCellHolder(View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.placeListCellName);
            placeImage = itemView.findViewById(R.id.placeListCellImage);
            placeDescription = itemView.findViewById(R.id.placeListCellDescription);
        }
    }
}
