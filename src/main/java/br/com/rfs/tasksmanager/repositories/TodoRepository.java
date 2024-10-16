package br.com.rfs.tasksmanager.repositories;

import br.com.rfs.tasksmanager.domain.constant.TodoStatus;
import br.com.rfs.tasksmanager.domain.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Query("""
            SELECT t FROM Todo t
            WHERE :status IS NULL OR t.status = :status
    """)
    Page<Todo> findByStatus(@Param("status") TodoStatus status, Pageable pageable);
}
