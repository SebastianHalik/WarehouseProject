package API;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Date;
@Table(name="rate_table")
@Entity
public class Rating {
    @Id
    @Column(name="rate_id")
    @GeneratedValue(generator = "increment")
            @GenericGenerator(name="increment", strategy = "increment")
    int id;


     transient int idCounter=0;
    @Column(name="mark")
            @Size(min = 0,max = 5)
    int mark;
    @Column(name="data")
    Date date;
    @Column(name="description", length = 200)
    String description;

    public Rating(){};

    public Rating(int mark,Date date,String description){
        this.mark=mark;
        this.date=date;
        this.description=description;
        id=idCounter;
        idCounter++;
    }

    public static void addRatingDataBase(Rating rate, SessionFactory sessionFactory){
        Session session = sessionFactory.openSession();
        session.save(rate);
        Transaction tx = session.beginTransaction();

        tx.commit();
        session.close();
    }

    public static void removeRatingDataBase(int RatingID, SessionFactory sessionFactory){
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        String hql ="DELETE FROM Rating s WHERE s.id="+RatingID;
        // String hql ="DELETE FROM Item s WHERE s.id="+Item.getID(); - ALTERNATYWA, gdy chcemy wyslac obiekt
        //  "SELECT c FROM Country c WHERE c.name = :name", Country.class);
        Query query = session.createQuery(hql);
        int count = query.executeUpdate();

        tx.commit();
        session.close();
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCounter() {
        return idCounter;
    }

    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMark() {
        return mark;
    }
}
