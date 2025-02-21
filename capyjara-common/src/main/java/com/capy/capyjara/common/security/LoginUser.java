package com.capy.capyjara.common.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements UserDetails, OAuth2User {

    private String userId;

    private String username;

    @JsonIgnore
    private String password;

    private Collection<String> roles;

    private Collection<String> apiPermissions;

    /**
     * 账号是否被锁定
     */
    private boolean locked;

    /**
     * 账号是否过期
     */
    private boolean accountExpired;

    /**
     * 账号是否可用
     */
    private boolean enable;

    /**
     *  密码是否过期
     */
    private boolean credentialsExpired;

    private Map<String, Object> attributes;

    private String tenantId;

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    /**
     * 返回用户角色以及api操作权限
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> roleSet = Optional.ofNullable(roles)
                .orElse(Collections.emptyList())
                .stream()
                .map(r -> "ROLE_" + r)
                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        Set<SimpleGrantedAuthority> authoritySet = Optional.ofNullable(apiPermissions).orElse(Collections.emptyList())
                .stream()
                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

        return List.of(roleSet, authoritySet)
                .stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }

    @Override
    public String getName() {
        return this.username;
    }
}
