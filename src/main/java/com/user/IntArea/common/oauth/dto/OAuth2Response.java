package com.user.IntArea.common.oauth.dto;

import com.user.IntArea.entity.enums.Platform;

public interface OAuth2Response {

    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();

    Platform getPlatform();
}
