package javio.com.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import javio.com.nytimessearch.R;
import javio.com.nytimessearch.interfaces.ArticleItemInterface;
import javio.com.nytimessearch.models.Article;
import javio.com.nytimessearch.utils.ArticleUtils;

/**
 * Created by javiosyc on 2017/2/21.
 */

public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ArticleItemInterface {


    public class ViewHolderOnlyText extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;

        public ViewHolderOnlyText(View view) {
            super((view));
            ButterKnife.bind(this, view);
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivImage)
        ImageView imageView;

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private List<Article> mArticles;
    // Store the context for easy access
    private Context mContext;

    private final int TEXT = 0, TEXT_AND_IMAGE = 1;

    // Pass in the contact array into the constructor
    public ArticleRecyclerViewAdapter(Context context, List<Article> articles) {
        mArticles = articles;
        mContext = context;
    }


    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case TEXT_AND_IMAGE:
                View v2 = inflater.inflate(R.layout.item_article_result_recyclerview, parent, false);
                viewHolder = new ViewHolder(v2);
                break;
            case TEXT:
            default:
                View v1 = inflater.inflate(R.layout.item_article_result_recyclerview_only_text, parent, false);
                viewHolder = new ViewHolderOnlyText(v1);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        Article article = mArticles.get(position);

        switch (viewHolder.getItemViewType()) {
            case TEXT_AND_IMAGE:
                ViewHolder vh1 = (ViewHolder) viewHolder;
                ArticleUtils.populatingArticleItemData(mContext, article, vh1.imageView, vh1.tvTitle);
                break;
            case TEXT:
            default:
                ViewHolderOnlyText vh2 = (ViewHolderOnlyText) viewHolder;
                ArticleUtils.populatingArticleItemData(mContext, article , vh2.tvTitle);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }


    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void clear() {
        mArticles.clear();
    }

    @Override
    public void addAllItem(ArrayList<Article> articles) {
        mArticles.addAll(articles);
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        Article article = mArticles.get(position);
        if (article.getMultimediaList().size() > 0) {
            type = TEXT_AND_IMAGE;
        } else {
            type = TEXT;
        }
        return type;
    }
}
