package forms;

import dao.ActivityDAO;
import dao.LaborerDAO;
import entities.Activity;
import entities.Laborer;

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
import java.util.Optional;

public class LaborerTable extends JTable {
    private ActivityDAO activityDao;
    private LaborerDAO laborerDao;
    private ArrayList<Activity> activities;
    private TableModel tableModel;
    private TableModelListener tableModelListener;
    private ListSelectionListener listSelectionListener;
    private Activity currentSelectedActivity;
    private LaborerTable childTable;
    ActivityTable parentTable;
    DefaultTableCellRenderer totalCostCellRenderer;

    public JTable getChildTable() {
        return childTable;
    }

    public void setChildTable(LaborerTable childTable) {
        this.childTable = childTable;
    }

    public LaborerTable(ActivityTable parentTable) {
        super();
        this.activityDao = new ActivityDAO();
        this.laborerDao = new LaborerDAO();
        this.parentTable = parentTable;
        this.currentSelectedActivity = parentTable.getCurrentSelectedActivity();
        setTableModel();

        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(false);
        setTableModelListener();
        setTotalCostColumnCellRenderer();

        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(false);
    }

    private void setTableModel() {
        this.tableModel = new DefaultTableModel(new String[]{"Title", "Item Code", "Total Cost"},
                currentSelectedActivity.getLaborerList().size());

        for (int i = 0; i < currentSelectedActivity.getLaborerList().size(); i++) {
            tableModel.setValueAt(currentSelectedActivity.getLaborerList().get(i).getTitle(), i, 0);
            tableModel.setValueAt(currentSelectedActivity.getLaborerList().get(i).getItemCode(), i, 1);
            tableModel.setValueAt(currentSelectedActivity.getLaborerList().get(i).getTotalCost(), i, 2);
        }

        this.setModel(tableModel);
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
                Optional<Activity> activityOptional = activityDao.get(
                        parentTable.getCurrentSelectedActivity().getActivityId()
                );

                if (getSelectedRow() > -1 && activityOptional.isPresent()) {
                    Activity activity = activityOptional.get();
                    Laborer laborer = activity.getLaborerList().get(getSelectedRow());
                    String value = (String) getModel().
                            getValueAt(getSelectedRow(), getSelectedColumn());
                    Object[] resetValues = {laborer.getTitle(), laborer.getItemCode(), laborer.getTotalCost()};
                    //Resets cell value if a new activity row is selected while editing cell.
                    if (parentTable.getCurrentSelectedActivity().getActivityId() != laborer.getActivity().getActivityId()) {
                        getModel().setValueAt(resetValues[getSelectedColumn()], getSelectedRow(), getSelectedColumn());
                        return;
                    }
                    //Reset Cell value if cell is Empty
                    if (value.isEmpty()) {
                        getModel().setValueAt(resetValues[getSelectedColumn()], getSelectedRow(), getSelectedColumn());
                        return;
                    }
                    //Update Laborer Title
                    if (getSelectedColumn() == 0) {
                        laborerDao.updateTitle(laborer, (String) value);
                        parentTable.clearSelection();
                        parentTable.updateTable();
                        clearSelection();
                    }
                    //Update Laborer ItemCode
                    if (getSelectedColumn() == 1) {
                        clearSelection();

                        laborerDao.updateItemCode(laborer, (String) value);
                        parentTable.clearSelection();
                        parentTable.updateTable();
                    }
                    //Update Laborer and Activity TotalCost
                    if (getSelectedColumn() == 2) {
                        laborerDao.updateTotalCost(laborer, Float.parseFloat(value));
                        activityDao.updateTotalCost(activity);
                        parentTable.clearSelection();
                        parentTable.updateTable();
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
    public void updateTable(int activityId) {
        Optional<Activity> activityOpt = activityDao.get(activityId);

        if (activityOpt.isPresent()) {
            Activity activity = activityOpt.get();
            DefaultTableModel tm = (DefaultTableModel) getModel();
            for (int i = getRowCount() - 1; i > -1; i--) {
                tm.removeRow(i);
            }
            for (Laborer laborer : activity.getLaborerList()) {
                tm.addRow(new Object[]{
                        laborer.getTitle(),
                        laborer.getItemCode(),
                        laborer.getTotalCost()
                });
            }
        }
    }


}
