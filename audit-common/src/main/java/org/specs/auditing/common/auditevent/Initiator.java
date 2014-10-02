package org.specs.auditing.common.auditevent;

public class Initiator {

    private String id;
    private String type;
    private String oauthAccessToken;
    private String certFingerprint;

    public Initiator() {
    }

    public Initiator(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOauthAccessToken() {
        return oauthAccessToken;
    }

    public void setOauthAccessToken(String oauthAccessToken) {
        this.oauthAccessToken = oauthAccessToken;
    }

    public String getCertFingerprint() {
        return certFingerprint;
    }

    public void setCertFingerprint(String certFingerprint) {
        this.certFingerprint = certFingerprint;
    }
}
