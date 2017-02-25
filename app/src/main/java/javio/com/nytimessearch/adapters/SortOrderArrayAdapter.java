package javio.com.nytimessearch.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by javiosyc on 2017/2/25.
 */

public class SortOrderArrayAdapter extends ArrayAdapter<String> {


    public SortOrderArrayAdapter(Context context, List<String> objects) {
        super(context, android.R.layout.simple_spinner_dropdown_item, objects);
    }
}
