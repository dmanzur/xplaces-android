package dmanzur.xplaces.Model.local;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import dmanzur.xplaces.MyApplication;


class InternalStorage {

    private final File imagesDirectory;

    public InternalStorage(){
        this.imagesDirectory = MyApplication.getAppContext().getDir("placesImages", Context.MODE_PRIVATE);
    }


    void deletePlaceImages(String placeIDToRemove) {

        File file = new File(this.imagesDirectory, placeIDToRemove);
        boolean deleted = file.delete();
        Log.d("tag", "InternalStorage : deletePlaceImages : if deleted: " + deleted);
    }

    void savePlaceImageToInternalStorage(Bitmap bitmapImage, String placeId, String imgid) {

        Log.d("tag", "InternalStorage :saveImageToInternalStorage: start");
        File placePath = new File(imagesDirectory, placeId);
        if (!placePath.exists())
            Log.d("tag","is dir created: " + placePath.mkdir());


        File imagePath = new File(placePath, imgid);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imagePath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            Log.d("tag", "InternalStorage :saveImageToInternalStorage: finish");
        } catch (Exception e) {
            Log.d("tag", "InternalStorage :saveImageToInternalStorage: error: " + e.toString());
            e.printStackTrace();
        }

    }

    Bitmap readImageFromFile(String imgid,String placeId) {

        if (imgid == null) {
            Log.d("tag", "internal storage: readImageFromFile : image id is null");
            return null;
        }
        try {
            Log.d("tag", "InternalStorage :readImageFromFile: start");
            File dirPath = new File(imagesDirectory,placeId);
            File imagePath = new File(dirPath,imgid);
            Bitmap img = BitmapFactory.decodeStream(new FileInputStream(imagePath));
            Log.d("tag", "InternalStorage :readImageFromFile: finish");
            return img;
        } catch (Exception e) {
            Log.d("tag", "InternalStorage :readImageFromFile: error : " + e.toString());

            e.printStackTrace();
            return null;
        }
    }

}