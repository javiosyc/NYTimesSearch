package javio.com.nytimessearch.network;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import javio.com.nytimessearch.models.Article;
import javio.com.nytimessearch.models.SearchSetting;

/**
 * Created by javiosyc on 2017/2/21.
 */

public class NYTimesAsyncHttpClient {
    private static final String URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

    private static final String API_KEY = "1085432d3bc14d72a7a0b84aa4c21903";

    private static final String LIST_FIELDS = "web_url,headline,multimedia";

    private final AsyncHttpClient asyncHttpClient;

    private NYTimesAsyncHttpClient() {
        asyncHttpClient = new AsyncHttpClient();
    }

    public static NYTimesAsyncHttpClient getInstance() {
        return NYTimesAsyncHttpClientHolder.instance;
    }

    public void articleSearch(final ArrayAdapter<Article> arrayAdapter, String query, int page, final boolean isReset, SearchSetting searchSetting) {
        RequestParams params = buildRequestParams(searchSetting);
        params.put("api-key", API_KEY);
        params.put("page", page);
        params.put("q", query);
        params.put("fl", LIST_FIELDS);

        Log.d("params", params.toString());

        asyncHttpClient.get(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("ERROR", String.valueOf(statusCode) + errorResponse.toString());
                Log.d("ERROR", throwable.toString());
                String message = null;
                try {
                    message = errorResponse.getString("message");
                } catch (JSONException e) {
                    Log.d("ERROR", "errorResponse can not parse." + errorResponse.toString(), e);
                }
                if (!TextUtils.isEmpty(message))
                    Toast.makeText(arrayAdapter.getContext(), message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());

                JSONArray articleJsonResults;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");

                    if (isReset)
                        arrayAdapter.clear();

                    arrayAdapter.addAll(Article.fromJsonArray(articleJsonResults));

                    arrayAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private RequestParams buildRequestParams(SearchSetting searchSetting) {

        RequestParams params = new RequestParams();

        if (searchSetting == null)
            return params;

        if (!TextUtils.isEmpty(searchSetting.getBeginDate()))
            params.put("begin_date", searchSetting.getBeginDate());


        if (!TextUtils.isEmpty(searchSetting.getSortOrder()))
            params.put("sort", searchSetting.getSortOrder());

        String newsDesk = getNewDeskString(searchSetting);
        if (!TextUtils.isEmpty(newsDesk))
            params.put("fq", newsDesk);

        return params;
    }

    private String getNewDeskString(SearchSetting searchSetting) {
        StringBuilder builder = new StringBuilder();

        //'fq': "news_desk:(\"Arts\" \"Sports\" \"Fashion & Style\")",
        if (searchSetting.isArts())
            builder.append("\"Arts\"");
        if (searchSetting.isFashion())
            builder.append(" \"Fashion & Style\"");
        if (searchSetting.isSports())
            builder.append(" \"Sports\"");

        if (builder.length() == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder("news_desk:(");
        result.append(builder).append(")");
        return result.toString();
    }

    public void articleSearch(ArrayAdapter<Article> arrayAdapter, String query, SearchSetting searchSetting) {
        articleSearch(arrayAdapter, query, 0, true, searchSetting);
    }

    private static class NYTimesAsyncHttpClientHolder {
        private static NYTimesAsyncHttpClient instance = new NYTimesAsyncHttpClient();
    }
}
