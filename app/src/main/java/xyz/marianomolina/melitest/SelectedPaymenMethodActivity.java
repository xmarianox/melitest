package xyz.marianomolina.melitest;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import xyz.marianomolina.melitest.model.CardIssuer;
import xyz.marianomolina.melitest.model.PaymentMethod;
import xyz.marianomolina.melitest.services.PaymentService;

public class SelectedPaymenMethodActivity extends AppCompatActivity {
    private static final String TAG = SelectedPaymenMethodActivity.class.getSimpleName();

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.credit_cards_list) RecyclerView mRecyclerView;

    private CardIssuerAdapter mAdapter;
    private ProgressDialog progressDialog;
    private PaymentMethod mPaymentMethod;

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
        //
        Gson gson = new Gson();
        String jsonExtra = getIntent().getStringExtra("PAYMENT_METHOD");
        mPaymentMethod = gson.fromJson(jsonExtra, PaymentMethod.class);
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
        Call<List<CardIssuer>> call = paymentService.getPaymentMethodSelected(publicApiKey, paymentMethodId);
        call.enqueue(new Callback<List<CardIssuer>>() {
            @Override
            public void onResponse(Call<List<CardIssuer>> call, Response<List<CardIssuer>> response) {
                progressDialog.dismiss();

                mAdapter = new CardIssuerAdapter(response.body(), R.layout.item_payment_method, getApplicationContext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "ITEM: " + v.getTag());
                    }
                });

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<CardIssuer>> call, Throwable t) {
                Log.d(TAG, "RetrofitError: " + t.getLocalizedMessage());
                // hideProgress
                progressDialog.dismiss();
                // show errorMessage
                Toast.makeText(getApplicationContext(), R.string.payment_error_message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
