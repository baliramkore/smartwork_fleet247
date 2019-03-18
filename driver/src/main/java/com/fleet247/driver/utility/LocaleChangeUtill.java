package com.fleet247.driver.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

public class LocaleChangeUtill {

    public static SharedPreferences localePref;

    public static void changeLocale(Context context, String language){
        Locale myLocale = new Locale(language);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (language.equals("en")) {
            Locale.setDefault(new Locale("en_US"));
            //conf.setLocale(Locale.getDefault());
            Log.d("LocaleLanguage","English "+GsonStringConvertor.gsonToString(Locale.getDefault()));
        }
        else {
            Locale.setDefault(new Locale("hi"));
            //conf.setLocale(myLocale);
            Log.d("LocaleLanguage","Hindi "+GsonStringConvertor.gsonToString(myLocale));
        }
        context=context.createConfigurationContext(conf);

        //res.updateConfiguration(conf, dm);
    }

    public static Context changeLocales(Context context, String language){
        Locale myLocale = new Locale(language);
        Configuration conf = context.getResources().getConfiguration();
        conf.setLocale(myLocale);
        context=context.createConfigurationContext(conf);

        //res.updateConfiguration(conf, dm);
        return context;
    }

    private static void createLocalePref(Context context){
        localePref=context.getSharedPreferences("localePref",Context.MODE_PRIVATE);
    }

    public static void setLanguage(Context context,String language){
        createLocalePref(context);
        localePref.edit().putString("prefLang",language).apply();

    }

    public static String getLanguage(Context context){
        createLocalePref(context);
        return localePref.getString("prefLang","en_US");


    }

}
