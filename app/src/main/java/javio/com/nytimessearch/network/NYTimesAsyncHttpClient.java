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

    private static class NYTimesAsyncHttpClientHolder {
        private static NYTimesAsyncHttpClient instance = new NYTimesAsyncHttpClient();
    }

    public void articleSearch(final ArrayAdapter<Article> arrayAdapter, String query, int page, final boolean isReset) {
        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);
        params.put("page", page);
        params.put("q", query);
        params.put("fl", LIST_FIELDS);
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

    public void articleSearch(ArrayAdapter<Article> arrayAdapter, String query) {
        articleSearch(arrayAdapter, query, 0, true);
    }
}
