package entities;

import javax.persistence.*;

@Entity
@Table(name = "LABORER")
public class Laborer {
    @Id
    private int laborerId;
    private String title;
    private String itemCode;
    private double totalCost;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activityid")
    private Activity activity;

    public Laborer(int laborerId, String title, String itemCode, int totalCost) {
        this.laborerId = laborerId;
        this.title = title;
        this.itemCode = itemCode;
        this.totalCost = totalCost;
    }

    public Laborer() {
    }

    public int getLaborerId() {
        return laborerId;
    }

    public void setLaborerId(int laborerId) {
        this.laborerId = laborerId;
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

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
