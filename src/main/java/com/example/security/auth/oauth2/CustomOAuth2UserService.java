package com.example.security.auth.oauth2;

import com.example.security.user.model.Role;
import com.example.security.user.model.RoleName;
import com.example.security.user.model.User;
import com.example.security.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  @Autowired
  private UserRepository userRepository;

  @Bean
  private OAuth2UserService<OidcUserRequest, OidcUser> oAuth2UserService() {
    final OidcUserService delegate = new OidcUserService();

    return (userRequest -> {
      OidcUser oidcUser = delegate.loadUser(userRequest);
      return processOidcUser(oidcUser);
    });

  }

  private OidcUser processOidcUser(OidcUser oidcUser) {
    String email = oidcUser.getEmail();
    String firstName = oidcUser.getGivenName();
    String lastName = oidcUser.getFamilyName();
    String avatar = oidcUser.getPicture();

    Optional<User> existingUser = userRepository.findByEmail(email);
    if(existingUser.isEmpty()) {
      User user = User.builder()
          .firstName(firstName)
          .lastName(lastName)
          .email(email)
          .avatar(avatar)
          .roles(List.of(Role.builder().name(RoleName.ROLE_USER).build())).build();

      userRepository.save(user);
    }

    return oidcUser;
  }
}
