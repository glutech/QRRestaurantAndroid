package cn.com.zdez.qrrestaurant.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;

import java.util.prefs.Preferences;

/**
 * Created by LuoHanLin on 13-12-13.
 */
public class PreferencesSpec {
    public static void setPreference(SharedPreferences prefs, Editor editor, Resources r, int key, int value){
        editor.putString(r.getString(key), Integer.toString(value));
    }

    public static void setPreference(SharedPreferences prefs, Editor editor, String key, int value){
        editor.putString(key, Integer.toString(value));
    }

    public static void setPreference(SharedPreferences prefs, Editor editor, Resources r, int key, boolean value){
        editor.putBoolean(r.getString(key), value);
    }

    public static void setPreference(SharedPreferences prefs, Editor editor, String key, boolean value){
        editor.putBoolean(key, value);
    }

    public static void setPreference(SharedPreferences prefs, Editor editor, Resources r, int key, String value){
        editor.putString(r.getString(key), value);
    }

    public static void setPreference(SharedPreferences prefs, Editor editor, String key, String value){
        editor.putString(key, value);
    }

    public static void setPreferencesDefaults(){
    }

}
