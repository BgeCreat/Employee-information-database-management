package Test;
import com.itheima.pojo.User;//导入User表实体类
import org.apache.ibatis.io.Resources;//用于加载MyBatis配置文件的资源
import org.apache.ibatis.session.SqlSession;//代表与数据库的会话，可以执行数据库操作
import org.apache.ibatis.session.SqlSessionFactory;//用于创建SqlSession的工厂类
import org.apache.ibatis.session.SqlSessionFactoryBuilder;//用于构建SqlSessionFactory实例的建造者类
import org.junit.Test;//用于标记测试方法的JUnit注解
import java.io.IOException;//导入输入输出异常类
import java.io.Reader;//读取字符流的抽象类
public class UserTest {
    @Test
    public void userFindByIdTest(){
        String resources = "mybatis-config.xml";
        Reader reader = null;
        try{
            reader = Resources.getResourceAsReader(resources);
            //读取文件内容到reader流中
        }catch (IOException e){
            e.printStackTrace();
        }
        SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
        //数据库工厂类
        SqlSession session = sqlMapper.openSession();
        //数据库操作类
        User user = session.selectOne("getUserById",1);
        //传入参数查询，返回结果
        System.out.println(user.getName());
        //输出结果
        session.close();
        //关闭数据库操作类
    }
}
