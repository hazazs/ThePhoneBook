package PhoneBook;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class PhoneBookViewController implements Initializable {
//<editor-fold defaultstate="collapsed" desc="Class variables">
    DataBase dataBase = new DataBase();
    private final ObservableList<Person> data = FXCollections.observableArrayList();
    private final TableColumn lastNameCol = new TableColumn("Last Name");
    private final TableColumn firstNameCol = new TableColumn("First Name");
    private final TableColumn emailCol = new TableColumn("E-mail");
    private final TableColumn deleteCol = new TableColumn("Delete");
    private final char[] illCharsList = {'<','>',':','"','/','\\','|','?','*'};
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="FXML items">
    @FXML
    private AnchorPane anchorPane;    
    @FXML
    private SplitPane mainSplit;
    @FXML
    private StackPane menuPane;
    @FXML
    private Pane contactPane, exportPane;
    @FXML
    private TableView table;
    @FXML
    private TextField inputFirstName, inputLastName, inputEmail, fileName;
//</editor-fold>
    @FXML
    private void addNewContact() {
        inputFirstName.setStyle("-fx-border-color:grey");
        inputLastName.setStyle("-fx-border-color:grey");
        inputEmail.setStyle("-fx-border-color:grey");
        if (inputFirstName.getText().length() > 0 && inputLastName.getText().length() > 0 && inputEmail.getText().length() >= 5 && inputEmail.getText().contains("@") && inputEmail.getText().contains(".")) {
            data.add(new Person(dataBase.createPerson(new Person(inputFirstName.getText(), inputLastName.getText(), inputEmail.getText())), inputFirstName.getText(), inputLastName.getText(), inputEmail.getText()));
            inputFirstName.clear();
            inputLastName.clear();
            inputEmail.clear();
        } else {
            if (inputFirstName.getText().length() == 0)
                inputFirstName.setStyle("-fx-border-color:red");
            if (inputLastName.getText().length() == 0)
                inputLastName.setStyle("-fx-border-color:red");
            if (inputEmail.getText().length() < 5 || !inputEmail.getText().contains("@") || !inputEmail.getText().contains("."))
                inputEmail.setStyle("-fx-border-color:red");
            alert("Something is wrong with the input field(s)!");
          }
    }
    @FXML
    private void generatePDF() {
        String fileNameString = fileName.getText().replaceAll("\\s+", "");
        int i = 0;
        while (fileNameString.length() > 0 && i != illCharsList.length && !fileNameString.contains(String.valueOf(illCharsList[i])))
            i++;
        if (i == illCharsList.length) {
            new GeneratePDF().generatePDF(fileNameString, data);
            fileName.setStyle("-fx-border-color:grey");
            fileName.clear();
        } else {
            fileName.setStyle("-fx-border-color:red");
            alert("Something is wrong with the file name!");
        }
    }
    private void setMenuData() {
        TreeItem<String> mainMenuRoot = new TreeItem<>("Main Menu");
        TreeView<String> treeView = new TreeView<>(mainMenuRoot);
        treeView.setShowRoot(false);
        treeView.setStyle("-fx-focus-color:transparent; -fx-faint-focus-color:transparent");
        TreeItem<String> nodeItemA = new TreeItem<>("Contacts");
        TreeItem<String> nodeItemB = new TreeItem<>("Exit");
        mainMenuRoot.getChildren().addAll(nodeItemA, nodeItemB);
        Node listNode = new ImageView(new Image(getClass().getResourceAsStream("/contacts.png")));
        Node exportNode = new ImageView(new Image(getClass().getResourceAsStream("/export.png")));
        TreeItem<String> nodeItemA1 = new TreeItem<>("List", listNode);
        TreeItem<String> nodeItemA2 = new TreeItem<>("Export..", exportNode);
        nodeItemA.getChildren().addAll(nodeItemA1, nodeItemA2);
        nodeItemA.setExpanded(true);
        menuPane.getChildren().add(treeView);
        treeView.getSelectionModel().select(nodeItemA1);
        treeView.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                    switch (selectedItem.getValue()) {
                        case "List" :
                            contactPane.setVisible(true);
                            exportPane.setVisible(false);
                            break;
                        case "Export.." :
                            exportPane.setVisible(true);
                            contactPane.setVisible(false);
                            break;
                        case "Exit" :
                            System.exit(0);
                            break;
                    }
                }
            }
        );
    }
    private void columnSettings(TableColumn tableColumn, double width, String column) {
        tableColumn.setMinWidth(width);
        tableColumn.setMaxWidth(width);
        tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        tableColumn.setCellValueFactory(new PropertyValueFactory<>(column));
    }
    private void setTableData() {
        columnSettings(lastNameCol, 121, "lastName");
        lastNameCol.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Person, String> t) {
                    if (t.getNewValue().length() > 0) {
                        ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setLastName(t.getNewValue());
                        dataBase.updatePerson((Person) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    } else {
                        table.refresh();
                      }
                }
            }
        );
        columnSettings(firstNameCol, 121, "firstName");
        firstNameCol.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Person, String> t) {
                    if (t.getNewValue().length() > 0) {
                        ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setFirstName(t.getNewValue());
                        dataBase.updatePerson((Person) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    } else {
                        table.refresh();
                      }
                }
            }
        );
        columnSettings(emailCol, 213, "email");
        emailCol.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<Person, String> t) {
                    if (t.getNewValue().length() >= 5 && t.getNewValue().contains("@") && t.getNewValue().contains(".")) {
                        ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmail(t.getNewValue());
                        dataBase.updatePerson((Person) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    } else {
                        table.refresh();
                      }
                }
            }
        );
        deleteCol.setMinWidth(51);
        deleteCol.setMaxWidth(51);
        table.getColumns().addAll(lastNameCol, firstNameCol, emailCol, deleteCol);
        Callback<TableColumn<Person, String>, TableCell<Person, String>> cellFactory = new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
            @Override
            public TableCell call(final TableColumn<Person, String> param) {
                final TableCell<Person, String> cell = new TableCell<Person, String>() {
                    final Button btn = new Button("DEL");
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(null);
                        if (empty)
                            setGraphic(null);
                        else {
                            btn.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    dataBase.deletePerson(getTableView().getItems().get(getIndex()));
                                    data.remove(getTableView().getItems().get(getIndex()));
                                }
                            });
                            setGraphic(btn);
                        }
                    }
                };
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        };
        deleteCol.setCellFactory(cellFactory);
        data.addAll(dataBase.readPersons());
        table.setItems(data);
    }
    private void disableSplitPaneDivider() {
        mainSplit.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                mainSplit.getDividers().get(0).setPosition(0.2);
            }
        });
    }
    private void alert(String alertFx) {
        mainSplit.setDisable(true);
        mainSplit.setOpacity(0.4);
        Button btn = new Button("OK");
        VBox box = new VBox(new Label(alertFx), btn);
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);
        StackPane sPane = new StackPane(box);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                mainSplit.setDisable(false);
                mainSplit.setOpacity(1);
                sPane.setVisible(false);
            }
        });
        anchorPane.getChildren().add(sPane);
        anchorPane.setLeftAnchor(sPane, 134.0);
        anchorPane.setRightAnchor(sPane, 0.0);
        anchorPane.setTopAnchor(sPane, 57.0);
        anchorPane.setBottomAnchor(sPane, 0.0);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setMenuData();
        setTableData();
        disableSplitPaneDivider();
    }
}