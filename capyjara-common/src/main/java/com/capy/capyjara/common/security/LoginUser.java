package com.capy.capyjara.common.security;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import java.util.stream.Stream;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements UserDetails, OAuth2User {

    private String userId;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Collection<String> roles;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * Api权限
     */
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


    /**
     * 将非敏感字段整理为OAuth2 attributes
     */
    @Override
    public Map<String, Object> getAttributes() {
        HashMap<String, Object> attributes = MapUtil.newHashMap();
        BeanUtil.beanToMap(this, attributes, CopyOptions.create().setIgnoreProperties("password"));
        return attributes;
    }

    /**
     * 返回用户角色以及api操作权限
     */
    @JsonIgnore
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

        return Stream.of(roleSet, authoritySet).flatMap(Collection::stream).collect(Collectors.toSet());
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
