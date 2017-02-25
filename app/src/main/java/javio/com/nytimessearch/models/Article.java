package javio.com.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by javiosyc on 2017/2/21.
 */

public class Article implements Parcelable {

    @SerializedName("web_url")
    private String webUrl;

    private Headline headline;

    @SerializedName("multimedia")
    private List<Multimedia> multimediaList;


    public Article() {
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Headline getHeadline() {
        return headline;
    }

    public void setHeadline(Headline headline) {
        this.headline = headline;
    }

    public List<Multimedia> getMultimediaList() {
        return multimediaList;
    }

    public void setMultimediaList(List<Multimedia> multimediaList) {
        this.multimediaList = multimediaList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.webUrl);
        dest.writeParcelable(this.headline, flags);
        dest.writeTypedList(this.multimediaList);
    }

    protected Article(Parcel in) {
        this.webUrl = in.readString();
        this.headline = in.readParcelable(Headline.class.getClassLoader());
        this.multimediaList = in.createTypedArrayList(Multimedia.CREATOR);
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
