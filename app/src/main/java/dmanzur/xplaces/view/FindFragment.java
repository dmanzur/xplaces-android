package dmanzur.xplaces.view;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dmanzur.xplaces.MainActivity;
import dmanzur.xplaces.Model.services.PlacesService;
import dmanzur.xplaces.MyApplication;
import dmanzur.xplaces.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends Fragment {


    private Uri imageUri;
    private ImageView imageView;
    private Bitmap imageToFind = null;
    private Button findImageBtn;

    private OnImagePlaceFoundListener imagePlaceFoundListener;


    public FindFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_find, container, false);

        Button cameraBtn = fragmentView.findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            200);
                } else {
                    takePhoto();
                }
            }
        });

        Button galleryBtn = fragmentView.findViewById(R.id.galleryBtn);
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            400);
                } else {
                    openGallery();
                }
            }
        });

        imageView = fragmentView.findViewById(R.id.imageToFind);

        findImageBtn = fragmentView.findViewById(R.id.findImageBtn);
        findImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start progress bar
                String imagePath = getPath();
                PlacesService.getInstance().findImage(imagePath, new IPlaceSearch() {
                    @Override
                    public void complete(String placeId) {
                        Log.d("tag","complete find");
                        imagePlaceFoundListener.onImagePlaceFoundListener(placeId);
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getActivity(), "Place not found...", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        });

        return fragmentView;
    }


    private String getPath(){
        File mFile1 = Environment.getExternalStorageDirectory();

        String fileName ="img1.jpg";

        File mFile2 = new File(mFile1,fileName);
        try {
            FileOutputStream outStream;
            outStream = new FileOutputStream(mFile2);
            imageToFind.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
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

    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    private void takePhoto() {

        try {

            File photo = null;
            photo = createImageFile();
            String authorize = MyApplication.getAppContext().getPackageName() + ".fileprovider";
            imageUri = FileProvider.getUriForFile(MyApplication.getAppContext(), authorize, photo);

            if (ContextCompat.checkSelfPermission(MyApplication.getAppContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        300);
            } else {
                openCamera();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("tag", "create image file exception");
        }
    }

    private void openGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        getActivity().startActivityFromFragment(this, chooserIntent, 50);
    }

    private void openCamera() {

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        getActivity().startActivityFromFragment(this, intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                Log.d("tag", "activity 100 result");
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getActivity().getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getActivity().getContentResolver();
                    Bitmap bitmap;
                    try {
                        Log.d("tag", "try to get selected image");

                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);
                        Log.d("tag", "got selected image");
                        imageView.setImageBitmap(bitmap);
                        imageToFind = bitmap;
                        findImageBtn.setEnabled(true);

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_LONG)
                                .show();
                        Log.d("tag", "Failed to load image from camera");
                    }
                }
                return;
            case 50:
                Log.d("tag", "activity 50 result");
                if (resultCode == Activity.RESULT_OK && data.getData() != null) {
                    try {
                        Log.d("tag", "try to get selected image");
                        Uri targetUri = data.getData();
                        Bitmap image = BitmapFactory.decodeStream(MyApplication.getAppContext().getContentResolver().openInputStream(targetUri));
                        Log.d("tag", "got selected image");
                        imageView.setImageBitmap(image);
                        imageToFind = image;
                        imageUri = targetUri;
                        findImageBtn.setEnabled(true);

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_LONG)
                                .show();
                        Log.d("tag", "Failed to load image from gallery");
                    }
                }
                return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("tag", "permission given for write external storage");
                    takePhoto();
                } else {
                    Toast.makeText(getActivity(), "Permission denied to read your External storage", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case 300: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("tag", "permission given for camera");
                    openCamera();
                } else {
                    Toast.makeText(getActivity(), "Permission denied to Camera", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case 400: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("tag", "permission given for Gallery");
                    openGallery();
                } else {
                    Toast.makeText(getActivity(), "Permission denied to Gallery", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            imagePlaceFoundListener = (OnImagePlaceFoundListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement onUserLoggedInListener");
        }
    }

    public interface OnImagePlaceFoundListener {
        void onImagePlaceFoundListener(String placeId);
    }

    public interface IPlaceSearch {
        void complete(String placeId);

        void onError();
    }

}
