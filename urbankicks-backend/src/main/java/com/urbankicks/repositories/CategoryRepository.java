package com.urbankicks.repositories;

import com.urbankicks.entities.Category;
import com.urbankicks.entities.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByCategoryNameAndGender(String categoryName, Gender gender);
}
