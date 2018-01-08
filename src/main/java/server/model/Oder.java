package server.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="Oder")
public class Oder implements Serializable {
    
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long Id;
    
    @Column(name = "datum", nullable = false)
    private String datum;
    
    @Column(name = "amnt", nullable = false)
    private int amnt;
    
    @Column(name = "forroom", nullable = false)
    private int forroom;

    public int getForroom() {
        return forroom;
    }
    
    public Oder(){
    }
    
    public Oder(String date, int amnt, int forroom){
        this.datum=date;
        this.amnt=amnt;
        this.forroom=forroom;
    }

    public String getDatum() {
        return datum;
    }

    public int getAmnt() {
        return amnt;
    }

    public void setAmnt(int amnt) {
        this.amnt = amnt;
    }
}
