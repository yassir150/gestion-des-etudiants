package com.projet.gestionetudiant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;


public class loginController{

    public AnchorPane main;
    @FXML
    private Button submit;

    @FXML
    private PasswordField txtpassword;

    @FXML
    private TextField txtuname;
    PreparedStatement pat;
    Connection conn;
    ResultSet rs;
    Stage Stage;


    @FXML
    void login(ActionEvent event) {
        String username = txtuname.getText();
        String password = txtpassword.getText();
        if(username.equals("") && password.equals("")){
            JOptionPane.showMessageDialog(null,"Please enter username and password");
        }else{
            try {
                conn = DriverManager.getConnection("jdbc:mysql://**put your database here**","**put your database username here**","**put your database password here**");
                pat = conn.prepareStatement("select * from users where username=? and password=?;");
                pat.setString(1,username);
                pat.setString(2,password);

                rs = pat.executeQuery();
                if(rs.next()){
                    try {
                        FXMLLoader fxmlLoa = new FXMLLoader(getClass().getResource("Home.fxml"));
                        Parent root = (Parent)fxmlLoa.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();
                        //this one is for closing
                        HomeController controller = fxmlLoa.getController();
                        controller.setName(username);
                        controller.setpassword(password);
                        stage.setTitle("Home");
                        stage.setFullScreen(false);
                        stage.resizableProperty().setValue(false);
                        Image img = new Image("student.png");
                        stage.getIcons().add(img);
                        stage.setOnCloseRequest(e -> {
                            e.consume();
                            logout(stage);
                        });
                        Stage = (Stage) txtpassword.getScene().getWindow();
                        Stage.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Invalid username or password");
                    txtuname.setText("");
                    txtpassword.setText("");
                    txtuname.requestFocus();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    protected void logout(Stage stage) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("do you want to log out?");
        alert.setContentText("do you want to save before logging out?");

        if (alert.showAndWait().get()== ButtonType.OK){
            System.out.println("you are logged out");
            stage.close();
        }
    }
}
