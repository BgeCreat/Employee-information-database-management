package dao;
import com.itheima.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    void addUser(User user);
    void deleteUser(int id);
    User getUserById(int id);
    void updateUser(User user);
    List<Map<String, Object>> selectAllUser();

}
