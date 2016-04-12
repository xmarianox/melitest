package xyz.marianomolina.melitest.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import xyz.marianomolina.melitest.model.Installments;
import xyz.marianomolina.melitest.model.Issuer;
import xyz.marianomolina.melitest.model.PaymentMethod;

/**
 * Created by Mariano Molina on 9/4/16.
 * Twitter: @xsincrueldadx
 */
public interface PaymentService {

    @GET("/v1/payment_methods")
    Call<List<PaymentMethod>> getPaymentMethod(@Query("public_key") String plublicKey);

    @GET("/v1/payment_methods/card_issuers")
    Call<List<Issuer>> getPaymentMethodSelected(@Query("public_key") String plublicKey, @Query("payment_method_id") String paymentMethodId);

    @GET("/v1/payment_methods/installments")
    Call<List<Installments>> getInstallmentsOptions(@Query("public_key") String plublicKey, @Query("amount") String amountValue, @Query("payment_method_id") String paymentMethodId, @Query("issuer.id") String issuerID);

}
