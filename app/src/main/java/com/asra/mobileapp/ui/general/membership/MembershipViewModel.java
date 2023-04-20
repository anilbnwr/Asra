package com.asra.mobileapp.ui.general.membership;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.MessageProvider;
import com.asra.mobileapp.common.SingleLiveEvent;
import com.asra.mobileapp.core.AppEngine;
import com.asra.mobileapp.model.Membership;
import com.asra.mobileapp.model.MrlMessage;
import com.asra.mobileapp.model.UserMembership;
import com.asra.mobileapp.ui.base.BaseViewModel;
import com.asra.mobileapp.usecase.CartUseCase;
import com.asra.mobileapp.usecase.MemberUseCase;
import com.asra.mobileapp.util.ListUtils;
import com.asra.mobileapp.util.ResourceFetcher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MembershipViewModel extends BaseViewModel {

    public SingleLiveEvent<UserMembership> memberShipObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<List<Membership>> memberShipListObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> memberShipErrorObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> memberShipListErrorObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> memberRoleObservable = new SingleLiveEvent<>();

    public SingleLiveEvent<String> membershipAddedToCartObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<String> membershipCartErrorObservable = new SingleLiveEvent<>();
    public SingleLiveEvent<MrlMessage> mrlConfirmationObservable = new SingleLiveEvent<>();

    private UserMembership currentUserMembership;
    private List<Membership> membershipList;
    private MemberUseCase memberUseCase;
    private CartUseCase cartUseCase;

    private MrlMessage mrlMessage;
    private Membership selectedMembership;

    @Inject
    public MembershipViewModel(CartUseCase cartUseCase,
                               MemberUseCase memberUseCase,
                               AppEngine appEngine,
                               ResourceFetcher resourceFetcher) {
        super(appEngine, resourceFetcher);
        this.memberUseCase = memberUseCase;
        this.cartUseCase = cartUseCase;
    }

    @Override
    public void onViewStarted() {
        super.onViewStarted();
        fetchCurrentMembership();
        fetchMrlMessage();
        memberRoleObservable.postValue(appEngine.getUserRole());
    }

    private void fetchCurrentMembership() {

            showProgressBar();
            Disposable disposable = memberUseCase.getCurrentUserMembership()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userMembership -> {
                                this.currentUserMembership = userMembership;
                                this.memberShipObservable.postValue(userMembership);
                                getAvailableMemberships();
                            },
                            throwable -> {
                                hideProgressBar();
                                memberShipErrorObservable.postValue(throwable.getMessage());
                            });
            addDisposable(disposable);

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

    private void getAvailableMemberships() {
        if(ListUtils.isEmpty(membershipList)) {
            Disposable disposable = memberUseCase.getAvailableMembershipList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(memberships -> {
                                this.membershipList = memberships;
                                this.memberShipListObservable.postValue(memberships);
                                hideProgressBar();
                            },
                            throwable -> {
                                hideProgressBar();
                                memberShipListErrorObservable.postValue(throwable.getMessage());
                            });
            addDisposable(disposable);
        }else{
            hideProgressBar();
            memberShipListObservable.postValue(membershipList);
        }
    }

    public void addMembershipToCart(Membership selectedMembership) {

        this.selectedMembership = selectedMembership;
        if(Membership.ID_MRL_MEMBERSHIP == selectedMembership.getMembershipId()){

            if (appEngine.getUserDetails().hasRaceLicense()){
                submitMembershipCartRequest();
            }else{
                mrlConfirmationObservable.postValue(mrlMessage);
            }
        }else {
            submitMembershipCartRequest();
        }

    }

    public void submitMembershipCartRequest(){
        showProgressBar();
        Disposable disposable = cartUseCase.addMembershipToCart(selectedMembership)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                            hideProgressBar();
                            membershipAddedToCartObservable.postValue(getConfigString(MessageProvider.msg_cart_membership_added));
                        },
                        throwable -> {
                            hideProgressBar();
                            membershipCartErrorObservable.postValue(throwable.getMessage());
                        });
        addDisposable(disposable);
    }
}
