package sample;

import API.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import java.util.Optional;


import static API.Cart.Bought;
import static sample.Data.GenerateItems;
import static sample.Data.GenerateSerialize;

public class Main extends Application  {
    ComboBox<String> comboBox = new ComboBox();
    static public ComboBox<String> csvCB=new ComboBox<>();
    TableView<Item> tableView ;
    TableView<Item>ItemView;
    GridPane pane=new GridPane();
    FulfillmentCenterContainer warehosues;
    TextField itemInput=new TextField();
    TextField amountInput=new TextField();
    Alert alert = new Alert(Alert.AlertType.ERROR);
    static public Alert alertSerialize = new Alert(Alert.AlertType.ERROR);
    static public Alert alertEmptyWarehouse=new Alert(Alert.AlertType.ERROR);
    Alert SaveAlert=new Alert(Alert.AlertType.CONFIRMATION);
    Button exportCSV,importWH,importCart,SerializeButton,DeserializeButton,exportDataBase;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Warehouse Project");
        pane.setPadding(new Insets(10,10,10,10));
        pane.setVgap(8);
        pane.setHgap(8);

        //Wywalenie logow
        Logger log = Logger.getLogger("org.hibernate.SQL");
        log.setLevel(Level.DEBUG);

        alertSerialize.setTitle("Error with reading data from file!");
        alertSerialize.setContentText("Problem with file!");
        alertEmptyWarehouse.setTitle("Error with ProductList!");
        alertEmptyWarehouse.setContentText("Product List is empty!");
        SaveAlert.setTitle("Saving Data");
        SaveAlert.setContentText("Do you want to save data?");
        SaveOnExit(primaryStage);




        exportCSV=new Button("Export-CVS");
        importWH=new Button("Import WH-CVS");
        importCart=new Button("Import Cart");
        SerializeButton=new Button("Serialize");
        DeserializeButton=new Button("Deserialize");
        exportDataBase=new Button("Export DB");


        // getData=REcznie tworzone itemy i magazyny, getSerializedData- odczyt z pliku, getDataBase- z bazy danych SQL
        //HibernateAnnotationUtil HB=new HibernateAnnotationUtil();
        //warehosues=HB.getHibernate();
        warehosues=Data.getData();



        alert.setTitle("Error with buying!");
        comboBox.getItems().addAll("WH1","WH2","WH3","dowolny");
        csvCB.getItems().addAll("WH1","WH2","WH3","Cart");
        comboBox.setValue("dowolny");
        csvCB.setValue("Export/Import");
        comboBox.setOnAction(e->getTable());
        alert.setContentText("You typed not a number or number is bigger than amount. Try again");



        GridPane.setConstraints(comboBox,2,0);
        GridPane.setConstraints(itemInput,2,1);
        GridPane.setConstraints(amountInput,2,2);
        GridPane.setConstraints(csvCB,2,3);
        GridPane.setConstraints(importWH,2,6);
        GridPane.setConstraints(exportCSV,2,7);
        GridPane.setConstraints(importCart,2,8);
        GridPane.setConstraints(DeserializeButton,2,5);
        GridPane.setConstraints(SerializeButton,2,4);
        GridPane.setConstraints(exportDataBase,2,9);
        ButtonsActions();

        pane.getChildren().addAll(comboBox,itemInput,amountInput,csvCB,importWH,exportCSV,importCart,SerializeButton,DeserializeButton,exportDataBase);
        itemInput.setPromptText("Write name of Item to search");
        amountInput.setPromptText("Write amount to buy");
        tableView=getTable();
        ItemView=getTableItem();
        Scene scene = new Scene(pane);

        for(FulfillmentCenter fulfillmentCenter: FulfillmentCenterContainer.WareHouseList)
            System.out.println("ID OF" + fulfillmentCenter.getWarehouseName() + "  ID OF THAT" + fulfillmentCenter.getId());




        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void SaveOnExit(Stage primaryStage){
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Optional<ButtonType> result = SaveAlert.showAndWait();
                if(result.get() == ButtonType.OK){
                    for(FulfillmentCenter ful: FulfillmentCenterContainer.WareHouseList)
                        Serialize.serialize(ful.WarehouseName);
                }
            }
        });
    }


    private void getList(){
        FilteredList<Item> filteredData = new FilteredList<>(getItem(), p -> true);
        itemInput.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return product.getName().toLowerCase().contains(lowerCaseFilter);
            });

        });
        SortedList<Item> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

    private TableView<Item> getTable(){

        TableColumn<Item,String> column1 = new TableColumn<>("Name of Item");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Item,String> column5 = new TableColumn<>("Status of Item");
        column5.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Item, Double> column2 = new TableColumn<>("Price in $");
        column2.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Item, Double> column6 = new TableColumn<>("Weight");
        column6.setCellValueFactory(new PropertyValueFactory<>("weight"));


        TableColumn<Item,Integer> column7 = new TableColumn<>("Amount of Item");
        column7.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Item,String> column4 = new TableColumn<>("Name of Warehouse");
        column4.setCellValueFactory(new PropertyValueFactory<>("WHname"));

        TableColumn<Item,Integer> column3 = new TableColumn<>("ID of Item");
        column3.setCellValueFactory(new PropertyValueFactory<>("id"));



        tableView=new TableView<>();
        tableView.setItems(getItem());
        tableView.getColumns().addAll(column1,column2,column3,column4,column5,column6,column7);
        pane.getChildren().add(tableView);
        tableView.setMinWidth(643);
        GridPane.setConstraints(tableView,0,0,1,10);
        addButtonToTable();
        tultip(tableView);
        getList();
        return tableView;
    }


    private void tultip(TableView<Item> tv2){
        tv2.setRowFactory(tv -> new TableRow<>() {
            private Tooltip tooltip = new Tooltip();
            @Override
            public void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setTooltip(null);
                } else if (!item.getWHname().equals("Brak")){
                    FulfillmentCenter ful=FulfillmentCenterContainer.search(item.getWHname());
                    assert ful != null;
                    tooltip.setText("Name of Warehouse:  "+ful.WarehouseName+"\nPlace:  "+ful.place+"\nMaxSpace:  "+ful.MaxSpace+"\nActual Space:  "+ful.ActualSpace);
                    setTooltip(tooltip);
                }
            }
        });
    }

    private TableView<Item> getTableItem(){

        TableColumn<Item,String> column1 = new TableColumn<>("Name of Item");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Item, Double> column2 = new TableColumn<>("Price in $");
        column2.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Item,Integer> column3 = new TableColumn<>("Amount to buy");
        column3.setCellValueFactory(new PropertyValueFactory<>("bought"));

        TableColumn<Item,Integer> column4 = new TableColumn<>("ID of Item");
        column4.setCellValueFactory(new PropertyValueFactory<>("id"));


        ItemView=new TableView<>();
        ItemView.setItems(getCart());
        ItemView.getColumns().addAll(column1,column2,column3,column4);
        pane.getChildren().add(ItemView);
        GridPane.setConstraints(ItemView,1,0,1,10);
        tultip(ItemView);
        return ItemView;
    }

    private ObservableList<Item> getItem() {
        ObservableList<Item> items=FXCollections.observableArrayList();
        FulfillmentCenter F1 = FulfillmentCenterContainer.search( comboBox.getValue());

        if(!comboBox.getValue().equals("dowolny")) {
            assert F1 != null;
            items=FXCollections.observableArrayList(F1.ProductList);
        }
        else
            for(int i = 0; i< FulfillmentCenterContainer.WareHouseList.size(); i++){
                items.addAll(FulfillmentCenterContainer.WareHouseList.get(i).ProductList);
            }
        return items;
    }

    public static ObservableList<Item> getCart(){
        ObservableList<Item> items=FXCollections.observableArrayList();
        items.addAll(Bought);
        return items;
    }

    private void ButtonsActions() {
        importCart.setOnAction(e -> {
            GenerateItems();
            ItemView = getTableItem();
        });

        importWH.setOnAction(e -> {
            GenerateItems();
            tableView = getTable();
        });

        exportDataBase.setOnAction(e -> CSV.WriteToCSVFromDataBase());
        exportCSV.setOnAction(e -> CSV.writeToCSV(csvCB.getValue()));
        SerializeButton.setOnAction(e -> Serialize.serialize(csvCB.getValue()));

        DeserializeButton.setOnAction(e -> {
            GenerateSerialize();
            tableView = getTable();
            ItemView=getTableItem();
        });
    }

    private void addButtonToTable() {
       TableColumn<Item, Void> colBtn = new TableColumn("Button to buy");

       Callback<TableColumn<Item, Void>, TableCell<Item, Void>> cellFactory = new Callback<>() {
           @Override
           public TableCell<Item, Void> call(final TableColumn<Item, Void> param) {
               final TableCell<Item, Void> cell = new TableCell<>() {

                   private final Button btn = new Button("BUY");
                   {
                       btn.setOnAction((ActionEvent event) -> {
                           Item Item = getTableView().getItems().get(getIndex());
                           FulfillmentCenter F1= FulfillmentCenterContainer.search(Item.getWHname());
                           assert F1 != null;
                           String aname=amountInput.getText();
                           int am=Integer.parseInt(aname);
                           if(Item.getAmount()>am) {
                               Item.amount = Item.getAmount() - am;
                               if(Item.bought==0) {
                                   Bought.add(Item);
                                   Item.bought = Item.bought + am;
                               }
                               else{
                                   Item.bought=Item.bought+am;
                               }
                           }
                            else if(Item.getAmount()==am) {
                               F1.removeProduct(Item);
                               Bought.add(Item);
                               Item.bought =am;
                           }
                            else
                                try {
                                    alert.showAndWait();
                                }
                                catch (Exception e) {
                                    alert.showAndWait();
                                }
                           getTable();
                                getTableItem();
                       });
                   }
                   @Override
                   public void updateItem(Void item, boolean empty) {
                       super.updateItem(item, empty);
                       if (empty) {
                           setGraphic(null);
                       } else {
                           setGraphic(btn);
                       }
                   }
               };
               return cell;
           }
       };
       colBtn.setCellFactory(cellFactory);

       tableView.getColumns().add(colBtn);
   }
}
