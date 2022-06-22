package com.khai.edu.knysh.provide_and_order_services.repository;

import com.khai.edu.knysh.provide_and_order_services.entity.WorkCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface WorkCategoryRepository extends CrudRepository<WorkCategory, Long> {

    List<WorkCategory> findAllByParentIsNull();

    List<WorkCategory> findAllBySpecialists_Id(Long specialistId);
}
