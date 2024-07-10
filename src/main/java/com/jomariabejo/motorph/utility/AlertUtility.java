package com.jomariabejo.motorph.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class AlertUtility {

    /**
     *
     * @param title
     * @param header
     * @param content
     */
    public static void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     *
     * @param title
     * @param header
     * @param content
     * @return
     */
    public static boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     *
     * @param title
     * @param header
     * @param content
     */
    public static void showInformation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Prompts the user to paste the path of the multi-employee registration file.
     *
     * @return the path entered by the user, or an empty string if cancelled
     */
    public static String showPathInputDialog() {
        // Create a TextInputDialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Paste the path of the multi-employee registration");
        dialog.setHeaderText(null); // No header
        dialog.setContentText("Please paste the path of the multi-employee registration:");

        // Get the user's input
        Optional<String> result = dialog.showAndWait();
        return result.orElse(""); // Return the input or an empty string if cancelled
    }

}
