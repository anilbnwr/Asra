package com.asra.mobileapp.ui.base;

public class BaseObserver {

    private BaseViewModel viewModel;


    public BaseObserver(BaseViewModel viewModel){
        this.viewModel = viewModel;
    }



    public void observeLiveData(ETFragment fragment) {
        viewModel.progressDialogObservable.observe(fragment, progressData -> fragment.getBaseActivity().showProgressBar(progressData));
        viewModel.switchModuleObservable.observe(fragment, fragment::switchModule);

        viewModel.errorMessageObservable.observe(fragment, fragment::showErrorMessage);
        viewModel.errorAlertMessageObservable.observe(fragment, message -> fragment.showSimpleDialog("", message, ""));

        viewModel.errorEmptyMessageObservable.observe(fragment, message -> fragment.showEmptyMessage(true, message));

        viewModel.logoutObservable.observe(fragment, logout -> fragment.getBaseActivity().doLogout());

        viewModel.exitScreenObservable.observe(fragment, fragment::exitScreen);
        viewModel.switchAppObservable.observe(fragment, changed -> fragment.onAppModeSwitched());
        viewModel.webPageObservable.observe(fragment, fragment::openWebPage);

        viewModel.loginRequestedObservable.observe(fragment, fragment::onLoginRequested);

        viewModel.successToastObservable.observe(fragment, fragment::showSuccessToast);
        viewModel.errorToastObservable.observe(fragment, fragment::showErrorToast);

        viewModel.successMessageObservable.observe(fragment, fragment::showSuccessMessage);
    }


    public void observeLiveData(BaseActivity activity) {
        viewModel.progressDialogObservable.observe(activity, activity::showProgressBar);


        viewModel.switchModuleObservable.observe(activity, (Boolean isAdmin) ->{
            //activity.switchModule(isAdmin);
        });

        viewModel.errorMessageObservable.observe(activity, activity::showErrorMessage);
        viewModel.logoutObservable.observe(activity, logout -> activity.doLogout());

        viewModel.switchAppObservable.observe(activity, changed -> activity.onAppModeSwitched());
    }
}
