package app.com.bloom.app.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class RedisBloomFilter {

    private Jedis jedis;

    private final String redisKey;

    private final int bitSize;

    private final int hashCount;

    public RedisBloomFilter(Jedis jedis, String redisKey, int bitSize, int hashCount) {
        this.jedis = jedis;
        this.redisKey = redisKey;
        this.bitSize = bitSize;
        this.hashCount = hashCount;
    }

    public void add(String value) {
        for (int i = 0; i < hashCount; i++) {
            int position = hash(value, i);
            jedis.setbit(redisKey, position, true);
        }
    }

    public boolean mightContain(String value) {
        for (int i = 0; i < hashCount; i++) {
            int position = hash(value, i);
            if (!jedis.getbit(redisKey, position)) {
                return false;
            }
        }
        return true;
    }

    private int hash(String value, int seed) {
        CRC32 crc = new CRC32();
        crc.update((value + ":" + seed).getBytes(StandardCharsets.UTF_8));
        return Math.abs((int)(crc.getValue() % bitSize));
    }

}
