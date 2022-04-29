package com.baska.RSE.Shedulers;


import com.baska.RSE.Models.Banned;
import com.baska.RSE.Repositories.BannedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@EnableScheduling
@Service
public class Unban {

    @Autowired
    BannedRepository bannedRepository;

    @Scheduled(cron = "10 * * * * *")
    public void unban(){
        List<Banned> banneds = bannedRepository.findAllByTime(Instant.now());
        System.out.println("asdasd]");
        for (Banned element : banneds){
            bannedRepository.delete(element);
        }

    }
}
