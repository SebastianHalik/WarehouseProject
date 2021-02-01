package API;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.springframework.http.HttpHeaders;
import sample.Main;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CSV {
    private static final String CSV_SEPARATOR = ",";

    public static void writeToCSV(String sname) {
        FulfillmentCenter WareHouse = FulfillmentCenterContainer.search(sname);
        List<Item> CartList=Main.getCart();
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Main.csvCB.getValue()+".csv")));
            if(WareHouse!=null) {
                for (Item product : WareHouse.ProductList) {
                    String oneLine = (product.getName().trim().length() == 0 ? "" : product.getName()) +
                            CSV_SEPARATOR +
                            (product.getAmount() < 0 ? "" : product.getWeight()) +
                            CSV_SEPARATOR +
                            (product.getAmount() < 0 ? "" : product.getAmount()) +
                            CSV_SEPARATOR +
                            (product.getPrice() < 0 ? "" : product.getPrice()) +
                            CSV_SEPARATOR +
                            (product.getWHname().trim().length() == 0 ? "" : product.getWHname()) +
                            CSV_SEPARATOR+
                            (product.getBought() < 0 ? "" : product.getBought()) +
                            CSV_SEPARATOR+
                            (product.getId() < 0 ? "" : product.getId());
                    bw.write(oneLine);
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            }
            else
                for(Item product: CartList) {
                    String oneLine = (product.getName().trim().length() == 0 ? "" : product.getName()) +
                            CSV_SEPARATOR +
                            (product.getAmount() < 0 ? "" : product.getWeight()) +
                            CSV_SEPARATOR +
                            (product.getAmount() < 0 ? "" : product.getAmount()) +
                            CSV_SEPARATOR +
                            (product.getPrice() < 0 ? "" : product.getPrice()) +
                            CSV_SEPARATOR +
                            (product.getWHname().trim().length() == 0 ? "" : product.getWHname()) +
                            CSV_SEPARATOR+
                            (product.getBought() < 0 ? "" : product.getBought()) +
                            CSV_SEPARATOR+
                            (product.getId() < 0 ? "" : product.getId());
                    bw.write(oneLine);
                    bw.newLine();
                }
            bw.close();
                } catch (IOException e) {
            e.printStackTrace();
            System.out.println("CSV SAVED");
        }
    }

    public static void WriteAllItemsToCSV(){
        try{
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("AllItems.csv")));
            for(FulfillmentCenter fulfillmentCenter: FulfillmentCenterContainer.WareHouseList)
                for( Item product : fulfillmentCenter.getProductList()){
                    String oneLine = (product.getName().trim().length() == 0 ? "" : product.getName()) +
                            CSV_SEPARATOR +
                            (product.getAmount() < 0 ? "" : product.getWeight()) +
                            CSV_SEPARATOR +
                            (product.getAmount() < 0 ? "" : product.getAmount()) +
                            CSV_SEPARATOR +
                            (product.getPrice() < 0 ? "" : product.getPrice()) +
                            CSV_SEPARATOR +
                            (product.getWHname().trim().length() == 0 ? "" : product.getWHname()) +
                            CSV_SEPARATOR+
                            (product.getBought() < 0 ? "" : product.getBought()) +
                            CSV_SEPARATOR+
                            (product.getId() < 0 ? "" : product.getId());
                    bw.write(oneLine);
                    bw.newLine();
                }
            bw.close();
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("All Items saved");
        }
    }

    public static void WriteToCSVFromDataBase() { //Zapis wszystkich produktow z bazy do pliku
        try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ProductList.csv")));
                Class.forName("org.mariadb.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/items", "manager", "1234");
                Statement stmt = con.createStatement();

                String q1 = "select * from item_table";
                ResultSet rs = stmt.executeQuery(q1);
                while (rs.next()) {
                    String oneLine=rs.getString(1)+CSV_SEPARATOR+rs.getString(2)+
                            CSV_SEPARATOR+rs.getString(3)+
                            CSV_SEPARATOR+rs.getString(4)+
                            CSV_SEPARATOR+rs.getString(5)+
                            CSV_SEPARATOR+rs.getString(6)+
                            CSV_SEPARATOR+rs.getString(7);
                    bw.write(oneLine);
                    bw.newLine();
                }
                bw.flush();
                bw.close();
                con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static List<Item> readFromCSV(String filename){
        List<Item> items=new ArrayList<>();
        Path pathToFile=Paths.get(filename);
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)){
            String line = br.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                Item item=createItem(attributes);
                items.add(item);
                line=br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        System.out.println("CSV READ");
        return items;
    }

    public static void DownloadCSV(HttpServletResponse response) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
        String filename = "AllItems.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        StatefulBeanToCsv<Item> writer = new StatefulBeanToCsvBuilder<Item>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();
        for(FulfillmentCenter fulfillmentCenter: FulfillmentCenterContainer.WareHouseList)
            writer.write(fulfillmentCenter.ProductList);
    }

    public static Item createItem(String[]metadata){
        Item item=new Item();
        item.setName(metadata[0]);
        item.setWeight(Double.parseDouble(metadata[1]));
        item.setAmount(Integer.parseInt(metadata[2]));
        item.setPrice(Double.parseDouble(metadata[3]));
        item.setWHname(metadata[4]);
        item.setBought(Integer.parseInt(metadata[5]));
        item.setStatus("NEW");

        FulfillmentCenter WH=FulfillmentCenterContainer.search(item.getWHname());
        Item nowyItem=WH.searchItem(item.getName());
        if(nowyItem!=null){
            item.setId(nowyItem.getId());
        }
        return item;
    }
}

