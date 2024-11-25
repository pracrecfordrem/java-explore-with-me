package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.category.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(nativeQuery = true,
            value = "select * " +
                     " from categories" +
                    " limit :size" +
                   " offset :from")
    List<Category> getCategories(@Param("from")Long from, @Param("size")Long size);

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);
}
