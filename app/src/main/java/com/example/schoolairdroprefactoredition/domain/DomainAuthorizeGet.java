package com.example.schoolairdroprefactoredition.domain;

public class DomainAuthorizeGet {

    @Override
    public String toString() {
        return "DomainAuthorizeGet{" +
                "public_key='" + public_key + '\'' +
                ", session_id='" + session_id + '\'' +
                '}';
    }

    /**
     * public_key : -----BEGIN PUBLIC KEY-----
     MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA7hh7JeO4xQxD6VbI1AN0
     0Gg3gLfuE+oVL3JxnegnTSbSfHRh7necMR2TCQeSvK97fYq3nbeDm9t6oHmt8BGb
     Ad/wpXpFQR77mAnXTSnikF4XU3xlbaUqmHhhmLJQm3i3HzXYhtzfjQxJhFvdEHfs
     D/URNYjx6zooWttHlMtFtMvRJrOKcsxMrdm4PAtKd/XafiYiHSSH49TWNXsLigOA
     uUcwsXdDcGfBOhhJVS4BBd/oo4OJaEIzhdPrGg/GJ3cY5d2ZbaptJfXHurVHudy/
     SsZd5tmBLen05XKBcueRyCIfIkZcp9KkoGLIcT8IiAeAjhanXcRl6RHmxL57v3GY
     UwIDAQAB
     -----END PUBLIC KEY-----
     * session_id : 1u4c5or0du8daf9mn6ns86nja9
     */



    private String public_key;
    private String session_id;

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}
