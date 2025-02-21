package com.capy.capyjara.auth.jackson;

import com.capy.capyjara.common.security.LoginUser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 反序列化Redis中的Session信息
 */
public class LoginUserDeserializer extends JsonDeserializer<LoginUser> {

    private static final TypeReference<Collection<SimpleGrantedAuthority>> SIMPLE_GRANTED_AUTHORITY_SET = new TypeReference<>() {
    };

    private static final TypeReference<List<String>> LIST_TYPE_REFERENCE = new TypeReference<>() {
    };

    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {
    };

    @Override
    public LoginUser deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode jsonNode = mapper.readTree(jsonParser);

//        Collection<? extends GrantedAuthority> authorities = mapper.convertValue(jsonNode.get("authorities"), SIMPLE_GRANTED_AUTHORITY_SET);
        List<String> roles = mapper.convertValue(jsonNode.get("roles"), LIST_TYPE_REFERENCE);
        Map<String, Object> attributes = mapper.convertValue(jsonNode.get("attributes"), MAP_TYPE_REFERENCE);

        Integer userId = jsonNode.get("userId").asInt();
        String username = jsonNode.get("username").asText();
//        String password = jsonNode.get("password").asText("");
        boolean enable = jsonNode.get("enable").asBoolean();
//        boolean expired = jsonNode.get("expired").asBoolean();
        boolean locked = jsonNode.get("locked").asBoolean();

        return LoginUser.builder()
//                .userId(userId)
                .username(username)
//                .password(password)
                .enable(enable)
                .accountExpired(false)
                .credentialsExpired(false)
                .locked(locked)
                .roles(roles)
                .attributes(attributes)
                .build();
    }
}
