package com.statkovit.userservice.domain.base;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseIndexedEntity extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_gen")
    @Column(name = "id")
    private Long id;

    @Type(type = "pg-uuid")
    @Column(name = "uuid", nullable = false, updatable = false)
    private UUID uuid;

    @PrePersist
    private void initializeUUID() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseIndexedEntity that = (BaseIndexedEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid);
    }
}
