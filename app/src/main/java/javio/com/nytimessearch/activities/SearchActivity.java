package javio.com.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import javio.com.nytimessearch.R;
import javio.com.nytimessearch.adapters.ArticleArrayAdapter;
import javio.com.nytimessearch.listeners.EndlessScrollListener;
import javio.com.nytimessearch.models.Article;
import javio.com.nytimessearch.network.NYTimesAsyncHttpClient;
import javio.com.nytimessearch.utils.NetworkUtils;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.gvResults)
    GridView gvResults;

    private List<Article> articles;

    private ArrayAdapter adapter;

    private String queryString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        setupViews();
    }

    private void setupViews() {
        ButterKnife.bind(this);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create an intent
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);

                Article article = articles.get(position);

                intent.putExtra("article", article);

                startActivity(intent);
            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                Log.d("DEBUG", "page: " + page + " , totalItemCount" + totalItemsCount);
                loadNextDataFromApi(page);

                return true;
            }
        });
    }

    private void loadNextDataFromApi(int page) {
        Log.d("DEBUG", "page = " + page);
        if (!TextUtils.isEmpty(queryString))
            NYTimesAsyncHttpClient.getInstance().articleSearch(adapter, queryString, page - 1, false);
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
                    NYTimesAsyncHttpClient.getInstance().articleSearch(adapter, query);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
