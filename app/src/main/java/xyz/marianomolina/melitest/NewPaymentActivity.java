package xyz.marianomolina.melitest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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
    @Bind(R.id.selectedBankImage) ImageView selectedBankImage;
    @Bind(R.id.selectedBankLabel) TextView selectedBankLabel;
    @Bind(R.id.installmentsSpinner) Spinner installmentsSpinner;
    @Bind(R.id.recomendedMessage) TextView recomendedMessage;

    private ProgressDialog progressDialog;
    private String EXTRA_AMOUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment);

        // instanciamos butterKnife
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        // set back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

                final Installments installmentsItem = response.body().get(0);

                Picasso.with(NewPaymentActivity.this)
                        .load(installmentsItem.getIssuer().getSecure_thumbnail())
                        .into(selectedBankImage);
                selectedBankImage.setContentDescription(installmentsItem.getIssuer().getName());
                selectedBankLabel.setText(installmentsItem.getIssuer().getName());

                // Spinner
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(NewPaymentActivity.this, R.array.installments_list, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                installmentsSpinner.setAdapter(adapter);

                installmentsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Log.d(TAG, installmentsItem.getPayer_costs().get(position).getRecommended_message());
                        recomendedMessage.setText(installmentsItem.getPayer_costs().get(position).getRecommended_message());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

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
