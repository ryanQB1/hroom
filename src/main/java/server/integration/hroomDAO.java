package server.integration;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import server.model.Oder;
import server.model.Profile;

@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class hroomDAO {

    @PersistenceContext(unitName = "hroompu")
    private EntityManager em;

    public boolean newProfile(String username, String password, boolean admin) {
        if (em.find(Profile.class, username) != null) {
            return false;
        }
        Profile temp = new Profile(username, password, admin);
        em.persist(temp);
        return true;
    }

    public void newOrder(String username, String date, int[] amnts) {
        Profile temp = em.find(Profile.class, username);
        for (int room = 1; room < 4; room++) {
            boolean found = false;
            for (Oder or : temp.getOrders()) {
                if (or.getDatum().equals(date) && or.getForroom() == room) {
                    or.setAmnt(or.getAmnt() + amnts[room-1]);
                    found=true;
                }
            }
            if(!found) temp.addOrder(room, amnts[room-1], date);
        }
    }

    public boolean loginCorrect(String username, String password) {
        Profile es = em.find(Profile.class, username);
        if (es == null) {
            return false;
        }
        return es.correctPassword(password);
    }

    public int occupation(int room, String date) {
        List<Oder> occs = em.createQuery("select e from Oder e where e.datum = :datum and e.forroom = :forroom", Oder.class).setParameter("datum", date).setParameter("forroom", room).getResultList();
        int tot = 0;
        for (Oder ord : occs) {
            tot += ord.getAmnt();
        }
        return tot;
    }

    public List<Oder> myOccupation(int room, String date, String username) {
        List<Oder> occs = em.find(Profile.class, username).getOrders();
        List<Oder> fin = new ArrayList<>();
        for (Oder or : occs) {
            if (or.getForroom() == room) {
                fin.add(or);
            }
        }
        return fin;
    }

    public void removeOrder(String username, String date, int amnt, int room) {
        Profile temp = em.find(Profile.class, username);
        for (Oder or : temp.getOrders()) {
            if (or.getDatum().equals(date) && or.getForroom() == room) {
                if (or.getAmnt() > amnt) {
                    or.setAmnt(or.getAmnt() - amnt);
                } else {
                    temp.removeOrder(or);
                }
                return;
            }
        }
    }

    public Profile findProfile(String username) {
        return em.find(Profile.class, username);
    }
    
    public boolean isAdmin(String username) {
        if(!exists(username)) return false;
        return em.find(Profile.class,username).getAdmin();
    }
    
    public boolean exists(String username) {
        return em.find(Profile.class,username) !=null;
    }
}
