package javio.com.nytimessearch.models;

import java.io.Serializable;

/**
 * Created by javiosyc on 2017/2/25.
 */

public class SearchSetting implements Serializable {
    private String beginDate;
    private String sortOrder;
    private boolean isArts;
    private boolean isFashion;
    private boolean isSports;

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isArts() {
        return isArts;
    }

    public void setArts(boolean arts) {
        isArts = arts;
    }

    public boolean isFashion() {
        return isFashion;
    }

    public void setFashion(boolean fashion) {
        isFashion = fashion;
    }

    public boolean isSports() {
        return isSports;
    }

    public void setSports(boolean sports) {
        isSports = sports;
    }


    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }
}
