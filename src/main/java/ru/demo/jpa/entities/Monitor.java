package ru.demo.jpa.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@NoArgsConstructor
public class Monitor {

    @Builder
    public Monitor(LocalDateTime id, String status) {
        this.id = id;
        this.status = status;
    }

    @Id
    @Getter
    private LocalDateTime id;

    @Setter
    //@Builder.Default
    private String status = "brand-new";

    private Long persist;

    private Long update;

    private Long remove;

    @Transient
    private Long postPersist;

    @Transient
    private Long postUpdate;

    @Transient
    private Long postRemove;

    @Transient
    private Long postLoad;

    @PrePersist
    private void prePersist() {
        persist = System.currentTimeMillis();
    }

    @PostPersist
    private void postPersist() {
        postPersist = System.currentTimeMillis();
    }

    @PreUpdate
    private void preUpdate() {
        update = System.currentTimeMillis();
    }

    @PostUpdate
    private void postUpdate() {
        postUpdate = System.currentTimeMillis();
    }

    @PreRemove
    private void preRemove() {
        remove = System.currentTimeMillis();
    }

    @PostRemove
    private void postRemove() {
        postRemove = System.currentTimeMillis();
    }

    @PostLoad
    private void postLoad() {
        postLoad = System.currentTimeMillis();
    }


    public Optional<Long> getUpdateDelata() {
        return Optional.ofNullable(postUpdate).flatMap(t2 -> Optional.ofNullable(update).map(t1 -> t2 - t1));
    }

    public Optional<Long> getRemoveDelata() {
        return Optional.ofNullable(postRemove).flatMap(t2 -> Optional.ofNullable(remove).map(t1 -> t2 - t1));
    }

    public Optional<Long> getPersistDelata() {
        return Optional.ofNullable(postPersist).flatMap(t2 -> Optional.ofNullable(persist).map(t1 -> t2 - t1));
    }

    public Optional<Long> getPostLoad() {
        return Optional.ofNullable(postLoad);
    }
}
