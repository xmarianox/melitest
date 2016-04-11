package xyz.marianomolina.melitest.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import xyz.marianomolina.melitest.model.PaymentMethod;

/**
 * Created by Mariano Molina on 9/4/16.
 * Twitter: @xsincrueldadx
 */
public interface PaymentService {

    @GET("/v1/payment_methods")
    Call<List<PaymentMethod>> getPaymentMethod(@Query("public_key") String plublicKey);

}
