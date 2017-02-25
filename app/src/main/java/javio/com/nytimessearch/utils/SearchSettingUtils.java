package javio.com.nytimessearch.utils;

import android.content.SharedPreferences;

import javio.com.nytimessearch.models.SearchSetting;

/**
 * Created by javiosyc on 2017/2/25.
 */

public class SearchSettingUtils {
    private static final String BEGIN_DATE = "beginDate";
    private static final String SORT_ORDER = "sortOrder";
    private static final String IS_ARTS = "isArts";
    private static final String IS_FASHION = "isFashion";
    private static final String IS_SPORTS = "isSports";

    private SearchSettingUtils() {

    }

    public static SearchSetting populateSearchSetting(SharedPreferences sharedPreferences, SearchSetting searchSetting) {
        if (searchSetting == null)
            return null;

        String beginDate = sharedPreferences.getString(BEGIN_DATE, "");
        String sortOrder = sharedPreferences.getString(SORT_ORDER, "oldest");
        boolean isArts = sharedPreferences.getBoolean(IS_ARTS, false);
        boolean isFashion = sharedPreferences.getBoolean(IS_FASHION, false);
        boolean isSports = sharedPreferences.getBoolean(IS_SPORTS, false);

        setSettings(searchSetting, beginDate, sortOrder, isArts, isFashion, isSports);
        return searchSetting;
    }

    private static void setSettings(SearchSetting searchSetting, String beginDate, String sortOrder, boolean isArts, boolean isFashion, boolean isSports) {
        searchSetting.setBeginDate(beginDate);
        searchSetting.setSortOrder(sortOrder);
        searchSetting.setArts(isArts);
        searchSetting.setFashion(isFashion);
        searchSetting.setSports(isSports);
    }

    public static SearchSetting saveSearchSetting(SharedPreferences sharedPreferences, SearchSetting searchSetting, SearchSetting newValues) {
        setSettings(searchSetting, newValues.getBeginDate(), newValues.getSortOrder(), newValues.isArts(), newValues.isFashion(), newValues.isSports());

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(BEGIN_DATE, searchSetting.getBeginDate());
        editor.putString(SORT_ORDER, searchSetting.getSortOrder());
        editor.putBoolean(IS_ARTS, searchSetting.isArts());
        editor.putBoolean(IS_FASHION, searchSetting.isFashion());
        editor.putBoolean(IS_SPORTS, searchSetting.isSports());

        editor.apply();

        return searchSetting;
    }
}
