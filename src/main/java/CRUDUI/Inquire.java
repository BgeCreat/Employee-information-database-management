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
        TextField Field1 = new TextField();

        Label Label2 = new Label("name:");
        TextField Field2 = new TextField();

        Label Label3 = new Label("age:");
        TextField Field3 = new TextField();

        Label Label4 = new Label("posotion:");
        TextField Field4 = new TextField();

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
        HBox resultContainer = new HBox();//显示查询结果的容器
        resultContainer.setSpacing(10); // 设置间距
        VBox root = new VBox();
        root.getChildren().addAll(tableView, gp, searchButton,resultContainer);

        // search按钮事件处理程序
        searchButton.setOnAction(event -> {
            String idStr = Field1.getText();
            if (!idStr.isEmpty()) {
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
            } else {
                showAlert("无效输入", "请输入id进行检索.");
            }
        });

        //启动
        Scene scene = new Scene(root, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

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
