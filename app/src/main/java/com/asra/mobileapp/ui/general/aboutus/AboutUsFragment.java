package com.asra.mobileapp.ui.general.aboutus;

import com.asra.mobileapp.BuildConfig;
import com.asra.mobileapp.R;
import com.asra.mobileapp.databinding.FragmentAboutUsBinding;
import com.asra.mobileapp.ui.base.ETFragment;

public class AboutUsFragment extends ETFragment<AboutusViewModel, FragmentAboutUsBinding> {

    public static AboutUsFragment newInstance(){
        AboutUsFragment fragment = new AboutUsFragment();
        return fragment;
    }


    @Override
    protected Class<AboutusViewModel> getViewModel() {
        return AboutusViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_about_us;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_about_us);
    }



    @Override
    public void initializeViews() {
        dataBinding.tvAppVersion.setText(BuildConfig.VERSION_NAME);
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.appVersionObservable.observe(this, message ->
                dataBinding.tvAppVersionMessage.setText(message));
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();

        dataBinding.imageView.setImageResource(R.drawable.splash_logo_moto);
        dataBinding.aboutUsRoot.setBackgroundResource(R.drawable.moto_gradient_bg);

    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.imageView.setImageResource(R.drawable.splash_logoo);
        dataBinding.aboutUsRoot.setBackgroundResource(R.drawable.splash_background);
    }
}
