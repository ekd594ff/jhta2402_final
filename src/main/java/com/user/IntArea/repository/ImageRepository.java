package com.user.IntArea.repository;

import com.user.IntArea.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends JpaRepository<Image, UUID> {

    Optional<Image> findByRefId(UUID refId);
    List<Image> findAllByRefId(UUID refId);
}
