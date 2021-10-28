/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author 0x5un5h1n3

 */
public class EmployeeDashboard extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    int mousepX;
    int mousepY;

    int discount;

    CardLayout cardLayout;
    Timer timer;
    String month;
    String year;
    String date;
    String today;
    String itemId;
    String path = null;

    EmployeeDashboard() {

        initComponents();

        EmployeeDashboard.this.getRootPane().setBorder(new LineBorder(((new Color(30, 30, 30)))));
        EmployeeDashboard.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIcon();
        setTableheader();
        generateInvoiceId();
        refreshStockTable();
        refreshLowStockTable();
        refreshSalesTable();
        refreshSalesHistoryTable();
        employeeCount();
        EmployeeDashboard.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        maximizeView.setVisible(true);
        restoreDownView.setVisible(false);
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        EmployeeDashboard.this.setMaximizedBounds(env.getMaximumWindowBounds());
        maximized = false;

        EmployeeDashboard.this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int choose = JOptionPane.showConfirmDialog(null,
                        "Do you really want to exit the application ?",
                        "Confirm Close", JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);
                if (choose == JOptionPane.YES_OPTION) {
                    e.getWindow().dispose();

                } else {

                }
            }
        });

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date1 = new Date();
                DateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                String time = timeFormat.format(date1);
                lblTime.setText(time);

                Date date2 = new Date();
                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String date = dateFormat.format(date2);
                lblDate.setText(date);

                DateFormat dayInWordsFormat = new SimpleDateFormat("EEEE");
                String dayInWords = dayInWordsFormat.format(date2);
                DateFormat monthFormat = new SimpleDateFormat("MMMM");
                String month = monthFormat.format(date2);
                DateFormat dayFormat = new SimpleDateFormat("dd");
                String day = dayFormat.format(date2);
                DateFormat yearFormat = new SimpleDateFormat("yyyy");
                String year = yearFormat.format(date2);
                jLabel71.setText("Today is " + dayInWords + ", " + month + " " + day + ", " + year);

                DateFormat wishBasedOnTime = new SimpleDateFormat("HH");
                Date date4 = new Date();
                String wishTime = wishBasedOnTime.format(date4);
                int now = Integer.parseInt(wishTime);

                if (now >= 6 && now < 12) {
                    loginWish.setText("Good morning");
                    employeeName.setText(lblEmployeeUsername.getText());
                } else if (now >= 12 && now < 17) {
                    loginWish.setText("Good afternoon");
                    employeeName.setText(lblEmployeeUsername.getText());
                } else if (now >= 17 && now < 20) {
                    loginWish.setText("Good evening");
                    employeeName.setText(lblEmployeeUsername.getText());
                } else {
                    loginWish.setText("Good night");
                    employeeName.setText(lblEmployeeUsername.getText());
                }

            }
        };
        timer = new Timer(1000, actionListener);

        timer.setInitialDelay(0);
        timer.start();
    }

    static boolean maximized = true;

    public void setPnlColor(JPanel pnl) {
        pnl.setBackground(new Color(60, 60, 60));
    }

    public void resetPnlColor(JPanel pnl) {
        pnl.setBackground(new Color(35, 35, 35));
    }

    public void setPnlIndicatorColor(JPanel pnl) {
        pnl.setBackground(new Color(75, 136, 198));
    }

    public void resetPnlIndicatorColor(JPanel pnl) {
        pnl.setBackground(new Color(35, 35, 35));
    }

    public void setLblColor(JLabel lbl) {
        lbl.setBackground(new Color(60, 60, 60));
    }

    public void resetLblColor(JLabel lbl) {
        lbl.setBackground(new Color(35, 35, 35));
    }

    private void setTableheader() {
        JTableHeader invtbl = tblInvoice.getTableHeader();
        invtbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        invtbl.setForeground(Color.white);
        invtbl.setBackground(new Color(18, 18, 18));
        tblInvoice.getTableHeader().setReorderingAllowed(false);

        JTableHeader stk = tblStock.getTableHeader();
        stk.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        stk.setForeground(Color.white);
        stk.setBackground(new Color(18, 18, 18));
        tblStock.getTableHeader().setReorderingAllowed(false);

        JTableHeader lowstk = tblLowStock.getTableHeader();
        lowstk.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lowstk.setForeground(Color.white);
        lowstk.setBackground(new Color(18, 18, 18));
        tblLowStock.getTableHeader().setReorderingAllowed(false);

        JTableHeader tdysls = tblTodaySales.getTableHeader();
        tdysls.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tdysls.setForeground(Color.white);
        tdysls.setBackground(new Color(18, 18, 18));
        tblTodaySales.getTableHeader().setReorderingAllowed(false);

        JTableHeader slshist = tblSalesHistory.getTableHeader();
        slshist.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        slshist.setForeground(Color.white);
        slshist.setBackground(new Color(18, 18, 18));
        tblSalesHistory.getTableHeader().setReorderingAllowed(false);
    }

    void showDate() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(s.format(d));
    }

    private void employeeCount() {
        try {

            ResultSet count = DB.DB.getData("SELECT count(id) FROM employee WHERE account_status = 'Active' ");

            if (count.next()) {
                int d = count.getInt(1);
                lblHomeEmployeeCount.setText("Employee Count : " + Integer.toString(d));
            }

        } catch (Exception e) {
        }
    }

    private void generateInvoiceId() {
        try {

            ResultSet rs = DB.DB.getData("SELECT invoice_id AS x FROM gin ORDER BY invoice_id DESC LIMIT 1");
            if (rs.next()) {

                int rowcount = Integer.parseInt(rs.getString("x"));
                rowcount++;

                this.txtInvoiceId.setText("" + rowcount);
                txtSearchInvoice.setText(txtInvoiceId.getText());

                lblInvoiceId.setText("Invoice Id : " + txtSearchInvoice.getText());
            }

        } catch (Exception e) {
        }
    }

    private void calculateStockCount() {
        int count = tblStock.getRowCount();
        lblStockCount.setText("SKU Count : " + Integer.toString(count));

    }

    private void refreshStockTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stock_status = 'Available' AND (stock_count > low_stock)");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(5));
                String e = (rs.getString(8));
                String f = (rs.getString(9));
                dtm.addRow(new Object[]{a, b, c, d, e, f});

            }

            calculateStockCount();
            txtSearchStock.setText(null);

            ResultSet stockCount = DB.DB.getData("SELECT COUNT(id) FROM stock WHERE stock_status = 'Available' AND (stock_count > low_stock)");
            if (stockCount.next()) {
                int d = stockCount.getInt(1);
                lblHomeStockCount.setText("Stock Count : " + d);
            }

            ResultSet lowStock = DB.DB.getData("SELECT COUNT(id) FROM stock WHERE stock_status = 'Available' AND (stock_count < low_stock)");
            if (lowStock.next()) {
                int f = lowStock.getInt(1);

                if (f > 0) {
                    lblHomeLowStockCount.setForeground(new Color(255, 0, 51));

                } else {
                    lblHomeLowStockCount.setForeground(new Color(180, 180, 180));
                }
                lblHomeLowStockCount.setText("Low Stock Count : " + f);
            }

            refreshLowStockTable();

        } catch (Exception e) {
        }

    }

    private void searchStock() {
        switch (cmbSortStock.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stock_status = 'Available' AND (stock_count > low_stock) AND id = '" + txtSearchStock.getText() + "' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(5));
                    String e = (rs.getString(8));
                    String f = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateStockCount();

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stock_status = 'Available' AND (stock_count > low_stock) AND item_code like '" + txtSearchStock.getText() + "%' order by item_code ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(5));
                    String e = (rs.getString(8));
                    String f = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateStockCount();

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stock_status = 'Available' AND (stock_count > low_stock) AND item_name like '" + txtSearchStock.getText() + "%' order by item_name ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(5));
                    String e = (rs.getString(8));
                    String f = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateStockCount();
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    public void printBarcodes() {
        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewBarcodeReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    private void calculateLowStockCount() {
        int count = tblLowStock.getRowCount();
        lblLowStockCount.setText("SKU Count : " + Integer.toString(count));
    }

    private void refreshLowStockTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblLowStock.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stock_status = 'Available' AND (stock_count < low_stock)");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(5));
                String e = (rs.getString(8));
                String f = (rs.getString(9));
                dtm.addRow(new Object[]{a, b, c, d, e, f});

            }

            calculateLowStockCount();
            txtSearchLowStock.setText(null);

        } catch (Exception e) {
        }

    }

    private void searchLowStock() {
        switch (cmbSortLowStock.getSelectedIndex()) {
            case 0:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblLowStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stock_status = 'Available' AND (stock_count < low_stock) AND id like '" + txtSearchLowStock.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(5));
                    String e = (rs.getString(8));
                    String f = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateLowStockCount();

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblLowStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stock_status = 'Available' AND (stock_count < low_stock) AND item_code like '" + txtSearchLowStock.getText() + "%' order by item_code ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(5));
                    String e = (rs.getString(8));
                    String f = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateLowStockCount();

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblLowStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stock_status = 'Available' AND (stock_count < low_stock) AND item_name like '" + txtSearchLowStock.getText() + "%' order by item_name ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(5));
                    String e = (rs.getString(8));
                    String f = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateLowStockCount();
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    private void addInvoiceItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Item to Invoice Item List?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtInvoiceItemCode.getText().equals("")
                    & (!txtInvoiceItemName.getText().equals(""))
                    & (!txtInvoiceItemPurchasePrice.getText().equals(""))
                    & (!txtInvoiceItemSellingPrice.getText().equals(""))
                    & (!txtInvoiceNewItemPrice.getText().equals(""))
                    & (!txtInvoiceItemAmount.getText().equals(""))) {

                try {

                    ResultSet rs = DB.DB.getData("SELECT item_name FROM gin WHERE item_code = '" + txtInvoiceItemCode.getText() + "' AND invoice_id = '" + txtInvoiceId.getText() + "' ");

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Item Already Added to Invoice!", "Invalid Data", JOptionPane.WARNING_MESSAGE);

                    } else if (Integer.parseInt(txtInvoiceItemAmount.getText()) > Integer.parseInt(txtInvoiceItemStockCount.getText())) {
                        JOptionPane.showMessageDialog(this, "No Sufficient Stock Count Available!", "Invalid Data", JOptionPane.WARNING_MESSAGE);

                    } else if ((Integer.parseInt(txtInvoiceItemPurchasePrice.getText())) > (Integer.parseInt(txtInvoiceNewItemPrice.getText()))) {
                        txtInvoiceItemAmount.setText(null);
                        JOptionPane.showMessageDialog(this, "New Selling Price of this Item is Too Low, Please Lower the Discount!");

                        cmbInvoiceItemDiscount.setPopupVisible(true);
                        cmbInvoiceItemDiscount.grabFocus();

                    } else {

                        switch (cmbInvoiceItemDiscount.getSelectedIndex()) {
                            case 0: {
                                discount = 0;
                                break;
                            }
                            case 1: {
                                discount = 5;
                                break;
                            }
                            case 2: {
                                discount = 10;
                                break;
                            }
                            case 3: {
                                discount = 15;
                                break;
                            }
                            case 4: {
                                discount = 20;
                                break;
                            }
                            case 5: {
                                discount = 25;
                                break;
                            }
                            case 6: {
                                discount = 30;
                                break;
                            }
                            case 7: {
                                discount = 35;
                                break;
                            }
                            default:
                                break;
                        }

                        int totalWithDiscount = Integer.parseInt(txtInvoiceNewItemPrice.getText()) * Integer.parseInt(txtInvoiceItemAmount.getText()); // With Savings 
                        int totalWithoutDiscount = Integer.parseInt(txtInvoiceItemSellingPrice.getText()) * Integer.parseInt(txtInvoiceItemAmount.getText());  // Without Savings [Savings = $0]
                        int savings = totalWithoutDiscount - totalWithDiscount;
                        int profitPerSingleItemPerEachItem = Integer.parseInt(txtInvoiceNewItemPrice.getText()) - Integer.parseInt(txtInvoiceItemPurchasePrice.getText()); // Profit per each item [WITH DISCOUNT]
                        int ptofitPerItemAmountPerEachItem = profitPerSingleItemPerEachItem * Integer.parseInt(txtInvoiceItemAmount.getText()); //Profite per item amount of specific item [WITH DISCOUNT]

                        Date DateMonth = new Date();
                        SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
                        month = "" + toMonth.format(DateMonth);

                        Date DateYear = new Date();
                        SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
                        year = "" + toYear.format(DateYear);

                        Date DateDate = new Date();
                        SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                        date = "" + toDate.format(DateDate);

                        String addInvoiceItem = "INSERT INTO gin(invoice_id, item_id, item_code, item_name, purchase_price, selling_price, discount, new_item_price, amount, total, new_total, savings, employee, profit, date, month, year) VALUES ('" + txtInvoiceId.getText() + "', (SELECT id FROM stock WHERE item_code = '" + txtInvoiceItemCode.getText() + "'), '" + txtInvoiceItemCode.getText() + "', '" + txtInvoiceItemName.getText() + "',  '" + txtInvoiceItemPurchasePrice.getText() + "', '" + txtInvoiceItemSellingPrice.getText() + "','" + discount + "', '" + txtInvoiceNewItemPrice.getText() + "', '" + txtInvoiceItemAmount.getText() + "', '" + totalWithoutDiscount + "', '" + totalWithDiscount + "','" + savings + "','" + lblEmployeeUsername.getText() + "','" + ptofitPerItemAmountPerEachItem + "', '" + date + "', '" + month + "', '" + year + "') ";
                        String addSalesItem = "INSERT INTO sales(invoice_id, item_id, item_code, item_name, purchase_price, selling_price, discount, new_item_price, amount, total, new_total, savings, employee, profit, date, month, year) VALUES ('" + txtInvoiceId.getText() + "', (SELECT id FROM stock WHERE item_code = '" + txtInvoiceItemCode.getText() + "'), '" + txtInvoiceItemCode.getText() + "', '" + txtInvoiceItemName.getText() + "',  '" + txtInvoiceItemPurchasePrice.getText() + "', '" + txtInvoiceItemSellingPrice.getText() + "','" + discount + "', '" + txtInvoiceNewItemPrice.getText() + "', '" + txtInvoiceItemAmount.getText() + "', '" + totalWithoutDiscount + "', '" + totalWithDiscount + "','" + savings + "','" + lblEmployeeUsername.getText() + "','" + ptofitPerItemAmountPerEachItem + "', '" + date + "', '" + month + "', '" + year + "') ";
                        String addNewSaleItemActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM employee WHERE username = '" + lblEmployeeUsername.getText() + "'),'Employee','" + lblEmployeeUsername.getText() + "','Add New Sale Item','Employee added new sale item " + txtInvoiceItemName.getText() + "','SUCCESS')";

                        try {

                            DB.DB.putData(addInvoiceItem);
                            DB.DB.putData(addSalesItem);
                            DB.DB.putData(addNewSaleItemActivityLog);

                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Item Added to Invoice Item List!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            ResultSet stockCountofSelectedItem = DB.DB.getData("SELECT stock_count FROM stock WHERE id = (SELECT id FROM stock WHERE item_code = '" + txtInvoiceItemCode.getText() + "') ");
                            if (stockCountofSelectedItem.next()) {

                                int currentStockCount = Integer.parseInt(stockCountofSelectedItem.getString(1));
                                int soldAmount = Integer.parseInt(txtInvoiceItemAmount.getText());
                                int newStockCount = currentStockCount - soldAmount;

                                DB.DB.putData("UPDATE stock SET stock_count ='" + newStockCount + "' WHERE item_code = '" + txtInvoiceItemCode.getText() + "' ");
                            }

                            txtInvoiceItemCode.setText(null);
                            txtInvoiceItemName.setText(null);
                            txtInvoiceItemPurchasePrice.setText(null);
                            txtInvoiceItemSellingPrice.setText(null);
                            cmbInvoiceItemDiscount.setSelectedIndex(0);
                            txtInvoiceNewItemPrice.setText(null);
                            txtInvoiceItemAmount.setText(null);
                            txtInvoiceItemStockCount.setText(null);
                            txtInvoiceItemLowStockCount.setText(null);
                            txtInvoiceItemStockStatus.setText(null);
                            txtInvoiceItemStockStatus.setBorder(new LineBorder((new Color(100, 100, 100))));

                        } catch (Exception e) {
                        }
                    }

                    refreshInvoiceTable();
                    refreshStockTable();
                    refreshSalesTable();
                    refreshSalesHistoryTable();

                } catch (Exception ex) {
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void deleteInvoiceItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this Invoice Item?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            try {

                if (txtInvoiceId.getText().equals(txtSearchInvoice.getText())) {
                    String selectedInvoiceItemId = ((String) tblInvoice.getValueAt(tblInvoice.getSelectedRow(), 0));
                    DB.DB.putData("DELETE FROM gin WHERE invoice_id  = '" + txtInvoiceId.getText() + "' AND id = '" + selectedInvoiceItemId + "' ");
                    DB.DB.putData("DELETE FROM sales WHERE invoice_id  = '" + txtInvoiceId.getText() + "' AND id = '" + selectedInvoiceItemId + "' ");

                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "Invoice Item Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                    JOptionPane.showMessageDialog(this, "Stock Updated!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                    JOptionPane.showMessageDialog(this, "Sales Updated!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                    ResultSet itemCodeFromItemName = DB.DB.getData("SELECT item_code FROM stock WHERE item_name = '" + txtInvoiceItemName.getText() + "'  ");

                    if (itemCodeFromItemName.next()) {
                        String itmCode = itemCodeFromItemName.getString(1);

                        ResultSet stockCountofSelectedItem = DB.DB.getData("SELECT stock_count FROM stock WHERE id = (SELECT id FROM stock WHERE item_code = '" + itmCode + "') ");
                        if (stockCountofSelectedItem.next()) {
                            int currentStockCount = Integer.parseInt(stockCountofSelectedItem.getString(1));
                            int soldAmount = Integer.parseInt(txtInvoiceItemAmount.getText());
                            int newStockCount = currentStockCount + soldAmount;

                            DB.DB.putData("UPDATE stock SET stock_count ='" + newStockCount + "' WHERE item_code = '" + itmCode + "' ");
                            String removeNewSaleItemActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM employee WHERE username = '" + lblEmployeeUsername.getText() + "'),'Employee','" + lblEmployeeUsername.getText() + "','Remove New Sale Item','Employee removed new sale item " + txtInvoiceItemName.getText() + "','SUCCESS')";
                            DB.DB.putData(removeNewSaleItemActivityLog);
                        }

                    }

                } else {
                    JOptionPane.showMessageDialog(this, "This Invoice Item has Already Issued, Cannot be Deleted!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                }

                txtInvoiceItemName.setText(null);
                txtInvoiceItemAmount.setText(null);

                refreshInvoiceTable();
                refreshStockTable();
                refreshSalesTable();
                refreshSalesHistoryTable();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(pnlParent, mousepX);
            }
        } else {

        }
    }

    private void calculateInvoiceBillPayments() {
        try {

            ResultSet invoiceAmountDue = DB.DB.getData("SELECT sum(new_total) FROM gin WHERE invoice_id  = '" + txtSearchInvoice.getText() + "'");    //Invoice Bill Total [With Discounts/Without Discounts}
            ResultSet invoiceBillSavings = DB.DB.getData("SELECT sum(savings) FROM gin WHERE invoice_id  = '" + txtSearchInvoice.getText() + "'");

            if (invoiceAmountDue.next()) {
                Double d = invoiceAmountDue.getDouble(1);
                txtInvoiceAmountDue.setText(Double.toString(d));
                lblInvoiceTotal.setText("Total : " + Double.toString(d));
            }

            if (invoiceBillSavings.next()) {
                Double d = invoiceBillSavings.getDouble(1);
                txtInvoiceAmountSavings.setText(Double.toString(d));
            }

        } catch (Exception e) {
        }
    }

    private void refreshInvoiceTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblInvoice.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM gin WHERE invoice_id = '" + txtSearchInvoice.getText() + "'");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(5));
                String c = (rs.getString(7));
                String d = (rs.getString(8));
                String e = (rs.getString(9));
                String f = (rs.getString(10));
                String g = (rs.getString(12));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g});

            }

            calculateInvoiceBillPayments();

        } catch (Exception e) {
        }

    }

    private void searchInvoiceItem() {
        switch (cmbSortInvoice.getSelectedIndex()) {
            case 0:
                calculateInvoiceBillPayments();

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblInvoice.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM gin WHERE invoice_id = '" + txtSearchInvoice.getText() + "%' order by invoice_id ");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(1));
                        String b = (rs.getString(5));
                        String c = (rs.getString(7));
                        String d = (rs.getString(8));
                        String e = (rs.getString(9));
                        String f = (rs.getString(10));
                        String g = (rs.getString(12));
                        dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                    }

                } catch (Exception e) {
                }
                break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblInvoice.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM gin WHERE item_name like '" + txtSearchInvoice.getText() + "%' order by item_name ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(5));
                    String c = (rs.getString(7));
                    String d = (rs.getString(8));
                    String e = (rs.getString(9));
                    String f = (rs.getString(10));
                    String g = (rs.getString(12));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

            } catch (Exception e) {
            }
            break;
            case 2:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblInvoice.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM gin WHERE item_code like '" + txtSearchInvoice.getText() + "%' order by item_code ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(5));
                    String c = (rs.getString(7));
                    String d = (rs.getString(8));
                    String e = (rs.getString(9));
                    String f = (rs.getString(10));
                    String g = (rs.getString(12));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    private void issueItems() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Issue Invoice Items?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (txtInvoiceId.getText().equals(txtSearchInvoice.getText())) {

                if (!txtInvoiceAmountPaid.getText().equals("")
                        & (!txtInvoiceAmountBalance.getText().equals(""))
                        & (!txtInvoiceAmountDue.getText().equals(""))) {

                    String issueInvoiceItems = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM employee WHERE username = '" + lblEmployeeUsername.getText() + "'),'Employee','" + lblEmployeeUsername.getText() + "','Issue Invoice/Items','Employee Issued Invoice No. " + txtInvoiceId.getText() + "','SUCCESS')";
                    try {
                        DB.DB.putData(issueInvoiceItems);
                    } catch (Exception e) {
                    }

                    generateInvoiceId();

                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "Invoice Items Issued!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                    refreshInvoiceTable();

                    txtInvoiceAmountPaid.setText(null);
                    jTabbedPaneInvoice.setSelectedIndex(0);
                    txtInvoiceItemCode.setText(null);
                    txtInvoiceItemName.setText(null);
                    txtInvoiceItemPurchasePrice.setText(null);
                    txtInvoiceItemSellingPrice.setText(null);
                    cmbInvoiceItemDiscount.setSelectedIndex(0);
                    txtInvoiceNewItemPrice.setText(null);
                    txtInvoiceItemAmount.setText(null);

                } else {
                    JOptionPane.showMessageDialog(this, "Please Fill All the Empty Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                }

            } else {
                JOptionPane.showMessageDialog(this, "This Transaction Already Done, Cannot be Issued Again!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }

        }
    }

    public void printSelectedInvoiceBill() {
        HashMap a = new HashMap();
        a.put("invoice_id", txtSearchInvoice.getText());
        a.put("invoice_total", txtInvoiceAmountDue.getText());
        a.put("paid_amount", txtInvoiceAmountPaid.getText());
        a.put("balance", txtInvoiceAmountBalance.getText());
        a.put("total_savings", txtInvoiceAmountSavings.getText());
        a.put("employee", lblEmployeeUsername.getText());

        new Thread() {
            @Override
            public void run() {

                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewSelectedInvoiceBill.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {
                }
            }
        }.start();
    }

    private void refreshSalesTable() {
        try {

            Date DateMonth = new Date();
            SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
            month = "" + toMonth.format(DateMonth);

            Date DateYear = new Date();
            SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
            year = "" + toYear.format(DateYear);

            Date DateDate = new Date();
            SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
            date = "" + toDate.format(DateDate);

            DefaultTableModel dtm = (DefaultTableModel) tblTodaySales.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE date = '" + date + "' AND employee = '" + lblEmployeeUsername.getText() + "' order by id desc ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(2));
                String b = (rs.getString(5));
                String c = (rs.getString(9));
                String d = (rs.getString(10));
                String e = (rs.getString(12));
                String f = (rs.getString(14));
                dtm.addRow(new Object[]{a, b, c, d, e, f});

            }

            cmbSortTodaySales.setSelectedIndex(0);
            txtSearchTodaySales.setText(null);

            ResultSet todaySalesCount = DB.DB.getData("SELECT count(id) FROM sales WHERE employee = '" + lblEmployeeUsername.getText() + "' AND date  = '" + date + "' ");
            ResultSet thisMonthSalesCount = DB.DB.getData("SELECT count(id) FROM sales WHERE employee = '" + lblEmployeeUsername.getText() + "' AND month  = '" + month + "' AND year = '" + year + "' ");

            if (todaySalesCount.next()) {
                int d = todaySalesCount.getInt(1);
                lblTodaySalesCount.setText("Sales Count : " + d);
                jLabel52.setText("Today Sales Count : " + d);
                jLabel52.setText("Today Count : " + d);
            }

            if (thisMonthSalesCount.next()) {
                int d = thisMonthSalesCount.getInt(1);
                jLabel53.setText("Monthly Count : " + d);
            }

        } catch (Exception e) {
        }

    }

    private void searchTodaySalesTable() {
        switch (cmbSortTodaySales.getSelectedIndex()) {

            case 0:

                Date DateDate = new Date();
                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                date = "" + toDate.format(DateDate);

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblTodaySales.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE employee = '" + lblEmployeeUsername.getText() + "' AND date = '" + date + "' AND invoice_id like '" + txtSearchTodaySales.getText() + "%' order by id desc");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(2));
                        String b = (rs.getString(5));
                        String c = (rs.getString(9));
                        String d = (rs.getString(10));
                        String e = (rs.getString(12));
                        String f = (rs.getString(14));
                        dtm.addRow(new Object[]{a, b, c, d, e, f});
                    }

                } catch (Exception e) {
                }
                break;
            case 1:

                

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblTodaySales.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE employee = '" + lblEmployeeUsername.getText() + "' AND date = '" + date + "' AND item_name like '" + txtSearchTodaySales.getText() + "%' order by id desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(5));
                    String c = (rs.getString(9));
                    String d = (rs.getString(10));
                    String e = (rs.getString(12));
                    String f = (rs.getString(14));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
            } catch (Exception e) {
            }

            break;
            default:
                break;
        }
    }

    private void refreshSalesHistoryTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblSalesHistory.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE employee = '" + lblEmployeeUsername.getText() + "' order by id desc ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(2));
                String b = (rs.getString(5));
                String c = (rs.getString(9));
                String d = (rs.getString(10));
                String e = (rs.getString(12));
                String f = (rs.getString(14));
                String g = (rs.getString(19));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g});

            }
            cmbSortSalesHistory.setSelectedIndex(0);
            txtSearchSalesHistory.setText(null);

        } catch (Exception e) {
        }

    }

    private void searchSalesHistoryTable() {
        switch (cmbSortSalesHistory.getSelectedIndex()) {

            case 0:


                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSalesHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE employee  ='" + lblEmployeeUsername.getText() + "' AND invoice_id like '" + txtSearchSalesHistory.getText() + "%' order by id desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(5));
                    String c = (rs.getString(9));
                    String d = (rs.getString(10));
                    String e = (rs.getString(12));
                    String f = (rs.getString(14));
                    String g = (rs.getString(16));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

            } catch (Exception e) {
            }
            break;
            case 1:

                

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSalesHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE employee  ='" + lblEmployeeUsername.getText() + "' AND item_name like '" + txtSearchSalesHistory.getText() + "%' order by id desc");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(5));
                    String c = (rs.getString(9));
                    String d = (rs.getString(10));
                    String e = (rs.getString(12));
                    String f = (rs.getString(14));
                    String g = (rs.getString(16));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFrames = new javax.swing.JPanel();
        pnlEmployeeDashboard = new javax.swing.JPanel();
        pnlTop = new javax.swing.JPanel();
        pnlTopIcons = new javax.swing.JPanel();
        maximizeView = new javax.swing.JPanel();
        minimize1 = new javax.swing.JLabel();
        maximize1 = new javax.swing.JLabel();
        close1 = new javax.swing.JLabel();
        restoreDownView = new javax.swing.JPanel();
        minimize2 = new javax.swing.JLabel();
        maximize2 = new javax.swing.JLabel();
        close2 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        title = new javax.swing.JLabel();
        pnlSidebar = new javax.swing.JPanel();
        pnlHome = new javax.swing.JPanel();
        home = new javax.swing.JLabel();
        pnlIndicatorHome = new javax.swing.JPanel();
        pnlPOS = new javax.swing.JPanel();
        sales = new javax.swing.JLabel();
        pnlIndicatorPOS = new javax.swing.JPanel();
        pnlSettings = new javax.swing.JPanel();
        reports = new javax.swing.JLabel();
        pnlIndicatorSettings = new javax.swing.JPanel();
        pnlBranding = new javax.swing.JPanel();
        jLabel111 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        watermark = new javax.swing.JLabel();
        pnlParent = new javax.swing.JPanel();
        pnlHeader = new javax.swing.JPanel();
        cardName = new javax.swing.JLabel();
        pnlLogout = new javax.swing.JPanel();
        lblLogout = new javax.swing.JLabel();
        pnlDateTime = new javax.swing.JPanel();
        lblTime = new javax.swing.JLabel();
        lblDate = new javax.swing.JLabel();
        mainCard = new javax.swing.JPanel();
        cardHome = new javax.swing.JPanel();
        pnlHomeSubSelection = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        lblHomeStockCount = new javax.swing.JLabel();
        lblHomeLowStockCount = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        lblHomeEmployeeCount = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        loginWish = new javax.swing.JLabel();
        employeeName = new javax.swing.JLabel();
        cardPOS = new javax.swing.JPanel();
        pnlPOSSubSelection = new javax.swing.JPanel();
        btn_employees = new javax.swing.JLabel();
        btn_employees1 = new javax.swing.JLabel();
        btn_employees2 = new javax.swing.JLabel();
        subCardPOS = new javax.swing.JPanel();
        card1 = new javax.swing.JPanel();
        jTabbedPaneInvoice = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel133 = new javax.swing.JLabel();
        txtInvoiceItemName = new javax.swing.JTextField();
        jLabel134 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        btnUpdateSelectedEmployee = new javax.swing.JButton();
        jLabel156 = new javax.swing.JLabel();
        txtInvoiceItemCode = new javax.swing.JTextField();
        btnDeleteSelectedEmployee = new javax.swing.JButton();
        btnPrintSelectedEmployee = new javax.swing.JButton();
        txtInvoiceItemAmount = new javax.swing.JTextField();
        cmbInvoiceItemDiscount = new javax.swing.JComboBox<>();
        jLabel140 = new javax.swing.JLabel();
        txtInvoiceItemPurchasePrice = new javax.swing.JTextField();
        txtInvoiceNewItemPrice = new javax.swing.JTextField();
        jLabel141 = new javax.swing.JLabel();
        jLabel142 = new javax.swing.JLabel();
        txtInvoiceItemSellingPrice = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel137 = new javax.swing.JLabel();
        txtInvoiceItemStockCount = new javax.swing.JTextField();
        jLabel138 = new javax.swing.JLabel();
        txtInvoiceItemLowStockCount = new javax.swing.JTextField();
        jLabel143 = new javax.swing.JLabel();
        txtInvoiceItemStockStatus = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel155 = new javax.swing.JLabel();
        txtInvoiceId = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblInvoice = new javax.swing.JTable();
        btnEditEmployee = new javax.swing.JButton();
        btnDeleteEmployee = new javax.swing.JButton();
        btnPrintAllEmployee = new javax.swing.JButton();
        txtSearchInvoice = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        btnRefreshEmployee = new javax.swing.JButton();
        cmbSortInvoice = new javax.swing.JComboBox<>();
        lblInvoiceId = new javax.swing.JLabel();
        lblInvoiceTotal = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jLabel139 = new javax.swing.JLabel();
        txtInvoiceAmountBalance = new javax.swing.JTextField();
        jLabel157 = new javax.swing.JLabel();
        txtInvoiceAmountDue = new javax.swing.JTextField();
        btnUpdateSelectedEmployee1 = new javax.swing.JButton();
        jLabel158 = new javax.swing.JLabel();
        txtInvoiceAmountPaid = new javax.swing.JTextField();
        btnDeleteSelectedEmployee1 = new javax.swing.JButton();
        jLabel144 = new javax.swing.JLabel();
        txtInvoiceAmountSavings = new javax.swing.JTextField();
        lblSelectedEmployeePositionValidation1 = new javax.swing.JLabel();
        lblSelectedEmployeePositionValidation2 = new javax.swing.JLabel();
        card2 = new javax.swing.JPanel();
        jTabbedPaneStock = new javax.swing.JTabbedPane();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        tblStock = new javax.swing.JTable();
        btnPrintAllEmployee1 = new javax.swing.JButton();
        txtSearchStock = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        cmbSortStock = new javax.swing.JComboBox<>();
        lblStockCount = new javax.swing.JLabel();
        btnPrintAllEmployee4 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        txtSearchLowStock = new javax.swing.JTextField();
        cmbSortLowStock = new javax.swing.JComboBox<>();
        jScrollPane24 = new javax.swing.JScrollPane();
        tblLowStock = new javax.swing.JTable();
        lblLowStockCount = new javax.swing.JLabel();
        btnPrintAllEmployee2 = new javax.swing.JButton();
        card3 = new javax.swing.JPanel();
        jTabbedPaneStock1 = new javax.swing.JTabbedPane();
        jPanel28 = new javax.swing.JPanel();
        txtSearchTodaySales = new javax.swing.JTextField();
        cmbSortTodaySales = new javax.swing.JComboBox<>();
        jLabel132 = new javax.swing.JLabel();
        jScrollPane27 = new javax.swing.JScrollPane();
        tblTodaySales = new javax.swing.JTable();
        btnRefreshMonthlyEmployeeAttendance23 = new javax.swing.JButton();
        lblTodaySalesCount = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        txtSearchSalesHistory = new javax.swing.JTextField();
        cmbSortSalesHistory = new javax.swing.JComboBox<>();
        jLabel131 = new javax.swing.JLabel();
        jScrollPane26 = new javax.swing.JScrollPane();
        tblSalesHistory = new javax.swing.JTable();
        btnRefreshMonthlyEmployeeAttendance24 = new javax.swing.JButton();
        cardSettings = new javax.swing.JPanel();
        pnlSettingsSubSelection = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        subCardSettings = new javax.swing.JPanel();
        card4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtBackupLocation = new javax.swing.JTextField();
        btnAddEmployee1 = new javax.swing.JButton();
        btnAddEmployee2 = new javax.swing.JButton();
        card5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtRestoreLocation = new javax.swing.JTextField();
        btnAddEmployee5 = new javax.swing.JButton();
        btnAddEmployee6 = new javax.swing.JButton();
        card6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        version1 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel25 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        pnlLogoutLoader = new javax.swing.JPanel();
        pnlLogoutLoaderBody = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Xeon Inventory");
        setLocationByPlatform(true);
        setUndecorated(true);
        setOpacity(0.985F);

        pnlFrames.setLayout(new java.awt.CardLayout());

        pnlEmployeeDashboard.setBackground(new java.awt.Color(0, 0, 0));
        pnlEmployeeDashboard.setPreferredSize(new java.awt.Dimension(1200, 690));

        pnlTop.setBackground(new java.awt.Color(0, 0, 0));
        pnlTop.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(50, 50, 50)));
        pnlTop.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlTopMouseDragged(evt);
            }
        });
        pnlTop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlTopMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlTopMouseReleased(evt);
            }
        });

        pnlTopIcons.setLayout(new java.awt.CardLayout());

        maximizeView.setBackground(new java.awt.Color(0, 0, 0));

        minimize1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minimize1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/transparent_titlebar_minimize.png"))); // NOI18N
        minimize1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minimize1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                minimize1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                minimize1MouseExited(evt);
            }
        });

        maximize1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        maximize1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/transparent_titlebar_maximize_state_1.png"))); // NOI18N
        maximize1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                maximize1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                maximize1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                maximize1MouseExited(evt);
            }
        });

        close1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        close1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/transparent_titlebar_close.png"))); // NOI18N
        close1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                close1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                close1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                close1MouseExited(evt);
            }
        });

        javax.swing.GroupLayout maximizeViewLayout = new javax.swing.GroupLayout(maximizeView);
        maximizeView.setLayout(maximizeViewLayout);
        maximizeViewLayout.setHorizontalGroup(
            maximizeViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(maximizeViewLayout.createSequentialGroup()
                .addComponent(minimize1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maximize1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(close1))
        );
        maximizeViewLayout.setVerticalGroup(
            maximizeViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(minimize1)
            .addComponent(maximize1)
            .addComponent(close1)
        );

        pnlTopIcons.add(maximizeView, "card3");

        restoreDownView.setBackground(new java.awt.Color(0, 0, 0));

        minimize2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        minimize2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/transparent_titlebar_minimize.png"))); // NOI18N
        minimize2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minimize2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                minimize2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                minimize2MouseExited(evt);
            }
        });

        maximize2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        maximize2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/transparent_titlebar_maximize_state_2.png"))); // NOI18N
        maximize2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                maximize2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                maximize2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                maximize2MouseExited(evt);
            }
        });

        close2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        close2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/transparent_titlebar_close.png"))); // NOI18N
        close2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                close2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                close2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                close2MouseExited(evt);
            }
        });

        javax.swing.GroupLayout restoreDownViewLayout = new javax.swing.GroupLayout(restoreDownView);
        restoreDownView.setLayout(restoreDownViewLayout);
        restoreDownViewLayout.setHorizontalGroup(
            restoreDownViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(restoreDownViewLayout.createSequentialGroup()
                .addComponent(minimize2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maximize2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(close2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        restoreDownViewLayout.setVerticalGroup(
            restoreDownViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(minimize2)
            .addComponent(maximize2)
            .addComponent(close2)
        );

        pnlTopIcons.add(restoreDownView, "card3");

        jPanel27.setBackground(new java.awt.Color(35, 35, 35));

        title.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        title.setForeground(new java.awt.Color(153, 153, 153));
        title.setText("Xeon Inventory");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblEmployeeUsername.setBackground(new java.awt.Color(0, 0, 0));
        lblEmployeeUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblEmployeeUsername.setForeground(new java.awt.Color(255, 255, 255));
        lblEmployeeUsername.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEmployeeUsername.setText("Employee");
        lblEmployeeUsername.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblEmployeeUsername.setOpaque(true);
        lblEmployeeUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblEmployeeUsernameMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblEmployeeUsernameMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnlTopLayout = new javax.swing.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblEmployeeUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTopIcons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlTopLayout.setVerticalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTopIcons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblEmployeeUsername, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlSidebar.setBackground(new java.awt.Color(35, 35, 35));
        pnlSidebar.setForeground(new java.awt.Color(255, 255, 255));
        pnlSidebar.setPreferredSize(new java.awt.Dimension(200, 647));

        pnlHome.setBackground(new java.awt.Color(60, 60, 60));
        pnlHome.setForeground(new java.awt.Color(20, 20, 20));
        pnlHome.setPreferredSize(new java.awt.Dimension(250, 55));
        pnlHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlHomeMouseClicked(evt);
            }
        });

        home.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        home.setForeground(new java.awt.Color(255, 255, 255));
        home.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        home.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/home.icon.png"))); // NOI18N
        home.setText("Home");
        home.setIconTextGap(20);
        home.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeMouseClicked(evt);
            }
        });

        pnlIndicatorHome.setBackground(new java.awt.Color(75, 136, 198));
        pnlIndicatorHome.setPreferredSize(new java.awt.Dimension(4, 0));

        javax.swing.GroupLayout pnlIndicatorHomeLayout = new javax.swing.GroupLayout(pnlIndicatorHome);
        pnlIndicatorHome.setLayout(pnlIndicatorHomeLayout);
        pnlIndicatorHomeLayout.setHorizontalGroup(
            pnlIndicatorHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        pnlIndicatorHomeLayout.setVerticalGroup(
            pnlIndicatorHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlHomeLayout = new javax.swing.GroupLayout(pnlHome);
        pnlHome.setLayout(pnlHomeLayout);
        pnlHomeLayout.setHorizontalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIndicatorHome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(home, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlHomeLayout.setVerticalGroup(
            pnlHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addComponent(home, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(pnlHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIndicatorHome, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlPOS.setBackground(new java.awt.Color(35, 35, 35));
        pnlPOS.setForeground(new java.awt.Color(20, 20, 20));
        pnlPOS.setPreferredSize(new java.awt.Dimension(250, 55));
        pnlPOS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlPOSMouseClicked(evt);
            }
        });

        sales.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        sales.setForeground(new java.awt.Color(255, 255, 255));
        sales.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/pos.icon.png"))); // NOI18N
        sales.setText("POS");
        sales.setIconTextGap(20);
        sales.setPreferredSize(new java.awt.Dimension(74, 30));
        sales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salesMouseClicked(evt);
            }
        });

        pnlIndicatorPOS.setBackground(new java.awt.Color(35, 35, 35));
        pnlIndicatorPOS.setPreferredSize(new java.awt.Dimension(4, 0));

        javax.swing.GroupLayout pnlIndicatorPOSLayout = new javax.swing.GroupLayout(pnlIndicatorPOS);
        pnlIndicatorPOS.setLayout(pnlIndicatorPOSLayout);
        pnlIndicatorPOSLayout.setHorizontalGroup(
            pnlIndicatorPOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        pnlIndicatorPOSLayout.setVerticalGroup(
            pnlIndicatorPOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlPOSLayout = new javax.swing.GroupLayout(pnlPOS);
        pnlPOS.setLayout(pnlPOSLayout);
        pnlPOSLayout.setHorizontalGroup(
            pnlPOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPOSLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIndicatorPOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sales, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlPOSLayout.setVerticalGroup(
            pnlPOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pnlPOSLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlIndicatorPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addGroup(pnlPOSLayout.createSequentialGroup()
                .addComponent(sales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        sales.getAccessibleContext().setAccessibleName("");

        pnlSettings.setBackground(new java.awt.Color(35, 35, 35));
        pnlSettings.setForeground(new java.awt.Color(20, 20, 20));
        pnlSettings.setPreferredSize(new java.awt.Dimension(250, 55));
        pnlSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlSettingsMouseClicked(evt);
            }
        });

        reports.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        reports.setForeground(new java.awt.Color(255, 255, 255));
        reports.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        reports.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/settings.icon.png"))); // NOI18N
        reports.setText("Settings");
        reports.setIconTextGap(20);
        reports.setPreferredSize(new java.awt.Dimension(96, 30));
        reports.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reportsMouseClicked(evt);
            }
        });

        pnlIndicatorSettings.setBackground(new java.awt.Color(35, 35, 35));
        pnlIndicatorSettings.setPreferredSize(new java.awt.Dimension(4, 0));

        javax.swing.GroupLayout pnlIndicatorSettingsLayout = new javax.swing.GroupLayout(pnlIndicatorSettings);
        pnlIndicatorSettings.setLayout(pnlIndicatorSettingsLayout);
        pnlIndicatorSettingsLayout.setHorizontalGroup(
            pnlIndicatorSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        pnlIndicatorSettingsLayout.setVerticalGroup(
            pnlIndicatorSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlSettingsLayout = new javax.swing.GroupLayout(pnlSettings);
        pnlSettings.setLayout(pnlSettingsLayout);
        pnlSettingsLayout.setHorizontalGroup(
            pnlSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSettingsLayout.createSequentialGroup()
                .addGap(0, 48, Short.MAX_VALUE)
                .addComponent(reports, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlSettingsLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlIndicatorSettings, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(233, Short.MAX_VALUE)))
        );
        pnlSettingsLayout.setVerticalGroup(
            pnlSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(reports, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(pnlSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlSettingsLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlIndicatorSettings, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        reports.getAccessibleContext().setAccessibleName("");

        pnlBranding.setBackground(new java.awt.Color(35, 35, 35));

        jLabel111.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel111.setForeground(new java.awt.Color(255, 255, 255));
        jLabel111.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel111.setText("Xeon Inventory");
        jLabel111.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        jLabel110.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel110.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel110.setText("Go beyond expectations");
        jLabel110.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout pnlBrandingLayout = new javax.swing.GroupLayout(pnlBranding);
        pnlBranding.setLayout(pnlBrandingLayout);
        pnlBrandingLayout.setHorizontalGroup(
            pnlBrandingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel111, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel110, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlBrandingLayout.setVerticalGroup(
            pnlBrandingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBrandingLayout.createSequentialGroup()
                .addComponent(jLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel110, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 32, Short.MAX_VALUE))
        );

        watermark.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        watermark.setForeground(new java.awt.Color(100, 100, 100));
        watermark.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        watermark.setText("© 0x5un5h1n3");
        watermark.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout pnlSidebarLayout = new javax.swing.GroupLayout(pnlSidebar);
        pnlSidebar.setLayout(pnlSidebarLayout);
        pnlSidebarLayout.setHorizontalGroup(
            pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHome, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
            .addComponent(pnlPOS, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
            .addComponent(pnlSettings, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
            .addComponent(pnlBranding, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(watermark, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlSidebarLayout.setVerticalGroup(
            pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSidebarLayout.createSequentialGroup()
                .addComponent(pnlBranding, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlHome, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlPOS, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(watermark, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        pnlParent.setBackground(new java.awt.Color(0, 0, 0));

        pnlHeader.setBackground(new java.awt.Color(0, 0, 0));
        pnlHeader.setPreferredSize(new java.awt.Dimension(120, 120));

        cardName.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        cardName.setForeground(new java.awt.Color(255, 255, 255));
        cardName.setText("Home");
        cardName.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        pnlLogout.setBackground(new java.awt.Color(0, 0, 0));

        lblLogout.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblLogout.setForeground(new java.awt.Color(255, 255, 255));
        lblLogout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/logout.png"))); // NOI18N
        lblLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLogoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblLogoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblLogoutMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnlLogoutLayout = new javax.swing.GroupLayout(pnlLogout);
        pnlLogout.setLayout(pnlLogoutLayout);
        pnlLogoutLayout.setHorizontalGroup(
            pnlLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblLogout, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
        );
        pnlLogoutLayout.setVerticalGroup(
            pnlLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblLogout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlDateTime.setBackground(new java.awt.Color(0, 0, 0));
        pnlDateTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                pnlDateTimeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                pnlDateTimeMouseExited(evt);
            }
        });

        lblTime.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblTime.setForeground(new java.awt.Color(255, 255, 255));
        lblTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTime.setText("TIME");
        lblTime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblTimeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblTimeMouseExited(evt);
            }
        });

        lblDate.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblDate.setForeground(new java.awt.Color(255, 255, 255));
        lblDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDate.setText("DATE");
        lblDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblDateMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblDateMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnlDateTimeLayout = new javax.swing.GroupLayout(pnlDateTime);
        pnlDateTime.setLayout(pnlDateTimeLayout);
        pnlDateTimeLayout.setHorizontalGroup(
            pnlDateTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(lblDate, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        pnlDateTimeLayout.setVerticalGroup(
            pnlDateTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDateTimeLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lblTime)
                .addGap(6, 6, 6)
                .addComponent(lblDate))
        );

        javax.swing.GroupLayout pnlHeaderLayout = new javax.swing.GroupLayout(pnlHeader);
        pnlHeader.setLayout(pnlHeaderLayout);
        pnlHeaderLayout.setHorizontalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHeaderLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(cardName, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlDateTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlLogout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlHeaderLayout.setVerticalGroup(
            pnlHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlDateTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlLogout, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(cardName, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mainCard.setBackground(new java.awt.Color(35, 35, 35));
        mainCard.setLayout(new java.awt.CardLayout());

        cardHome.setBackground(new java.awt.Color(0, 0, 0));

        pnlHomeSubSelection.setBackground(new java.awt.Color(20, 20, 20));

        jLabel71.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(204, 204, 204));
        jLabel71.setText("Today is Monday, January 1, 2020");

        javax.swing.GroupLayout pnlHomeSubSelectionLayout = new javax.swing.GroupLayout(pnlHomeSubSelection);
        pnlHomeSubSelection.setLayout(pnlHomeSubSelectionLayout);
        pnlHomeSubSelectionLayout.setHorizontalGroup(
            pnlHomeSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeSubSelectionLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(477, Short.MAX_VALUE))
        );
        pnlHomeSubSelectionLayout.setVerticalGroup(
            pnlHomeSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel71, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
        );

        jPanel15.setBackground(new java.awt.Color(20, 47, 91));

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setText("Your Attendance");

        lblHomeEmployeeAttendanceThisMonth.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        lblHomeEmployeeAttendanceThisMonth.setForeground(new java.awt.Color(180, 180, 180));
        lblHomeEmployeeAttendanceThisMonth.setText("This Month : N/A");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHomeEmployeeAttendanceThisMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel60)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHomeEmployeeAttendanceThisMonth)
                .addContainerGap(57, Short.MAX_VALUE))
        );

        jPanel34.setBackground(new java.awt.Color(33, 94, 64));

        jLabel68.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(180, 180, 180));
        jLabel68.setText("Status : N/A");

        jLabel67.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(255, 255, 255));
        jLabel67.setText("Your Salary");

        jLabel72.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(180, 180, 180));
        jLabel72.setText("Salary : N/A");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel72, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                    .addComponent(jLabel67, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(116, 116, 116))
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                                .addComponent(jLabel68)
                                .addGap(30, 30, 30))
                            .addComponent(jLabel72, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(50, 50, 50))))
        );

        jPanel18.setBackground(new java.awt.Color(54, 54, 54));

        jLabel63.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(255, 255, 255));
        jLabel63.setText("Stock Summary");

        lblHomeStockCount.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        lblHomeStockCount.setForeground(new java.awt.Color(180, 180, 180));
        lblHomeStockCount.setText("Stock Count : N/A");

        lblHomeLowStockCount.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        lblHomeLowStockCount.setForeground(new java.awt.Color(180, 180, 180));
        lblHomeLowStockCount.setText("Low Stock Count : N/A");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHomeStockCount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblHomeLowStockCount, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel63)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHomeStockCount, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHomeLowStockCount)
                .addContainerGap(316, Short.MAX_VALUE))
        );

        jPanel29.setBackground(new java.awt.Color(137, 84, 0));

        jLabel57.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("User Summary");

        lblHomeEmployeeCount.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        lblHomeEmployeeCount.setForeground(new java.awt.Color(180, 180, 180));
        lblHomeEmployeeCount.setText("Employee Count : N/A");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHomeEmployeeCount, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel57)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHomeEmployeeCount)
                .addContainerGap(167, Short.MAX_VALUE))
        );

        jPanel30.setBackground(new java.awt.Color(143, 28, 40));

        jLabel53.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(180, 180, 180));
        jLabel53.setText("Monthly Count : N/A");

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Sales Summary");

        jLabel52.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(180, 180, 180));
        jLabel52.setText("Today Count : N/A");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel52)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel53)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        loginWish.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        loginWish.setForeground(new java.awt.Color(255, 255, 255));
        loginWish.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        loginWish.setText("Good morning");
        loginWish.setToolTipText("");

        employeeName.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        employeeName.setForeground(new java.awt.Color(255, 255, 255));
        employeeName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        employeeName.setText("Employee");

        javax.swing.GroupLayout cardHomeLayout = new javax.swing.GroupLayout(cardHome);
        cardHome.setLayout(cardHomeLayout);
        cardHomeLayout.setHorizontalGroup(
            cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHomeSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(cardHomeLayout.createSequentialGroup()
                .addGroup(cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardHomeLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cardHomeLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(loginWish)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(employeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardHomeLayout.setVerticalGroup(
            cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardHomeLayout.createSequentialGroup()
                .addComponent(pnlHomeSubSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loginWish, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(employeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(cardHomeLayout.createSequentialGroup()
                        .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cardHomeLayout.createSequentialGroup()
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        mainCard.add(cardHome, "card2");

        cardPOS.setBackground(new java.awt.Color(0, 0, 0));

        pnlPOSSubSelection.setBackground(new java.awt.Color(20, 20, 20));

        btn_employees.setBackground(new java.awt.Color(60, 60, 60));
        btn_employees.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_employees.setForeground(new java.awt.Color(255, 255, 255));
        btn_employees.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_employees.setText("Invoice");
        btn_employees.setOpaque(true);
        btn_employees.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_employeesMouseClicked(evt);
            }
        });

        btn_employees1.setBackground(new java.awt.Color(35, 35, 35));
        btn_employees1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_employees1.setForeground(new java.awt.Color(255, 255, 255));
        btn_employees1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_employees1.setText("Stock");
        btn_employees1.setOpaque(true);
        btn_employees1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_employees1MouseClicked(evt);
            }
        });

        btn_employees2.setBackground(new java.awt.Color(35, 35, 35));
        btn_employees2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_employees2.setForeground(new java.awt.Color(255, 255, 255));
        btn_employees2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_employees2.setText("Sales");
        btn_employees2.setOpaque(true);
        btn_employees2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_employees2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlPOSSubSelectionLayout = new javax.swing.GroupLayout(pnlPOSSubSelection);
        pnlPOSSubSelection.setLayout(pnlPOSSubSelectionLayout);
        pnlPOSSubSelectionLayout.setHorizontalGroup(
            pnlPOSSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPOSSubSelectionLayout.createSequentialGroup()
                .addComponent(btn_employees, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(btn_employees1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(btn_employees2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlPOSSubSelectionLayout.setVerticalGroup(
            pnlPOSSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPOSSubSelectionLayout.createSequentialGroup()
                .addGroup(pnlPOSSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_employees, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_employees1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_employees2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        subCardPOS.setLayout(new java.awt.CardLayout());

        card1.setBackground(new java.awt.Color(20, 20, 20));

        jTabbedPaneInvoice.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPaneInvoice.setFocusable(false);
        jTabbedPaneInvoice.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPaneInvoice.setOpaque(true);

        jPanel11.setBackground(new java.awt.Color(0, 0, 0));

        jPanel25.setBackground(new java.awt.Color(0, 0, 0));

        jLabel133.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel133.setForeground(new java.awt.Color(255, 255, 255));
        jLabel133.setText("Item Name");

        txtInvoiceItemName.setBackground(new java.awt.Color(45, 45, 45));
        txtInvoiceItemName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceItemName.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceItemName.setEnabled(false);

        jLabel134.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel134.setForeground(new java.awt.Color(255, 255, 255));
        jLabel134.setText("Discount");

        jLabel135.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel135.setForeground(new java.awt.Color(255, 255, 255));
        jLabel135.setText("New Selling Price");

        btnUpdateSelectedEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnUpdateSelectedEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnUpdateSelectedEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateSelectedEmployee.setText("Add Item");
        btnUpdateSelectedEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateSelectedEmployeeActionPerformed(evt);
            }
        });

        jLabel156.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel156.setForeground(new java.awt.Color(255, 255, 255));
        jLabel156.setText("Item Code");

        txtInvoiceItemCode.setBackground(new java.awt.Color(18, 18, 18));
        txtInvoiceItemCode.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceItemCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceItemCodeKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInvoiceItemCodeKeyReleased(evt);
            }
        });

        btnDeleteSelectedEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnDeleteSelectedEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeleteSelectedEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteSelectedEmployee.setText("View Items");
        btnDeleteSelectedEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSelectedEmployeeActionPerformed(evt);
            }
        });

        btnPrintSelectedEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintSelectedEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintSelectedEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintSelectedEmployee.setText("Clear");
        btnPrintSelectedEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintSelectedEmployeeActionPerformed(evt);
            }
        });

        txtInvoiceItemAmount.setBackground(new java.awt.Color(18, 18, 18));
        txtInvoiceItemAmount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceItemAmount.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceItemAmount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceItemAmountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInvoiceItemAmountKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtInvoiceItemAmountKeyTyped(evt);
            }
        });

        cmbInvoiceItemDiscount.setBackground(new java.awt.Color(18, 18, 18));
        cmbInvoiceItemDiscount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbInvoiceItemDiscount.setForeground(new java.awt.Color(255, 255, 255));
        cmbInvoiceItemDiscount.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0%", "5%", "10%", "15%", "20%", "25%", "30%", "35%" }));
        cmbInvoiceItemDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbInvoiceItemDiscountKeyPressed(evt);
            }
        });

        jLabel140.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel140.setForeground(new java.awt.Color(255, 255, 255));
        jLabel140.setText("Purchase Price");

        txtInvoiceItemPurchasePrice.setBackground(new java.awt.Color(45, 45, 45));
        txtInvoiceItemPurchasePrice.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceItemPurchasePrice.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceItemPurchasePrice.setEnabled(false);

        txtInvoiceNewItemPrice.setBackground(new java.awt.Color(45, 45, 45));
        txtInvoiceNewItemPrice.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceNewItemPrice.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceNewItemPrice.setEnabled(false);

        jLabel141.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel141.setForeground(new java.awt.Color(255, 255, 255));
        jLabel141.setText("Amount");

        jLabel142.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel142.setForeground(new java.awt.Color(255, 255, 255));
        jLabel142.setText("Selling Price");

        txtInvoiceItemSellingPrice.setBackground(new java.awt.Color(45, 45, 45));
        txtInvoiceItemSellingPrice.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceItemSellingPrice.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceItemSellingPrice.setEnabled(false);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(btnUpdateSelectedEmployee)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteSelectedEmployee)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintSelectedEmployee))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel135, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel134, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel133, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel156, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel140, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel141, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel142, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtInvoiceItemName)
                            .addComponent(txtInvoiceItemCode)
                            .addComponent(txtInvoiceItemPurchasePrice)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(cmbInvoiceItemDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtInvoiceItemAmount)
                            .addComponent(txtInvoiceNewItemPrice)
                            .addComponent(txtInvoiceItemSellingPrice)))))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceItemCode)
                    .addComponent(jLabel156, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceItemName)
                    .addComponent(jLabel133, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceItemPurchasePrice)
                    .addComponent(jLabel140, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceItemSellingPrice)
                    .addComponent(jLabel142, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel134)
                    .addComponent(cmbInvoiceItemDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel135, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInvoiceNewItemPrice))
                .addGap(18, 18, 18)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel141, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInvoiceItemAmount))
                .addGap(35, 35, 35)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateSelectedEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteSelectedEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrintSelectedEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(18, 18, 18));

        jLabel137.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel137.setForeground(new java.awt.Color(255, 255, 255));
        jLabel137.setText("Stock Count");

        txtInvoiceItemStockCount.setBackground(new java.awt.Color(45, 45, 45));
        txtInvoiceItemStockCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceItemStockCount.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceItemStockCount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtInvoiceItemStockCount.setEnabled(false);

        jLabel138.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel138.setForeground(new java.awt.Color(255, 255, 255));
        jLabel138.setText("Low Stock");

        txtInvoiceItemLowStockCount.setBackground(new java.awt.Color(45, 45, 45));
        txtInvoiceItemLowStockCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceItemLowStockCount.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceItemLowStockCount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtInvoiceItemLowStockCount.setEnabled(false);

        jLabel143.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel143.setForeground(new java.awt.Color(255, 255, 255));
        jLabel143.setText("Stock Status");

        txtInvoiceItemStockStatus.setEditable(false);
        txtInvoiceItemStockStatus.setBackground(new java.awt.Color(18, 18, 18));
        txtInvoiceItemStockStatus.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceItemStockStatus.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceItemStockStatus.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtInvoiceItemStockStatus.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)));
        txtInvoiceItemStockStatus.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel138, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel143, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(jLabel137, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceItemStockCount)
                    .addComponent(txtInvoiceItemLowStockCount)
                    .addComponent(txtInvoiceItemStockStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceItemStockCount)
                    .addComponent(jLabel137, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceItemLowStockCount)
                    .addComponent(jLabel138, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceItemStockStatus)
                    .addComponent(jLabel143, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        jPanel4.setBackground(new java.awt.Color(18, 18, 18));

        jLabel155.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel155.setForeground(new java.awt.Color(255, 255, 255));
        jLabel155.setText("Invoice Id");

        txtInvoiceId.setEditable(false);
        txtInvoiceId.setBackground(new java.awt.Color(45, 45, 45));
        txtInvoiceId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtInvoiceId.setText("1");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel155, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtInvoiceId, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel155, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInvoiceId))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPaneInvoice.addTab("Add Items", jPanel11);

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));

        tblInvoice.setBackground(new java.awt.Color(0, 0, 0));
        tblInvoice.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblInvoice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice Item Id", "Item Name", "Selling Price", "Discount", "New Selling Price", "Amount", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInvoice.setGridColor(new java.awt.Color(70, 70, 70));
        tblInvoice.setRequestFocusEnabled(false);
        tblInvoice.setRowHeight(25);
        tblInvoice.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblInvoice.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInvoiceMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tblInvoice);

        btnEditEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnEditEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEditEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnEditEmployee.setText("Delete Item");
        btnEditEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditEmployeeActionPerformed(evt);
            }
        });

        btnDeleteEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnDeleteEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeleteEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteEmployee.setText("Add Item");
        btnDeleteEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteEmployeeActionPerformed(evt);
            }
        });

        btnPrintAllEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintAllEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintAllEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintAllEmployee.setText("Payments");
        btnPrintAllEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintAllEmployeeActionPerformed(evt);
            }
        });

        txtSearchInvoice.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchInvoice.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchInvoice.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchInvoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchInvoiceKeyReleased(evt);
            }
        });

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        btnRefreshEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshEmployee.setText("Refresh");
        btnRefreshEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshEmployeeActionPerformed(evt);
            }
        });

        cmbSortInvoice.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortInvoice.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortInvoice.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Invoice Id", "Item Name", "Item Code" }));
        cmbSortInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortInvoiceActionPerformed(evt);
            }
        });

        lblInvoiceId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblInvoiceId.setForeground(new java.awt.Color(153, 153, 153));
        lblInvoiceId.setText("Invoice Id : ");

        lblInvoiceTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblInvoiceTotal.setForeground(new java.awt.Color(153, 153, 153));
        lblInvoiceTotal.setText("Total : 0");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 153, 153));
        jLabel4.setText("|");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(txtSearchInvoice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(lblInvoiceId, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblInvoiceTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addComponent(btnRefreshEmployee)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditEmployee)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteEmployee)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintAllEmployee))
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(20, 20, 20))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbSortInvoice, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtSearchInvoice, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrintAllEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefreshEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInvoiceId)
                    .addComponent(lblInvoiceTotal)
                    .addComponent(jLabel4))
                .addGap(20, 20, 20))
        );

        jTabbedPaneInvoice.addTab("View Invoice", jPanel10);

        jPanel13.setBackground(new java.awt.Color(0, 0, 0));

        jPanel26.setBackground(new java.awt.Color(0, 0, 0));

        jLabel139.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel139.setForeground(new java.awt.Color(255, 255, 255));
        jLabel139.setText("Balance");

        txtInvoiceAmountBalance.setBackground(new java.awt.Color(45, 45, 45));
        txtInvoiceAmountBalance.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceAmountBalance.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceAmountBalance.setEnabled(false);

        jLabel157.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel157.setForeground(new java.awt.Color(255, 255, 255));
        jLabel157.setText("Amount Due");

        txtInvoiceAmountDue.setEditable(false);
        txtInvoiceAmountDue.setBackground(new java.awt.Color(45, 45, 45));
        txtInvoiceAmountDue.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        btnUpdateSelectedEmployee1.setBackground(new java.awt.Color(40, 40, 40));
        btnUpdateSelectedEmployee1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnUpdateSelectedEmployee1.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateSelectedEmployee1.setText("Issue");
        btnUpdateSelectedEmployee1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateSelectedEmployee1ActionPerformed(evt);
            }
        });

        jLabel158.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel158.setForeground(new java.awt.Color(255, 255, 255));
        jLabel158.setText("Paid Amount");

        txtInvoiceAmountPaid.setBackground(new java.awt.Color(18, 18, 18));
        txtInvoiceAmountPaid.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceAmountPaid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInvoiceAmountPaidessed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInvoiceAmountPaidKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtInvoiceAmountPaidKeyTyped(evt);
            }
        });

        btnDeleteSelectedEmployee1.setBackground(new java.awt.Color(40, 40, 40));
        btnDeleteSelectedEmployee1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeleteSelectedEmployee1.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteSelectedEmployee1.setText("Print Invoice");
        btnDeleteSelectedEmployee1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSelectedEmployee1ActionPerformed(evt);
            }
        });

        jLabel144.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel144.setForeground(new java.awt.Color(255, 255, 255));
        jLabel144.setText("Savings");

        txtInvoiceAmountSavings.setBackground(new java.awt.Color(45, 45, 45));
        txtInvoiceAmountSavings.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtInvoiceAmountSavings.setForeground(new java.awt.Color(255, 255, 255));
        txtInvoiceAmountSavings.setEnabled(false);

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel139, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel157, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel158, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel144, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtInvoiceAmountBalance, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtInvoiceAmountPaid)
                            .addComponent(txtInvoiceAmountDue)
                            .addComponent(txtInvoiceAmountSavings, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(btnUpdateSelectedEmployee1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteSelectedEmployee1)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceAmountDue)
                    .addComponent(jLabel157, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceAmountPaid)
                    .addComponent(jLabel158, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceAmountBalance)
                    .addComponent(jLabel139, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtInvoiceAmountSavings)
                    .addComponent(jLabel144, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateSelectedEmployee1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteSelectedEmployee1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(66, 66, 66))
        );

        lblSelectedEmployeePositionValidation1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeePositionValidation1.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedEmployeePositionValidation2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeePositionValidation2.setForeground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSelectedEmployeePositionValidation1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSelectedEmployeePositionValidation2, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(85, 85, 85)
                        .addComponent(lblSelectedEmployeePositionValidation2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedEmployeePositionValidation1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(138, Short.MAX_VALUE))
        );

        jTabbedPaneInvoice.addTab("Payments", jPanel13);

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneInvoice)
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneInvoice)
        );

        subCardPOS.add(card1, "card3");

        card2.setBackground(new java.awt.Color(0, 0, 0));

        jTabbedPaneStock.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPaneStock.setFocusable(false);
        jTabbedPaneStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPaneStock.setOpaque(true);

        jPanel20.setBackground(new java.awt.Color(0, 0, 0));

        tblStock.setBackground(new java.awt.Color(0, 0, 0));
        tblStock.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SKU Id", "Item Code", "Item Name", "Selling Price", "Stock Count", "Low Stock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblStock.setGridColor(new java.awt.Color(70, 70, 70));
        tblStock.setRequestFocusEnabled(false);
        tblStock.setRowHeight(25);
        tblStock.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblStock.getTableHeader().setReorderingAllowed(false);
        jScrollPane19.setViewportView(tblStock);

        btnPrintAllEmployee1.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintAllEmployee1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintAllEmployee1.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintAllEmployee1.setText("Print Barcodes");
        btnPrintAllEmployee1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintAllEmployee1ActionPerformed(evt);
            }
        });

        txtSearchStock.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchStock.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchStockKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchStockKeyTyped(evt);
            }
        });

        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        cmbSortStock.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortStock.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SKU Id", "Item Code", "Item Name" }));
        cmbSortStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortStockActionPerformed(evt);
            }
        });

        lblStockCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblStockCount.setForeground(new java.awt.Color(153, 153, 153));
        lblStockCount.setText("SKU Count : 0");

        btnPrintAllEmployee4.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintAllEmployee4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintAllEmployee4.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintAllEmployee4.setText("Refresh");
        btnPrintAllEmployee4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintAllEmployee4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(txtSearchStock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortStock, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                        .addComponent(lblStockCount, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPrintAllEmployee4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintAllEmployee1))
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 902, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbSortStock, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtSearchStock, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrintAllEmployee1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStockCount)
                    .addComponent(btnPrintAllEmployee4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jTabbedPaneStock.addTab("Available Stock", jPanel20);

        jPanel8.setBackground(new java.awt.Color(0, 0, 0));

        jLabel49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        txtSearchLowStock.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchLowStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchLowStock.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchLowStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchLowStockKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchLowStockKeyTyped(evt);
            }
        });

        cmbSortLowStock.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortLowStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortLowStock.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SKU Id", "Item Code", "Item Name" }));
        cmbSortLowStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortLowStockActionPerformed(evt);
            }
        });

        tblLowStock.setBackground(new java.awt.Color(0, 0, 0));
        tblLowStock.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblLowStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " SKU Id", "Item Code", "Item Name", "Selling Price", "Stock Count", "Low Stock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblLowStock.setGridColor(new java.awt.Color(70, 70, 70));
        tblLowStock.setRequestFocusEnabled(false);
        tblLowStock.setRowHeight(25);
        tblLowStock.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblLowStock.getTableHeader().setReorderingAllowed(false);
        jScrollPane24.setViewportView(tblLowStock);

        lblLowStockCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblLowStockCount.setForeground(new java.awt.Color(153, 153, 153));
        lblLowStockCount.setText("SKU Count : 0");

        btnPrintAllEmployee2.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintAllEmployee2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintAllEmployee2.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintAllEmployee2.setText("Refresh");
        btnPrintAllEmployee2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintAllEmployee2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(lblLowStockCount, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPrintAllEmployee2))
                    .addComponent(jScrollPane24, javax.swing.GroupLayout.DEFAULT_SIZE, 902, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(txtSearchLowStock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortLowStock, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel49)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearchLowStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbSortLowStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane24, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLowStockCount)
                    .addComponent(btnPrintAllEmployee2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jTabbedPaneStock.addTab("Low Stock", jPanel8);

        javax.swing.GroupLayout card2Layout = new javax.swing.GroupLayout(card2);
        card2.setLayout(card2Layout);
        card2Layout.setHorizontalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneStock)
        );
        card2Layout.setVerticalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneStock)
        );

        subCardPOS.add(card2, "card4");

        card3.setBackground(new java.awt.Color(0, 0, 0));

        jTabbedPaneStock1.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPaneStock1.setFocusable(false);
        jTabbedPaneStock1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPaneStock1.setOpaque(true);

        jPanel28.setBackground(new java.awt.Color(0, 0, 0));

        txtSearchTodaySales.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchTodaySales.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchTodaySales.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchTodaySales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchTodaySalesKeyReleased(evt);
            }
        });

        cmbSortTodaySales.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortTodaySales.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortTodaySales.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sales / Invoice Id", "Item Name" }));
        cmbSortTodaySales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortTodaySalesActionPerformed(evt);
            }
        });

        jLabel132.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        tblTodaySales.setBackground(new java.awt.Color(0, 0, 0));
        tblTodaySales.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblTodaySales.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sales  / Invoice Id", "Item Name", "Sold Price", "Amount", "Total", "Billed by"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTodaySales.setGridColor(new java.awt.Color(70, 70, 70));
        tblTodaySales.setRequestFocusEnabled(false);
        tblTodaySales.setRowHeight(25);
        tblTodaySales.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblTodaySales.getTableHeader().setReorderingAllowed(false);
        jScrollPane27.setViewportView(tblTodaySales);

        btnRefreshMonthlyEmployeeAttendance23.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshMonthlyEmployeeAttendance23.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshMonthlyEmployeeAttendance23.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshMonthlyEmployeeAttendance23.setText("Refresh");
        btnRefreshMonthlyEmployeeAttendance23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshMonthlyEmployeeAttendance23ActionPerformed(evt);
            }
        });

        lblTodaySalesCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTodaySalesCount.setForeground(new java.awt.Color(153, 153, 153));
        lblTodaySalesCount.setText("Sales Count : 0");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane27, javax.swing.GroupLayout.DEFAULT_SIZE, 902, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel28Layout.createSequentialGroup()
                        .addComponent(lblTodaySalesCount, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshMonthlyEmployeeAttendance23))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(txtSearchTodaySales)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortTodaySales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel132)))
                .addGap(20, 20, 20))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel132)
                    .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearchTodaySales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbSortTodaySales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane27, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefreshMonthlyEmployeeAttendance23, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTodaySalesCount))
                .addGap(20, 20, 20))
        );

        jTabbedPaneStock1.addTab("View Your Today Sales", jPanel28);

        jPanel19.setBackground(new java.awt.Color(0, 0, 0));

        txtSearchSalesHistory.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchSalesHistory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchSalesHistory.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchSalesHistory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchSalesHistoryKeyReleased(evt);
            }
        });

        cmbSortSalesHistory.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortSalesHistory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortSalesHistory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sales / Invoice Id", "Item Name" }));
        cmbSortSalesHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortSalesHistoryActionPerformed(evt);
            }
        });

        jLabel131.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        tblSalesHistory.setBackground(new java.awt.Color(0, 0, 0));
        tblSalesHistory.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblSalesHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sales / Invoice Id", "Item Name", "Sold Price", "Amount", "Total", "Billed by", "Date / Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSalesHistory.setGridColor(new java.awt.Color(70, 70, 70));
        tblSalesHistory.setRequestFocusEnabled(false);
        tblSalesHistory.setRowHeight(25);
        tblSalesHistory.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblSalesHistory.getTableHeader().setReorderingAllowed(false);
        jScrollPane26.setViewportView(tblSalesHistory);

        btnRefreshMonthlyEmployeeAttendance24.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshMonthlyEmployeeAttendance24.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshMonthlyEmployeeAttendance24.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshMonthlyEmployeeAttendance24.setText("Refresh");
        btnRefreshMonthlyEmployeeAttendance24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshMonthlyEmployeeAttendance24ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshMonthlyEmployeeAttendance24))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane26, javax.swing.GroupLayout.DEFAULT_SIZE, 902, Short.MAX_VALUE)
                            .addGroup(jPanel19Layout.createSequentialGroup()
                                .addComponent(txtSearchSalesHistory)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbSortSalesHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel131)))))
                .addGap(20, 20, 20))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel131)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearchSalesHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbSortSalesHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane26, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnRefreshMonthlyEmployeeAttendance24, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        jTabbedPaneStock1.addTab("Your Sales History", jPanel19);

        javax.swing.GroupLayout card3Layout = new javax.swing.GroupLayout(card3);
        card3.setLayout(card3Layout);
        card3Layout.setHorizontalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneStock1)
        );
        card3Layout.setVerticalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneStock1)
        );

        subCardPOS.add(card3, "card4");

        javax.swing.GroupLayout cardPOSLayout = new javax.swing.GroupLayout(cardPOS);
        cardPOS.setLayout(cardPOSLayout);
        cardPOSLayout.setHorizontalGroup(
            cardPOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPOSSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(subCardPOS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardPOSLayout.setVerticalGroup(
            cardPOSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardPOSLayout.createSequentialGroup()
                .addComponent(pnlPOSSubSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subCardPOS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainCard.add(cardPOS, "card2");

        cardSettings.setBackground(new java.awt.Color(0, 0, 0));

        pnlSettingsSubSelection.setBackground(new java.awt.Color(20, 20, 20));

        jLabel17.setBackground(new java.awt.Color(60, 60, 60));
        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Backup");
        jLabel17.setOpaque(true);
        jLabel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel17MouseClicked(evt);
            }
        });

        jLabel34.setBackground(new java.awt.Color(35, 35, 35));
        jLabel34.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("Restore");
        jLabel34.setOpaque(true);
        jLabel34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel34MouseClicked(evt);
            }
        });

        jLabel42.setBackground(new java.awt.Color(35, 35, 35));
        jLabel42.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("About");
        jLabel42.setOpaque(true);
        jLabel42.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel42MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlSettingsSubSelectionLayout = new javax.swing.GroupLayout(pnlSettingsSubSelection);
        pnlSettingsSubSelection.setLayout(pnlSettingsSubSelectionLayout);
        pnlSettingsSubSelectionLayout.setHorizontalGroup(
            pnlSettingsSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSettingsSubSelectionLayout.createSequentialGroup()
                .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSettingsSubSelectionLayout.setVerticalGroup(
            pnlSettingsSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
            .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        subCardSettings.setLayout(new java.awt.CardLayout());

        card4.setBackground(new java.awt.Color(0, 0, 0));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Backup Database");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(150, 150, 150));
        jLabel7.setText("Select Location");

        txtBackupLocation.setBackground(new java.awt.Color(18, 18, 18));
        txtBackupLocation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtBackupLocation.setForeground(new java.awt.Color(255, 255, 255));

        btnAddEmployee1.setBackground(new java.awt.Color(40, 40, 40));
        btnAddEmployee1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAddEmployee1.setForeground(new java.awt.Color(255, 255, 255));
        btnAddEmployee1.setText("Browse Path");
        btnAddEmployee1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddEmployee1ActionPerformed(evt);
            }
        });

        btnAddEmployee2.setBackground(new java.awt.Color(40, 40, 40));
        btnAddEmployee2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAddEmployee2.setForeground(new java.awt.Color(255, 255, 255));
        btnAddEmployee2.setText("Backup");
        btnAddEmployee2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddEmployee2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout card4Layout = new javax.swing.GroupLayout(card4);
        card4.setLayout(card4Layout);
        card4Layout.setHorizontalGroup(
            card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card4Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(card4Layout.createSequentialGroup()
                        .addComponent(btnAddEmployee1)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddEmployee2))
                    .addComponent(txtBackupLocation))
                .addContainerGap(469, Short.MAX_VALUE))
        );
        card4Layout.setVerticalGroup(
            card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card4Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(txtBackupLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddEmployee1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddEmployee2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(324, Short.MAX_VALUE))
        );

        subCardSettings.add(card4, "card2");

        card5.setBackground(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Restore Database");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(150, 150, 150));
        jLabel11.setText("Open Location");

        txtRestoreLocation.setBackground(new java.awt.Color(18, 18, 18));
        txtRestoreLocation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtRestoreLocation.setForeground(new java.awt.Color(255, 255, 255));

        btnAddEmployee5.setBackground(new java.awt.Color(40, 40, 40));
        btnAddEmployee5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAddEmployee5.setForeground(new java.awt.Color(255, 255, 255));
        btnAddEmployee5.setText("Browse Path");
        btnAddEmployee5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddEmployee5ActionPerformed(evt);
            }
        });

        btnAddEmployee6.setBackground(new java.awt.Color(40, 40, 40));
        btnAddEmployee6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAddEmployee6.setForeground(new java.awt.Color(255, 255, 255));
        btnAddEmployee6.setText("Restore");
        btnAddEmployee6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddEmployee6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout card5Layout = new javax.swing.GroupLayout(card5);
        card5.setLayout(card5Layout);
        card5Layout.setHorizontalGroup(
            card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card5Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(card5Layout.createSequentialGroup()
                            .addComponent(btnAddEmployee5)
                            .addGap(18, 18, 18)
                            .addComponent(btnAddEmployee6)))
                    .addComponent(txtRestoreLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(469, Short.MAX_VALUE))
        );
        card5Layout.setVerticalGroup(
            card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card5Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(txtRestoreLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddEmployee5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddEmployee6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(324, Short.MAX_VALUE))
        );

        subCardSettings.add(card5, "card3");

        card6.setBackground(new java.awt.Color(0, 0, 0));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/logo.png"))); // NOI18N

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Developed by");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("0x5un5h1n3 ");

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/xeon_logo.png"))); // NOI18N

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Xeon Inventory");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(204, 204, 204));
        jLabel23.setText("Go beyond expectations");

        version1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        version1.setForeground(new java.awt.Color(180, 180, 180));
        version1.setText("© 0x5un5h1n3  | Xeon Inventory | Version 20.07");
        version1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel24.setBackground(new java.awt.Color(0, 0, 0));
        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(180, 180, 180));
        jLabel24.setText("Xeon is an Inventory Management software for growing business");

        jSeparator4.setForeground(new java.awt.Color(150, 150, 150));
        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(120, 120, 120));
        jLabel25.setText("Increase your sales and keep track of every unit with our powerful stock management, order fulfillment, ");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(120, 120, 120));
        jLabel50.setText("user management and inventory control software ");

        jLabel54.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(120, 120, 120));
        jLabel54.setText("Run a more efficient business :)");

        javax.swing.GroupLayout card6Layout = new javax.swing.GroupLayout(card6);
        card6.setLayout(card6Layout);
        card6Layout.setHorizontalGroup(
            card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card6Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel50)
                    .addComponent(jLabel25)
                    .addComponent(version1)
                    .addGroup(card6Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addGroup(card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(card6Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel24)
                    .addComponent(jLabel54))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        card6Layout.setVerticalGroup(
            card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card6Layout.createSequentialGroup()
                .addGroup(card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card6Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addGroup(card6Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(card6Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(card6Layout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(card6Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(49, 49, 49)
                .addComponent(jLabel24)
                .addGap(18, 18, 18)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel50)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel54)
                .addGap(38, 38, 38)
                .addComponent(version1)
                .addContainerGap(162, Short.MAX_VALUE))
        );

        subCardSettings.add(card6, "card4");

        javax.swing.GroupLayout cardSettingsLayout = new javax.swing.GroupLayout(cardSettings);
        cardSettings.setLayout(cardSettingsLayout);
        cardSettingsLayout.setHorizontalGroup(
            cardSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlSettingsSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(subCardSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardSettingsLayout.setVerticalGroup(
            cardSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardSettingsLayout.createSequentialGroup()
                .addComponent(pnlSettingsSubSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subCardSettings, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainCard.add(cardSettings, "card2");

        javax.swing.GroupLayout pnlParentLayout = new javax.swing.GroupLayout(pnlParent);
        pnlParent.setLayout(pnlParentLayout);
        pnlParentLayout.setHorizontalGroup(
            pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParentLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 936, Short.MAX_VALUE)
                    .addComponent(mainCard, javax.swing.GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlParentLayout.setVerticalGroup(
            pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParentLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(7, 7, 7))
        );

        javax.swing.GroupLayout pnlEmployeeDashboardLayout = new javax.swing.GroupLayout(pnlEmployeeDashboard);
        pnlEmployeeDashboard.setLayout(pnlEmployeeDashboardLayout);
        pnlEmployeeDashboardLayout.setHorizontalGroup(
            pnlEmployeeDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pnlTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlEmployeeDashboardLayout.createSequentialGroup()
                .addComponent(pnlSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlParent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlEmployeeDashboardLayout.setVerticalGroup(
            pnlEmployeeDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmployeeDashboardLayout.createSequentialGroup()
                .addComponent(pnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlEmployeeDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlParent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        pnlFrames.add(pnlEmployeeDashboard, "card2");

        pnlLogoutLoaderBody.setBackground(new java.awt.Color(35, 35, 35));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Logging out");
        jLabel5.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/loading.gif"))); // NOI18N
        jLabel6.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout pnlLogoutLoaderBodyLayout = new javax.swing.GroupLayout(pnlLogoutLoaderBody);
        pnlLogoutLoaderBody.setLayout(pnlLogoutLoaderBodyLayout);
        pnlLogoutLoaderBodyLayout.setHorizontalGroup(
            pnlLogoutLoaderBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
        );
        pnlLogoutLoaderBodyLayout.setVerticalGroup(
            pnlLogoutLoaderBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLogoutLoaderBodyLayout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                .addGap(50, 50, 50)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlLogoutLoaderLayout = new javax.swing.GroupLayout(pnlLogoutLoader);
        pnlLogoutLoader.setLayout(pnlLogoutLoaderLayout);
        pnlLogoutLoaderLayout.setHorizontalGroup(
            pnlLogoutLoaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlLogoutLoaderBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlLogoutLoaderLayout.setVerticalGroup(
            pnlLogoutLoaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlLogoutLoaderBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlFrames.add(pnlLogoutLoader, "card3");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFrames, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFrames, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1200, 690));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pnlTopMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTopMousePressed
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            if (EmployeeDashboard.this.getExtendedState() == MAXIMIZED_BOTH) {
                EmployeeDashboard.this.setExtendedState(JFrame.NORMAL);
                maximizeView.setVisible(false);
                restoreDownView.setVisible(true);
            } else {
                EmployeeDashboard.this.setExtendedState(MAXIMIZED_BOTH);
                maximizeView.setVisible(true);
                restoreDownView.setVisible(false);
            }
        }

        mousepX = evt.getX();
        mousepY = evt.getY();


    }//GEN-LAST:event_pnlTopMousePressed

    private void pnlTopMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTopMouseDragged
        int locX = evt.getXOnScreen();
        int locY = evt.getYOnScreen();

        if (EmployeeDashboard.this.getExtendedState() != MAXIMIZED_BOTH) {
            this.setLocation(locX - mousepX, locY - mousepY);
            setOpacity((float) (0.97));
        }

    }//GEN-LAST:event_pnlTopMouseDragged

    private void homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMouseClicked
        setPnlColor(pnlHome);
        resetPnlColor(pnlPOS);
        resetPnlColor(pnlSettings);

        setPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorPOS);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Home");

        cardHome.setVisible(true);
        cardPOS.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_homeMouseClicked

    private void salesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesMouseClicked
        resetPnlColor(pnlHome);
        setPnlColor(pnlPOS);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        setPnlIndicatorColor(pnlIndicatorPOS);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Point of Sales");

        cardHome.setVisible(false);
        cardPOS.setVisible(true);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_salesMouseClicked

    private void reportsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportsMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlPOS);
        setPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorPOS);
        setPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Settings");

        cardHome.setVisible(false);
        cardPOS.setVisible(false);
        cardSettings.setVisible(true);
    }//GEN-LAST:event_reportsMouseClicked

    private void lblLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLogoutMouseClicked

        int choose = JOptionPane.showConfirmDialog(this,
                "Do you want to Logout the application?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (choose == JOptionPane.YES_OPTION) {

            String logoutActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM employee WHERE username= '" + lblEmployeeUsername.getText() + "'),'Employee','" + lblEmployeeUsername.getText() + "','Employee Logout', '" + lblEmployeeUsername.getText() + " logged out' , 'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            pnlLogoutLoader.setVisible(true);
            pnlEmployeeDashboard.setVisible(false);

            new java.util.Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    for (float i = 0.985f; i > 0; i -= 0.000001) {
                        EmployeeDashboard.this.setOpacity(i);

                    }
                    EmployeeDashboard.this.dispose();

                    Login login = new Login();
                    login.setVisible(true);
                    for (float j = 0f; j < 0.985; j += 0.000001) {
                        login.setOpacity(j);
                    }

                }
            }, 1000 * 1);

        } else {

        }
    }//GEN-LAST:event_lblLogoutMouseClicked

    private void lblLogoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLogoutMouseEntered
        pnlLogout.setBackground(new Color(35, 35, 35));
    }//GEN-LAST:event_lblLogoutMouseEntered

    private void lblLogoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLogoutMouseExited
        pnlLogout.setBackground(new Color(0, 0, 0));
    }//GEN-LAST:event_lblLogoutMouseExited

    private void lblTimeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTimeMouseEntered
        pnlDateTime.setBackground(new Color(35, 35, 35));
    }//GEN-LAST:event_lblTimeMouseEntered

    private void lblTimeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTimeMouseExited
        pnlDateTime.setBackground(new Color(0, 0, 0));
    }//GEN-LAST:event_lblTimeMouseExited

    private void lblDateMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDateMouseEntered
        pnlDateTime.setBackground(new Color(35, 35, 35));
    }//GEN-LAST:event_lblDateMouseEntered

    private void lblDateMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDateMouseExited
        pnlDateTime.setBackground(new Color(0, 0, 0));
    }//GEN-LAST:event_lblDateMouseExited

    private void pnlDateTimeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlDateTimeMouseEntered
        pnlDateTime.setBackground(new Color(35, 35, 35));
    }//GEN-LAST:event_pnlDateTimeMouseEntered

    private void pnlDateTimeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlDateTimeMouseExited
        pnlDateTime.setBackground(new Color(0, 0, 0));
    }//GEN-LAST:event_pnlDateTimeMouseExited

    private void btn_employeesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_employeesMouseClicked
        card1.setVisible(true);
        card2.setVisible(false);
        card3.setVisible(false);

        setLblColor(btn_employees);
        resetLblColor(btn_employees1);
        resetLblColor(btn_employees2);
    }//GEN-LAST:event_btn_employeesMouseClicked

    private void pnlHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlHomeMouseClicked
        setPnlColor(pnlHome);
        resetPnlColor(pnlPOS);
        resetPnlColor(pnlSettings);

        setPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorPOS);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Home");

        cardHome.setVisible(true);
        cardPOS.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_pnlHomeMouseClicked

    private void pnlPOSMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlPOSMouseClicked
        resetPnlColor(pnlHome);
        setPnlColor(pnlPOS);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        setPnlIndicatorColor(pnlIndicatorPOS);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Point of Sales");

        cardHome.setVisible(false);
        cardPOS.setVisible(true);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_pnlPOSMouseClicked

    private void pnlSettingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlSettingsMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlPOS);
        setPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorPOS);
        setPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Settings");

        cardHome.setVisible(false);
        cardPOS.setVisible(false);
        cardSettings.setVisible(true);
    }//GEN-LAST:event_pnlSettingsMouseClicked

    private void minimize1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimize1MouseClicked
        EmployeeDashboard.this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_minimize1MouseClicked

    private void minimize1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimize1MouseEntered
        ImageIcon ii = new ImageIcon(getClass().getResource("img/titlebar_minimize_hover.png"));
        minimize1.setIcon(ii);
    }//GEN-LAST:event_minimize1MouseEntered

    private void minimize1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimize1MouseExited
        ImageIcon ii = new ImageIcon(getClass().getResource("img/transparent_titlebar_minimize.png"));
        minimize1.setIcon(ii);
    }//GEN-LAST:event_minimize1MouseExited

    private void maximize1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maximize1MouseClicked
        if (maximized) {
            EmployeeDashboard.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            maximizeView.setVisible(true);
            restoreDownView.setVisible(false);
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            EmployeeDashboard.this.setMaximizedBounds(env.getMaximumWindowBounds());
            maximized = false;

        } else {
            setExtendedState(JFrame.NORMAL);
            maximizeView.setVisible(false);
            restoreDownView.setVisible(true);
            maximized = true;
        }

    }//GEN-LAST:event_maximize1MouseClicked

    private void maximize1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maximize1MouseEntered
        ImageIcon ii = new ImageIcon(getClass().getResource("img/titlebar_maximize_state_1_hover.png"));
        maximize1.setIcon(ii);
    }//GEN-LAST:event_maximize1MouseEntered

    private void maximize1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maximize1MouseExited
        ImageIcon ii = new ImageIcon(getClass().getResource("img/transparent_titlebar_maximize_state_1.png"));
        maximize1.setIcon(ii);
    }//GEN-LAST:event_maximize1MouseExited

    private void close1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close1MouseClicked
        int choose = JOptionPane.showConfirmDialog(this,
                "Do you really want to exit the application?",
                "Confirm Close", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (choose == JOptionPane.YES_OPTION) {

            String logoutActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM employee WHERE username= '" + lblEmployeeUsername.getText() + "'),'Employee','" + lblEmployeeUsername.getText() + "','Employee System Exit', '" + lblEmployeeUsername.getText() + " system exited' , 'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            for (float i = 1f; i > 0; i -= 0.000001) {
                EmployeeDashboard.this.setOpacity(i);
            }
            System.exit(0);

        } else {
        }
    }//GEN-LAST:event_close1MouseClicked

    private void close1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close1MouseEntered
        ImageIcon ii = new ImageIcon(getClass().getResource("img/titlebar_close_hover.png"));
        close1.setIcon(ii);
    }//GEN-LAST:event_close1MouseEntered

    private void close1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close1MouseExited
        ImageIcon ii = new ImageIcon(getClass().getResource("img/transparent_titlebar_close.png"));
        close1.setIcon(ii);
    }//GEN-LAST:event_close1MouseExited

    private void minimize2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimize2MouseClicked
        EmployeeDashboard.this.setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_minimize2MouseClicked

    private void minimize2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimize2MouseEntered
        ImageIcon ii = new ImageIcon(getClass().getResource("img/titlebar_minimize_hover.png"));
        minimize2.setIcon(ii);
    }//GEN-LAST:event_minimize2MouseEntered

    private void minimize2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimize2MouseExited
        ImageIcon ii = new ImageIcon(getClass().getResource("img/transparent_titlebar_minimize.png"));
        minimize2.setIcon(ii);
    }//GEN-LAST:event_minimize2MouseExited

    private void maximize2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maximize2MouseClicked
        if (maximized) {
            EmployeeDashboard.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            maximizeView.setVisible(true);
            restoreDownView.setVisible(false);
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            EmployeeDashboard.this.setMaximizedBounds(env.getMaximumWindowBounds());
            maximized = false;

        } else {
            setExtendedState(JFrame.NORMAL);
            maximizeView.setVisible(false);
            restoreDownView.setVisible(true);
            maximized = true;
        }
    }//GEN-LAST:event_maximize2MouseClicked

    private void maximize2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maximize2MouseEntered
        ImageIcon ii = new ImageIcon(getClass().getResource("img/titlebar_maximize_state_2_hover.png"));
        maximize2.setIcon(ii);
    }//GEN-LAST:event_maximize2MouseEntered

    private void maximize2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_maximize2MouseExited
        ImageIcon ii = new ImageIcon(getClass().getResource("img/transparent_titlebar_maximize_state_2.png"));
        maximize2.setIcon(ii);
    }//GEN-LAST:event_maximize2MouseExited

    private void close2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close2MouseClicked
        int choose = JOptionPane.showConfirmDialog(this,
                "Do you really want to exit the application?",
                "Confirm Close", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (choose == JOptionPane.YES_OPTION) {

            String logoutActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM employee WHERE username= '" + lblEmployeeUsername.getText() + "'),'Employee','" + lblEmployeeUsername.getText() + "','Employee System Exit', '" + lblEmployeeUsername.getText() + " system exited' , 'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            for (float i = 1f; i > 0; i -= 0.000001) {
                EmployeeDashboard.this.setOpacity(i);
            }
            System.exit(0);

        } else {
        }
    }//GEN-LAST:event_close2MouseClicked

    private void close2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close2MouseEntered
        ImageIcon ii = new ImageIcon(getClass().getResource("img/titlebar_close_hover.png"));
        close2.setIcon(ii);
    }//GEN-LAST:event_close2MouseEntered

    private void close2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_close2MouseExited
        ImageIcon ii = new ImageIcon(getClass().getResource("img/transparent_titlebar_close.png"));
        close2.setIcon(ii);
    }//GEN-LAST:event_close2MouseExited

    private void lblEmployeeUsernameMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEmployeeUsernameMouseEntered
        lblEmployeeUsername.setBackground(new Color(35, 35, 35));
    }//GEN-LAST:event_lblEmployeeUsernameMouseEntered

    private void lblEmployeeUsernameMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblEmployeeUsernameMouseExited
        lblEmployeeUsername.setBackground(new Color(0, 0, 0));
    }//GEN-LAST:event_lblEmployeeUsernameMouseExited


    private void pnlTopMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTopMouseReleased
        setOpacity((float) (0.985));
    }//GEN-LAST:event_pnlTopMouseReleased

    private void btnAddEmployee1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddEmployee1ActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.showSaveDialog(this);
        String currentDate = new SimpleDateFormat("dd_MM_yyyy").format(new Date());
        String currentTime = new SimpleDateFormat("hh_mm_a").format(new Date());

        try {
            File f = fc.getSelectedFile();
            path = f.getAbsolutePath();
            path = path.replace('\\', '/');
            path = path + "_" + currentDate + "-" + currentTime + ".sql";
            txtBackupLocation.setText(path);
        } catch (Exception e) {
        }
        card4.setVisible(false);
        card4.setVisible(true);
    }//GEN-LAST:event_btnAddEmployee1ActionPerformed

    private void btnAddEmployee2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddEmployee2ActionPerformed
        int option = JOptionPane.showConfirmDialog(this, "Do you want to create a New Backup?", "Backup Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if ("".equals(txtBackupLocation.getText())) {
                JOptionPane.showMessageDialog(this, "Please Choose a Backup Location!", "Invalid Backup Location", JOptionPane.WARNING_MESSAGE);

                JFileChooser fc = new JFileChooser();
                fc.showSaveDialog(this);
                String currentDate = new SimpleDateFormat("dd_MM_yyyy").format(new Date());
                String currentTime = new SimpleDateFormat("hh_mm_a").format(new Date());

                try {
                    File f = fc.getSelectedFile();
                    path = f.getAbsolutePath();
                    path = path.replace('\\', '/');
                    path = path + "_" + currentDate + "-" + currentTime + ".sql";
                    txtBackupLocation.setText(path);
                } catch (Exception e) {
                }
                card4.setVisible(false);
                card4.setVisible(true);

            } else {

                Process p;
                try {
                    Runtime runtime = Runtime.getRuntime();
                    p = runtime.exec("C:/Program Files/MySQL/MySQL Server 5.7/bin/mysqldump.exe --host=localhost --user=root --password=1234 xeon_inventory -r " + path);

                    int processcomplete = p.waitFor();
                    if (processcomplete == 0) {
                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Backup Created Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                        txtBackupLocation.setText(null);

                        String createBackupActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM employee WHERE username = '" + lblEmployeeUsername.getText() + "'),'Employee','" + lblEmployeeUsername.getText() + "','Create Backup','Employee created new backup on file location : " + txtBackupLocation.getText() + "','SUCCESS')";
                        DB.DB.putData(createBackupActivityLog);

                    } else {
                        JOptionPane.showMessageDialog(this, "Backup Creation Failed!", "Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                }
            }
        }

        card4.setVisible(false);
        card4.setVisible(true);
    }//GEN-LAST:event_btnAddEmployee2ActionPerformed

    private void btnAddEmployee5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddEmployee5ActionPerformed
        JFileChooser fcrestore = new JFileChooser();
        fcrestore.showOpenDialog(this);
        try {
            File f = fcrestore.getSelectedFile();
            path = f.getAbsolutePath();
            path = path.replace('\\', '/');
            txtRestoreLocation.setText(path);
        } catch (Exception e) {
        }
        card5.setVisible(false);
        card5.setVisible(true);
    }//GEN-LAST:event_btnAddEmployee5ActionPerformed

    private void btnAddEmployee6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddEmployee6ActionPerformed
        String user = "root";
        String pwd = "1234";
        String[] restoreCmd = new String[]{"C:/Program Files/MySQL/MySQL Server 5.7/bin/mysql.exe", "--user=" + user, "--password=" + pwd, "-e", "source " + path};
        Process process;
        try {
            process = Runtime.getRuntime().exec(restoreCmd);
            int procCom = process.waitFor();
            if (procCom == 0) {
                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                JOptionPane.showMessageDialog(this, "Restore SuccessFul!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                String restoreBackupActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM employee WHERE username = '" + lblEmployeeUsername.getText() + "'),'Employee','" + lblEmployeeUsername.getText() + "','Restore Backup','Employee restored new backup from file location : " + txtRestoreLocation.getText() + "','SUCCESS')";
                DB.DB.putData(restoreBackupActivityLog);

            } else {
                JOptionPane.showMessageDialog(this, "Restore Failed!", "Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
        }
        card5.setVisible(false);
        card5.setVisible(true);
    }//GEN-LAST:event_btnAddEmployee6ActionPerformed

    private void jLabel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseClicked
        card4.setVisible(true);
        card5.setVisible(false);
        card6.setVisible(false);

        setLblColor(jLabel17);
        resetLblColor(jLabel34);
        resetLblColor(jLabel42);
    }//GEN-LAST:event_jLabel17MouseClicked

    private void jLabel34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseClicked
        card4.setVisible(false);
        card5.setVisible(true);
        card6.setVisible(false);

        resetLblColor(jLabel17);
        setLblColor(jLabel34);
        resetLblColor(jLabel42);
    }//GEN-LAST:event_jLabel34MouseClicked

    private void jLabel42MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel42MouseClicked
        card4.setVisible(false);
        card5.setVisible(false);
        card6.setVisible(true);

        resetLblColor(jLabel17);
        resetLblColor(jLabel34);
        setLblColor(jLabel42);
    }//GEN-LAST:event_jLabel42MouseClicked

    private void btn_employees1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_employees1MouseClicked
        card1.setVisible(false);
        card2.setVisible(true);
        card3.setVisible(false);

        resetLblColor(btn_employees);
        setLblColor(btn_employees1);
        resetLblColor(btn_employees2);
    }//GEN-LAST:event_btn_employees1MouseClicked

    private void btn_employees2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_employees2MouseClicked
        card1.setVisible(false);
        card2.setVisible(false);
        card3.setVisible(true);

        resetLblColor(btn_employees);
        resetLblColor(btn_employees1);
        setLblColor(btn_employees2);
    }//GEN-LAST:event_btn_employees2MouseClicked

    private void tblInvoiceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInvoiceMouseClicked
        txtInvoiceItemName.setText((String) tblInvoice.getValueAt(tblInvoice.getSelectedRow(), 1));
        txtInvoiceItemAmount.setText((String) tblInvoice.getValueAt(tblInvoice.getSelectedRow(), 5));
    }//GEN-LAST:event_tblInvoiceMouseClicked

    private void cmbInvoiceItemDiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbInvoiceItemDiscountKeyPressed
        try {

            if (evt.getKeyCode() == 10) {

                switch (cmbInvoiceItemDiscount.getSelectedIndex()) {
                    case 0: {
                        discount = 0;
                        break;
                    }
                    case 1: {
                        discount = 5;
                        break;
                    }
                    case 2: {
                        discount = 10;
                        break;
                    }
                    case 3: {
                        discount = 15;
                        break;
                    }
                    case 4: {
                        discount = 20;
                        break;
                    }
                    case 5: {
                        discount = 25;
                        break;
                    }
                    case 6: {
                        discount = 30;
                        break;
                    }
                    case 7: {
                        discount = 35;
                        break;
                    }
                    default:
                        break;
                }

                int sellingPrice = (Integer.parseInt(txtInvoiceItemSellingPrice.getText()));
                int totalWithDiscount = sellingPrice - (sellingPrice * discount / 100);

                String newItemPrice = Integer.toString(totalWithDiscount);
                txtInvoiceNewItemPrice.setText(newItemPrice);

                int purchasePrice = (Integer.parseInt(txtInvoiceItemPurchasePrice.getText()));  //item profit cheching
                int newSellingPrice = (Integer.parseInt(txtInvoiceNewItemPrice.getText()));

                if (purchasePrice > newSellingPrice) {
                    JOptionPane.showMessageDialog(this, "New Selling Price of this Item is Too Low, Please Lower the Discount!");

                }

                txtInvoiceItemAmount.grabFocus();
            }
        } catch (NumberFormatException e) {
        }
    }//GEN-LAST:event_cmbInvoiceItemDiscountKeyPressed

    private void txtInvoiceAmountPaidessed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceAmountPaidessed
        if (evt.getKeyCode() == 10) {
            if (!(tblInvoice.getRowCount() == 0)) {
                issueItems();
                jPanel13.setVisible(false);
                jPanel13.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(this, "No Items Found on Invoice Bill!");
                jPanel13.setVisible(false);
                jPanel13.setVisible(true);
            }
        }
    }//GEN-LAST:event_txtInvoiceAmountPaidessed

    private void txtSearchStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStockKeyReleased
        searchStock();
    }//GEN-LAST:event_txtSearchStockKeyReleased

    private void txtSearchStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStockKeyTyped
        calculateStockCount();
    }//GEN-LAST:event_txtSearchStockKeyTyped

    private void cmbSortStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortStockActionPerformed
        searchStock();
    }//GEN-LAST:event_cmbSortStockActionPerformed

    private void btnPrintAllEmployee4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintAllEmployee4ActionPerformed

        refreshStockTable();
        jPanel20.setVisible(false);
        jPanel20.setVisible(true);
    }//GEN-LAST:event_btnPrintAllEmployee4ActionPerformed

    private void txtSearchLowStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchLowStockKeyReleased
        searchLowStock();
    }//GEN-LAST:event_txtSearchLowStockKeyReleased

    private void txtSearchLowStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchLowStockKeyTyped
        calculateLowStockCount();
    }//GEN-LAST:event_txtSearchLowStockKeyTyped

    private void cmbSortLowStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortLowStockActionPerformed
        searchLowStock();
    }//GEN-LAST:event_cmbSortLowStockActionPerformed

    private void btnPrintAllEmployee2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintAllEmployee2ActionPerformed
        txtSearchLowStock.setText(null);
        refreshLowStockTable();
        jPanel8.setVisible(false);
        jPanel8.setVisible(true);
    }//GEN-LAST:event_btnPrintAllEmployee2ActionPerformed

    private void btnPrintAllEmployee1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintAllEmployee1ActionPerformed
        printBarcodes();
        jPanel20.setVisible(false);
        jPanel20.setVisible(true);
    }//GEN-LAST:event_btnPrintAllEmployee1ActionPerformed

    private void btnUpdateSelectedEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateSelectedEmployeeActionPerformed
        addInvoiceItem();
        jPanel11.setVisible(false);
        jPanel11.setVisible(true);
    }//GEN-LAST:event_btnUpdateSelectedEmployeeActionPerformed

    private void txtInvoiceItemCodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceItemCodeKeyReleased
        try {
            ResultSet InvoiceAutoFillDataFromStock = DB.DB.getData("SELECT item_name, item_price, selling_price, stock_count, low_stock FROM stock WHERE stock_status = 'Available' AND (stock_count >= 0) AND item_code = '" + txtInvoiceItemCode.getText() + "';");

            if (InvoiceAutoFillDataFromStock.next()) {
                String InvoiceItemName = InvoiceAutoFillDataFromStock.getString(1);
                String InvoiceItemPurchasePrice = InvoiceAutoFillDataFromStock.getString(2);
                String InvoiceItemSellingPrice = InvoiceAutoFillDataFromStock.getString(3);
                String InvoiceItemStockCount = InvoiceAutoFillDataFromStock.getString(4);
                String InvoiceItemLowStockCount = InvoiceAutoFillDataFromStock.getString(5);

                txtInvoiceItemName.setText(InvoiceItemName);
                txtInvoiceItemPurchasePrice.setText(InvoiceItemPurchasePrice);
                txtInvoiceItemSellingPrice.setText(InvoiceItemSellingPrice);
                txtInvoiceItemStockCount.setText(InvoiceItemStockCount);
                txtInvoiceItemLowStockCount.setText(InvoiceItemLowStockCount);

                ResultSet lowStockStatus = DB.DB.getData("SELECT id FROM stock WHERE stock_status = 'Available' AND (stock_count < low_stock) AND item_code = '" + txtInvoiceItemCode.getText() + "' ");

                if (lowStockStatus.next()) {
                    txtInvoiceItemStockStatus.setText("Low Stock");
                    txtInvoiceItemStockStatus.setForeground(new Color(255, 0, 51));
                    txtInvoiceItemStockStatus.setBorder(new LineBorder((new Color(255, 0, 51))));

                } else {

                    txtInvoiceItemStockStatus.setText("Available");
                    txtInvoiceItemStockStatus.setForeground(new Color(15, 184, 37));
                    txtInvoiceItemStockStatus.setBorder(new LineBorder((new Color(15, 184, 37))));

                }

            } else {
                txtInvoiceItemName.setText(null);
                txtInvoiceItemPurchasePrice.setText(null);
                txtInvoiceItemSellingPrice.setText(null);
                txtInvoiceItemStockCount.setText(null);
                txtInvoiceItemLowStockCount.setText(null);
                txtInvoiceItemStockStatus.setText(null);
                txtInvoiceItemStockStatus.setBorder(new LineBorder((new Color(100, 100, 100))));

            }

        } catch (Exception ex) {
        }
    }//GEN-LAST:event_txtInvoiceItemCodeKeyReleased

    private void txtInvoiceItemCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceItemCodeKeyPressed
        if (evt.getKeyCode() == 10) {
            cmbInvoiceItemDiscount.setPopupVisible(true);
            cmbInvoiceItemDiscount.grabFocus();
        }
    }//GEN-LAST:event_txtInvoiceItemCodeKeyPressed

    private void txtInvoiceItemAmountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceItemAmountKeyPressed
        if (evt.getKeyCode() == 10) {
            addInvoiceItem();
            jPanel11.setVisible(false);
            jPanel11.setVisible(true);
        }
    }//GEN-LAST:event_txtInvoiceItemAmountKeyPressed

    private void txtInvoiceItemAmountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceItemAmountKeyReleased
        try {

            int amountTyped = Integer.parseInt(txtInvoiceItemAmount.getText());
            int availableStockCount = Integer.parseInt(txtInvoiceItemStockCount.getText());

            if (amountTyped > availableStockCount) {
                JOptionPane.showMessageDialog(this, "No Sufficient Stock Count Available!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                txtInvoiceItemAmount.setText(null);
            }

        } catch (HeadlessException | NumberFormatException e) {
        }

    }//GEN-LAST:event_txtInvoiceItemAmountKeyReleased

    private void txtInvoiceItemAmountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceItemAmountKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();
        }
    }//GEN-LAST:event_txtInvoiceItemAmountKeyTyped

    private void btnDeleteSelectedEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSelectedEmployeeActionPerformed
        jTabbedPaneInvoice.setSelectedIndex(1);
        refreshInvoiceTable();
        txtSearchInvoice.setText(txtInvoiceId.getText());
        cmbSortInvoice.setSelectedIndex(0);
        lblInvoiceId.setText("Invoice Id : " + txtSearchInvoice.getText());

    }//GEN-LAST:event_btnDeleteSelectedEmployeeActionPerformed

    private void btnDeleteEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteEmployeeActionPerformed
        jTabbedPaneInvoice.setSelectedIndex(0);
    }//GEN-LAST:event_btnDeleteEmployeeActionPerformed

    private void btnPrintSelectedEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintSelectedEmployeeActionPerformed
        txtInvoiceItemCode.setText(null);
        txtInvoiceItemName.setText(null);
        txtInvoiceItemPurchasePrice.setText(null);
        txtInvoiceItemSellingPrice.setText(null);
        cmbInvoiceItemDiscount.setSelectedIndex(0);
        txtInvoiceNewItemPrice.setText(null);
        txtInvoiceItemAmount.setText(null);

        txtInvoiceItemStockCount.setText(null);
        txtInvoiceItemLowStockCount.setText(null);

        txtInvoiceItemStockStatus.setText(null);
        txtInvoiceItemStockStatus.setBorder(new LineBorder((new Color(100, 100, 100))));

        jPanel11.setVisible(false);
        jPanel11.setVisible(true);
    }//GEN-LAST:event_btnPrintSelectedEmployeeActionPerformed

    private void txtSearchInvoiceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchInvoiceKeyReleased
        searchInvoiceItem();
    }//GEN-LAST:event_txtSearchInvoiceKeyReleased

    private void cmbSortInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortInvoiceActionPerformed
        searchInvoiceItem();
    }//GEN-LAST:event_cmbSortInvoiceActionPerformed

    private void btnPrintAllEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintAllEmployeeActionPerformed
        jTabbedPaneInvoice.setSelectedIndex(2);
        txtInvoiceAmountPaid.grabFocus();
    }//GEN-LAST:event_btnPrintAllEmployeeActionPerformed

    private void btnRefreshEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshEmployeeActionPerformed
        refreshInvoiceTable();
        txtSearchInvoice.setText(txtInvoiceId.getText());
        cmbSortInvoice.setSelectedIndex(0);
        lblInvoiceId.setText("Invoice Id : " + txtSearchInvoice.getText());
        jPanel10.setVisible(false);
        jPanel10.setVisible(true);
    }//GEN-LAST:event_btnRefreshEmployeeActionPerformed

    private void txtInvoiceAmountPaidKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceAmountPaidKeyReleased
        try {

            double amountDue = Double.parseDouble(txtInvoiceAmountDue.getText());
            double paidAmount = Double.parseDouble(txtInvoiceAmountPaid.getText());
            double balance = paidAmount - amountDue;
            txtInvoiceAmountBalance.setText(Double.toString(balance));

        } catch (NumberFormatException e) {
        }
    }//GEN-LAST:event_txtInvoiceAmountPaidKeyReleased

    private void txtInvoiceAmountPaidKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInvoiceAmountPaidKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();
        }
    }//GEN-LAST:event_txtInvoiceAmountPaidKeyTyped

    private void btnUpdateSelectedEmployee1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateSelectedEmployee1ActionPerformed
        if (!(tblInvoice.getRowCount() == 0)) {
            issueItems();
            jPanel13.setVisible(false);
            jPanel13.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "No Items Found on Invoice Bill!");
            jTabbedPaneInvoice.setSelectedIndex(1);
        }
    }//GEN-LAST:event_btnUpdateSelectedEmployee1ActionPerformed

    private void btnEditEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditEmployeeActionPerformed
        if (!(tblInvoice.getSelectedRowCount() == 0)) {
            deleteInvoiceItem();
            jPanel10.setVisible(false);
            jPanel10.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "No Item Selected!");
            jPanel10.setVisible(false);
            jPanel10.setVisible(true);
        }
    }//GEN-LAST:event_btnEditEmployeeActionPerformed

    private void btnDeleteSelectedEmployee1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSelectedEmployee1ActionPerformed
        printSelectedInvoiceBill();
        jPanel13.setVisible(false);
        jPanel13.setVisible(true);
    }//GEN-LAST:event_btnDeleteSelectedEmployee1ActionPerformed

    private void txtSearchTodaySalesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchTodaySalesKeyReleased
        searchTodaySalesTable();
    }//GEN-LAST:event_txtSearchTodaySalesKeyReleased

    private void cmbSortTodaySalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortTodaySalesActionPerformed
        searchTodaySalesTable();
    }//GEN-LAST:event_cmbSortTodaySalesActionPerformed

    private void btnRefreshMonthlyEmployeeAttendance23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshMonthlyEmployeeAttendance23ActionPerformed
        refreshSalesTable();
        jPanel28.setVisible(false);
        jPanel28.setVisible(true);
    }//GEN-LAST:event_btnRefreshMonthlyEmployeeAttendance23ActionPerformed

    private void txtSearchSalesHistoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSalesHistoryKeyReleased
        searchSalesHistoryTable();
    }//GEN-LAST:event_txtSearchSalesHistoryKeyReleased

    private void cmbSortSalesHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortSalesHistoryActionPerformed
        searchSalesHistoryTable();
    }//GEN-LAST:event_cmbSortSalesHistoryActionPerformed

    private void btnRefreshMonthlyEmployeeAttendance24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshMonthlyEmployeeAttendance24ActionPerformed
        refreshSalesHistoryTable();
        jPanel19.setVisible(false);
        jPanel19.setVisible(true);
    }//GEN-LAST:event_btnRefreshMonthlyEmployeeAttendance24ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println(ex + "Failed to initialize LaF");
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmployeeDashboard().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/xeon_icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddEmployee1;
    private javax.swing.JButton btnAddEmployee2;
    private javax.swing.JButton btnAddEmployee5;
    private javax.swing.JButton btnAddEmployee6;
    private javax.swing.JButton btnDeleteEmployee;
    private javax.swing.JButton btnDeleteSelectedEmployee;
    private javax.swing.JButton btnDeleteSelectedEmployee1;
    private javax.swing.JButton btnEditEmployee;
    private javax.swing.JButton btnPrintAllEmployee;
    private javax.swing.JButton btnPrintAllEmployee1;
    private javax.swing.JButton btnPrintAllEmployee2;
    private javax.swing.JButton btnPrintAllEmployee4;
    private javax.swing.JButton btnPrintSelectedEmployee;
    private javax.swing.JButton btnRefreshEmployee;
    private javax.swing.JButton btnRefreshMonthlyEmployeeAttendance23;
    private javax.swing.JButton btnRefreshMonthlyEmployeeAttendance24;
    private javax.swing.JButton btnUpdateSelectedEmployee;
    private javax.swing.JButton btnUpdateSelectedEmployee1;
    private javax.swing.JLabel btn_employees;
    private javax.swing.JLabel btn_employees1;
    private javax.swing.JLabel btn_employees2;
    private javax.swing.JPanel card1;
    private javax.swing.JPanel card2;
    private javax.swing.JPanel card3;
    private javax.swing.JPanel card4;
    private javax.swing.JPanel card5;
    private javax.swing.JPanel card6;
    private javax.swing.JPanel cardHome;
    private javax.swing.JLabel cardName;
    private javax.swing.JPanel cardPOS;
    private javax.swing.JPanel cardSettings;
    private javax.swing.JLabel close1;
    private javax.swing.JLabel close2;
    private javax.swing.JComboBox<String> cmbInvoiceItemDiscount;
    private javax.swing.JComboBox<String> cmbSortInvoice;
    private javax.swing.JComboBox<String> cmbSortLowStock;
    private javax.swing.JComboBox<String> cmbSortSalesHistory;
    private javax.swing.JComboBox<String> cmbSortStock;
    private javax.swing.JComboBox<String> cmbSortTodaySales;
    private javax.swing.JLabel employeeName;
    private javax.swing.JLabel home;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel67;
    public static final javax.swing.JLabel jLabel68 = new javax.swing.JLabel();
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel71;
    public static final javax.swing.JLabel jLabel72 = new javax.swing.JLabel();
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPaneInvoice;
    private javax.swing.JTabbedPane jTabbedPaneStock;
    private javax.swing.JTabbedPane jTabbedPaneStock1;
    private javax.swing.JLabel lblDate;
    public static final javax.swing.JLabel lblEmployeeUsername = new javax.swing.JLabel();
    public static final javax.swing.JLabel lblHomeEmployeeAttendanceThisMonth = new javax.swing.JLabel();
    private javax.swing.JLabel lblHomeEmployeeCount;
    private javax.swing.JLabel lblHomeLowStockCount;
    private javax.swing.JLabel lblHomeStockCount;
    private javax.swing.JLabel lblInvoiceId;
    private javax.swing.JLabel lblInvoiceTotal;
    private javax.swing.JLabel lblLogout;
    private javax.swing.JLabel lblLowStockCount;
    private javax.swing.JLabel lblSelectedEmployeePositionValidation1;
    private javax.swing.JLabel lblSelectedEmployeePositionValidation2;
    private javax.swing.JLabel lblStockCount;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTodaySalesCount;
    private javax.swing.JLabel loginWish;
    private javax.swing.JPanel mainCard;
    private javax.swing.JLabel maximize1;
    private javax.swing.JLabel maximize2;
    private javax.swing.JPanel maximizeView;
    private javax.swing.JLabel minimize1;
    private javax.swing.JLabel minimize2;
    private javax.swing.JPanel pnlBranding;
    private javax.swing.JPanel pnlDateTime;
    private javax.swing.JPanel pnlEmployeeDashboard;
    private javax.swing.JPanel pnlFrames;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlHomeSubSelection;
    private javax.swing.JPanel pnlIndicatorHome;
    private javax.swing.JPanel pnlIndicatorPOS;
    private javax.swing.JPanel pnlIndicatorSettings;
    private javax.swing.JPanel pnlLogout;
    private javax.swing.JPanel pnlLogoutLoader;
    private javax.swing.JPanel pnlLogoutLoaderBody;
    private javax.swing.JPanel pnlPOS;
    private javax.swing.JPanel pnlPOSSubSelection;
    private javax.swing.JPanel pnlParent;
    private javax.swing.JPanel pnlSettings;
    private javax.swing.JPanel pnlSettingsSubSelection;
    private javax.swing.JPanel pnlSidebar;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JPanel pnlTopIcons;
    private javax.swing.JLabel reports;
    private javax.swing.JPanel restoreDownView;
    private javax.swing.JLabel sales;
    private javax.swing.JPanel subCardPOS;
    private javax.swing.JPanel subCardSettings;
    private javax.swing.JTable tblInvoice;
    private javax.swing.JTable tblLowStock;
    private javax.swing.JTable tblSalesHistory;
    private javax.swing.JTable tblStock;
    private javax.swing.JTable tblTodaySales;
    private javax.swing.JLabel title;
    private javax.swing.JTextField txtBackupLocation;
    private javax.swing.JTextField txtInvoiceAmountBalance;
    private javax.swing.JTextField txtInvoiceAmountDue;
    private javax.swing.JTextField txtInvoiceAmountPaid;
    private javax.swing.JTextField txtInvoiceAmountSavings;
    private javax.swing.JTextField txtInvoiceId;
    private javax.swing.JTextField txtInvoiceItemAmount;
    private javax.swing.JTextField txtInvoiceItemCode;
    private javax.swing.JTextField txtInvoiceItemLowStockCount;
    private javax.swing.JTextField txtInvoiceItemName;
    private javax.swing.JTextField txtInvoiceItemPurchasePrice;
    private javax.swing.JTextField txtInvoiceItemSellingPrice;
    private javax.swing.JTextField txtInvoiceItemStockCount;
    private javax.swing.JTextField txtInvoiceItemStockStatus;
    private javax.swing.JTextField txtInvoiceNewItemPrice;
    private javax.swing.JTextField txtRestoreLocation;
    private javax.swing.JTextField txtSearchInvoice;
    private javax.swing.JTextField txtSearchLowStock;
    private javax.swing.JTextField txtSearchSalesHistory;
    private javax.swing.JTextField txtSearchStock;
    private javax.swing.JTextField txtSearchTodaySales;
    private javax.swing.JLabel version1;
    private javax.swing.JLabel watermark;
    // End of variables declaration//GEN-END:variables
}
