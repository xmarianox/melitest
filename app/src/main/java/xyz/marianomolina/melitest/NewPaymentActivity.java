package xyz.marianomolina.melitest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.marianomolina.melitest.model.Installments;
import xyz.marianomolina.melitest.model.Issuer;
import xyz.marianomolina.melitest.model.PaymentMethod;
import xyz.marianomolina.melitest.services.PaymentService;

public class NewPaymentActivity extends AppCompatActivity {
    private static final String TAG = NewPaymentActivity.class.getSimpleName();

    @Bind(R.id.toolbar) Toolbar toolbar;

    private ProgressDialog progressDialog;
    private String EXTRA_AMOUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment);

        // instanciamos butterKnife
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.payment_method_activity);
        // set back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.payment_method_activity);
        }

        try {
            EXTRA_AMOUNT = URLEncoder.encode(getIntent().getStringExtra("EXTRA_PAYMENT_VALUE"), "UTF-8");

            Log.d(TAG, "A PAGAR: " + EXTRA_AMOUNT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Issuer EXTRA_ISSUER = gson.fromJson(getIntent().getStringExtra("EXTRA_ISSUER"), Issuer.class);
        PaymentMethod EXTRA_PAYMENT_METHOD = gson.fromJson(getIntent().getStringExtra("EXTRA_PAYMENT_METHOD"), PaymentMethod.class);

        // call
        getAsyncCardIssuers(getString(R.string.public_key), EXTRA_AMOUNT, EXTRA_PAYMENT_METHOD.getId(), EXTRA_ISSUER.getId());
    }

    public void getAsyncCardIssuers(String publicApiKey, String amount ,String paymentMethodId, String issuerID) {

        // show progressbar
        progressDialog = new ProgressDialog(NewPaymentActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.dialog_text));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mercadopago.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PaymentService paymentService = retrofit.create(PaymentService.class);
        Call<List<Installments>> call = paymentService.getInstallmentsOptions(publicApiKey, amount, paymentMethodId, issuerID);
        call.enqueue(new Callback<List<Installments>>() {
            @Override
            public void onResponse(Call<List<Installments>> call, Response<List<Installments>> response) {
                progressDialog.dismiss();

                Log.d(TAG, "RESPONSE: " + response.body());
            }

            @Override
            public void onFailure(Call<List<Installments>> call, Throwable t) {
                Log.d(TAG, "RetrofitError: " + t.getLocalizedMessage());
                // hideProgress
                progressDialog.dismiss();
                // show errorMessage
                Toast.makeText(getApplicationContext(), R.string.payment_error_message, Toast.LENGTH_SHORT).show();
            }
        });

    }



}
