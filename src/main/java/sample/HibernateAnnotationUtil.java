package sample;

import API.FulfillmentCenter;
import API.FulfillmentCenterContainer;
import API.Item;
import API.Rating;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HibernateAnnotationUtil {
    public static SessionFactory sessionFactory; //public static, abym mogl latwo testowac funkcje dodawania magazynu do bazy etc

    public HibernateAnnotationUtil() {
        setUp();
    }


    private void setUp() {

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {

            StandardServiceRegistryBuilder.destroy(registry);
            e.printStackTrace();
        }
    }

    public FulfillmentCenterContainer getHibernate(){
        FulfillmentCenterContainer warehouses=new FulfillmentCenterContainer();
        Session session=sessionFactory.openSession();
        List<Item> items= session.createQuery("from Item", Item.class).list();
        FulfillmentCenterContainer.WareHouseList =session.createQuery("from FulfillmentCenter",FulfillmentCenter.class).list();
        List<Rating> rates=session.createQuery("from Rating ",Rating.class).list(); //ostatecznie niepotrzebne, ale zostawiam
        for( FulfillmentCenter ful: FulfillmentCenterContainer.WareHouseList)
        {
        for (Item item : items)
        {
            if(item.getWHname().equals(ful.getWarehouseName()))
            {
                ful.addProduct(item);
            }

        }
        }

        session.close();

        return warehouses;
    }

}