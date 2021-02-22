package entities;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.util.List;

@Entity

@Table(name = "ACTIVITY")
public class Activity {
    @Id
    @Column(name = "activityid")

    private int activityId;
    private String title;
    private String itemCode;

    private double totalCost;
    @OneToMany(targetEntity = Laborer.class, mappedBy = "activity", cascade = CascadeType.ALL)

    private List<Laborer> laborerList;

    public Activity() {
    }


    public Activity(int id, String title, String itemCode) {
        this.activityId = id;
        this.title = title;
        this.itemCode = itemCode;
        setTotalCost();
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost() {

        if (this.laborerList == null || this.laborerList.isEmpty()) {
            this.totalCost = 0;
        } else {
            this.totalCost = 0;
            this.laborerList.forEach(l -> this.totalCost += l.getTotalCost());
        }
    }

    public List<Laborer> getLaborerList() {
        return laborerList;
    }

    public void setLaborerList(List<Laborer> laborerList) {
        this.laborerList = laborerList;
    }
}
