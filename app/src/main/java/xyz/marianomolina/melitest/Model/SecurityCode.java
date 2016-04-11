package xyz.marianomolina.melitest.model;

/**
 * Created by Mariano Molina on 11/4/16.
 * Twitter: @xsincrueldadx
 */
public class SecurityCode {
    /*
    "security_code": {
        "mode": "mandatory",
        "length": 3,
        "card_location": "back"
    }
    */
    private String mode;
    private int length;
    private String card_location;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getCard_location() {
        return card_location;
    }

    public void setCard_location(String card_location) {
        this.card_location = card_location;
    }
}
