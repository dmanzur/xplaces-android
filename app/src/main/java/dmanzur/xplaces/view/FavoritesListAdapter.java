package dmanzur.xplaces.view;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import dmanzur.xplaces.MainActivity;
import dmanzur.xplaces.Model.services.PlacesService;
import dmanzur.xplaces.MyApplication;
import dmanzur.xplaces.R;
import dmanzur.xplaces.entities.AppPlace;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.FavoritesListCellHolder>{

    private List<AppPlace> favoritesPlaces;
    private FavoritesListFragment.FavoritesListRecyclerViewCellClickListener recyclerViewCellClickListener;

    public FavoritesListAdapter(FavoritesListFragment.FavoritesListRecyclerViewCellClickListener recyclerViewCellClick) {
        this.recyclerViewCellClickListener = recyclerViewCellClick;
        this.favoritesPlaces = PlacesService.getInstance().getFavoritesPlaces();
    }

    @NonNull
    @Override
    public FavoritesListCellHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_list_cell, parent, false);

        view.setOnClickListener(recyclerViewCellClickListener);

        return new FavoritesListCellHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final FavoritesListCellHolder holder, int position) {

        AppPlace place = this.favoritesPlaces.get(position);
        Log.d("tag",place.getName());
        holder.placeName.setText(place.getName());
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
        return this.favoritesPlaces.size();
    }

    public AppPlace getItemClicked(int position){
        return this.favoritesPlaces.get(position);
    }

    public static class FavoritesListCellHolder extends RecyclerView.ViewHolder  {

        TextView placeName;
        ImageView placeImage;

        public FavoritesListCellHolder(View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.favoritesListCellName);
            placeImage = itemView.findViewById(R.id.favoritesListCellImage);
        }
    }
}
