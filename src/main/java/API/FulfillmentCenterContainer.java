package API;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;



public class FulfillmentCenterContainer
{
    public static List<FulfillmentCenter>WareHouseList=new ArrayList<>();

    public void addCenter(FulfillmentCenter New)
    {
        WareHouseList.add(New);
    }
    public static void removeCenter(FulfillmentCenter deleted)
    {
        WareHouseList.remove(deleted);
    }

    public static FulfillmentCenter search(String Wname)
    {
        for (FulfillmentCenter ful : WareHouseList) {
            if (ful.WarehouseName.equals(Wname))
                return ful;
            }
        return null;
    }

    public static FulfillmentCenter searchID(int id)
    {
        for (FulfillmentCenter ful : WareHouseList) {
            if (ful.getId()==id)
                return ful;
        }
        return null;
    }

    public static boolean deleteWH(int id) {
        boolean status=false;
        for (FulfillmentCenter fulfillmentCenter : FulfillmentCenterContainer.WareHouseList) {
            if (fulfillmentCenter.getId() == id) {
                FulfillmentCenterContainer.removeCenter(fulfillmentCenter);
                status=true;
                break;
            }

        }
        return status;
    }



    public static void addCenterDataBase(FulfillmentCenter fulfillmentCenter, SessionFactory sessionFactory){
        Session session = sessionFactory.openSession();
        session.save(fulfillmentCenter);
        Transaction tx = session.beginTransaction();

        tx.commit();
        session.close();
    }

    public static void removeCenterDataBase(int fulfillmentCenterID, SessionFactory sessionFactory){
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        String hql ="DELETE FROM FulfillmentCenter s WHERE s.id="+fulfillmentCenterID;
        // String hql ="DELETE FROM FulfillmentCenter s WHERE s.id="+fulfillmentCenter.getID(); - ALTERNATYWA, gdy chcemy wyslac obiekt
        //  "SELECT c FROM Country c WHERE c.name = :name", Country.class);
        Query query = session.createQuery(hql);
        int count = query.executeUpdate();

        tx.commit();
        session.close();
    }

    public List<FulfillmentCenter> findWarehouse(String name,SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<FulfillmentCenter> criteria = cb.createQuery(FulfillmentCenter.class);
        Root<FulfillmentCenter> root = criteria.from(FulfillmentCenter.class);
        criteria.select(root);
        criteria.where(cb.equal(root.get("wh_name"), name));
        return session.createQuery(criteria).getResultList();
    }
}
