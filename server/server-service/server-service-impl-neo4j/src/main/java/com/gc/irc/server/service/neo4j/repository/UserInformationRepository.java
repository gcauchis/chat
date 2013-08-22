package com.gc.irc.server.service.neo4j.repository;


import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.transaction.annotation.Transactional;

import com.gc.irc.server.service.neo4j.model.UserInformationEntity;

@Transactional
public interface UserInformationRepository extends GraphRepository<UserInformationEntity> {

}
