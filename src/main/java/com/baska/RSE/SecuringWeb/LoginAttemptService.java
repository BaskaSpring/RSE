package com.baska.RSE.SecuringWeb;

import com.baska.RSE.Models.Banned;
import com.baska.RSE.Repositories.BannedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Optional;

@Service
public class LoginAttemptService {

    @Autowired
    BannedRepository bannedRepository;

    private final int MAX_ATTEMPT = 10;

    public void loginSucceeded(String key) {
        Optional<Banned> banned = bannedRepository.findById(key);
        if (banned.isPresent()){
            Banned b = banned.get();
            bannedRepository.delete(b);
        }
    }

    public void loginFailed(String key) {
        int attempts = 0;
        Optional<Banned> banned = bannedRepository.findById(key);
        if (banned.isPresent()) {
            Banned b = banned.get();
            attempts = b.getCount();
            attempts++;
            b.setCount(attempts);
            b.setTimestamp(Instant.now().plusSeconds(60*15));
            bannedRepository.save(b);
        } else {
            attempts++;
            Banned newBanned = new Banned();
            newBanned.setIp(key);
            newBanned.setTimestamp(Instant.now().plusSeconds(60*15));
            newBanned.setCount(attempts);
            bannedRepository.save(newBanned);
        }
    }

    public boolean isBlocked(String key) {
        Optional<Banned> banned = bannedRepository.findById(key);
        if (banned.isPresent()) {
            Banned b = banned.get();
            return (b.getCount() >= MAX_ATTEMPT);
        } else return false;
    }
}
