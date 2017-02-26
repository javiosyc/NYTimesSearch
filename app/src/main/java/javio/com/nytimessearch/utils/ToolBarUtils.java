package javio.com.nytimessearch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import javio.com.nytimessearch.R;
import javio.com.nytimessearch.models.SearchSetting;

/**
 * Created by javiosyc on 2017/2/26.
 */

public class ToolBarUtils {

    private static final int TOOL_BAR_LOGO = R.drawable.ic_book_24;

    private static final String SETTINGS_FILE = "Settings";

    private ToolBarUtils() {
    }

    public static void setUpToolbar(String title, AppCompatActivity appCompatActivity) {
        Toolbar toolbar = (Toolbar) appCompatActivity.findViewById(R.id.toolbar);

        toolbar.setLogo(TOOL_BAR_LOGO);

        toolbar.setTitle(title);

        toolbar.setBackgroundColor(ContextCompat.getColor(appCompatActivity, R.color.colorActionBar));

        appCompatActivity.setSupportActionBar(toolbar);
    }

    public static SearchSetting initSettings(SearchSetting searchSetting, AppCompatActivity appCompatActivity) {

        if (searchSetting == null) {
            searchSetting = new SearchSetting();
        }

        SharedPreferences mSettings = appCompatActivity.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE);
        SearchSettingUtils.populateSearchSetting(mSettings, searchSetting);

        return searchSetting;
    }
}
