package xyz.marianomolina.melitest.model;

/**
 * Created by Mariano Molina on 11/4/16.
 * Twitter: @xsincrueldadx
 *
 * Issuer ResponseModel {
 *  "id": "297",
 *  "name": "Macro",
 *  "secure_thumbnail": "https://www.mercadopago.com/org-img/MP3/API/logos/297.gif",
 *  "thumbnail": "http://img.mlstatic.com/org-img/MP3/API/logos/297.gif"
 * }
 */
public class Issuer {
    private String id;
    private String name;
    private String secure_thumbnail;

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

    public String getSecure_thumbnail() {
        return secure_thumbnail;
    }

    public void setSecure_thumbnail(String secure_thumbnail) {
        this.secure_thumbnail = secure_thumbnail;
    }
}
