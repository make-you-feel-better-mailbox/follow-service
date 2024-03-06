package com.onetwo.followservice.domain;

import com.onetwo.followservice.adapter.out.persistence.entity.BaseEntity;
import lombok.Getter;

import java.time.Instant;

@Getter
public class BaseDomain {

    private Instant createdAt;

    private String createUser;

    private Instant updatedAt;

    private String updateUser;

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    protected void setMetaDataByEntity(BaseEntity entity) {
        this.setCreateUser(entity.getCreateUser());
        this.setCreatedAt(entity.getCreatedAt());
        this.setUpdateUser(entity.getUpdateUser());
        this.setUpdatedAt(entity.getUpdatedAt());
    }
}