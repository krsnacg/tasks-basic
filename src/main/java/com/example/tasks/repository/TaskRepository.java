package com.example.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tasks.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
}
