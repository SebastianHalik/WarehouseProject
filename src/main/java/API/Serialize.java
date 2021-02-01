package API;

import sample.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Serialize {

    public static void serialize(String sname) {
        FulfillmentCenter WH = FulfillmentCenterContainer.search(sname);
        FileOutputStream file = null;
        try {
            if(WH!=null) {
                if(Main.csvCB.getValue()!=null)
                    if(!Main.csvCB.getValue().equals("Export/Import"))
                    file = new FileOutputStream(Main.csvCB.getValue()+".ser");
                else{
                    file=new FileOutputStream(WH.WarehouseName+".ser");
                }
            } else {
                file=new FileOutputStream("cart.ser");
            }

                ObjectOutputStream out = new ObjectOutputStream(file);

                if(WH!=null)
                    out.writeObject(WH.ProductList);
                else
                    out.writeObject(Cart.Bought);

                out.close();
                file.close();

                System.out.println("Object has been serialized");
            } catch (IOException ex) {
                System.out.println("IOException is caught");
            }
    }

    public static List<Item> deserialize(String sname) {
        FulfillmentCenter WH = FulfillmentCenterContainer.search(sname);
        List<Item> PL = new ArrayList<>();
        FileInputStream file;
        try {
                if(WH!=null) {
                    if(Main.csvCB.getValue()!=null)
                    file = new FileInputStream(Main.csvCB.getValue()+".ser");
                    else{
                        file=new FileInputStream(WH.WarehouseName+".ser");
                    }
                } else {
                    file=new FileInputStream("cart.ser");
                }

                ObjectInputStream in = new ObjectInputStream(file);

                PL = (List<Item>) in.readObject();
                if(PL.isEmpty())
                    Main.alertEmptyWarehouse.showAndWait();

            if(WH!=null){
                for (Item item : PL) {
                    item.status = Itemstatus.NEW;
                    WH.ActualSpace = WH.ActualSpace + item.weight * item.amount;

                }}
                in.close();
                file.close();

            } catch (IOException ex) {
                Main.alertSerialize.showAndWait();
            } catch (ClassNotFoundException ex) {
                System.out.println("ClassNotFoundException is caught");
            }
            return PL;
    }
}
