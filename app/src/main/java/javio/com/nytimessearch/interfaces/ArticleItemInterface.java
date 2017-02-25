package javio.com.nytimessearch.interfaces;

import java.util.ArrayList;

import javio.com.nytimessearch.models.Article;

/**
 * Created by javiosyc on 2017/2/25.
 */

public interface ArticleItemInterface {
    public void showErrorMessage(String message);

    public void clear();

    public void addAllItem(ArrayList<Article> articles);

    public void notifyDataSetChanged();
}
