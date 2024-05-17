package CRUDUI;

import com.itheima.pojo.User;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import java.util.Map;

public class Inquire extends Application {

    private SqlSession sqlSession = MyBatisUtils.getSession();

    private TextField Field1;
    private TextField Field2;
    private TextField Field3;
    private TextField Field4;

    private HBox resultContainer;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //展示表中所有数据
        TableView<Map<String, Object>> tableView = new TableView<>();
        ObservableList<Map<String, Object>> data = FXCollections.observableArrayList();

        List<Map<String, Object>> dataList = sqlSession.selectList("selectAllUser");
        data.addAll(dataList);

        if (!dataList.isEmpty()) {
            Map<String, Object> sampleData = dataList.get(0);
            for (String columnName : sampleData.keySet()) {
                TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                column.setCellValueFactory(param -> {
                    Map<String, Object> row = param.getValue();
                    return javafx.beans.binding.Bindings.createObjectBinding(() -> row.get(columnName));
                });
                tableView.getColumns().add(column);
            }
        }
        tableView.setItems(data);

        //标签和按钮
        Label Label1 = new Label("id:");
        Field1 = new TextField();

        Label Label2 = new Label("name:");
        Field2 = new TextField();

        Label Label3 = new Label("age:");
        Field3 = new TextField();

        Label Label4 = new Label("posotion:");
        Field4 = new TextField();

        Button searchButton = new Button("Search");

        // 界面布局
        GridPane gp = new GridPane();
        gp.add(Label1, 0, 0);
        gp.add(Field1, 1, 0);
        gp.add(Label2, 2, 0);
        gp.add(Field2, 3, 0);
        gp.add(Label3, 4, 0);
        gp.add(Field3, 5, 0);
        gp.add(Label4, 6, 0);
        gp.add(Field4, 7, 0);
        resultContainer = new HBox();//显示查询结果的容器
        resultContainer.setSpacing(10); // 设置间距
        VBox root = new VBox();
        root.getChildren().addAll(tableView, gp, searchButton,resultContainer);

        // search按钮事件处理程序
        searchButton.setOnAction(event -> {
            String idStr = Field1.getText();
            String nameStr = Field2.getText(); // 获取用户输入的姓名
            String posotionStr = Field4.getText();

            //四选一
            if (!idStr.isEmpty()) {
                searchDataById(idStr);
            }
            else if (idStr.isEmpty() && !nameStr.isEmpty()) {
                searchDataByName(nameStr); // 调用根据姓名进行搜索的方法
            }
            else if(nameStr.isEmpty() && !posotionStr.isEmpty()){
                searchDataByposotion();
            }
            else {
                showAlert("无效输入", "请输入id或姓名进行检索.");
            }

        });


        //启动
        Scene scene = new Scene(root, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void searchDataById(String idStr) {
        try {
            int id = Integer.parseInt(idStr);//将字符串转化为整型
            User user = searchData(id);//返回查找出的类
            if (user != null) {//查找成功
                Field2.setText(user.getName());
                Field3.setText(String.valueOf(user.getAge()));//将整型转化为字符串
                Field4.setText(user.getPosotion());
                // 将查询结果添加到结果容器中
                resultContainer.getChildren().clear(); // 清空之前的结果
                resultContainer.getChildren().add(new Label("查询结果："));
                resultContainer.getChildren().add(new Label("ID：" + user.getId()));
                resultContainer.getChildren().add(new Label("姓名：" + user.getName()));
                resultContainer.getChildren().add(new Label("年龄：" + user.getAge()));
                resultContainer.getChildren().add(new Label("职位：" + user.getPosotion()));
            } else {//查找失败
                showAlert("查找失败", "没有找到id: " + id);
                // 清空文本框
                Field2.clear();
                Field3.clear();
                Field4.clear();
            }
        } catch (NumberFormatException e) {//字符串转化为数字异常
            e.printStackTrace();
            showAlert("报错", "NumberFormatException异常抛出");
        }
    }

    private void searchDataByName(String name) {
        try {
            List<User> userList = sqlSession.selectList("getUserByName", name); // 根据姓名从数据库中搜索用户列表
            if (!userList.isEmpty()) {//查找成功
                User user = userList.get(0); // 假设只返回第一个匹配的用户
                Field1.setText(String.valueOf(user.getId()));
                Field3.setText(String.valueOf(user.getAge()));//将整型转化为字符串
                Field4.setText(user.getPosotion());
                // 将查询结果添加到结果容器中
                resultContainer.getChildren().clear(); // 清空之前的结果
                resultContainer.getChildren().add(new Label("查询结果："));
                resultContainer.getChildren().add(new Label("ID：" + user.getId()));
                resultContainer.getChildren().add(new Label("姓名：" + user.getName()));
                resultContainer.getChildren().add(new Label("年龄：" + user.getAge()));
                resultContainer.getChildren().add(new Label("职位：" + user.getPosotion()));
            } else {//查找失败
                showAlert("查找失败", "没有找到姓名: " + name);
                // 清空文本框
                Field1.clear();
                Field3.clear();
                Field4.clear();
            }
        } catch (Exception e) {//处理异常
            e.printStackTrace();
            showAlert("报错", "数据库查找异常抛出");
        }
    }

    private void searchDataByposotion() {
        try {
            String posotionStr = Field4.getText(); // 获取用户输入的职位
            List<User> userList = sqlSession.selectList("getUserByPosotion", posotionStr); // 根据职位从数据库中搜索用户列表
            if (!userList.isEmpty()) { // 查找成功
                User user = userList.get(0); // 假设只返回第一个匹配的用户
                Field1.setText(String.valueOf(user.getId()));
                Field2.setText(user.getName());
                Field3.setText(String.valueOf(user.getAge()));
                // 将查询结果添加到结果容器中
                resultContainer.getChildren().clear(); // 清空之前的结果
                resultContainer.getChildren().add(new Label("查询结果："));
                resultContainer.getChildren().add(new Label("ID：" + user.getId()));
                resultContainer.getChildren().add(new Label("姓名：" + user.getName()));
                resultContainer.getChildren().add(new Label("年龄：" + user.getAge()));
                resultContainer.getChildren().add(new Label("职位：" + user.getPosotion()));
            } else { // 查找失败
                showAlert("查找失败", "没有找到职位: " + posotionStr);
                // 清空文本框
                Field1.clear();
                Field2.clear();
                Field3.clear();
            }
        } catch (Exception e) { // 处理异常
            e.printStackTrace();
            showAlert("报错", "数据库查找异常抛出");
        }
    }


    //用于从数据库中根据用户ID搜索并返回对应的用户信息
    private User searchData(int id) {
        try {
            User user = sqlSession.selectOne("getUserById", id);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("报错", "数据库查找异常抛出");
            return null;
        }
    }

    // 显示警报
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
