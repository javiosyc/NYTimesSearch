package javio.com.nytimessearch.models;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Created by javiosyc on 2017/2/21.
 */

public class ArticleTest {

    @Test
    public void test() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        Gson gson = gsonBuilder.create();

        Article article = new Article();
        article.setHeadline(new Headline("test1"));

        article.setWebUrl("wer");

        System.out.println(gson.toJson(article));
    }

    @Test
    public void testJson() throws JSONException {


        String item = "{\n" +
                "        \"headline\": {\n" +
                "            \"main\": \"Google Expected to Introduce a Wireless Payment System\",\n" +
                "            \"print_headline\": \"Google Expected to Prepare A Wireless Payment System\",\n" +
                "            \"seo\": \"Google Is Said to Have a Wireless Payment System\"\n" +
                "        },\n" +
                "        \"multimedia\": [],\n" +
                "        \"web_url\": \"http://www.nytimes.com/2011/05/25/technology/25mobile.html\"\n" +
                "    }";

        JSONObject object = new JSONObject(item);


        //String item1 = array.get(0).toString();
        System.out.println(object);


        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Article article = gson.fromJson(object.toString(), Article.class);
        System.out.println(article.getHeadline().getMain());
    }

}
