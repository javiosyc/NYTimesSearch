package javio.com.nytimessearch.activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import javio.com.nytimessearch.R;
import javio.com.nytimessearch.adapters.ArticleArrayAdapter;
import javio.com.nytimessearch.adapters.ArticleRecyclerViewAdapter;
import javio.com.nytimessearch.fragments.SearchSettingFragment;
import javio.com.nytimessearch.interfaces.ArticleItemInterface;
import javio.com.nytimessearch.listeners.EndlessRecyclerViewScrollListener;
import javio.com.nytimessearch.listeners.EndlessScrollListener;
import javio.com.nytimessearch.models.Article;
import javio.com.nytimessearch.models.SearchSetting;
import javio.com.nytimessearch.network.NYTimesAsyncHttpClient;
import javio.com.nytimessearch.utils.NetworkUtils;
import javio.com.nytimessearch.utils.RecyclerViewUtil;
import javio.com.nytimessearch.utils.SearchSettingUtils;

public class SearchActivity extends AppCompatActivity implements SearchSettingFragment.SearchSettingDiglogListener {
    private static final boolean IS_USE_CUSTOMER_TAB = true;

    private static final boolean IS_USE_RECYCLER_VIEW = true;

    @Nullable
    @BindView(R.id.gvResults)
    GridView gvResults;

    @Nullable
    @BindView(R.id.rvResults)
    RecyclerView rvResults;

    private List<Article> articles;

    private ArticleArrayAdapter adapter;

    private ArticleRecyclerViewAdapter recyclerViewAdapter;

    private String queryString;

    private SearchSetting searchSetting;

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layoutResID;

        if (IS_USE_RECYCLER_VIEW) {
            layoutResID = R.layout.activity_search_recyclerview;
        } else {
            layoutResID = R.layout.activity_search;
        }

        setContentView(layoutResID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        setupViews();

        initSettings();
    }

    private void initSettings() {
        searchSetting = new SearchSetting();
        SharedPreferences mSettings = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SearchSettingUtils.populateSearchSetting(mSettings, searchSetting);
    }

    private void setupViews() {
        ButterKnife.bind(this);
        articles = new ArrayList<>();

        if (IS_USE_RECYCLER_VIEW) {
            // Create adapter passing in the sample user data
            recyclerViewAdapter = new ArticleRecyclerViewAdapter(this, articles);

            // Attach the adapter to the recyclerview to populate items
            rvResults.setAdapter(recyclerViewAdapter);

            StaggeredGridLayoutManager linearLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

            rvResults.setLayoutManager(linearLayoutManager);

            scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    Log.d("DEBUG", "page: " + page + " , totalItemCount" + totalItemsCount);
                    loadNextDataFromApi(page + 1, recyclerViewAdapter);
                }
            };

            rvResults.addOnScrollListener(scrollListener);

            RecyclerViewUtil util = new RecyclerViewUtil(this, rvResults);

            util.setOnItemClickListener((position, view) -> {
                Article article = articles.get(position);
                showWebView(article);
            });
        } else {
            adapter = new ArticleArrayAdapter(this, articles);
            gvResults.setAdapter(adapter);

            gvResults.setOnItemClickListener((parent, view, position, id) -> {

                Article article = articles.get(position);

                showWebView(article);
            });

            gvResults.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public boolean onLoadMore(int page, int totalItemsCount) {
                    Log.d("DEBUG", "page: " + page + " , totalItemCount" + totalItemsCount);
                    loadNextDataFromApi(page, adapter);

                    return true;
                }
            });
        }
    }

    private void showWebView(Article article) {
        if (IS_USE_CUSTOMER_TAB) {
            launchCustomTab(article.getWebUrl());
        } else {
            startArticleActivity(article);
        }
    }

    private void startArticleActivity(Article article) {

        Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);

        intent.putExtra("article", article);

        startActivity(intent);
    }

    private void launchCustomTab(String webUrl) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, webUrl);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_black_24dp);
        int requestCode = 100;

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // set toolbar color and/or setting custom actions before invoking build()
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent

        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));

        builder.setActionButton(bitmap, "Share Link", pendingIntent, true);

        CustomTabsIntent customTabsIntent = builder.build();
        // and launch the desired Url with CustomTabsIntent.launchUrl()

        customTabsIntent.launchUrl(SearchActivity.this, Uri.parse(webUrl));
    }

    private void loadNextDataFromApi(int page, ArticleItemInterface itemAdapter) {
        Log.d("DEBUG", "page = " + page);
        if (!TextUtils.isEmpty(queryString))
            NYTimesAsyncHttpClient.getInstance().articleSearch(itemAdapter, queryString, page - 1, false, this.searchSetting);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                if (!TextUtils.isEmpty(query) && NetworkUtils.isNetworkAvailable(SearchActivity.this, true)) {
                    queryString = query;

                    if (IS_USE_RECYCLER_VIEW) {
                        NYTimesAsyncHttpClient.getInstance().articleSearch(recyclerViewAdapter, query, searchSetting);
                    } else {
                        NYTimesAsyncHttpClient.getInstance().articleSearch(adapter, query, searchSetting);
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSettingDialogFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSettingDialogFragment() {
        FragmentManager fm = getSupportFragmentManager();
        SearchSettingFragment searchSettingFragmentar = SearchSettingFragment.newInstance(this.searchSetting);
        searchSettingFragmentar.show(fm, "fragment_edit_settings");
    }

    @Override
    public void onFinishEditDialog(SearchSetting settings) {

        Log.d("DEBUG", settings.getBeginDate());

        saveSettings(settings);
    }

    private void saveSettings(SearchSetting settings) {
        SharedPreferences mSettings = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SearchSettingUtils.saveSearchSetting(mSettings, this.searchSetting, settings);
    }
}
