package sample;

import API.*;


import java.sql.*;


import static sample.Main.csvCB;


public class Data {
    private static final String URL="jdbc:mariadb://localhost:3306/items";
    private static final String USERNAME="manager";
    private static final String PASSWORD="1234";

    private static final String SQL_GET_ALL_ITEMS=
            "SELECT car_id,item_name,WH_name,Item_status,price,weight,amount FROM item_table";

    private static final String SQL_GET_ALL_WH=
            "SELECT wh_id,wh_name,max_space,place FROM wh_table";

    private static final String SQL_GET_ALL_RATE=
            "SELECT rate_id,mark,data,description,car_id FROM rate_table";


    public static FulfillmentCenterContainer getData() {
        FulfillmentCenterContainer warehouses=new FulfillmentCenterContainer();

        Item pc=new Item("PC", Itemstatus.INSTOCK,150,2,4000);
        Item phone=new Item("Phone",Itemstatus.INSTOCK,15,10,300);
        Item headphones=new Item("Headphones",Itemstatus.INSTOCK,5,7,50);
        Item citrus=new Item("Citrus",Itemstatus.INSTOCK,1,50,1);


        FulfillmentCenter WH1=new FulfillmentCenter("WH1",4000,"Warsaw");
        FulfillmentCenter WH2=new FulfillmentCenter("WH2",600,"New York");
        FulfillmentCenter WH3=new FulfillmentCenter("WH3",300,"Tokio");

        Date date1=new Date(2020,11,11);
        Date date2=new Date(2010,10,10);
        Date date3=new Date(2012,12,12);

        Rating rate1=new Rating(3,date1,"1st rate");
        Rating rate2=new Rating(1,date2,"2nd rate");
        Rating rate3=new Rating(5,date3,"3rd rate");

        pc.RatingList.add(rate1);
        pc.RatingList.add(rate2);
        phone.RatingList.add(rate3);

        warehouses.addCenter(WH1);
        warehouses.addCenter(WH2);
        warehouses.addCenter(WH3);


        WH1.addProduct(pc);
        WH2.addProduct(phone);
        WH1.addProduct(headphones);
        WH3.addProduct(citrus);

        return warehouses;
    }

    public static FulfillmentCenterContainer getSerializedData() {
        FulfillmentCenterContainer warehouses=new FulfillmentCenterContainer();
        FulfillmentCenter WH1=new FulfillmentCenter("WH1",400,"Warsaw");
        FulfillmentCenter WH2=new FulfillmentCenter("WH2",600,"New York");
        FulfillmentCenter WH3=new FulfillmentCenter("WH3",300,"Tokio");


        warehouses.addCenter(WH1);
        warehouses.addCenter(WH2);
        warehouses.addCenter(WH3);

        WH1.ProductList=Serialize.deserialize("WH1");
        WH2.ProductList=Serialize.deserialize("WH2");
        WH3.ProductList=Serialize.deserialize("WH3");


        return warehouses;
    }

    public static FulfillmentCenterContainer getWarehousesFromDataBase(){
        FulfillmentCenterContainer warehouses = new FulfillmentCenterContainer();
        try{

            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_GET_ALL_WH);
            int idx;
            while(rs.next()){
                idx=1;
                FulfillmentCenter WH = new FulfillmentCenter();
                WH.setId(rs.getInt(idx++));
                WH.setWarehouseName(rs.getString(idx++));
                WH.setMaxSpace(rs.getDouble(idx++));
                WH.setPlace(rs.getString(idx++));
                warehouses.addCenter(WH);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Problem with Reading WH form DataBase");
        }
        return warehouses;
    }

    public static FulfillmentCenterContainer getRating(){
        FulfillmentCenterContainer warehouses = getDataBase();
        try{
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_GET_ALL_RATE);
            int idx;
            while(rs.next()){
                idx=1;
                Rating rate=new Rating();
                rate.setId(rs.getInt(idx++));
                rate.setMark(rs.getInt(idx++));
                rate.setDate(rs.getDate(idx++));
                rate.setDescription(rs.getString(idx++));
                int item_id=rs.getInt(idx++);
                if(rate.getMark()>0 && rate.getMark() < 6)
                {
                    for(FulfillmentCenter WH : FulfillmentCenterContainer.WareHouseList)
                    {
                        Item item=WH.SearchItemId(item_id);
                        if(item!=null){
                            item.RatingList.add(rate);
                        }
                    }

                }

            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Problem with Reading Rating form DataBase");
        }
        return warehouses;
    }

    public static FulfillmentCenterContainer getDataBase() {
        FulfillmentCenterContainer warehouses = getWarehousesFromDataBase();
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL_GET_ALL_ITEMS);
            int idx;
            while (rs.next()) {
                idx = 1;
                Item item = new Item();
                item.setId(rs.getInt(idx++));
                item.setName(rs.getString(idx++));
                item.setWHname(rs.getString(idx++));
                String tmpName = item.getWHname();
                item.setStatus(rs.getString(idx++));
                item.setPrice(rs.getDouble(idx++));
                item.setWeight(rs.getDouble(idx++));
                item.setAmount(rs.getInt(idx++));
                FulfillmentCenter WHN = FulfillmentCenterContainer.search(tmpName);
                assert WHN != null;
                WHN.addProduct(item);
            }
            rs.close();
            stmt.close();
            conn.close();
        }
        catch (SQLException e){
        System.out.println("Database not connected. ERROR!!!!   ");}
        return warehouses;
    }

    public static void GenerateItems() {
        FulfillmentCenter ful=FulfillmentCenterContainer.search(csvCB.getValue());
        if(ful!=null)
        ful.ProductList= CSV.readFromCSV(Main.csvCB.getValue()+".csv");
        else
            Cart.Bought=CSV.readFromCSV("Cart.csv");
    }

    public static void GenerateSerialize(){
        FulfillmentCenter ful=FulfillmentCenterContainer.search(csvCB.getValue());
        if(ful!=null)
            ful.ProductList=Serialize.deserialize(Main.csvCB.getValue());
        else{
            Cart.Bought=Serialize.deserialize("cart.ser");
        }
    }
}
