package xyz.marianomolina.melitest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import xyz.marianomolina.melitest.adapters.PaymentMethodAdapter;
import xyz.marianomolina.melitest.model.PaymentMethod;
import xyz.marianomolina.melitest.services.PaymentService;
/**
 * Created by Mariano Molina on 9/4/16.
 * Twitter: @xsincrueldadx
 */
public class PaymentMethodActivity extends AppCompatActivity {
    private static final String TAG = PaymentMethodActivity.class.getSimpleName();

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.payment_methods_list) RecyclerView mRecyclerView;

    private PaymentMethodAdapter adapter;
    private ProgressDialog progressDialog;
    private String EXTRA_PAYMENT_VALUE;

    private int RESULT_CODE = 128;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
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

        if (savedInstanceState != null) {
            EXTRA_PAYMENT_VALUE = savedInstanceState.getString("EXTRA_PAYMENT_VALUE");
        } else {
            EXTRA_PAYMENT_VALUE = getIntent().getStringExtra("EXTRA_PAYMENT_VALUE");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("EXTRA_PAYMENT_VALUE", EXTRA_PAYMENT_VALUE);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        EXTRA_PAYMENT_VALUE = savedInstanceState.getString("EXTRA_PAYMENT_VALUE");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter == null) {
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(PaymentMethodActivity.this, DividerItemDecoration.VERTICAL_LIST));
            // Get paymentMethod
            getAsyncPaymentMethods(getString(R.string.public_key));
        }
    }

    public void getAsyncPaymentMethods(String publicApiKey) {

        // show progressbar
        progressDialog = new ProgressDialog(PaymentMethodActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.dialog_text));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mercadopago.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PaymentService paymentService = retrofit.create(PaymentService.class);

        Call<List<PaymentMethod>> call = paymentService.getPaymentMethod(publicApiKey);
        call.enqueue(new Callback<List<PaymentMethod>>() {
            @Override
            public void onResponse(Call<List<PaymentMethod>> call, final Response<List<PaymentMethod>> response) {
                //Log.d(TAG, response.body().toString());
                // hideProgress
                progressDialog.dismiss();
                // setAdapter
                adapter = new PaymentMethodAdapter(response.body(), R.layout.item_payment_method, getApplicationContext(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PaymentMethod selectedPaymentMethod = (PaymentMethod) v.getTag();
                        Gson gson = new Gson();
                        Intent mIntent = new Intent(PaymentMethodActivity.this, SelectedPaymenMethodActivity.class);
                        mIntent.putExtra("PAYMENT_METHOD", gson.toJson(selectedPaymentMethod));
                        mIntent.putExtra("EXTRA_PAYMENT_VALUE", EXTRA_PAYMENT_VALUE);
                        //startActivity(mIntent);
                        startActivityForResult(mIntent, RESULT_CODE);
                    }
                });
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<PaymentMethod>> call, Throwable t) {
                Log.d(TAG, "RetrofitError: " + t.getLocalizedMessage());
                // hideProgress
                progressDialog.dismiss();
                // show errorMessage
                Toast.makeText(getApplicationContext(), R.string.payment_error_message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                EXTRA_PAYMENT_VALUE = data.getStringExtra("EXTRA_PAYMENT_VALUE");
            }
        }
    }
}
