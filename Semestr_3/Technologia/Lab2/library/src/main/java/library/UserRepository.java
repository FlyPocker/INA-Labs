package library;

import java.util.List;

public interface UserRepository {
    User findUser(String name);

    List<User> findAllUsers();

    void saveUser(User user);
}
