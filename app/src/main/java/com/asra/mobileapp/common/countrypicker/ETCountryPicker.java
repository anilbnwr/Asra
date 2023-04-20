package com.asra.mobileapp.common.countrypicker;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.model.Country;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.appcompat.app.AppCompatDialog;
import androidx.core.view.ViewCompat;

public class ETCountryPicker extends AppCompatDialog {
    private List<Country> countries;
    private CountryPickerCallback callbacks;
    private ListView listview;

    public ETCountryPicker(Context context, List<Country> countries,
                           CountryPickerCallback callbacks) {

        super(context);
        this.countries = countries;
        this.callbacks = callbacks;
        Collections.sort(countries, new Comparator<Country>() {
            public int compare(Country result1, Country result2) {
                return result1.name.compareTo(result2.name);
            }
        });


    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_country_picker);
        ViewCompat.setElevation(this.getWindow().getDecorView(), 3.0F);
        this.listview = this.findViewById(R.id.country_picker_listview);
        CountryListAdapter adapter = new CountryListAdapter(this.getContext(), this.countries);
        this.listview.setAdapter(adapter);
        this.listview.setOnItemClickListener((parent, view, position, id) -> {
            ETCountryPicker.this.hide();
            Country country = ETCountryPicker.this.countries.get(position);
            ETCountryPicker.this.callbacks.onCountrySelected(country, position);
        });
    }


}