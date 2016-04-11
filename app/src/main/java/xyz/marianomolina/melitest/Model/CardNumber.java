package xyz.marianomolina.melitest.model;

/**
 * Created by Mariano Molina on 11/4/16.
 * Twitter: @xsincrueldadx
 */
public class CardNumber {
    /*
    "card_number": {
        "length": 16,
        "validation": "standard"
    },
    */

    private int length;
    private String validation;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }
}
