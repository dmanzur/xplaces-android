package dmanzur.xplaces.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dmanzur.xplaces.MainActivity;
import dmanzur.xplaces.Model.services.PlacesService;
import dmanzur.xplaces.MyApplication;
import dmanzur.xplaces.R;
import dmanzur.xplaces.entities.AppPlace;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceDetailsFragment extends Fragment {

    private AppPlace place;
    private OnMapClickListener onMapClickListener;
    public ImageAdapter imageAdapter;

    public PlaceDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View fragmentView =  inflater.inflate(R.layout.fragment_place_details, container, false);

        final String placeId = getArguments().getString("placeId");
        this.place = PlacesService.getInstance().getPlace(placeId);

        if (this.place != null) {

            TextView name = fragmentView.findViewById(R.id.nameTextView);
            name.setText(this.place.getName());

            TextView country = fragmentView.findViewById(R.id.countryTextView);
            country.setText(this.place.getCountry());

            TextView description = fragmentView.findViewById(R.id.descriptionTextView);
            description.setText(this.place.getDescription());

            PlacesService.getInstance().getPlacePhotos(this.place.getPlaceId(), new IPlacePhotos() {
                @Override
                public void onComplete(List<Bitmap> photos) {
                    ViewPager viewPager = fragmentView.findViewById(R.id.viewPager);
                    imageAdapter = new ImageAdapter(MyApplication.getAppContext(),photos);
                    viewPager.setAdapter(imageAdapter);
                }
            });

            ImageButton shareBtn = fragmentView.findViewById(R.id.shareBtn);
            shareBtn.setBackgroundResource(R.drawable.ic_share_black_24dp);

            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/jpg");
                    String imagePath = getPath();
                    Log.d("tag",imagePath);
                    File f = new File(imagePath);
                    Uri myUri = Uri.fromFile(f);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, myUri);
                    startActivity(Intent.createChooser(shareIntent,"Share via..."));
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("tag", "create image file exception");
                }
                }
            });

            ImageButton mapBtn = fragmentView.findViewById(R.id.mapBtn);
            mapBtn.setBackgroundResource(R.drawable.ic_map_black_24dp);
            mapBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onMapClickListener.onMapClick(placeId);
                }
            });

            final ImageButton favoriteBtn = fragmentView.findViewById(R.id.favoriteBtn);
            if(place.getIsFavorite() == 0){
                favoriteBtn.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
            } else {
                favoriteBtn.setBackgroundResource(R.drawable.ic_star_black_24dp);
            }
            favoriteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(place.getIsFavorite() == 0){
                        PlacesService.getInstance().markPlaceAsFavorite(placeId);
                        favoriteBtn.setBackgroundResource(R.drawable.ic_star_black_24dp);

                    } else {
                        PlacesService.getInstance().unMarkPlaceAsFavorite(placeId);
                        favoriteBtn.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
                    }
                }
            });
        }

        return fragmentView;
    }

    private String getPath(){
        File mFile1 = Environment.getExternalStorageDirectory();

        String fileName ="img1.jpg";

        File mFile2 = new File(mFile1,fileName);
        try {
            FileOutputStream outStream;
            outStream = new FileOutputStream(mFile2);
            Bitmap imageToShare = imageAdapter.getPresentedImage();
            imageToShare.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Log.d("tag","FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.d("tag","IOException");
            e.printStackTrace();
        }
        String sdPath = mFile1.getAbsolutePath().toString()+"/"+fileName;
        File temp=new File(sdPath);
        if(!temp.exists()){
            Log.d("tag","no image file at location :"+sdPath);
        }
        Log.d("tag","temp file: "+ temp.toString());
        return temp.getAbsolutePath();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            onMapClickListener = (OnMapClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement onMapClickListener");
        }
    }

    public interface OnMapClickListener {
        void onMapClick(String placeId);
    }

    public interface IPlacePhotos {
        void onComplete(List<Bitmap> photos);
    }

}
