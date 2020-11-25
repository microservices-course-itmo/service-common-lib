package com.wine.to.up.commonlib.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Auth {
  public static boolean isAnonymous() {
    return getUser().getRole().equals(HeadersAuthFilter.ROLE_ANONYMOUS);
  }

  public static User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (User) authentication.getPrincipal();
  }
}
