package com.example.football_api.security.oauth2;

import lombok.ToString;

import java.util.Map;
@ToString
public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        System.out.println(attributes);
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        System.out.println(attributes);
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }

}