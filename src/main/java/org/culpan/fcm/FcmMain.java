package org.culpan.fcm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by harryculpan on 5/25/17.
 */
public class FcmMain  extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main-form.fxml"));
        primaryStage.setTitle("Fantasy Campaign Manager");
        primaryStage.setScene(new Scene(root, 744, 1203));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
