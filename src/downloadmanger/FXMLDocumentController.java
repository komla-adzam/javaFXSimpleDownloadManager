/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package downloadmanger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 *
 * @author kbee
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private JFXButton closeButton;

    @FXML
    private JFXTextField downloadAddressTextField;

    @FXML
    private JFXTextField fileNameTextField;

    @FXML
    private JFXButton downloadButton;

    @FXML
    private JFXProgressBar downloadProgressBar;

    @FXML
    private Label downloadLabel;

    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == closeButton) {
            System.exit(0);
        }

        URL url = null;

        try {
            url = new URL(downloadAddressTextField.getText());

        } catch (Exception ex) {
            downloadAddressTextField.setText("");
            fileNameTextField.setText("");
            Shaker s1 = new Shaker(downloadAddressTextField);
            Shaker s2 = new Shaker(fileNameTextField);
            s1.shake();
            s2.shake();
        }

        String fileExtension = url.getFile().substring(url.getFile().lastIndexOf("."));
        String filename = "Download" + fileExtension;
        if (!fileNameTextField.getText().equals("")) {
            filename = fileNameTextField.getText() + fileExtension;
        }

        DownloadTask downloader = new DownloadTask(url, filename);

        downloadProgressBar.progressProperty().bind(downloader.progressProperty());
        downloadLabel.textProperty().bind(downloader.messageProperty());

        executor.submit(downloader);

        fileNameTextField.clear();
        downloadAddressTextField.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}

// This class is to handle vibrating effect when you enter a wrong url
class Shaker {

    private TranslateTransition tt;

    public Shaker(Node node) {
        tt = new TranslateTransition(Duration.millis(50), node);
        tt.setFromX(0f);
        tt.setByX(10f);
        tt.setCycleCount(4);
        tt.setAutoReverse(true);
    }

    public void shake() {
        tt.playFromStart();
    }
}
