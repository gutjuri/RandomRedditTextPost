import java.io.IOException;
import java.net.URL;
import org.jsoup.Jsoup;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;

/**
 * 
 * @author Juri Dispan
 *
 */
public class RandomPost extends Application {

    private Parent root;

    @FXML
    private TextArea tarea;

    @FXML
    private TextField tfield;

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    private static String getRandomNudel(String subreddit) throws IOException {
        String url = "http://www.reddit.com/r/" + subreddit + "/random/.json";
        String json = Jsoup.connect(url).ignoreContentType(true).get().text();
        JsonParser parser = new JsonParser();
        JsonElement toplevel = parser.parse(json).getAsJsonArray().get(0).getAsJsonObject()
                        .get("data").getAsJsonObject().get("children").getAsJsonArray().get(0)
                        .getAsJsonObject().get("data").getAsJsonObject().get("selftext");
        String text = toplevel.getAsString();
        return text;
    }

    // TODO buggy
    @SuppressWarnings("unused")
    private static String removeSource(String str) {
        return str.substring(0, str.lastIndexOf("\n\n"));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxml = getClass().getResource("/mainMenu.fxml");

        FXMLLoader loader = new FXMLLoader(fxml);
        loader.setController(this);
        root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @FXML
    private void onGet(ActionEvent e) throws IOException {
        String post = getRandomNudel(tfield.getText());
        tarea.setText(post);
    }

    @FXML
    private void onCopy(ActionEvent e) throws IOException {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(tarea.getText());
        clipboard.setContent(content);
    }

    @FXML
    private void onExit(ActionEvent e) throws IOException {
        Platform.exit();
    }
}
