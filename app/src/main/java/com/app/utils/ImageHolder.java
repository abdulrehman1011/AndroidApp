package com.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.HashMap;
import java.util.List;

public class ImageHolder {
    private static ImageHolder instance;
    public static HashMap<String,Bitmap> imagelList = new HashMap<>();
    private static String headerColor = "#FF0000";
    private static String menuItemBgColor = "#FF0000";
    private static String menuTextColor = "#FF0000";
    private static String menuMainBackgroundColor = "#FF0000";
    private static String statusbarColor = "#FF0000";
    private static String redBtnUrl = "";
    private static String logoUrl = "";
    private  static String sideMennuLogoBackgroundColor = "";

    public static String getHeaderTextColor() {
        return headerTextColor;
    }

    public static void setHeaderTextColor(String headerTextColor) {
        ImageHolder.headerTextColor = headerTextColor;
    }

    private  static String headerTextColor = "";

    static Bitmap bit;
    public static void addBitmap(Bitmap bitmap, String key)
    {

        imagelList.put(key,bitmap);
       /* Picasso.get()
                .load(url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                        *//* Save the bitmap or do something with it here *//*

                        //Set it in the ImageView
                        if(bitmap != null)
                        {
                            imagelList.put(key,bitmap);
                        }

                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });*/


    }
    public static Bitmap getBitmap(String key)
    {
        bit = imagelList.get(key);

        return imagelList.get(key);
    }

    public static String getHeaderColor()
    {
        return headerColor;
    }
    public static void setHeaderColor(String color)
    {
        headerColor = color;
    }

    public static String getMenuItemBgColor() {
        return menuItemBgColor;
    }

    public static void setMenuItemBgColor(String menuItemBgColor) {
        ImageHolder.menuItemBgColor = menuItemBgColor;
    }

    public static String getMenuTextColor() {
        return menuTextColor;
    }

    public static void setMenuTextColor(String menuTextColor) {
        ImageHolder.menuTextColor = menuTextColor;
    }

    public static String getMenuMainBackgroundColor() {
        return menuMainBackgroundColor;
    }

    public static void setMenuMainBackgroundColor(String menuMainBackgroundColor) {
        ImageHolder.menuMainBackgroundColor = menuMainBackgroundColor;
    }

    public static String getStatusbarColor() {
        return statusbarColor;
    }

    public static void setStatusbarColor(String statusbarColor) {
        ImageHolder.statusbarColor = statusbarColor;
    }

    public static String getRedBtnUrl() {
        return redBtnUrl;
    }

    public static void setRedBtnUrl(String redBtnUrl) {
        ImageHolder.redBtnUrl = redBtnUrl;
    }

    public static String getLogoUrl() {
        return logoUrl;
    }

    public static void setLogoUrl(String logoUrl) {
        ImageHolder.logoUrl = logoUrl;
    }

    public static String getSideMennuLogoBackgroundColor() {
        return sideMennuLogoBackgroundColor;
    }

    public static void setSideMennuLogoBackgroundColor(String sideMennuLogoBackgroundColor) {
        ImageHolder.sideMennuLogoBackgroundColor = sideMennuLogoBackgroundColor;
    }
}
