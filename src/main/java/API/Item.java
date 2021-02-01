package API;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="item_table")
public class Item implements Serializable {

    static int idCounter=0;
    @Id
    @NotEmpty(message ="ID must not be empty")
    @Column(name = "car_id") // Mam swiadomosc
    @GeneratedValue(generator = "increment")
            @GenericGenerator(name="increment", strategy ="increment")
    int id;

    @Column(name = "item_name",length = 100,nullable =false)
    @NotEmpty(message ="name must not be empty")
    String name;

    @Enumerated(EnumType.STRING)
    @NotEmpty(message ="status must not be empty")
    @Column(name = "Item_status", length = 100, nullable = false)
    Itemstatus status;

    @NotEmpty(message ="weight must not be empty")
    @Column(name = "weight")
    double weight;

    @NotEmpty(message ="amount must not be empty")
    @Column(name = "amount")
    public int amount;

    @NotEmpty(message ="price must not be empty")
    @Column(name = "price")
    double price;

    @NotEmpty(message ="WH_name must not be empty")
    @Column(name = "WH_name",length = 100,nullable = false)
    String WHname;
    @NotEmpty(message ="bought must not be empty")
    transient public int bought = 0;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="car_id")
    public List<Rating> RatingList=new ArrayList<>();

    public Item() {
    }

    public Item(String n, Itemstatus q, double w, int a, double p) {
        name = n;
        status = q;
        weight = w;
        amount = a;
        price = p;
        WHname = "Brak";
        id=idCounter;
        idCounter++;
    }


    public int getBought() {
        return bought;
    }
    public int getId(){return  id;}

    public String getName() {
        return name;
    }

    public Itemstatus getStatus() {
        return status;
    }

    public double getWeight() {
        return weight;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public String getWHname() {
        return WHname;
    }

    public static void setIdCounter(int idCounter) {
        Item.idCounter = idCounter;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String stat) {
        if(stat.equals("INSTOCK"))
            status=Itemstatus.INSTOCK;
        if(stat.equals("NEW"))
            status=Itemstatus.NEW;
        if(stat.equals("ORDERED"))
            status=Itemstatus.ORDERED;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setWHname(String WHname) {
        this.WHname = WHname;
    }

    public void setBought(int bought) {
        this.bought = bought;
    }



    public List<Rating> getRatingList() {
        return RatingList;
    }

    public void setRatingList(List<Rating> ratingList) {
        RatingList = ratingList;
    }
}
