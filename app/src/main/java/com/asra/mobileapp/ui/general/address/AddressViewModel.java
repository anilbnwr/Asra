package com.asra.mobileapp.ui.general.address;

import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.Address;
import com.asra.mobileapp.model.Country;
import com.asra.mobileapp.model.State;
import com.asra.mobileapp.model.UserDetails;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddressViewModel extends BaseViewModel {

    public SingleLiveEvent<UserDetails> userDetailsObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<List<Country>> countryListObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<List<State>> stateListObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> stateListErrorObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<Country> countrySelectionObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<State> stateSelectionObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<String> addressUpdatedObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> addressUpdateFailedObservable = new SingleLiveEvent<>();

    private MemberUseCase memberUseCase;


    private State selectedState;

    private int addressType;
    private UserDetails userDetails;

    private List<State> stateList;
    private List<Country> countryList;

    private Country selectedCountry;

    @Inject
    public AddressViewModel(MemberUseCase memberUseCase,
                            AppEngine appEngine,
                            ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
    }



    @Override
    public void onViewStarted() {
        super.onViewStarted();

        this.userDetails = appEngine.getUserDetails();
        userDetailsObservable.postValue(userDetails);

        if(ListUtils.isEmpty(appEngine.getCountries())) {
            fetchCountryList();
        }else {
            countryListFetched(appEngine.getCountries());
        }
    }

    private void fetchCountryList() {
        showProgressBar();
        Disposable disposable = memberUseCase.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::countryListFetched, throwable -> hideProgressBar());
        addDisposable(disposable);
    }

    private void countryListFetched(List<Country> countries) {
        hideProgressBar();
        this.countryList = countries;
        appEngine.setCountries(countries);
        countryListObservable.postValue(countries);

        if(countries.size() == 1){
            selectedCountry = countries.get(0);
            countrySelectionObservable.postValue(selectedCountry);
            fetchStateList(selectedCountry.countryCode);
        }else{
            if(addressType == AddressConstants.TYPE_BILLING)
                setSelectedCountry(userDetails.billingCountryCode);
            else
                setSelectedCountry(userDetails.shippingCountryCode);

        }
    }

    private void fetchStateList(String countryCode) {
        showProgressBar();
        Disposable disposable = memberUseCase.getStates(countryCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::stateListFetched, throwable -> {
                    hideProgressBar();
                    stateListErrorObservable.postValue(throwable.getMessage());
                });
        addDisposable(disposable);
    }

    private void stateListFetched(List<State> states) {
        this.stateList = states;
        stateListObservable.postValue(states);
        hideProgressBar();
        if(addressType == AddressConstants.TYPE_BILLING)
            setSelectedState(userDetails.billingState);
        else
            setSelectedState(userDetails.shippingState);
    }

    public void onCountrySelected(Country country) {
        selectedCountry = country;
        onStateSelected(null);
        fetchStateList(country.countryCode);
    }

    public void onStateSelected(State state) {
        selectedState = state;
        stateSelectionObservable.postValue(state);
    }

    public void updateBillingAddress(Address address) {
        if(selectedCountry != null){
            address.country = selectedCountry.shortCode;
        }
        if(selectedState != null){
            address.state = selectedState.shortName;
        }


        showProgressBar(getConfigString(MessageProvider.msg_updating_billing_address));
        Disposable disposable = memberUseCase.updateBillingAddress(address)
                .concatWith(memberUseCase.getUserDetails().ignoreElement())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAddressUpdated, this:: onAddressUpdateFailed);
        addDisposable(disposable);
    }

    private void onAddressUpdateFailed(Throwable throwable) {
        hideProgressBar();
        addressUpdateFailedObservable.postValue(throwable.getMessage());
    }


    private void onAddressUpdated() {
        hideProgressBar();
        if(addressType == AddressConstants.TYPE_BILLING){

            addressUpdatedObservable.postValue(getConfigString(MessageProvider.msg_billing_address_updated));
        }else{
            addressUpdatedObservable.postValue(getConfigString(MessageProvider.msg_mailing_address_updated));
        }
        exitScreenObservable.postValue(true);
    }

    public void updateShippingAddress(Address address) {
        if(selectedCountry != null){
            address.country = selectedCountry.shortCode;
        }
        if(selectedState != null){
            address.state = selectedState.shortName;
        }
        showProgressBar(getConfigString(MessageProvider.msg_updating_mailing_address));
        Disposable disposable = memberUseCase.updateMailingAddress(address)
                .concatWith(memberUseCase.getUserDetails().ignoreElement())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onAddressUpdated, this:: onAddressUpdateFailed);
        addDisposable(disposable);
    }

    private void setSelectedState(String stateCode){
        if(stateList != null && !stateList.isEmpty()){
            for(State state: stateList){
                if(state.shortName.equalsIgnoreCase(stateCode)
                        || state.name.equalsIgnoreCase(stateCode) ){
                    onStateSelected(state);
                    break;
                }
            }
        }
    }

    private void setSelectedCountry(String countryCode){
        if(countryList != null && !countryList.isEmpty()){
            for(Country country: countryList){
                if(country.shortCode.equalsIgnoreCase(countryCode)
                        || country.name.equalsIgnoreCase(countryCode) ){
                    onCountrySelected(country);
                    break;
                }
            }
        }
    }

    public void setAddressType(int requestType) {
        addressType = requestType;
    }
}
