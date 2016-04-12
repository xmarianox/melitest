package xyz.marianomolina.melitest.model;

import java.util.List;

/**
 * Created by Mariano Molina on 12/4/16.
 * Twitter: @xsincrueldadx
 */
public class PayerCost {
    private int installments;
    private double installment_rate;
    private double discount_rate;
    private List<String> labels;
    private int min_allowed_amount;
    private long max_allowed_amount;
    private String recommended_message;
    private double installment_amount;
    private double total_amount;

    public int getInstallments() {
        return installments;
    }

    public void setInstallments(int installments) {
        this.installments = installments;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public int getMin_allowed_amount() {
        return min_allowed_amount;
    }

    public void setMin_allowed_amount(int min_allowed_amount) {
        this.min_allowed_amount = min_allowed_amount;
    }

    public String getRecommended_message() {
        return recommended_message;
    }

    public void setRecommended_message(String recommended_message) {
        this.recommended_message = recommended_message;
    }

    public long getMax_allowed_amount() {
        return max_allowed_amount;
    }

    public void setMax_allowed_amount(long max_allowed_amount) {
        this.max_allowed_amount = max_allowed_amount;
    }

    public double getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(double discount_rate) {
        this.discount_rate = discount_rate;
    }

    public double getInstallment_amount() {
        return installment_amount;
    }

    public void setInstallment_amount(double installment_amount) {
        this.installment_amount = installment_amount;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public double getInstallment_rate() {
        return installment_rate;
    }

    public void setInstallment_rate(double installment_rate) {
        this.installment_rate = installment_rate;
    }
}
