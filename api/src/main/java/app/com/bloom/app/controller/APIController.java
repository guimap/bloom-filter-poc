package app.com.bloom.app.controller;

import app.com.bloom.app.entity.User;
import app.com.bloom.app.repositories.UserRepository;
import app.com.bloom.app.service.RedisBloomFilterService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;




@RestController
public class APIController {

    @Autowired
    private UserRepository repository;

    Logger log = LoggerFactory.getLogger(APIController.class);

    @Autowired
    private RedisBloomFilterService redisBloomFilterService;

    @GetMapping("/api/no-bloom")
    public ResponseEntity<List<User>> getUserWithouBloom(@RequestParam String email) {
        List<User> userList = repository.findByEmail(email);
        if (userList.isEmpty()) {
            log.warn("Nenhum dado encontrado");
            return ResponseEntity.noContent().build();
        }
        log.info("Dados encontrados - qtd {}", userList.size());
        return ResponseEntity.status(HttpStatus.OK)
                .body(userList);
    }

    @GetMapping("/api/bloom")
    public ResponseEntity<List<User>> getUserWithloom(@RequestParam String email) {
        log.info("Verificando se existe no redis");
        boolean isExistsEmail = redisBloomFilterService.mightContain(email);
        log.info("Email existe? {}", isExistsEmail);
        if (isExistsEmail) {
            List<User> userList = repository.findByEmail(email);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(userList);
        } else {
            log.warn("Nenhum dado encontrado");
            return ResponseEntity.noContent().build();
        }
    }
}
