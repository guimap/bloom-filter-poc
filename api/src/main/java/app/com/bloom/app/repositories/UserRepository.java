package app.com.bloom.app.repositories;

import app.com.bloom.app.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    List<User> findByEmail(String email);
}
