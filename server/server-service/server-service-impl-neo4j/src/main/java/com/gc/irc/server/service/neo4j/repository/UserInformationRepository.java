package com.gc.irc.server.service.neo4j.repository;


import org.springframework.data.neo4j.repository.GraphRepository;

import com.gc.irc.server.service.neo4j.model.UserInformationEntity;

public interface UserInformationRepository extends GraphRepository<UserInformationEntity> {


}
