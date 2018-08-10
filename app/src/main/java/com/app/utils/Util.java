package com.app.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Nirmal Dhara on 12-07-2015.
 */
public class Util {
    //Util.getProperty("name",getApplicationContext()) read method
    public static String getProperty(String key,Context context) throws IOException {
        Properties properties = new Properties();;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("config.properties");
        properties.load(inputStream);
        return properties.getProperty(key);

    }
    public static String getDayFormat(int day, String month)
    {
        switch(day)
        {
            case 1: return ""+day+" st "+month;
            case 2: return ""+day+" nd "+month;
            case 3: return ""+day+" rd "+month;
            default: return ""+day+"th "+month;
        }


    }
    public static String encodeTobase64(Bitmap image, String ext) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(ext.equalsIgnoreCase("png"))
        {
            immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        }
        if(ext.equalsIgnoreCase("jpeg"))
        {
            immage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        if(ext.equalsIgnoreCase("jpg"))
        {
            immage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}