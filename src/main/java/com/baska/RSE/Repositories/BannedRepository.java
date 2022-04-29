package com.baska.RSE.Repositories;

import com.baska.RSE.Models.Banned;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BannedRepository extends JpaRepository<Banned,String> {

    @Query("select x from Banned as x where x.timestamp<=?1")
    List<Banned> findAllByTime(Instant time);
}
