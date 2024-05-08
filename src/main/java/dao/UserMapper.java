package dao;
import com.itheima.pojo.User;
public interface UserMapper {
    void addUser(User user);
    void deleteUser(int id);
    User getUserById(int id);
    void updateUser(User user);
}
