package com.asra.mobileapp.ui.dashboard.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.model.Category;
import com.asra.mobileapp.ui.base.ETFragment;
import com.asra.mobileapp.ui.dashboard.shop.product.ProductFragment;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class CategoryTabAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private List<Category> categoryList;
    private String categoryType;

    public CategoryTabAdapter(String categoryType, List<Category> categoryList, Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        this.categoryList = categoryList;
        this.categoryType = categoryType;

    }

    @Override
    public ETFragment getItem(int position) {
        if(position < categoryList.size()) {
            return ProductFragment.newInstance(categoryList.get(position).id, categoryType);
        }else{
            return ProductFragment.newInstance("", "");
        }
    }

    @Override
    public int getCount() {
        return categoryList == null ? 0 : categoryList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(position < categoryList.size()){
            return  categoryList.get(position).title;
        }else{
            return "";
        }
    }

    public View getTabView(int position) {
        View tab = LayoutInflater.from(context).inflate(R.layout.tab_view, null);
        TextView tv = tab.findViewById(R.id.custom_text);
        tv.setText(getPageTitle(position).toString());
        return tab;
    }
}
