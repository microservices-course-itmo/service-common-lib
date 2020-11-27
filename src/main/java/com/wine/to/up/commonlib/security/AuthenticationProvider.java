package com.wine.to.up.commonlib.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * The AuthenticationProvider represents information about current user's session
 *
 * @see <a href="https://www.notion.so/097028bcc9004428b65852f13c3211af">Full documentation on notion.so</a>
 */
public class AuthenticationProvider {
  /**
   * Tests if current user is anonimous
   * @return true if current user is anonimous
   */
  public static boolean isAnonymous() {
    return getUser().getRole().equals(HeadersAuthFilter.ROLE_ANONYMOUS);
  }

  /**
   * Returns User entity of current user, logged in to the service
   * @return currently logged in user's entity
   */
  public static User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (User) authentication.getPrincipal();
  }
}
