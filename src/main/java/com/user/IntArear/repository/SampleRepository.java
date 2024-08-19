package com.user.IntArear.repository;

import com.user.IntArear.entity.Sample;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SampleRepository {

    private final EntityManager em;

    public void save(Sample sample) {
        em.persist(sample);
    }

    public Sample findOne(Long id) {
        return em.find(Sample.class, id);
    }

    public List<Sample> findAll() {
        return em.createQuery("select m from Sample  s", Sample.class)
                .getResultList();
    }
}
