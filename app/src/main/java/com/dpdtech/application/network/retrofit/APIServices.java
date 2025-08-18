package com.dpdtech.application.network.retrofit;


import com.dpdtech.application.network.models.request.LoginRequest;
import com.dpdtech.application.network.models.response.LoginResponse;
import com.dpdtech.application.oper.GetData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface APIServices {

    @POST
    Call<com.dpdtech.application.CurrentDayTicketDetailsResponse> getTicketdata(
            @Url String url,
            @Body com.dpdtech.application.BarcodeData barcodedata);

    @POST
    Call<com.dpdtech.application.GetDataResponse> getdetails(
            @Url String url,
            @Body GetData barcodedata);


    @POST
    Call<LoginResponse> performLogin(
            @Header("userID") String userId,
            @Url String url,
            @Body LoginRequest loginRequest);


}
