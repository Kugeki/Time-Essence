package com.example.timeessence;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MotivationTextRepository {

    public static String get(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> motivationTexts = pref.getStringSet("MOTIVATION_TEXTS",
                new HashSet<>(Arrays.asList(context.getResources().getStringArray(R.array.motivation_texts))));

        if (motivationTexts.size() == 0) {
            motivationTexts = new HashSet<>(Arrays.asList(context.getResources().getStringArray(R.array.motivation_texts)));
        }

        String[] arr = motivationTexts.toArray(new String[motivationTexts.size()]);

        int i = (new Random()).nextInt(arr.length);
        String s = arr[i];
        motivationTexts.remove(arr[i]);

        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet("MOTIVATION_TEXTS", motivationTexts);
        editor.apply();

        return s;
    }
}