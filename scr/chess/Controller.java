package Chess;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;


public class Controller {
    @FXML
    Label a8;

    @FXML
    private GridPane board;
    ObservableList<Node> children;

    @FXML
    public void initialize() {
    }
}
