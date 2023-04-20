package com.asra.mobileapp.common.countrypicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.asra.mobileapp.model.Country;

import java.util.List;

public class  CountryListAdapter extends BaseAdapter {
    private final Context mContext;
    private LayoutInflater inflater;
    private List<Country> countries;

    public CountryListAdapter(Context context, List<Country> countries) {
        this.mContext = context;
        this.countries = countries;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.countries.size();
    }

    public Object getItem(int position) {
        return this.countries.get(position);
    }

    public long getItemId(int position) {
        return 0L;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        Country country = (Country)this.countries.get(position);
        ViewItem item;
        if (convertView == null) {
            item = new ViewItem();
            itemView = this.inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            item.setName((TextView)itemView.findViewById(android.R.id.text1));
            itemView.setTag(item);
        } else {
            item = (ViewItem) convertView.getTag();
        }

        item.getName().setText(country.name);
        return itemView;
    }

    public static class ViewItem {
        private TextView name;

        public ViewItem() {
        }

        public TextView getName() {
            return this.name;
        }

        public void setName(TextView name) {
            this.name = name;
        }
    }
}