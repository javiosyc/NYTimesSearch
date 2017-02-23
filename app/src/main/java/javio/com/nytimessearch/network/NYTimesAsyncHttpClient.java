package javio.com.nytimessearch.network;

import android.util.Log;
import android.widget.ArrayAdapter;

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
    private final String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

    private final String apiKey = "1085432d3bc14d72a7a0b84aa4c21903";

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

    public void articleSearch(final ArrayAdapter arrayAdapter, String query, int page, final boolean isReset) {
        RequestParams params = new RequestParams();
        params.put("api-key", apiKey);
        params.put("page", page);
        params.put("q", query);

        asyncHttpClient.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());

                JSONArray articleJsonResults = null;

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

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    public void articleSearch(ArrayAdapter arrayAdapter, String query) {
        articleSearch(arrayAdapter, query, 0, true);
    }
}
