package com.capy.capyjara.auth.service.impl;

import com.capy.capyjara.admin.api.remote.TenantRemoteService;
import com.capy.capyjara.admin.api.remote.UserRemoteService;
import com.capy.capyjara.common.security.LoginUser;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public class BEndUserDetailServiceImpl implements UserDetailsService {

    private UserRemoteService userRemoteService;

    private TenantRemoteService tenantRemoteService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        SysUser sysUser = userRemoteService.getByUsername(username).getData();
//        if (Objects.isNull(sysUser)) {
//            throw new BusinessException(AuthResultEnum.NOT_FOUND_USER);
//        }

        //  attributes: nickname、avatar
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("nickname", "this is nickName");
        attributes.put("avatar", "this is avatar");
        List<String> roles = new ArrayList<>();
        roles.add("ADMIN");
        roles.add("NORMAL");

        return LoginUser.builder()
                .userId("test-id")
                .username("testUser")
                .tenantId("test-TenantId")
                .password("{noop}password")
                .roles(roles)
                .attributes(attributes)
                .enable(true)
                .locked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .build();
    }

}
