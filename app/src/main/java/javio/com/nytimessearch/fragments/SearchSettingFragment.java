package javio.com.nytimessearch.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javio.com.nytimessearch.R;
import javio.com.nytimessearch.adapters.SortOrderArrayAdapter;
import javio.com.nytimessearch.models.SearchSetting;

/**
 *
 */
public class SearchSettingFragment extends DialogFragment implements Button.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String ARG_PARAM1 = "setting";
    List<String> order;
    private SearchSetting setting;
    private TextView tvBeginDateSetting;
    private ImageButton btBeginDate;
    private Spinner spSortOrder;
    private CheckBox cbArts;
    private CheckBox cbFashion;
    private CheckBox cbSports;
    private Button btSave;
    private SearchSetting searchSetting;

    public SearchSettingFragment() {
        order = new ArrayList<>();
        order.add("oldest");
        order.add("newest");
    }

    public static SearchSettingFragment newInstance(SearchSetting searchSetting) {
        SearchSettingFragment fragment = new SearchSettingFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, searchSetting);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchSetting = (SearchSetting) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_setting, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvBeginDateSetting = (TextView) view.findViewById(R.id.tvBeginDateSetting);
        btBeginDate = (ImageButton) view.findViewById(R.id.ibBeginDate);
        spSortOrder = (Spinner) view.findViewById(R.id.spSortOrder);
        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbFashion = (CheckBox) view.findViewById(R.id.cbFashion);
        cbSports = (CheckBox) view.findViewById(R.id.cbSports);
        btSave = (Button) view.findViewById(R.id.btSave);


        spSortOrder.setAdapter(new SortOrderArrayAdapter(this.getContext(), order));

        tvBeginDateSetting.setText(searchSetting.getBeginDate());

        String sortOrder = searchSetting.getSortOrder();
        if ("oldest".equals(sortOrder) || TextUtils.isEmpty(sortOrder))
            spSortOrder.setSelection(0);
        else if ("newest".equals(sortOrder)) {
            spSortOrder.setSelection(1);
        }

        cbArts.setChecked(searchSetting.isArts());
        cbFashion.setChecked(searchSetting.isFashion());
        cbSports.setChecked(searchSetting.isSports());

        btBeginDate.setOnClickListener(v -> {
            showEditDialog();
        });

        btSave.setOnClickListener(this::onClick);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onClick(View v) {

        SearchSettingDiglogListener listener = (SearchSettingDiglogListener) getActivity();

        SearchSetting searchSetting = new SearchSetting();

        searchSetting.setBeginDate(tvBeginDateSetting.getText().toString());
        searchSetting.setSortOrder(spSortOrder.getSelectedItem().toString());
        searchSetting.setArts(cbArts.isChecked());
        searchSetting.setFashion(cbFashion.isChecked());
        searchSetting.setSports(cbSports.isChecked());

        listener.onFinishEditDialog(searchSetting);

        dismiss();
    }


    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        // SETS the target fragment for use later when sending results
        datePickerFragment.setTargetFragment(SearchSettingFragment.this, 300);
        datePickerFragment.show(fm, "fragment_edit_name");
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        String date = sdf.format(c.getTime());

        tvBeginDateSetting.setText(date, TextView.BufferType.EDITABLE);
    }

    public interface SearchSettingDiglogListener {
        void onFinishEditDialog(SearchSetting settings);
    }

}
