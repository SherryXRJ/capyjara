package com.capy.capyjara.auth.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginAdminUser implements UserDetails, OAuth2AuthenticatedPrincipal {

    private Integer userId;

    private String username;

    @JsonIgnore
    private String password;

    private Collection<String> roles;

    private boolean locked;

    private boolean expired;

    private boolean enable;

    private Map<String, Object> attributes;

    private String token;

    private boolean admin;

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(roles)
                .orElse(Collections.emptyList())
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return !this.expired;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return this.enable;
    }

    @Override
    public String getName() {
        return this.username;
    }
}
