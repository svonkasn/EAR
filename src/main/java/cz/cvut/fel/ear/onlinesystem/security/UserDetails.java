package cz.cvut.fel.ear.onlinesystem.security;


import cz.cvut.fel.ear.onlinesystem.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails{

  private final User user;
  private final Set<GrantedAuthority> authorities;

  public UserDetails(User user) {
    Objects.requireNonNull(user);
    this.user = user;
    this.authorities = new HashSet<>();
    // Add user Role
    authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));
  }

  public UserDetails(User user, Set<GrantedAuthority> authorities) {
    this.user = user;
    this.authorities = authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return org.springframework.security.core.userdetails.UserDetails.super.isAccountNonExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return org.springframework.security.core.userdetails.UserDetails.super.isAccountNonLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return org.springframework.security.core.userdetails.UserDetails.super.isCredentialsNonExpired();
  }

  @Override
  public boolean isEnabled() {
    return org.springframework.security.core.userdetails.UserDetails.super.isEnabled();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.unmodifiableCollection(authorities);
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  public User getUser() {
    return user;
  }
}
