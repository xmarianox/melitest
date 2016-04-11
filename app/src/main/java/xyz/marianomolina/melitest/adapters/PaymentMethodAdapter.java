package xyz.marianomolina.melitest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xyz.marianomolina.melitest.R;
import xyz.marianomolina.melitest.model.PaymentMethod;


/**
 * Created by Mariano Molina on 10/4/16.
 * Twitter: @xsincrueldadx
 */
public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {
    private static final String LOG_TAG = PaymentMethodAdapter.class.getSimpleName();

    private List<PaymentMethod> dataset;
    private int itemLayout;
    private Context mContext;

    public PaymentMethodAdapter(List<PaymentMethod> dataset, int layout, Context context) {
        this.dataset = dataset;
        this.itemLayout = layout;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PaymentMethod paymentMethod = dataset.get(position);

        // set imageView contentDescription
        holder.mImageView.setContentDescription(paymentMethod.getId());
        // set name
        holder.mTextView.setText(paymentMethod.getName());
        // load images
        Picasso.with(mContext)
                .load(paymentMethod.getThumbnail())
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.itemImage) ImageView mImageView;
        @Bind(R.id.itemName) TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
