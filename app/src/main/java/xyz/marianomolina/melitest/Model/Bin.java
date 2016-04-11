package xyz.marianomolina.melitest.model;

/**
 * Created by Mariano Molina on 11/4/16.
 * Twitter: @xsincrueldadx
 */
public class Bin {
    private String pattern;
    private String exclusion_pattern;
    private String installments_pattern;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getExclusion_pattern() {
        return exclusion_pattern;
    }

    public void setExclusion_pattern(String exclusion_pattern) {
        this.exclusion_pattern = exclusion_pattern;
    }

    public String getInstallments_pattern() {
        return installments_pattern;
    }

    public void setInstallments_pattern(String installments_pattern) {
        this.installments_pattern = installments_pattern;
    }
}
