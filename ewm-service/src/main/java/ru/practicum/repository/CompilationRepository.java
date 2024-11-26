package ru.practicum.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.compilation.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation,Long> {

    @Query(value = " select c" +
            "          from ru.practicum.model.compilation.Compilation c " +
            "         where (:pinned IS NULL or c.pinned = :pinned) ")
    List<Compilation> getCompilations(@Param("pinned") Boolean pinned);
}
