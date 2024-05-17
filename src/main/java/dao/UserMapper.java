package dao;
import com.itheima.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    void addUser(User user);
    void deleteUser(int id);
    void updateUser(User user);
    List<Map<String, Object>> selectAllUser();

    //通过id查询
    User getUserById(int id);
    // 通过姓名查询用户
    List<User> getUserByName(String name);
    // 通过职位查询用户
    List<User> getUserByPosotion(String posotion);
}
