package com.pchab.JoParis2024.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pchab.JoParis2024.pojo.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Event findEventById(Long event_id);

}
