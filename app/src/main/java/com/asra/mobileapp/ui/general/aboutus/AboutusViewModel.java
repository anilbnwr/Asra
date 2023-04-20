package com.asra.mobileapp.ui.general.aboutus;

import com.asra.mobileapp.BuildConfig;
import com.asra.mobileapp.common.AppUtils;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.AppVersionStatus;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

public class AboutusViewModel extends BaseViewModel {


    public SingleLiveEvent<String> appVersionObservable = new SingleLiveEvent<>();
    @Inject
    public AboutusViewModel(AppEngine appEngine, ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();

        checkAppVersion();
    }

    private void checkAppVersion() {
        if(appEngine.getVersionCode() <= AppVersionStatus.VERSION_NOT_AVAILABLE) {
            if (AppUtils.isAppLatest()) {
                appVersionObservable.postValue(getConfigString(MessageProvider.msg_version_latest));
            } else {
                appVersionObservable.postValue(getConfigString(MessageProvider.msg_version_update));
            }
        }else{
            if (appEngine.getVersionCode() <= BuildConfig.VERSION_CODE ) {
                appVersionObservable.postValue(getConfigString(MessageProvider.msg_version_latest));
            } else {
                appVersionObservable.postValue(getConfigString(MessageProvider.msg_version_update));
            }
        }
    }
}
