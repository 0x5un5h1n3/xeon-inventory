/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.ui.FlatBorder;
//import com.sun.glass.events.KeyEvent;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class EmergencyDashboard extends javax.swing.JFrame {

    /**
     * Creates new form Dashboard
     */
    int mousepX;
    int mousepY;

    CardLayout cardLayout;
    Timer timer;
    String month;
    String year;
    String date;
    String today;
    String leadTime;
    String itemId;
    String selectedPOItemId;
    String selectedPOItemId1;
    String path = null;

    EmergencyDashboard() {

        initComponents();

        EmergencyDashboard.this.getRootPane().setBorder(new LineBorder(((new Color(30, 30, 30)))));
        EmergencyDashboard.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIcon();
        refreshAdministratorTable();
        refreshSystemLogsTable();
        setTableheader();
        refreshEmployeePasswordTable();
        refreshAdministratorPasswordTable();
        refreshSuperUserPasswordTable();
        refreshStockTable();
        refreshStockReturnTable();
        EmergencyDashboard.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        maximizeView.setVisible(true);
        restoreDownView.setVisible(false);
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        EmergencyDashboard.this.setMaximizedBounds(env.getMaximumWindowBounds());
        maximized = false;

        EmergencyDashboard.this.addWindowListener(new WindowAdapter() {
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
                jLabel69.setText("Today is " + dayInWords + ", " + month + " " + day + ", " + year);

                DateFormat wishBasedOnTime = new SimpleDateFormat("HH");
                Date date4 = new Date();
                String wishTime = wishBasedOnTime.format(date4);
                int now = Integer.parseInt(wishTime);

                if (now >= 6 && now < 12) {
                    loginWish.setText("Good morning");
                    adminName.setText(lblSuperUserUsername.getText());
                } else if (now >= 12 && now < 17) {
                    loginWish.setText("Good afternoon");
                    adminName.setText(lblSuperUserUsername.getText());
                } else if (now >= 17 && now < 20) {
                    loginWish.setText("Good evening");
                    adminName.setText(lblSuperUserUsername.getText());
                } else {
                    loginWish.setText("Good night");
                    adminName.setText(lblSuperUserUsername.getText());
                }

            }
        };
        timer = new Timer(1000, actionListener);

        timer.setInitialDelay(
                0);
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
        JTableHeader emptbl = tblAdmin.getTableHeader();
        emptbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emptbl.setForeground(Color.white);
        emptbl.setBackground(new Color(18, 18, 18));
        tblAdmin.getTableHeader().setReorderingAllowed(false);

        JTableHeader syslog = tblSystemLogs.getTableHeader();
        syslog.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        syslog.setForeground(Color.white);
        syslog.setBackground(new Color(18, 18, 18));
        tblSystemLogs.getTableHeader().setReorderingAllowed(false);

        JTableHeader emppwd = tblEmployeePassword.getTableHeader();
        emppwd.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emppwd.setForeground(Color.white);
        emppwd.setBackground(new Color(18, 18, 18));
        tblEmployeePassword.getTableHeader().setReorderingAllowed(false);

        JTableHeader admpwd = tblAdministratorPassword.getTableHeader();
        admpwd.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        admpwd.setForeground(Color.white);
        admpwd.setBackground(new Color(18, 18, 18));
        tblAdministratorPassword.getTableHeader().setReorderingAllowed(false);

        JTableHeader suppwd = tblSuperUserPassword.getTableHeader();
        suppwd.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        suppwd.setForeground(Color.white);
        suppwd.setBackground(new Color(18, 18, 18));
        tblSuperUserPassword.getTableHeader().setReorderingAllowed(false);

        JTableHeader stk = tblStock.getTableHeader();
        stk.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        stk.setForeground(Color.white);
        stk.setBackground(new Color(18, 18, 18));
        tblStock.getTableHeader().setReorderingAllowed(false);

        JTableHeader stkrtn = tblStockReturn.getTableHeader();
        stkrtn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        stkrtn.setForeground(Color.white);
        stkrtn.setBackground(new Color(18, 18, 18));
        tblStockReturn.getTableHeader().setReorderingAllowed(false);

    }

    void showDate() {
        Date d = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(s.format(d));
    }

    private void addNewAdministrator() {

        int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Administrator?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            if (!txtAdminUsername.getText().equals("")
                    & !txtAdminNIC.getText().equals("")
                    & (!String.valueOf(pwdAdminPassword.getPassword()).equals("")
                    & (!String.valueOf(pwdRepeatAdminPassword.getPassword()).equals("")))) {

                try {
                    ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE username like '" + txtAdminUsername.getText() + "%' order by id ");

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Username Already Exists!", "Invalid Username", JOptionPane.WARNING_MESSAGE);

                    } else if (!(String.valueOf(pwdAdminPassword.getPassword()).length() >= 4)) {
                        JOptionPane.showMessageDialog(this, "Password is Too Short!", "Invalid Password", JOptionPane.WARNING_MESSAGE); //Short password
                        pwdAdminPassword.setText(null);
                        pwdRepeatAdminPassword.setText(null);
                        pwdAdminPassword.grabFocus();

                    } else if (!String.valueOf(pwdAdminPassword.getPassword()).equals(String.valueOf(pwdRepeatAdminPassword.getPassword()))) {
                        JOptionPane.showMessageDialog(this, "Password doesn't match!", "Invalid Password Matching", JOptionPane.WARNING_MESSAGE); //invalid password matching
                        pwdAdminPassword.setText(null);
                        pwdRepeatAdminPassword.setText(null);
                        pwdAdminPassword.grabFocus();

                    } else if (!(txtAdminNIC.getText().length() >= 9)) {
                        JOptionPane.showMessageDialog(this, "Invalid NIC!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                        txtAdminNIC.setText(null);
                        txtAdminNIC.grabFocus();

                    } else if (txtAdminPhoneNo.getText().length() != 10) {
                        JOptionPane.showMessageDialog(this, "Invalid Phone Number Length!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                        txtAdminPhoneNo.setText(null);
                        txtAdminPhoneNo.grabFocus();

                    } else {
                        Date DateMonth = new Date();
                        SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
                        month = "" + toMonth.format(DateMonth);

                        Date DateYear = new Date();
                        SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
                        year = "" + toYear.format(DateYear);

                        Date DateDate = new Date();
                        SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                        date = "" + toDate.format(DateDate);

                        String addAdministrator = "INSERT INTO account(username, type, nic, phone_no, password, account_status) VALUES ('" + txtAdminUsername.getText().trim() + "','admin','" + txtAdminNIC.getText() + "','" + txtAdminPhoneNo.getText() + "','" + String.valueOf(pwdAdminPassword.getPassword()) + "', 'Active')";

                        String AddAdministratorActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblSuperUserUsername.getText() + "'),'Super User','" + lblSuperUserUsername.getText() + "','Add New Admin','Super User added new administrator " + txtAdminUsername.getText() + "','SUCCESS')";
                        try {
                            DB.DB.putData(addAdministrator);

                            DB.DB.putData(AddAdministratorActivityLog);

                            refreshAdministratorTable();
                            refreshAdministratorPasswordTable();

                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Administrator Added!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            txtAdminUsername.setText(null);
                            txtAdminNIC.setText(null);
                            txtAdminPhoneNo.setText(null);
                            pwdAdminPassword.setText(null);
                            pwdRepeatAdminPassword.setText(null);

                            lblEmployeeUserameValidation.setText(null);
                            lblEmployeeNICValidation.setText(null);
                            lblEmployeePhoneNoValidation.setText(null);
                            lblEmployeePasswordValidation.setText(null);
                            lblEmployeeRepeatPasswordValidation.setText(null);

                            txtAdminUsername.setBorder(new FlatBorder());
                            txtAdminPhoneNo.setBorder(new FlatBorder());
                            pwdAdminPassword.setBorder(new FlatBorder());
                            pwdRepeatAdminPassword.setBorder(new FlatBorder());

                        } catch (Exception e) {
                        }

                    }
                } catch (Exception ex) {
                    Logger.getLogger(EmergencyDashboard.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void deleteAdministrator() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this Administrator?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            try {
                DB.DB.putData("UPDATE account SET account_status = 'Dective' WHERE id ='" + txtSelectedAdminId.getText() + "' ");
                String deleteAdminActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblSuperUserUsername.getText() + "'),'Super User','" + lblSuperUserUsername.getText() + "','Delete Admin','Super User deleted admin " + txtSelectedAdminUsername.getText() + "','SUCCESS')";
                DB.DB.putData(deleteAdminActivityLog);

                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                JOptionPane.showMessageDialog(this, "Administrator Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                txtSearchAdmin.setText(null);
                txtSelectedAdminId.setText(null);
                txtSelectedAdminUsername.setText(null);
                txtSelectedAdminNIC.setText(null);
                txtSelectedAdminPhoneNo.setText(null);
                refreshAdministratorTable();

            } catch (Exception e) {
            }
        } else {

        }
    }

    private void updateAdministrator() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Update this Administrator?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSelectedAdminUsername.getText().equals("")
                    & !txtSelectedAdminNIC.getText().equals("")
                    & !txtSelectedAdminPhoneNo.getText().equals("")) {

                if (!(txtSelectedAdminPhoneNo.getText().length() == 10)) {
                    JOptionPane.showMessageDialog(this, "Invalid Phone Number!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    txtSelectedAdminPhoneNo.setText(null);
                    txtSelectedAdminPhoneNo.grabFocus();

                } else if (!(txtSelectedAdminNIC.getText().length() >= 9)) {
                    JOptionPane.showMessageDialog(this, "Invalid NIC!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    txtSelectedAdminNIC.setText(null);
                    txtSelectedAdminNIC.grabFocus();

                } else {

                    try {

                        DB.DB.putData("UPDATE account SET  nic ='" + txtSelectedAdminNIC.getText() + "', phone_no= '" + txtSelectedAdminPhoneNo.getText() + "' WHERE id ='" + txtSelectedAdminId.getText() + "' ");

                        String updateAdministratorActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblSuperUserUsername.getText() + "'),'Super User','" + lblSuperUserUsername.getText() + "','Update Admin','Super user updated administrator " + txtSelectedAdminUsername.getText() + "','SUCCESS')";
                        DB.DB.putData(updateAdministratorActivityLog);

                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Administrator Updated Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                        txtSelectedAdminId.setText("");
                        txtSelectedAdminUsername.setText("");
                        txtSelectedAdminNIC.setText("");
                        txtSelectedAdminPhoneNo.setText("");
                        refreshAdministratorTable();

                    } catch (Exception e) {
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void calculateAdminCount() {

        int count = tblAdmin.getRowCount();
        lblAdminCount.setText("Administrator Count : " + Integer.toString(count));

    }

    private void refreshAdministratorTable() {

        try {
            DefaultTableModel dtm = (DefaultTableModel) tblAdmin.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE type = 'admin' AND account_status = 'Active' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                dtm.addRow(new Object[]{a, b, c, d, e});
            }
            calculateAdminCount();
        } catch (Exception e) {
        }

    }

    private void searchAdministrator() {
        switch (cmbSortAdmin.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblAdmin.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE type = 'admin' AND account_status = 'Active' AND id like '" + txtSearchAdmin.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
                calculateAdminCount();

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblAdmin.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE type = 'admin' AND account_status = 'Active' AND  username like '" + txtSearchAdmin.getText() + "%' order by username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
                calculateAdminCount();

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblAdmin.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE type = 'admin' AND account_status = 'Active' AND  nic like '" + txtSearchAdmin.getText() + "%' order by nic ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
                calculateAdminCount();
            } catch (Exception e) {
            }
            break;
            case 3:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblAdmin.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE type = 'admin' AND account_status = 'Active' AND  phone_no like '" + txtSearchAdmin.getText() + "%' order by phone_no ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
                calculateAdminCount();
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    public void printAllAdmins() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewAdministratorReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    public void printSelectedAdministrator() {
        HashMap a = new HashMap();
        a.put("id", txtSelectedAdminId.getText());

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewSelectedAdministratorReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    private void calculateSystemLogCount() {
        int count = tblSystemLogs.getRowCount();
        lblLogCount.setText("Log(s) Count : " + Integer.toString(count));

    }

    private void refreshSystemLogsTable() {

        try {
            DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM log order by `date/time` desc");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(6));
                String g = (rs.getString(7));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g});
            }

            calculateSystemLogCount();
            txtSearchSystemLogs.setText(null);

        } catch (Exception e) {
        }

    }

    private void searchSystemLogs() {
        switch (cmbSortSystemLogs.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' || user like '" + txtSearchSystemLogs.getText() + "%' || activity like '" + txtSearchSystemLogs.getText() + "%' || description like '" + txtSearchSystemLogs.getText() + "%' || state like '" + txtSearchSystemLogs.getText() + "%' || `date/time` like '" + txtSearchSystemLogs.getText() + "%' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Intruder Login' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Intruder Login' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Intruder Login' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Intruder Login' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Intruder Login' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Intruder Login'  order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 2:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee Login' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee Login' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee Login' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee Login' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Employee Login' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Employee Login' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 3:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Admin Login' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Admin Login' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Admin Login' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Admin Login' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Admin Login' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Admin Login' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 4:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Super User Login' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Super User Login' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Super User Login' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Super User Login' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Super User Login' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Super User Login' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 5:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee Logout' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee Logout' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee Logout' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee Logout' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Employee Logout' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Employee Logout' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 6:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Admin Logout' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Admin Logout' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Admin Logout' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Admin Logout' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Admin Logout' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Admin Logout' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 7:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Super User Logout' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Super User Logout' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Super User Logout' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Super User Logout' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Super User Logout' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Super User Logout' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 8:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Supplier' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Supplier' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Supplier' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Supplier' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Add New Supplier' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Add New Supplier' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 9:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Supplier' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Supplier' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Supplier' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Supplier' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Update Supplier' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Update Supplier' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 10:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Supplier' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Supplier' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Supplier' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Supplier' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Delete Supplier' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Delete Supplier' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 11:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add Item to Supplier Item Collection' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add Item to Supplier Item Collection' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add Item to Supplier Item Collection' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add Item to Supplier Item Collection' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Add Item to Supplier Item Collection' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Add Item to Supplier Item Collection' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 12:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Supplier Item' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Supplier Item' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Supplier Item' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Supplier Item' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Update Supplier Item' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Update Supplier Item' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 13:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Supplier Item' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Supplier Item' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Supplier Item' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Supplier Item' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Delete Supplier Item' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Delete Supplier Item' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 14:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Employee' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Employee' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Employee' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Employee' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Add New Employee' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Add New Employee' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 15:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Employee' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Employee' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Employee' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Employee' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Update Employee' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Update Employee' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 16:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Employee' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Employee' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Employee' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Employee' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Delete Employee' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Delete Employee' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 17:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee System Exit' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee System Exit' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee System Exit' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee System Exit' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Employee System Exit' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Employee System Exit' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 18:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Admin System Exit' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Admin System Exit' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Admin System Exit' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Admin System Exit' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Admin System Exit' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Admin System Exit' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 19:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Super User System Exit' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Super User System Exit' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Super User System Exit' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Super User System Exit' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Super User System Exit' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Super User System Exit' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 20:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee Payroll' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee Payroll' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee Payroll' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Employee Payroll' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Employee Payroll' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Employee Payroll' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 21:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Purchase Order' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Purchase Order' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Purchase Order' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Purchase Order' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Purchase Order' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Purchase Order' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 22:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Bad Order' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Bad Order' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Bad Order' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Bad Order' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Bad Order' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Bad Order' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 23:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Stock Item' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Stock Item' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Stock Item' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Stock Item' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Add New Stock Item' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Add New Stock Item' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 24:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'GRN Checked' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'GRN Checked' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'GRN Checked' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'GRN Checked' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'GRN Checked' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'GRN Checked' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 25:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Re-Order Stock Item' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Re-Order Stock Item' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Re-Order Stock Item' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Re-Order Stock Item' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Re-Order Stock Item' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Re-Order Stock Item' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 26:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Sale Item' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Sale Item' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Sale Item' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Sale Item' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Add New Sale Item' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Add New Sale Item' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 27:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Remove New Sale Item' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Remove New Sale Item' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Remove New Sale Item' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Remove New Sale Item' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Remove New Sale Item' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Remove New Sale Item' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 28:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Issue Invoice / Items' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Issue Invoice / Items' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Issue Invoice / Items' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Issue Invoice / Items' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Issue Invoice / Items' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Issue Invoice / Items' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 29:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Force Access Employee Login' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Force Access Employee Login' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Force Access Employee Login' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Force Access Employee Login' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Force Access Employee Login' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Force Access Employee Login' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 30:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Force Access Admin Login' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Force Access Admin Login' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Force Access Admin Login' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Force Access Admin Login' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Force Access Admin Login' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Force Access Admin Login' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 31:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'System Backup' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'System Backup' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'System Backup' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'System Backup' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'System Backup' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'System Backup' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 32:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'System Restore' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'System Restore' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'System Restore' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'System Restore' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'System Restore' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'System Restore' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 33:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Admin' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Admin' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Admin' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Add New Admin' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Add New Admin' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Add New Admin' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 34:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Admin' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Admin' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Admin' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Update Admin' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Update Admin' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Update Admin' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 35:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Admin' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Admin' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Admin' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Admin' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Delete Admin' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Delete Admin' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 36:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Stock Item' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Stock Item' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Stock Item' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Delete Stock Item' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Delete Stock Item' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Delete Stock Item' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 37:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Return Stock Item' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Return Stock Item' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Return Stock Item' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Return Stock Item' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Return Stock Item' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Return Stock Item' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            case 38:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSystemLogs.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM log WHERE id like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Change Password' || user like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Change Password' || activity like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Change Password' || description like '" + txtSearchSystemLogs.getText() + "%' AND activity = 'Change Password' || state like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Change Password' || `date/time` like '" + txtSearchSystemLogs.getText() + "%'  AND activity = 'Change Password' order by `date/time` desc");

                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

                calculateSystemLogCount();

            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    public void printLogs() {
        new Thread() {
            @Override
            public void run() {

                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewLogReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    private void calculateEmployeeCount() {
        int count = tblEmployeePassword.getRowCount();
        lblEmployeeCount.setText("Employee Count : " + Integer.toString(count));

    }

    private void refreshEmployeePasswordTable() {

        try {
            DefaultTableModel dtm = (DefaultTableModel) tblEmployeePassword.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM employee");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(7));
                String d = (rs.getString(8));
                dtm.addRow(new Object[]{a, b, c, d});
            }
            calculateEmployeeCount();
        } catch (Exception e) {
        }

    }

    private void searchEmployeePassword() {
        switch (cmbSortEmployeePassword.getSelectedIndex()) {
            case 0:

                try {

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePassword.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE id like '" + txtSearchEmployeePassword.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(7));
                    String d = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d});
                }
                calculateEmployeeCount();
            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePassword.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE username like '" + txtSearchEmployeePassword.getText() + "%' order by username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(7));
                    String d = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d});
                }
                calculateEmployeeCount();
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    private void changeEmployeePassword() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Change Password of this Employee?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSelectedEmployeeId.getText().equals("")
                    & !txtSelectedEmployeeUsername.getText().equals("")
                    & !pwdSelectedEmployeeNewPassword.getPassword().equals("")
                    & !pwdSelectedRepeatEmployeePassword.getPassword().equals("")) {

                if (!(String.valueOf(pwdSelectedEmployeeNewPassword.getPassword()).length() >= 4)) {
                    JOptionPane.showMessageDialog(this, "Password is Too Short!", "Invalid Password", JOptionPane.WARNING_MESSAGE); //Short password
                    pwdSelectedEmployeeNewPassword.setText(null);
                    pwdSelectedRepeatEmployeePassword.setText(null);
                    pwdSelectedEmployeeNewPassword.grabFocus();

                } else if (!String.valueOf(pwdSelectedEmployeeNewPassword.getPassword()).equals(String.valueOf(pwdSelectedRepeatEmployeePassword.getPassword()))) {
                    JOptionPane.showMessageDialog(this, "Password doesn't match!", "Invalid Password Matching", JOptionPane.WARNING_MESSAGE);
                    pwdSelectedEmployeeNewPassword.setText(null);
                    pwdSelectedRepeatEmployeePassword.setText(null);
                    pwdSelectedEmployeeNewPassword.grabFocus();

                } else {

                    try {
                        DB.DB.putData("UPDATE employee SET password ='" + String.valueOf(pwdSelectedRepeatEmployeePassword.getPassword()) + "' WHERE id ='" + txtSelectedEmployeeId.getText() + "' ");
                        String changeEmployeePassowrdActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblSuperUserUsername.getText() + "'),'Super User','" + lblSuperUserUsername.getText() + "','Change Password','Super user changed password of employee " + txtSelectedEmployeeUsername.getText() + "','SUCCESS')";
                        DB.DB.putData(changeEmployeePassowrdActivityLog);

                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Employee Password Changed Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                        txtSelectedEmployeeId.setText("");
                        txtSelectedEmployeeUsername.setText("");
                        txtSelectedEmployeeCurrentPassword.setText("");
                        pwdSelectedEmployeeNewPassword.setText("");
                        pwdSelectedRepeatEmployeePassword.setText("");
                        refreshEmployeePasswordTable();

                    } catch (Exception e) {
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void calculateAdministratorCount() {
        int count = tblAdministratorPassword.getRowCount();
        lblAdministratorCount.setText("Administrator Count : " + Integer.toString(count));

    }

    private void refreshAdministratorPasswordTable() {

        try {
            DefaultTableModel dtm = (DefaultTableModel) tblAdministratorPassword.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE type = 'admin' AND account_status = 'Active' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(6));
                dtm.addRow(new Object[]{a, b, c});
            }
            calculateAdministratorCount();
        } catch (Exception e) {
        }

    }

    private void searchAdministratorPassword() {
        switch (cmbSortAdministratorPassword.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblAdministratorPassword.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE type = 'admin' AND account_status = 'Active' AND id like '" + txtSearchEmployeePassword1.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c});
                }
                calculateAdministratorCount();
            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblAdministratorPassword.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE type = 'admin' AND account_status = 'Active' AND username like '" + txtSearchEmployeePassword1.getText() + "%' order by username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c});
                }
                calculateAdministratorCount();
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    private void changeAdministratorPassword() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Change Password of this Administrator?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSelectedAdminId1.getText().equals("")
                    & !txtSelectedAdminUsername1.getText().equals("")
                    & !pwdSelectedAdminNewPassword.getPassword().equals("")
                    & !pwdSelectedRepeatAdminPassword.getPassword().equals("")) {

                if (!(String.valueOf(pwdSelectedAdminNewPassword.getPassword()).length() >= 4)) {
                    JOptionPane.showMessageDialog(this, "Password is Too Short!", "Invalid Password", JOptionPane.WARNING_MESSAGE); //Short password
                    pwdSelectedAdminNewPassword.setText(null);
                    pwdSelectedRepeatAdminPassword.setText(null);
                    pwdSelectedAdminNewPassword.grabFocus();

                } else if (!String.valueOf(pwdSelectedAdminNewPassword.getPassword()).equals(String.valueOf(pwdSelectedRepeatAdminPassword.getPassword()))) {
                    JOptionPane.showMessageDialog(this, "Password doesn't match!", "Invalid Password Matching", JOptionPane.WARNING_MESSAGE); //invalid password matching
                    pwdSelectedAdminNewPassword.setText(null);
                    pwdSelectedRepeatAdminPassword.setText(null);
                    pwdSelectedAdminNewPassword.grabFocus();

                } else {

                    try {
                        DB.DB.putData("UPDATE account SET password ='" + String.valueOf(pwdSelectedRepeatAdminPassword.getPassword()) + "' WHERE id ='" + txtSelectedAdminId1.getText() + "' ");
                        String changeEmployeePassowrdActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblSuperUserUsername.getText() + "'),'Super User','" + lblSuperUserUsername.getText() + "','Change Password','Super user changed password of admin " + txtSelectedAdminUsername1.getText() + "','SUCCESS')";
                        DB.DB.putData(changeEmployeePassowrdActivityLog);

                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Administrator Password Changed Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                        txtSelectedAdminId1.setText("");
                        txtSelectedAdminUsername1.setText("");
                        txtSelectedAdminCurrentPassword.setText("");
                        pwdSelectedAdminNewPassword.setText("");
                        pwdSelectedRepeatAdminPassword.setText("");
                        refreshAdministratorPasswordTable();

                    } catch (Exception e) {
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void calculateSuperUserCount() {
        int count = tblSuperUserPassword.getRowCount();
        lblSuperUserCount.setText("Super User Count : " + Integer.toString(count));

    }

    private void refreshSuperUserPasswordTable() {

        try {
            DefaultTableModel dtm = (DefaultTableModel) tblSuperUserPassword.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE type = 'super' AND account_status = 'Active' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(6));
                dtm.addRow(new Object[]{a, b, c});
            }
            calculateSuperUserCount();
        } catch (Exception e) {
        }

    }

    private void searchSuperUserPassword() {
        switch (cmbSortSuperUserPassword.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSuperUserPassword.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE type = 'super' AND account_status = 'Active' AND id like '" + txtSearchSuperUserPassword.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c});
                }
                calculateSuperUserCount();
            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSuperUserPassword.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE type = 'super' AND account_status = 'Active' AND username like '" + txtSearchSuperUserPassword.getText() + "%' order by username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c});
                }
                calculateSuperUserCount();
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    private void changeSuperUserPassword() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Change Password of this Super User?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSelectedSuperUserId.getText().equals("")
                    & !txtSelectedSuperUserUsername.getText().equals("")
                    & !pwdSelectedSuperUserNewPassword.getPassword().equals("")
                    & !pwdSelectedRepeatSuperUserPassword.getPassword().equals("")) {

                if (!(String.valueOf(pwdSelectedSuperUserNewPassword.getPassword()).length() >= 4)) {
                    JOptionPane.showMessageDialog(this, "Password is Too Short!", "Invalid Password", JOptionPane.WARNING_MESSAGE); //Short password
                    pwdSelectedSuperUserNewPassword.setText(null);
                    pwdSelectedRepeatSuperUserPassword.setText(null);
                    pwdSelectedSuperUserNewPassword.grabFocus();

                } else if (!String.valueOf(pwdSelectedSuperUserNewPassword.getPassword()).equals(String.valueOf(pwdSelectedRepeatSuperUserPassword.getPassword()))) {
                    JOptionPane.showMessageDialog(this, "Password doesn't match!", "Invalid Password Matching", JOptionPane.WARNING_MESSAGE); //invalid password matching
                    pwdSelectedSuperUserNewPassword.setText(null);
                    pwdSelectedRepeatSuperUserPassword.setText(null);
                    pwdSelectedSuperUserNewPassword.grabFocus();

                } else {

                    try {
                        DB.DB.putData("UPDATE account SET password ='" + String.valueOf(pwdSelectedRepeatSuperUserPassword.getPassword()) + "' WHERE id ='" + txtSelectedSuperUserId.getText() + "' ");
                        String changeEmployeePassowrdActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblSuperUserUsername.getText() + "'),'Super User','" + lblSuperUserUsername.getText() + "','Change Password','Super user changed password of super user " + txtSelectedSuperUserUsername.getText() + "','SUCCESS')";
                        DB.DB.putData(changeEmployeePassowrdActivityLog);

                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Super User Password Changed Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                        txtSelectedSuperUserId.setText("");
                        txtSelectedSuperUserUsername.setText("");
                        txtSelectedSuperUserCurrentPassword.setText("");
                        pwdSelectedSuperUserNewPassword.setText("");
                        pwdSelectedRepeatSuperUserPassword.setText("");
                        refreshSuperUserPasswordTable();

                    } catch (Exception e) {
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
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
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(7));
                String g = (rs.getString(8));
                String h = (rs.getString(9));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});

            }

            calculateStockCount();
            txtSearchStock.setText(null);

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
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
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
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
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
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateStockCount();
            } catch (Exception e) {
            }
            break;
            case 3:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stock_status = 'Available' AND (stock_count > low_stock) AND supplier like '" + txtSearchStock.getText() + "%' order by supplier ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateStockCount();
            } catch (Exception e) {
            }
            break;
            case 4:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stock_status = 'Available' AND (stock_count > low_stock) AND selling_price like '" + txtSearchStock.getText() + "%' order by selling_price ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateStockCount();
            } catch (Exception e) {
            }
            break;
            case 5:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stock_status = 'Available' AND id like '" + txtSearchStock.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateStockCount();
            } catch (Exception e) {
            }
            break;
            case 6:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stock_status = 'Available' AND (stock_count < low_stock) AND id like '" + txtSearchStock.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateStockCount();
            } catch (Exception e) {
            }
            default:
                break;
        }
    }

    private void deleteStockItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Permanently Delete this Stock Item?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            try {

                DB.DB.putData("UPDATE stock SET stock_status = 'Unavailable' WHERE id ='" + txtSKUId.getText() + "' ");
                String deleteStockItemActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblSuperUserUsername.getText() + "'),'Super User','" + lblSuperUserUsername.getText() + "','Delete Stock Item','Super user deleted stock item " + txtItemName.getText() + "','SUCCESS')";
                DB.DB.putData(deleteStockItemActivityLog);

                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                JOptionPane.showMessageDialog(this, "Stock Item Permanently Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                txtSKUId.setText(null);
                txtItemCode.setText(null);
                txtItemName.setText(null);
                txtSupplier.setText(null);
                txtStockCount.setText(null);

                refreshStockTable();
            } catch (Exception e) {
            }
        } else {

        }
    }

    public void printStock() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewStockReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    private void calculateStockReturnCount() {
        int count = tblStockReturn.getRowCount();
        lblStockReturnCount.setText("Item Count : " + Integer.toString(count));

    }

    private void refreshStockReturnTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblStockReturn.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM stock_return order by `date / time` desc ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(2));
                String b = (rs.getString(3));
                String c = (rs.getString(4));
                String d = (rs.getString(5));
                String e = (rs.getString(6));
                String f = (rs.getString(7));
                dtm.addRow(new Object[]{a, b, c, d, e, f});

            }

            calculateStockReturnCount();
            txtSearchStockReturn.setText(null);

        } catch (Exception e) {
        }

    }

    private void searchStockReturn() {
        switch (cmbSortStockReturn.getSelectedIndex()) {
            case 0:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStockReturn.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock_return WHERE sku_id like '" + txtSearchStockReturn.getText() + "%' order by sku_id");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});

                }

                calculateStockReturnCount();

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStockReturn.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock_return WHERE item_code like '" + txtSearchStockReturn.getText() + "%' order by item_code");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});

                }

                calculateStockReturnCount();

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStockReturn.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock_return WHERE item_name like '" + txtSearchStockReturn.getText() + "%' order by item_name");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});

                }

                calculateStockReturnCount();
            } catch (Exception e) {
            }
            break;
            case 3:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblStockReturn.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock_return WHERE supplier like '" + txtSearchStockReturn.getText() + "%' order by supplier");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(2));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});

                }

                calculateStockReturnCount();
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    private void returnStockItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Return these Items?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSKUId.getText().equals("")
                    & (!txtItemCode.getText().equals(""))
                    & (!txtItemName.getText().equals(""))
                    & (!txtSupplier.getText().equals(""))
                    & (!txtStockCount.getText().equals(""))
                    & (!txtReturnCount.getText().equals(""))) {

                try {

                    if (Integer.parseInt(txtReturnCount.getText()) > Integer.parseInt(txtStockCount.getText())) {
                        JOptionPane.showMessageDialog(this, "No Sufficient Stock Count Available!", "Invalid Data", JOptionPane.WARNING_MESSAGE);

                    } else {

                        int currentStockCount = Integer.parseInt(txtStockCount.getText());
                        int returnAmount = Integer.parseInt(txtReturnCount.getText());
                        int newStockCount = currentStockCount - returnAmount;

                        DB.DB.putData("UPDATE stock SET stock_count ='" + newStockCount + "' WHERE item_code = '" + txtItemCode.getText() + "' ");
                        String returnStockItemActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblSuperUserUsername.getText() + "'),'Super User','" + lblSuperUserUsername.getText() + "','Return Stock Item','Super user returned stock item " + txtItemName.getText() + "','SUCCESS')";
                        DB.DB.putData(returnStockItemActivityLog);

                        String addToReturnHistory = "INSERT INTO stock_return(sku_id, item_code, item_name, supplier, return_count) VALUES ('" + txtSKUId.getText() + "', '" + txtItemCode.getText() + "', '" + txtItemName.getText() + "', '" + txtSupplier.getText() + "', '" + txtReturnCount.getText() + "') ";
                        DB.DB.putData(addToReturnHistory);

                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Stock Item Returned!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                        JOptionPane.showMessageDialog(this, "Stock Updated!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                    }

                    refreshStockTable();
                    refreshStockReturnTable();

                } catch (Exception ex) {
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }

        }

    }

    public void printSelectedStockReturn() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewSelectedStockReturnReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    public void printStockReturn() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewStockReturnReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
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
        pnlSuperUserDashboard = new javax.swing.JPanel();
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
        pnlLog = new javax.swing.JPanel();
        pnlIndicatorLog = new javax.swing.JPanel();
        logs = new javax.swing.JLabel();
        pnlAdmin = new javax.swing.JPanel();
        pnlIndicatorAdmin = new javax.swing.JPanel();
        admin = new javax.swing.JLabel();
        pnlSettings = new javax.swing.JPanel();
        pnlIndicatorSettings = new javax.swing.JPanel();
        reports = new javax.swing.JLabel();
        pnlBranding = new javax.swing.JPanel();
        jLabel111 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        watermark = new javax.swing.JLabel();
        pnlSales1 = new javax.swing.JPanel();
        pnlIndicatorSales1 = new javax.swing.JPanel();
        stock1 = new javax.swing.JLabel();
        pnlStockReturn = new javax.swing.JPanel();
        pnlIndicatorStockReturn = new javax.swing.JPanel();
        stock2 = new javax.swing.JLabel();
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
        jLabel69 = new javax.swing.JLabel();
        subCardHome = new javax.swing.JPanel();
        loginWish = new javax.swing.JLabel();
        adminName = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        btnEditEmployee1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        btnEditEmployee2 = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        cardSystemLog = new javax.swing.JPanel();
        pnlSystemLogSubSelection = new javax.swing.JPanel();
        btn_addUsers1 = new javax.swing.JLabel();
        subCardSystemLog = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tblSystemLogs = new javax.swing.JTable();
        btnRefreshLogs = new javax.swing.JButton();
        btnPrintLogs = new javax.swing.JButton();
        txtSearchSystemLogs = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        lblLogCount = new javax.swing.JLabel();
        cmbSortSystemLogs = new javax.swing.JComboBox<>();
        cardAdmin = new javax.swing.JPanel();
        pnlAdminSubSelection = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        subCardAdmin = new javax.swing.JPanel();
        card1 = new javax.swing.JPanel();
        jTabbedPaneAdministrators = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        txtAdminUsername = new javax.swing.JTextField();
        lblEmployeeUserameValidation = new javax.swing.JLabel();
        lblEmployeeNICValidation = new javax.swing.JLabel();
        txtAdminNIC = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtAdminPhoneNo = new javax.swing.JTextField();
        lblEmployeePhoneNoValidation = new javax.swing.JLabel();
        lblEmployeePasswordValidation = new javax.swing.JLabel();
        pwdAdminPassword = new javax.swing.JPasswordField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        pwdRepeatAdminPassword = new javax.swing.JPasswordField();
        lblEmployeeRepeatPasswordValidation = new javax.swing.JLabel();
        btnAddNewAdmin = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblAdmin = new javax.swing.JTable();
        btnEditAdmin = new javax.swing.JButton();
        btnDeleteAdmin = new javax.swing.JButton();
        btnPrintAdmins = new javax.swing.JButton();
        txtSearchAdmin = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        btnAdmin = new javax.swing.JButton();
        cmbSortAdmin = new javax.swing.JComboBox<>();
        lblAdminCount = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        lblSelectedEmployeePhoneNoValidation = new javax.swing.JLabel();
        lblSelectedEmployeeUsernameValidation = new javax.swing.JLabel();
        lblSelectedEmployeeIdValidation = new javax.swing.JLabel();
        lblSelectedEmployeeNICValidation = new javax.swing.JLabel();
        btnGoBackToViewAdmin = new javax.swing.JButton();
        btnUpdateSelectedAdmin = new javax.swing.JButton();
        btnDeleteSelectedAdmin = new javax.swing.JButton();
        btnPrintSelectedAdmin = new javax.swing.JButton();
        txtSelectedAdminPhoneNo = new javax.swing.JTextField();
        txtSelectedAdminNIC = new javax.swing.JTextField();
        txtSelectedAdminUsername = new javax.swing.JTextField();
        txtSelectedAdminId = new javax.swing.JTextField();
        jLabel155 = new javax.swing.JLabel();
        jLabel156 = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        cardStockReturn = new javax.swing.JPanel();
        pnlStockReturnSubSelection = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        subCardStockReturn = new javax.swing.JPanel();
        card2 = new javax.swing.JPanel();
        jTabbedPaneStockReturn = new javax.swing.JTabbedPane();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        tblStock = new javax.swing.JTable();
        btnDeleteStock = new javax.swing.JButton();
        btnReturnSelectedStockItems = new javax.swing.JButton();
        btnPrintStock = new javax.swing.JButton();
        txtSearchStock = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        btnStock = new javax.swing.JButton();
        cmbSortStock = new javax.swing.JComboBox<>();
        lblStockCount = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        txtSKUId = new javax.swing.JTextField();
        txtItemCode = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        txtItemName = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        btnReturnStockItems = new javax.swing.JButton();
        jLabel50 = new javax.swing.JLabel();
        txtSupplier = new javax.swing.JTextField();
        txtStockCount = new javax.swing.JTextField();
        txtReturnCount = new javax.swing.JTextField();
        btnPrintStockReturnBill = new javax.swing.JButton();
        btnGoBackToStock = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        tblStockReturn = new javax.swing.JTable();
        btnRefreshStockReturnHistory = new javax.swing.JButton();
        btnPrintStockReturnHistory = new javax.swing.JButton();
        txtSearchStockReturn = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        cmbSortStockReturn = new javax.swing.JComboBox<>();
        lblStockReturnCount = new javax.swing.JLabel();
        cardPassword = new javax.swing.JPanel();
        pnlPasswordSubSelection = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        subCardPassword = new javax.swing.JPanel();
        card3 = new javax.swing.JPanel();
        jTabbedPaneEmployeePassword = new javax.swing.JTabbedPane();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tblEmployeePassword = new javax.swing.JTable();
        btnChangeSelectedEmployeePassword = new javax.swing.JButton();
        txtSearchEmployeePassword = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        btnRefreshEmployeePasswords = new javax.swing.JButton();
        cmbSortEmployeePassword = new javax.swing.JComboBox<>();
        lblEmployeeCount = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        lblSelectedEmployeePhoneNoValidation1 = new javax.swing.JLabel();
        lblSelectedEmployeeSalaryValidation = new javax.swing.JLabel();
        btnGoBackToViewEmployeePasswords = new javax.swing.JButton();
        btnChangeEmployeePassword = new javax.swing.JButton();
        txtSelectedEmployeeCurrentPassword = new javax.swing.JTextField();
        txtSelectedEmployeeUsername = new javax.swing.JTextField();
        txtSelectedEmployeeId = new javax.swing.JTextField();
        jLabel157 = new javax.swing.JLabel();
        jLabel158 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        jLabel141 = new javax.swing.JLabel();
        pwdSelectedEmployeeNewPassword = new javax.swing.JPasswordField();
        pwdSelectedRepeatEmployeePassword = new javax.swing.JPasswordField();
        card4 = new javax.swing.JPanel();
        jTabbedPaneAdministratorPassword = new javax.swing.JTabbedPane();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        tblAdministratorPassword = new javax.swing.JTable();
        btnChangeSelectedAdminPassword = new javax.swing.JButton();
        txtSearchEmployeePassword1 = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        btnRefreshAdminPasswords = new javax.swing.JButton();
        cmbSortAdministratorPassword = new javax.swing.JComboBox<>();
        lblAdministratorCount = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        lblSelectedEmployeePhoneNoValidation4 = new javax.swing.JLabel();
        lblSelectedEmployeeSalaryValidation3 = new javax.swing.JLabel();
        btnGoBackToViewAdminPasswords = new javax.swing.JButton();
        btnChangeAdminPassword = new javax.swing.JButton();
        txtSelectedAdminCurrentPassword = new javax.swing.JTextField();
        txtSelectedAdminUsername1 = new javax.swing.JTextField();
        txtSelectedAdminId1 = new javax.swing.JTextField();
        jLabel163 = new javax.swing.JLabel();
        jLabel164 = new javax.swing.JLabel();
        jLabel146 = new javax.swing.JLabel();
        jLabel147 = new javax.swing.JLabel();
        jLabel148 = new javax.swing.JLabel();
        pwdSelectedAdminNewPassword = new javax.swing.JPasswordField();
        pwdSelectedRepeatAdminPassword = new javax.swing.JPasswordField();
        card5 = new javax.swing.JPanel();
        jTabbedPaneSuperUserPassword = new javax.swing.JTabbedPane();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        tblSuperUserPassword = new javax.swing.JTable();
        btnChangeSelectedSuperUserPassword = new javax.swing.JButton();
        txtSearchSuperUserPassword = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        btnSuperUserPasswords = new javax.swing.JButton();
        cmbSortSuperUserPassword = new javax.swing.JComboBox<>();
        lblSuperUserCount = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        lblSelectedEmployeePhoneNoValidation5 = new javax.swing.JLabel();
        lblSelectedEmployeeSalaryValidation4 = new javax.swing.JLabel();
        btnGoBackToViewSuperUserPasswords = new javax.swing.JButton();
        btnChangeSuperUserPassword = new javax.swing.JButton();
        txtSelectedSuperUserCurrentPassword = new javax.swing.JTextField();
        txtSelectedSuperUserUsername = new javax.swing.JTextField();
        txtSelectedSuperUserId = new javax.swing.JTextField();
        jLabel165 = new javax.swing.JLabel();
        jLabel166 = new javax.swing.JLabel();
        jLabel149 = new javax.swing.JLabel();
        jLabel150 = new javax.swing.JLabel();
        jLabel151 = new javax.swing.JLabel();
        pwdSelectedSuperUserNewPassword = new javax.swing.JPasswordField();
        pwdSelectedRepeatSuperUserPassword = new javax.swing.JPasswordField();
        cardSettings = new javax.swing.JPanel();
        pnlSettingsSubSelection = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        subCardSettings = new javax.swing.JPanel();
        card6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtBackupLocation = new javax.swing.JTextField();
        btnBrowseBackupPath = new javax.swing.JButton();
        btnBackup = new javax.swing.JButton();
        card7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtRestoreLocation = new javax.swing.JTextField();
        btnBrowseRestorePath = new javax.swing.JButton();
        btnRestore = new javax.swing.JButton();
        card8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        version = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        pnlLogoutLoader = new javax.swing.JPanel();
        pnlLogoutLoaderBody = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        pnlForceAccessLoader = new javax.swing.JPanel();
        pnlForceAccessLoaderBody = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Xeon Inventory");
        setLocationByPlatform(true);
        setUndecorated(true);
        setOpacity(0.985F);

        pnlFrames.setLayout(new java.awt.CardLayout());

        pnlSuperUserDashboard.setBackground(new java.awt.Color(0, 0, 0));
        pnlSuperUserDashboard.setPreferredSize(new java.awt.Dimension(1200, 690));

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

        lblSuperUserUsername.setBackground(new java.awt.Color(0, 0, 0));
        lblSuperUserUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblSuperUserUsername.setForeground(new java.awt.Color(255, 255, 255));
        lblSuperUserUsername.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSuperUserUsername.setText("Super User");
        lblSuperUserUsername.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblSuperUserUsername.setOpaque(true);
        lblSuperUserUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblSuperUserUsernameMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblSuperUserUsernameMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnlTopLayout = new javax.swing.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 671, Short.MAX_VALUE)
                .addComponent(lblSuperUserUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTopIcons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlTopLayout.setVerticalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTopIcons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblSuperUserUsername, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        pnlLog.setBackground(new java.awt.Color(35, 35, 35));
        pnlLog.setForeground(new java.awt.Color(20, 20, 20));
        pnlLog.setPreferredSize(new java.awt.Dimension(250, 55));
        pnlLog.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlLogMouseClicked(evt);
            }
        });

        pnlIndicatorLog.setBackground(new java.awt.Color(35, 35, 35));
        pnlIndicatorLog.setPreferredSize(new java.awt.Dimension(4, 0));

        javax.swing.GroupLayout pnlIndicatorLogLayout = new javax.swing.GroupLayout(pnlIndicatorLog);
        pnlIndicatorLog.setLayout(pnlIndicatorLogLayout);
        pnlIndicatorLogLayout.setHorizontalGroup(
            pnlIndicatorLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        pnlIndicatorLogLayout.setVerticalGroup(
            pnlIndicatorLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        logs.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        logs.setForeground(new java.awt.Color(255, 255, 255));
        logs.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        logs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/logs.icon.png"))); // NOI18N
        logs.setText("Logs");
        logs.setIconTextGap(20);
        logs.setPreferredSize(new java.awt.Dimension(74, 30));
        logs.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlLogLayout = new javax.swing.GroupLayout(pnlLog);
        pnlLog.setLayout(pnlLogLayout);
        pnlLogLayout.setHorizontalGroup(
            pnlLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIndicatorLog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logs, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlLogLayout.setVerticalGroup(
            pnlLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIndicatorLog, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(logs, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
        );

        logs.getAccessibleContext().setAccessibleName("");

        pnlAdmin.setBackground(new java.awt.Color(35, 35, 35));
        pnlAdmin.setForeground(new java.awt.Color(20, 20, 20));
        pnlAdmin.setPreferredSize(new java.awt.Dimension(250, 55));
        pnlAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlAdminMouseClicked(evt);
            }
        });

        pnlIndicatorAdmin.setBackground(new java.awt.Color(35, 35, 35));
        pnlIndicatorAdmin.setPreferredSize(new java.awt.Dimension(4, 0));

        javax.swing.GroupLayout pnlIndicatorAdminLayout = new javax.swing.GroupLayout(pnlIndicatorAdmin);
        pnlIndicatorAdmin.setLayout(pnlIndicatorAdminLayout);
        pnlIndicatorAdminLayout.setHorizontalGroup(
            pnlIndicatorAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        pnlIndicatorAdminLayout.setVerticalGroup(
            pnlIndicatorAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );

        admin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        admin.setForeground(new java.awt.Color(255, 255, 255));
        admin.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        admin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/admin.icon.png"))); // NOI18N
        admin.setText("Admin");
        admin.setIconTextGap(20);
        admin.setPreferredSize(new java.awt.Dimension(78, 30));
        admin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adminMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlAdminLayout = new javax.swing.GroupLayout(pnlAdmin);
        pnlAdmin.setLayout(pnlAdminLayout);
        pnlAdminLayout.setHorizontalGroup(
            pnlAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAdminLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIndicatorAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(admin, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlAdminLayout.setVerticalGroup(
            pnlAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pnlAdminLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlIndicatorAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlAdminLayout.createSequentialGroup()
                .addComponent(admin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        admin.getAccessibleContext().setAccessibleName("");

        pnlSettings.setBackground(new java.awt.Color(35, 35, 35));
        pnlSettings.setForeground(new java.awt.Color(20, 20, 20));
        pnlSettings.setPreferredSize(new java.awt.Dimension(250, 55));
        pnlSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlSettingsMouseClicked(evt);
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
            .addGap(0, 48, Short.MAX_VALUE)
        );

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

        pnlSales1.setBackground(new java.awt.Color(35, 35, 35));
        pnlSales1.setForeground(new java.awt.Color(20, 20, 20));
        pnlSales1.setPreferredSize(new java.awt.Dimension(250, 55));
        pnlSales1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlSales1MouseClicked(evt);
            }
        });

        pnlIndicatorSales1.setBackground(new java.awt.Color(35, 35, 35));
        pnlIndicatorSales1.setPreferredSize(new java.awt.Dimension(4, 0));

        javax.swing.GroupLayout pnlIndicatorSales1Layout = new javax.swing.GroupLayout(pnlIndicatorSales1);
        pnlIndicatorSales1.setLayout(pnlIndicatorSales1Layout);
        pnlIndicatorSales1Layout.setHorizontalGroup(
            pnlIndicatorSales1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        pnlIndicatorSales1Layout.setVerticalGroup(
            pnlIndicatorSales1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );

        stock1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        stock1.setForeground(new java.awt.Color(255, 255, 255));
        stock1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        stock1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/passwords.icon.png"))); // NOI18N
        stock1.setText("Passwords");
        stock1.setIconTextGap(20);
        stock1.setPreferredSize(new java.awt.Dimension(78, 30));
        stock1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stock1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlSales1Layout = new javax.swing.GroupLayout(pnlSales1);
        pnlSales1.setLayout(pnlSales1Layout);
        pnlSales1Layout.setHorizontalGroup(
            pnlSales1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSales1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIndicatorSales1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(stock1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlSales1Layout.setVerticalGroup(
            pnlSales1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSales1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlIndicatorSales1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addGroup(pnlSales1Layout.createSequentialGroup()
                .addComponent(stock1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlStockReturn.setBackground(new java.awt.Color(35, 35, 35));
        pnlStockReturn.setForeground(new java.awt.Color(20, 20, 20));
        pnlStockReturn.setPreferredSize(new java.awt.Dimension(250, 55));
        pnlStockReturn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlStockReturnMouseClicked(evt);
            }
        });

        pnlIndicatorStockReturn.setBackground(new java.awt.Color(35, 35, 35));
        pnlIndicatorStockReturn.setPreferredSize(new java.awt.Dimension(4, 0));

        javax.swing.GroupLayout pnlIndicatorStockReturnLayout = new javax.swing.GroupLayout(pnlIndicatorStockReturn);
        pnlIndicatorStockReturn.setLayout(pnlIndicatorStockReturnLayout);
        pnlIndicatorStockReturnLayout.setHorizontalGroup(
            pnlIndicatorStockReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        pnlIndicatorStockReturnLayout.setVerticalGroup(
            pnlIndicatorStockReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );

        stock2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        stock2.setForeground(new java.awt.Color(255, 255, 255));
        stock2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        stock2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/stock_return.icon.png"))); // NOI18N
        stock2.setText("Stock Return");
        stock2.setIconTextGap(20);
        stock2.setPreferredSize(new java.awt.Dimension(78, 30));
        stock2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stock2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlStockReturnLayout = new javax.swing.GroupLayout(pnlStockReturn);
        pnlStockReturn.setLayout(pnlStockReturnLayout);
        pnlStockReturnLayout.setHorizontalGroup(
            pnlStockReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStockReturnLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIndicatorStockReturn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(stock2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlStockReturnLayout.setVerticalGroup(
            pnlStockReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStockReturnLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlIndicatorStockReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addGroup(pnlStockReturnLayout.createSequentialGroup()
                .addComponent(stock2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlSidebarLayout = new javax.swing.GroupLayout(pnlSidebar);
        pnlSidebar.setLayout(pnlSidebarLayout);
        pnlSidebarLayout.setHorizontalGroup(
            pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHome, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
            .addComponent(pnlLog, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
            .addComponent(pnlAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
            .addComponent(pnlSettings, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
            .addComponent(pnlBranding, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(watermark, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlSales1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
            .addComponent(pnlStockReturn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
        );
        pnlSidebarLayout.setVerticalGroup(
            pnlSidebarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSidebarLayout.createSequentialGroup()
                .addComponent(pnlBranding, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlHome, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlLog, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlStockReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlSales1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jLabel69.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(204, 204, 204));
        jLabel69.setText("Today is Monday, January 1, 2020");

        javax.swing.GroupLayout pnlHomeSubSelectionLayout = new javax.swing.GroupLayout(pnlHomeSubSelection);
        pnlHomeSubSelection.setLayout(pnlHomeSubSelectionLayout);
        pnlHomeSubSelectionLayout.setHorizontalGroup(
            pnlHomeSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHomeSubSelectionLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel69, javax.swing.GroupLayout.PREFERRED_SIZE, 426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlHomeSubSelectionLayout.setVerticalGroup(
            pnlHomeSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel69, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
        );

        subCardHome.setBackground(new java.awt.Color(0, 0, 0));

        loginWish.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        loginWish.setForeground(new java.awt.Color(255, 255, 255));
        loginWish.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        loginWish.setText("Good morning");
        loginWish.setToolTipText("");

        adminName.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        adminName.setForeground(new java.awt.Color(255, 255, 255));
        adminName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        adminName.setText("Super User");

        jPanel2.setBackground(new java.awt.Color(20, 47, 91));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Employee Login");

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/employee.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(20, 20, 20))
        );

        btnEditEmployee1.setBackground(new java.awt.Color(40, 40, 40));
        btnEditEmployee1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEditEmployee1.setForeground(new java.awt.Color(255, 255, 255));
        btnEditEmployee1.setText("Force Access Login");
        btnEditEmployee1.setFocusable(false);
        btnEditEmployee1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditEmployee1ActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(143, 28, 40));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Administrator Login");

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/administrator.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 313, Short.MAX_VALUE)
            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addGap(20, 20, 20))
        );

        btnEditEmployee2.setBackground(new java.awt.Color(40, 40, 40));
        btnEditEmployee2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEditEmployee2.setForeground(new java.awt.Color(255, 255, 255));
        btnEditEmployee2.setText("Force Access Login");
        btnEditEmployee2.setFocusable(false);
        btnEditEmployee2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditEmployee2ActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(100, 100, 100));
        jLabel39.setText("* Access Employee / Administrator Logins without usernames and passwords");

        javax.swing.GroupLayout subCardHomeLayout = new javax.swing.GroupLayout(subCardHome);
        subCardHome.setLayout(subCardHomeLayout);
        subCardHomeLayout.setHorizontalGroup(
            subCardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subCardHomeLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(subCardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39)
                    .addGroup(subCardHomeLayout.createSequentialGroup()
                        .addComponent(loginWish)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(adminName, javax.swing.GroupLayout.PREFERRED_SIZE, 456, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(subCardHomeLayout.createSequentialGroup()
                        .addGroup(subCardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEditEmployee2)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(subCardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEditEmployee1)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(253, Short.MAX_VALUE))
        );
        subCardHomeLayout.setVerticalGroup(
            subCardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subCardHomeLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(subCardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(loginWish, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(adminName, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addGroup(subCardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(subCardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEditEmployee1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditEmployee2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addComponent(jLabel39)
                .addContainerGap(69, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout cardHomeLayout = new javax.swing.GroupLayout(cardHome);
        cardHome.setLayout(cardHomeLayout);
        cardHomeLayout.setHorizontalGroup(
            cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHomeSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(subCardHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardHomeLayout.setVerticalGroup(
            cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardHomeLayout.createSequentialGroup()
                .addComponent(pnlHomeSubSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subCardHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainCard.add(cardHome, "card2");

        cardSystemLog.setBackground(new java.awt.Color(0, 0, 0));

        pnlSystemLogSubSelection.setBackground(new java.awt.Color(20, 20, 20));

        btn_addUsers1.setBackground(new java.awt.Color(18, 18, 18));
        btn_addUsers1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        btn_addUsers1.setForeground(new java.awt.Color(204, 204, 204));
        btn_addUsers1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn_addUsers1.setText("Entire Xeon Inventory Logs");
        btn_addUsers1.setOpaque(true);
        btn_addUsers1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_addUsers1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlSystemLogSubSelectionLayout = new javax.swing.GroupLayout(pnlSystemLogSubSelection);
        pnlSystemLogSubSelection.setLayout(pnlSystemLogSubSelectionLayout);
        pnlSystemLogSubSelectionLayout.setHorizontalGroup(
            pnlSystemLogSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSystemLogSubSelectionLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(btn_addUsers1, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(441, Short.MAX_VALUE))
        );
        pnlSystemLogSubSelectionLayout.setVerticalGroup(
            pnlSystemLogSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSystemLogSubSelectionLayout.createSequentialGroup()
                .addComponent(btn_addUsers1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        subCardSystemLog.setBackground(new java.awt.Color(0, 0, 0));

        tblSystemLogs.setAutoCreateRowSorter(true);
        tblSystemLogs.setBackground(new java.awt.Color(0, 0, 0));
        tblSystemLogs.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblSystemLogs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " Id", "User", "Username", "Activity", "Description", "State", "Date / Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSystemLogs.setGridColor(new java.awt.Color(70, 70, 70));
        tblSystemLogs.setRequestFocusEnabled(false);
        tblSystemLogs.setRowHeight(25);
        tblSystemLogs.setSelectionBackground(new java.awt.Color(0, 59, 94));
        jScrollPane13.setViewportView(tblSystemLogs);

        btnRefreshLogs.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshLogs.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshLogs.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshLogs.setText("Refresh");
        btnRefreshLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshLogsActionPerformed(evt);
            }
        });

        btnPrintLogs.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintLogs.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintLogs.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintLogs.setText("Print All");
        btnPrintLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintLogsActionPerformed(evt);
            }
        });

        txtSearchSystemLogs.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchSystemLogs.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchSystemLogs.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchSystemLogs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchSystemLogsKeyReleased(evt);
            }
        });

        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        lblLogCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblLogCount.setForeground(new java.awt.Color(153, 153, 153));
        lblLogCount.setText("Log Count : 0");

        cmbSortSystemLogs.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortSystemLogs.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortSystemLogs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "View All Logs", "Intruder Login", "Employee Login", "Admin Login", "Super User Login", "Employee Logout", "Admin Logout", "Super User Logout", "Add New Supplier", "Update Supplier", "Delete Supplier", "Add Item to Item Collection", "Update Supplier Item", "Delete Supplier Item", "Add New Employee", "Update Employee", "Delete Employee", "Employee System Exit", "Admin System Exit", "Super User System Exit", "Employee Payroll", "Purchase Order", "Bad Order", "Add New Stock Item", "GRN Checked", "Re-Order Stock Item", "Add New Sale Item", "Remove New Sale Item", "Issue Invoice / Items", "Force Access Employee Login", "Force Access Admin Login", "System Backup", "System Restore", "Add New Administrator", "Update Administrator", "Delete Administrator", "Delete Stock Item", "Return Stock Item", "Change Password" }));
        cmbSortSystemLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortSystemLogsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subCardSystemLogLayout = new javax.swing.GroupLayout(subCardSystemLog);
        subCardSystemLog.setLayout(subCardSystemLogLayout);
        subCardSystemLogLayout.setHorizontalGroup(
            subCardSystemLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subCardSystemLogLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(subCardSystemLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(subCardSystemLogLayout.createSequentialGroup()
                        .addComponent(txtSearchSystemLogs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortSystemLogs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subCardSystemLogLayout.createSequentialGroup()
                        .addComponent(lblLogCount, javax.swing.GroupLayout.PREFERRED_SIZE, 438, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 294, Short.MAX_VALUE)
                        .addComponent(btnRefreshLogs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintLogs))
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(20, 20, 20))
        );
        subCardSystemLogLayout.setVerticalGroup(
            subCardSystemLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subCardSystemLogLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(subCardSystemLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addGroup(subCardSystemLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearchSystemLogs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbSortSystemLogs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(subCardSystemLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrintLogs, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefreshLogs, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLogCount))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout cardSystemLogLayout = new javax.swing.GroupLayout(cardSystemLog);
        cardSystemLog.setLayout(cardSystemLogLayout);
        cardSystemLogLayout.setHorizontalGroup(
            cardSystemLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlSystemLogSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(subCardSystemLog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardSystemLogLayout.setVerticalGroup(
            cardSystemLogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardSystemLogLayout.createSequentialGroup()
                .addComponent(pnlSystemLogSubSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subCardSystemLog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainCard.add(cardSystemLog, "card2");

        cardAdmin.setBackground(new java.awt.Color(0, 0, 0));

        pnlAdminSubSelection.setBackground(new java.awt.Color(20, 20, 20));

        jLabel21.setBackground(new java.awt.Color(20, 20, 20));
        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(204, 204, 204));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel21.setText("Manage Administrators");
        jLabel21.setOpaque(true);

        javax.swing.GroupLayout pnlAdminSubSelectionLayout = new javax.swing.GroupLayout(pnlAdminSubSelection);
        pnlAdminSubSelection.setLayout(pnlAdminSubSelectionLayout);
        pnlAdminSubSelectionLayout.setHorizontalGroup(
            pnlAdminSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAdminSubSelectionLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 706, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlAdminSubSelectionLayout.setVerticalGroup(
            pnlAdminSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
        );

        subCardAdmin.setBackground(new java.awt.Color(153, 153, 153));
        subCardAdmin.setLayout(new java.awt.CardLayout());

        card1.setBackground(new java.awt.Color(20, 20, 20));

        jTabbedPaneAdministrators.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPaneAdministrators.setFocusable(false);
        jTabbedPaneAdministrators.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPaneAdministrators.setOpaque(true);

        jPanel5.setBackground(new java.awt.Color(0, 0, 0));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Name");

        txtAdminUsername.setBackground(new java.awt.Color(18, 18, 18));
        txtAdminUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtAdminUsername.setForeground(new java.awt.Color(255, 255, 255));
        txtAdminUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAdminUsernameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAdminUsernameKeyTyped(evt);
            }
        });

        lblEmployeeUserameValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmployeeUserameValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblEmployeeNICValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmployeeNICValidation.setForeground(new java.awt.Color(255, 0, 51));

        txtAdminNIC.setBackground(new java.awt.Color(18, 18, 18));
        txtAdminNIC.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtAdminNIC.setForeground(new java.awt.Color(255, 255, 255));
        txtAdminNIC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAdminNICKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAdminNICKeyTyped(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("NIC");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Phone No.");

        txtAdminPhoneNo.setBackground(new java.awt.Color(18, 18, 18));
        txtAdminPhoneNo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtAdminPhoneNo.setForeground(new java.awt.Color(255, 255, 255));
        txtAdminPhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAdminPhoneNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAdminPhoneNoKeyTyped(evt);
            }
        });

        lblEmployeePhoneNoValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmployeePhoneNoValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblEmployeePasswordValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmployeePasswordValidation.setForeground(new java.awt.Color(255, 0, 51));

        pwdAdminPassword.setBackground(new java.awt.Color(18, 18, 18));
        pwdAdminPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pwdAdminPassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdAdminPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdAdminPasswordKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pwdAdminPasswordKeyTyped(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Password");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Repeat Password");

        pwdRepeatAdminPassword.setBackground(new java.awt.Color(18, 18, 18));
        pwdRepeatAdminPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pwdRepeatAdminPassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdRepeatAdminPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pwdRepeatAdminPasswordActionPerformed(evt);
            }
        });
        pwdRepeatAdminPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdRepeatAdminPasswordKeyPressed(evt);
            }
        });

        lblEmployeeRepeatPasswordValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmployeeRepeatPasswordValidation.setForeground(new java.awt.Color(255, 0, 51));

        btnAddNewAdmin.setBackground(new java.awt.Color(40, 40, 40));
        btnAddNewAdmin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAddNewAdmin.setForeground(new java.awt.Color(255, 255, 255));
        btnAddNewAdmin.setText("Add New Admin");
        btnAddNewAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewAdminActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtAdminPhoneNo, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(pwdAdminPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtAdminUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtAdminNIC, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(pwdRepeatAdminPassword)))
                    .addComponent(btnAddNewAdmin))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblEmployeeUserameValidation, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                    .addComponent(lblEmployeeNICValidation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEmployeePhoneNoValidation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEmployeePasswordValidation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEmployeeRepeatPasswordValidation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtAdminUsername)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtAdminNIC))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtAdminPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pwdAdminPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pwdRepeatAdminPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addComponent(btnAddNewAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblEmployeeUserameValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblEmployeeNICValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblEmployeePhoneNoValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblEmployeePasswordValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblEmployeeRepeatPasswordValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(155, Short.MAX_VALUE))
        );

        jTabbedPaneAdministrators.addTab("New Admin", jPanel5);

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));

        tblAdmin.setBackground(new java.awt.Color(0, 0, 0));
        tblAdmin.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblAdmin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " Id", "Name", "Account Type", "NIC", "Phone No."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAdmin.setGridColor(new java.awt.Color(70, 70, 70));
        tblAdmin.setRequestFocusEnabled(false);
        tblAdmin.setRowHeight(25);
        tblAdmin.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAdminMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tblAdmin);
        if (tblAdmin.getColumnModel().getColumnCount() > 0) {
            tblAdmin.getColumnModel().getColumn(0).setPreferredWidth(25);
        }

        btnEditAdmin.setBackground(new java.awt.Color(40, 40, 40));
        btnEditAdmin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEditAdmin.setForeground(new java.awt.Color(255, 255, 255));
        btnEditAdmin.setText("Edit");
        btnEditAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditAdminActionPerformed(evt);
            }
        });

        btnDeleteAdmin.setBackground(new java.awt.Color(40, 40, 40));
        btnDeleteAdmin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeleteAdmin.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteAdmin.setText("Delete");
        btnDeleteAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAdminActionPerformed(evt);
            }
        });

        btnPrintAdmins.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintAdmins.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintAdmins.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintAdmins.setText("Print All");
        btnPrintAdmins.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintAdminsActionPerformed(evt);
            }
        });

        txtSearchAdmin.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchAdmin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchAdmin.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchAdmin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchAdminKeyReleased(evt);
            }
        });

        jLabel19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        btnAdmin.setBackground(new java.awt.Color(40, 40, 40));
        btnAdmin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAdmin.setForeground(new java.awt.Color(255, 255, 255));
        btnAdmin.setText("Refresh");
        btnAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdminActionPerformed(evt);
            }
        });

        cmbSortAdmin.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortAdmin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortAdmin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id", "Name", "NIC", "Phone No." }));
        cmbSortAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortAdminActionPerformed(evt);
            }
        });

        lblAdminCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblAdminCount.setForeground(new java.awt.Color(153, 153, 153));
        lblAdminCount.setText("Administrator Count : 0");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(txtSearchAdmin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(lblAdminCount, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdmin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditAdmin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteAdmin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintAdmins))
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 924, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbSortAdmin, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtSearchAdmin, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrintAdmins, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAdminCount))
                .addGap(20, 20, 20))
        );

        jTabbedPaneAdministrators.addTab("View Admin", jPanel10);

        jPanel11.setBackground(new java.awt.Color(0, 0, 0));

        lblSelectedEmployeePhoneNoValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeePhoneNoValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedEmployeeUsernameValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeeUsernameValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedEmployeeIdValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeeIdValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedEmployeeNICValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeeNICValidation.setForeground(new java.awt.Color(255, 0, 51));

        btnGoBackToViewAdmin.setBackground(new java.awt.Color(40, 40, 40));
        btnGoBackToViewAdmin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnGoBackToViewAdmin.setForeground(new java.awt.Color(255, 255, 255));
        btnGoBackToViewAdmin.setText("Go Back");
        btnGoBackToViewAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackToViewAdminActionPerformed(evt);
            }
        });

        btnUpdateSelectedAdmin.setBackground(new java.awt.Color(40, 40, 40));
        btnUpdateSelectedAdmin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnUpdateSelectedAdmin.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateSelectedAdmin.setText("Update");
        btnUpdateSelectedAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateSelectedAdminActionPerformed(evt);
            }
        });

        btnDeleteSelectedAdmin.setBackground(new java.awt.Color(40, 40, 40));
        btnDeleteSelectedAdmin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeleteSelectedAdmin.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteSelectedAdmin.setText("Delete");
        btnDeleteSelectedAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSelectedAdminActionPerformed(evt);
            }
        });

        btnPrintSelectedAdmin.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintSelectedAdmin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintSelectedAdmin.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintSelectedAdmin.setText("Print");
        btnPrintSelectedAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintSelectedAdminActionPerformed(evt);
            }
        });

        txtSelectedAdminPhoneNo.setBackground(new java.awt.Color(18, 18, 18));
        txtSelectedAdminPhoneNo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedAdminPhoneNo.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedAdminPhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedAdminPhoneNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedAdminPhoneNoKeyTyped(evt);
            }
        });

        txtSelectedAdminNIC.setBackground(new java.awt.Color(18, 18, 18));
        txtSelectedAdminNIC.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedAdminNIC.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedAdminNIC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedAdminNICKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedAdminNICKeyTyped(evt);
            }
        });

        txtSelectedAdminUsername.setEditable(false);
        txtSelectedAdminUsername.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedAdminUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedAdminUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedAdminUsernameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedAdminUsernameKeyTyped(evt);
            }
        });

        txtSelectedAdminId.setEditable(false);
        txtSelectedAdminId.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedAdminId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedAdminId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedAdminIdKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedAdminIdKeyTyped(evt);
            }
        });

        jLabel155.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel155.setForeground(new java.awt.Color(255, 255, 255));
        jLabel155.setText("Id");

        jLabel156.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel156.setForeground(new java.awt.Color(255, 255, 255));
        jLabel156.setText("Name");

        jLabel134.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel134.setForeground(new java.awt.Color(255, 255, 255));
        jLabel134.setText("NIC");

        jLabel135.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel135.setForeground(new java.awt.Color(255, 255, 255));
        jLabel135.setText("Phone No.");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGoBackToViewAdmin)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(btnUpdateSelectedAdmin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteSelectedAdmin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintSelectedAdmin))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel135, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel134, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel155, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel156, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtSelectedAdminPhoneNo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtSelectedAdminNIC, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSelectedAdminUsername, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSelectedAdminId, javax.swing.GroupLayout.Alignment.LEADING))))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSelectedEmployeeUsernameValidation, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                    .addComponent(lblSelectedEmployeePhoneNoValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSelectedEmployeeNICValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                    .addComponent(lblSelectedEmployeeIdValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(lblSelectedEmployeeIdValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedEmployeeUsernameValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedEmployeeNICValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedEmployeePhoneNoValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSelectedAdminId)
                            .addComponent(jLabel155, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSelectedAdminUsername)
                            .addComponent(jLabel156, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel134, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSelectedAdminNIC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel135, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSelectedAdminPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUpdateSelectedAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeleteSelectedAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPrintSelectedAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGoBackToViewAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(148, Short.MAX_VALUE))
        );

        jTabbedPaneAdministrators.addTab("Edit Admin", jPanel11);

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAdministrators)
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAdministrators)
        );

        subCardAdmin.add(card1, "card3");

        javax.swing.GroupLayout cardAdminLayout = new javax.swing.GroupLayout(cardAdmin);
        cardAdmin.setLayout(cardAdminLayout);
        cardAdminLayout.setHorizontalGroup(
            cardAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(subCardAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlAdminSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardAdminLayout.setVerticalGroup(
            cardAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardAdminLayout.createSequentialGroup()
                .addComponent(pnlAdminSubSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subCardAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mainCard.add(cardAdmin, "card2");

        cardStockReturn.setBackground(new java.awt.Color(0, 0, 0));

        pnlStockReturnSubSelection.setBackground(new java.awt.Color(20, 20, 20));

        jLabel24.setBackground(new java.awt.Color(20, 20, 20));
        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(204, 204, 204));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel24.setText("Return Stock Items");
        jLabel24.setOpaque(true);

        javax.swing.GroupLayout pnlStockReturnSubSelectionLayout = new javax.swing.GroupLayout(pnlStockReturnSubSelection);
        pnlStockReturnSubSelection.setLayout(pnlStockReturnSubSelectionLayout);
        pnlStockReturnSubSelectionLayout.setHorizontalGroup(
            pnlStockReturnSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStockReturnSubSelectionLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 706, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlStockReturnSubSelectionLayout.setVerticalGroup(
            pnlStockReturnSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
        );

        subCardStockReturn.setBackground(new java.awt.Color(153, 153, 153));
        subCardStockReturn.setLayout(new java.awt.CardLayout());

        card2.setBackground(new java.awt.Color(20, 20, 20));

        jTabbedPaneStockReturn.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPaneStockReturn.setFocusable(false);
        jTabbedPaneStockReturn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPaneStockReturn.setOpaque(true);

        jPanel15.setBackground(new java.awt.Color(0, 0, 0));

        tblStock.setBackground(new java.awt.Color(0, 0, 0));
        tblStock.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " SKU Id", "Item Code", "Item Name", "Purchase Price", "Selling Price", "Supplier", "Stock Count", "Low Stock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblStock.setGridColor(new java.awt.Color(70, 70, 70));
        tblStock.setRequestFocusEnabled(false);
        tblStock.setRowHeight(25);
        tblStock.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStockMouseClicked(evt);
            }
        });
        jScrollPane15.setViewportView(tblStock);
        if (tblStock.getColumnModel().getColumnCount() > 0) {
            tblStock.getColumnModel().getColumn(0).setPreferredWidth(25);
        }

        btnDeleteStock.setBackground(new java.awt.Color(40, 40, 40));
        btnDeleteStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeleteStock.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteStock.setText("Delete");
        btnDeleteStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteStockActionPerformed(evt);
            }
        });

        btnReturnSelectedStockItems.setBackground(new java.awt.Color(40, 40, 40));
        btnReturnSelectedStockItems.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnReturnSelectedStockItems.setForeground(new java.awt.Color(255, 255, 255));
        btnReturnSelectedStockItems.setText("Return Item(s)");
        btnReturnSelectedStockItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturnSelectedStockItemsActionPerformed(evt);
            }
        });

        btnPrintStock.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintStock.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintStock.setText("Print All");
        btnPrintStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintStockActionPerformed(evt);
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

        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        btnStock.setBackground(new java.awt.Color(40, 40, 40));
        btnStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnStock.setForeground(new java.awt.Color(255, 255, 255));
        btnStock.setText("Refresh");
        btnStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockActionPerformed(evt);
            }
        });

        cmbSortStock.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortStock.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SKU Id", "Item Code", "Item Name", "Supplier", "Selling Stock", "Whole Stock", "Low Stock" }));
        cmbSortStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortStockActionPerformed(evt);
            }
        });

        lblStockCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblStockCount.setForeground(new java.awt.Color(153, 153, 153));
        lblStockCount.setText("SKU Count : 0");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(txtSearchStock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortStock, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addComponent(lblStockCount, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnStock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteStock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnReturnSelectedStockItems)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintStock))
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(20, 20, 20))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbSortStock, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtSearchStock, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteStock, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrintStock, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReturnSelectedStockItems, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStock, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStockCount))
                .addGap(20, 20, 20))
        );

        jTabbedPaneStockReturn.addTab("Available Stock", jPanel15);

        jPanel8.setBackground(new java.awt.Color(0, 0, 0));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("SKU Id");

        txtSKUId.setBackground(new java.awt.Color(45, 45, 45));
        txtSKUId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSKUId.setForeground(new java.awt.Color(255, 255, 255));
        txtSKUId.setEnabled(false);

        txtItemCode.setBackground(new java.awt.Color(45, 45, 45));
        txtItemCode.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtItemCode.setForeground(new java.awt.Color(255, 255, 255));
        txtItemCode.setEnabled(false);

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Item Code");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Item Name");

        txtItemName.setBackground(new java.awt.Color(45, 45, 45));
        txtItemName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtItemName.setForeground(new java.awt.Color(255, 255, 255));
        txtItemName.setEnabled(false);

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Stock Count");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Return Count");

        btnReturnStockItems.setBackground(new java.awt.Color(40, 40, 40));
        btnReturnStockItems.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnReturnStockItems.setForeground(new java.awt.Color(255, 255, 255));
        btnReturnStockItems.setText("Return Stock Item(s)");
        btnReturnStockItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReturnStockItemsActionPerformed(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("Supplier");

        txtSupplier.setBackground(new java.awt.Color(45, 45, 45));
        txtSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSupplier.setForeground(new java.awt.Color(255, 255, 255));
        txtSupplier.setEnabled(false);

        txtStockCount.setBackground(new java.awt.Color(45, 45, 45));
        txtStockCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtStockCount.setForeground(new java.awt.Color(255, 255, 255));
        txtStockCount.setEnabled(false);

        txtReturnCount.setBackground(new java.awt.Color(18, 18, 18));
        txtReturnCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtReturnCount.setForeground(new java.awt.Color(255, 255, 255));
        txtReturnCount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtReturnCountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtReturnCountKeyReleased(evt);
            }
        });

        btnPrintStockReturnBill.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintStockReturnBill.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintStockReturnBill.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintStockReturnBill.setText("Print");
        btnPrintStockReturnBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintStockReturnBillActionPerformed(evt);
            }
        });

        btnGoBackToStock.setBackground(new java.awt.Color(40, 40, 40));
        btnGoBackToStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnGoBackToStock.setForeground(new java.awt.Color(255, 255, 255));
        btnGoBackToStock.setText("Go back");
        btnGoBackToStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackToStockActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtItemName, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtSKUId, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtItemCode, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtStockCount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtReturnCount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(btnReturnStockItems)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintStockReturnBill)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGoBackToStock)))
                .addContainerGap(441, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSKUId)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtItemCode))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStockCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtReturnCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnReturnStockItems, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrintStockReturnBill, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGoBackToStock, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(106, Short.MAX_VALUE))
        );

        jTabbedPaneStockReturn.addTab("Stock Return Details", jPanel8);

        jPanel16.setBackground(new java.awt.Color(0, 0, 0));

        tblStockReturn.setBackground(new java.awt.Color(0, 0, 0));
        tblStockReturn.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblStockReturn.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " SKU Id", "Item Code", "Item Name", "Supplier", "Return Count", "Date / Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblStockReturn.setGridColor(new java.awt.Color(70, 70, 70));
        tblStockReturn.setRequestFocusEnabled(false);
        tblStockReturn.setRowHeight(25);
        tblStockReturn.setSelectionBackground(new java.awt.Color(0, 59, 94));
        jScrollPane19.setViewportView(tblStockReturn);
        if (tblStockReturn.getColumnModel().getColumnCount() > 0) {
            tblStockReturn.getColumnModel().getColumn(0).setPreferredWidth(25);
        }

        btnRefreshStockReturnHistory.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshStockReturnHistory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshStockReturnHistory.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshStockReturnHistory.setText("Refresh");
        btnRefreshStockReturnHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshStockReturnHistoryActionPerformed(evt);
            }
        });

        btnPrintStockReturnHistory.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintStockReturnHistory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintStockReturnHistory.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintStockReturnHistory.setText("Print All");
        btnPrintStockReturnHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintStockReturnHistoryActionPerformed(evt);
            }
        });

        txtSearchStockReturn.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchStockReturn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchStockReturn.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchStockReturn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchStockReturnKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchStockReturnKeyTyped(evt);
            }
        });

        jLabel45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        cmbSortStockReturn.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortStockReturn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortStockReturn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SKU Id", "Item Code", "Item Name", "Supplier" }));
        cmbSortStockReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortStockReturnActionPerformed(evt);
            }
        });

        lblStockReturnCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblStockReturnCount.setForeground(new java.awt.Color(153, 153, 153));
        lblStockReturnCount.setText("Item Count : 0");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(txtSearchStockReturn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortStockReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel45))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                        .addComponent(lblStockReturnCount, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshStockReturnHistory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintStockReturnHistory))
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel45)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbSortStockReturn, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtSearchStockReturn, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrintStockReturnHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefreshStockReturnHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStockReturnCount))
                .addGap(20, 20, 20))
        );

        jTabbedPaneStockReturn.addTab("Stock Return History", jPanel16);

        javax.swing.GroupLayout card2Layout = new javax.swing.GroupLayout(card2);
        card2.setLayout(card2Layout);
        card2Layout.setHorizontalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneStockReturn)
        );
        card2Layout.setVerticalGroup(
            card2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneStockReturn)
        );

        subCardStockReturn.add(card2, "card3");

        javax.swing.GroupLayout cardStockReturnLayout = new javax.swing.GroupLayout(cardStockReturn);
        cardStockReturn.setLayout(cardStockReturnLayout);
        cardStockReturnLayout.setHorizontalGroup(
            cardStockReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(subCardStockReturn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlStockReturnSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardStockReturnLayout.setVerticalGroup(
            cardStockReturnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardStockReturnLayout.createSequentialGroup()
                .addComponent(pnlStockReturnSubSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subCardStockReturn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mainCard.add(cardStockReturn, "card2");

        cardPassword.setBackground(new java.awt.Color(0, 0, 0));

        pnlPasswordSubSelection.setBackground(new java.awt.Color(20, 20, 20));

        jLabel8.setBackground(new java.awt.Color(60, 60, 60));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Employee Passwords");
        jLabel8.setOpaque(true);
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        jLabel23.setBackground(new java.awt.Color(35, 35, 35));
        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Admin Passwords");
        jLabel23.setOpaque(true);
        jLabel23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel23MouseClicked(evt);
            }
        });

        jLabel31.setBackground(new java.awt.Color(35, 35, 35));
        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Super User Passwords");
        jLabel31.setOpaque(true);
        jLabel31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel31MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlPasswordSubSelectionLayout = new javax.swing.GroupLayout(pnlPasswordSubSelection);
        pnlPasswordSubSelection.setLayout(pnlPasswordSubSelectionLayout);
        pnlPasswordSubSelectionLayout.setHorizontalGroup(
            pnlPasswordSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPasswordSubSelectionLayout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlPasswordSubSelectionLayout.setVerticalGroup(
            pnlPasswordSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        subCardPassword.setBackground(new java.awt.Color(153, 153, 153));
        subCardPassword.setLayout(new java.awt.CardLayout());

        card3.setBackground(new java.awt.Color(20, 20, 20));

        jTabbedPaneEmployeePassword.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPaneEmployeePassword.setFocusable(false);
        jTabbedPaneEmployeePassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPaneEmployeePassword.setOpaque(true);

        jPanel13.setBackground(new java.awt.Color(0, 0, 0));

        tblEmployeePassword.setBackground(new java.awt.Color(0, 0, 0));
        tblEmployeePassword.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblEmployeePassword.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " Id", "Username", "Password", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmployeePassword.setGridColor(new java.awt.Color(70, 70, 70));
        tblEmployeePassword.setRequestFocusEnabled(false);
        tblEmployeePassword.setRowHeight(25);
        tblEmployeePassword.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblEmployeePassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmployeePasswordMouseClicked(evt);
            }
        });
        jScrollPane14.setViewportView(tblEmployeePassword);

        btnChangeSelectedEmployeePassword.setBackground(new java.awt.Color(40, 40, 40));
        btnChangeSelectedEmployeePassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnChangeSelectedEmployeePassword.setForeground(new java.awt.Color(255, 255, 255));
        btnChangeSelectedEmployeePassword.setText("Change Password");
        btnChangeSelectedEmployeePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeSelectedEmployeePasswordActionPerformed(evt);
            }
        });

        txtSearchEmployeePassword.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchEmployeePassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchEmployeePassword.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchEmployeePassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchEmployeePasswordKeyReleased(evt);
            }
        });

        jLabel43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        btnRefreshEmployeePasswords.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshEmployeePasswords.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshEmployeePasswords.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshEmployeePasswords.setText("Refresh");
        btnRefreshEmployeePasswords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshEmployeePasswordsActionPerformed(evt);
            }
        });

        cmbSortEmployeePassword.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortEmployeePassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortEmployeePassword.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id", "Username" }));
        cmbSortEmployeePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortEmployeePasswordActionPerformed(evt);
            }
        });

        lblEmployeeCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmployeeCount.setForeground(new java.awt.Color(153, 153, 153));
        lblEmployeeCount.setText("Employee Count : 0");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(txtSearchEmployeePassword)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortEmployeePassword, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel43))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(lblEmployeeCount, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshEmployeePasswords)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnChangeSelectedEmployeePassword))
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 924, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbSortEmployeePassword, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtSearchEmployeePassword, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChangeSelectedEmployeePassword, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefreshEmployeePasswords, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmployeeCount))
                .addGap(20, 20, 20))
        );

        jTabbedPaneEmployeePassword.addTab("View Employee", jPanel13);

        jPanel14.setBackground(new java.awt.Color(0, 0, 0));

        lblSelectedEmployeePhoneNoValidation1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeePhoneNoValidation1.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedEmployeeSalaryValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeeSalaryValidation.setForeground(new java.awt.Color(255, 0, 51));

        btnGoBackToViewEmployeePasswords.setBackground(new java.awt.Color(40, 40, 40));
        btnGoBackToViewEmployeePasswords.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnGoBackToViewEmployeePasswords.setForeground(new java.awt.Color(255, 255, 255));
        btnGoBackToViewEmployeePasswords.setText("Go Back");
        btnGoBackToViewEmployeePasswords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackToViewEmployeePasswordsActionPerformed(evt);
            }
        });

        btnChangeEmployeePassword.setBackground(new java.awt.Color(40, 40, 40));
        btnChangeEmployeePassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnChangeEmployeePassword.setForeground(new java.awt.Color(255, 255, 255));
        btnChangeEmployeePassword.setText("Change Password");
        btnChangeEmployeePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeEmployeePasswordActionPerformed(evt);
            }
        });

        txtSelectedEmployeeCurrentPassword.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedEmployeeCurrentPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedEmployeeCurrentPassword.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedEmployeeCurrentPassword.setEnabled(false);

        txtSelectedEmployeeUsername.setEditable(false);
        txtSelectedEmployeeUsername.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedEmployeeUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedEmployeeUsername.setEnabled(false);

        txtSelectedEmployeeId.setEditable(false);
        txtSelectedEmployeeId.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedEmployeeId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedEmployeeId.setEnabled(false);

        jLabel157.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel157.setForeground(new java.awt.Color(255, 255, 255));
        jLabel157.setText("Id");

        jLabel158.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel158.setForeground(new java.awt.Color(255, 255, 255));
        jLabel158.setText("Name");

        jLabel137.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel137.setForeground(new java.awt.Color(255, 255, 255));
        jLabel137.setText("Current Password");

        jLabel138.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel138.setForeground(new java.awt.Color(255, 255, 255));
        jLabel138.setText("Repeat Password");

        jLabel141.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel141.setForeground(new java.awt.Color(255, 255, 255));
        jLabel141.setText("New Password");

        pwdSelectedEmployeeNewPassword.setBackground(new java.awt.Color(18, 18, 18));
        pwdSelectedEmployeeNewPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pwdSelectedEmployeeNewPassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdSelectedEmployeeNewPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdSelectedEmployeeNewPasswordKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pwdSelectedEmployeeNewPasswordKeyTyped(evt);
            }
        });

        pwdSelectedRepeatEmployeePassword.setBackground(new java.awt.Color(18, 18, 18));
        pwdSelectedRepeatEmployeePassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pwdSelectedRepeatEmployeePassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdSelectedRepeatEmployeePassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdSelectedRepeatEmployeePasswordKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel138, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel157, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel158, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel141, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel137)
                    .addComponent(btnChangeEmployeePassword))
                .addGap(9, 9, 9)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGoBackToViewEmployeePasswords)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSelectedEmployeeCurrentPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSelectedEmployeeUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSelectedEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pwdSelectedEmployeeNewPassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pwdSelectedRepeatEmployeePassword, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSelectedEmployeeSalaryValidation, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                            .addComponent(lblSelectedEmployeePhoneNoValidation1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSelectedEmployeeId)
                    .addComponent(jLabel157, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSelectedEmployeeUsername)
                    .addComponent(jLabel158, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel137, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSelectedEmployeeCurrentPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel141, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pwdSelectedEmployeeNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pwdSelectedRepeatEmployeePassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel138, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(lblSelectedEmployeePhoneNoValidation1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedEmployeeSalaryValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChangeEmployeePassword, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGoBackToViewEmployeePasswords, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(151, Short.MAX_VALUE))
        );

        jTabbedPaneEmployeePassword.addTab("Change Password", jPanel14);

        javax.swing.GroupLayout card3Layout = new javax.swing.GroupLayout(card3);
        card3.setLayout(card3Layout);
        card3Layout.setHorizontalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneEmployeePassword)
        );
        card3Layout.setVerticalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneEmployeePassword)
        );

        subCardPassword.add(card3, "card3");

        card4.setBackground(new java.awt.Color(20, 20, 20));

        jTabbedPaneAdministratorPassword.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPaneAdministratorPassword.setFocusable(false);
        jTabbedPaneAdministratorPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPaneAdministratorPassword.setOpaque(true);

        jPanel19.setBackground(new java.awt.Color(0, 0, 0));

        tblAdministratorPassword.setBackground(new java.awt.Color(0, 0, 0));
        tblAdministratorPassword.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblAdministratorPassword.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " Id", "Username", "Password"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAdministratorPassword.setGridColor(new java.awt.Color(70, 70, 70));
        tblAdministratorPassword.setRequestFocusEnabled(false);
        tblAdministratorPassword.setRowHeight(25);
        tblAdministratorPassword.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblAdministratorPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAdministratorPasswordMouseClicked(evt);
            }
        });
        jScrollPane17.setViewportView(tblAdministratorPassword);

        btnChangeSelectedAdminPassword.setBackground(new java.awt.Color(40, 40, 40));
        btnChangeSelectedAdminPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnChangeSelectedAdminPassword.setForeground(new java.awt.Color(255, 255, 255));
        btnChangeSelectedAdminPassword.setText("Change Password");
        btnChangeSelectedAdminPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeSelectedAdminPasswordActionPerformed(evt);
            }
        });

        txtSearchEmployeePassword1.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchEmployeePassword1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchEmployeePassword1.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchEmployeePassword1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchEmployeePassword1KeyReleased(evt);
            }
        });

        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        btnRefreshAdminPasswords.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshAdminPasswords.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshAdminPasswords.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshAdminPasswords.setText("Refresh");
        btnRefreshAdminPasswords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshAdminPasswordsActionPerformed(evt);
            }
        });

        cmbSortAdministratorPassword.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortAdministratorPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortAdministratorPassword.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id", "Username" }));
        cmbSortAdministratorPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortAdministratorPasswordActionPerformed(evt);
            }
        });

        lblAdministratorCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblAdministratorCount.setForeground(new java.awt.Color(153, 153, 153));
        lblAdministratorCount.setText("Administrator Count : 0");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(txtSearchEmployeePassword1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortAdministratorPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel46))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                        .addComponent(lblAdministratorCount, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshAdminPasswords)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnChangeSelectedAdminPassword))
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 924, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbSortAdministratorPassword, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtSearchEmployeePassword1, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChangeSelectedAdminPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefreshAdminPasswords, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAdministratorCount))
                .addGap(20, 20, 20))
        );

        jTabbedPaneAdministratorPassword.addTab("View Administrator", jPanel19);

        jPanel20.setBackground(new java.awt.Color(0, 0, 0));

        lblSelectedEmployeePhoneNoValidation4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeePhoneNoValidation4.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedEmployeeSalaryValidation3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeeSalaryValidation3.setForeground(new java.awt.Color(255, 0, 51));

        btnGoBackToViewAdminPasswords.setBackground(new java.awt.Color(40, 40, 40));
        btnGoBackToViewAdminPasswords.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnGoBackToViewAdminPasswords.setForeground(new java.awt.Color(255, 255, 255));
        btnGoBackToViewAdminPasswords.setText("Go Back");
        btnGoBackToViewAdminPasswords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackToViewAdminPasswordsActionPerformed(evt);
            }
        });

        btnChangeAdminPassword.setBackground(new java.awt.Color(40, 40, 40));
        btnChangeAdminPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnChangeAdminPassword.setForeground(new java.awt.Color(255, 255, 255));
        btnChangeAdminPassword.setText("Change Password");
        btnChangeAdminPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeAdminPasswordActionPerformed(evt);
            }
        });

        txtSelectedAdminCurrentPassword.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedAdminCurrentPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedAdminCurrentPassword.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedAdminCurrentPassword.setEnabled(false);

        txtSelectedAdminUsername1.setEditable(false);
        txtSelectedAdminUsername1.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedAdminUsername1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedAdminUsername1.setEnabled(false);

        txtSelectedAdminId1.setEditable(false);
        txtSelectedAdminId1.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedAdminId1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedAdminId1.setEnabled(false);

        jLabel163.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel163.setForeground(new java.awt.Color(255, 255, 255));
        jLabel163.setText("Id");

        jLabel164.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel164.setForeground(new java.awt.Color(255, 255, 255));
        jLabel164.setText("Name");

        jLabel146.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel146.setForeground(new java.awt.Color(255, 255, 255));
        jLabel146.setText("Current Password");

        jLabel147.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel147.setForeground(new java.awt.Color(255, 255, 255));
        jLabel147.setText("Repeat Password");

        jLabel148.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel148.setForeground(new java.awt.Color(255, 255, 255));
        jLabel148.setText("New Password");

        pwdSelectedAdminNewPassword.setBackground(new java.awt.Color(18, 18, 18));
        pwdSelectedAdminNewPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pwdSelectedAdminNewPassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdSelectedAdminNewPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdSelectedAdminNewPasswordKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pwdSelectedAdminNewPasswordKeyTyped(evt);
            }
        });

        pwdSelectedRepeatAdminPassword.setBackground(new java.awt.Color(18, 18, 18));
        pwdSelectedRepeatAdminPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pwdSelectedRepeatAdminPassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdSelectedRepeatAdminPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdSelectedRepeatAdminPasswordKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel147, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel163, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel164, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel148, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel146)
                    .addComponent(btnChangeAdminPassword))
                .addGap(9, 9, 9)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGoBackToViewAdminPasswords)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSelectedAdminCurrentPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSelectedAdminUsername1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSelectedAdminId1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pwdSelectedAdminNewPassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pwdSelectedRepeatAdminPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSelectedEmployeeSalaryValidation3, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                            .addComponent(lblSelectedEmployeePhoneNoValidation4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSelectedAdminId1)
                    .addComponent(jLabel163, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSelectedAdminUsername1)
                    .addComponent(jLabel164, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel146, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSelectedAdminCurrentPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel148, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pwdSelectedAdminNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pwdSelectedRepeatAdminPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel147, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(lblSelectedEmployeePhoneNoValidation4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedEmployeeSalaryValidation3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChangeAdminPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGoBackToViewAdminPasswords, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(151, Short.MAX_VALUE))
        );

        jTabbedPaneAdministratorPassword.addTab("Change Password", jPanel20);

        javax.swing.GroupLayout card4Layout = new javax.swing.GroupLayout(card4);
        card4.setLayout(card4Layout);
        card4Layout.setHorizontalGroup(
            card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAdministratorPassword)
        );
        card4Layout.setVerticalGroup(
            card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAdministratorPassword)
        );

        subCardPassword.add(card4, "card3");

        card5.setBackground(new java.awt.Color(20, 20, 20));

        jTabbedPaneSuperUserPassword.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPaneSuperUserPassword.setFocusable(false);
        jTabbedPaneSuperUserPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPaneSuperUserPassword.setOpaque(true);

        jPanel21.setBackground(new java.awt.Color(0, 0, 0));

        tblSuperUserPassword.setBackground(new java.awt.Color(0, 0, 0));
        tblSuperUserPassword.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblSuperUserPassword.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " Id", "Username", "Password"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSuperUserPassword.setGridColor(new java.awt.Color(70, 70, 70));
        tblSuperUserPassword.setRequestFocusEnabled(false);
        tblSuperUserPassword.setRowHeight(25);
        tblSuperUserPassword.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblSuperUserPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSuperUserPasswordMouseClicked(evt);
            }
        });
        jScrollPane18.setViewportView(tblSuperUserPassword);

        btnChangeSelectedSuperUserPassword.setBackground(new java.awt.Color(40, 40, 40));
        btnChangeSelectedSuperUserPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnChangeSelectedSuperUserPassword.setForeground(new java.awt.Color(255, 255, 255));
        btnChangeSelectedSuperUserPassword.setText("Change Password");
        btnChangeSelectedSuperUserPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeSelectedSuperUserPasswordActionPerformed(evt);
            }
        });

        txtSearchSuperUserPassword.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchSuperUserPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchSuperUserPassword.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchSuperUserPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchSuperUserPasswordKeyReleased(evt);
            }
        });

        jLabel49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        btnSuperUserPasswords.setBackground(new java.awt.Color(40, 40, 40));
        btnSuperUserPasswords.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSuperUserPasswords.setForeground(new java.awt.Color(255, 255, 255));
        btnSuperUserPasswords.setText("Refresh");
        btnSuperUserPasswords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuperUserPasswordsActionPerformed(evt);
            }
        });

        cmbSortSuperUserPassword.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortSuperUserPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortSuperUserPassword.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id", "Username" }));
        cmbSortSuperUserPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortSuperUserPasswordActionPerformed(evt);
            }
        });

        lblSuperUserCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSuperUserCount.setForeground(new java.awt.Color(153, 153, 153));
        lblSuperUserCount.setText("Super User Count : 0");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(txtSearchSuperUserPassword)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortSuperUserPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel49))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addComponent(lblSuperUserCount, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSuperUserPasswords)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnChangeSelectedSuperUserPassword))
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 924, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel49)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbSortSuperUserPassword, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtSearchSuperUserPassword, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChangeSelectedSuperUserPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSuperUserPasswords, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSuperUserCount))
                .addGap(20, 20, 20))
        );

        jTabbedPaneSuperUserPassword.addTab("View Super User", jPanel21);

        jPanel22.setBackground(new java.awt.Color(0, 0, 0));

        lblSelectedEmployeePhoneNoValidation5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeePhoneNoValidation5.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedEmployeeSalaryValidation4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeeSalaryValidation4.setForeground(new java.awt.Color(255, 0, 51));

        btnGoBackToViewSuperUserPasswords.setBackground(new java.awt.Color(40, 40, 40));
        btnGoBackToViewSuperUserPasswords.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnGoBackToViewSuperUserPasswords.setForeground(new java.awt.Color(255, 255, 255));
        btnGoBackToViewSuperUserPasswords.setText("Go Back");
        btnGoBackToViewSuperUserPasswords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackToViewSuperUserPasswordsActionPerformed(evt);
            }
        });

        btnChangeSuperUserPassword.setBackground(new java.awt.Color(40, 40, 40));
        btnChangeSuperUserPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnChangeSuperUserPassword.setForeground(new java.awt.Color(255, 255, 255));
        btnChangeSuperUserPassword.setText("Change Password");
        btnChangeSuperUserPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeSuperUserPasswordActionPerformed(evt);
            }
        });

        txtSelectedSuperUserCurrentPassword.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedSuperUserCurrentPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedSuperUserCurrentPassword.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedSuperUserCurrentPassword.setEnabled(false);

        txtSelectedSuperUserUsername.setEditable(false);
        txtSelectedSuperUserUsername.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedSuperUserUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedSuperUserUsername.setEnabled(false);

        txtSelectedSuperUserId.setEditable(false);
        txtSelectedSuperUserId.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedSuperUserId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedSuperUserId.setEnabled(false);

        jLabel165.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel165.setForeground(new java.awt.Color(255, 255, 255));
        jLabel165.setText("Id");

        jLabel166.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel166.setForeground(new java.awt.Color(255, 255, 255));
        jLabel166.setText("Name");

        jLabel149.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel149.setForeground(new java.awt.Color(255, 255, 255));
        jLabel149.setText("Current Password");

        jLabel150.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel150.setForeground(new java.awt.Color(255, 255, 255));
        jLabel150.setText("Repeat Password");

        jLabel151.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel151.setForeground(new java.awt.Color(255, 255, 255));
        jLabel151.setText("New Password");

        pwdSelectedSuperUserNewPassword.setBackground(new java.awt.Color(18, 18, 18));
        pwdSelectedSuperUserNewPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pwdSelectedSuperUserNewPassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdSelectedSuperUserNewPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdSelectedSuperUserNewPasswordKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pwdSelectedSuperUserNewPasswordKeyTyped(evt);
            }
        });

        pwdSelectedRepeatSuperUserPassword.setBackground(new java.awt.Color(18, 18, 18));
        pwdSelectedRepeatSuperUserPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pwdSelectedRepeatSuperUserPassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdSelectedRepeatSuperUserPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdSelectedRepeatSuperUserPasswordKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel150, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel165, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel166, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel151, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel149)
                    .addComponent(btnChangeSuperUserPassword))
                .addGap(9, 9, 9)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGoBackToViewSuperUserPasswords)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSelectedSuperUserCurrentPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSelectedSuperUserUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSelectedSuperUserId, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pwdSelectedSuperUserNewPassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pwdSelectedRepeatSuperUserPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSelectedEmployeeSalaryValidation4, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
                            .addComponent(lblSelectedEmployeePhoneNoValidation5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSelectedSuperUserId)
                    .addComponent(jLabel165, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSelectedSuperUserUsername)
                    .addComponent(jLabel166, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel149, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSelectedSuperUserCurrentPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel151, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pwdSelectedSuperUserNewPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pwdSelectedRepeatSuperUserPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel150, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(lblSelectedEmployeePhoneNoValidation5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedEmployeeSalaryValidation4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnChangeSuperUserPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGoBackToViewSuperUserPasswords, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(151, Short.MAX_VALUE))
        );

        jTabbedPaneSuperUserPassword.addTab("Change Password", jPanel22);

        javax.swing.GroupLayout card5Layout = new javax.swing.GroupLayout(card5);
        card5.setLayout(card5Layout);
        card5Layout.setHorizontalGroup(
            card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneSuperUserPassword)
        );
        card5Layout.setVerticalGroup(
            card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneSuperUserPassword)
        );

        subCardPassword.add(card5, "card3");

        javax.swing.GroupLayout cardPasswordLayout = new javax.swing.GroupLayout(cardPassword);
        cardPassword.setLayout(cardPasswordLayout);
        cardPasswordLayout.setHorizontalGroup(
            cardPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(subCardPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlPasswordSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardPasswordLayout.setVerticalGroup(
            cardPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardPasswordLayout.createSequentialGroup()
                .addComponent(pnlPasswordSubSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subCardPassword, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mainCard.add(cardPassword, "card2");

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

        card6.setBackground(new java.awt.Color(0, 0, 0));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Backup Database");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(150, 150, 150));
        jLabel7.setText("Select Location");

        txtBackupLocation.setBackground(new java.awt.Color(18, 18, 18));
        txtBackupLocation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtBackupLocation.setForeground(new java.awt.Color(255, 255, 255));

        btnBrowseBackupPath.setBackground(new java.awt.Color(40, 40, 40));
        btnBrowseBackupPath.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBrowseBackupPath.setForeground(new java.awt.Color(255, 255, 255));
        btnBrowseBackupPath.setText("Browse Path");
        btnBrowseBackupPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseBackupPathActionPerformed(evt);
            }
        });

        btnBackup.setBackground(new java.awt.Color(40, 40, 40));
        btnBackup.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBackup.setForeground(new java.awt.Color(255, 255, 255));
        btnBackup.setText("Backup");
        btnBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackupActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout card6Layout = new javax.swing.GroupLayout(card6);
        card6.setLayout(card6Layout);
        card6Layout.setHorizontalGroup(
            card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card6Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(card6Layout.createSequentialGroup()
                        .addComponent(btnBrowseBackupPath)
                        .addGap(18, 18, 18)
                        .addComponent(btnBackup))
                    .addComponent(txtBackupLocation))
                .addContainerGap(491, Short.MAX_VALUE))
        );
        card6Layout.setVerticalGroup(
            card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card6Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(txtBackupLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBrowseBackupPath, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBackup, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(324, Short.MAX_VALUE))
        );

        subCardSettings.add(card6, "card2");

        card7.setBackground(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Restore Database");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(150, 150, 150));
        jLabel11.setText("Open Location");

        txtRestoreLocation.setBackground(new java.awt.Color(18, 18, 18));
        txtRestoreLocation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtRestoreLocation.setForeground(new java.awt.Color(255, 255, 255));

        btnBrowseRestorePath.setBackground(new java.awt.Color(40, 40, 40));
        btnBrowseRestorePath.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBrowseRestorePath.setForeground(new java.awt.Color(255, 255, 255));
        btnBrowseRestorePath.setText("Browse Path");
        btnBrowseRestorePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseRestorePathActionPerformed(evt);
            }
        });

        btnRestore.setBackground(new java.awt.Color(40, 40, 40));
        btnRestore.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRestore.setForeground(new java.awt.Color(255, 255, 255));
        btnRestore.setText("Restore");
        btnRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestoreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout card7Layout = new javax.swing.GroupLayout(card7);
        card7.setLayout(card7Layout);
        card7Layout.setHorizontalGroup(
            card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card7Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(card7Layout.createSequentialGroup()
                            .addComponent(btnBrowseRestorePath)
                            .addGap(18, 18, 18)
                            .addComponent(btnRestore)))
                    .addComponent(txtRestoreLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(491, Short.MAX_VALUE))
        );
        card7Layout.setVerticalGroup(
            card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card7Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(txtRestoreLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBrowseRestorePath, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRestore, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(324, Short.MAX_VALUE))
        );

        subCardSettings.add(card7, "card3");

        card8.setBackground(new java.awt.Color(0, 0, 0));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/logo.png"))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Developed by");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("0x5un5h1n3 ");

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/xeon_logo.png"))); // NOI18N

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Xeon Inventory");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(204, 204, 204));
        jLabel14.setText("Go beyond expectations");

        version.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        version.setForeground(new java.awt.Color(180, 180, 180));
        version.setText("© 0x5un5h1n3  | Xeon Inventory | Version 20.07");
        version.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel15.setBackground(new java.awt.Color(0, 0, 0));
        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(180, 180, 180));
        jLabel15.setText("Xeon is an Inventory Management software for growing business");

        jSeparator3.setForeground(new java.awt.Color(150, 150, 150));
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(120, 120, 120));
        jLabel18.setText("Increase your sales and keep track of every unit with our powerful stock management, order fulfillment, ");

        jLabel47.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(120, 120, 120));
        jLabel47.setText("user management and inventory control software ");

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(120, 120, 120));
        jLabel48.setText("Run a more efficient business :)");

        javax.swing.GroupLayout card8Layout = new javax.swing.GroupLayout(card8);
        card8.setLayout(card8Layout);
        card8Layout.setHorizontalGroup(
            card8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card8Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(card8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47)
                    .addComponent(jLabel18)
                    .addComponent(version)
                    .addGroup(card8Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(card8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(card8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(card8Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel15)
                    .addComponent(jLabel48))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        card8Layout.setVerticalGroup(
            card8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card8Layout.createSequentialGroup()
                .addGroup(card8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card8Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(card8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(card8Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(card8Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(card8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(card8Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(card8Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(49, 49, 49)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel48)
                .addGap(38, 38, 38)
                .addComponent(version)
                .addContainerGap(162, Short.MAX_VALUE))
        );

        subCardSettings.add(card8, "card4");

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
                    .addComponent(mainCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        javax.swing.GroupLayout pnlSuperUserDashboardLayout = new javax.swing.GroupLayout(pnlSuperUserDashboard);
        pnlSuperUserDashboard.setLayout(pnlSuperUserDashboardLayout);
        pnlSuperUserDashboardLayout.setHorizontalGroup(
            pnlSuperUserDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pnlTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlSuperUserDashboardLayout.createSequentialGroup()
                .addComponent(pnlSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlParent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSuperUserDashboardLayout.setVerticalGroup(
            pnlSuperUserDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSuperUserDashboardLayout.createSequentialGroup()
                .addComponent(pnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlSuperUserDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlParent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)))
        );

        pnlFrames.add(pnlSuperUserDashboard, "card2");

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
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 1224, Short.MAX_VALUE)
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

        pnlForceAccessLoaderBody.setBackground(new java.awt.Color(35, 35, 35));

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Force Accessing");
        jLabel37.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/loading.gif"))); // NOI18N
        jLabel38.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout pnlForceAccessLoaderBodyLayout = new javax.swing.GroupLayout(pnlForceAccessLoaderBody);
        pnlForceAccessLoaderBody.setLayout(pnlForceAccessLoaderBodyLayout);
        pnlForceAccessLoaderBodyLayout.setHorizontalGroup(
            pnlForceAccessLoaderBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
        );
        pnlForceAccessLoaderBodyLayout.setVerticalGroup(
            pnlForceAccessLoaderBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlForceAccessLoaderBodyLayout.createSequentialGroup()
                .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                .addGap(50, 50, 50)
                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlForceAccessLoaderLayout = new javax.swing.GroupLayout(pnlForceAccessLoader);
        pnlForceAccessLoader.setLayout(pnlForceAccessLoaderLayout);
        pnlForceAccessLoaderLayout.setHorizontalGroup(
            pnlForceAccessLoaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlForceAccessLoaderBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlForceAccessLoaderLayout.setVerticalGroup(
            pnlForceAccessLoaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlForceAccessLoaderBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlFrames.add(pnlForceAccessLoader, "card3");

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
            if (EmergencyDashboard.this.getExtendedState() == MAXIMIZED_BOTH) {
                EmergencyDashboard.this.setExtendedState(JFrame.NORMAL);
                maximizeView.setVisible(false);
                restoreDownView.setVisible(true);
            } else {
                EmergencyDashboard.this.setExtendedState(MAXIMIZED_BOTH);
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

        if (EmergencyDashboard.this.getExtendedState() != MAXIMIZED_BOTH) {
            this.setLocation(locX - mousepX, locY - mousepY);
            setOpacity((float) (0.97));
        }

    }//GEN-LAST:event_pnlTopMouseDragged

    private void homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMouseClicked
        setPnlColor(pnlHome);
        resetPnlColor(pnlLog);
        resetPnlColor(pnlAdmin);
        resetPnlColor(pnlStockReturn);
        resetPnlColor(pnlSales1);
        resetPnlColor(pnlSettings);

        setPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorLog);
        resetPnlIndicatorColor(pnlIndicatorAdmin);
        resetPnlIndicatorColor(pnlIndicatorStockReturn);
        resetPnlIndicatorColor(pnlIndicatorSales1);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Home");

        cardHome.setVisible(true);
        cardSystemLog.setVisible(false);
        cardAdmin.setVisible(false);
        cardStockReturn.setVisible(false);
        cardPassword.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_homeMouseClicked

    private void reportsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportsMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlLog);
        resetPnlColor(pnlAdmin);
        resetPnlColor(pnlStockReturn);
        resetPnlColor(pnlSales1);
        setPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorLog);
        resetPnlIndicatorColor(pnlIndicatorAdmin);
        resetPnlIndicatorColor(pnlIndicatorStockReturn);
        resetPnlIndicatorColor(pnlIndicatorSales1);
        setPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Settings");

        cardHome.setVisible(false);
        cardSystemLog.setVisible(false);
        cardAdmin.setVisible(false);
        cardStockReturn.setVisible(false);
        cardPassword.setVisible(false);
        cardSettings.setVisible(true);
    }//GEN-LAST:event_reportsMouseClicked

    private void lblLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLogoutMouseClicked
        int choose = JOptionPane.showConfirmDialog(this,
                "Do you want to Logout the application?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (choose == JOptionPane.YES_OPTION) {

            String logoutActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username= '" + lblSuperUserUsername.getText() + "'),'Super User','" + lblSuperUserUsername.getText() + "','Super User Logout', '" + lblSuperUserUsername.getText() + " logged out' , 'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            pnlLogoutLoader.setVisible(true);
            pnlSuperUserDashboard.setVisible(false);

            new java.util.Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    for (float i = 0.985f; i > 0; i -= 0.000001) {
                        EmergencyDashboard.this.setOpacity(i);

                    }
                    EmergencyDashboard.this.dispose();

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

    private void pnlHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlHomeMouseClicked
        setPnlColor(pnlHome);
        resetPnlColor(pnlLog);
        resetPnlColor(pnlAdmin);
        resetPnlColor(pnlStockReturn);
        resetPnlColor(pnlSales1);
        resetPnlColor(pnlSettings);

        setPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorLog);
        resetPnlIndicatorColor(pnlIndicatorAdmin);
        resetPnlIndicatorColor(pnlIndicatorStockReturn);
        resetPnlIndicatorColor(pnlIndicatorSales1);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Home");

        cardHome.setVisible(true);
        cardSystemLog.setVisible(false);
        cardAdmin.setVisible(false);
        cardStockReturn.setVisible(false);
        cardPassword.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_pnlHomeMouseClicked

    private void pnlSettingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlSettingsMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlLog);
        resetPnlColor(pnlAdmin);
        resetPnlColor(pnlStockReturn);
        resetPnlColor(pnlSales1);
        setPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorLog);
        resetPnlIndicatorColor(pnlIndicatorAdmin);
        resetPnlIndicatorColor(pnlIndicatorStockReturn);
        resetPnlIndicatorColor(pnlIndicatorSales1);
        setPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Settings");

        cardHome.setVisible(false);
        cardSystemLog.setVisible(false);
        cardAdmin.setVisible(false);
        cardStockReturn.setVisible(false);
        cardPassword.setVisible(false);
        cardSettings.setVisible(true);
    }//GEN-LAST:event_pnlSettingsMouseClicked

    private void minimize1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimize1MouseClicked
        EmergencyDashboard.this.setState(JFrame.ICONIFIED);
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
            EmergencyDashboard.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            maximizeView.setVisible(true);
            restoreDownView.setVisible(false);
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            EmergencyDashboard.this.setMaximizedBounds(env.getMaximumWindowBounds());
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

            String logoutActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username= '" + lblSuperUserUsername.getText() + "'),'Super User','" + lblSuperUserUsername.getText() + "','Super User System Exit', '" + lblSuperUserUsername.getText() + " system exited' , 'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            for (float i = 1f; i > 0; i -= 0.000001) {
                EmergencyDashboard.this.setOpacity(i);
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
        EmergencyDashboard.this.setState(JFrame.ICONIFIED);
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
            EmergencyDashboard.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            maximizeView.setVisible(true);
            restoreDownView.setVisible(false);
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            EmergencyDashboard.this.setMaximizedBounds(env.getMaximumWindowBounds());
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

            String logoutActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username= '" + lblSuperUserUsername.getText() + "'),'Super User','" + lblSuperUserUsername.getText() + "','Super User System Exit', '" + lblSuperUserUsername.getText() + " system exited' , 'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            for (float i = 1f; i > 0; i -= 0.000001) {
                EmergencyDashboard.this.setOpacity(i);
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

    private void lblSuperUserUsernameMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSuperUserUsernameMouseEntered
        lblSuperUserUsername.setBackground(new Color(35, 35, 35));
    }//GEN-LAST:event_lblSuperUserUsernameMouseEntered

    private void lblSuperUserUsernameMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblSuperUserUsernameMouseExited
        lblSuperUserUsername.setBackground(new Color(0, 0, 0));
    }//GEN-LAST:event_lblSuperUserUsernameMouseExited


    private void pnlTopMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTopMouseReleased
        setOpacity((float) (0.985));
    }//GEN-LAST:event_pnlTopMouseReleased

    private void btnRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestoreActionPerformed
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

            } else {
                JOptionPane.showMessageDialog(this, "Restore Failed!", "Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (HeadlessException | IOException | InterruptedException e) {
        }
        card7.setVisible(false);
        card7.setVisible(true);
    }//GEN-LAST:event_btnRestoreActionPerformed

    private void btnBrowseRestorePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseRestorePathActionPerformed
        JFileChooser fcrestore = new JFileChooser();
        fcrestore.showOpenDialog(this);
        try {
            File f = fcrestore.getSelectedFile();
            path = f.getAbsolutePath();
            path = path.replace('\\', '/');
            txtRestoreLocation.setText(path);
        } catch (Exception e) {
        }
        card7.setVisible(false);
        card7.setVisible(true);
    }//GEN-LAST:event_btnBrowseRestorePathActionPerformed

    private void btnBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackupActionPerformed
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
                card6.setVisible(false);
                card6.setVisible(true);

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

                        String createBackupActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblSuperUserUsername.getText() + "'),'Super User','" + lblSuperUserUsername.getText() + "','Create Backup','Super User created new backup on file location : " + txtBackupLocation.getText() + "','SUCCESS')";
                        DB.DB.putData(createBackupActivityLog);

                    } else {
                        JOptionPane.showMessageDialog(this, "Backup Creation Failed!", "Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                }
            }
        }

        card6.setVisible(false);
        card6.setVisible(true);

    }//GEN-LAST:event_btnBackupActionPerformed

    private void btnBrowseBackupPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseBackupPathActionPerformed
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

        card6.setVisible(false);
        card6.setVisible(true);
    }//GEN-LAST:event_btnBrowseBackupPathActionPerformed

    private void jLabel42MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel42MouseClicked
        card6.setVisible(false);
        card7.setVisible(false);
        card8.setVisible(true);

        resetLblColor(jLabel17);
        resetLblColor(jLabel34);
        setLblColor(jLabel42);
    }//GEN-LAST:event_jLabel42MouseClicked

    private void jLabel34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseClicked
        card6.setVisible(false);
        card7.setVisible(true);
        card8.setVisible(false);

        resetLblColor(jLabel17);
        setLblColor(jLabel34);
        resetLblColor(jLabel42);
    }//GEN-LAST:event_jLabel34MouseClicked

    private void jLabel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseClicked
        card6.setVisible(true);
        card7.setVisible(false);
        card8.setVisible(false);

        setLblColor(jLabel17);
        resetLblColor(jLabel34);
        resetLblColor(jLabel42);
    }//GEN-LAST:event_jLabel17MouseClicked

    private void btn_addUsers1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_addUsers1MouseClicked

    }//GEN-LAST:event_btn_addUsers1MouseClicked

    private void txtSelectedAdminIdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedAdminIdKeyTyped
        lblSelectedEmployeeIdValidation.setText("Employee Id can't be changed!");
    }//GEN-LAST:event_txtSelectedAdminIdKeyTyped

    private void txtSelectedAdminIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedAdminIdKeyPressed
        if (evt.getKeyCode() == 10) {
            txtSelectedAdminUsername.grabFocus();
            lblSelectedEmployeeIdValidation.setText(null);
            lblSelectedEmployeeUsernameValidation.setText(null);
        }
    }//GEN-LAST:event_txtSelectedAdminIdKeyPressed

    private void txtSelectedAdminUsernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedAdminUsernameKeyTyped
        lblSelectedEmployeeUsernameValidation.setText("Employee Username can't be changed!");

    }//GEN-LAST:event_txtSelectedAdminUsernameKeyTyped

    private void txtSelectedAdminUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedAdminUsernameKeyPressed
        if (evt.getKeyCode() == 10) {
            lblSelectedEmployeeIdValidation.setText(null);
            lblSelectedEmployeeUsernameValidation.setText(null);
        }
    }//GEN-LAST:event_txtSelectedAdminUsernameKeyPressed

    private void txtSelectedAdminNICKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedAdminNICKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(evt.getKeyChar()))) {
            evt.consume();

        }
        String nic = txtSelectedAdminNIC.getText();
        int length = nic.length();
        if (length >= 12) {
            evt.consume();
        }

        if (length < 8) {
            lblSelectedEmployeeNICValidation.setText("Please Input a Valid NIC!");
            txtSelectedAdminNIC.setBorder(new LineBorder(Color.red));
            txtSelectedAdminNIC.setMinimumSize(new Dimension(64, 31));

        } else {
            lblSelectedEmployeeNICValidation.setText(null);
            txtSelectedAdminNIC.setBorder(new FlatBorder());

        }
    }//GEN-LAST:event_txtSelectedAdminNICKeyTyped

    private void txtSelectedAdminNICKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedAdminNICKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSelectedAdminNIC.getText().isEmpty()) {
                lblSelectedEmployeeNICValidation.setText("Please Fill this Field!");
                txtSelectedAdminNIC.setBorder(new LineBorder(Color.red));
                txtSelectedAdminNIC.setMinimumSize(new Dimension(64, 31));

            } else {
                lblSelectedEmployeeNICValidation.setText(null);
                txtSelectedAdminNIC.setBorder(new FlatBorder());
            }
            txtSelectedAdminPhoneNo.grabFocus();
        }
    }//GEN-LAST:event_txtSelectedAdminNICKeyPressed

    private void txtSelectedAdminPhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedAdminPhoneNoKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String phoneNo = txtSelectedAdminPhoneNo.getText();
        int length = phoneNo.length();
        if (length >= 10) {
            evt.consume();
        }

        if (length < 10) {
            lblSelectedEmployeePhoneNoValidation.setText("Please Input a Valid Phone Number!");
            txtSelectedAdminPhoneNo.setBorder(new LineBorder(Color.red));
            txtSelectedAdminPhoneNo.setMinimumSize(new Dimension(64, 31));
        } else {
            lblSelectedEmployeePhoneNoValidation.setText(null);
            txtSelectedAdminPhoneNo.setBorder(new FlatBorder());

        }
    }//GEN-LAST:event_txtSelectedAdminPhoneNoKeyTyped

    private void txtSelectedAdminPhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedAdminPhoneNoKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSelectedAdminPhoneNo.getText().isEmpty()) {
                lblSelectedEmployeePhoneNoValidation.setText("Please Fill this Field!");
                txtSelectedAdminPhoneNo.setBorder(new LineBorder(Color.red));
                txtSelectedAdminPhoneNo.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSelectedEmployeePhoneNoValidation.setText(null);
                lblSelectedEmployeeIdValidation.setText(null);
                lblSelectedEmployeeUsernameValidation.setText(null);
                txtSelectedAdminPhoneNo.setBorder(new FlatBorder());
            }
        }
    }//GEN-LAST:event_txtSelectedAdminPhoneNoKeyPressed

    private void btnPrintSelectedAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintSelectedAdminActionPerformed
        printSelectedAdministrator();
        jPanel11.setVisible(false);
        jPanel11.setVisible(true);

    }//GEN-LAST:event_btnPrintSelectedAdminActionPerformed

    private void btnDeleteSelectedAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSelectedAdminActionPerformed
        if (tblAdmin.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Admin Selected!");
            jTabbedPaneAdministrators.setSelectedIndex(1);

        } else {

            deleteAdministrator();
            jPanel11.setVisible(false);
            jPanel11.setVisible(true);
        }
    }//GEN-LAST:event_btnDeleteSelectedAdminActionPerformed

    private void btnUpdateSelectedAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateSelectedAdminActionPerformed

        if (tblAdmin.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Admin Selected!");
            jTabbedPaneAdministrators.setSelectedIndex(1);

        } else {
            updateAdministrator();

            jPanel11.setVisible(false);
            jPanel11.setVisible(true);
        }
    }//GEN-LAST:event_btnUpdateSelectedAdminActionPerformed

    private void btnGoBackToViewAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackToViewAdminActionPerformed
        jTabbedPaneAdministrators.setSelectedIndex(1);
    }//GEN-LAST:event_btnGoBackToViewAdminActionPerformed

    private void cmbSortAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortAdminActionPerformed
        searchAdministrator();
    }//GEN-LAST:event_cmbSortAdminActionPerformed

    private void btnAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdminActionPerformed

        txtSearchAdmin.setText(null);
        txtSelectedAdminId.setText(null);
        txtSelectedAdminUsername.setText(null);
        txtSelectedAdminNIC.setText(null);
        txtSelectedAdminPhoneNo.setText(null);
        refreshAdministratorTable();
        cmbSortAdmin.setSelectedIndex(0);
        jPanel10.setVisible(false);
        jPanel10.setVisible(true);
    }//GEN-LAST:event_btnAdminActionPerformed

    private void txtSearchAdminKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchAdminKeyReleased
        searchAdministrator();
    }//GEN-LAST:event_txtSearchAdminKeyReleased

    private void btnPrintAdminsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintAdminsActionPerformed
        printAllAdmins();
        jPanel10.setVisible(false);
        jPanel10.setVisible(true);
    }//GEN-LAST:event_btnPrintAdminsActionPerformed

    private void btnDeleteAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteAdminActionPerformed

        if (tblAdmin.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Admin Selected!");
            jPanel10.setVisible(false);
            jPanel10.setVisible(true);

        } else {

            deleteAdministrator();
            jPanel10.setVisible(false);
            jPanel10.setVisible(true);
        }
    }//GEN-LAST:event_btnDeleteAdminActionPerformed

    private void btnEditAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditAdminActionPerformed
        if (tblAdmin.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Admin Selected!");
            jPanel10.setVisible(false);
            jPanel10.setVisible(true);

        } else {
            jTabbedPaneAdministrators.setSelectedIndex(2);
        }
    }//GEN-LAST:event_btnEditAdminActionPerformed

    private void tblAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAdminMouseClicked
        txtSelectedAdminId.setText((String) tblAdmin.getValueAt(tblAdmin.getSelectedRow(), 0));
        txtSelectedAdminUsername.setText((String) tblAdmin.getValueAt(tblAdmin.getSelectedRow(), 1));
        txtSelectedAdminNIC.setText((String) tblAdmin.getValueAt(tblAdmin.getSelectedRow(), 3));
        txtSelectedAdminPhoneNo.setText((String) tblAdmin.getValueAt(tblAdmin.getSelectedRow(), 4));

        lblSelectedEmployeeIdValidation.setText(null);
        lblSelectedEmployeeUsernameValidation.setText(null);
        lblSelectedEmployeePhoneNoValidation.setText(null);

        txtSelectedAdminId.setBorder(new FlatBorder());
        txtSelectedAdminUsername.setBorder(new FlatBorder());
        txtSelectedAdminPhoneNo.setBorder(new FlatBorder());
    }//GEN-LAST:event_tblAdminMouseClicked

    private void txtAdminUsernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdminUsernameKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c)) || (Character.isLetter(c)) || (Character.isISOControl(c)))) {
            evt.consume();
        }

        try {
            ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE username like '" + txtAdminUsername.getText() + "' ");

            if (rs.next()) {
                lblEmployeeUserameValidation.setText("Username Already Exists!");
                txtAdminUsername.setBorder(new LineBorder(Color.red));
                txtAdminUsername.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeeUserameValidation.setText(null);
                txtAdminUsername.setBorder(new FlatBorder());
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_txtAdminUsernameKeyTyped

    private void txtAdminUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdminUsernameKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtAdminUsername.getText().isEmpty()) {
                lblEmployeeUserameValidation.setText("Please Fill this Field!");
                txtAdminUsername.setBorder(new LineBorder(Color.red));
                txtAdminUsername.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeeUserameValidation.setText(null);
                txtAdminUsername.setBorder(new FlatBorder());
            }
            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE username like '" + txtAdminUsername.getText() + "' ");

                if (rs.next()) {
                    lblEmployeeUserameValidation.setText("Username Already Exists!");
                    txtAdminUsername.setBorder(new LineBorder(Color.red));
                    txtAdminUsername.setMinimumSize(new Dimension(64, 31));

                } else {
                    lblEmployeeUserameValidation.setText(null);
                    txtAdminUsername.setBorder(new FlatBorder());
                }

            } catch (Exception e) {
            }
            txtAdminNIC.grabFocus();

        }
    }//GEN-LAST:event_txtAdminUsernameKeyPressed

    private void txtAdminNICKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdminNICKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String nic = txtAdminNIC.getText();
        int length = nic.length();
        if (length >= 12) {
            evt.consume();
        }

        if (length < 8) {
            lblEmployeeNICValidation.setText("Please Input a Valid NIC!");
            txtAdminNIC.setBorder(new LineBorder(Color.red));
            txtAdminNIC.setMinimumSize(new Dimension(64, 31));

        } else {
            lblEmployeeNICValidation.setText(null);
            txtAdminNIC.setBorder(new FlatBorder());

        }
    }//GEN-LAST:event_txtAdminNICKeyTyped

    private void txtAdminNICKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdminNICKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtAdminNIC.getText().isEmpty()) {
                lblEmployeeNICValidation.setText("Please Fill this Field!");
                txtAdminNIC.setBorder(new LineBorder(Color.red));
                txtAdminNIC.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeeNICValidation.setText(null);
                txtAdminNIC.setBorder(new FlatBorder());
            }
            if (txtAdminUsername.getText().isEmpty()) {
                lblEmployeeUserameValidation.setText("Please Fill this Field!");
                txtAdminUsername.setBorder(new LineBorder(Color.red));
                txtAdminUsername.setMinimumSize(new Dimension(64, 31));
            } else {
                lblEmployeeUserameValidation.setText(null);
                txtAdminUsername.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE username like '" + txtAdminUsername.getText() + "'");

                if (rs.next()) {
                    lblEmployeeUserameValidation.setText("Username Already Exists!");
                    txtAdminUsername.setBorder(new LineBorder(Color.red));
                    txtAdminUsername.setMinimumSize(new Dimension(64, 31));
                } else {
                    lblEmployeeUserameValidation.setText(null);
                    txtAdminUsername.setBorder(new FlatBorder());
                }
            } catch (Exception e) {
            }
            txtAdminPhoneNo.grabFocus();
        }
    }//GEN-LAST:event_txtAdminNICKeyPressed

    private void txtAdminPhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdminPhoneNoKeyTyped

        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String phoneNo = txtAdminPhoneNo.getText();
        int length = phoneNo.length();
        if (length >= 10) {
            evt.consume();
        }

        if (length < 9) {
            lblEmployeePhoneNoValidation.setText("Please Input a Valid Phone Number!");
            txtAdminPhoneNo.setBorder(new LineBorder(Color.red));
            txtAdminPhoneNo.setMinimumSize(new Dimension(64, 31));

        } else {
            lblEmployeePhoneNoValidation.setText(null);
            txtAdminPhoneNo.setBorder(new FlatBorder());

        }
    }//GEN-LAST:event_txtAdminPhoneNoKeyTyped

    private void txtAdminPhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAdminPhoneNoKeyPressed
        if (evt.getKeyCode() == 10) {
            String phoneNo = txtAdminPhoneNo.getText();
            int length = phoneNo.length();

            if (phoneNo.isEmpty() || (length < 10)) {
                lblEmployeePhoneNoValidation.setText("Please Fill this Field!");
                txtAdminPhoneNo.setBorder(new LineBorder(Color.red));
                txtAdminPhoneNo.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeePhoneNoValidation.setText(null);
                txtAdminPhoneNo.setBorder(new FlatBorder());
            }

            if (txtAdminUsername.getText().isEmpty()) {
                lblEmployeeUserameValidation.setText("Please Fill this Field!");
                txtAdminUsername.setBorder(new LineBorder(Color.red));
                txtAdminUsername.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeeUserameValidation.setText(null);
                txtAdminUsername.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE username like '" + txtAdminUsername.getText() + "'");

                if (rs.next()) {
                    lblEmployeeUserameValidation.setText("Username Already Exists!");
                    txtAdminUsername.setBorder(new LineBorder(Color.red));
                    txtAdminUsername.setMinimumSize(new Dimension(64, 31));

                } else {
                    lblEmployeeUserameValidation.setText(null);
                    txtAdminUsername.setBorder(new FlatBorder());
                }

            } catch (Exception e) {
            }

            pwdAdminPassword.grabFocus();
        }
    }//GEN-LAST:event_txtAdminPhoneNoKeyPressed

    private void pwdAdminPasswordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdAdminPasswordKeyTyped
        String password = (String.valueOf(pwdAdminPassword.getPassword()));
        int length = password.length();

        if (length < 3) {
            lblEmployeePasswordValidation.setText("Password is Too Short!");
            pwdAdminPassword.setBorder(new LineBorder(Color.red));
            pwdAdminPassword.setMinimumSize(new Dimension(64, 31));

        } else {
            lblEmployeePasswordValidation.setText(null);
            pwdAdminPassword.setBorder(new FlatBorder());
            pwdRepeatAdminPassword.setBorder(new FlatBorder());

        }
    }//GEN-LAST:event_pwdAdminPasswordKeyTyped

    private void pwdAdminPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdAdminPasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            if (String.valueOf(pwdAdminPassword.getPassword()).isEmpty()) {
                lblEmployeePasswordValidation.setText("Please Fill this Field!");
                pwdAdminPassword.setBorder(new LineBorder(Color.red));
                pwdAdminPassword.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeePasswordValidation.setText(null);
                pwdAdminPassword.setBorder(new FlatBorder());
            }

            if (txtAdminUsername.getText().isEmpty()) {
                lblEmployeeUserameValidation.setText("Please Fill this Field!");
                txtAdminUsername.setBorder(new LineBorder(Color.red));
                txtAdminUsername.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeeUserameValidation.setText(null);
                txtAdminUsername.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE username like '" + txtAdminUsername.getText() + "'");

                if (rs.next()) {
                    lblEmployeeUserameValidation.setText("Username Already Exists!");
                    txtAdminUsername.setBorder(new LineBorder(Color.red));
                    txtAdminUsername.setMinimumSize(new Dimension(64, 31));

                } else {
                    lblEmployeeUserameValidation.setText(null);
                    txtAdminUsername.setBorder(new FlatBorder());
                }

            } catch (Exception e) {
            }
            pwdRepeatAdminPassword.grabFocus();
        }
    }//GEN-LAST:event_pwdAdminPasswordKeyPressed

    private void btnAddNewAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewAdminActionPerformed
        addNewAdministrator();
        jPanel5.setVisible(false);
        jPanel5.setVisible(true);
    }//GEN-LAST:event_btnAddNewAdminActionPerformed

    private void pwdRepeatAdminPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdRepeatAdminPasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            if (String.valueOf(pwdRepeatAdminPassword.getPassword()).isEmpty()) {
                lblEmployeeRepeatPasswordValidation.setText("Please Fill this Field!");
                pwdRepeatAdminPassword.setBorder(new LineBorder(Color.red));
                pwdRepeatAdminPassword.setMinimumSize(new Dimension(64, 31));
                pwdAdminPassword.grabFocus();

            } else {
                lblEmployeeRepeatPasswordValidation.setText(null);
                pwdRepeatAdminPassword.setBorder(new FlatBorder());
            }

            if (txtAdminUsername.getText().isEmpty()) {
                lblEmployeeUserameValidation.setText("Please Fill this Field!");
                txtAdminUsername.setBorder(new LineBorder(Color.red));
                txtAdminUsername.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeeUserameValidation.setText(null);
                txtAdminUsername.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE username like '" + txtAdminUsername.getText() + "'");

                if (rs.next()) {
                    lblEmployeeUserameValidation.setText("Username Already Exists!");
                    txtAdminUsername.setBorder(new LineBorder(Color.red));
                    txtAdminUsername.setMinimumSize(new Dimension(64, 31));

                } else {
                    lblEmployeeUserameValidation.setText(null);
                    txtAdminUsername.setBorder(new FlatBorder());
                }

            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_pwdRepeatAdminPasswordKeyPressed

    private void pwdRepeatAdminPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwdRepeatAdminPasswordActionPerformed
        addNewAdministrator();
        jPanel5.setVisible(false);
        jPanel5.setVisible(true);
    }//GEN-LAST:event_pwdRepeatAdminPasswordActionPerformed

    private void pnlAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlAdminMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlLog);
        setPnlColor(pnlAdmin);
        resetPnlColor(pnlStockReturn);
        resetPnlColor(pnlSales1);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorLog);
        setPnlIndicatorColor(pnlIndicatorAdmin);
        resetPnlIndicatorColor(pnlIndicatorStockReturn);
        resetPnlIndicatorColor(pnlIndicatorSales1);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Administrators");

        cardHome.setVisible(false);
        cardSystemLog.setVisible(false);
        cardAdmin.setVisible(true);
        cardStockReturn.setVisible(false);
        cardPassword.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_pnlAdminMouseClicked

    private void pnlLogMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLogMouseClicked
        resetPnlColor(pnlHome);
        setPnlColor(pnlLog);
        resetPnlColor(pnlAdmin);
        resetPnlColor(pnlStockReturn);
        resetPnlColor(pnlSales1);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        setPnlIndicatorColor(pnlIndicatorLog);
        resetPnlIndicatorColor(pnlIndicatorAdmin);
        resetPnlIndicatorColor(pnlIndicatorStockReturn);
        resetPnlIndicatorColor(pnlIndicatorSales1);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("System Logs");

        cardHome.setVisible(false);
        cardSystemLog.setVisible(true);
        cardAdmin.setVisible(false);
        cardStockReturn.setVisible(false);
        cardPassword.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_pnlLogMouseClicked

    private void adminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adminMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlLog);
        setPnlColor(pnlAdmin);
        resetPnlColor(pnlStockReturn);
        resetPnlColor(pnlSales1);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorLog);
        setPnlIndicatorColor(pnlIndicatorAdmin);
        resetPnlIndicatorColor(pnlIndicatorStockReturn);
        resetPnlIndicatorColor(pnlIndicatorSales1);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Administrators");

        cardHome.setVisible(false);
        cardSystemLog.setVisible(false);
        cardAdmin.setVisible(true);
        cardStockReturn.setVisible(false);
        cardPassword.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_adminMouseClicked

    private void logsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logsMouseClicked
        resetPnlColor(pnlHome);
        setPnlColor(pnlLog);
        resetPnlColor(pnlAdmin);
        resetPnlColor(pnlStockReturn);
        resetPnlColor(pnlSales1);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        setPnlIndicatorColor(pnlIndicatorLog);
        resetPnlIndicatorColor(pnlIndicatorAdmin);
        resetPnlIndicatorColor(pnlIndicatorStockReturn);
        resetPnlIndicatorColor(pnlIndicatorSales1);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("System Logs");

        cardHome.setVisible(false);
        cardSystemLog.setVisible(true);
        cardAdmin.setVisible(false);
        cardStockReturn.setVisible(false);
        cardPassword.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_logsMouseClicked

    private void btnRefreshLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshLogsActionPerformed
        refreshSystemLogsTable();
        subCardSystemLog.setVisible(false);
        subCardSystemLog.setVisible(true);
    }//GEN-LAST:event_btnRefreshLogsActionPerformed

    private void btnPrintLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintLogsActionPerformed
        printLogs();
        subCardSystemLog.setVisible(false);
        subCardSystemLog.setVisible(true);
    }//GEN-LAST:event_btnPrintLogsActionPerformed

    private void txtSearchSystemLogsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSystemLogsKeyReleased
        searchSystemLogs();
    }//GEN-LAST:event_txtSearchSystemLogsKeyReleased

    private void cmbSortSystemLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortSystemLogsActionPerformed
        searchSystemLogs();
    }//GEN-LAST:event_cmbSortSystemLogsActionPerformed

    private void btnEditEmployee1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditEmployee1ActionPerformed
        int choose = JOptionPane.showConfirmDialog(this,
                "Do you want to Force Access Admin Login?",
                "Confirm Force Access", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (choose == JOptionPane.YES_OPTION) {

            String logoutActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username= '" + lblSuperUserUsername.getText() + "'),'Super User','" + lblSuperUserUsername.getText() + "','Force Access Admin Login', '" + lblSuperUserUsername.getText() + " force accessed Admin Login' , 'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            pnlForceAccessLoader.setVisible(true);
            pnlSuperUserDashboard.setVisible(false);

            new java.util.Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    for (float i = 0.985f; i > 0; i -= 0.000001) {
                        EmergencyDashboard.this.setOpacity(i);

                    }
                    EmergencyDashboard.this.dispose();

                    AdminDashboard a = new AdminDashboard();
                    AdminDashboard.lblAdminUsername.setText(lblSuperUserUsername.getText());
                    a.setVisible(true);
                    for (float j = 0f; j < 0.985; j += 0.000001) {
                        a.setOpacity(j);
                    }

                }
            }, 1000 * 1);

        } else {

        }
        subCardHome.setVisible(false);
        subCardHome.setVisible(true);
    }//GEN-LAST:event_btnEditEmployee1ActionPerformed

    private void btnEditEmployee2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditEmployee2ActionPerformed
        int choose = JOptionPane.showConfirmDialog(this,
                "Do you want to Force Access Employee Login?",
                "Confirm Force Access", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (choose == JOptionPane.YES_OPTION) {

            String logoutActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username= '" + lblSuperUserUsername.getText() + "'),'Super User','" + lblSuperUserUsername.getText() + "','Force Access Employee Login', '" + lblSuperUserUsername.getText() + " force accessed Employee Login' , 'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            pnlForceAccessLoader.setVisible(true);
            pnlSuperUserDashboard.setVisible(false);

            new java.util.Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    for (float i = 0.985f; i > 0; i -= 0.000001) {
                        EmergencyDashboard.this.setOpacity(i);

                    }
                    EmergencyDashboard.this.dispose();

                    EmployeeDashboard e = new EmployeeDashboard();
                    EmployeeDashboard.lblEmployeeUsername.setText(lblSuperUserUsername.getText());
                    e.setVisible(true);

                    for (float j = 0f; j < 0.985; j += 0.000001) {
                        e.setOpacity(j);
                    }
                }
            }, 1000 * 1);

        } else {

        }
        subCardHome.setVisible(false);
        subCardHome.setVisible(true);
    }//GEN-LAST:event_btnEditEmployee2ActionPerformed

    private void stock1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stock1MouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlLog);
        resetPnlColor(pnlAdmin);
        resetPnlColor(pnlStockReturn);
        setPnlColor(pnlSales1);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorLog);
        resetPnlIndicatorColor(pnlIndicatorAdmin);
        resetPnlIndicatorColor(pnlIndicatorStockReturn);
        setPnlIndicatorColor(pnlIndicatorSales1);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Passwords");

        cardHome.setVisible(false);
        cardSystemLog.setVisible(false);
        cardAdmin.setVisible(false);
        cardStockReturn.setVisible(false);
        cardPassword.setVisible(true);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_stock1MouseClicked

    private void pnlSales1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlSales1MouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlLog);
        resetPnlColor(pnlAdmin);
        resetPnlColor(pnlStockReturn);
        setPnlColor(pnlSales1);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorLog);
        resetPnlIndicatorColor(pnlIndicatorAdmin);
        resetPnlIndicatorColor(pnlIndicatorStockReturn);
        setPnlIndicatorColor(pnlIndicatorSales1);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Passwords");

        cardHome.setVisible(false);
        cardSystemLog.setVisible(false);
        cardAdmin.setVisible(false);
        cardStockReturn.setVisible(false);
        cardPassword.setVisible(true);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_pnlSales1MouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        card3.setVisible(true);
        card4.setVisible(false);
        card5.setVisible(false);

        setLblColor(jLabel8);
        resetLblColor(jLabel23);
        resetLblColor(jLabel31);
        txtAdminUsername.grabFocus();
    }//GEN-LAST:event_jLabel8MouseClicked

    private void jLabel23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel23MouseClicked
        card3.setVisible(false);
        card4.setVisible(true);
        card5.setVisible(false);

        resetLblColor(jLabel8);
        setLblColor(jLabel23);
        resetLblColor(jLabel31);
    }//GEN-LAST:event_jLabel23MouseClicked

    private void jLabel31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseClicked
        card3.setVisible(false);
        card4.setVisible(false);
        card5.setVisible(true);

        resetLblColor(jLabel8);
        resetLblColor(jLabel23);
        setLblColor(jLabel31);
    }//GEN-LAST:event_jLabel31MouseClicked

    private void tblEmployeePasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmployeePasswordMouseClicked
        txtSelectedEmployeeId.setText((String) tblEmployeePassword.getValueAt(tblEmployeePassword.getSelectedRow(), 0));
        txtSelectedEmployeeUsername.setText((String) tblEmployeePassword.getValueAt(tblEmployeePassword.getSelectedRow(), 1));
        txtSelectedEmployeeCurrentPassword.setText((String) tblEmployeePassword.getValueAt(tblEmployeePassword.getSelectedRow(), 2));

        lblSelectedEmployeePhoneNoValidation1.setText(null);
        lblSelectedEmployeeSalaryValidation.setText(null);

        pwdSelectedEmployeeNewPassword.setBorder(new FlatBorder());
        pwdSelectedRepeatEmployeePassword.setBorder(new FlatBorder());
    }//GEN-LAST:event_tblEmployeePasswordMouseClicked

    private void btnChangeSelectedEmployeePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeSelectedEmployeePasswordActionPerformed
        if (tblEmployeePassword.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Employee Selected!");
            jPanel13.setVisible(false);
            jPanel13.setVisible(true);

        } else {
            jTabbedPaneEmployeePassword.setSelectedIndex(1);
        }
    }//GEN-LAST:event_btnChangeSelectedEmployeePasswordActionPerformed

    private void txtSearchEmployeePasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchEmployeePasswordKeyReleased
        searchEmployeePassword();
    }//GEN-LAST:event_txtSearchEmployeePasswordKeyReleased

    private void btnRefreshEmployeePasswordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshEmployeePasswordsActionPerformed

        txtSearchEmployeePassword.setText(null);
        txtSelectedEmployeeId.setText(null);
        txtSelectedEmployeeUsername.setText(null);
        txtSelectedEmployeeCurrentPassword.setText(null);
        txtSelectedEmployeeCurrentPassword.setText(null);
        pwdSelectedEmployeeNewPassword.setText(null);
        pwdSelectedRepeatEmployeePassword.setText(null);
        refreshEmployeePasswordTable();
        cmbSortEmployeePassword.setSelectedIndex(0);
        jPanel13.setVisible(false);
        jPanel13.setVisible(true);
    }//GEN-LAST:event_btnRefreshEmployeePasswordsActionPerformed

    private void cmbSortEmployeePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortEmployeePasswordActionPerformed
        searchEmployeePassword();
    }//GEN-LAST:event_cmbSortEmployeePasswordActionPerformed

    private void btnGoBackToViewEmployeePasswordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackToViewEmployeePasswordsActionPerformed
        jTabbedPaneEmployeePassword.setSelectedIndex(0);
    }//GEN-LAST:event_btnGoBackToViewEmployeePasswordsActionPerformed

    private void btnChangeEmployeePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeEmployeePasswordActionPerformed

        if (tblEmployeePassword.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Employee Selected!");
            jTabbedPaneEmployeePassword.setSelectedIndex(0);

        } else {
            changeEmployeePassword();

            jPanel14.setVisible(false);
            jPanel14.setVisible(true);
        }
    }//GEN-LAST:event_btnChangeEmployeePasswordActionPerformed

    private void pwdSelectedEmployeeNewPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdSelectedEmployeeNewPasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            if (String.valueOf(pwdSelectedEmployeeNewPassword.getPassword()).isEmpty()) {
                lblSelectedEmployeePhoneNoValidation1.setText("Please Fill this Field!");
                pwdSelectedEmployeeNewPassword.setBorder(new LineBorder(Color.red));
                pwdSelectedEmployeeNewPassword.setMinimumSize(new Dimension(64, 31));

            } else {
                lblSelectedEmployeePhoneNoValidation1.setText(null);
                pwdSelectedEmployeeNewPassword.setBorder(new FlatBorder());
            }
            pwdSelectedRepeatEmployeePassword.grabFocus();
        }
    }//GEN-LAST:event_pwdSelectedEmployeeNewPasswordKeyPressed

    private void pwdSelectedEmployeeNewPasswordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdSelectedEmployeeNewPasswordKeyTyped
        String password = (String.valueOf(pwdSelectedEmployeeNewPassword.getPassword()));
        int length = password.length();

        if (length < 3) {
            lblSelectedEmployeePhoneNoValidation1.setText("Password is Too Short!");
            pwdSelectedEmployeeNewPassword.setBorder(new LineBorder(Color.red));
            pwdSelectedEmployeeNewPassword.setMinimumSize(new Dimension(64, 31));

        } else {
            lblSelectedEmployeePhoneNoValidation1.setText(null);
            pwdSelectedEmployeeNewPassword.setBorder(new FlatBorder());

        }
    }//GEN-LAST:event_pwdSelectedEmployeeNewPasswordKeyTyped

    private void pwdSelectedRepeatEmployeePasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdSelectedRepeatEmployeePasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            if (String.valueOf(pwdSelectedRepeatEmployeePassword.getPassword()).isEmpty()) {
                lblSelectedEmployeeSalaryValidation.setText("Please Fill this Field!");
                pwdSelectedRepeatEmployeePassword.setBorder(new LineBorder(Color.red));
                pwdSelectedRepeatEmployeePassword.setMinimumSize(new Dimension(64, 31));

            } else {
                lblSelectedEmployeeSalaryValidation.setText(null);
                pwdSelectedRepeatEmployeePassword.setBorder(new FlatBorder());
            }
            changeEmployeePassword();
        }
    }//GEN-LAST:event_pwdSelectedRepeatEmployeePasswordKeyPressed

    private void tblAdministratorPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAdministratorPasswordMouseClicked
        txtSelectedAdminId1.setText((String) tblAdministratorPassword.getValueAt(tblAdministratorPassword.getSelectedRow(), 0));
        txtSelectedAdminUsername1.setText((String) tblAdministratorPassword.getValueAt(tblAdministratorPassword.getSelectedRow(), 1));
        txtSelectedAdminCurrentPassword.setText((String) tblAdministratorPassword.getValueAt(tblAdministratorPassword.getSelectedRow(), 2));

        lblSelectedEmployeePhoneNoValidation4.setText(null);
        lblSelectedEmployeeSalaryValidation3.setText(null);

        pwdSelectedAdminNewPassword.setBorder(new FlatBorder());
        pwdSelectedRepeatAdminPassword.setBorder(new FlatBorder());
    }//GEN-LAST:event_tblAdministratorPasswordMouseClicked

    private void btnChangeSelectedAdminPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeSelectedAdminPasswordActionPerformed
        if (tblAdministratorPassword.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Admin Selected!");
            jPanel19.setVisible(false);
            jPanel19.setVisible(true);

        } else {
            jTabbedPaneAdministratorPassword.setSelectedIndex(1);
        }
    }//GEN-LAST:event_btnChangeSelectedAdminPasswordActionPerformed

    private void txtSearchEmployeePassword1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchEmployeePassword1KeyReleased
        searchAdministratorPassword();
    }//GEN-LAST:event_txtSearchEmployeePassword1KeyReleased

    private void btnRefreshAdminPasswordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshAdminPasswordsActionPerformed
        txtSearchEmployeePassword1.setText(null);
        txtSelectedAdminId1.setText(null);
        txtSelectedAdminUsername1.setText(null);
        txtSelectedAdminCurrentPassword.setText(null);
        pwdSelectedAdminNewPassword.setText(null);
        pwdSelectedRepeatAdminPassword.setText(null);
        refreshAdministratorPasswordTable();
        cmbSortAdministratorPassword.setSelectedIndex(0);
        jPanel19.setVisible(false);
        jPanel19.setVisible(true);
    }//GEN-LAST:event_btnRefreshAdminPasswordsActionPerformed

    private void cmbSortAdministratorPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortAdministratorPasswordActionPerformed
        searchAdministratorPassword();
    }//GEN-LAST:event_cmbSortAdministratorPasswordActionPerformed

    private void btnGoBackToViewAdminPasswordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackToViewAdminPasswordsActionPerformed
        jTabbedPaneAdministratorPassword.setSelectedIndex(0);
    }//GEN-LAST:event_btnGoBackToViewAdminPasswordsActionPerformed

    private void btnChangeAdminPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeAdminPasswordActionPerformed
        if (tblAdministratorPassword.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Admin Selected!");
            jTabbedPaneAdministratorPassword.setSelectedIndex(0);

        } else {
            changeAdministratorPassword();
            jPanel20.setVisible(false);
            jPanel20.setVisible(true);
        }
    }//GEN-LAST:event_btnChangeAdminPasswordActionPerformed

    private void pwdSelectedAdminNewPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdSelectedAdminNewPasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            if (String.valueOf(pwdSelectedAdminNewPassword.getPassword()).isEmpty()) {
                lblSelectedEmployeePhoneNoValidation4.setText("Please Fill this Field!");
                pwdSelectedAdminNewPassword.setBorder(new LineBorder(Color.red));
                pwdSelectedAdminNewPassword.setMinimumSize(new Dimension(64, 31));

            } else {
                lblSelectedEmployeePhoneNoValidation4.setText(null);
                pwdSelectedAdminNewPassword.setBorder(new FlatBorder());
            }
            pwdSelectedRepeatAdminPassword.grabFocus();
        }
    }//GEN-LAST:event_pwdSelectedAdminNewPasswordKeyPressed

    private void pwdSelectedAdminNewPasswordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdSelectedAdminNewPasswordKeyTyped
        String password = (String.valueOf(pwdSelectedAdminNewPassword.getPassword()));
        int length = password.length();

        if (length < 3) {
            lblSelectedEmployeePhoneNoValidation4.setText("Password is Too Short!");
            pwdSelectedAdminNewPassword.setBorder(new LineBorder(Color.red));
            pwdSelectedAdminNewPassword.setMinimumSize(new Dimension(64, 31));

        } else {
            lblSelectedEmployeePhoneNoValidation4.setText(null);
            pwdSelectedAdminNewPassword.setBorder(new FlatBorder());

        }
    }//GEN-LAST:event_pwdSelectedAdminNewPasswordKeyTyped

    private void pwdSelectedRepeatAdminPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdSelectedRepeatAdminPasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            if (String.valueOf(pwdSelectedRepeatAdminPassword.getPassword()).isEmpty()) {
                lblSelectedEmployeeSalaryValidation3.setText("Please Fill this Field!");
                pwdSelectedRepeatAdminPassword.setBorder(new LineBorder(Color.red));
                pwdSelectedRepeatAdminPassword.setMinimumSize(new Dimension(64, 31));

            } else {
                lblSelectedEmployeeSalaryValidation3.setText(null);
                pwdSelectedRepeatAdminPassword.setBorder(new FlatBorder());
            }
            changeAdministratorPassword();
        }
    }//GEN-LAST:event_pwdSelectedRepeatAdminPasswordKeyPressed

    private void tblSuperUserPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSuperUserPasswordMouseClicked
        txtSelectedSuperUserId.setText((String) tblSuperUserPassword.getValueAt(tblSuperUserPassword.getSelectedRow(), 0));
        txtSelectedSuperUserUsername.setText((String) tblSuperUserPassword.getValueAt(tblSuperUserPassword.getSelectedRow(), 1));
        txtSelectedSuperUserCurrentPassword.setText((String) tblSuperUserPassword.getValueAt(tblSuperUserPassword.getSelectedRow(), 2));

        lblSelectedEmployeePhoneNoValidation5.setText(null);
        lblSelectedEmployeeSalaryValidation4.setText(null);

        pwdSelectedSuperUserNewPassword.setBorder(new FlatBorder());
        pwdSelectedRepeatSuperUserPassword.setBorder(new FlatBorder());
    }//GEN-LAST:event_tblSuperUserPasswordMouseClicked

    private void btnChangeSelectedSuperUserPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeSelectedSuperUserPasswordActionPerformed
        if (tblSuperUserPassword.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Super User Selected!");
            jPanel21.setVisible(false);
            jPanel21.setVisible(true);

        } else {
            jTabbedPaneSuperUserPassword.setSelectedIndex(1);
        }
    }//GEN-LAST:event_btnChangeSelectedSuperUserPasswordActionPerformed

    private void txtSearchSuperUserPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSuperUserPasswordKeyReleased
        searchSuperUserPassword();
    }//GEN-LAST:event_txtSearchSuperUserPasswordKeyReleased

    private void btnSuperUserPasswordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuperUserPasswordsActionPerformed
        txtSearchSuperUserPassword.setText(null);
        txtSelectedSuperUserId.setText(null);
        txtSelectedSuperUserUsername.setText(null);
        txtSelectedSuperUserCurrentPassword.setText(null);
        pwdSelectedSuperUserNewPassword.setText(null);
        pwdSelectedRepeatSuperUserPassword.setText(null);
        refreshSuperUserPasswordTable();
        cmbSortSuperUserPassword.setSelectedIndex(0);
        jPanel21.setVisible(false);
        jPanel21.setVisible(true);
    }//GEN-LAST:event_btnSuperUserPasswordsActionPerformed

    private void cmbSortSuperUserPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortSuperUserPasswordActionPerformed
        searchSuperUserPassword();
    }//GEN-LAST:event_cmbSortSuperUserPasswordActionPerformed

    private void btnGoBackToViewSuperUserPasswordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackToViewSuperUserPasswordsActionPerformed
        jTabbedPaneSuperUserPassword.setSelectedIndex(0);
    }//GEN-LAST:event_btnGoBackToViewSuperUserPasswordsActionPerformed

    private void btnChangeSuperUserPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeSuperUserPasswordActionPerformed
        if (tblSuperUserPassword.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Super User Selected!");
            jTabbedPaneSuperUserPassword.setSelectedIndex(0);

        } else {
            changeSuperUserPassword();

            jPanel22.setVisible(false);
            jPanel22.setVisible(true);
        }
    }//GEN-LAST:event_btnChangeSuperUserPasswordActionPerformed

    private void pwdSelectedSuperUserNewPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdSelectedSuperUserNewPasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            if (String.valueOf(pwdSelectedSuperUserNewPassword.getPassword()).isEmpty()) {
                lblSelectedEmployeePhoneNoValidation5.setText("Please Fill this Field!");
                pwdSelectedSuperUserNewPassword.setBorder(new LineBorder(Color.red));
                pwdSelectedSuperUserNewPassword.setMinimumSize(new Dimension(64, 31));

            } else {
                lblSelectedEmployeePhoneNoValidation5.setText(null);
                pwdSelectedAdminNewPassword.setBorder(new FlatBorder());
            }
            pwdSelectedRepeatSuperUserPassword.grabFocus();
        }
    }//GEN-LAST:event_pwdSelectedSuperUserNewPasswordKeyPressed

    private void pwdSelectedSuperUserNewPasswordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdSelectedSuperUserNewPasswordKeyTyped
        String password = (String.valueOf(pwdSelectedSuperUserNewPassword.getPassword()));
        int length = password.length();

        if (length < 3) {
            lblSelectedEmployeePhoneNoValidation5.setText("Password is Too Short!");
            pwdSelectedSuperUserNewPassword.setBorder(new LineBorder(Color.red));
            pwdSelectedSuperUserNewPassword.setMinimumSize(new Dimension(64, 31));
        } else {
            lblSelectedEmployeePhoneNoValidation5.setText(null);
            pwdSelectedSuperUserNewPassword.setBorder(new FlatBorder());

        }
    }//GEN-LAST:event_pwdSelectedSuperUserNewPasswordKeyTyped

    private void pwdSelectedRepeatSuperUserPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdSelectedRepeatSuperUserPasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            if (String.valueOf(pwdSelectedRepeatSuperUserPassword.getPassword()).isEmpty()) {
                lblSelectedEmployeeSalaryValidation4.setText("Please Fill this Field!");
                pwdSelectedRepeatSuperUserPassword.setBorder(new LineBorder(Color.red));
                pwdSelectedRepeatSuperUserPassword.setMinimumSize(new Dimension(64, 31));

            } else {
                lblSelectedEmployeeSalaryValidation4.setText(null);
                pwdSelectedRepeatSuperUserPassword.setBorder(new FlatBorder());
            }
            changeSuperUserPassword();
        }
    }//GEN-LAST:event_pwdSelectedRepeatSuperUserPasswordKeyPressed

    private void stock2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stock2MouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlLog);
        resetPnlColor(pnlAdmin);
        setPnlColor(pnlStockReturn);
        resetPnlColor(pnlSales1);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorLog);
        resetPnlIndicatorColor(pnlIndicatorAdmin);
        setPnlIndicatorColor(pnlIndicatorStockReturn);
        resetPnlIndicatorColor(pnlIndicatorSales1);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Stock Return");

        cardHome.setVisible(false);
        cardSystemLog.setVisible(false);
        cardAdmin.setVisible(false);
        cardStockReturn.setVisible(true);
        cardPassword.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_stock2MouseClicked

    private void pnlStockReturnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlStockReturnMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlLog);
        resetPnlColor(pnlAdmin);
        setPnlColor(pnlStockReturn);
        resetPnlColor(pnlSales1);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorLog);
        resetPnlIndicatorColor(pnlIndicatorAdmin);
        setPnlIndicatorColor(pnlIndicatorStockReturn);
        resetPnlIndicatorColor(pnlIndicatorSales1);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Stock Return");

        cardHome.setVisible(false);
        cardSystemLog.setVisible(false);
        cardAdmin.setVisible(false);
        cardStockReturn.setVisible(true);
        cardPassword.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_pnlStockReturnMouseClicked

    private void btnReturnStockItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturnStockItemsActionPerformed
        returnStockItem();
        jPanel8.setVisible(false);
        jPanel8.setVisible(true);
    }//GEN-LAST:event_btnReturnStockItemsActionPerformed

    private void tblStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStockMouseClicked
        try {

            txtSKUId.setText((String) tblStock.getValueAt(tblStock.getSelectedRow(), 0));
            txtItemCode.setText((String) tblStock.getValueAt(tblStock.getSelectedRow(), 1));
            txtItemName.setText((String) tblStock.getValueAt(tblStock.getSelectedRow(), 2));
            txtSupplier.setText((String) tblStock.getValueAt(tblStock.getSelectedRow(), 5));
            txtStockCount.setText((String) tblStock.getValueAt(tblStock.getSelectedRow(), 6));

            DB.DB.con.close();

        } catch (SQLException e) {
        }
    }//GEN-LAST:event_tblStockMouseClicked

    private void btnDeleteStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteStockActionPerformed
        if (tblStock.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Stock Item Selected!");
        } else {

            deleteStockItem();

        }
        jPanel15.setVisible(false);
        jPanel15.setVisible(true);
    }//GEN-LAST:event_btnDeleteStockActionPerformed

    private void btnReturnSelectedStockItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReturnSelectedStockItemsActionPerformed
        if (tblStock.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Stock Item Selected!");
        } else {

            jTabbedPaneStockReturn.setSelectedIndex(1);
            txtReturnCount.setText("");
            txtReturnCount.grabFocus();

        }
        jPanel15.setVisible(false);
        jPanel15.setVisible(true);
    }//GEN-LAST:event_btnReturnSelectedStockItemsActionPerformed

    private void btnPrintStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintStockActionPerformed
        printStock();
        jPanel15.setVisible(false);
        jPanel15.setVisible(true);
    }//GEN-LAST:event_btnPrintStockActionPerformed

    private void txtSearchStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStockKeyReleased
        searchStock();
    }//GEN-LAST:event_txtSearchStockKeyReleased

    private void btnStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockActionPerformed
        refreshStockTable();
        jPanel15.setVisible(false);
        jPanel15.setVisible(true);

        txtSKUId.setText(null);
        txtItemCode.setText(null);
        txtItemName.setText(null);
        txtSupplier.setText(null);
        txtStockCount.setText(null);
        txtReturnCount.setText(null);

    }//GEN-LAST:event_btnStockActionPerformed

    private void cmbSortStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortStockActionPerformed
        searchStock();
    }//GEN-LAST:event_cmbSortStockActionPerformed

    private void btnRefreshStockReturnHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshStockReturnHistoryActionPerformed
        refreshStockReturnTable();
        jPanel16.setVisible(false);
        jPanel16.setVisible(true);
    }//GEN-LAST:event_btnRefreshStockReturnHistoryActionPerformed

    private void btnPrintStockReturnHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintStockReturnHistoryActionPerformed
        printStockReturn();
        jPanel16.setVisible(false);
        jPanel16.setVisible(true);
    }//GEN-LAST:event_btnPrintStockReturnHistoryActionPerformed

    private void txtSearchStockReturnKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStockReturnKeyReleased
        searchStockReturn();
    }//GEN-LAST:event_txtSearchStockReturnKeyReleased

    private void cmbSortStockReturnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortStockReturnActionPerformed
        searchStockReturn();
    }//GEN-LAST:event_cmbSortStockReturnActionPerformed

    private void txtSearchStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStockKeyTyped
        calculateStockCount();
    }//GEN-LAST:event_txtSearchStockKeyTyped

    private void txtReturnCountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReturnCountKeyPressed
        if (evt.getKeyCode() == 10) {
            returnStockItem();
            jPanel8.setVisible(false);
            jPanel8.setVisible(true);
        }
    }//GEN-LAST:event_txtReturnCountKeyPressed

    private void txtReturnCountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReturnCountKeyReleased
        try {

            int amountTyped = Integer.parseInt(txtReturnCount.getText());
            int availableStockCount = Integer.parseInt(txtStockCount.getText());
            if (amountTyped > availableStockCount) {
                JOptionPane.showMessageDialog(this, "No Sufficient Stock Count Available!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                txtReturnCount.setText(null);
            }
        } catch (HeadlessException | NumberFormatException e) {
        }
    }//GEN-LAST:event_txtReturnCountKeyReleased

    private void txtSearchStockReturnKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStockReturnKeyTyped
        calculateStockReturnCount();
    }//GEN-LAST:event_txtSearchStockReturnKeyTyped

    private void btnGoBackToStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackToStockActionPerformed
        jTabbedPaneStockReturn.setSelectedIndex(0);
    }//GEN-LAST:event_btnGoBackToStockActionPerformed

    private void btnPrintStockReturnBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintStockReturnBillActionPerformed
        printSelectedStockReturn();
        jPanel8.setVisible(false);
        jPanel8.setVisible(true);
    }//GEN-LAST:event_btnPrintStockReturnBillActionPerformed

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
                new EmergencyDashboard().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/xeon_icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel admin;
    private javax.swing.JLabel adminName;
    private javax.swing.JButton btnAddNewAdmin;
    private javax.swing.JButton btnAdmin;
    private javax.swing.JButton btnBackup;
    private javax.swing.JButton btnBrowseBackupPath;
    private javax.swing.JButton btnBrowseRestorePath;
    private javax.swing.JButton btnChangeAdminPassword;
    private javax.swing.JButton btnChangeEmployeePassword;
    private javax.swing.JButton btnChangeSelectedAdminPassword;
    private javax.swing.JButton btnChangeSelectedEmployeePassword;
    private javax.swing.JButton btnChangeSelectedSuperUserPassword;
    private javax.swing.JButton btnChangeSuperUserPassword;
    private javax.swing.JButton btnDeleteAdmin;
    private javax.swing.JButton btnDeleteSelectedAdmin;
    private javax.swing.JButton btnDeleteStock;
    private javax.swing.JButton btnEditAdmin;
    private javax.swing.JButton btnEditEmployee1;
    private javax.swing.JButton btnEditEmployee2;
    private javax.swing.JButton btnGoBackToStock;
    private javax.swing.JButton btnGoBackToViewAdmin;
    private javax.swing.JButton btnGoBackToViewAdminPasswords;
    private javax.swing.JButton btnGoBackToViewEmployeePasswords;
    private javax.swing.JButton btnGoBackToViewSuperUserPasswords;
    private javax.swing.JButton btnPrintAdmins;
    private javax.swing.JButton btnPrintLogs;
    private javax.swing.JButton btnPrintSelectedAdmin;
    private javax.swing.JButton btnPrintStock;
    private javax.swing.JButton btnPrintStockReturnBill;
    private javax.swing.JButton btnPrintStockReturnHistory;
    private javax.swing.JButton btnRefreshAdminPasswords;
    private javax.swing.JButton btnRefreshEmployeePasswords;
    private javax.swing.JButton btnRefreshLogs;
    private javax.swing.JButton btnRefreshStockReturnHistory;
    private javax.swing.JButton btnRestore;
    private javax.swing.JButton btnReturnSelectedStockItems;
    private javax.swing.JButton btnReturnStockItems;
    private javax.swing.JButton btnStock;
    private javax.swing.JButton btnSuperUserPasswords;
    private javax.swing.JButton btnUpdateSelectedAdmin;
    private javax.swing.JLabel btn_addUsers1;
    private javax.swing.JPanel card1;
    private javax.swing.JPanel card2;
    private javax.swing.JPanel card3;
    private javax.swing.JPanel card4;
    private javax.swing.JPanel card5;
    private javax.swing.JPanel card6;
    private javax.swing.JPanel card7;
    private javax.swing.JPanel card8;
    private javax.swing.JPanel cardAdmin;
    private javax.swing.JPanel cardHome;
    private javax.swing.JLabel cardName;
    private javax.swing.JPanel cardPassword;
    private javax.swing.JPanel cardSettings;
    private javax.swing.JPanel cardStockReturn;
    private javax.swing.JPanel cardSystemLog;
    private javax.swing.JLabel close1;
    private javax.swing.JLabel close2;
    private javax.swing.JComboBox<String> cmbSortAdmin;
    private javax.swing.JComboBox<String> cmbSortAdministratorPassword;
    private javax.swing.JComboBox<String> cmbSortEmployeePassword;
    private javax.swing.JComboBox<String> cmbSortStock;
    private javax.swing.JComboBox<String> cmbSortStockReturn;
    private javax.swing.JComboBox<String> cmbSortSuperUserPassword;
    private javax.swing.JComboBox<String> cmbSortSystemLogs;
    private javax.swing.JLabel home;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel166;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPaneAdministratorPassword;
    private javax.swing.JTabbedPane jTabbedPaneAdministrators;
    private javax.swing.JTabbedPane jTabbedPaneEmployeePassword;
    private javax.swing.JTabbedPane jTabbedPaneStockReturn;
    private javax.swing.JTabbedPane jTabbedPaneSuperUserPassword;
    private javax.swing.JLabel lblAdminCount;
    private javax.swing.JLabel lblAdministratorCount;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblEmployeeCount;
    private javax.swing.JLabel lblEmployeeNICValidation;
    private javax.swing.JLabel lblEmployeePasswordValidation;
    private javax.swing.JLabel lblEmployeePhoneNoValidation;
    private javax.swing.JLabel lblEmployeeRepeatPasswordValidation;
    private javax.swing.JLabel lblEmployeeUserameValidation;
    private javax.swing.JLabel lblLogCount;
    private javax.swing.JLabel lblLogout;
    private javax.swing.JLabel lblSelectedEmployeeIdValidation;
    private javax.swing.JLabel lblSelectedEmployeeNICValidation;
    private javax.swing.JLabel lblSelectedEmployeePhoneNoValidation;
    private javax.swing.JLabel lblSelectedEmployeePhoneNoValidation1;
    private javax.swing.JLabel lblSelectedEmployeePhoneNoValidation4;
    private javax.swing.JLabel lblSelectedEmployeePhoneNoValidation5;
    private javax.swing.JLabel lblSelectedEmployeeSalaryValidation;
    private javax.swing.JLabel lblSelectedEmployeeSalaryValidation3;
    private javax.swing.JLabel lblSelectedEmployeeSalaryValidation4;
    private javax.swing.JLabel lblSelectedEmployeeUsernameValidation;
    private javax.swing.JLabel lblStockCount;
    private javax.swing.JLabel lblStockReturnCount;
    private javax.swing.JLabel lblSuperUserCount;
    public static final javax.swing.JLabel lblSuperUserUsername = new javax.swing.JLabel();
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel loginWish;
    private javax.swing.JLabel logs;
    private javax.swing.JPanel mainCard;
    private javax.swing.JLabel maximize1;
    private javax.swing.JLabel maximize2;
    private javax.swing.JPanel maximizeView;
    private javax.swing.JLabel minimize1;
    private javax.swing.JLabel minimize2;
    private javax.swing.JPanel pnlAdmin;
    private javax.swing.JPanel pnlAdminSubSelection;
    private javax.swing.JPanel pnlBranding;
    private javax.swing.JPanel pnlDateTime;
    private javax.swing.JPanel pnlForceAccessLoader;
    private javax.swing.JPanel pnlForceAccessLoaderBody;
    private javax.swing.JPanel pnlFrames;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlHomeSubSelection;
    private javax.swing.JPanel pnlIndicatorAdmin;
    private javax.swing.JPanel pnlIndicatorHome;
    private javax.swing.JPanel pnlIndicatorLog;
    private javax.swing.JPanel pnlIndicatorSales1;
    private javax.swing.JPanel pnlIndicatorSettings;
    private javax.swing.JPanel pnlIndicatorStockReturn;
    private javax.swing.JPanel pnlLog;
    private javax.swing.JPanel pnlLogout;
    private javax.swing.JPanel pnlLogoutLoader;
    private javax.swing.JPanel pnlLogoutLoaderBody;
    private javax.swing.JPanel pnlParent;
    private javax.swing.JPanel pnlPasswordSubSelection;
    private javax.swing.JPanel pnlSales1;
    private javax.swing.JPanel pnlSettings;
    private javax.swing.JPanel pnlSettingsSubSelection;
    private javax.swing.JPanel pnlSidebar;
    private javax.swing.JPanel pnlStockReturn;
    private javax.swing.JPanel pnlStockReturnSubSelection;
    private javax.swing.JPanel pnlSuperUserDashboard;
    private javax.swing.JPanel pnlSystemLogSubSelection;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JPanel pnlTopIcons;
    private javax.swing.JPasswordField pwdAdminPassword;
    private javax.swing.JPasswordField pwdRepeatAdminPassword;
    private javax.swing.JPasswordField pwdSelectedAdminNewPassword;
    private javax.swing.JPasswordField pwdSelectedEmployeeNewPassword;
    private javax.swing.JPasswordField pwdSelectedRepeatAdminPassword;
    private javax.swing.JPasswordField pwdSelectedRepeatEmployeePassword;
    private javax.swing.JPasswordField pwdSelectedRepeatSuperUserPassword;
    private javax.swing.JPasswordField pwdSelectedSuperUserNewPassword;
    private javax.swing.JLabel reports;
    private javax.swing.JPanel restoreDownView;
    private javax.swing.JLabel stock1;
    private javax.swing.JLabel stock2;
    private javax.swing.JPanel subCardAdmin;
    private javax.swing.JPanel subCardHome;
    private javax.swing.JPanel subCardPassword;
    private javax.swing.JPanel subCardSettings;
    private javax.swing.JPanel subCardStockReturn;
    private javax.swing.JPanel subCardSystemLog;
    private javax.swing.JTable tblAdmin;
    private javax.swing.JTable tblAdministratorPassword;
    private javax.swing.JTable tblEmployeePassword;
    private javax.swing.JTable tblStock;
    private javax.swing.JTable tblStockReturn;
    private javax.swing.JTable tblSuperUserPassword;
    private javax.swing.JTable tblSystemLogs;
    private javax.swing.JLabel title;
    private javax.swing.JTextField txtAdminNIC;
    private javax.swing.JTextField txtAdminPhoneNo;
    private javax.swing.JTextField txtAdminUsername;
    private javax.swing.JTextField txtBackupLocation;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtRestoreLocation;
    private javax.swing.JTextField txtReturnCount;
    private javax.swing.JTextField txtSKUId;
    private javax.swing.JTextField txtSearchAdmin;
    private javax.swing.JTextField txtSearchEmployeePassword;
    private javax.swing.JTextField txtSearchEmployeePassword1;
    private javax.swing.JTextField txtSearchStock;
    private javax.swing.JTextField txtSearchStockReturn;
    private javax.swing.JTextField txtSearchSuperUserPassword;
    private javax.swing.JTextField txtSearchSystemLogs;
    private javax.swing.JTextField txtSelectedAdminCurrentPassword;
    private javax.swing.JTextField txtSelectedAdminId;
    private javax.swing.JTextField txtSelectedAdminId1;
    private javax.swing.JTextField txtSelectedAdminNIC;
    private javax.swing.JTextField txtSelectedAdminPhoneNo;
    private javax.swing.JTextField txtSelectedAdminUsername;
    private javax.swing.JTextField txtSelectedAdminUsername1;
    private javax.swing.JTextField txtSelectedEmployeeCurrentPassword;
    private javax.swing.JTextField txtSelectedEmployeeId;
    private javax.swing.JTextField txtSelectedEmployeeUsername;
    private javax.swing.JTextField txtSelectedSuperUserCurrentPassword;
    private javax.swing.JTextField txtSelectedSuperUserId;
    private javax.swing.JTextField txtSelectedSuperUserUsername;
    private javax.swing.JTextField txtStockCount;
    private javax.swing.JTextField txtSupplier;
    private javax.swing.JLabel version;
    private javax.swing.JLabel watermark;
    // End of variables declaration//GEN-END:variables
}
