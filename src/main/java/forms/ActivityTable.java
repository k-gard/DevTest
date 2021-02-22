package forms;

import dao.ActivityDAO;
import entities.Activity;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;

public class ActivityTable extends JTable {
    private ActivityDAO activityDao;
    private ArrayList<Activity> activities;

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    private TableModel tableModel;
    private TableModelListener tableModelListener;
    private ListSelectionListener listSelectionListener;
    private Activity currentSelectedActivity;
    private LaborerTable childTable;
    DefaultTableCellRenderer totalCostCellRenderer;

    public JTable getChildTable() {
        return childTable;
    }

    public void setChildTable(LaborerTable childTable) {
        this.childTable = childTable;
    }

    public ActivityTable() {
        super();
        this.activityDao = new ActivityDAO();
        this.activities = (ArrayList<Activity>) activityDao.getAll();
        this.currentSelectedActivity = this.activities.get(0);
        setTableModel();
        setListSelectionListener();
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(false);
        setTableModelListener();
        setTotalCostColumnCellRenderer();

        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(false);
    }

    private void setTableModel() {
        this.tableModel = new DefaultTableModel(new String[]{"Title", "Item Code", "Total Cost"}, activities.size()) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 2;
            }
        };

        for (int i = 0; i < activities.size(); i++) {
            tableModel.setValueAt(activities.get(i).getTitle(), i, 0);
            tableModel.setValueAt(activities.get(i).getItemCode(), i, 1);
            tableModel.setValueAt(activities.get(i).getTotalCost(), i, 2);
        }

        this.setModel(tableModel);
    }

    private void setListSelectionListener() {
        this.listSelectionListener = new ListSelectionListener() {
            @Override
            //Action performed when Activity Table row selection is changed
            public void valueChanged(ListSelectionEvent e) {
                if (getSelectedRow() > -1) {
                    setCurrentSelectedActivity(activities.get(getSelectedRow()));
                }
                childTable.clearSelection();
                changeChildTable(childTable);
            }
        };
        this.getSelectionModel().addListSelectionListener(this.listSelectionListener);
    }


    private void setTotalCostColumnCellRenderer() {
        this.totalCostCellRenderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Number) {
                    value = NumberFormat.getNumberInstance().format(value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        totalCostCellRenderer.setHorizontalAlignment(JLabel.RIGHT);
        getColumnModel().getColumn(2).setCellRenderer(totalCostCellRenderer);
    }


    private void setTableModelListener() {
        this.tableModelListener = new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tableModelEvent) {
                Activity activity = currentSelectedActivity;
                if (getSelectedRow() > -1) {
                    String value = (String) getModel()
                            .getValueAt(getSelectedRow(), getSelectedColumn());
                    //Reset Cell value if cell is Empty
                    if (value.isEmpty()) {
                        Object[] resetValues = {activity.getTitle(), activity.getItemCode()};
                        getModel().setValueAt(
                                resetValues[getSelectedColumn()],
                                getSelectedRow(),
                                getSelectedColumn()
                        );
                        return;
                    }
                    //Update Activity Title
                    if (getSelectedColumn() == 0) {
                        activityDao.updateTitle(activity, value);
                        clearSelection();
                    }
                    //Update Activity Item Code
                    if (getSelectedColumn() == 1) {
                        activityDao.updateItemCode(activity, value);
                        clearSelection();
                    }
                }


            }
        };
        this.getModel().addTableModelListener(tableModelListener);
    }

    //When Selected Row is changed change child Table to show current Activity's entries
    private void changeChildTable(LaborerTable table) {
        table.clearSelection();
        table.updateTable(currentSelectedActivity.getActivityId());

    }

    //When a cell value is Changed update the Table's model
    public void updateTable() {
        activityDao = new ActivityDAO();
        this.activities = (ArrayList<Activity>) activityDao.getAll();
        DefaultTableModel tm = (DefaultTableModel) getModel();
        for (int i = tm.getRowCount() - 1; i > -1; i--) {
            tm.removeRow(i);
        }
        for (Activity activity : activities) {
            tm.addRow(new Object[]{
                    activity.getTitle(),
                    activity.getItemCode(),
                    activity.getTotalCost()
            });

        }

    }

    public Activity getCurrentSelectedActivity() {
        return currentSelectedActivity;
    }

    public void setCurrentSelectedActivity(Activity currentSelectedActivity) {
        this.currentSelectedActivity = currentSelectedActivity;
    }
}
