package br.com.rfs.tasksmanager.util;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {
    @Autowired
    EntityManager entityManager;

    @Transactional
    public void clean(String tableName) {
        entityManager.createNativeQuery("DELETE FROM " + tableName).executeUpdate();
    }
}