package com.asra.mobileapp.common;

import android.view.View;
import android.widget.TextView;

import com.asra.mobileapp.R;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.ui.dashboard.home.ExpandableCardHelper;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ThemeHelper {

    private AppEngine appEngine;
    private ResourceFetcher resourceFetcher;

    boolean isEvApp;
    @Inject
    public ThemeHelper(AppEngine appEngine, ResourceFetcher resourceFetcher){
        this.appEngine = appEngine;
        this.resourceFetcher = resourceFetcher;
        isEvApp = appEngine.isEvApp();
    }

    public int getColorFromResource(int colorResource){
        return resourceFetcher.getColor(colorResource);
    }

    public int getPrimaryColor(){
        int colorCode = isEvApp ? R.color.colorGreen :R.color.colorBlue;
        return getColorFromResource(colorCode);
    }

    public void applyPrimaryTextColor(List<TextView> textViews){
        int primaryColor = getPrimaryColor();
        for(TextView tv : textViews){
            tv.setTextColor(primaryColor);
        }
    }
    public void applyPrimaryTextColor(TextView textView){
        int primaryColor = getPrimaryColor();
        if(textView != null)
        textView.setTextColor(primaryColor);
    }

    public void applyBackgroundColor(View view){
        int primaryColor = getPrimaryColor();
        if(view != null)
        view.setBackgroundColor(primaryColor);
    }

    public void applyButtonBackground(View view){
        int resource = isEvApp ? R.drawable.selector_button_primary :
                R.drawable.selector_button_moto_primary;
        if(view != null)
        view.setBackgroundResource(resource);
    }

    public void applyTheme(ExpandableCardHelper expandableCardHelper){
        TextView cardTitle = expandableCardHelper.getRootView().findViewById(R.id.expandableTitle);
        TextView eventTitle = expandableCardHelper.getRootView().findViewById(R.id.expandableDataTitle);
        TextView seeMore = expandableCardHelper.getRootView().findViewById(R.id.btn_see_more);
        applyPrimaryTextColor(cardTitle);
        applyPrimaryTextColor(eventTitle);
        applyPrimaryTextColor(seeMore);
    }
}
