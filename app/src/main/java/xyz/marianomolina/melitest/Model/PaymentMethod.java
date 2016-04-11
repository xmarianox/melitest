package xyz.marianomolina.melitest.model;

/**
 * Created by Mariano Molina on 9/4/16.
 * Twitter: @xsincrueldadx
 */
public class PaymentMethod {
    private String id;
    private String name;
    private String payment_type_id;
    private String secure_thumbnail;

    /**
     * PaymentMethod ResponseModel
     *
        "id": "visa",
        "name": "Visa",
        "payment_type_id": "credit_card",
        "status": "active",
        "secure_thumbnail": "https://www.mercadopago.com/org-img/MP3/API/logos/visa.gif",
        "thumbnail": "http://img.mlstatic.com/org-img/MP3/API/logos/visa.gif",
        "deferred_capture": "supported",
        "settings": [
            {
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
            }
        ],
        "additional_info_needed": [
            "cardholder_name",
            "cardholder_identification_type",
            "cardholder_identification_number"
        ],
        "min_allowed_amount": 0,
        "max_allowed_amount": 250000,
        "accreditation_time": 2880
    */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayment_type_id() {
        return payment_type_id;
    }

    public void setPayment_type_id(String payment_type_id) {
        this.payment_type_id = payment_type_id;
    }

    public String getSecure_thumbnail() {
        return secure_thumbnail;
    }

    public void setSecure_thumbnail(String secure_thumbnail) {
        this.secure_thumbnail = secure_thumbnail;
    }
}