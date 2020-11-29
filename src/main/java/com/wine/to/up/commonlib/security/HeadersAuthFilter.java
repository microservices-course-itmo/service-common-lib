package com.wine.to.up.commonlib.security;

import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * HeadersAuthFilter parses "id" and "role" HTTP headers which are set by Api Gateway and authenticates user
 * if headers are present in the request, anonymous user is created otherwise.
 *
 * @see <a href="https://www.notion.so/097028bcc9004428b65852f13c3211af">Full documentation on notion.so</a>
 */
public class HeadersAuthFilter extends OncePerRequestFilter {
  private static final String ID_HEADER_NAME = "id";
  private static final String ROLE_HEADER_NAME = "role";
  public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    String idHeader = request.getHeader(ID_HEADER_NAME);
    String roleHeader = request.getHeader(ROLE_HEADER_NAME);

    Authentication token;
    if (!StringUtils.isEmpty(idHeader) && !StringUtils.isEmpty(roleHeader) ) {
      User user = new User()
          .setId(Long.parseLong(idHeader))
          .setRole(roleHeader);

      token = new PreAuthenticatedAuthenticationToken(
          user,
          null,
          Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
      );
    } else {
      token = new AnonymousAuthenticationToken(
          "guest",
          new User().setId(-1L).setRole(ROLE_ANONYMOUS),
          Collections.singletonList(new SimpleGrantedAuthority(ROLE_ANONYMOUS))
      );
    }

    token.setAuthenticated(true);
    SecurityContextHolder.getContext().setAuthentication(token);

    chain.doFilter(request, response);
  }
}
