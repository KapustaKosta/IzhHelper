package ru.konstantin_starikov.samsung.izhhelper.models;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import ru.konstantin_starikov.samsung.izhhelper.R;

public final class Helper {
    private static final String TAG = "Helper";

    public static String getConfigValue(Context context, String name) {
        Resources resources = context.getResources();

        try {
            InputStream rawResource = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Failed to open config file.");
        }

        return null;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static Bitmap rotateImageIfRequired(Bitmap img) {

        if(img.getWidth() > img.getHeight()) return rotateImage(img, 90);
        else return img;
    }

    public static Bitmap rotateImage(Bitmap image, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        image.recycle();
        return rotatedImg;
    }

    @SuppressLint("LongLogTag")
    public static void deleteImageByPath(String imagePath) {
        Log.i("Image deleted, path was - ", imagePath);
        new File(imagePath).delete();
    }

    public static String saveAvatarFromBitmap(Bitmap avatar, String userAccountID, Context context)
    {
        String avatarPath = null;
        try {
            FileOutputStream fileOutputStream = null;
            avatarPath = String.format(context.getApplicationInfo().dataDir + File.separator + userAccountID + ".jpg");
            fileOutputStream = new FileOutputStream(avatarPath);
            avatar.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            if (fileOutputStream != null) fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return avatarPath;
    }

    public static String cropText(String inputText, int size)
    {
        String result;
        if(size <= inputText.length())
            result = inputText.substring(0, size);
        else result = inputText;
        return result;
    }

    public static String getFullPathFromDataDirectory(String fileName, Context context)
    {
        String ANDROID_DATA_DIR = context.getApplicationInfo().dataDir;
        return ANDROID_DATA_DIR + File.separatorChar + fileName;
    }

    public static Bitmap getBitmapFromPath(String imagePath) {

        File imgFile = new  File(imagePath);
        Bitmap result = null;

        if(imgFile.exists()) {
            result = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }

        return result;
    }
}