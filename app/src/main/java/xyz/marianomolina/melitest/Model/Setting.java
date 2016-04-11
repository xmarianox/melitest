package xyz.marianomolina.melitest.model;

import java.util.List;

/**
 * Created by Mariano Molina on 11/4/16.
 * Twitter: @xsincrueldadx
 */
public class Setting {
    /**
     "bin": {
        "pattern": "^4",
        "exclusion_pattern": "^(487017)",
        "installments_pattern": "^4"
     },
     "card_number": {
        "length": 16,
        "validation": "standard"
     },
     "security_code": {
        "mode": "mandatory",
        "length": 3,
        "card_location": "back"
     }
     * */
    private Bin bin;
    private CardNumber cardNumber;
    private SecurityCode securityCode;

    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }

    public CardNumber getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(CardNumber cardNumber) {
        this.cardNumber = cardNumber;
    }

    public SecurityCode getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(SecurityCode securityCode) {
        this.securityCode = securityCode;
    }

    public static Setting getSettingByBin(List<Setting> settings, String bin) {
        Setting mSetting = null;

        if (settings != null && settings.size() > 0) {
            for (Setting setting : settings) {
                if (!"".equals(bin) && bin.matches(setting.getBin().getPattern() + ".*") &&
                        (setting.getBin().getExclusion_pattern() == null ||
                                !bin.matches(setting.getBin().getExclusion_pattern() + ".*"))) {
                    mSetting = setting;
                }
            }
        }

        return mSetting;
    }
}
