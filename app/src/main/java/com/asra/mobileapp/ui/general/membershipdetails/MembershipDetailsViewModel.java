package com.asra.mobileapp.ui.general.membershipdetails;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.Membership;
import com.asra.mobileapp.model.MembershipDetail;
import com.asra.mobileapp.model.MrlMessage;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ResourceFetcher;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MembershipDetailsViewModel extends BaseViewModel {

    private CartUseCase cartUseCase;
    private MemberUseCase memberUseCase;

    private MembershipDetail membershipDetail;

    public SingleLiveEvent<MembershipDetail> membershipDetailObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> membershipErrorObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> canPurchaseMembershipObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> membershipAddedToCartObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> membershipCartErrorObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<MrlMessage> mrlConfirmationObservable = new SingleLiveEvent<>();

    private MrlMessage mrlMessage;

    @Inject
    public MembershipDetailsViewModel(MemberUseCase memberUseCase,
                                      CartUseCase cartUseCase,
                                      AppEngine appEngine,
                                      ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
        this.cartUseCase = cartUseCase;
        fetchMrlMessage();
    }

    private void fetchMrlMessage() {

        Disposable disposable = memberUseCase.getMrlMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mrlMessage -> {
                            this.mrlMessage = mrlMessage;
                        },
                        throwable -> {
                            mrlMessage = new MrlMessage();
                            mrlMessage.title = "Motogladiator Race License Details";
                            mrlMessage.message = resourceFetcher.readFileFromRawDirectory(R.raw.mrl_licence);
                        });
        addDisposable(disposable);

    }

    public void addMembershipToCart() {

        if(Membership.ID_MRL_MEMBERSHIP == membershipDetail.getMembershipId()){

            if (appEngine.getUserDetails().hasRaceLicense()){
                onUpgrade();
            }else{
                mrlConfirmationObservable.postValue(mrlMessage);
            }
        }else {
            onUpgrade();
        }

    }

    public void onUpgrade() {
        showProgressBar();
        Membership membership = new Membership();
        membership.setSlug(membershipDetail.getSlug());
        membership.setMembershipId(membershipDetail.getMembershipId());
        membership.setPrice(membershipDetail.getPrice());
        membership.setTitle(membershipDetail.getTitle());
        membership.setImage(membershipDetail.getImage());

        Disposable disposable = cartUseCase.addMembershipToCart(membership)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            hideProgressBar();
                            membershipAddedToCartObservable.postValue(
                                    getConfigString(MessageProvider.msg_cart_membership_added));
                        },
                        throwable -> {
                            hideProgressBar();
                            membershipCartErrorObservable.postValue(throwable.getMessage());
                        });
        addDisposable(disposable);
    }

    private void checkPurchaseStatus(){
        canPurchaseMembershipObservable.postValue(
                membershipDetail.canPurchase(appEngine.getCurrentUserMembership()));

    }

    public void fetchMembershipDetails(String slug) {
        showProgressBar();
        Disposable disposable = memberUseCase.getMembershipDetail(slug)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(membershipDetail -> {
                            hideProgressBar();
                            membershipDetailObservable.postValue(membershipDetail);
                            this.membershipDetail = membershipDetail;
                            checkPurchaseStatus();
                        },
                        throwable -> {
                            hideProgressBar();
                            membershipErrorObservable.postValue(throwable.getMessage());
                        });
        addDisposable(disposable);
    }
}
