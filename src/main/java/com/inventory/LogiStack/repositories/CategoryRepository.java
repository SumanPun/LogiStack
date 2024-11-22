package com.inventory.LogiStack.repositories;

import com.inventory.LogiStack.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.id = ?1")
    Category findCategoryById(Long id);
}
