package com.asra.mobileapp.retrofit;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitHelper {


    @POST("deletemember")
    Call<DeleteUserResponseModel> deleteApiPost();

}
