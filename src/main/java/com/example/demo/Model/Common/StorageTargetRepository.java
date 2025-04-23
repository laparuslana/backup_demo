package com.example.demo.Model.Common;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StorageTargetRepository extends JpaRepository<StorageTarget, Long> {
    Optional<StorageTarget> findByName(String name);
}
