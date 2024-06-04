package com.bueno.persistence.dao;

import com.bueno.persistence.dto.RemoteBotEntity;
import com.bueno.persistence.dto.UserEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


public interface RemoteBotDao {
    RemoteBotEntity getByUuid(UUID uuid) throws SQLException;

    RemoteBotEntity getByName(String username) throws SQLException;

    List<RemoteBotEntity> getRemoteBotEntitiesByUser(UserEntity userEntity) throws SQLException;

    boolean existsByName(String botName) throws SQLException;

    void save(RemoteBotEntity bot) throws SQLException;

    void delete(RemoteBotEntity bot) throws SQLException;

    List<RemoteBotEntity> findAll() throws SQLException;
}
