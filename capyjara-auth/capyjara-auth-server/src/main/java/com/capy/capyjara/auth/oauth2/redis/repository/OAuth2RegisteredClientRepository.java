package com.capy.capyjara.auth.oauth2.redis.repository;


import com.capy.capyjara.auth.oauth2.redis.entity.OAuth2RegisteredClient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2RegisteredClientRepository extends CrudRepository<OAuth2RegisteredClient, String> {

    OAuth2RegisteredClient findByClientId(String clientId);

}
