package javio.com.nytimessearch.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javio.com.nytimessearch.models.Article;
import javio.com.nytimessearch.models.Headline;
import javio.com.nytimessearch.models.Multimedia;

/**
 * Created by javiosyc on 2017/2/25.
 */

public class ArticleUtils {
    private static final String NT_TIMES_URL = "http://www.nytimes.com/";

    public static ArrayList<Article> fromJsonArray(JSONArray array) {
        ArrayList<Article> results = new ArrayList<>();

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(parseArticleFromJson(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public static Article parseArticleFromJson(JSONObject jsonObject) {
        Article article = new Article();
        try {
            article.setWebUrl(jsonObject.getString("web_url"));

            article.setHeadline( new Headline(jsonObject.getJSONObject("headline").getString("main")));

            List<Multimedia> multimediaList = new ArrayList<>();

            JSONArray multimediaJsonArray = jsonObject.getJSONArray("multimedia");

            for (int i = 0; i < multimediaJsonArray.length(); i++) {
                String url = multimediaJsonArray.getJSONObject(i).getString("url");
                Multimedia media = new Multimedia();
                media.setUrl(url);
                multimediaList.add(media);
            }

            article.setMultimediaList(multimediaList);
        } catch (JSONException e) {

        }
        return article;
    }

    public static ArrayList<Article> fromJsonArrayUsingJson(JSONArray jsonArray) throws JSONException {
        ArrayList<Article> results = new ArrayList<>();

        Gson gson = new GsonBuilder().create();

        for (int i = 0; i < jsonArray.length(); i++) {
            results.add(gson.fromJson(jsonArray.getString(i).toString(), Article.class));
        }
        return results;
    }

    public static String getThumbNailUrl(Article article) {
        String url ="";

        if(article.getMultimediaList().size() > 0) {
            StringBuilder builder = new StringBuilder(NT_TIMES_URL);
            url = builder.append(article.getMultimediaList().get(0).getUrl()).toString();
        }
        return url;
    }

    public static void populatingArticleItemData(Context context, Article article, ImageView imageView, TextView tvTitle) {
        //clear out recycled image from convertView from
        imageView.setImageResource(0);
        tvTitle.setText(article.getHeadline().getMain());

        // populate the thumbnail image
        // remote download the image in the background
        String thumbnail = ArticleUtils.getThumbNailUrl(article);

        if (!TextUtils.isEmpty(thumbnail) && NetworkUtils.isNetworkAvailable(context, true)) {
            Picasso.with(context).load(thumbnail).into(imageView);
        }
    }
}
