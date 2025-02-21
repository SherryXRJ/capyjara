//package com.capy.capyjara.auth.service.impl;
//
//import com.capy.capyjara.auth.api.rpc.resp.ClientDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.core.oidc.OidcScopes;
//import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
//import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
//
//import java.time.Duration;
//
////@Service
//public class RegisteredClientServiceImpl implements RegisteredClientService {
//
//    private final static String ENCODE_TYPE = "{noop}";
//
//    @Autowired
//    private RegisteredClientRepository registeredClientRepository;
//
//    @Autowired
//    private OAuth2AuthorizationService authorizationService;
//
//
//
//
//    @Override
//    public void addClient(ClientDTO clientClientDTO) {
//        //添加到oauth2_registered_client
//        TokenSettings tokenSettings = TokenSettings.builder()
//                .refreshTokenTimeToLive(Duration.ofDays(7))
//                .accessTokenTimeToLive(Duration.ofHours(8))
//                .idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
//                .reuseRefreshTokens(false)
//                .build();
//        ClientSettings clientSettings = ClientSettings.builder()
//                .tokenEndpointAuthenticationSigningAlgorithm(SignatureAlgorithm.RS256)
//                .requireAuthorizationConsent(false)
//                .build();
//        RegisteredClient client = RegisteredClient.withId(clientClientDTO.getCode())
//                .clientId(clientClientDTO.getClientId())
//                .clientSecret(ENCODE_TYPE + clientClientDTO.getClientSecret())
//                .clientName(clientClientDTO.getName())
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .redirectUri(clientClientDTO.getRedirectUrl())
//                .scope("all")
//                .scope(OidcScopes.OPENID)
//                .scope(OidcScopes.PROFILE)
//                .clientSettings(clientSettings)
//                .tokenSettings(tokenSettings)
//                .build();
//        registeredClientRepository.save(client);
//
//    }
//
//}
