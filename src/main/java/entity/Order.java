package entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Order.findAll", query = "SELECT o FROM Order o")
})
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    final static String ORDER_CREATED = "Created";
    final static String ORDER_PAYED = "Payed";
    final static String ORDER_IN_PROCESS = "In process";
    final static String ORDER_READY = "Available for pickup";
    final static String ORDER_DELIVERED = "Order delivered";

    private Date dateTime;
    private String status;
    private double price;

    @Id
    private String id;

    @ManyToOne
    @JsonManagedReference
    private Account customer;

    @ManyToMany
    @JsonManagedReference
    private List<Sandwich> sandwiches = new ArrayList<>();

    @XmlElement(name="_links")
    @Transient
    private List<Link> links = new ArrayList<>();

    public Order(){
        this.price = 0;
    }

    public Order(Account customer, Date dateTime, List<Sandwich> sandwiches) {
        this.price = 0;
        this.customer = customer;
        this.dateTime = dateTime;
        this.sandwiches = sandwiches;
        this.status = ORDER_CREATED;

        if (!sandwiches.isEmpty()) {
            for (Sandwich sandwich : sandwiches) {
                this.price += sandwich.getPrice();
            }
        }
    }

    /**
     * Method to add a sandwich to the order
     * @param sandwich to add
     */
    public void addSandwich(Sandwich sandwich) {
        sandwiches.add(sandwich);
        price+= sandwich.getPrice();
    }

    /**
     * Method to remove a sandwich from the order
     * @param id of the Sandwich to remove
     * @return boolean : if the sandwich has been removed
     */
    public boolean removeSandwich(String id) {
        for (Sandwich sandwich : sandwiches) {
            if (sandwich.getId().equals(id)) {
                sandwiches.remove(sandwich);
                price-= sandwich.getPrice();
                return true;
            }
        }
        return false;
    }

    /**
     * Helper function that converts a String into a Date
     * @param s the String in the format 'dd/MM/yyyy HH:mm'
     * @return the Date if it's ok else null
     */
    public Date toDate(String s) {
        Date date = null;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            date = inputDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public void addLink(String uri, String rel) {
        this.links.add(new Link(rel,uri));
    }

    public List<Link> getLinks() {
        return links;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account getCustomer() {
        return customer;
    }

    public void setCustomer(Account customer) {
        this.customer = customer;
    }

    public List<Sandwich> getSandwiches() {
        return sandwiches;
    }

    public void setSandwiches(List<Sandwich> sandwiches) {
        this.sandwiches = sandwiches;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}