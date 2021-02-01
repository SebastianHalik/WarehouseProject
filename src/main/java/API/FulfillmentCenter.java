package API;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "wh_table")
public class FulfillmentCenter {

    static int idCounter = 0;
    @Id
    @Column(name = "wh_id")
    int id;
    @Column(name = "wh_name")
    public String WarehouseName;
    @Column(name = "max_space")
    public double MaxSpace;
    transient public List<Item> ProductList = new ArrayList<>();
    transient public double ActualSpace;
    @Column
    public String place;

    public FulfillmentCenter() {
    }

    public FulfillmentCenter(String name, double Space, String miejsce) {
        MaxSpace = Space;
        WarehouseName = name;
        place = miejsce;
        id = idCounter;
        idCounter++;
    }


    public Item SearchItemId(int ItemID) {
        for (Item item : ProductList) {
            if (item.getId() == ItemID)
                return item;
        }
        return null;
    }


    public void addProduct(Item item) {
        if (ActualSpace + item.weight * item.amount > MaxSpace)
            System.err.println("Full Warehouse!! Error!");
        else {
            boolean added = true;
            for (Item New : ProductList) {
                if (item.name.equals(New.name)) {
                    New.amount = New.amount + item.amount;
                    added = false;
                }
            }
            if (added) ProductList.add(item);
            ActualSpace = ActualSpace + item.weight * item.amount;
            item.WHname = this.WarehouseName;
        }
    }

    public static boolean AddProductByID(int WH_id, Item item) {

        FulfillmentCenter ful = FulfillmentCenterContainer.searchID(WH_id);
        if (ful != null) {
            ful.addProduct(item);
            return true;
        } else {
            return false;
        }
    }

    public static double getAverage(int item_id) {
        double average = 0;
        double counter = 0;
        for (FulfillmentCenter fulfillmentCenter : FulfillmentCenterContainer.WareHouseList)
            for (Item item : fulfillmentCenter.getProductList())
                if (item.getId() == item_id)
                    for (Rating rate : item.getRatingList()) {
                        average = average + rate.getMark();
                        counter++;
                    }

        if (counter != 0) {
            return average / counter;
        }
        else{
    return counter;
        }
    }

    public static boolean addRating(Rating rating,int item_id){
        for(FulfillmentCenter fulfillmentCenter: FulfillmentCenterContainer.WareHouseList)
            for(Item item: fulfillmentCenter.getProductList())
                if(item.getId()==item_id) {
                    item.getRatingList().add(rating);
                    return true;
                }
        return false;
    }

    public static void addItemDataBase(Item item, SessionFactory sessionFactory){
        Session session = sessionFactory.openSession();
        session.save(item);
        Transaction tx = session.beginTransaction();

        tx.commit();
        session.close();
    }

    public double getPercent(FulfillmentCenter fulfillmentCenter){

        if(fulfillmentCenter!=null)
            return fulfillmentCenter.getActualSpace()/100;
        else {
            return -1;
        }
    }

    public static void removeItemDataBase(int ItemID, SessionFactory sessionFactory){
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        String hql ="DELETE FROM Item s WHERE s.id="+ItemID;
        // String hql ="DELETE FROM Item s WHERE s.id="+Item.getID(); - ALTERNATYWA, gdy chcemy wyslac obiekt
        //  "SELECT c FROM Country c WHERE c.name = :name", Country.class);
        Query query = session.createQuery(hql);
        int count = query.executeUpdate();

        tx.commit();
        session.close();
    }

    public static boolean removeItemByID(int item_id){
        for( FulfillmentCenter fulfillmentCenter: FulfillmentCenterContainer.WareHouseList)
            for (Item item: fulfillmentCenter.getProductList())
                if(item.getId()==item_id)
                {
                    fulfillmentCenter.removeProduct(item);
                    return true;
                }
    return false;
    }

    public void removeProduct(Item item)
    {
        ActualSpace=ActualSpace-item.weight*item.amount;
        ProductList.remove(item);
    }

    public Item searchItem(String name)
    {
        for (Item item : ProductList) {
            if (compare(item.name, name) == 0)
                return item;

        }
        return null;
    }

    private int compare(String i1, String i2) {
        return i1.compareTo(i2);
    }

    public String getWarehouseName() {
        return WarehouseName;
    }

    public double getMaxSpace() {
        return MaxSpace;
    }

    public List<Item> getProductList() {
        return ProductList;
    }

    public double getActualSpace() {
        return ActualSpace;
    }

    public String getPlace() {
        return place;
    }

    public void setWarehouseName(String warehouseName) {
        WarehouseName = warehouseName;
    }

    public void setMaxSpace(double maxSpace) {
        MaxSpace = maxSpace;
    }

    public void setActualSpace(double actualSpace) {
        ActualSpace = actualSpace;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}