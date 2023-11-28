package com.vention.authorization_service.utils;

import org.springframework.beans.factory.annotation.Value;

public final class PropertyReader {
    @Value(value = "${user.roles.user}")
    public static String ROLE_USER;
}
