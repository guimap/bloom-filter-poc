package app.com.bloom.app.service;

import app.com.bloom.app.repositories.RedisBloomFilter;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class RedisBloomFilterService {
    private final RedisBloomFilter bloomFilter;


    public RedisBloomFilterService(
            @Value("${redis.host}") String redisHost,
            @Value("${redis.port}") int redisPort,
            @Value("${redis.password}") String redisPassword
    ) {
        Jedis jedis = new Jedis(redisHost, redisPort); // ou seu endpoint do ElastiCache
        jedis.auth(redisPassword);
        String redisKey = "bloom:usuarios";
        int bitSize = 48_000_000; // ~6 MB
        int hashCount = 7;
        this.bloomFilter = new RedisBloomFilter(jedis, redisKey, bitSize, hashCount);
    }


    public void add(String value) {
        bloomFilter.add(value);
    }

    public boolean mightContain(String value) {
        return bloomFilter.mightContain(value);
    }


}
