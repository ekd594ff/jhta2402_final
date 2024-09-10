package com.user.IntArea.repository;

import com.user.IntArea.entity.ImageDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<ImageDto, UUID> {

    Optional<ImageDto> findByRefId(UUID refId);

    void deleteByRefId(UUID refId);

    List<ImageDto> findAllByRefId(UUID refId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ImageDto i WHERE i.id NOT IN :ids")
    void deleteAllNotInId(List<UUID> ids);
}
