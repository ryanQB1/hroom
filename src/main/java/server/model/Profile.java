package server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity(name="Profile")
public class Profile implements Serializable {
    
    @Id
    @Column(name = "un", nullable = false)
    String Id;
    
    @Column(name = "pw", nullable = false)
    String password;
    
    @Column(name = "ad", nullable = false)
    boolean admin;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orders")
    private List<Oder> orders;
    
    public Profile(){
    }
    
    public Profile(String username, String password, boolean admin){
        this.Id=username;
        this.password=password;
        this.admin=admin;
        this.orders=new ArrayList<>();
    }

    public String getId() {
        return Id;
    }
    
    public boolean getAdmin(){
        return admin;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public boolean correctPassword(String pas) {
        return this.password.equals(pas);
    }
    
    public List<Oder> getOrders(){
        return orders;
    }
    
    public void addOrder(int a, int amnt, String datum){
        Oder t = new Oder(datum, amnt, a);
        orders.add(t);
    }
    
    public void removeOrder(Oder ord){
        orders.remove(ord);
    }
}
