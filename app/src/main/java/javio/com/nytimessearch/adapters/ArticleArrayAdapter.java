package javio.com.nytimessearch.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javio.com.nytimessearch.R;
import javio.com.nytimessearch.interfaces.ArticleItemInterface;
import javio.com.nytimessearch.models.Article;
import javio.com.nytimessearch.utils.ArticleUtils;

/**
 * Created by javiosyc on 2017/2/21.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> implements ArticleItemInterface {
    private Context context;

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // get the data item for position
        Article article = this.getItem(position);

        // check to see if existing view being reuse
        // not using a recycled view -> inflate the layout
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }


        //find the image view
        ImageView imageView = (ImageView) convertView.findViewById(R.id.ivImage);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        ArticleUtils.populatingArticleItemData(context, article, imageView, tvTitle);

        return convertView;
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void addAllItem(ArrayList<Article> articles) {
        addAll(articles);
    }
}
