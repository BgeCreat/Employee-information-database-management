package CRUDUI;

import com.itheima.pojo.User;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class Crud extends Application {

    private SqlSessionFactory sqlMapper;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            sqlMapper = new SqlSessionFactoryBuilder().build(reader);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Label Label1 = new Label("id:");
        TextField Field1 = new TextField();

        Label Label2 = new Label("name:");
        TextField Field2 = new TextField();

        Label Label3 = new Label("age:");
        TextField Field3 = new TextField();

        Label Label4 = new Label("posotion:");
        TextField Field4 = new TextField();

        Button addButton = new Button("Add");
        Button searchButton = new Button("Search");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        // Button action
        addButton.setOnAction(event -> {
            String i = Field1.getText();
            int id = Integer.parseInt(i);
            String name = Field2.getText();
            String a = Field3.getText();
            int age = Integer.parseInt(a);
            String posotion = Field4.getText();

            addData(id, name, age, posotion);
            Field1.clear();
            Field2.clear();
            Field3.clear();
            Field4.clear();
        });

        searchButton.setOnAction(event -> {
            String idStr = Field1.getText();
            if (!idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    User user = searchData(id);
                    if (user != null) {
                        Field2.setText(user.getName());
                        Field3.setText(String.valueOf(user.getAge()));
                        Field4.setText(user.getPosotion());
                    } else {
                        showAlert("Not Found", "No user found with ID: " + id);
                        // 清空文本框
                        Field2.clear();
                        Field3.clear();
                        Field4.clear();
                    }
                } catch (NumberFormatException e) {
                    showAlert("Invalid input", "Please enter a valid ID.");
                }
            } else {
                showAlert("Invalid input", "Please enter an ID to search.");
            }
        });

        // Update按钮事件处理程序
        updateButton.setOnAction(event -> {
            String idStr = Field1.getText();
            if (!idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    String name = Field2.getText();
                    int age = Integer.parseInt(Field3.getText());
                    String position = Field4.getText();
                    updateData(id, name, age, position);
                } catch (NumberFormatException e) {
                    showAlert("Invalid input", "Please enter valid data for updating.");
                }
            } else {
                showAlert("Invalid input", "Please enter an ID to update.");
            }
        });


        // Delete按钮事件处理程序
        deleteButton.setOnAction(event -> {
            String idstr = Field1.getText();
            int id = Integer.parseInt(idstr);
            deleteData(id);
            Field1.clear();
        });


        // Layout
        GridPane root = new GridPane();
        root.add(Label1, 0, 0);
        root.add(Field1, 1, 0);
        root.add(Label2, 0, 1);
        root.add(Field2, 1, 1);
        root.add(Label3, 0, 2);
        root.add(Field3, 1, 2);
        root.add(Label4, 0, 3);
        root.add(Field4, 1, 3);
        root.add(addButton, 0, 4);
        root.add(searchButton, 1, 4);
        root.add(updateButton, 0, 5);
        root.add(deleteButton, 1, 5);


        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    private void addData(int id, String name, int age, String posotion ) {
        try (SqlSession session = sqlMapper.openSession()) {
            User user = new User(id, name, age, posotion);
            session.insert("addUser", user);
            session.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User searchData(int id) {
        try (SqlSession session = sqlMapper.openSession()) {
            User user = session.selectOne("getUserById", id);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to search user.");
            return null;
        }
    }


    // 删除员工信息
    private void deleteData(int id) {
        try (SqlSession session = sqlMapper.openSession()) {
            session.delete("deleteUser", id);
            session.commit();
            showAlert("Success", "删除元素成功");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete employee information.");
        }
    }
    private void updateData(int id, String name, int age, String position) {
        try (SqlSession session = sqlMapper.openSession()) {
            User user = new User(id, name, age, position);
            session.update("updateUser", user);
            session.commit();
            showAlert("Success", "Employee information updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update employee information.");
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
