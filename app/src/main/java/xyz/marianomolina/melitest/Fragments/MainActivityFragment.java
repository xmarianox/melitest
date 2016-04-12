package xyz.marianomolina.melitest.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.github.florent37.viewanimator.ViewAnimator;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.marianomolina.melitest.PaymentMethodActivity;
import xyz.marianomolina.melitest.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String TAG = "MainActivityFragment";
    @Bind(R.id.inputMonto) EditText inputMonto;
    @Bind(R.id.btnSelectPaymentMethod) Button btnSelectPaymentMethod;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnSelectPaymentMethod.setVisibility(View.GONE);

        inputMonto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.d(TAG, "beforeTextChanged: " + s.toString() + ", count: " + count);
                if (count == 0) {
                    fadeOut(btnSelectPaymentMethod);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d(TAG, "onTextChanged: " + s.toString() + ", count: " + count);
                if (count > 0) {
                    fadeIn(btnSelectPaymentMethod);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEmpty(s)) {
                    fadeIn(btnSelectPaymentMethod);
                } else {
                    fadeOut(btnSelectPaymentMethod);
                }
            }
        });

        btnSelectPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // hideKeyboard
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(inputMonto.getWindowToken(), 0);
                // openPaymentMethodActivity
                Intent intentPaymentMethod = new Intent(getActivity(), PaymentMethodActivity.class);
                intentPaymentMethod.putExtra("EXTRA_PAYMENT_VALUE", inputMonto.getText().toString());
                getActivity().startActivity(intentPaymentMethod);
            }
        });
    }

    public void fadeIn(Button button) {
        button.setVisibility(View.VISIBLE);
        ViewAnimator.animate(button)
                .fadeIn()
                .descelerate()
                .duration(1000)
                .start();
    }

    public void fadeOut(Button button) {
        button.setVisibility(View.GONE);
        ViewAnimator.animate(button)
                .fadeOut()
                .accelerate()
                .duration(1000)
                .start();
    }

    public boolean isEmpty(Editable text) {
        boolean valid = true;
        if (text.toString().matches("")) {
            valid = false;
        }
        return valid;
    }
}
