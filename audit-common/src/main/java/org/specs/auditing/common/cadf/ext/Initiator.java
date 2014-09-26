package org.specs.auditing.common.cadf.ext;

import org.specs.auditing.common.cadf.Resource;

public class Initiator extends Resource {
    public static final String TYPE_URI = "contrail:initiator";

    private String oauthAccessToken;

    public Initiator() {
        typeURI = TYPE_URI;
    }

    public String getOauthAccessToken() {
        return oauthAccessToken;
    }

    public void setOauthAccessToken(String oauthAccessToken) {
        this.oauthAccessToken = oauthAccessToken;
    }
}
