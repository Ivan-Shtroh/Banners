package org.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.test.entity.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long>,
        JpaSpecificationExecutor<Request> {
    List<Request> findAll();

}
