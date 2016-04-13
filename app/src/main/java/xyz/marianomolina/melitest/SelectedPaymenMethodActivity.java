package xyz.marianomolina.melitest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mercadopago.decorations.DividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.marianomolina.melitest.adapters.CardIssuerAdapter;
import xyz.marianomolina.melitest.model.Issuer;
import xyz.marianomolina.melitest.model.PaymentMethod;
import xyz.marianomolina.melitest.services.PaymentService;

public class SelectedPaymenMethodActivity extends AppCompatActivity {
    private static final String TAG = SelectedPaymenMethodActivity.class.getSimpleName();

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.credit_cards_list) RecyclerView mRecyclerView;

    private CardIssuerAdapter mAdapter;
    private ProgressDialog progressDialog;
    private PaymentMethod mPaymentMethod;
    private String EXTRA_PAYMENT_VALUE;
    private String PAYMENT_METHOD;
    private int RESULT_CODE = 129;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_paymen_method);
        // instanciamos butterKnife
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.selected_payment_method_activity);
        // set back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.selected_payment_method_activity);
        }

        if (savedInstanceState != null) {
            PAYMENT_METHOD = savedInstanceState.getString("PAYMENT_METHOD");
            EXTRA_PAYMENT_VALUE = savedInstanceState.getString("EXTRA_PAYMENT_VALUE");

            Log.d(TAG, "Payment method: " + PAYMENT_METHOD + ", " + EXTRA_PAYMENT_VALUE);
        } else {
            PAYMENT_METHOD = getIntent().getStringExtra("PAYMENT_METHOD");
            EXTRA_PAYMENT_VALUE = getIntent().getStringExtra("EXTRA_PAYMENT_VALUE");
        }

        // getData
        Gson gson = new Gson();
        mPaymentMethod = gson.fromJson(PAYMENT_METHOD, PaymentMethod.class);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("PAYMENT_METHOD", PAYMENT_METHOD);
        outState.putString("EXTRA_PAYMENT_VALUE", EXTRA_PAYMENT_VALUE);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        PAYMENT_METHOD = savedInstanceState.getString("PAYMENT_METHOD");
        EXTRA_PAYMENT_VALUE = savedInstanceState.getString("EXTRA_PAYMENT_VALUE");

        Log.d(TAG, "Payment method: " + PAYMENT_METHOD + ", " + EXTRA_PAYMENT_VALUE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAdapter == null) {
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(SelectedPaymenMethodActivity.this, DividerItemDecoration.VERTICAL_LIST));
            // Get
            getAsyncCardIssuers(getString(R.string.public_key), mPaymentMethod.getId());
        }
    }

    public void getAsyncCardIssuers(String publicApiKey, String paymentMethodId) {

        // show progressbar
        progressDialog = new ProgressDialog(SelectedPaymenMethodActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.dialog_text));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mercadopago.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PaymentService paymentService = retrofit.create(PaymentService.class);
        Call<List<Issuer>> call = paymentService.getPaymentMethodSelected(publicApiKey, paymentMethodId);
        call.enqueue(new Callback<List<Issuer>>() {
            @Override
            public void onResponse(Call<List<Issuer>> call, Response<List<Issuer>> response) {
                progressDialog.dismiss();

                mAdapter = new CardIssuerAdapter(response.body(), R.layout.item_payment_method, getApplicationContext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Issuer mIssuer = (Issuer) v.getTag();
                        Gson gson = new Gson();
                        Intent mInten = new Intent(SelectedPaymenMethodActivity.this, NewPaymentActivity.class);
                        mInten.putExtra("EXTRA_PAYMENT_METHOD", gson.toJson(mPaymentMethod));
                        mInten.putExtra("EXTRA_ISSUER", gson.toJson(mIssuer));
                        mInten.putExtra("EXTRA_PAYMENT_VALUE", EXTRA_PAYMENT_VALUE);

                        startActivity(mInten);
                    }
                });

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Issuer>> call, Throwable t) {
                Log.d(TAG, "RetrofitError: " + t.getLocalizedMessage());
                // hideProgress
                progressDialog.dismiss();
                // show errorMessage
                Toast.makeText(getApplicationContext(), R.string.payment_error_message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
