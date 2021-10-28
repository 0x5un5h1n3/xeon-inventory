/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.ui.FlatBorder;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import javax.swing.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
public class AdminDashboard extends javax.swing.JFrame {

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

    private double salary;
    private double extra;
    private double result;

    AdminDashboard() {

        initComponents();

        AdminDashboard.this.getRootPane().setBorder(new LineBorder(((new Color(30, 30, 30)))));
        AdminDashboard.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setIcon();
        refreshEmployeeTable();
        refreshEmployeeAttendanceTable();
        refreshEmployeeMonthlyAttendanceTable();
        refreshEmployeePayrollTable();
        refreshEmployeePayrollHistoryTable();
        refreshSupplierTable();
        refreshPOTable();
        refreshGRNTable();
        fillCmbSuppliers();
        setTableheader();
        generateOPId();
        refreshSupplierItemTable();
        refreshStockTable();
        refreshLowStockTable();
        refreshSalesTable();
        refreshSalesHistoryTable();
        AdminDashboard.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        maximizeView.setVisible(true);
        restoreDownView.setVisible(false);
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        AdminDashboard.this.setMaximizedBounds(env.getMaximumWindowBounds());
        maximized = false;

        AdminDashboard.this.addWindowListener(new WindowAdapter() {
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
                    adminName.setText(lblAdminUsername.getText());
                } else if (now >= 12 && now < 17) {
                    loginWish.setText("Good afternoon");
                    adminName.setText(lblAdminUsername.getText());
                } else if (now >= 17 && now < 20) {
                    loginWish.setText("Good evening");
                    adminName.setText(lblAdminUsername.getText());
                } else {
                    loginWish.setText("Good night");
                    adminName.setText(lblAdminUsername.getText());
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
        JTableHeader emptbl = tblEmployee.getTableHeader();
        emptbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emptbl.setForeground(Color.white);
        emptbl.setBackground(new Color(18, 18, 18));
        tblEmployee.getTableHeader().setReorderingAllowed(false);

        JTableHeader empatttbl = tblEmployeeAttendance.getTableHeader();
        empatttbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        empatttbl.setForeground(Color.white);
        empatttbl.setBackground(new Color(18, 18, 18));
        tblEmployeeAttendance.getTableHeader().setReorderingAllowed(false);

        JTableHeader empmoattttbl = tblEmployeeMonthlyAttendance.getTableHeader();
        empmoattttbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        empmoattttbl.setForeground(Color.white);
        empmoattttbl.setBackground(new Color(18, 18, 18));
        tblEmployeeMonthlyAttendance.getTableHeader().setReorderingAllowed(false);

        JTableHeader emppaytbl = tblEmployeePayroll.getTableHeader();
        emppaytbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emppaytbl.setForeground(Color.white);
        emppaytbl.setBackground(new Color(18, 18, 18));
        tblEmployeePayroll.getTableHeader().setReorderingAllowed(false);

        JTableHeader emppayhisttbl = tblEmployeePayrollHistory.getTableHeader();
        emppayhisttbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emppayhisttbl.setForeground(Color.white);
        emppayhisttbl.setBackground(new Color(18, 18, 18));
        tblEmployeePayrollHistory.getTableHeader().setReorderingAllowed(false);

        JTableHeader suptbl = tblSupplier.getTableHeader();
        suptbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        suptbl.setForeground(Color.white);
        suptbl.setBackground(new Color(18, 18, 18));
        tblSupplier.getTableHeader().setReorderingAllowed(false);

        JTableHeader potbl = tblPOItem.getTableHeader();
        potbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        potbl.setForeground(Color.white);
        potbl.setBackground(new Color(18, 18, 18));
        tblPOItem.getTableHeader().setReorderingAllowed(false);

        JTableHeader grntbl = tblGRN.getTableHeader();
        grntbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        grntbl.setForeground(Color.white);
        grntbl.setBackground(new Color(18, 18, 18));
        tblGRN.getTableHeader().setReorderingAllowed(false);

        JTableHeader supitmtbl = tblSupplierItem.getTableHeader();
        supitmtbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        supitmtbl.setForeground(Color.white);
        supitmtbl.setBackground(new Color(18, 18, 18));
        tblSupplierItem.getTableHeader().setReorderingAllowed(false);

        JTableHeader potbl1 = tblPOItem1.getTableHeader();
        potbl1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        potbl1.setForeground(Color.white);
        potbl1.setBackground(new Color(18, 18, 18));
        tblPOItem1.getTableHeader().setReorderingAllowed(false);

        JTableHeader grntbl1 = tblGRN1.getTableHeader();
        grntbl1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        grntbl1.setForeground(Color.white);
        grntbl1.setBackground(new Color(18, 18, 18));
        tblGRN1.getTableHeader().setReorderingAllowed(false);

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

    private void addNewEmployee() {

        int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Employee?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            if (!txtEmployeeUsername.getText().equals("")
                    & !txtEmployeePosition.getText().equals("")
                    & !txtEmployeePhoneNo.getText().equals("")
                    & (!String.valueOf(pwdEmployeePassword.getPassword()).equals("")
                    & (!String.valueOf(pwdRepeatEmployeePassword.getPassword()).equals("")))) {

                try {
                    ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE username like '" + txtEmployeeUsername.getText() + "%' order by id ");

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Username Already Exists!", "Invalid Username", JOptionPane.WARNING_MESSAGE);

                    } else if (!(String.valueOf(pwdEmployeePassword.getPassword()).length() >= 4)) {
                        JOptionPane.showMessageDialog(this, "Password is Too Short!", "Invalid Password", JOptionPane.WARNING_MESSAGE); //Short password
                        pwdEmployeePassword.setText(null);
                        pwdRepeatEmployeePassword.setText(null);
                        pwdEmployeePassword.grabFocus();

                    } else if (!String.valueOf(pwdEmployeePassword.getPassword()).equals(String.valueOf(pwdRepeatEmployeePassword.getPassword()))) {
                        JOptionPane.showMessageDialog(this, "Password doesn't match!", "Invalid Password Matching", JOptionPane.WARNING_MESSAGE); //invalid password matching
                        pwdEmployeePassword.setText(null);
                        pwdRepeatEmployeePassword.setText(null);
                        pwdEmployeePassword.grabFocus();

                    } else if (!(txtEmployeeNIC.getText().length() >= 9)) {
                        JOptionPane.showMessageDialog(this, "Invalid NIC!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                        txtEmployeeNIC.setText(null);
                        txtEmployeeNIC.grabFocus();

                    } else if (txtEmployeePhoneNo.getText().length() != 10) {
                        JOptionPane.showMessageDialog(this, "Invalid Phone Number Length!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                        txtEmployeePhoneNo.setText(null);
                        txtEmployeePhoneNo.grabFocus();

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

                        String addEmployee = "INSERT INTO employee(username, position, nic, phone_no, salary, password, account_status) VALUES ('" + txtEmployeeUsername.getText().trim() + "','" + txtEmployeePosition.getText() + "','" + txtEmployeeNIC.getText() + "','" + txtEmployeePhoneNo.getText() + "','" + txtEmployeeSalary.getText() + "','" + String.valueOf(pwdEmployeePassword.getPassword()) + "', 'Active')";
                        String fillDataToPayroll = "INSERT INTO payroll(id, username, month, year, date, attendance, payment, status) VALUES ((SELECT id FROM employee WHERE username = '" + txtEmployeeUsername.getText() + "'),'" + txtEmployeeUsername.getText() + "','" + month + "','" + year + "','" + date + "',(SELECT COUNT(id) FROM employee_attendance WHERE username = '" + txtEmployeeUsername.getText() + "' AND month = '" + month + "' AND year = '" + year + "'),(SELECT salary FROM employee WHERE username = '" + txtEmployeeUsername.getText() + "'),'UNPAID')";
                        String AddEmployeeActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Add New Employee','Admin added new employee " + txtEmployeeUsername.getText() + "','SUCCESS')";
                        try {
                            DB.DB.putData(addEmployee);
                            DB.DB.putData(fillDataToPayroll);
                            DB.DB.putData(AddEmployeeActivityLog);

                            DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                            dtm.setRowCount(0);
                            refreshEmployeeTable();

                            refreshEmployeeMonthlyAttendanceTable();

                            DefaultTableModel dtm1 = (DefaultTableModel) tblEmployeePayroll.getModel();
                            dtm1.setRowCount(0);
                            refreshEmployeePayrollTable();

                            DefaultTableModel dtm2 = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                            dtm2.setRowCount(0);
                            refreshEmployeePayrollHistoryTable();

                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Employee Added!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            txtEmployeeUsername.setText(null);
                            txtEmployeePosition.setText(null);
                            txtEmployeeNIC.setText(null);
                            txtEmployeePhoneNo.setText(null);
                            txtEmployeeSalary.setText(null);
                            pwdEmployeePassword.setText(null);
                            pwdRepeatEmployeePassword.setText(null);

                            lblEmployeeUserameValidation.setText(null);
                            lblEmployeePositionValidation.setText(null);
                            lblEmployeeNICValidation.setText(null);
                            lblEmployeePhoneNoValidation.setText(null);
                            lblEmployeeSalaryValidation.setText(null);
                            lblEmployeePasswordValidation.setText(null);
                            lblEmployeeRepeatPasswordValidation.setText(null);

                            txtEmployeeUsername.setBorder(new FlatBorder());
                            txtEmployeePosition.setBorder(new FlatBorder());
                            txtEmployeePhoneNo.setBorder(new FlatBorder());
                            txtEmployeeSalary.setBorder(new FlatBorder());
                            pwdEmployeePassword.setBorder(new FlatBorder());
                            pwdRepeatEmployeePassword.setBorder(new FlatBorder());

                        } catch (Exception e) {
                        }

                    }
                } catch (Exception ex) {
                    Logger.getLogger(AdminDashboard.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void deleteEmployee() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this Employee?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            try {
                DB.DB.putData("UPDATE employee SET account_status = 'Dective' WHERE id ='" + txtSelectedEmployeeId.getText() + "' ");
                String deleteEmployeeActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Delete Employee','Admin deleted employee " + txtSelectedEmployeeUsername.getText() + "','SUCCESS')";
                DB.DB.putData(deleteEmployeeActivityLog);
                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                JOptionPane.showMessageDialog(this, "Employee Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                txtSearchEmployee.setText(null);
                txtSelectedEmployeeId.setText(null);
                txtSelectedEmployeeUsername.setText(null);
                txtSelectedEmployeePosition.setText(null);
                txtSelectedEmployeeNIC.setText(null);
                txtSelectedEmployeePhoneNo.setText(null);
                txtSelectedEmployeeSalary.setText(null);
                DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                dtm.setRowCount(0);
                refreshEmployeeTable();

            } catch (Exception e) {
            }
        } else {

        }
    }

    private void updateEmployee() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Update this Employee?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSelectedEmployeeUsername.getText().equals("")
                    & !txtSelectedEmployeePosition.getText().equals("")
                    & !txtSelectedEmployeePhoneNo.getText().equals("")
                    & !txtSelectedEmployeeSalary.getText().equals("")) {

                if (!(txtSelectedEmployeePhoneNo.getText().length() == 10)) {
                    JOptionPane.showMessageDialog(this, "Invalid Phone Number!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    txtSelectedEmployeePhoneNo.setText(null);
                    txtSelectedEmployeePhoneNo.grabFocus();

                } else if (!(txtSelectedEmployeeNIC.getText().length() >= 9)) {
                    JOptionPane.showMessageDialog(this, "Invalid NIC!", "Invalid Data", JOptionPane.WARNING_MESSAGE);

                    txtSelectedEmployeeNIC.setText(null);
                    txtSelectedEmployeeNIC.grabFocus();

                } else {

                    try {
                        DB.DB.putData("UPDATE employee SET position ='" + txtSelectedEmployeePosition.getText() + "', nic ='" + txtSelectedEmployeeNIC.getText() + "', phone_no= '" + txtSelectedEmployeePhoneNo.getText() + "', salary ='" + txtSelectedEmployeeSalary.getText() + "' where id ='" + txtSelectedEmployeeId.getText() + "' ");
                        DB.DB.putData("UPDATE payroll SET payment ='" + txtSelectedEmployeeSalary.getText() + "' where id ='" + txtSelectedEmployeeId.getText() + "' ");
                        String updateEmployeeActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Update Employee','Admin updated employee " + txtSelectedEmployeeUsername.getText() + "','SUCCESS')";
                        DB.DB.putData(updateEmployeeActivityLog);
                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Employee Updated Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                        JOptionPane.showMessageDialog(this, "Payroll Updated Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                        txtSelectedEmployeeId.setText("");
                        txtSelectedEmployeeUsername.setText("");
                        txtSelectedEmployeePosition.setText("");
                        txtSelectedEmployeeNIC.setText("");
                        txtSelectedEmployeePhoneNo.setText("");
                        txtSelectedEmployeeSalary.setText("");
                        DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                        dtm.setRowCount(0);
                        refreshEmployeeTable();
                        refreshEmployeePayrollTable();

                    } catch (Exception e) {
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void refreshEmployeeTable() {
        try {
            DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE account_status = 'Active' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(6));
                dtm.addRow(new Object[]{a, b, c, d, e, f});
            }

            ResultSet count = DB.DB.getData("SELECT count(id) FROM employee WHERE account_status = 'Active' ");

            if (count.next()) {
                int d = count.getInt(1);
                lblEmployeeCount.setText("Employee Count : " + Integer.toString(d));
                lblHomeEmployeeCount.setText("Employee Count : " + Integer.toString(d));
            }

        } catch (Exception e) {
        }

    }

    private void searchEmployee() {
        switch (cmbSortEmployee.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE account_status = 'Active' AND id like '" + txtSearchEmployee.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE account_status = 'Active' AND  username like '" + txtSearchEmployee.getText() + "%' order by username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE account_status = 'Active' AND  position like '" + txtSearchEmployee.getText() + "%' order by position ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
            } catch (Exception e) {
            }
            break;
            case 3:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE account_status = 'Active' AND  nic like '" + txtSearchEmployee.getText() + "%' order by nic ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
            } catch (Exception e) {
            }
            break;
            case 4:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE account_status = 'Active' AND  phone_no like '" + txtSearchEmployee.getText() + "%' order by phone_no ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
            } catch (Exception e) {
            }
            break;
            case 5:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployee.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE account_status = 'Active' AND  salary like '" + txtSearchEmployee.getText() + "%' order by salary ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
            } catch (Exception e) {
            }
            default:
                break;
        }
    }

    private void refreshEmployeeAttendanceTable() {
        try {

            Date DateDate = new Date();
            SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
            date = "" + toDate.format(DateDate);

            DefaultTableModel dtm = (DefaultTableModel) tblEmployeeAttendance.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM employee_attendance WHERE date = '" + date + "' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(6));
                dtm.addRow(new Object[]{a, b, c, d, e, f});
            }

            String AttendanceCount = ("Attendance Count : " + Integer.toString(tblEmployeeAttendance.getRowCount()));

            int AttCount = tblEmployeeAttendance.getRowCount();

            lblAttendanceCount.setText(AttendanceCount);
            lblHomeEmployeeAttendancePresent.setText(AttendanceCount);

            int EmployeeCount = tblEmployee.getRowCount();
            int AbsCount = (EmployeeCount - AttCount);

            lblAbsenceCount.setText("Absence Count : " + AbsCount);
            lblHomeEmployeeAttendanceAbsent.setText("Absence Count : " + AbsCount);

        } catch (Exception e) {
        }
    }

    private void searchEmployeeAttendance() {

        switch (cmbSortEmployeeAttendance.getSelectedIndex()) {
            case 0:
                Date DateDate = new Date();
                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                date = "" + toDate.format(DateDate);

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblEmployeeAttendance.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM employee_attendance WHERE date = '" + date + "' AND id like '" + txtSearchEmployeeAttendance.getText() + "%' order by id ");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(1));
                        String b = (rs.getString(2));
                        String c = (rs.getString(3));
                        String d = (rs.getString(4));
                        String e = (rs.getString(5));
                        String f = (rs.getString(6));
                        dtm.addRow(new Object[]{a, b, c, d, e, f});
                    }

                } catch (Exception e) {
                }
                break;
            case 1:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeeAttendance.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee_attendance WHERE id like '" + txtSearchEmployeeAttendance.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeeAttendance.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee_attendance WHERE username like '" + txtSearchEmployeeAttendance.getText() + "%' order by username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }

            } catch (Exception e) {
            }
            break;
            case 3:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeeAttendance.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee_attendance WHERE month like '" + txtSearchEmployeeAttendance.getText() + "%' order by month ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }

            } catch (Exception e) {
            }
            break;
            case 4:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeeAttendance.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee_attendance WHERE year like '" + txtSearchEmployeeAttendance.getText() + "%' order by year ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }

            } catch (Exception e) {
            }
            break;
            case 5:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeeAttendance.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee_attendance WHERE date like '" + txtSearchEmployeeAttendance.getText() + "%' order by date ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }

            } catch (Exception e) {
            }
            break;
            case 6:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeeAttendance.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM employee_attendance WHERE time like '" + txtSearchEmployeeAttendance.getText() + "%' order by time ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }

            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
        jXDatePicker1.setDate(null);
    }

    private void refreshEmployeeMonthlyAttendanceTable() {

        try {

            Date DateMonth = new Date();
            SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
            month = "" + toMonth.format(DateMonth);

            Date DateYear = new Date();
            SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
            year = "" + toYear.format(DateYear);

            DefaultTableModel dtm = (DefaultTableModel) tblEmployeeMonthlyAttendance.getModel();
            ResultSet rs = DB.DB.getData("SELECT A.id, A.username, B.attendance FROM employee AS A LEFT JOIN payroll AS B ON B.id = A.id WHERE month = '" + month + "' AND year = '" + year + "'");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (month);
                String e = (year);
                dtm.addRow(new Object[]{a, b, c, d, e});
            }

        } catch (Exception e) {
        }
    }

    private void searchMonthlyEmployeeAttendance() {

        switch (cmbSortMonthlyEmployeeAttendance.getSelectedIndex()) {
            case 0:
                try {
                Date DateMonth = new Date();
                SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
                month = "" + toMonth.format(DateMonth);

                Date DateYear = new Date();
                SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
                year = "" + toYear.format(DateYear);

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeeMonthlyAttendance.getModel();
                ResultSet rs = DB.DB.getData("SELECT A.id, A.username, B.attendance FROM employee AS A LEFT JOIN payroll AS B ON B.id = A.id WHERE month = '" + month + "' AND year = '" + year + "' AND A.id like '" + txtSearchMonthlyEmployeeAttendance.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (month);
                    String e = (year);
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }

            } catch (Exception e) {
            }
            break;

            case 1:
                try {
                Date DateMonth = new Date();
                SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
                month = "" + toMonth.format(DateMonth);

                Date DateYear = new Date();
                SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
                year = "" + toYear.format(DateYear);

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeeMonthlyAttendance.getModel();
                ResultSet rs = DB.DB.getData("SELECT A.id, A.username, B.attendance FROM employee AS A LEFT JOIN payroll AS B ON B.id = A.id WHERE month = '" + month + "' AND year = '" + year + "' AND A.username like '" + txtSearchMonthlyEmployeeAttendance.getText() + "%' order by username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (month);
                    String e = (year);
                    dtm.addRow(new Object[]{a, b, c, d, e});
                }
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
        jXDatePicker1.setDate(null);
    }

    private void refreshEmployeePayrollTable() {
        try {

            Date DateMonth = new Date();
            SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
            month = "" + toMonth.format(DateMonth);

            Date DateYear = new Date();
            SimpleDateFormat toYear = new SimpleDateFormat("YYYY");
            year = "" + toYear.format(DateYear);

            DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE month = '" + month + "'  AND year = '" + year + "' order by status desc");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(6));
                String g = (rs.getString(7));
                String h = (rs.getString(8));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});

            }
            ResultSet paidCount = DB.DB.getData("SELECT count(id) FROM payroll WHERE status = 'PAID' AND month = '" + month + "'  AND year = '" + year + "' ");
            if (paidCount.next()) {
                int i = paidCount.getInt(1);
                if (i <= 0) {
                    lblHomePayrollPaid.setForeground(new Color(255, 0, 51));
                } else {
                    lblHomePayrollPaid.setForeground(new Color(180, 180, 180));
                }
                lblPaidCount.setText("Paid Count : " + i);
                lblHomePayrollPaid.setText("Paid Count : " + i);
            }

            ResultSet unpaidCount = DB.DB.getData("SELECT count(id) FROM payroll WHERE status = 'UNPAID' AND month = '" + month + "' AND year = '" + year + "' ");
            if (unpaidCount.next()) {
                int j = unpaidCount.getInt(1);
                if (j != 0) {
                    lblHomePayrollUnpaid.setForeground(new Color(255, 0, 51));
                } else {
                    lblHomePayrollUnpaid.setForeground(new Color(180, 180, 180));
                }
                lblUnpaidCount.setText("Unpaid Count : " + j);
                lblHomePayrollUnpaid.setText("Unpaid Count : " + j);
            }

            ResultSet payrollPaidAmount = DB.DB.getData("SELECT SUM(payment) FROM payroll WHERE month = '" + month + "' AND year = '" + year + "' AND status = 'PAID'");
            if (payrollPaidAmount.next()) {
                int p = payrollPaidAmount.getInt(1);

                lblHomePayrollPaidAmount.setText("Payroll Expenses : $ " + p);

            }

            ResultSet payrollUnpaidAmount = DB.DB.getData("SELECT SUM(payment) FROM payroll WHERE month = '" + month + "' AND year = '" + year + "' AND status = 'UNPAID'");
            if (payrollUnpaidAmount.next()) {
                int u = payrollUnpaidAmount.getInt(1);

                if (u != 0) {
                    lblHomePayrollUnpaidAmount.setForeground(new Color(255, 0, 51));
                } else {
                    lblHomePayrollUnpaidAmount.setForeground(new Color(180, 180, 180));
                }
                lblHomePayrollUnpaidAmount.setText("Payroll Due : $ " + u);

            }

        } catch (Exception e) {
        }
    }

    private void searchPaymentSummary() {
        switch (cmbSortEmployeePayroll.getSelectedIndex()) {
            case 0:

                Date DateMonth = new Date();
                SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
                month = "" + toMonth.format(DateMonth);

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE month = '" + month + "'  AND year = '" + year + "' AND id like '" + txtSearchPayrollSummary.getText() + "%' order by id ");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(1));
                        String b = (rs.getString(2));
                        String c = (rs.getString(3));
                        String d = (rs.getString(4));
                        String e = (rs.getString(5));
                        String f = (rs.getString(6));
                        String g = (rs.getString(7));
                        String h = (rs.getString(8));
                        dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                    }

                } catch (Exception e) {
                }
                break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE month = '" + month + "'  AND year = '" + year + "' AND username like '" + txtSearchPayrollSummary.getText() + "%' order by username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE month = '" + month + "'  AND year = '" + year + "' AND month like '" + txtSearchPayrollSummary.getText() + "%' order by month ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 3:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE month = '" + month + "' AND year = '" + year + "' AND year like '" + txtSearchPayrollSummary.getText() + "%' order by year ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 4:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE month = '" + month + "'  AND year = '" + year + "' AND date like '" + txtSearchPayrollSummary.getText() + "%' order by date ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 5:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE month = '" + month + "' AND  AND year = '" + year + "' attendance like '" + txtSearchPayrollSummary.getText() + "%' order by attendance ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 6:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE month = '" + month + "'  AND year = '" + year + "' AND payment like '" + txtSearchPayrollSummary.getText() + "%' order by payment ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 7:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE month = '" + month + "'  AND year = '" + year + "' AND status = 'PAID' AND id like '" + txtSearchPayrollSummary.getText() + "%' order by status ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 8:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE month = '" + month + "'  AND year = '" + year + "' AND status = 'UNPAID' AND id like '" + txtSearchPayrollSummary.getText() + "%' order by status ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            default:
                break;
        }
    }

    private void payEmployee() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to settle payment of this Employee?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSelectedPayrollEmployeeExtraSalary.getText().equals("")
                    & !txtSelectedPayrollEmployeePayment.getText().equals("")) {

                if ("PAID".equals(txtSelectedPayrollEmployeeStatus.getText())) {
                    JOptionPane.showMessageDialog(this, "Already Paid!", "Invalid Payroll", JOptionPane.WARNING_MESSAGE);

                } else if (!Pattern.matches("^[0-9]+$", txtSelectedPayrollEmployeeExtraSalary.getText())) {
                    lblSelectedEmployeeExtraSalaryValidation.setText("Please Input a Valid Phone Number!");
                    JOptionPane.showMessageDialog(this, "Invalid Extra Salary!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    txtSelectedPayrollEmployeeExtraSalary.grabFocus();
                    txtSelectedPayrollEmployeeExtraSalary.setText(null);

                } else {

                    try {

                        Date DateDate = new Date();
                        SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                        date = "" + toDate.format(DateDate);

                        DB.DB.putData("UPDATE payroll SET date = '" + date + "', payment ='" + txtSelectedPayrollEmployeePayment.getText() + "', status = 'PAID' WHERE id ='" + txtSelectedPayrollEmployeeId.getText() + "' ");
                        String employeePayrollActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Employee Payroll','Admin paid employee " + txtSelectedPayrollEmployeeUsername.getText() + "','SUCCESS')";
                        DB.DB.putData(employeePayrollActivityLog);
                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Employee Payment Settled!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                        txtSelectedPayrollEmployeeStatus.setText("PAID");
                        DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayroll.getModel();
                        dtm.setRowCount(0);
                        refreshEmployeePayrollTable();

                        refreshEmployeePayrollHistoryTable();

                    } catch (Exception e) {
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void refreshEmployeePayrollHistoryTable() {
        try {

            Date DateMonth = new Date();
            SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
            month = "" + toMonth.format(DateMonth);

            DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE status = 'PAID' order by date desc ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(6));
                String g = (rs.getString(7));
                String h = (rs.getString(8));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
            }

        } catch (Exception e) {
        }
    }

    private void searchPaymentHistory() {
        switch (cmbSortEmployeePayrollHistory.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE status = 'PAID' AND date like '" + txtSearchPayrollHistory.getText() + "%' order by date desc ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE status = 'PAID' AND id like '" + txtSearchPayrollHistory.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE status = 'PAID' AND username like '" + txtSearchPayrollHistory.getText() + "%' order by username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 3:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE status = 'PAID' AND month like '" + txtSearchPayrollHistory.getText() + "%' order by month ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 4:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE status = 'PAID' AND year like '" + txtSearchPayrollHistory.getText() + "%' order by year ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 5:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE status = 'PAID' AND date like '" + txtSearchPayrollHistory.getText() + "%' order by date ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 6:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE status = 'PAID' AND attendance like '" + txtSearchPayrollHistory.getText() + "%' order by attendance ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 7:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblEmployeePayrollHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM payroll WHERE status = 'PAID' AND payment like '" + txtSearchPayrollHistory.getText() + "%' order by payment ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    String g = (rs.getString(7));
                    String h = (rs.getString(8));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    public void printAllEmployee() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewEmployeeReport.jrxml";
                    
                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {
                }
            }
        }.start();
    }

    public void printSelectedEmployee() {
        HashMap a = new HashMap();
        a.put("id", txtSelectedEmployeeId.getText());

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewSelectedEmployeeReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    public void printSelectedEmployeePaymentBill() {

        Date DateMonth = new Date();
        SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
        month = "" + toMonth.format(DateMonth);

        Date DateYear = new Date();
        SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
        year = "" + toYear.format(DateYear);

        HashMap a = new HashMap();
        a.put("id", txtSelectedPayrollEmployeeId.getText());
        a.put("month", month);
        a.put("year", year);

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewSelectedEmployeePaymentBill.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    private void addNewSupplier() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Supplier?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            if (!txtSupplierUsername.getText().equals("")
                    & !txtSupplierAddress.getText().equals("")
                    & !txtSupplierEmail.getText().equals("")
                    & (!txtSupplierPhoneNo.getText().equals("")
                    & (!txtSupplierCompany.getText().equals("")))) {

                try {
                    ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE username like '" + txtSupplierUsername.getText() + "%' order by id ");

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Username Already Exists!", "Invalid Username", JOptionPane.WARNING_MESSAGE);

                    } else if (txtSupplierPhoneNo.getText().length() != 10) {
                        JOptionPane.showMessageDialog(this, "Invalid Phone Number Length!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                        txtSupplierPhoneNo.setText(null);
                        txtSupplierPhoneNo.grabFocus();

                    } else if (!Pattern.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", txtSupplierEmail.getText())) {
                        JOptionPane.showMessageDialog(this, "Invalid Email!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                        txtSupplierEmail.setText(null);
                        txtSupplierEmail.grabFocus();
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

                        String addSupplier = "INSERT INTO supplier(username, address, email, phone_no, company, account_status) VALUES ('" + txtSupplierUsername.getText().trim() + "','" + txtSupplierAddress.getText() + "','" + txtSupplierEmail.getText() + "','" + txtSupplierPhoneNo.getText() + "','" + txtSupplierCompany.getText() + "', 'Active')";
                        String AddSupplierActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Add New Supplier','Admin added new supplier " + txtSupplierUsername.getText() + "','SUCCESS')";
                        try {
                            DB.DB.putData(addSupplier);
                            DB.DB.putData(AddSupplierActivityLog);

                            DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                            dtm.setRowCount(0);
                            refreshSupplierTable();
                            fillCmbSuppliers();

                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Supplier Added!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            txtSupplierUsername.setText(null);
                            txtSupplierAddress.setText(null);
                            txtSupplierEmail.setText(null);
                            txtSupplierPhoneNo.setText(null);
                            txtSupplierCompany.setText(null);

                            lblSupplierUserameValidation.setText(null);
                            lblSupplierAddressValidation.setText(null);
                            lblSupplierEmailValidation.setText(null);
                            lblSupplierPhoneNoValidation.setText(null);
                            lblSupplierCompanyValidation.setText(null);

                            txtSupplierUsername.setBorder(new FlatBorder());
                            txtSupplierAddress.setBorder(new FlatBorder());
                            txtSupplierEmail.setBorder(new FlatBorder());
                            txtSupplierPhoneNo.setBorder(new FlatBorder());
                            txtSupplierCompany.setBorder(new FlatBorder());

                        } catch (Exception e) {
                        }

                    }
                } catch (Exception ex) {
                    Logger.getLogger(AdminDashboard.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void refreshSupplierTable() {

        try {

            DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE account_status = 'Active'");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(6));
                dtm.addRow(new Object[]{a, b, c, d, e, f});
            }

            int SupplierCount = tblSupplier.getRowCount();
            lblSupplierCount.setText("Supplier Count : " + SupplierCount);

            ResultSet supplierCount = DB.DB.getData("SELECT count(id) FROM supplier WHERE account_status = 'Active'");
            if (supplierCount.next()) {
                int e = supplierCount.getInt(1);
                lblHomeSupplierCount.setText("Supplier Count : " + e);
            }

        } catch (Exception e) {
        }
    }

    private void searchSupplier() {
        switch (cmbSortSupplier.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE account_status = 'Active' AND id like '" + txtSearchSupplier.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE account_status = 'Active' AND username like '" + txtSearchSupplier.getText() + "%' order by username ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }

            } catch (Exception e) {
            }
            break;
            case 2:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE account_status = 'Active' AND address like '" + txtSearchSupplier.getText() + "%' order by address ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
            } catch (Exception e) {
            }
            break;
            case 3:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE account_status = 'Active' AND email like '" + txtSearchSupplier.getText() + "%' order by email ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
            } catch (Exception e) {
            }
            break;
            case 4:
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE account_status = 'Active' AND phone_no like '" + txtSearchSupplier.getText() + "%' order by phone_no ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
            } catch (Exception e) {
            }
            break;
            case 5:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE account_status = 'Active' AND company like '" + txtSearchSupplier.getText() + "%' order by company ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(2));
                    String c = (rs.getString(3));
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(6));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
            } catch (Exception e) {
            }
            default:
                break;
        }
    }

    private void deleteSupplier() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this Supplier?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            try {

                DB.DB.putData("UPDATE supplier SET account_status = 'Dective' WHERE id ='" + txtSelectedSupplierId.getText() + "' ");
                String deleteSupplierActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Delete Supplier','Admin deleted supplier " + txtSelectedSupplierUsername.getText() + "','SUCCESS')";
                DB.DB.putData(deleteSupplierActivityLog);

                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                JOptionPane.showMessageDialog(this, "Supplier Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                txtSearchSupplier.setText(null);
                txtSelectedSupplierId.setText(null);
                txtSelectedSupplierUsername.setText(null);
                txtSelectedSupplierAddress.setText(null);
                txtSelectedSupplierEmail.setText(null);
                txtSelectedSupplierPhoneNo.setText(null);
                txtSelectedSupplierCompany.setText(null);

                refreshSupplierTable();
                fillCmbSuppliers();

            } catch (Exception e) {
            }
        } else {

        }
    }

    private void updateSupplier() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Update this Supplier?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSelectedSupplierUsername.getText().equals("")
                    & !txtSelectedSupplierAddress.getText().equals("")
                    & !txtSelectedSupplierEmail.getText().equals("")
                    & !txtSelectedSupplierPhoneNo.getText().equals("")
                    & !txtSelectedSupplierCompany.getText().equals("")) {

                if (!(txtSelectedSupplierPhoneNo.getText().length() == 10)) {
                    JOptionPane.showMessageDialog(this, "Invalid Phone Number!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    txtSelectedSupplierPhoneNo.setText(null);
                    txtSelectedSupplierPhoneNo.grabFocus();

                } else if (!Pattern.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", txtSelectedSupplierEmail.getText())) {
                    JOptionPane.showMessageDialog(this, "Invalid E-mail!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    txtSelectedSupplierEmail.setText(null);
                    txtSelectedSupplierEmail.grabFocus();

                } else {

                    try {
                        DB.DB.putData("UPDATE supplier SET address ='" + txtSelectedSupplierAddress.getText() + "', email ='" + txtSelectedSupplierEmail.getText() + "', phone_no= '" + txtSelectedSupplierPhoneNo.getText() + "', company ='" + txtSelectedSupplierCompany.getText() + "' where id ='" + txtSelectedSupplierId.getText() + "' ");
                        String updateSupplierActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Update Supplier','Admin updated supplier " + txtSelectedSupplierUsername.getText() + "','SUCCESS')";
                        DB.DB.putData(updateSupplierActivityLog);
                        ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                        JOptionPane.showMessageDialog(this, "Supplier Updated Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                        txtSelectedSupplierId.setText("");
                        txtSelectedSupplierUsername.setText("");
                        txtSelectedSupplierAddress.setText("");
                        txtSelectedSupplierEmail.setText("");
                        txtSelectedSupplierPhoneNo.setText("");
                        txtSelectedSupplierCompany.setText("");

                        DefaultTableModel dtm = (DefaultTableModel) tblSupplier.getModel();
                        dtm.setRowCount(0);
                        refreshSupplierTable();
                    } catch (Exception e) {
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public void printAllSupplier() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewSupplierReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    public void printSelectedSupplier() {
        HashMap a = new HashMap();
        a.put("id", txtSelectedSupplierId.getText());

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewSelectedSupplierReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    private void calculateSupplierItemsCount() {
        int count = tblSupplierItem.getRowCount();
        lblItemCount.setText("Item Count : " + Integer.toString(count));
    }

    private void refreshSupplierItemTable() {

        try {

            DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE item_status = 'Available' order by supplier_id");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(6));
                String b = (rs.getString(5));
                String c = (rs.getString(2));
                String d = (rs.getString(3));
                String e = (rs.getString(4));
                String f = (rs.getString(7));
                dtm.addRow(new Object[]{a, b, c, d, e, f});
            }

            calculateSupplierItemsCount();

        } catch (Exception e) {
        }
    }

    private void updateSupplierItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Update this Item?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtSelectedItemSupplierItemName.getText().equals("")
                    & !txtSelectedItemSupplierItemCode.getText().equals("")) {

                try {

                    ResultSet stockAvailabilityChecking = DB.DB.getData("SELECT id FROM item WHERE item_name = '" + txtSelectedItemSupplierItemName.getText() + "' AND item_status = 'Unavailable'");
                    if (stockAvailabilityChecking.next()) {

                        ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE item_name = '" + txtSelectedItemSupplierItemName.getText() + "' AND id != '" + itemId + "' ");
                        ResultSet rs1 = DB.DB.getData("SELECT * FROM item WHERE item_code = '" + txtSelectedItemSupplierItemCode.getText() + "' AND id != '" + itemId + "' ");

                        if (rs.next()) {
                            JOptionPane.showMessageDialog(this, "Item Name Already Used!", "Invalid Item Name", JOptionPane.WARNING_MESSAGE);
                        }

                        if (rs1.next()) {
                            JOptionPane.showMessageDialog(this, "Item Code Already Used!", "Invalid Item Code", JOptionPane.WARNING_MESSAGE);

                        } else {

                            try {

                                DB.DB.putData("UPDATE item SET item_name ='" + txtSelectedItemSupplierItemName.getText() + "', item_code ='" + txtSelectedItemSupplierItemCode.getText() + "', item_category ='" + cmbSelectedItemSupplierItemCategory.getSelectedItem().toString() + "' WHERE id = '" + itemId + "'");
                                String updateEmployeeActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Update Supplier Item','Admin updated supplier item " + txtSelectedItemSupplierItemName.getText() + "','SUCCESS')";
                                DB.DB.putData(updateEmployeeActivityLog);

                                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                                JOptionPane.showMessageDialog(this, "Item Updated Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                                txtSelectedItemSupplierId.setText("");
                                txtSelectedItemSupplierUsername.setText("");
                                txtSelectedItemSupplierItemName.setText("");
                                txtSelectedItemSupplierItemCode.setText("");
                                cmbSelectedItemSupplierItemCategory.setSelectedIndex(0);
                                refreshSupplierItemTable();

                            } catch (Exception e) {
                            }
                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "Available Items Cannot be Modified!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    }

                } catch (Exception ex) {
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void deleteSupplierItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this Item?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            try {

                ResultSet stockAvailabilityChecking = DB.DB.getData("SELECT id FROM item WHERE item_name = '" + txtSelectedItemSupplierItemName.getText() + "' AND item_status = 'Unavailable'");

                if (stockAvailabilityChecking.next()) {

                    ResultSet rs = DB.DB.getData("SELECT id FROM item WHERE item_name = '" + txtSelectedItemSupplierItemName.getText() + "'");
                    if (rs.next()) {
                        String i = rs.getString(1);
                        String supplierItemId = i;

                        DB.DB.putData("Delete FROM item WHERE id = '" + supplierItemId + "' AND item_status = 'Unavailable' ");      //REMOVE THIS FUNCTION!!!!!
                    }

                    String deleteSupplierItemLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Delete Supplier Item','Admin deleted Item " + txtSelectedItemSupplierItemName.getText() + "','SUCCESS')";
                    DB.DB.putData(deleteSupplierItemLog);

                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "Item Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                    refreshSupplierItemTable();
                    fillCmbItems();
                    calculateSupplierItemsCount();
                    refreshStockTable();

                } else {
                    JOptionPane.showMessageDialog(this, "Available Items Cannot be Modified!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                }

            } catch (Exception e) {
            }
        } else {

        }
    }

    private void searchSupplierItem() {
        switch (cmbSortSupplierItem.getSelectedIndex()) {
            case 0:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE item_status = 'Available' AND supplier_id = '" + txtSearchSupplierItem.getText() + "' order by supplier_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();

            } catch (Exception e) {
            }
            break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE item_status = 'Available' AND  supplier like '" + txtSearchSupplierItem.getText() + "%' order by supplier ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();

            } catch (Exception e) {
            }
            break;
            case 2:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE item_status = 'Available' AND  item_name like '" + txtSearchSupplierItem.getText() + "%' order by item_name ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();
            } catch (Exception e) {
            }
            break;
            case 3:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE item_status = 'Available' AND item_code like '" + txtSearchSupplierItem.getText() + "%' order by item_code ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();
            } catch (Exception e) {
            }
            break;
            case 4:
                
                try {

                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE item_status = 'Available' AND item_category like '" + txtSearchSupplierItem.getText() + "%' order by item_category ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();
            } catch (Exception e) {
            }
            break;
            case 5:                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE item_status = 'Available' AND  id like '" + txtSearchSupplierItem.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();
            } catch (Exception e) {
            }
            break;
            case 6:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE item_status = 'Unavailable' AND  id like '" + txtSearchSupplierItem.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();
            } catch (Exception e) {
            }
            break;
            case 7:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSupplierItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE id like '" + txtSearchSupplierItem.getText() + "%' order by id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(6));
                    String b = (rs.getString(5));
                    String c = (rs.getString(2));
                    String d = (rs.getString(3));
                    String e = (rs.getString(4));
                    String f = (rs.getString(7));
                    dtm.addRow(new Object[]{a, b, c, d, e, f});
                }
                calculateSupplierItemsCount();
            } catch (Exception e) {
            }
            default:
                break;
        }
    }

    private void generateOPId() {
        try {

            ResultSet rs = DB.DB.getData("SELECT po_id AS x FROM purchase_order ORDER BY po_id DESC LIMIT 1");
            if (rs.next()) {

                int rowcount = Integer.parseInt(rs.getString("x"));
                rowcount++;

                this.txtPOId.setText("" + rowcount);
                this.txtPOId1.setText("" + rowcount);

                txtSearchPOItem.setText(txtPOId.getText());
                txtSearchPOItem1.setText(txtPOId1.getText());

                txtSearchGRNItem.setText(txtPOId.getText());
                txtSearchGRNItem1.setText(txtPOId1.getText());

                lblPOId.setText(txtSearchPOItem.getText());
                lblPOId2.setText(txtSearchPOItem1.getText());

                txtSearchGRNItem.setText(txtPOId.getText());
                txtSearchGRNItem1.setText(txtPOId1.getText());

                lblGRNId.setText(txtSearchGRNItem.getText());
                lblGRNId1.setText(txtSearchGRNItem1.getText());

            }

        } catch (Exception e) {
        }
    }

    private void calculatePOTotal() {
        try {

            ResultSet poTotal = DB.DB.getData("SELECT sum(total) FROM purchase_order WHERE po_id = '" + txtSearchPOItem.getText() + "'");
            ResultSet poTotal1 = DB.DB.getData("SELECT sum(total) FROM purchase_order WHERE po_id = '" + txtSearchPOItem1.getText() + "'");

            if (poTotal.next()) {
                Double d = poTotal.getDouble(1);
                lblTotal.setText("$ " + Double.toString(d));
            }

            if (poTotal1.next()) {
                Double d = poTotal1.getDouble(1);
                lblTotal3.setText("$ " + Double.toString(d));
            }

        } catch (Exception e) {
        }
    }

    private void calculateGRNTotal() {
        try {

            ResultSet poTotal = DB.DB.getData("SELECT sum(total) FROM grn WHERE grn_status = 'Checked' AND grn_id = '" + txtSearchGRNItem.getText() + "'");

            if (poTotal.next()) {
                Double d = poTotal.getDouble(1);
                lblGRNTotal.setText("$ " + Double.toString(d));
            }

        } catch (Exception e) {
        }
    }

    private void calculateGRNTotal1() {
        try {

            ResultSet poTotal = DB.DB.getData("SELECT sum(total) FROM grn WHERE grn_status = 'Checked' AND grn_id = '" + txtSearchGRNItem1.getText() + "'");

            if (poTotal.next()) {
                Double d = poTotal.getDouble(1);
                lblGRNTotal1.setText("$ " + Double.toString(d));
            }

        } catch (Exception e) {
        }
    }

    private void fillCmbSuppliers() {
        try {

            cmbPOItemSupplier.removeAllItems();
            cmbPOItemSupplier1.removeAllItems();
            cmbItemSupplier.removeAllItems();
            ResultSet rs = DB.DB.getData("SELECT username FROM supplier WHERE account_status = 'Active' ");
            while (rs.next()) {
                cmbPOItemSupplier.addItem(rs.getString(1));
                cmbPOItemSupplier1.addItem(rs.getString(1));
                cmbItemSupplier.addItem(rs.getString(1));
            }

        } catch (Exception e) {
        }
    }

    private void fillCmbItems() {
        try {

            cmbPOItemName.removeAllItems();
            ResultSet rs = DB.DB.getData("SELECT item_name FROM item WHERE item_status = 'Available' ");
            while (rs.next()) {
                cmbPOItemName.addItem(rs.getString(1));
            }
            DB.DB.con.close();

        } catch (Exception e) {
        }
    }

    private void addItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Item to the System?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
            if (!txtItemName.getText().equals("")
                    & (!txtItemCode.getText().equals(""))) {

                try {
                    ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE item_name like '" + txtItemName.getText() + "'");

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Item Name Already Exists!", "Invalid Item Name", JOptionPane.WARNING_MESSAGE);
                    }

                    ResultSet rs1 = DB.DB.getData("SELECT * FROM item WHERE item_code like '" + txtItemCode.getText() + "'");
                    if (rs1.next()) {
                        JOptionPane.showMessageDialog(this, "Item Code Already Exists!", "Invalid Item Code", JOptionPane.WARNING_MESSAGE);

                    } else {

                        String addItem = "INSERT INTO item(item_name, item_code, item_category, supplier, supplier_id, item_status) VALUES ('" + txtItemName.getText() + "','" + txtItemCode.getText() + "','" + cmbItemCategory.getSelectedItem().toString() + "','" + cmbItemSupplier.getSelectedItem().toString() + "',(SELECT id FROM supplier WHERE username= '" + cmbItemSupplier.getSelectedItem().toString() + "' ),'Unavailable')";
                        String AddItemActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Add Item to Supplier Item Collection','Admin added new Item " + txtItemName.getText() + "','SUCCESS')";
                        try {
                            DB.DB.putData(addItem);
                            fillCmbItems();
                            refreshSupplierItemTable();
                            DB.DB.putData(AddItemActivityLog);
                            refreshSupplierItemTable();
                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Item Added Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            txtItemName.setText(null);
                            txtItemCode.setText(null);
                            cmbItemCategory.setSelectedIndex(0);
                            cmbItemSupplier.setSelectedIndex(0);
                            lblItemNameValidation.setText(null);
                            lblItemCodeValidation.setText(null);
                            txtItemName.setBorder(new FlatBorder());
                            txtItemCode.setBorder(new FlatBorder());
                        } catch (Exception e) {
                        }

                    }
                } catch (Exception ex) {
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void addPOItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Item to PO List?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            if (!txtPOItemQuantity.getText().equals("")
                    & (!(cmbPOItemName.getItemCount() == 0))
                    & (!txtPOItemPrice.getText().equals(""))
                    & (!txtPOItemQuantity.getText().equals(""))) {

                try {

                    ResultSet rs = DB.DB.getData("SELECT item_name FROM purchase_order WHERE item_name = '" + cmbPOItemName.getSelectedItem().toString() + "' AND po_id = '" + txtPOId.getText() + "' ");

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Item Already Added to PO Bill!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    } else {

                        switch (cmbPOLeadTime.getSelectedIndex()) {
                            case 0: {
                                LocalDate localDate = LocalDate.now().plusWeeks(1);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            case 1: {
                                LocalDate localDate = LocalDate.now().plusWeeks(2);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            case 2: {
                                LocalDate localDate = LocalDate.now().plusWeeks(3);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            case 3: {
                                LocalDate localDate = LocalDate.now().plusMonths(1);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            default:
                                break;
                        }

                        int total = Integer.parseInt(txtPOItemQuantity.getText()) * Integer.parseInt(txtPOItemPrice.getText());

                        String addPOItem = "INSERT INTO purchase_order(po_id, item_name, item_code, quantity, item_price, supplier, total, lead_time) VALUES ('" + txtPOId.getText() + "','" + cmbPOItemName.getSelectedItem().toString() + "',(SELECT item_code FROM item WHERE item_name = '" + cmbPOItemName.getSelectedItem().toString() + "'),'" + txtPOItemQuantity.getText() + "','" + txtPOItemPrice.getText() + "' ,'" + cmbPOItemSupplier.getSelectedItem().toString() + "','" + total + "','" + leadTime + "') ";
                        String addGRNItem = "INSERT INTO grn(grn_id, item_name, item_code, quantity, item_price, supplier, total, lead_time, grn_status) VALUES ('" + txtPOId.getText() + "','" + cmbPOItemName.getSelectedItem().toString() + "',(SELECT item_code FROM item WHERE item_name = '" + cmbPOItemName.getSelectedItem().toString() + "'),'" + txtPOItemQuantity.getText() + "','" + txtPOItemPrice.getText() + "' ,'" + cmbPOItemSupplier.getSelectedItem().toString() + "','" + total + "','" + leadTime + "', 'Unchecked') ";

                        try {
                            DB.DB.putData(addPOItem);
                            DB.DB.putData(addGRNItem);

                            refreshPOTable();
                            refreshPOTable1();

                            calculatePOTotal();

                            calculateGRNTotal();
                            calculateGRNTotal1();

                            refreshGRNTable();
                            refreshGRNTable1();

                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Item Added to Purchase Order List!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            txtPOItemQuantity.setText(null);
                            txtPOItemPrice.setText(null);
                            cmbPOItemName.setSelectedItem(null);
                            cmbPOLeadTime.setSelectedIndex(0);
                            lblPOItemCodeValidation.setText(null);
                            lblPOItemNameValidation.setText(null);
                            txtPOItemQuantity.setBorder(new FlatBorder());
                            txtPOItemPrice.setBorder(new FlatBorder());

                        } catch (Exception e) {
                        }

                    }
                } catch (Exception ex) {
                    Logger.getLogger(AdminDashboard.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void addPOItem1() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Item to PO List?", "", JOptionPane.YES_NO_OPTION);

        if (option == 0) {

            if (!txtPOItemQuantity1.getText().equals("")
                    & (!(cmbPOItemName1.getItemCount() == 0))
                    & (!txtPOItemPrice1.getText().equals(""))
                    & (!txtPOItemQuantity1.getText().equals(""))) {

                try {

                    ResultSet rs = DB.DB.getData("SELECT item_name FROM purchase_order WHERE item_name = '" + cmbPOItemName1.getSelectedItem().toString() + "' AND po_id = '" + txtPOId1.getText() + "' ");

                    if (rs.next()) {
                        JOptionPane.showMessageDialog(this, "Item Already Added to PO Bill!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    } else {
                        switch (cmbPOLeadTime1.getSelectedIndex()) {
                            case 0: {
                                LocalDate localDate = LocalDate.now().plusWeeks(1);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            case 1: {
                                LocalDate localDate = LocalDate.now().plusWeeks(2);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            case 2: {
                                LocalDate localDate = LocalDate.now().plusWeeks(3);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            case 3: {
                                LocalDate localDate = LocalDate.now().plusMonths(1);
                                ZoneId defaultZoneId = ZoneId.systemDefault();
                                Date leadTimeSelection = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
                                SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                                date = "" + toDate.format(leadTimeSelection);
                                leadTime = date;
                                break;
                            }
                            default:
                                break;
                        }

                        int total = Integer.parseInt(txtPOItemQuantity1.getText()) * Integer.parseInt(txtPOItemPrice1.getText());

                        String addPOItem = "INSERT INTO purchase_order(po_id, item_name, item_code, quantity, item_price, supplier, total, lead_time) VALUES ('" + txtPOId1.getText() + "','" + cmbPOItemName1.getSelectedItem().toString() + "',(SELECT item_code FROM item WHERE item_name = '" + cmbPOItemName1.getSelectedItem().toString() + "'),'" + txtPOItemQuantity1.getText() + "','" + txtPOItemPrice1.getText() + "' ,'" + cmbPOItemSupplier1.getSelectedItem().toString() + "','" + total + "','" + leadTime + "') ";
                        String addGRNItem = "INSERT INTO grn(grn_id, item_name, item_code, quantity, item_price, supplier, total, lead_time, grn_status) VALUES ('" + txtPOId1.getText() + "','" + cmbPOItemName1.getSelectedItem().toString() + "',(SELECT item_code FROM item WHERE item_name = '" + cmbPOItemName1.getSelectedItem().toString() + "'),'" + txtPOItemQuantity1.getText() + "','" + txtPOItemPrice1.getText() + "' ,'" + cmbPOItemSupplier1.getSelectedItem().toString() + "','" + total + "','" + leadTime + "', 'Unchecked') ";

                        try {

                            DB.DB.putData(addPOItem);
                            DB.DB.putData(addGRNItem);
                            refreshPOTable();
                            refreshPOTable1();

                            calculatePOTotal();

                            calculateGRNTotal();
                            calculateGRNTotal1();

                            refreshGRNTable();
                            refreshGRNTable1();

                            ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                            JOptionPane.showMessageDialog(this, "Item Added to Purchase Order List!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                            txtPOItemQuantity1.setText(null);
                            txtPOItemPrice1.setText(null);
                            cmbPOItemName1.setSelectedItem(null);
                            cmbPOLeadTime1.setSelectedIndex(0);
                            lblPOItemNameValidation1.setText(null);
                            lblPOItemCodeValidation1.setText(null);
                            txtPOItemQuantity1.setBorder(new FlatBorder());
                            txtPOItemPrice1.setBorder(new FlatBorder());

                        } catch (Exception e) {
                        }

                    }
                } catch (Exception ex) {
                    Logger.getLogger(AdminDashboard.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void deletePOItem() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this PO Item?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            try {

                if (txtPOId.getText().equals(lblPOId.getText())) {

                    DB.DB.putData("DELETE FROM purchase_order WHERE id ='" + selectedPOItemId + "' AND po_id = '" + txtPOId.getText() + "' ");
                    DB.DB.putData("DELETE FROM grn WHERE id ='" + selectedPOItemId + "' AND grn_id = '" + txtPOId.getText() + "' ");

                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "PO Item Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                } else {
                    JOptionPane.showMessageDialog(this, "This PO Item has Already Ordered, Cannot be Deleted!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                }

                refreshPOTable();
                refreshPOTable1();

                refreshGRNTable();
                refreshGRNTable1();

                calculatePOTotal();
                calculateGRNTotal();

                calculateGRNTotal1();

            } catch (Exception e) {
            }

        } else {
        }
    }

    private void deletePOItem1() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Delete this PO Item?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            try {

                if (txtPOId1.getText().equals(lblPOId2.getText())) {

                    DB.DB.putData("DELETE FROM purchase_order WHERE id ='" + selectedPOItemId1 + "' AND po_id = '" + txtPOId1.getText() + "' ");
                    DB.DB.putData("DELETE FROM grn WHERE id ='" + selectedPOItemId1 + "' AND grn_id = '" + txtPOId1.getText() + "' ");

                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "PO Item Deleted!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                } else {
                    JOptionPane.showMessageDialog(this, "This PO Item has Already Ordered, Cannot be Deleted!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                }

                refreshPOTable();
                refreshPOTable1();

                refreshGRNTable();
                refreshGRNTable1();

                calculatePOTotal();
                calculateGRNTotal();

                calculateGRNTotal1();

            } catch (Exception e) {
            }
        } else {

        }
    }

    private void refreshPOTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblPOItem.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE po_id = '" + txtSearchPOItem.getText() + "' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(3));
                String c = (rs.getString(4));
                String d = (rs.getString(5));
                String e = (rs.getString(6));
                String f = (rs.getString(7));
                String g = (rs.getString(8));
                String h = (rs.getString(9));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
            }

            calculatePOTotal();

            ResultSet pendingPOCount = DB.DB.getData("SELECT count(grn_id) FROM grn WHERE grn_status = 'unchecked'");
            if (pendingPOCount.next()) {
                int pndngPOCnt = pendingPOCount.getInt(1);

                if (pndngPOCnt > 0) {
                    lblHomePendingPOCount.setForeground(new Color(255, 0, 51));
                } else {
                    lblHomePendingPOCount.setForeground(new Color(180, 180, 180));
                }
                lblHomePendingPOCount.setText("Pending PO Count : " + pndngPOCnt);

            }

        } catch (Exception e) {
        }
    }

    private void refreshPOTable1() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblPOItem1.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE po_id = '" + txtSearchPOItem1.getText() + "' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(3));
                String c = (rs.getString(4));
                String d = (rs.getString(5));
                String e = (rs.getString(6));
                String f = (rs.getString(7));
                String g = (rs.getString(8));
                String h = (rs.getString(9));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
            }
            calculatePOTotal();

        } catch (Exception e) {
        }
    }

    private void searchPOItem() {
        switch (cmbSortPOItem.getSelectedIndex()) {
            case 0:

                calculatePOTotal();

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblPOItem.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE po_id = '" + txtSearchPOItem.getText() + "%' order by po_id ");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(1));
                        String b = (rs.getString(3));
                        String c = (rs.getString(4));
                        String d = (rs.getString(5));
                        String e = (rs.getString(6));
                        String f = (rs.getString(7));
                        String g = (rs.getString(8));
                        String h = (rs.getString(9));
                        dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                    }

                } catch (Exception e) {
                }
                break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblPOItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE item_name like '" + txtSearchPOItem.getText() + "%' order by item_name ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }

            } catch (Exception e) {
            }
            break;
            case 2:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblPOItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE item_code like '" + txtSearchPOItem.getText() + "%' order by item_code ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 3:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblPOItem.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE supplier like '" + txtSearchPOItem.getText() + "%' order by supplier ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            default:
                break;
        }
    }

    private void searchPOItem1() {
        switch (cmbSortPOItem1.getSelectedIndex()) {
            case 0:
                calculatePOTotal();

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblPOItem1.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE po_id = '" + txtSearchPOItem1.getText() + "%' order by po_id ");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(1));
                        String b = (rs.getString(3));
                        String c = (rs.getString(4));
                        String d = (rs.getString(5));
                        String e = (rs.getString(6));
                        String f = (rs.getString(7));
                        String g = (rs.getString(8));
                        String h = (rs.getString(9));
                        dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                    }

                } catch (Exception e) {
                }
                break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblPOItem1.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE item_name like '" + txtSearchPOItem1.getText() + "%' order by item_name ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }

            } catch (Exception e) {
            }
            break;
            case 2:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblPOItem1.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE item_code like '" + txtSearchPOItem1.getText() + "%' order by item_code ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            break;
            case 3:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblPOItem1.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE supplier like '" + txtSearchPOItem1.getText() + "%' order by supplier ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
            } catch (Exception e) {
            }
            default:
                break;
        }
    }

    private void purchaseOrder() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Save current PO Bill?", "", JOptionPane.YES_NO_OPTION);

        if (option == 0) {

            if (txtPOId.getText().equals(lblPOId.getText())) {

                String PurchaseOrderLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Purchase Order','Admin Ordered PO Bill No. " + txtSearchPOItem.getText() + "','SUCCESS')";
                try {
                    DB.DB.putData(PurchaseOrderLog);
                } catch (Exception e) {
                }

                generateOPId();

                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                JOptionPane.showMessageDialog(this, "PO Bill Saved Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                refreshPOTable();
                refreshPOTable1();

                refreshGRNTable();
                refreshGRNTable1();

            } else {
                JOptionPane.showMessageDialog(this, "This PO Bill has Already Saved, Cannot be Modified!!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void purchaseOrder1() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Save current PO Bill?", "", JOptionPane.YES_NO_OPTION);

        if (option == 0) {

            if (txtPOId1.getText().equals(lblPOId2.getText())) {

                String PurchaseOrderLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Purchase Order','Admin Ordered PO Bill No. " + txtSearchPOItem1.getText() + "','SUCCESS')";
                try {
                    DB.DB.putData(PurchaseOrderLog);
                } catch (Exception e) {
                }

                generateOPId();

                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                JOptionPane.showMessageDialog(this, "PO Bill Saved Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                refreshPOTable();
                refreshPOTable1();

                refreshGRNTable();
                refreshGRNTable1();

            } else {
                JOptionPane.showMessageDialog(this, "This PO Bill has Already Saved, Cannot be Modified!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
            }

        }
    }

    public void printSelectedPOBill() {
        HashMap a = new HashMap();
        a.put("po_id", txtSearchPOItem.getText());
        a.put("po_total", lblTotal.getText());

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewSelectedPOBill.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {
                }
            }
        }.start();
    }

    public void printSelectedPOBill1() {
        HashMap a = new HashMap();
        a.put("po_id", txtSearchPOItem1.getText());
        a.put("po_total", lblTotal3.getText());

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource ="C:\\Program Files\\Xeon Reports\\reports\\viewSelectedPOBill.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {
                }
            }
        }.start();
    }

    private void refreshGRNTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblGRN.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_id = '" + txtSearchGRNItem.getText() + "' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(3));
                String c = (rs.getString(4));
                String d = (rs.getString(5));
                String e = (rs.getString(6));
                String f = (rs.getString(7));
                String g = (rs.getString(10));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g});
            }

        } catch (Exception e) {
        }

        txtGRNItemName.setText(null);
        txtGRNStatus.setText(null);
        txtGRNItemQuantity.setText(null);
        txtGRNItemPrice.setText(null);
        txtGRNItemSellingPrice.setText(null);
        txtGRNItemLowStock.setText(null);
    }

    private void refreshGRNTable1() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblGRN1.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_id = '" + txtSearchGRNItem1.getText() + "' ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(3));
                String c = (rs.getString(4));
                String d = (rs.getString(5));
                String e = (rs.getString(6));
                String f = (rs.getString(7));
                String g = (rs.getString(10));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g});
            }

        } catch (Exception e) {
        }

        txtGRNItemName1.setText(null);
        txtGRNStatus1.setText(null);
        txtGRNItemQuantity1.setText(null);
        txtGRNItemPrice1.setText(null);
        txtGRNItemSellingPrice1.setText(null);
        txtGRNItemLowStock1.setText(null);
        txtGRNItemStockCount1.setText(null);
    }

    private void searchGRNItem() {
        switch (cmbSortGRNItem.getSelectedIndex()) {
            case 0:
                calculateGRNTotal();

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblGRN.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_id = '" + txtSearchGRNItem.getText() + "%' order by grn_id ");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(1));
                        String b = (rs.getString(3));
                        String c = (rs.getString(4));
                        String d = (rs.getString(5));
                        String e = (rs.getString(6));
                        String f = (rs.getString(7));
                        String g = (rs.getString(10));
                        dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                    }

                } catch (Exception e) {
                }
                break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_id = '" + txtSearchGRNItem.getText() + "%' order by grn_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

            } catch (Exception e) {
            }
            break;
            case 2:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_status = 'Checked' AND grn_id = '" + txtSearchGRNItem.getText() + "' order by grn_status ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            case 3:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_status = 'Unchecked' AND grn_id = '" + txtSearchGRNItem.getText() + "' order by grn_status ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            case 4:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_status = 'Bad Order' AND grn_id = '" + txtSearchGRNItem.getText() + "'  order by grn_status ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            default:
                break;
        }
    }

    private void searchGRNItem1() {
        switch (cmbSortGRNItem1.getSelectedIndex()) {
            case 0:
                calculateGRNTotal1();

                try {
                    DefaultTableModel dtm = (DefaultTableModel) tblGRN1.getModel();
                    ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_id = '" + txtSearchGRNItem1.getText() + "%' order by grn_id ");
                    dtm.setRowCount(0);

                    while (rs.next()) {
                        String a = (rs.getString(1));
                        String b = (rs.getString(3));
                        String c = (rs.getString(4));
                        String d = (rs.getString(5));
                        String e = (rs.getString(6));
                        String f = (rs.getString(7));
                        String g = (rs.getString(10));
                        dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                    }

                } catch (Exception e) {
                }
                break;
            case 1:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN1.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_id = '" + txtSearchGRNItem1.getText() + "%' order by grn_id ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }

            } catch (Exception e) {
            }
            break;
            case 2:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN1.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_status = 'Checked' AND grn_id = '" + txtSearchGRNItem1.getText() + "' order by grn_status ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            case 3:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN1.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_status = 'Unchecked' AND grn_id = '" + txtSearchGRNItem1.getText() + "' order by grn_status ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            break;
            case 4:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblGRN1.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM grn WHERE grn_status = 'Bad Order' AND grn_id = '" + txtSearchGRNItem1.getText() + "'  order by grn_status ");
                dtm.setRowCount(0);

                while (rs.next()) {
                    String a = (rs.getString(1));
                    String b = (rs.getString(3));
                    String c = (rs.getString(4));
                    String d = (rs.getString(5));
                    String e = (rs.getString(6));
                    String f = (rs.getString(7));
                    String g = (rs.getString(10));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g});
                }
            } catch (Exception e) {
            }
            default:
                break;
        }
    }

    private void markAsBadOrder() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to mark this GRN Item as Bad Order?", "", JOptionPane.YES_NO_OPTION);

        if (option == 0) {

            String checkStatus = txtGRNStatus.getText();
            String checkedStatus = "Checked";
            String uncheckedStatus = "Unchecked";

            if (checkStatus.equals(uncheckedStatus)) {
                try {
                    String selectedGRNItemId = ((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 0));

                    DB.DB.putData("UPDATE grn SET grn_status = 'Bad Order' WHERE id = '" + selectedGRNItemId + "'");
                    String markingBadOrderActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Bad Order','Admin marked bad order item on GRN Bill No. " + txtSearchGRNItem.getText() + "','SUCCESS')";
                    DB.DB.putData(markingBadOrderActivityLog);
                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "Bad Order Marked Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                    refreshGRNTable();
                    refreshGRNTable1();

                    calculateGRNTotal();
                    calculateGRNTotal1();

                } catch (Exception e) {
                }

            } else if (checkStatus.equals(checkedStatus)) {
                JOptionPane.showMessageDialog(this, "Checked Items cannot be Marked as Bad Order!");

            } else {
                JOptionPane.showMessageDialog(this, "GRN Item Already marked as Bad Order!");
            }
        }
    }

    private void markAsBadOrder1() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to mark this GRN Item as Bad Order?", "", JOptionPane.YES_NO_OPTION);
        if (option == 0) {

            String checkStatus = txtGRNStatus1.getText();
            String checkedStatus = "Checked";
            String uncheckedStatus = "Unchecked";

            if (checkStatus.equals(uncheckedStatus)) {
                try {
                    String selectedGRNItemId = ((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 0));

                    DB.DB.putData("UPDATE grn SET grn_status = 'Bad Order' WHERE id = '" + selectedGRNItemId + "'");
                    String markingBadOrderActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Bad Order','Admin marked bad order item on GRN Bill No. " + txtSearchGRNItem1.getText() + "','SUCCESS')";
                    DB.DB.putData(markingBadOrderActivityLog);
                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                    JOptionPane.showMessageDialog(this, "Bad Order Marked Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                    refreshGRNTable();
                    refreshGRNTable1();

                    calculateGRNTotal();
                    calculateGRNTotal1();

                } catch (Exception e) {
                }
            } else if (checkStatus.equals(checkedStatus)) {
                JOptionPane.showMessageDialog(this, "Checked Items cannot be Marked as Bad Order!");

            } else {
                JOptionPane.showMessageDialog(this, "GRN Item Already marked as Bad Order!");
            }
        }
    }

    public void printSelectedGRNBill() {
        HashMap a = new HashMap();
        a.put("grn_id", txtSearchGRNItem.getText());
        a.put("grn_total", lblGRNTotal.getText());

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewSelectedGRNBill.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());
                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    public void printSelectedGRNBill1() {
        HashMap a = new HashMap();
        a.put("grn_id", txtSearchGRNItem1.getText());
        a.put("grn_total", lblGRNTotal1.getText());

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewSelectedGRNBill.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    private void addToStock() {
        try {
            ResultSet stockAvailability = DB.DB.getData("SELECT item_name FROM stock WHERE item_name = '" + txtGRNItemName.getText() + "' ");
            if (stockAvailability.next()) {
                JOptionPane.showMessageDialog(this, "Existing Items cannot be Added as New Item!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                jTabbedPaneAddNewItems.setSelectedIndex(2);
            } else {
                int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Item to Stock?", "", JOptionPane.YES_NO_OPTION);
                if (option == 0) {
                    if (!txtGRNItemSellingPrice.getText().equals("")
                            & !txtGRNItemLowStock.getText().equals("")) {

                        if ("Checked".equals(txtGRNStatus.getText())) {
                            JOptionPane.showMessageDialog(this, "Item Already Added to Stock!", "Invalid Data", JOptionPane.WARNING_MESSAGE);

                        } else {

                            txtGRNItemName.setText((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 1));
                            String selectedGRNItemCode = ((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 2));
                            txtGRNItemQuantity.setText((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 3));
                            txtGRNItemPrice.setText((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 4));
                            String selectedGRNItemSupplier = ((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 5));

                            int selling_price = Integer.parseInt(txtGRNItemSellingPrice.getText());
                            int purchase_price = Integer.parseInt(txtGRNItemPrice.getText());
                            int profit = selling_price - purchase_price;

                            String profitPerEachItem = Integer.toString(profit);
                            String selectedGRNItemProfitPerSingleItem = profitPerEachItem;

                            try {

                                String addItemToStock = "INSERT INTO stock(item_code, item_name, item_price, selling_price, profit, supplier, stock_count, low_stock, stock_status) VALUES ('" + selectedGRNItemCode + "','" + txtGRNItemName.getText() + "','" + txtGRNItemPrice.getText() + "','" + txtGRNItemSellingPrice.getText() + "','" + selectedGRNItemProfitPerSingleItem + "','" + selectedGRNItemSupplier + "','" + txtGRNItemQuantity.getText() + "','" + txtGRNItemLowStock.getText() + "','Available')";
                                String selectedGRNItemId = ((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 0));

                                ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                                JOptionPane.showMessageDialog(this, "GRN Item Checked Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                                JOptionPane.showMessageDialog(this, "New Item Added to Stock!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                                String addNewStockItemActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Add New Stock Item','Admin added new Stock Item " + txtGRNItemName.getText() + "','SUCCESS')";
                                String GRNItemCheckingActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','GRN Checked','Admin checked GRN item " + txtGRNItemName.getText() + "','SUCCESS')";

                                DB.DB.putData(addItemToStock);
                                DB.DB.putData(addNewStockItemActivityLog);
                                DB.DB.putData(GRNItemCheckingActivityLog);

                                DB.DB.putData("UPDATE grn SET grn_status = 'Checked' WHERE id = '" + selectedGRNItemId + "'");
                                txtGRNStatus.setText("Checked");
                                DB.DB.putData("UPDATE item SET item_status = 'Available' WHERE item_name = '" + txtGRNItemName.getText() + "'");
                                searchGRNItem();
                                refreshStockTable();
                                refreshLowStockTable();
                                refreshSupplierItemTable();
                                jTabbedPaneAddNewItems.setSelectedIndex(2);

                                txtGRNItemName.setText(null);
                                txtGRNStatus.setText(null);
                                txtGRNItemQuantity.setText(null);
                                txtGRNItemPrice.setText(null);
                                txtGRNItemSellingPrice.setText(null);
                                txtGRNItemLowStock.setText(null);

                            } catch (Exception e) {
                            }
                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    }
                }

            }
            DB.DB.con.close();

        } catch (Exception e) {
        }

    }

    private void addReOrdersToStock() {
        try {
            ResultSet stockAvailability = DB.DB.getData("SELECT item_name FROM stock WHERE item_name = '" + txtGRNItemName1.getText() + "' ");
            if (!(stockAvailability.next())) {
                JOptionPane.showMessageDialog(this, "Non Existing Items cannot be Added as Existing Item!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                jTabbedPaneAddExistingItems.setSelectedIndex(2);
            } else {
                int option = JOptionPane.showConfirmDialog(this, "Do you want to Add this Item to Stock?", "", JOptionPane.YES_NO_OPTION);
                if (option == 0) {
                    if (!txtGRNItemSellingPrice1.getText().equals("")
                            & !txtGRNItemLowStock1.getText().equals("")) {

                        if ("Checked".equals(txtGRNStatus1.getText())) {
                            JOptionPane.showMessageDialog(this, "Item Already Added to Stock!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                        } else {

                            ResultSet rs3 = DB.DB.getData("SELECT id FROM stock WHERE item_name = '" + txtGRNItemName1.getText() + "' ");

                            if (rs3.next()) {

                                String stockId = (rs3.getString(1));

                                txtGRNItemName1.setText((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 1));
                                txtGRNItemQuantity1.setText((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 3));
                                txtGRNItemPrice1.setText((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 4));
//
                                int selling_price = Integer.parseInt(txtGRNItemSellingPrice1.getText());
                                int purchase_price = Integer.parseInt(txtGRNItemPrice1.getText());

                                int profit = selling_price - purchase_price;

                                String profitPerEachItem = Integer.toString(profit);
                                String selectedGRNItemProfitPerSingleItem = profitPerEachItem;

                                ResultSet stockCountofSelectedItem = DB.DB.getData("SELECT stock_count FROM stock WHERE id = '" + stockId + "' ");
                                if (stockCountofSelectedItem.next()) {
                                    int currentStockCount = Integer.parseInt(stockCountofSelectedItem.getString(1));
                                    int addedAmount = Integer.parseInt(txtGRNItemQuantity1.getText());
                                    int newStockCount = currentStockCount + addedAmount;

                                    DB.DB.putData("UPDATE stock SET item_price ='" + txtGRNItemPrice1.getText() + "',selling_price = '" + txtGRNItemSellingPrice1.getText() + "', profit = '" + selectedGRNItemProfitPerSingleItem + "', stock_count ='" + newStockCount + "' WHERE id = '" + stockId + "' ");

                                    String reOrderStockItemActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Re-Order Stock Item','Admin Re-Ordered Item " + txtGRNItemName1.getText() + "','SUCCESS')";
                                    String GRNItemCheckingActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','GRN Checked','Admin checked GRN item " + txtGRNItemName1.getText() + "','SUCCESS')";

                                    DB.DB.putData(reOrderStockItemActivityLog);
                                    DB.DB.putData(GRNItemCheckingActivityLog);

                                    ImageIcon icon = new ImageIcon(getClass().getResource("img/done.png"));
                                    JOptionPane.showMessageDialog(this, "GRN Item Checked Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);
                                    JOptionPane.showMessageDialog(this, "Stock count Updated!", "Done", JOptionPane.INFORMATION_MESSAGE, icon);

                                    String selectedGRNItemId = ((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 0));

                                    DB.DB.putData("UPDATE grn SET grn_status = 'Checked' WHERE id = '" + selectedGRNItemId + "'");
                                    txtGRNStatus1.setText("Checked");
                                    DB.DB.putData("UPDATE item SET item_status = 'Available' WHERE item_name = '" + txtGRNItemName1.getText() + "'");
                                    searchGRNItem1();
                                    refreshSupplierItemTable();
                                    refreshStockTable();
                                    refreshLowStockTable();
                                    refreshPOTable();
                                    refreshPOTable1();
                                    jTabbedPaneAddExistingItems.setSelectedIndex(2);

                                    String stockCount = Integer.toString(newStockCount);
                                    txtGRNItemStockCount1.setText(stockCount);

                                }
                            }

                            txtGRNItemName1.setText(null);
                            txtGRNStatus1.setText(null);
                            txtGRNItemQuantity1.setText(null);
                            txtGRNItemPrice1.setText(null);
                            txtGRNItemSellingPrice1.setText(null);
                            txtGRNItemLowStock1.setText(null);
                            txtGRNItemStockCount1.setText(null);

                        }

                    } else {
                        JOptionPane.showMessageDialog(this, "Please Fill All the Fields!", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
            DB.DB.con.close();
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
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(7));
                String g = (rs.getString(8));
                String h = (rs.getString(9));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});

            }

            calculateStockCount();
            txtSearchStock.setText(null);

            ResultSet stockCount = DB.DB.getData("SELECT COUNT(id) FROM stock WHERE stock_status = 'Available' AND (stock_count > low_stock)");
            if (stockCount.next()) {
                int d = stockCount.getInt(1);
                lblHomeStockCount.setText("Stock Count : " + d);
            }

            ResultSet sellingStock = DB.DB.getData("SELECT COUNT(id) FROM stock WHERE stock_status = 'Available' AND (stock_count > low_stock)");
            if (sellingStock.next()) {
                int e = sellingStock.getInt(1);
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
            cmbPOItemName1.setSelectedItem("");
            txtPOItemPrice1.setText(null);

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
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(7));
                String g = (rs.getString(8));
                String h = (rs.getString(9));
                dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});

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
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
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
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
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
                    String d = (rs.getString(4));
                    String e = (rs.getString(5));
                    String f = (rs.getString(7));
                    String g = (rs.getString(8));
                    String h = (rs.getString(9));
                    dtm.addRow(new Object[]{a, b, c, d, e, f, g, h});
                }
                calculateLowStockCount();
            } catch (Exception e) {
            }
            break;
            case 3:
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblLowStock.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM stock WHERE stock_status = 'Available' AND (stock_count < low_stock) AND supplier like '" + txtSearchLowStock.getText() + "%' order by supplier ");
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
                calculateLowStockCount();
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    public void printLowStock() {

        new Thread() {
            @Override
            public void run() {
                try {

                    String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewLowStockReport.jrxml";

                    JasperDesign jdesign = JRXmlLoader.load(reportSource);
                    JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                    JasperPrint jprint = JasperFillManager.fillReport(jreport, null, DB.DB.getConnection());

                    JasperViewer.viewReport(jprint, false);

                } catch (JRException ex) {

                }
            }
        }.start();
    }

    private void calculateSalesIncome() {
        try {

            Date DateDate = new Date();
            SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
            date = "" + toDate.format(DateDate);

            Date DateMonth = new Date();
            SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
            month = "" + toMonth.format(DateMonth);

            Date DateYear = new Date();
            SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
            year = "" + toYear.format(DateYear);

            ResultSet todaySalesEarning = DB.DB.getData("SELECT sum(new_total) FROM sales WHERE date  = '" + date + "'");
            ResultSet thisMonthSalesEarning = DB.DB.getData("SELECT sum(new_total) FROM sales WHERE month = '" + month + "' AND year = '" + year + "' ");
            ResultSet thisYearSalesEarning = DB.DB.getData("SELECT sum(new_total) FROM sales WHERE year = '" + year + "'");

            if (todaySalesEarning.next()) {
                Double d = todaySalesEarning.getDouble(1);
                lblSalesEarning.setText("Today Sales Income : $ " + Double.toString(d));
                jLabel53.setText("Today Income : $ " + Double.toString(d));
                jLabel59.setText("Today Sales Income       :    $ " + Double.toString(d));
            }

            if (thisMonthSalesEarning.next()) {
                Double d = thisMonthSalesEarning.getDouble(1);

                jLabel16.setText("This Month Income : $ " + Double.toString(d));
                jLabel56.setText("Monthly Income : $ " + Double.toString(d));
                jLabel62.setText("This Month Sales Income       :    $ " + Double.toString(d));
            }

            if (thisYearSalesEarning.next()) {
                Double d = thisYearSalesEarning.getDouble(1);

                jLabel50.setText("This Year Income : $ " + Double.toString(d));
                jLabel72.setText("Annual Income : $ " + Double.toString(d));
                jLabel65.setText("This Year Sales Income       :    $ " + Double.toString(d));

            }

        } catch (Exception e) {
        }
    }

    private void refreshSalesTable() {
        try {

            Date DateDate = new Date();
            SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
            date = "" + toDate.format(DateDate);

            Date DateMonth = new Date();
            SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
            month = "" + toMonth.format(DateMonth);

            Date DateYear = new Date();
            SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
            year = "" + toYear.format(DateYear);

            DefaultTableModel dtm = (DefaultTableModel) tblTodaySales.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE date = '" + date + "' order by id desc ");
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

            calculateSalesIncome();
            cmbSortTodaySales.setSelectedIndex(0);
            txtSearchTodaySales.setText(null);

            ResultSet todaySalesCount = DB.DB.getData("SELECT count(id) FROM sales WHERE date  = '" + date + "' ");
            ResultSet thisMonthSalesCount = DB.DB.getData("SELECT count(id) FROM sales WHERE month  = '" + month + "' ");

            ResultSet todaySalesProfit = DB.DB.getData("SELECT sum(profit) FROM sales WHERE date  = '" + date + "' ");
            ResultSet thisMonthSalesProfit = DB.DB.getData("SELECT sum(profit) FROM sales WHERE month  = '" + month + "' ");
            ResultSet thisYearSalesProfit = DB.DB.getData("SELECT sum(profit) FROM sales WHERE year  = '" + year + "' ");

            if (todaySalesCount.next()) {
                int d = todaySalesCount.getInt(1);
                lblTodaySalesCount.setText("Sales Count : " + d);
                jLabel52.setText("Today Count : " + d);
            }

            if (thisMonthSalesCount.next()) {
                int d = thisMonthSalesCount.getInt(1);
                jLabel55.setText("Monthly Count : " + d);
            }

            if (todaySalesProfit.next()) {
                double d = todaySalesProfit.getDouble(1);
                jLabel61.setText("Today Sales Profit          :    $ " + d);
                jLabel68.setText("Today Sales Profit : $ " + d);
            }

            if (thisMonthSalesProfit.next()) {
                double d = thisMonthSalesProfit.getDouble(1);
                jLabel71.setText("Monthly Sales Profit : $ " + d);
                jLabel64.setText("This Month Sales Profit          :    $ " + d);
            }

            if (thisYearSalesProfit.next()) {
                double d = thisYearSalesProfit.getDouble(1);
                jLabel66.setText("This Year Sales Profit          :    $ " + d);
                jLabel70.setText("Annual Sales Profit : $ " + d);
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
                    ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE date = '" + date + "' AND invoice_id like '" + txtSearchTodaySales.getText() + "%' order by id desc");
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
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE date = '" + date + "' AND item_name like '" + txtSearchTodaySales.getText() + "%' order by item_name desc");
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

    public void printTodaySalesReport() {
        new Thread() {
            @Override
            public void run() {
                try {

                    Date DateToday = new Date();
                    SimpleDateFormat toToday = new SimpleDateFormat("yyyy-MM-dd");
                    today = "" + toToday.format(DateToday);

                    ResultSet rs = DB.DB.getData("SELECT count(id), sum(new_total) FROM sales WHERE date  = '" + today + "' ");
                    if (rs.next()) {
                        int d = rs.getInt(1);
                        int e = rs.getInt(2);

                        HashMap a = new HashMap();
                        a.put("date", today);
                        a.put("today_sales_count", d);
                        a.put("today_sales_income", e);

                        String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewTodaySalesReport.jrxml";

                        JasperDesign jdesign = JRXmlLoader.load(reportSource);
                        JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                        JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());

                        JasperViewer.viewReport(jprint, false);
                    }
                } catch (Exception ex) {

                }
            }
        }.start();
    }

    private void refreshSalesHistoryTable() {
        try {

            DefaultTableModel dtm = (DefaultTableModel) tblSalesHistory.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM sales order by id desc ");
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
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE invoice_id like '" + txtSearchSalesHistory.getText() + "%' order by id desc");
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

            } catch (Exception e) {
            }
            break;
            case 1:

                

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSalesHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE item_name like '" + txtSearchSalesHistory.getText() + "%' order by item_name desc");
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
            } catch (Exception e) {
            }
            break;
            case 2:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSalesHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE date like '" + txtSearchSalesHistory.getText() + "%' order by date desc");
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
            } catch (Exception e) {
            }
            break;
            case 3:
                
                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSalesHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE month like '" + txtSearchSalesHistory.getText() + "%' order by month desc");
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
            } catch (Exception e) {
            }
            break;
            case 4:

                try {
                DefaultTableModel dtm = (DefaultTableModel) tblSalesHistory.getModel();
                ResultSet rs = DB.DB.getData("SELECT * FROM sales WHERE year like '" + txtSearchSalesHistory.getText() + "%' order by year desc");
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
            } catch (Exception e) {
            }
            break;
            default:
                break;
        }
    }

    public void printSalesReport() {

        new Thread() {
            @Override
            public void run() {
                try {

                    Date DateMonth = new Date();
                    SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
                    month = "" + toMonth.format(DateMonth);

                    Date DateYear = new Date();
                    SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
                    year = "" + toYear.format(DateYear);

                    ResultSet rs = DB.DB.getData("SELECT sum(new_total) FROM sales WHERE month = '" + month + "' AND year = '" + year + "'");
                    ResultSet rs1 = DB.DB.getData("SELECT sum(new_total) FROM sales WHERE year = '" + year + "' ");

                    if (rs.next()) {
                        Double d = rs.getDouble(1);

                        if (rs1.next()) {
                            Double e = rs1.getDouble(1);

                            HashMap a = new HashMap();
                            a.put("this_month_sales_income", d);
                            a.put("this_year_sales_income", e);

                            String reportSource = "C:\\Program Files\\Xeon Reports\\reports\\viewSalesReport.jrxml";

                            JasperDesign jdesign = JRXmlLoader.load(reportSource);
                            JasperReport jreport = JasperCompileManager.compileReport(jdesign);
                            JasperPrint jprint = JasperFillManager.fillReport(jreport, a, DB.DB.getConnection());

                            JasperViewer.viewReport(jprint, false);
                        }
                    }
                } catch (Exception ex) {

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
        pnlAdminDashboard = new javax.swing.JPanel();
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
        pnlStock = new javax.swing.JPanel();
        stock = new javax.swing.JLabel();
        pnlIndicatorStock = new javax.swing.JPanel();
        pnlSales = new javax.swing.JPanel();
        sales = new javax.swing.JLabel();
        pnlIndicatorSales = new javax.swing.JPanel();
        pnlEmployee = new javax.swing.JPanel();
        employee = new javax.swing.JLabel();
        pnlIndicatorEmployee = new javax.swing.JPanel();
        pnlSupplier = new javax.swing.JPanel();
        supplier = new javax.swing.JLabel();
        pnlIndicatorSupplier = new javax.swing.JPanel();
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
        jLabel69 = new javax.swing.JLabel();
        loginWish = new javax.swing.JLabel();
        adminName = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        lblHomeEmployeeAttendancePresent = new javax.swing.JLabel();
        lblHomeEmployeeAttendanceAbsent = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        lblHomeEmployeeCount = new javax.swing.JLabel();
        lblHomeSupplierCount = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        lblHomeStockCount = new javax.swing.JLabel();
        lblHomeLowStockCount = new javax.swing.JLabel();
        lblHomePendingPOCount = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        lblHomePayrollPaidAmount = new javax.swing.JLabel();
        lblHomePayrollUnpaidAmount = new javax.swing.JLabel();
        lblHomePayrollPaid = new javax.swing.JLabel();
        lblHomePayrollUnpaid = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        cardStock = new javax.swing.JPanel();
        pnlStockSubSelection = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        subCardStock = new javax.swing.JPanel();
        card1 = new javax.swing.JPanel();
        jTabbedPaneAddNewItems = new javax.swing.JTabbedPane();
        jPanel36 = new javax.swing.JPanel();
        lblPOItemNameValidation = new javax.swing.JLabel();
        lblPOItemCodeValidation = new javax.swing.JLabel();
        btnAddToPOList = new javax.swing.JButton();
        btnViewPOItems = new javax.swing.JButton();
        btnClearPOFields = new javax.swing.JButton();
        cmbPOLeadTime = new javax.swing.JComboBox<>();
        jLabel154 = new javax.swing.JLabel();
        jLabel148 = new javax.swing.JLabel();
        txtPOItemPrice = new javax.swing.JTextField();
        txtPOItemQuantity = new javax.swing.JTextField();
        cmbPOItemName = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel142 = new javax.swing.JLabel();
        jLabel147 = new javax.swing.JLabel();
        jLabel166 = new javax.swing.JLabel();
        jLabel161 = new javax.swing.JLabel();
        txtPOId = new javax.swing.JTextField();
        cmbPOItemSupplier = new javax.swing.JComboBox<>();
        jPanel40 = new javax.swing.JPanel();
        jScrollPane20 = new javax.swing.JScrollPane();
        tblPOItem = new javax.swing.JTable();
        btnIssuePOBill = new javax.swing.JButton();
        txtSearchPOItem = new javax.swing.JTextField();
        jLabel125 = new javax.swing.JLabel();
        cmbSortPOItem = new javax.swing.JComboBox<>();
        btnPrintPOBill = new javax.swing.JButton();
        btnDeletePOItem = new javax.swing.JButton();
        lblPOId = new javax.swing.JLabel();
        btnRefreshPOItems = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblPOId1 = new javax.swing.JLabel();
        btnViewAddToPOList = new javax.swing.JButton();
        lblTotal1 = new javax.swing.JLabel();
        jPanel42 = new javax.swing.JPanel();
        jScrollPane21 = new javax.swing.JScrollPane();
        tblGRN = new javax.swing.JTable();
        btnCheckGRNItems = new javax.swing.JButton();
        txtSearchGRNItem = new javax.swing.JTextField();
        jLabel126 = new javax.swing.JLabel();
        cmbSortGRNItem = new javax.swing.JComboBox<>();
        btnPrintGRNBill = new javax.swing.JButton();
        btnRefreshGRNItems = new javax.swing.JButton();
        lblAttendanceCount3 = new javax.swing.JLabel();
        btnMarkAsBadOrder = new javax.swing.JButton();
        lblGRNId = new javax.swing.JLabel();
        lblAttendanceCount6 = new javax.swing.JLabel();
        lblTotal2 = new javax.swing.JLabel();
        lblGRNTotal = new javax.swing.JLabel();
        jPanel43 = new javax.swing.JPanel();
        txtGRNItemName = new javax.swing.JTextField();
        jLabel163 = new javax.swing.JLabel();
        jLabel170 = new javax.swing.JLabel();
        txtGRNStatus = new javax.swing.JTextField();
        txtGRNItemQuantity = new javax.swing.JTextField();
        jLabel150 = new javax.swing.JLabel();
        jLabel151 = new javax.swing.JLabel();
        txtGRNItemPrice = new javax.swing.JTextField();
        txtGRNItemSellingPrice = new javax.swing.JTextField();
        jLabel152 = new javax.swing.JLabel();
        jLabel164 = new javax.swing.JLabel();
        txtGRNItemLowStock = new javax.swing.JTextField();
        txtGRNItemStockCount = new javax.swing.JTextField();
        jLabel186 = new javax.swing.JLabel();
        btnCheckAndAddToStock = new javax.swing.JButton();
        btnViewGRN = new javax.swing.JButton();
        lblPOItemNameValidation2 = new javax.swing.JLabel();
        lblPOItemNameValidation3 = new javax.swing.JLabel();
        card2 = new javax.swing.JPanel();
        jTabbedPaneStock = new javax.swing.JTabbedPane();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane19 = new javax.swing.JScrollPane();
        tblStock = new javax.swing.JTable();
        btnRefreshStock = new javax.swing.JButton();
        btnReOrder = new javax.swing.JButton();
        btnPrintAllStock = new javax.swing.JButton();
        txtSearchStock = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        cmbSortStock = new javax.swing.JComboBox<>();
        lblStockCount = new javax.swing.JLabel();
        btnPrintBarcodes = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        txtSearchLowStock = new javax.swing.JTextField();
        cmbSortLowStock = new javax.swing.JComboBox<>();
        jScrollPane24 = new javax.swing.JScrollPane();
        tblLowStock = new javax.swing.JTable();
        lblLowStockCount = new javax.swing.JLabel();
        btnPrintLowStock = new javax.swing.JButton();
        btnReOrder1 = new javax.swing.JButton();
        card3 = new javax.swing.JPanel();
        jTabbedPaneAddExistingItems = new javax.swing.JTabbedPane();
        jPanel37 = new javax.swing.JPanel();
        lblPOItemNameValidation1 = new javax.swing.JLabel();
        lblPOItemCodeValidation1 = new javax.swing.JLabel();
        jLabel176 = new javax.swing.JLabel();
        jLabel178 = new javax.swing.JLabel();
        jLabel149 = new javax.swing.JLabel();
        txtPOId1 = new javax.swing.JTextField();
        cmbPOItemSupplier1 = new javax.swing.JComboBox<>();
        jSeparator2 = new javax.swing.JSeparator();
        cmbPOItemName1 = new javax.swing.JComboBox<>();
        txtPOItemQuantity1 = new javax.swing.JTextField();
        txtPOItemPrice1 = new javax.swing.JTextField();
        cmbPOLeadTime1 = new javax.swing.JComboBox<>();
        btnClearPOFields1 = new javax.swing.JButton();
        btnViewPOItems1 = new javax.swing.JButton();
        btnAddToPOList1 = new javax.swing.JButton();
        jLabel177 = new javax.swing.JLabel();
        jLabel175 = new javax.swing.JLabel();
        jLabel174 = new javax.swing.JLabel();
        jPanel46 = new javax.swing.JPanel();
        jScrollPane22 = new javax.swing.JScrollPane();
        tblPOItem1 = new javax.swing.JTable();
        btnIssuePOBill1 = new javax.swing.JButton();
        txtSearchPOItem1 = new javax.swing.JTextField();
        jLabel127 = new javax.swing.JLabel();
        cmbSortPOItem1 = new javax.swing.JComboBox<>();
        btnPrintPOBill1 = new javax.swing.JButton();
        btnDeletePOItem1 = new javax.swing.JButton();
        lblPOId2 = new javax.swing.JLabel();
        btnRefreshPOItems1 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        lblTotal3 = new javax.swing.JLabel();
        lblPOId3 = new javax.swing.JLabel();
        btnViewAddToPOList1 = new javax.swing.JButton();
        lblTotal4 = new javax.swing.JLabel();
        jPanel49 = new javax.swing.JPanel();
        jScrollPane23 = new javax.swing.JScrollPane();
        tblGRN1 = new javax.swing.JTable();
        btnCheckGRNItems1 = new javax.swing.JButton();
        txtSearchGRNItem1 = new javax.swing.JTextField();
        jLabel128 = new javax.swing.JLabel();
        cmbSortGRNItem1 = new javax.swing.JComboBox<>();
        btnPrintGRN1 = new javax.swing.JButton();
        btnRefreshGRN1 = new javax.swing.JButton();
        lblAttendanceCount4 = new javax.swing.JLabel();
        btnMarkAsBadOrder1 = new javax.swing.JButton();
        lblGRNId1 = new javax.swing.JLabel();
        lblAttendanceCount7 = new javax.swing.JLabel();
        lblTotal5 = new javax.swing.JLabel();
        lblGRNTotal1 = new javax.swing.JLabel();
        jPanel50 = new javax.swing.JPanel();
        jLabel180 = new javax.swing.JLabel();
        txtGRNStatus1 = new javax.swing.JTextField();
        txtGRNItemName1 = new javax.swing.JTextField();
        btnCheckAndAddToStock1 = new javax.swing.JButton();
        jLabel183 = new javax.swing.JLabel();
        txtGRNItemStockCount1 = new javax.swing.JTextField();
        txtGRNItemSellingPrice1 = new javax.swing.JTextField();
        jLabel182 = new javax.swing.JLabel();
        jLabel185 = new javax.swing.JLabel();
        txtGRNItemQuantity1 = new javax.swing.JTextField();
        btnViewGRN1 = new javax.swing.JButton();
        jLabel179 = new javax.swing.JLabel();
        jLabel181 = new javax.swing.JLabel();
        txtGRNItemPrice1 = new javax.swing.JTextField();
        jLabel184 = new javax.swing.JLabel();
        txtGRNItemLowStock1 = new javax.swing.JTextField();
        lblPOItemCodeValidation2 = new javax.swing.JLabel();
        lblPOItemCodeValidation3 = new javax.swing.JLabel();
        cardSales = new javax.swing.JPanel();
        pnlSalesSubSelection = new javax.swing.JPanel();
        btn_addUsers1 = new javax.swing.JLabel();
        btn_addUsers3 = new javax.swing.JLabel();
        btn_addUsers2 = new javax.swing.JLabel();
        subCardSales = new javax.swing.JPanel();
        card4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel26 = new javax.swing.JPanel();
        txtSearchTodaySales = new javax.swing.JTextField();
        cmbSortTodaySales = new javax.swing.JComboBox<>();
        jLabel132 = new javax.swing.JLabel();
        jScrollPane27 = new javax.swing.JScrollPane();
        tblTodaySales = new javax.swing.JTable();
        lblSalesEarning = new javax.swing.JLabel();
        btnPrintTodaySales = new javax.swing.JButton();
        btnRefreshTodaySales = new javax.swing.JButton();
        lblTodaySalesCount = new javax.swing.JLabel();
        lblTodaySalesCount1 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        txtSearchSalesHistory = new javax.swing.JTextField();
        cmbSortSalesHistory = new javax.swing.JComboBox<>();
        jLabel131 = new javax.swing.JLabel();
        jScrollPane26 = new javax.swing.JScrollPane();
        tblSalesHistory = new javax.swing.JTable();
        btnPrintSalesHistory = new javax.swing.JButton();
        btnRefreshSalesHistory = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        card5 = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        card6 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        cardEmployee = new javax.swing.JPanel();
        pnlEmployeeSubSelection = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        subCardEmployee = new javax.swing.JPanel();
        card7 = new javax.swing.JPanel();
        lblEmployeeUserameValidation = new javax.swing.JLabel();
        lblEmployeePositionValidation = new javax.swing.JLabel();
        lblEmployeeNICValidation = new javax.swing.JLabel();
        lblEmployeePhoneNoValidation = new javax.swing.JLabel();
        lblEmployeeSalaryValidation = new javax.swing.JLabel();
        lblEmployeePasswordValidation = new javax.swing.JLabel();
        lblEmployeeRepeatPasswordValidation = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        pwdRepeatEmployeePassword = new javax.swing.JPasswordField();
        btnAddEmployee = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        pwdEmployeePassword = new javax.swing.JPasswordField();
        txtEmployeeSalary = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtEmployeePhoneNo = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txtEmployeeNIC = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txtEmployeePosition = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtEmployeeUsername = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        card8 = new javax.swing.JPanel();
        jTabbedPaneEmployee = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        tblEmployee = new javax.swing.JTable();
        btnEditEmployee = new javax.swing.JButton();
        btnDeleteEmployee = new javax.swing.JButton();
        btnPrintAllEmployee = new javax.swing.JButton();
        txtSearchEmployee = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        btnRefreshEmployee = new javax.swing.JButton();
        cmbSortEmployee = new javax.swing.JComboBox<>();
        lblEmployeeCount = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        lblSelectedEmployeePhoneNoValidation = new javax.swing.JLabel();
        lblSelectedEmployeeSalaryValidation = new javax.swing.JLabel();
        lblSelectedEmployeeUsernameValidation = new javax.swing.JLabel();
        lblSelectedEmployeeIdValidation = new javax.swing.JLabel();
        lblSelectedEmployeePositionValidation = new javax.swing.JLabel();
        lblSelectedEmployeeNICValidation = new javax.swing.JLabel();
        btnGoBackToViewEmployee = new javax.swing.JButton();
        btnUpdateSelectedEmployee = new javax.swing.JButton();
        btnDeleteSelectedEmployee = new javax.swing.JButton();
        btnPrintSelectedEmployee = new javax.swing.JButton();
        txtSelectedEmployeeSalary = new javax.swing.JTextField();
        txtSelectedEmployeePhoneNo = new javax.swing.JTextField();
        txtSelectedEmployeeNIC = new javax.swing.JTextField();
        txtSelectedEmployeePosition = new javax.swing.JTextField();
        txtSelectedEmployeeUsername = new javax.swing.JTextField();
        txtSelectedEmployeeId = new javax.swing.JTextField();
        jLabel155 = new javax.swing.JLabel();
        jLabel156 = new javax.swing.JLabel();
        jLabel133 = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        jLabel136 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        tblEmployeeAttendance = new javax.swing.JTable();
        txtSearchEmployeeAttendance = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        btnRefreshEmployeeAttendance = new javax.swing.JButton();
        cmbSortEmployeeAttendance = new javax.swing.JComboBox<>();
        jXDatePicker1 = new org.jdesktop.swingx.JXDatePicker();
        lblAttendanceCount = new javax.swing.JLabel();
        lblAbsenceCount = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        tblEmployeeMonthlyAttendance = new javax.swing.JTable();
        btnRefreshMonthlyEmployeeAttendance = new javax.swing.JButton();
        txtSearchMonthlyEmployeeAttendance = new javax.swing.JTextField();
        jLabel109 = new javax.swing.JLabel();
        cmbSortMonthlyEmployeeAttendance = new javax.swing.JComboBox<>();
        card9 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        tblEmployeePayroll = new javax.swing.JTable();
        btnRefreshEmployeePayroll = new javax.swing.JButton();
        btnPayEmployee = new javax.swing.JButton();
        txtSearchPayrollSummary = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        cmbSortEmployeePayroll = new javax.swing.JComboBox<>();
        lblPaidCount = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        lblUnpaidCount = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jLabel141 = new javax.swing.JLabel();
        txtSelectedPayrollEmployeeMonth = new javax.swing.JTextField();
        jLabel143 = new javax.swing.JLabel();
        jLabel144 = new javax.swing.JLabel();
        txtSelectedPayrollEmployeeSalary = new javax.swing.JTextField();
        jLabel159 = new javax.swing.JLabel();
        txtSelectedPayrollEmployeeId = new javax.swing.JTextField();
        btnPaySelectedEmployee = new javax.swing.JButton();
        btnGoBackToPayrollSummary = new javax.swing.JButton();
        jLabel160 = new javax.swing.JLabel();
        txtSelectedPayrollEmployeeUsername = new javax.swing.JTextField();
        btnPrintSelectedEmployeePayrollBill = new javax.swing.JButton();
        txtSelectedPayrollEmployeeExtraSalary = new javax.swing.JTextField();
        jLabel145 = new javax.swing.JLabel();
        txtSelectedPayrollEmployeePayment = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtSelectedPayrollEmployeeAttendance = new javax.swing.JTextField();
        jLabel146 = new javax.swing.JLabel();
        txtSelectedPayrollEmployeeStatus = new javax.swing.JTextField();
        lblSelectedEmployeeExtraSalaryValidation = new javax.swing.JLabel();
        lblSelectedEmployeePaymentValidation = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        tblEmployeePayrollHistory = new javax.swing.JTable();
        btnRefreshPayrollHistory = new javax.swing.JButton();
        txtSearchPayrollHistory = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        cmbSortEmployeePayrollHistory = new javax.swing.JComboBox<>();
        cardSupplier = new javax.swing.JPanel();
        pnlSupplierSubSelection = new javax.swing.JPanel();
        jLabel114 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        jLabel124 = new javax.swing.JLabel();
        subCardSupplier = new javax.swing.JPanel();
        card10 = new javax.swing.JPanel();
        lblSupplierAddressValidation = new javax.swing.JLabel();
        lblSupplierUserameValidation = new javax.swing.JLabel();
        lblSupplierEmailValidation = new javax.swing.JLabel();
        lblSupplierPhoneNoValidation = new javax.swing.JLabel();
        lblSupplierCompanyValidation = new javax.swing.JLabel();
        btnAddSupplier = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        txtSupplierCompany = new javax.swing.JTextField();
        txtSupplierPhoneNo = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        txtSupplierEmail = new javax.swing.JTextField();
        txtSupplierAddress = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        txtSupplierUsername = new javax.swing.JTextField();
        card11 = new javax.swing.JPanel();
        jTabbedPaneSupplier = new javax.swing.JTabbedPane();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        tblSupplier = new javax.swing.JTable();
        btnEditSupplier = new javax.swing.JButton();
        btnDeleteSupplier = new javax.swing.JButton();
        btnPrintAllSupplier = new javax.swing.JButton();
        txtSearchSupplier = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        btnRefreshSupplier = new javax.swing.JButton();
        cmbSortSupplier = new javax.swing.JComboBox<>();
        lblSupplierCount = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        lblSelectedSupplierPhoneNoValidation = new javax.swing.JLabel();
        lblSelectedSupplierCompanyValidation = new javax.swing.JLabel();
        lblSelectedSupplierUsernameValidation = new javax.swing.JLabel();
        lblSelectedSupplierIdValidation = new javax.swing.JLabel();
        lblSelectedSupplierAddressValidation = new javax.swing.JLabel();
        lblSelectedSupplierEmailValidation = new javax.swing.JLabel();
        btnGoBackToViewSupplier = new javax.swing.JButton();
        btnUpdateSelectedSupplier = new javax.swing.JButton();
        btnDeleteSelectedSupplier = new javax.swing.JButton();
        btnPrintSelectedSupplier = new javax.swing.JButton();
        txtSelectedSupplierCompany = new javax.swing.JTextField();
        jLabel140 = new javax.swing.JLabel();
        jLabel139 = new javax.swing.JLabel();
        jLabel138 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        jLabel158 = new javax.swing.JLabel();
        jLabel157 = new javax.swing.JLabel();
        txtSelectedSupplierId = new javax.swing.JTextField();
        txtSelectedSupplierUsername = new javax.swing.JTextField();
        txtSelectedSupplierAddress = new javax.swing.JTextField();
        txtSelectedSupplierEmail = new javax.swing.JTextField();
        txtSelectedSupplierPhoneNo = new javax.swing.JTextField();
        card12 = new javax.swing.JPanel();
        jTabbedPaneSupplierItem = new javax.swing.JTabbedPane();
        jPanel16 = new javax.swing.JPanel();
        lblItemNameValidation = new javax.swing.JLabel();
        lblItemCodeValidation = new javax.swing.JLabel();
        lblPOItemQuantityValidation1 = new javax.swing.JLabel();
        btnSaveItem = new javax.swing.JButton();
        jLabel169 = new javax.swing.JLabel();
        cmbItemSupplier = new javax.swing.JComboBox<>();
        cmbItemCategory = new javax.swing.JComboBox<>();
        jLabel171 = new javax.swing.JLabel();
        jLabel168 = new javax.swing.JLabel();
        txtItemCode = new javax.swing.JTextField();
        txtItemName = new javax.swing.JTextField();
        jLabel165 = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jScrollPane18 = new javax.swing.JScrollPane();
        tblSupplierItem = new javax.swing.JTable();
        btnDeleteSupplierItem = new javax.swing.JButton();
        btnEditSupplierItem = new javax.swing.JButton();
        txtSearchSupplierItem = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        cmbSortSupplierItem = new javax.swing.JComboBox<>();
        lblItemCount = new javax.swing.JLabel();
        btnRefreshSupplierItem = new javax.swing.JButton();
        jPanel44 = new javax.swing.JPanel();
        lblSelectedSupplierPhoneNoValidation2 = new javax.swing.JLabel();
        lblSelectedSupplierCompanyValidation2 = new javax.swing.JLabel();
        lblSelectedSupplierUsernameValidation2 = new javax.swing.JLabel();
        lblSelectedSupplierIdValidation2 = new javax.swing.JLabel();
        lblSelectedSupplierAddressValidation2 = new javax.swing.JLabel();
        lblSelectedSupplierEmailValidation2 = new javax.swing.JLabel();
        jLabel172 = new javax.swing.JLabel();
        txtSelectedItemSupplierId = new javax.swing.JTextField();
        jLabel173 = new javax.swing.JLabel();
        txtSelectedItemSupplierUsername = new javax.swing.JTextField();
        jLabel153 = new javax.swing.JLabel();
        txtSelectedItemSupplierItemName = new javax.swing.JTextField();
        jLabel162 = new javax.swing.JLabel();
        txtSelectedItemSupplierItemCode = new javax.swing.JTextField();
        jLabel167 = new javax.swing.JLabel();
        cmbSelectedItemSupplierItemCategory = new javax.swing.JComboBox<>();
        btnDeleteSelectedSupplierItem = new javax.swing.JButton();
        btnUpdateSelectedSupplierItem = new javax.swing.JButton();
        btnGoBackToViewItem = new javax.swing.JButton();
        cardSettings = new javax.swing.JPanel();
        pnlSettingsSubSelection = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        subCardSettings = new javax.swing.JPanel();
        card13 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtBackupLocation = new javax.swing.JTextField();
        btnBrowseBackupPath = new javax.swing.JButton();
        btnBackup = new javax.swing.JButton();
        card14 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtRestoreLocation = new javax.swing.JTextField();
        btnBrowseRestoreLocation = new javax.swing.JButton();
        btnRestore = new javax.swing.JButton();
        card15 = new javax.swing.JPanel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Xeon Inventory");
        setLocationByPlatform(true);
        setUndecorated(true);
        setOpacity(0.985F);

        pnlFrames.setBackground(new java.awt.Color(0, 0, 0));
        pnlFrames.setLayout(new java.awt.CardLayout());

        pnlAdminDashboard.setBackground(new java.awt.Color(0, 0, 0));
        pnlAdminDashboard.setPreferredSize(new java.awt.Dimension(1200, 690));

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

        lblAdminUsername.setBackground(new java.awt.Color(0, 0, 0));
        lblAdminUsername.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        lblAdminUsername.setForeground(new java.awt.Color(255, 255, 255));
        lblAdminUsername.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAdminUsername.setText("Admin");
        lblAdminUsername.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblAdminUsername.setOpaque(true);
        lblAdminUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblAdminUsernameMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblAdminUsernameMouseExited(evt);
            }
        });

        javax.swing.GroupLayout pnlTopLayout = new javax.swing.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblAdminUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTopIcons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlTopLayout.setVerticalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTopIcons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblAdminUsername, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        pnlStock.setBackground(new java.awt.Color(35, 35, 35));
        pnlStock.setForeground(new java.awt.Color(20, 20, 20));
        pnlStock.setPreferredSize(new java.awt.Dimension(250, 55));
        pnlStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlStockMouseClicked(evt);
            }
        });

        stock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        stock.setForeground(new java.awt.Color(255, 255, 255));
        stock.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        stock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/stock.icon.png"))); // NOI18N
        stock.setText("Stock");
        stock.setIconTextGap(20);
        stock.setPreferredSize(new java.awt.Dimension(78, 30));
        stock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stockMouseClicked(evt);
            }
        });

        pnlIndicatorStock.setBackground(new java.awt.Color(35, 35, 35));
        pnlIndicatorStock.setPreferredSize(new java.awt.Dimension(4, 0));

        javax.swing.GroupLayout pnlIndicatorStockLayout = new javax.swing.GroupLayout(pnlIndicatorStock);
        pnlIndicatorStock.setLayout(pnlIndicatorStockLayout);
        pnlIndicatorStockLayout.setHorizontalGroup(
            pnlIndicatorStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        pnlIndicatorStockLayout.setVerticalGroup(
            pnlIndicatorStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlStockLayout = new javax.swing.GroupLayout(pnlStock);
        pnlStock.setLayout(pnlStockLayout);
        pnlStockLayout.setHorizontalGroup(
            pnlStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStockLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIndicatorStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(stock, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlStockLayout.setVerticalGroup(
            pnlStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(stock, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(pnlStockLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIndicatorStock, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        stock.getAccessibleContext().setAccessibleName("");

        pnlSales.setBackground(new java.awt.Color(35, 35, 35));
        pnlSales.setForeground(new java.awt.Color(20, 20, 20));
        pnlSales.setPreferredSize(new java.awt.Dimension(250, 55));
        pnlSales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlSalesMouseClicked(evt);
            }
        });

        sales.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        sales.setForeground(new java.awt.Color(255, 255, 255));
        sales.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sales.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/sales.icon.png"))); // NOI18N
        sales.setText("Sales");
        sales.setIconTextGap(20);
        sales.setPreferredSize(new java.awt.Dimension(74, 30));
        sales.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salesMouseClicked(evt);
            }
        });

        pnlIndicatorSales.setBackground(new java.awt.Color(35, 35, 35));
        pnlIndicatorSales.setPreferredSize(new java.awt.Dimension(4, 0));

        javax.swing.GroupLayout pnlIndicatorSalesLayout = new javax.swing.GroupLayout(pnlIndicatorSales);
        pnlIndicatorSales.setLayout(pnlIndicatorSalesLayout);
        pnlIndicatorSalesLayout.setHorizontalGroup(
            pnlIndicatorSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        pnlIndicatorSalesLayout.setVerticalGroup(
            pnlIndicatorSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 48, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlSalesLayout = new javax.swing.GroupLayout(pnlSales);
        pnlSales.setLayout(pnlSalesLayout);
        pnlSalesLayout.setHorizontalGroup(
            pnlSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSalesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIndicatorSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sales, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlSalesLayout.setVerticalGroup(
            pnlSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pnlSalesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlIndicatorSales, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addGroup(pnlSalesLayout.createSequentialGroup()
                .addComponent(sales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        sales.getAccessibleContext().setAccessibleName("");

        pnlEmployee.setBackground(new java.awt.Color(35, 35, 35));
        pnlEmployee.setForeground(new java.awt.Color(20, 20, 20));
        pnlEmployee.setPreferredSize(new java.awt.Dimension(250, 55));
        pnlEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlEmployeeMouseClicked(evt);
            }
        });

        employee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        employee.setForeground(new java.awt.Color(255, 255, 255));
        employee.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        employee.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/employee.icon.png"))); // NOI18N
        employee.setText("Employee");
        employee.setIconTextGap(20);
        employee.setPreferredSize(new java.awt.Dimension(112, 30));
        employee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                employeeMouseClicked(evt);
            }
        });

        pnlIndicatorEmployee.setBackground(new java.awt.Color(35, 35, 35));
        pnlIndicatorEmployee.setPreferredSize(new java.awt.Dimension(4, 0));

        javax.swing.GroupLayout pnlIndicatorEmployeeLayout = new javax.swing.GroupLayout(pnlIndicatorEmployee);
        pnlIndicatorEmployee.setLayout(pnlIndicatorEmployeeLayout);
        pnlIndicatorEmployeeLayout.setHorizontalGroup(
            pnlIndicatorEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );
        pnlIndicatorEmployeeLayout.setVerticalGroup(
            pnlIndicatorEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlEmployeeLayout = new javax.swing.GroupLayout(pnlEmployee);
        pnlEmployee.setLayout(pnlEmployeeLayout);
        pnlEmployeeLayout.setHorizontalGroup(
            pnlEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEmployeeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIndicatorEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(employee, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlEmployeeLayout.setVerticalGroup(
            pnlEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(pnlEmployeeLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(employee, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlEmployeeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlIndicatorEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        employee.getAccessibleContext().setAccessibleName("");

        pnlSupplier.setBackground(new java.awt.Color(35, 35, 35));
        pnlSupplier.setForeground(new java.awt.Color(20, 20, 20));
        pnlSupplier.setPreferredSize(new java.awt.Dimension(250, 55));
        pnlSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlSupplierMouseClicked(evt);
            }
        });

        supplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        supplier.setForeground(new java.awt.Color(255, 255, 255));
        supplier.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        supplier.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/supplier.icon.png"))); // NOI18N
        supplier.setText("Supplier");
        supplier.setIconTextGap(20);
        supplier.setPreferredSize(new java.awt.Dimension(99, 30));
        supplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                supplierMouseClicked(evt);
            }
        });

        pnlIndicatorSupplier.setBackground(new java.awt.Color(35, 35, 35));
        pnlIndicatorSupplier.setPreferredSize(new java.awt.Dimension(4, 0));

        javax.swing.GroupLayout pnlIndicatorSupplierLayout = new javax.swing.GroupLayout(pnlIndicatorSupplier);
        pnlIndicatorSupplier.setLayout(pnlIndicatorSupplierLayout);
        pnlIndicatorSupplierLayout.setHorizontalGroup(
            pnlIndicatorSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );
        pnlIndicatorSupplierLayout.setVerticalGroup(
            pnlIndicatorSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlSupplierLayout = new javax.swing.GroupLayout(pnlSupplier);
        pnlSupplier.setLayout(pnlSupplierLayout);
        pnlSupplierLayout.setHorizontalGroup(
            pnlSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSupplierLayout.createSequentialGroup()
                .addGap(0, 48, Short.MAX_VALUE)
                .addComponent(supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlSupplierLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlIndicatorSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(233, Short.MAX_VALUE)))
        );
        pnlSupplierLayout.setVerticalGroup(
            pnlSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(supplier, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(pnlSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlSupplierLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(pnlIndicatorSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        supplier.getAccessibleContext().setAccessibleName("");

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
            .addComponent(pnlStock, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
            .addComponent(pnlSales, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
            .addComponent(pnlEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
            .addComponent(pnlSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
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
                .addComponent(pnlStock, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlSales, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
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

        loginWish.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        loginWish.setForeground(new java.awt.Color(255, 255, 255));
        loginWish.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        loginWish.setText("Good morning");
        loginWish.setToolTipText("");

        adminName.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        adminName.setForeground(new java.awt.Color(255, 255, 255));
        adminName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        adminName.setText("Admin");

        jPanel15.setBackground(new java.awt.Color(20, 47, 91));

        jLabel60.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setText("Today Attendance");

        lblHomeEmployeeAttendancePresent.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        lblHomeEmployeeAttendancePresent.setForeground(new java.awt.Color(180, 180, 180));
        lblHomeEmployeeAttendancePresent.setText("Present : N/A");

        lblHomeEmployeeAttendanceAbsent.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        lblHomeEmployeeAttendanceAbsent.setForeground(new java.awt.Color(180, 180, 180));
        lblHomeEmployeeAttendanceAbsent.setText("Absent : N/A");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblHomeEmployeeAttendanceAbsent, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblHomeEmployeeAttendancePresent, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel60)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHomeEmployeeAttendancePresent)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHomeEmployeeAttendanceAbsent)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel30.setBackground(new java.awt.Color(143, 28, 40));

        jLabel53.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(180, 180, 180));
        jLabel53.setText("Today Income : N/A");

        jLabel55.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(180, 180, 180));
        jLabel55.setText("Monthly Count : N/A");

        jLabel51.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Sales Summary");

        jLabel52.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(180, 180, 180));
        jLabel52.setText("Today Count : N/A");

        jLabel56.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(180, 180, 180));
        jLabel56.setText("Monthly Income : N/A");

        jLabel72.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        jLabel72.setText("Annual Income : N/A");

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
                    .addComponent(jLabel72, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel56)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel72)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel28.setBackground(new java.awt.Color(137, 84, 0));

        jLabel57.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("User Summary");

        lblHomeEmployeeCount.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        lblHomeEmployeeCount.setForeground(new java.awt.Color(180, 180, 180));
        lblHomeEmployeeCount.setText("Employee Count : N/A");

        lblHomeSupplierCount.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        lblHomeSupplierCount.setForeground(new java.awt.Color(180, 180, 180));
        lblHomeSupplierCount.setText("Supplier Count : N/A");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblHomeEmployeeCount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                        .addComponent(lblHomeSupplierCount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel57)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHomeEmployeeCount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHomeSupplierCount)
                .addContainerGap(137, Short.MAX_VALUE))
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

        lblHomePendingPOCount.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        lblHomePendingPOCount.setForeground(new java.awt.Color(180, 180, 180));
        lblHomePendingPOCount.setText("Pending PO Count : N/A");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHomeStockCount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblHomeLowStockCount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblHomePendingPOCount, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHomePendingPOCount)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel33.setBackground(new java.awt.Color(97, 59, 80));

        lblHomePayrollPaidAmount.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        lblHomePayrollPaidAmount.setForeground(new java.awt.Color(180, 180, 180));
        lblHomePayrollPaidAmount.setText("Payroll Expense : N/A");

        lblHomePayrollUnpaidAmount.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        lblHomePayrollUnpaidAmount.setForeground(new java.awt.Color(180, 180, 180));
        lblHomePayrollUnpaidAmount.setText("Payroll Due : N/A");

        lblHomePayrollPaid.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        lblHomePayrollPaid.setForeground(new java.awt.Color(180, 180, 180));
        lblHomePayrollPaid.setText("Paid : N/A");

        lblHomePayrollUnpaid.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        lblHomePayrollUnpaid.setForeground(new java.awt.Color(180, 180, 180));
        lblHomePayrollUnpaid.setText("Unpaid : N/A");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Payroll Summary");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblHomePayrollUnpaid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHomePayrollPaid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblHomePayrollUnpaidAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblHomePayrollPaidAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHomePayrollUnpaid)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHomePayrollPaid)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblHomePayrollUnpaidAmount)
                .addGap(6, 6, 6)
                .addComponent(lblHomePayrollPaidAmount)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel34.setBackground(new java.awt.Color(33, 94, 64));

        jLabel68.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(180, 180, 180));
        jLabel68.setText("Today Sales Profit : N/A");

        jLabel70.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        jLabel70.setText("Annual Sales Profit : N/A");

        jLabel67.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(255, 255, 255));
        jLabel67.setText("Profit Summary");

        jLabel71.setFont(new java.awt.Font("Segoe UI Semibold", 0, 17)); // NOI18N
        jLabel71.setText("Monthly Sales Profit : N/A");

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                        .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel71, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                            .addComponent(jLabel70, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(24, 24, 24))))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(96, 96, 96))
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                                .addComponent(jLabel68)
                                .addGap(30, 30, 30))
                            .addComponent(jLabel71, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel70)))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout cardHomeLayout = new javax.swing.GroupLayout(cardHome);
        cardHome.setLayout(cardHomeLayout);
        cardHomeLayout.setHorizontalGroup(
            cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHomeSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(cardHomeLayout.createSequentialGroup()
                .addGroup(cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardHomeLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(loginWish)
                        .addGap(6, 6, 6)
                        .addComponent(adminName, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cardHomeLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        cardHomeLayout.setVerticalGroup(
            cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardHomeLayout.createSequentialGroup()
                .addComponent(pnlHomeSubSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(adminName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(loginWish, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cardHomeLayout.createSequentialGroup()
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(cardHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, cardHomeLayout.createSequentialGroup()
                            .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(cardHomeLayout.createSequentialGroup()
                            .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(31, 31, 31))
        );

        mainCard.add(cardHome, "card2");

        cardStock.setBackground(new java.awt.Color(0, 0, 0));

        pnlStockSubSelection.setBackground(new java.awt.Color(20, 20, 20));

        jLabel44.setBackground(new java.awt.Color(60, 60, 60));
        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("New Item");
        jLabel44.setOpaque(true);
        jLabel44.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel44MouseClicked(evt);
            }
        });

        jLabel45.setBackground(new java.awt.Color(35, 35, 35));
        jLabel45.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("Re-Order");
        jLabel45.setOpaque(true);
        jLabel45.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel45MouseClicked(evt);
            }
        });

        jLabel46.setBackground(new java.awt.Color(35, 35, 35));
        jLabel46.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("Manage Stock");
        jLabel46.setOpaque(true);
        jLabel46.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel46MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlStockSubSelectionLayout = new javax.swing.GroupLayout(pnlStockSubSelection);
        pnlStockSubSelection.setLayout(pnlStockSubSelectionLayout);
        pnlStockSubSelectionLayout.setHorizontalGroup(
            pnlStockSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStockSubSelectionLayout.createSequentialGroup()
                .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlStockSubSelectionLayout.setVerticalGroup(
            pnlStockSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel44, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
            .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        subCardStock.setBackground(new java.awt.Color(153, 153, 153));
        subCardStock.setLayout(new java.awt.CardLayout());

        card1.setBackground(new java.awt.Color(20, 20, 20));

        jTabbedPaneAddNewItems.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPaneAddNewItems.setFocusable(false);
        jTabbedPaneAddNewItems.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPaneAddNewItems.setOpaque(true);

        jPanel36.setBackground(new java.awt.Color(0, 0, 0));

        lblPOItemNameValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPOItemNameValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblPOItemCodeValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPOItemCodeValidation.setForeground(new java.awt.Color(255, 0, 51));

        btnAddToPOList.setBackground(new java.awt.Color(40, 40, 40));
        btnAddToPOList.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAddToPOList.setForeground(new java.awt.Color(255, 255, 255));
        btnAddToPOList.setText("Add to PO List");
        btnAddToPOList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToPOListActionPerformed(evt);
            }
        });

        btnViewPOItems.setBackground(new java.awt.Color(40, 40, 40));
        btnViewPOItems.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnViewPOItems.setForeground(new java.awt.Color(255, 255, 255));
        btnViewPOItems.setText("View PO Items");
        btnViewPOItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewPOItemsActionPerformed(evt);
            }
        });

        btnClearPOFields.setBackground(new java.awt.Color(40, 40, 40));
        btnClearPOFields.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnClearPOFields.setForeground(new java.awt.Color(255, 255, 255));
        btnClearPOFields.setText("Clear");
        btnClearPOFields.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearPOFieldsActionPerformed(evt);
            }
        });

        cmbPOLeadTime.setBackground(new java.awt.Color(18, 18, 18));
        cmbPOLeadTime.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbPOLeadTime.setForeground(new java.awt.Color(255, 255, 255));
        cmbPOLeadTime.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 Week", "2 Weeks", "3 Weeks", "1 Month" }));
        cmbPOLeadTime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOLeadTimeKeyPressed(evt);
            }
        });

        jLabel154.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel154.setForeground(new java.awt.Color(255, 255, 255));
        jLabel154.setText("Lead Time");

        jLabel148.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel148.setForeground(new java.awt.Color(255, 255, 255));
        jLabel148.setText("Item Price");

        txtPOItemPrice.setBackground(new java.awt.Color(18, 18, 18));
        txtPOItemPrice.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtPOItemPrice.setForeground(new java.awt.Color(255, 255, 255));
        txtPOItemPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPOItemPriceKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPOItemPriceKeyTyped(evt);
            }
        });

        txtPOItemQuantity.setBackground(new java.awt.Color(18, 18, 18));
        txtPOItemQuantity.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtPOItemQuantity.setForeground(new java.awt.Color(255, 255, 255));
        txtPOItemQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPOItemQuantityKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPOItemQuantityKeyTyped(evt);
            }
        });

        cmbPOItemName.setBackground(new java.awt.Color(18, 18, 18));
        cmbPOItemName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbPOItemName.setForeground(new java.awt.Color(255, 255, 255));
        cmbPOItemName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOItemNameKeyPressed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(100, 100, 100));

        jLabel142.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel142.setForeground(new java.awt.Color(255, 255, 255));
        jLabel142.setText("Item Name");

        jLabel147.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel147.setForeground(new java.awt.Color(255, 255, 255));
        jLabel147.setText("Quantity");

        jLabel166.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel166.setForeground(new java.awt.Color(255, 255, 255));
        jLabel166.setText("Supplier");

        jLabel161.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel161.setForeground(new java.awt.Color(255, 255, 255));
        jLabel161.setText("PO Id");

        txtPOId.setBackground(new java.awt.Color(45, 45, 45));
        txtPOId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtPOId.setForeground(new java.awt.Color(255, 255, 255));
        txtPOId.setText("1");
        txtPOId.setEnabled(false);

        cmbPOItemSupplier.setBackground(new java.awt.Color(18, 18, 18));
        cmbPOItemSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbPOItemSupplier.setForeground(new java.awt.Color(255, 255, 255));
        cmbPOItemSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPOItemSupplierActionPerformed(evt);
            }
        });
        cmbPOItemSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOItemSupplierKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel148, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel147, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel142, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel161, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel154, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel166, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAddToPOList, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel36Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(btnViewPOItems)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnClearPOFields))
                            .addGroup(jPanel36Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cmbPOLeadTime, 0, 307, Short.MAX_VALUE)
                                    .addComponent(txtPOItemPrice)
                                    .addComponent(txtPOItemQuantity)
                                    .addComponent(cmbPOItemSupplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtPOId)
                                    .addComponent(cmbPOItemName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addComponent(jSeparator1))
                .addGap(18, 18, 18)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPOItemCodeValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .addComponent(lblPOItemNameValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPOId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel161, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbPOItemSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel166, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbPOItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel142, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel147, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtPOItemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblPOItemCodeValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel148, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPOItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblPOItemNameValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbPOLeadTime, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel154, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClearPOFields, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddToPOList, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewPOItems, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(88, Short.MAX_VALUE))
        );

        jTabbedPaneAddNewItems.addTab("Purchase Order", jPanel36);

        jPanel40.setBackground(new java.awt.Color(0, 0, 0));

        tblPOItem.setBackground(new java.awt.Color(0, 0, 0));
        tblPOItem.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblPOItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PO Item Id", "Item Name", "Item Code", "Quantity", "Item Price", "Supplier", "Total", "Expected Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPOItem.setGridColor(new java.awt.Color(70, 70, 70));
        tblPOItem.setRequestFocusEnabled(false);
        tblPOItem.setRowHeight(25);
        tblPOItem.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblPOItem.getTableHeader().setReorderingAllowed(false);
        tblPOItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPOItemMouseClicked(evt);
            }
        });
        jScrollPane20.setViewportView(tblPOItem);

        btnIssuePOBill.setBackground(new java.awt.Color(40, 40, 40));
        btnIssuePOBill.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnIssuePOBill.setForeground(new java.awt.Color(255, 255, 255));
        btnIssuePOBill.setText("Issue");
        btnIssuePOBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIssuePOBillActionPerformed(evt);
            }
        });

        txtSearchPOItem.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchPOItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchPOItem.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchPOItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchPOItemKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchPOItemKeyTyped(evt);
            }
        });

        jLabel125.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        cmbSortPOItem.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortPOItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortPOItem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PO Id", "Item Name", "Item Code", "Supplier" }));
        cmbSortPOItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortPOItemActionPerformed(evt);
            }
        });

        btnPrintPOBill.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintPOBill.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintPOBill.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintPOBill.setText("Print");
        btnPrintPOBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintPOBillActionPerformed(evt);
            }
        });

        btnDeletePOItem.setBackground(new java.awt.Color(40, 40, 40));
        btnDeletePOItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeletePOItem.setForeground(new java.awt.Color(255, 255, 255));
        btnDeletePOItem.setText("Delete");
        btnDeletePOItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletePOItemActionPerformed(evt);
            }
        });

        lblPOId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPOId.setForeground(new java.awt.Color(153, 153, 153));

        btnRefreshPOItems.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshPOItems.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshPOItems.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshPOItems.setText("Refresh");
        btnRefreshPOItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshPOItemsActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(153, 153, 153));
        jLabel20.setText("|");

        lblTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(153, 153, 153));
        lblTotal.setText("$ 0.0");

        lblPOId1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPOId1.setForeground(new java.awt.Color(153, 153, 153));
        lblPOId1.setText("PO Id :");

        btnViewAddToPOList.setBackground(new java.awt.Color(40, 40, 40));
        btnViewAddToPOList.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnViewAddToPOList.setForeground(new java.awt.Color(255, 255, 255));
        btnViewAddToPOList.setText("Add PO Item");
        btnViewAddToPOList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAddToPOListActionPerformed(evt);
            }
        });

        lblTotal1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTotal1.setForeground(new java.awt.Color(153, 153, 153));
        lblTotal1.setText("PO Total : ");

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addComponent(txtSearchPOItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortPOItem, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel125))
                    .addComponent(jScrollPane20)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addComponent(lblPOId1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPOId, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotal1)
                        .addGap(0, 0, 0)
                        .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshPOItems)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnViewAddToPOList)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeletePOItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintPOBill)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnIssuePOBill)))
                .addGap(20, 20, 20))
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel125)
                    .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearchPOItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbSortPOItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19)
                .addComponent(jScrollPane20, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel40Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(lblPOId1))
                            .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnRefreshPOItems, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnViewAddToPOList, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnDeletePOItem, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPrintPOBill, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnIssuePOBill, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel20))
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(lblPOId, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel40Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTotal)
                            .addComponent(lblTotal1))))
                .addGap(20, 20, 20))
        );

        jTabbedPaneAddNewItems.addTab("View PO Items", jPanel40);

        jPanel42.setBackground(new java.awt.Color(0, 0, 0));

        tblGRN.setBackground(new java.awt.Color(0, 0, 0));
        tblGRN.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblGRN.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "GRN Item Id", "Item Name", "Item Code", "Quantity", "Item Price", "Supplier", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGRN.setGridColor(new java.awt.Color(70, 70, 70));
        tblGRN.setRequestFocusEnabled(false);
        tblGRN.setRowHeight(25);
        tblGRN.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblGRN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGRNMouseClicked(evt);
            }
        });
        jScrollPane21.setViewportView(tblGRN);

        btnCheckGRNItems.setBackground(new java.awt.Color(40, 40, 40));
        btnCheckGRNItems.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCheckGRNItems.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckGRNItems.setText("Check");
        btnCheckGRNItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckGRNItemsActionPerformed(evt);
            }
        });

        txtSearchGRNItem.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchGRNItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchGRNItem.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchGRNItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchGRNItemKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchGRNItemKeyTyped(evt);
            }
        });

        jLabel126.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        cmbSortGRNItem.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortGRNItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortGRNItem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PO Id", "GRN Id", "Checked", "Unchecked", "Bad Order" }));
        cmbSortGRNItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortGRNItemActionPerformed(evt);
            }
        });

        btnPrintGRNBill.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintGRNBill.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintGRNBill.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintGRNBill.setText("Print");
        btnPrintGRNBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintGRNBillActionPerformed(evt);
            }
        });

        btnRefreshGRNItems.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshGRNItems.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshGRNItems.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshGRNItems.setText("Refresh");
        btnRefreshGRNItems.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshGRNItemsActionPerformed(evt);
            }
        });

        lblAttendanceCount3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblAttendanceCount3.setForeground(new java.awt.Color(153, 153, 153));
        lblAttendanceCount3.setText("GRN Id : ");

        btnMarkAsBadOrder.setBackground(new java.awt.Color(40, 40, 40));
        btnMarkAsBadOrder.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnMarkAsBadOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnMarkAsBadOrder.setText("Bad Order");
        btnMarkAsBadOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarkAsBadOrderActionPerformed(evt);
            }
        });

        lblGRNId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblGRNId.setForeground(new java.awt.Color(153, 153, 153));

        lblAttendanceCount6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblAttendanceCount6.setForeground(new java.awt.Color(153, 153, 153));
        lblAttendanceCount6.setText("|");

        lblTotal2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTotal2.setForeground(new java.awt.Color(153, 153, 153));
        lblTotal2.setText("GRN Total : ");

        lblGRNTotal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblGRNTotal.setForeground(new java.awt.Color(153, 153, 153));
        lblGRNTotal.setText("$ 0.0");

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane21, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel42Layout.createSequentialGroup()
                        .addComponent(lblAttendanceCount3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGRNId, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAttendanceCount6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotal2)
                        .addGap(0, 0, 0)
                        .addComponent(lblGRNTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                        .addComponent(btnRefreshGRNItems)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMarkAsBadOrder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintGRNBill)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCheckGRNItems))
                    .addGroup(jPanel42Layout.createSequentialGroup()
                        .addComponent(txtSearchGRNItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortGRNItem, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel126)))
                .addGap(20, 20, 20))
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel126)
                    .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(txtSearchGRNItem)
                        .addComponent(cmbSortGRNItem)))
                .addGap(19, 19, 19)
                .addComponent(jScrollPane21, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblGRNId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnCheckGRNItems, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPrintGRNBill, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRefreshGRNItems, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAttendanceCount3)
                            .addComponent(btnMarkAsBadOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAttendanceCount6)
                            .addComponent(lblTotal2)))
                    .addComponent(lblGRNTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jTabbedPaneAddNewItems.addTab("GRN", jPanel42);

        jPanel43.setBackground(new java.awt.Color(0, 0, 0));

        txtGRNItemName.setBackground(new java.awt.Color(45, 45, 45));
        txtGRNItemName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGRNItemName.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemName.setEnabled(false);

        jLabel163.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel163.setForeground(new java.awt.Color(255, 255, 255));
        jLabel163.setText("Item Name");

        jLabel170.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel170.setForeground(new java.awt.Color(255, 255, 255));
        jLabel170.setText("GRN Status");

        txtGRNStatus.setBackground(new java.awt.Color(45, 45, 45));
        txtGRNStatus.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGRNStatus.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNStatus.setEnabled(false);

        txtGRNItemQuantity.setBackground(new java.awt.Color(45, 45, 45));
        txtGRNItemQuantity.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGRNItemQuantity.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemQuantity.setEnabled(false);

        jLabel150.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel150.setForeground(new java.awt.Color(255, 255, 255));
        jLabel150.setText("Quantity");

        jLabel151.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel151.setForeground(new java.awt.Color(255, 255, 255));
        jLabel151.setText("Item Price");

        txtGRNItemPrice.setBackground(new java.awt.Color(45, 45, 45));
        txtGRNItemPrice.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGRNItemPrice.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemPrice.setEnabled(false);

        txtGRNItemSellingPrice.setBackground(new java.awt.Color(18, 18, 18));
        txtGRNItemSellingPrice.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGRNItemSellingPrice.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemSellingPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGRNItemSellingPriceKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGRNItemSellingPriceKeyTyped(evt);
            }
        });

        jLabel152.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel152.setForeground(new java.awt.Color(255, 255, 255));
        jLabel152.setText("Selling Price");

        jLabel164.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel164.setForeground(new java.awt.Color(255, 255, 255));
        jLabel164.setText("Low Stock");

        txtGRNItemLowStock.setBackground(new java.awt.Color(18, 18, 18));
        txtGRNItemLowStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGRNItemLowStock.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemLowStock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGRNItemLowStockKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGRNItemLowStockKeyTyped(evt);
            }
        });

        txtGRNItemStockCount.setBackground(new java.awt.Color(45, 45, 45));
        txtGRNItemStockCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGRNItemStockCount.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemStockCount.setText("0");
        txtGRNItemStockCount.setEnabled(false);

        jLabel186.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel186.setForeground(new java.awt.Color(255, 255, 255));
        jLabel186.setText("Available Stock");

        btnCheckAndAddToStock.setBackground(new java.awt.Color(40, 40, 40));
        btnCheckAndAddToStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCheckAndAddToStock.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckAndAddToStock.setText("Check and Add to Stock");
        btnCheckAndAddToStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckAndAddToStockActionPerformed(evt);
            }
        });

        btnViewGRN.setBackground(new java.awt.Color(40, 40, 40));
        btnViewGRN.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnViewGRN.setForeground(new java.awt.Color(255, 255, 255));
        btnViewGRN.setText("View GRN");
        btnViewGRN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewGRNActionPerformed(evt);
            }
        });

        lblPOItemNameValidation2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPOItemNameValidation2.setForeground(new java.awt.Color(255, 0, 51));

        lblPOItemNameValidation3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPOItemNameValidation3.setForeground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel151, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel150, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel163, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel164, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel170, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel152, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel186, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtGRNItemQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtGRNItemPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtGRNStatus, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtGRNItemLowStock, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtGRNItemStockCount, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtGRNItemSellingPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtGRNItemName))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPOItemNameValidation2, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                            .addComponent(lblPOItemNameValidation3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addComponent(btnCheckAndAddToStock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnViewGRN)))
                .addContainerGap())
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGRNItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel163, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGRNStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel170, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel150, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGRNItemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel151, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGRNItemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel152, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtGRNItemSellingPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblPOItemNameValidation2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGRNItemLowStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel164, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPOItemNameValidation3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGRNItemStockCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel186, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCheckAndAddToStock, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewGRN, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPaneAddNewItems.addTab("GRN Item Checking", jPanel43);

        javax.swing.GroupLayout card1Layout = new javax.swing.GroupLayout(card1);
        card1.setLayout(card1Layout);
        card1Layout.setHorizontalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAddNewItems)
        );
        card1Layout.setVerticalGroup(
            card1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAddNewItems, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        subCardStock.add(card1, "card3");

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
        tblStock.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblStock.setGridColor(new java.awt.Color(70, 70, 70));
        tblStock.setRequestFocusEnabled(false);
        tblStock.setRowHeight(25);
        tblStock.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblStock.getTableHeader().setReorderingAllowed(false);
        tblStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStockMouseClicked(evt);
            }
        });
        jScrollPane19.setViewportView(tblStock);

        btnRefreshStock.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshStock.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshStock.setText("Refresh");
        btnRefreshStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshStockActionPerformed(evt);
            }
        });

        btnReOrder.setBackground(new java.awt.Color(40, 40, 40));
        btnReOrder.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnReOrder.setForeground(new java.awt.Color(255, 255, 255));
        btnReOrder.setText("Re-Order");
        btnReOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReOrderActionPerformed(evt);
            }
        });

        btnPrintAllStock.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintAllStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintAllStock.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintAllStock.setText("Print All");
        btnPrintAllStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintAllStockActionPerformed(evt);
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
        cmbSortStock.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SKU Id", "Item Code", "Item Name", "Supplier", "Selling Stock", "Whole Stock", "Low Stock" }));
        cmbSortStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortStockActionPerformed(evt);
            }
        });

        lblStockCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblStockCount.setForeground(new java.awt.Color(153, 153, 153));
        lblStockCount.setText("SKU Count : 0");

        btnPrintBarcodes.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintBarcodes.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintBarcodes.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintBarcodes.setText("Print Barcodes");
        btnPrintBarcodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintBarcodesActionPerformed(evt);
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
                        .addComponent(lblStockCount, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshStock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnReOrder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintBarcodes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintAllStock))
                    .addComponent(jScrollPane19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE))
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
                .addComponent(jScrollPane19, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefreshStock, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrintAllStock, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStockCount)
                    .addComponent(btnPrintBarcodes, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        cmbSortLowStock.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SKU Id", "Item Code", "Item Name", "Supplier" }));
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
                "SKU  Id", "Item Code", "Item Name", "Purchase Price", "Selling Price", "Supplier", "Stock Count", "Low Stock"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
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
        tblLowStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLowStockMouseClicked(evt);
            }
        });
        jScrollPane24.setViewportView(tblLowStock);
        if (tblLowStock.getColumnModel().getColumnCount() > 0) {
            tblLowStock.getColumnModel().getColumn(3).setHeaderValue("Purchase Price");
            tblLowStock.getColumnModel().getColumn(4).setHeaderValue("Selling Price");
            tblLowStock.getColumnModel().getColumn(7).setHeaderValue("Low Stock");
        }

        lblLowStockCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblLowStockCount.setForeground(new java.awt.Color(153, 153, 153));
        lblLowStockCount.setText("SKU Count : 0");

        btnPrintLowStock.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintLowStock.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintLowStock.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintLowStock.setText("Print All");
        btnPrintLowStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintLowStockActionPerformed(evt);
            }
        });

        btnReOrder1.setBackground(new java.awt.Color(40, 40, 40));
        btnReOrder1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnReOrder1.setForeground(new java.awt.Color(255, 255, 255));
        btnReOrder1.setText("Re-Order");
        btnReOrder1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReOrder1ActionPerformed(evt);
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
                        .addComponent(lblLowStockCount, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnReOrder1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintLowStock))
                    .addComponent(jScrollPane24, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
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
                .addComponent(jScrollPane24, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLowStockCount)
                    .addComponent(btnPrintLowStock, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReOrder1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        subCardStock.add(card2, "card4");

        card3.setBackground(new java.awt.Color(0, 0, 0));

        jTabbedPaneAddExistingItems.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPaneAddExistingItems.setFocusable(false);
        jTabbedPaneAddExistingItems.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPaneAddExistingItems.setOpaque(true);

        jPanel37.setBackground(new java.awt.Color(0, 0, 0));

        lblPOItemNameValidation1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPOItemNameValidation1.setForeground(new java.awt.Color(255, 0, 51));

        lblPOItemCodeValidation1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPOItemCodeValidation1.setForeground(new java.awt.Color(255, 0, 51));

        jLabel176.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel176.setForeground(new java.awt.Color(255, 255, 255));
        jLabel176.setText("PO Id");

        jLabel178.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel178.setForeground(new java.awt.Color(255, 255, 255));
        jLabel178.setText("Supplier");

        jLabel149.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel149.setForeground(new java.awt.Color(255, 255, 255));
        jLabel149.setText("Item Name");

        txtPOId1.setBackground(new java.awt.Color(45, 45, 45));
        txtPOId1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtPOId1.setForeground(new java.awt.Color(255, 255, 255));
        txtPOId1.setText("1");
        txtPOId1.setEnabled(false);

        cmbPOItemSupplier1.setBackground(new java.awt.Color(18, 18, 18));
        cmbPOItemSupplier1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbPOItemSupplier1.setForeground(new java.awt.Color(255, 255, 255));
        cmbPOItemSupplier1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPOItemSupplier1ActionPerformed(evt);
            }
        });
        cmbPOItemSupplier1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOItemSupplier1KeyPressed(evt);
            }
        });

        jSeparator2.setForeground(new java.awt.Color(100, 100, 100));

        cmbPOItemName1.setBackground(new java.awt.Color(18, 18, 18));
        cmbPOItemName1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbPOItemName1.setForeground(new java.awt.Color(255, 255, 255));
        cmbPOItemName1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOItemName1KeyPressed(evt);
            }
        });

        txtPOItemQuantity1.setBackground(new java.awt.Color(18, 18, 18));
        txtPOItemQuantity1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtPOItemQuantity1.setForeground(new java.awt.Color(255, 255, 255));
        txtPOItemQuantity1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPOItemQuantity1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPOItemQuantity1KeyTyped(evt);
            }
        });

        txtPOItemPrice1.setBackground(new java.awt.Color(18, 18, 18));
        txtPOItemPrice1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtPOItemPrice1.setForeground(new java.awt.Color(255, 255, 255));
        txtPOItemPrice1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPOItemPrice1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPOItemPrice1KeyTyped(evt);
            }
        });

        cmbPOLeadTime1.setBackground(new java.awt.Color(18, 18, 18));
        cmbPOLeadTime1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbPOLeadTime1.setForeground(new java.awt.Color(255, 255, 255));
        cmbPOLeadTime1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1 Week", "2 Weeks", "3 Weeks", "1 Month" }));
        cmbPOLeadTime1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbPOLeadTime1KeyPressed(evt);
            }
        });

        btnClearPOFields1.setBackground(new java.awt.Color(40, 40, 40));
        btnClearPOFields1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnClearPOFields1.setForeground(new java.awt.Color(255, 255, 255));
        btnClearPOFields1.setText("Clear");
        btnClearPOFields1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearPOFields1ActionPerformed(evt);
            }
        });

        btnViewPOItems1.setBackground(new java.awt.Color(40, 40, 40));
        btnViewPOItems1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnViewPOItems1.setForeground(new java.awt.Color(255, 255, 255));
        btnViewPOItems1.setText("View PO Items");
        btnViewPOItems1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewPOItems1ActionPerformed(evt);
            }
        });

        btnAddToPOList1.setBackground(new java.awt.Color(40, 40, 40));
        btnAddToPOList1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAddToPOList1.setForeground(new java.awt.Color(255, 255, 255));
        btnAddToPOList1.setText("Add to PO List");
        btnAddToPOList1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddToPOList1ActionPerformed(evt);
            }
        });

        jLabel177.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel177.setForeground(new java.awt.Color(255, 255, 255));
        jLabel177.setText("Lead Time");

        jLabel175.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel175.setForeground(new java.awt.Color(255, 255, 255));
        jLabel175.setText("Item Price");

        jLabel174.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel174.setForeground(new java.awt.Color(255, 255, 255));
        jLabel174.setText("Quantity");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel175, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel174, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel149, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel176, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel177, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel178, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAddToPOList1))
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel37Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtPOItemPrice1)
                                        .addComponent(txtPOItemQuantity1)
                                        .addComponent(cmbPOItemName1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtPOId1)
                                        .addComponent(cmbPOLeadTime1, 0, 307, Short.MAX_VALUE))
                                    .addComponent(cmbPOItemSupplier1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel37Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnViewPOItems1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnClearPOFields1))))
                    .addComponent(jSeparator2))
                .addGap(18, 18, 18)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblPOItemCodeValidation1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .addComponent(lblPOItemNameValidation1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPOId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel176, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbPOItemSupplier1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel178, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbPOItemName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel149, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel174, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtPOItemQuantity1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel175, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPOItemPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbPOLeadTime1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel177, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnClearPOFields1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnAddToPOList1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnViewPOItems1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(lblPOItemNameValidation1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblPOItemCodeValidation1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(88, Short.MAX_VALUE))
        );

        jTabbedPaneAddExistingItems.addTab("Purchase Order", jPanel37);

        jPanel46.setBackground(new java.awt.Color(0, 0, 0));

        tblPOItem1.setBackground(new java.awt.Color(0, 0, 0));
        tblPOItem1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblPOItem1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PO Item Id", "Item Name", "Item Code", "Quantity", "Item Price", "Supplier", "Total", "Expected Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPOItem1.setGridColor(new java.awt.Color(70, 70, 70));
        tblPOItem1.setRequestFocusEnabled(false);
        tblPOItem1.setRowHeight(25);
        tblPOItem1.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblPOItem1.getTableHeader().setReorderingAllowed(false);
        tblPOItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPOItem1MouseClicked(evt);
            }
        });
        jScrollPane22.setViewportView(tblPOItem1);

        btnIssuePOBill1.setBackground(new java.awt.Color(40, 40, 40));
        btnIssuePOBill1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnIssuePOBill1.setForeground(new java.awt.Color(255, 255, 255));
        btnIssuePOBill1.setText("Issue");
        btnIssuePOBill1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIssuePOBill1ActionPerformed(evt);
            }
        });

        txtSearchPOItem1.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchPOItem1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchPOItem1.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchPOItem1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchPOItem1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchPOItem1KeyTyped(evt);
            }
        });

        jLabel127.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        cmbSortPOItem1.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortPOItem1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortPOItem1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PO Id", "Item Name", "Item Code", "Supplier" }));
        cmbSortPOItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortPOItem1ActionPerformed(evt);
            }
        });

        btnPrintPOBill1.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintPOBill1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintPOBill1.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintPOBill1.setText("Print");
        btnPrintPOBill1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintPOBill1ActionPerformed(evt);
            }
        });

        btnDeletePOItem1.setBackground(new java.awt.Color(40, 40, 40));
        btnDeletePOItem1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeletePOItem1.setForeground(new java.awt.Color(255, 255, 255));
        btnDeletePOItem1.setText("Delete");
        btnDeletePOItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeletePOItem1ActionPerformed(evt);
            }
        });

        lblPOId2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPOId2.setForeground(new java.awt.Color(153, 153, 153));

        btnRefreshPOItems1.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshPOItems1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshPOItems1.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshPOItems1.setText("Refresh");
        btnRefreshPOItems1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshPOItems1ActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(153, 153, 153));
        jLabel22.setText("|");

        lblTotal3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTotal3.setForeground(new java.awt.Color(153, 153, 153));
        lblTotal3.setText("$ 0.0");

        lblPOId3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPOId3.setForeground(new java.awt.Color(153, 153, 153));
        lblPOId3.setText("PO Id :");

        btnViewAddToPOList1.setBackground(new java.awt.Color(40, 40, 40));
        btnViewAddToPOList1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnViewAddToPOList1.setForeground(new java.awt.Color(255, 255, 255));
        btnViewAddToPOList1.setText("Add PO Item");
        btnViewAddToPOList1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAddToPOList1ActionPerformed(evt);
            }
        });

        lblTotal4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTotal4.setForeground(new java.awt.Color(153, 153, 153));
        lblTotal4.setText("PO Total : ");

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addComponent(txtSearchPOItem1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortPOItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel127))
                    .addComponent(jScrollPane22)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addComponent(lblPOId3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPOId2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotal4)
                        .addGap(0, 0, 0)
                        .addComponent(lblTotal3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshPOItems1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnViewAddToPOList1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeletePOItem1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintPOBill1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnIssuePOBill1)))
                .addGap(20, 20, 20))
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel127)
                    .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearchPOItem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbSortPOItem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(19, 19, 19)
                .addComponent(jScrollPane22, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel46Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(lblPOId3))
                            .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnRefreshPOItems1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnViewAddToPOList1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnDeletePOItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPrintPOBill1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnIssuePOBill1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jLabel22))
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(lblPOId2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTotal3)
                            .addComponent(lblTotal4))))
                .addGap(20, 20, 20))
        );

        jTabbedPaneAddExistingItems.addTab("View PO Items", jPanel46);

        jPanel49.setBackground(new java.awt.Color(0, 0, 0));

        tblGRN1.setBackground(new java.awt.Color(0, 0, 0));
        tblGRN1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblGRN1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "GRN Item Id", "Item Name", "Item Code", "Quantity", "Item Price", "Supplier", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblGRN1.setGridColor(new java.awt.Color(70, 70, 70));
        tblGRN1.setRequestFocusEnabled(false);
        tblGRN1.setRowHeight(25);
        tblGRN1.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblGRN1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGRN1MouseClicked(evt);
            }
        });
        jScrollPane23.setViewportView(tblGRN1);

        btnCheckGRNItems1.setBackground(new java.awt.Color(40, 40, 40));
        btnCheckGRNItems1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCheckGRNItems1.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckGRNItems1.setText("Check");
        btnCheckGRNItems1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckGRNItems1ActionPerformed(evt);
            }
        });

        txtSearchGRNItem1.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchGRNItem1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchGRNItem1.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchGRNItem1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchGRNItem1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchGRNItem1KeyTyped(evt);
            }
        });

        jLabel128.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        cmbSortGRNItem1.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortGRNItem1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortGRNItem1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PO Id", "GRN Id", "Checked", "Unchecked", "Bad Order" }));
        cmbSortGRNItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortGRNItem1ActionPerformed(evt);
            }
        });

        btnPrintGRN1.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintGRN1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintGRN1.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintGRN1.setText("Print");
        btnPrintGRN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintGRN1ActionPerformed(evt);
            }
        });

        btnRefreshGRN1.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshGRN1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshGRN1.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshGRN1.setText("Refresh");
        btnRefreshGRN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshGRN1ActionPerformed(evt);
            }
        });

        lblAttendanceCount4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblAttendanceCount4.setForeground(new java.awt.Color(153, 153, 153));
        lblAttendanceCount4.setText("GRN Id : ");

        btnMarkAsBadOrder1.setBackground(new java.awt.Color(40, 40, 40));
        btnMarkAsBadOrder1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnMarkAsBadOrder1.setForeground(new java.awt.Color(255, 255, 255));
        btnMarkAsBadOrder1.setText("Bad Order");
        btnMarkAsBadOrder1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarkAsBadOrder1ActionPerformed(evt);
            }
        });

        lblGRNId1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblGRNId1.setForeground(new java.awt.Color(153, 153, 153));

        lblAttendanceCount7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblAttendanceCount7.setForeground(new java.awt.Color(153, 153, 153));
        lblAttendanceCount7.setText("|");

        lblTotal5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTotal5.setForeground(new java.awt.Color(153, 153, 153));
        lblTotal5.setText("GRN Total : ");

        lblGRNTotal1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblGRNTotal1.setForeground(new java.awt.Color(153, 153, 153));
        lblGRNTotal1.setText("$ 0.0");

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel49Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane23, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel49Layout.createSequentialGroup()
                        .addComponent(lblAttendanceCount4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblGRNId1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAttendanceCount7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotal5)
                        .addGap(0, 0, 0)
                        .addComponent(lblGRNTotal1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 96, Short.MAX_VALUE)
                        .addComponent(btnRefreshGRN1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMarkAsBadOrder1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintGRN1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCheckGRNItems1))
                    .addGroup(jPanel49Layout.createSequentialGroup()
                        .addComponent(txtSearchGRNItem1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortGRNItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel128)))
                .addGap(20, 20, 20))
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel49Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel128)
                    .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(txtSearchGRNItem1)
                        .addComponent(cmbSortGRNItem1)))
                .addGap(19, 19, 19)
                .addComponent(jScrollPane23, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCheckGRNItems1, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(btnPrintGRN1, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(btnRefreshGRN1, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(lblAttendanceCount4)
                    .addComponent(btnMarkAsBadOrder1, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(lblAttendanceCount7)
                    .addComponent(lblTotal5)
                    .addComponent(lblGRNId1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblGRNTotal1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );

        jTabbedPaneAddExistingItems.addTab("GRN", jPanel49);

        jPanel50.setBackground(new java.awt.Color(0, 0, 0));

        jLabel180.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel180.setForeground(new java.awt.Color(255, 255, 255));
        jLabel180.setText("Item Name");

        txtGRNStatus1.setBackground(new java.awt.Color(45, 45, 45));
        txtGRNStatus1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGRNStatus1.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNStatus1.setEnabled(false);

        txtGRNItemName1.setBackground(new java.awt.Color(45, 45, 45));
        txtGRNItemName1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGRNItemName1.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemName1.setEnabled(false);

        btnCheckAndAddToStock1.setBackground(new java.awt.Color(40, 40, 40));
        btnCheckAndAddToStock1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnCheckAndAddToStock1.setForeground(new java.awt.Color(255, 255, 255));
        btnCheckAndAddToStock1.setText("Check and Add to Stock");
        btnCheckAndAddToStock1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckAndAddToStock1ActionPerformed(evt);
            }
        });

        jLabel183.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel183.setForeground(new java.awt.Color(255, 255, 255));
        jLabel183.setText("Low Stock");

        txtGRNItemStockCount1.setBackground(new java.awt.Color(45, 45, 45));
        txtGRNItemStockCount1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGRNItemStockCount1.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemStockCount1.setEnabled(false);

        txtGRNItemSellingPrice1.setBackground(new java.awt.Color(18, 18, 18));
        txtGRNItemSellingPrice1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGRNItemSellingPrice1.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemSellingPrice1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGRNItemSellingPrice1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGRNItemSellingPrice1KeyTyped(evt);
            }
        });

        jLabel182.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel182.setForeground(new java.awt.Color(255, 255, 255));
        jLabel182.setText("Item Price");

        jLabel185.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel185.setForeground(new java.awt.Color(255, 255, 255));
        jLabel185.setText("Available Stock");

        txtGRNItemQuantity1.setBackground(new java.awt.Color(45, 45, 45));
        txtGRNItemQuantity1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGRNItemQuantity1.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemQuantity1.setEnabled(false);

        btnViewGRN1.setBackground(new java.awt.Color(40, 40, 40));
        btnViewGRN1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnViewGRN1.setForeground(new java.awt.Color(255, 255, 255));
        btnViewGRN1.setText("View GRN");
        btnViewGRN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewGRN1ActionPerformed(evt);
            }
        });

        jLabel179.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel179.setForeground(new java.awt.Color(255, 255, 255));
        jLabel179.setText("GRN Status");

        jLabel181.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel181.setForeground(new java.awt.Color(255, 255, 255));
        jLabel181.setText("Quantity");

        txtGRNItemPrice1.setBackground(new java.awt.Color(45, 45, 45));
        txtGRNItemPrice1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGRNItemPrice1.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemPrice1.setEnabled(false);

        jLabel184.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel184.setForeground(new java.awt.Color(255, 255, 255));
        jLabel184.setText("Selliing Price");

        txtGRNItemLowStock1.setBackground(new java.awt.Color(18, 18, 18));
        txtGRNItemLowStock1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGRNItemLowStock1.setForeground(new java.awt.Color(255, 255, 255));
        txtGRNItemLowStock1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGRNItemLowStock1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtGRNItemLowStock1KeyTyped(evt);
            }
        });

        lblPOItemCodeValidation2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPOItemCodeValidation2.setForeground(new java.awt.Color(255, 0, 51));

        lblPOItemCodeValidation3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPOItemCodeValidation3.setForeground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel182, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel181, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel180, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel183, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel179, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel184, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel185, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtGRNItemLowStock1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtGRNItemSellingPrice1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGRNItemPrice1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGRNItemQuantity1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGRNStatus1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGRNItemStockCount1)
                            .addComponent(txtGRNItemName1, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPOItemCodeValidation2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPOItemCodeValidation3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)))
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addComponent(btnCheckAndAddToStock1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnViewGRN1)))
                .addContainerGap())
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGRNItemName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel180, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGRNStatus1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel179, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel181, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGRNItemQuantity1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel182, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGRNItemPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel184, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtGRNItemSellingPrice1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblPOItemCodeValidation2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGRNItemLowStock1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel183, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPOItemCodeValidation3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGRNItemStockCount1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel185, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCheckAndAddToStock1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnViewGRN1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48))
        );

        jTabbedPaneAddExistingItems.addTab("GRN Item Checking", jPanel50);

        javax.swing.GroupLayout card3Layout = new javax.swing.GroupLayout(card3);
        card3.setLayout(card3Layout);
        card3Layout.setHorizontalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAddExistingItems)
        );
        card3Layout.setVerticalGroup(
            card3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneAddExistingItems)
        );

        subCardStock.add(card3, "card3");

        javax.swing.GroupLayout cardStockLayout = new javax.swing.GroupLayout(cardStock);
        cardStock.setLayout(cardStockLayout);
        cardStockLayout.setHorizontalGroup(
            cardStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(subCardStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlStockSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardStockLayout.setVerticalGroup(
            cardStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardStockLayout.createSequentialGroup()
                .addComponent(pnlStockSubSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subCardStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mainCard.add(cardStock, "card2");

        cardSales.setBackground(new java.awt.Color(0, 0, 0));

        pnlSalesSubSelection.setBackground(new java.awt.Color(20, 20, 20));

        btn_addUsers1.setBackground(new java.awt.Color(35, 35, 35));
        btn_addUsers1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_addUsers1.setForeground(new java.awt.Color(255, 255, 255));
        btn_addUsers1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_addUsers1.setText("Sales Profit");
        btn_addUsers1.setOpaque(true);
        btn_addUsers1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_addUsers1MouseClicked(evt);
            }
        });

        btn_addUsers3.setBackground(new java.awt.Color(60, 60, 60));
        btn_addUsers3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_addUsers3.setForeground(new java.awt.Color(255, 255, 255));
        btn_addUsers3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_addUsers3.setText("Sales Summary");
        btn_addUsers3.setOpaque(true);
        btn_addUsers3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_addUsers3MouseClicked(evt);
            }
        });

        btn_addUsers2.setBackground(new java.awt.Color(35, 35, 35));
        btn_addUsers2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btn_addUsers2.setForeground(new java.awt.Color(255, 255, 255));
        btn_addUsers2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_addUsers2.setText("Sales Income");
        btn_addUsers2.setOpaque(true);
        btn_addUsers2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_addUsers2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlSalesSubSelectionLayout = new javax.swing.GroupLayout(pnlSalesSubSelection);
        pnlSalesSubSelection.setLayout(pnlSalesSubSelectionLayout);
        pnlSalesSubSelectionLayout.setHorizontalGroup(
            pnlSalesSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSalesSubSelectionLayout.createSequentialGroup()
                .addComponent(btn_addUsers3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(btn_addUsers2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(btn_addUsers1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSalesSubSelectionLayout.setVerticalGroup(
            pnlSalesSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSalesSubSelectionLayout.createSequentialGroup()
                .addGroup(pnlSalesSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_addUsers1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_addUsers3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_addUsers2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        subCardSales.setBackground(new java.awt.Color(153, 153, 153));
        subCardSales.setLayout(new java.awt.CardLayout());

        card4.setBackground(new java.awt.Color(0, 0, 0));

        jTabbedPane1.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPane1.setFocusable(false);
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPane1.setOpaque(true);

        jPanel26.setBackground(new java.awt.Color(0, 0, 0));

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

        lblSalesEarning.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSalesEarning.setForeground(new java.awt.Color(153, 153, 153));
        lblSalesEarning.setText("Today Sales Income : $ 0.0");

        btnPrintTodaySales.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintTodaySales.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintTodaySales.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintTodaySales.setText("Print");
        btnPrintTodaySales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintTodaySalesActionPerformed(evt);
            }
        });

        btnRefreshTodaySales.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshTodaySales.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshTodaySales.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshTodaySales.setText("Refresh");
        btnRefreshTodaySales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshTodaySalesActionPerformed(evt);
            }
        });

        lblTodaySalesCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTodaySalesCount.setForeground(new java.awt.Color(153, 153, 153));
        lblTodaySalesCount.setText("Sales Count : 0");

        lblTodaySalesCount1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblTodaySalesCount1.setForeground(new java.awt.Color(153, 153, 153));
        lblTodaySalesCount1.setText("|");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane27)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel26Layout.createSequentialGroup()
                        .addComponent(lblTodaySalesCount, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTodaySalesCount1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSalesEarning, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
                        .addComponent(btnRefreshTodaySales)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintTodaySales))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(txtSearchTodaySales)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortTodaySales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel132)))
                .addGap(20, 20, 20))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel132)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSearchTodaySales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbSortTodaySales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane27, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblSalesEarning)
                        .addComponent(lblTodaySalesCount)
                        .addComponent(lblTodaySalesCount1))
                    .addComponent(btnRefreshTodaySales, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrintTodaySales, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jTabbedPane1.addTab("View Today Sales", jPanel26);

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
        cmbSortSalesHistory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sales / Invoice Id", "Item Name", "Date", "Month", "Year" }));
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

        btnPrintSalesHistory.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintSalesHistory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintSalesHistory.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintSalesHistory.setText("Print All");
        btnPrintSalesHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintSalesHistoryActionPerformed(evt);
            }
        });

        btnRefreshSalesHistory.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshSalesHistory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshSalesHistory.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshSalesHistory.setText("Refresh");
        btnRefreshSalesHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshSalesHistoryActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(153, 153, 153));
        jLabel16.setText("This Month Sales Income : $ 0");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(153, 153, 153));
        jLabel50.setText("This Year Sales Income : $ 0");

        jLabel58.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(153, 153, 153));
        jLabel58.setText("|");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel58)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshSalesHistory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintSalesHistory))
                    .addComponent(jScrollPane26)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(txtSearchSalesHistory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortSalesHistory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel131)))
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
                .addComponent(jScrollPane26, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPrintSalesHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefreshSalesHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel50)
                    .addComponent(jLabel58))
                .addGap(20, 20, 20))
        );

        jTabbedPane1.addTab("Sales History", jPanel19);

        javax.swing.GroupLayout card4Layout = new javax.swing.GroupLayout(card4);
        card4.setLayout(card4Layout);
        card4Layout.setHorizontalGroup(
            card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        card4Layout.setVerticalGroup(
            card4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        subCardSales.add(card4, "card2");

        card5.setBackground(new java.awt.Color(0, 0, 0));

        jLabel59.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("Today Sales Income       :    $ 0.0");

        jLabel62.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setText("This Month Sales Income       :    $ 0.0");

        jLabel65.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(255, 255, 255));
        jLabel65.setText("This Year Sales Income       :    $ 0.0");

        javax.swing.GroupLayout card5Layout = new javax.swing.GroupLayout(card5);
        card5.setLayout(card5Layout);
        card5Layout.setHorizontalGroup(
            card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card5Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(397, Short.MAX_VALUE))
        );
        card5Layout.setVerticalGroup(
            card5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card5Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel59)
                .addGap(18, 18, 18)
                .addComponent(jLabel62)
                .addGap(18, 18, 18)
                .addComponent(jLabel65)
                .addContainerGap(383, Short.MAX_VALUE))
        );

        subCardSales.add(card5, "card4");

        card6.setBackground(new java.awt.Color(0, 0, 0));

        jLabel61.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("Today Sales Profit          :    $ 0.0");

        jLabel64.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 255, 255));
        jLabel64.setText("This Month Sales Profit          :    $ 0.0");

        jLabel66.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(255, 255, 255));
        jLabel66.setText("This Year Sales Profit          :    $ 0.0");

        javax.swing.GroupLayout card6Layout = new javax.swing.GroupLayout(card6);
        card6.setLayout(card6Layout);
        card6Layout.setHorizontalGroup(
            card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card6Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(397, Short.MAX_VALUE))
        );
        card6Layout.setVerticalGroup(
            card6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card6Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel61)
                .addGap(18, 18, 18)
                .addComponent(jLabel64)
                .addGap(18, 18, 18)
                .addComponent(jLabel66)
                .addContainerGap(383, Short.MAX_VALUE))
        );

        subCardSales.add(card6, "card4");

        javax.swing.GroupLayout cardSalesLayout = new javax.swing.GroupLayout(cardSales);
        cardSales.setLayout(cardSalesLayout);
        cardSalesLayout.setHorizontalGroup(
            cardSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(subCardSales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlSalesSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardSalesLayout.setVerticalGroup(
            cardSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardSalesLayout.createSequentialGroup()
                .addComponent(pnlSalesSubSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subCardSales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainCard.add(cardSales, "card2");

        cardEmployee.setBackground(new java.awt.Color(0, 0, 0));

        pnlEmployeeSubSelection.setBackground(new java.awt.Color(20, 20, 20));

        jLabel8.setBackground(new java.awt.Color(60, 60, 60));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("New Employee");
        jLabel8.setOpaque(true);
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        jLabel21.setBackground(new java.awt.Color(35, 35, 35));
        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Manage Employee");
        jLabel21.setOpaque(true);
        jLabel21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel21MouseClicked(evt);
            }
        });

        jLabel31.setBackground(new java.awt.Color(35, 35, 35));
        jLabel31.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("Employee Payroll");
        jLabel31.setOpaque(true);
        jLabel31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel31MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlEmployeeSubSelectionLayout = new javax.swing.GroupLayout(pnlEmployeeSubSelection);
        pnlEmployeeSubSelection.setLayout(pnlEmployeeSubSelectionLayout);
        pnlEmployeeSubSelectionLayout.setHorizontalGroup(
            pnlEmployeeSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmployeeSubSelectionLayout.createSequentialGroup()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlEmployeeSubSelectionLayout.setVerticalGroup(
            pnlEmployeeSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        subCardEmployee.setBackground(new java.awt.Color(153, 153, 153));
        subCardEmployee.setLayout(new java.awt.CardLayout());

        card7.setBackground(new java.awt.Color(0, 0, 0));

        lblEmployeeUserameValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmployeeUserameValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblEmployeePositionValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmployeePositionValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblEmployeeNICValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmployeeNICValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblEmployeePhoneNoValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmployeePhoneNoValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblEmployeeSalaryValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmployeeSalaryValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblEmployeePasswordValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmployeePasswordValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblEmployeeRepeatPasswordValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmployeeRepeatPasswordValidation.setForeground(new java.awt.Color(255, 0, 51));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Repeat Password");

        pwdRepeatEmployeePassword.setBackground(new java.awt.Color(18, 18, 18));
        pwdRepeatEmployeePassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pwdRepeatEmployeePassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdRepeatEmployeePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pwdRepeatEmployeePasswordActionPerformed(evt);
            }
        });
        pwdRepeatEmployeePassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdRepeatEmployeePasswordKeyPressed(evt);
            }
        });

        btnAddEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnAddEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAddEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnAddEmployee.setText("Add New Employee");
        btnAddEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddEmployeeActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Password");

        pwdEmployeePassword.setBackground(new java.awt.Color(18, 18, 18));
        pwdEmployeePassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pwdEmployeePassword.setForeground(new java.awt.Color(255, 255, 255));
        pwdEmployeePassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pwdEmployeePasswordKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pwdEmployeePasswordKeyTyped(evt);
            }
        });

        txtEmployeeSalary.setBackground(new java.awt.Color(18, 18, 18));
        txtEmployeeSalary.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtEmployeeSalary.setForeground(new java.awt.Color(255, 255, 255));
        txtEmployeeSalary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmployeeSalaryKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmployeeSalaryKeyTyped(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Salary");

        txtEmployeePhoneNo.setBackground(new java.awt.Color(18, 18, 18));
        txtEmployeePhoneNo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtEmployeePhoneNo.setForeground(new java.awt.Color(255, 255, 255));
        txtEmployeePhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmployeePhoneNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmployeePhoneNoKeyTyped(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Phone No.");

        txtEmployeeNIC.setBackground(new java.awt.Color(18, 18, 18));
        txtEmployeeNIC.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtEmployeeNIC.setForeground(new java.awt.Color(255, 255, 255));
        txtEmployeeNIC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmployeeNICKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmployeeNICKeyTyped(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("NIC");

        txtEmployeePosition.setBackground(new java.awt.Color(18, 18, 18));
        txtEmployeePosition.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtEmployeePosition.setForeground(new java.awt.Color(255, 255, 255));
        txtEmployeePosition.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmployeePositionKeyPressed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Position");

        txtEmployeeUsername.setBackground(new java.awt.Color(18, 18, 18));
        txtEmployeeUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtEmployeeUsername.setForeground(new java.awt.Color(255, 255, 255));
        txtEmployeeUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmployeeUsernameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEmployeeUsernameKeyTyped(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Name");

        javax.swing.GroupLayout card7Layout = new javax.swing.GroupLayout(card7);
        card7.setLayout(card7Layout);
        card7Layout.setHorizontalGroup(
            card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card7Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddEmployee)
                    .addGroup(card7Layout.createSequentialGroup()
                        .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(44, 44, 44)
                        .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEmployeeSalary, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtEmployeePhoneNo, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(pwdEmployeePassword, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtEmployeePosition, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtEmployeeUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtEmployeeNIC, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(pwdRepeatEmployeePassword))
                        .addGap(18, 18, 18)
                        .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblEmployeeUserameValidation, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                            .addComponent(lblEmployeePositionValidation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEmployeeNICValidation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEmployeePhoneNoValidation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEmployeeSalaryValidation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEmployeePasswordValidation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEmployeeRepeatPasswordValidation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        card7Layout.setVerticalGroup(
            card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card7Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card7Layout.createSequentialGroup()
                        .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEmployeeUsername)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEmployeePosition)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtEmployeeNIC))
                        .addGap(18, 18, 18)
                        .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtEmployeePhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEmployeeSalary)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pwdEmployeePassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(card7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pwdRepeatEmployeePassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(card7Layout.createSequentialGroup()
                        .addComponent(lblEmployeeUserameValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblEmployeePositionValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblEmployeeNICValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblEmployeePhoneNoValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblEmployeeSalaryValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblEmployeePasswordValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblEmployeeRepeatPasswordValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35)
                .addComponent(btnAddEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(92, Short.MAX_VALUE))
        );

        subCardEmployee.add(card7, "card3");

        card8.setBackground(new java.awt.Color(20, 20, 20));

        jTabbedPaneEmployee.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPaneEmployee.setFocusable(false);
        jTabbedPaneEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPaneEmployee.setOpaque(true);

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));

        tblEmployee.setBackground(new java.awt.Color(0, 0, 0));
        tblEmployee.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblEmployee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " Id", "Name", "Position", "NIC", "Phone No.", "Salary"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmployee.setGridColor(new java.awt.Color(70, 70, 70));
        tblEmployee.setRequestFocusEnabled(false);
        tblEmployee.setRowHeight(25);
        tblEmployee.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmployeeMouseClicked(evt);
            }
        });
        jScrollPane12.setViewportView(tblEmployee);

        btnEditEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnEditEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEditEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnEditEmployee.setText("Edit");
        btnEditEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditEmployeeActionPerformed(evt);
            }
        });

        btnDeleteEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnDeleteEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeleteEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteEmployee.setText("Delete");
        btnDeleteEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteEmployeeActionPerformed(evt);
            }
        });

        btnPrintAllEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintAllEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintAllEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintAllEmployee.setText("Print All");
        btnPrintAllEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintAllEmployeeActionPerformed(evt);
            }
        });

        txtSearchEmployee.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchEmployee.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchEmployee.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchEmployeeKeyReleased(evt);
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

        cmbSortEmployee.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortEmployee.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id", "Name", "Position", "NIC", "Phone No.", "Salary" }));
        cmbSortEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortEmployeeActionPerformed(evt);
            }
        });

        lblEmployeeCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEmployeeCount.setForeground(new java.awt.Color(153, 153, 153));
        lblEmployeeCount.setText("Employee Count : 0");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(txtSearchEmployee)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(lblEmployeeCount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 377, Short.MAX_VALUE)
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
                        .addComponent(cmbSortEmployee, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtSearchEmployee, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrintAllEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefreshEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmployeeCount))
                .addGap(20, 20, 20))
        );

        jTabbedPaneEmployee.addTab("View Employee", jPanel10);

        jPanel11.setBackground(new java.awt.Color(0, 0, 0));

        lblSelectedEmployeePhoneNoValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeePhoneNoValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedEmployeeSalaryValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeeSalaryValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedEmployeeUsernameValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeeUsernameValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedEmployeeIdValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeeIdValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedEmployeePositionValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeePositionValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedEmployeeNICValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeeNICValidation.setForeground(new java.awt.Color(255, 0, 51));

        btnGoBackToViewEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnGoBackToViewEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnGoBackToViewEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnGoBackToViewEmployee.setText("Go Back");
        btnGoBackToViewEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackToViewEmployeeActionPerformed(evt);
            }
        });

        btnUpdateSelectedEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnUpdateSelectedEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnUpdateSelectedEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateSelectedEmployee.setText("Update");
        btnUpdateSelectedEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateSelectedEmployeeActionPerformed(evt);
            }
        });

        btnDeleteSelectedEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnDeleteSelectedEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeleteSelectedEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteSelectedEmployee.setText("Delete");
        btnDeleteSelectedEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSelectedEmployeeActionPerformed(evt);
            }
        });

        btnPrintSelectedEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintSelectedEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintSelectedEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintSelectedEmployee.setText("Print");
        btnPrintSelectedEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintSelectedEmployeeActionPerformed(evt);
            }
        });

        txtSelectedEmployeeSalary.setBackground(new java.awt.Color(18, 18, 18));
        txtSelectedEmployeeSalary.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedEmployeeSalary.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedEmployeeSalary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeeSalaryKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeeSalaryKeyTyped(evt);
            }
        });

        txtSelectedEmployeePhoneNo.setBackground(new java.awt.Color(18, 18, 18));
        txtSelectedEmployeePhoneNo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedEmployeePhoneNo.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedEmployeePhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeePhoneNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeePhoneNoKeyTyped(evt);
            }
        });

        txtSelectedEmployeeNIC.setBackground(new java.awt.Color(18, 18, 18));
        txtSelectedEmployeeNIC.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedEmployeeNIC.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedEmployeeNIC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeeNICKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeeNICKeyTyped(evt);
            }
        });

        txtSelectedEmployeePosition.setBackground(new java.awt.Color(18, 18, 18));
        txtSelectedEmployeePosition.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedEmployeePosition.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedEmployeePosition.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeePositionKeyPressed(evt);
            }
        });

        txtSelectedEmployeeUsername.setEditable(false);
        txtSelectedEmployeeUsername.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedEmployeeUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedEmployeeUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeeUsernameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeeUsernameKeyTyped(evt);
            }
        });

        txtSelectedEmployeeId.setEditable(false);
        txtSelectedEmployeeId.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedEmployeeId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedEmployeeId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeeIdKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedEmployeeIdKeyTyped(evt);
            }
        });

        jLabel155.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel155.setForeground(new java.awt.Color(255, 255, 255));
        jLabel155.setText("Id");

        jLabel156.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel156.setForeground(new java.awt.Color(255, 255, 255));
        jLabel156.setText("Name");

        jLabel133.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel133.setForeground(new java.awt.Color(255, 255, 255));
        jLabel133.setText("Position");

        jLabel134.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel134.setForeground(new java.awt.Color(255, 255, 255));
        jLabel134.setText("NIC");

        jLabel135.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel135.setForeground(new java.awt.Color(255, 255, 255));
        jLabel135.setText("Phone No.");

        jLabel136.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel136.setForeground(new java.awt.Color(255, 255, 255));
        jLabel136.setText("Salary");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGoBackToViewEmployee)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(btnUpdateSelectedEmployee)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteSelectedEmployee)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintSelectedEmployee))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel136, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel135, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel134, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel133, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel155, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel156, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtSelectedEmployeePhoneNo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                            .addComponent(txtSelectedEmployeeNIC, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSelectedEmployeePosition, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSelectedEmployeeUsername, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSelectedEmployeeId, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSelectedEmployeeSalary))))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSelectedEmployeeSalaryValidation, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .addComponent(lblSelectedEmployeeUsernameValidation, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .addComponent(lblSelectedEmployeePositionValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .addComponent(lblSelectedEmployeePhoneNoValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSelectedEmployeeNICValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
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
                        .addComponent(lblSelectedEmployeePositionValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedEmployeeNICValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedEmployeePhoneNoValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedEmployeeSalaryValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSelectedEmployeeId)
                            .addComponent(jLabel155, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSelectedEmployeeUsername)
                            .addComponent(jLabel156, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSelectedEmployeePosition)
                            .addComponent(jLabel133, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel134, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSelectedEmployeeNIC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel135, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSelectedEmployeePhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSelectedEmployeeSalary)
                            .addComponent(jLabel136, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUpdateSelectedEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeleteSelectedEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPrintSelectedEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGoBackToViewEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPaneEmployee.addTab("Edit Employee", jPanel11);

        jPanel13.setBackground(new java.awt.Color(0, 0, 0));

        tblEmployeeAttendance.setBackground(new java.awt.Color(0, 0, 0));
        tblEmployeeAttendance.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblEmployeeAttendance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " Id", "Name", "Month", "Year", "Date", "Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmployeeAttendance.setGridColor(new java.awt.Color(70, 70, 70));
        tblEmployeeAttendance.setRequestFocusEnabled(false);
        tblEmployeeAttendance.setRowHeight(25);
        tblEmployeeAttendance.setSelectionBackground(new java.awt.Color(0, 59, 94));
        jScrollPane14.setViewportView(tblEmployeeAttendance);
        if (tblEmployeeAttendance.getColumnModel().getColumnCount() > 0) {
            tblEmployeeAttendance.getColumnModel().getColumn(5).setHeaderValue("Time");
        }

        txtSearchEmployeeAttendance.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchEmployeeAttendance.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchEmployeeAttendance.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchEmployeeAttendance.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchEmployeeAttendanceKeyReleased(evt);
            }
        });

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        btnRefreshEmployeeAttendance.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshEmployeeAttendance.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshEmployeeAttendance.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshEmployeeAttendance.setText("Refresh");
        btnRefreshEmployeeAttendance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshEmployeeAttendanceActionPerformed(evt);
            }
        });

        cmbSortEmployeeAttendance.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortEmployeeAttendance.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortEmployeeAttendance.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Today", "Id", "Name", "Month", "Year", "Date", "Time" }));
        cmbSortEmployeeAttendance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortEmployeeAttendanceActionPerformed(evt);
            }
        });

        jXDatePicker1.setBackground(new java.awt.Color(0, 0, 0));
        jXDatePicker1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jXDatePicker1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jXDatePicker1ActionPerformed(evt);
            }
        });

        lblAttendanceCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblAttendanceCount.setForeground(new java.awt.Color(153, 153, 153));
        lblAttendanceCount.setText("Attendance Count : 0");

        lblAbsenceCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblAbsenceCount.setForeground(new java.awt.Color(153, 153, 153));
        lblAbsenceCount.setText("Absence Count : 0");

        jLabel108.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel108.setForeground(new java.awt.Color(153, 153, 153));
        jLabel108.setText("|");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane14, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(lblAttendanceCount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel108)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAbsenceCount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshEmployeeAttendance))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                        .addComponent(txtSearchEmployeeAttendance, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortEmployeeAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(jXDatePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel33)))
                .addGap(20, 20, 20))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jXDatePicker1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSearchEmployeeAttendance)
                            .addComponent(cmbSortEmployeeAttendance))))
                .addGap(19, 19, 19)
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefreshEmployeeAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAttendanceCount)
                    .addComponent(lblAbsenceCount)
                    .addComponent(jLabel108))
                .addGap(20, 20, 20))
        );

        jTabbedPaneEmployee.addTab("Employee Attendance", jPanel13);

        jPanel14.setBackground(new java.awt.Color(0, 0, 0));

        tblEmployeeMonthlyAttendance.setBackground(new java.awt.Color(0, 0, 0));
        tblEmployeeMonthlyAttendance.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblEmployeeMonthlyAttendance.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " Id", "Name", "Attendance", "Month", "Year"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmployeeMonthlyAttendance.setGridColor(new java.awt.Color(70, 70, 70));
        tblEmployeeMonthlyAttendance.setRequestFocusEnabled(false);
        tblEmployeeMonthlyAttendance.setRowHeight(25);
        tblEmployeeMonthlyAttendance.setSelectionBackground(new java.awt.Color(0, 59, 94));
        jScrollPane15.setViewportView(tblEmployeeMonthlyAttendance);

        btnRefreshMonthlyEmployeeAttendance.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshMonthlyEmployeeAttendance.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshMonthlyEmployeeAttendance.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshMonthlyEmployeeAttendance.setText("Refresh");
        btnRefreshMonthlyEmployeeAttendance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshMonthlyEmployeeAttendanceActionPerformed(evt);
            }
        });

        txtSearchMonthlyEmployeeAttendance.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchMonthlyEmployeeAttendance.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchMonthlyEmployeeAttendance.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchMonthlyEmployeeAttendance.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchMonthlyEmployeeAttendanceKeyReleased(evt);
            }
        });

        jLabel109.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        cmbSortMonthlyEmployeeAttendance.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortMonthlyEmployeeAttendance.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortMonthlyEmployeeAttendance.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id", "Name" }));
        cmbSortMonthlyEmployeeAttendance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortMonthlyEmployeeAttendanceActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                    .addComponent(btnRefreshMonthlyEmployeeAttendance, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(txtSearchMonthlyEmployeeAttendance)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortMonthlyEmployeeAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel109)))
                .addGap(20, 20, 20))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel109)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(txtSearchMonthlyEmployeeAttendance)
                        .addComponent(cmbSortMonthlyEmployeeAttendance)))
                .addGap(19, 19, 19)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addComponent(btnRefreshMonthlyEmployeeAttendance, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jTabbedPaneEmployee.addTab("Monthly Attendance", jPanel14);

        javax.swing.GroupLayout card8Layout = new javax.swing.GroupLayout(card8);
        card8.setLayout(card8Layout);
        card8Layout.setHorizontalGroup(
            card8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneEmployee)
        );
        card8Layout.setVerticalGroup(
            card8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneEmployee)
        );

        subCardEmployee.add(card8, "card3");

        card9.setBackground(new java.awt.Color(20, 20, 20));

        jTabbedPane3.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPane3.setFocusable(false);
        jTabbedPane3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPane3.setOpaque(true);

        jPanel21.setBackground(new java.awt.Color(0, 0, 0));

        tblEmployeePayroll.setBackground(new java.awt.Color(0, 0, 0));
        tblEmployeePayroll.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblEmployeePayroll.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " Id", "Name", "Month", "Year", "Date", "Attendance", "Payment", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmployeePayroll.setGridColor(new java.awt.Color(70, 70, 70));
        tblEmployeePayroll.setRequestFocusEnabled(false);
        tblEmployeePayroll.setRowHeight(25);
        tblEmployeePayroll.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblEmployeePayroll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmployeePayrollMouseClicked(evt);
            }
        });
        jScrollPane16.setViewportView(tblEmployeePayroll);

        btnRefreshEmployeePayroll.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshEmployeePayroll.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshEmployeePayroll.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshEmployeePayroll.setText("Refresh");
        btnRefreshEmployeePayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshEmployeePayrollActionPerformed(evt);
            }
        });

        btnPayEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnPayEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPayEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnPayEmployee.setText("Pay");
        btnPayEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayEmployeeActionPerformed(evt);
            }
        });

        txtSearchPayrollSummary.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchPayrollSummary.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchPayrollSummary.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchPayrollSummary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchPayrollSummaryKeyReleased(evt);
            }
        });

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        cmbSortEmployeePayroll.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortEmployeePayroll.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortEmployeePayroll.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id", "Name", "Month", "Year", "Date", "Attendance", "Payment", "Paid", "Unpaid" }));
        cmbSortEmployeePayroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortEmployeePayrollActionPerformed(evt);
            }
        });

        lblPaidCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPaidCount.setForeground(new java.awt.Color(153, 153, 153));
        lblPaidCount.setText("Paid Count : 0");

        jLabel112.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel112.setForeground(new java.awt.Color(153, 153, 153));
        jLabel112.setText("|");

        lblUnpaidCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblUnpaidCount.setForeground(new java.awt.Color(153, 153, 153));
        lblUnpaidCount.setText("Unpaid Count : 0");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addComponent(lblPaidCount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel112)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUnpaidCount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshEmployeePayroll)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPayEmployee))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addComponent(txtSearchPayrollSummary)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortEmployeePayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel35)))
                .addGap(20, 20, 20))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbSortEmployeePayroll, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtSearchPayrollSummary, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPaidCount)
                        .addComponent(lblUnpaidCount)
                        .addComponent(jLabel112))
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnPayEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnRefreshEmployeePayroll, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );

        jTabbedPane3.addTab("Payroll Summary", jPanel21);

        jPanel22.setBackground(new java.awt.Color(0, 0, 0));

        jPanel31.setBackground(new java.awt.Color(0, 0, 0));

        jLabel141.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel141.setForeground(new java.awt.Color(255, 255, 255));
        jLabel141.setText("Month");

        txtSelectedPayrollEmployeeMonth.setEditable(false);
        txtSelectedPayrollEmployeeMonth.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedPayrollEmployeeMonth.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel143.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel143.setForeground(new java.awt.Color(255, 255, 255));
        jLabel143.setText("Attendance");

        jLabel144.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel144.setForeground(new java.awt.Color(255, 255, 255));
        jLabel144.setText("Salary + Extra");

        txtSelectedPayrollEmployeeSalary.setEditable(false);
        txtSelectedPayrollEmployeeSalary.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedPayrollEmployeeSalary.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel159.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel159.setForeground(new java.awt.Color(255, 255, 255));
        jLabel159.setText("Id");

        txtSelectedPayrollEmployeeId.setEditable(false);
        txtSelectedPayrollEmployeeId.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedPayrollEmployeeId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        btnPaySelectedEmployee.setBackground(new java.awt.Color(40, 40, 40));
        btnPaySelectedEmployee.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPaySelectedEmployee.setForeground(new java.awt.Color(255, 255, 255));
        btnPaySelectedEmployee.setText("Pay");
        btnPaySelectedEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPaySelectedEmployeeActionPerformed(evt);
            }
        });

        btnGoBackToPayrollSummary.setBackground(new java.awt.Color(40, 40, 40));
        btnGoBackToPayrollSummary.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnGoBackToPayrollSummary.setForeground(new java.awt.Color(255, 255, 255));
        btnGoBackToPayrollSummary.setText("Go Back");
        btnGoBackToPayrollSummary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackToPayrollSummaryActionPerformed(evt);
            }
        });

        jLabel160.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel160.setForeground(new java.awt.Color(255, 255, 255));
        jLabel160.setText("Name");

        txtSelectedPayrollEmployeeUsername.setEditable(false);
        txtSelectedPayrollEmployeeUsername.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedPayrollEmployeeUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        btnPrintSelectedEmployeePayrollBill.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintSelectedEmployeePayrollBill.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintSelectedEmployeePayrollBill.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintSelectedEmployeePayrollBill.setText("Print");
        btnPrintSelectedEmployeePayrollBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintSelectedEmployeePayrollBillActionPerformed(evt);
            }
        });

        txtSelectedPayrollEmployeeExtraSalary.setBackground(new java.awt.Color(18, 18, 18));
        txtSelectedPayrollEmployeeExtraSalary.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedPayrollEmployeeExtraSalary.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedPayrollEmployeeExtraSalary.setText("0");
        txtSelectedPayrollEmployeeExtraSalary.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedPayrollEmployeeExtraSalaryKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedPayrollEmployeeExtraSalaryKeyTyped(evt);
            }
        });

        jLabel145.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel145.setForeground(new java.awt.Color(255, 255, 255));
        jLabel145.setText("Payment");

        txtSelectedPayrollEmployeePayment.setEditable(false);
        txtSelectedPayrollEmployeePayment.setBackground(new java.awt.Color(18, 18, 18));
        txtSelectedPayrollEmployeePayment.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedPayrollEmployeePayment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedPayrollEmployeePaymentKeyPressed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("+");

        txtSelectedPayrollEmployeeAttendance.setEditable(false);
        txtSelectedPayrollEmployeeAttendance.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedPayrollEmployeeAttendance.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel146.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel146.setForeground(new java.awt.Color(255, 255, 255));
        jLabel146.setText("Status");

        txtSelectedPayrollEmployeeStatus.setEditable(false);
        txtSelectedPayrollEmployeeStatus.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedPayrollEmployeeStatus.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(btnPaySelectedEmployee)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintSelectedEmployeePayrollBill)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGoBackToPayrollSummary)
                        .addContainerGap(225, Short.MAX_VALUE))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel144, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel143, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel141, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel159, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel160, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel146, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSelectedPayrollEmployeeUsername)
                            .addComponent(txtSelectedPayrollEmployeeMonth)
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addComponent(txtSelectedPayrollEmployeeSalary, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSelectedPayrollEmployeeExtraSalary, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE))
                            .addComponent(txtSelectedPayrollEmployeeAttendance)
                            .addComponent(txtSelectedPayrollEmployeeStatus)
                            .addComponent(txtSelectedPayrollEmployeeId)))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel145, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(txtSelectedPayrollEmployeePayment))))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSelectedPayrollEmployeeId)
                    .addComponent(jLabel159, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSelectedPayrollEmployeeUsername)
                    .addComponent(jLabel160, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSelectedPayrollEmployeeMonth)
                    .addComponent(jLabel141, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSelectedPayrollEmployeeStatus)
                    .addComponent(jLabel146, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel143, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSelectedPayrollEmployeeAttendance))
                .addGap(18, 18, 18)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSelectedPayrollEmployeeSalary)
                        .addComponent(txtSelectedPayrollEmployeeExtraSalary)
                        .addComponent(jLabel23))
                    .addComponent(jLabel144, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSelectedPayrollEmployeePayment)
                    .addComponent(jLabel145, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGoBackToPayrollSummary, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrintSelectedEmployeePayrollBill, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPaySelectedEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        lblSelectedEmployeeExtraSalaryValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeeExtraSalaryValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedEmployeePaymentValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedEmployeePaymentValidation.setForeground(new java.awt.Color(255, 0, 51));

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSelectedEmployeePaymentValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                    .addComponent(lblSelectedEmployeeExtraSalaryValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 46, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addGap(283, 283, 283)
                .addComponent(lblSelectedEmployeeExtraSalaryValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblSelectedEmployeePaymentValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Manage Payroll", jPanel22);

        jPanel23.setBackground(new java.awt.Color(0, 0, 0));

        tblEmployeePayrollHistory.setBackground(new java.awt.Color(0, 0, 0));
        tblEmployeePayrollHistory.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblEmployeePayrollHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " Id", "Name", "Month", "Year", "Date", "Attendance", "Payemnt", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmployeePayrollHistory.setGridColor(new java.awt.Color(70, 70, 70));
        tblEmployeePayrollHistory.setRequestFocusEnabled(false);
        tblEmployeePayrollHistory.setRowHeight(25);
        tblEmployeePayrollHistory.setSelectionBackground(new java.awt.Color(0, 59, 94));
        jScrollPane17.setViewportView(tblEmployeePayrollHistory);

        btnRefreshPayrollHistory.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshPayrollHistory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshPayrollHistory.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshPayrollHistory.setText("Refresh");
        btnRefreshPayrollHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshPayrollHistoryActionPerformed(evt);
            }
        });

        txtSearchPayrollHistory.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchPayrollHistory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchPayrollHistory.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchPayrollHistory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchPayrollHistoryKeyReleased(evt);
            }
        });

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        cmbSortEmployeePayrollHistory.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortEmployeePayrollHistory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortEmployeePayrollHistory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Recent", "Id", "Name", "Month", "Year", "Date", "Attendance", "Payment" }));
        cmbSortEmployeePayrollHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortEmployeePayrollHistoryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnRefreshPayrollHistory))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addComponent(txtSearchPayrollHistory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortEmployeePayrollHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36)))
                .addGap(20, 20, 20))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbSortEmployeePayrollHistory, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtSearchPayrollHistory, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane17, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addComponent(btnRefreshPayrollHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jTabbedPane3.addTab("Payroll History", jPanel23);

        javax.swing.GroupLayout card9Layout = new javax.swing.GroupLayout(card9);
        card9.setLayout(card9Layout);
        card9Layout.setHorizontalGroup(
            card9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        card9Layout.setVerticalGroup(
            card9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane3)
        );

        subCardEmployee.add(card9, "card3");

        javax.swing.GroupLayout cardEmployeeLayout = new javax.swing.GroupLayout(cardEmployee);
        cardEmployee.setLayout(cardEmployeeLayout);
        cardEmployeeLayout.setHorizontalGroup(
            cardEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(subCardEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlEmployeeSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardEmployeeLayout.setVerticalGroup(
            cardEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardEmployeeLayout.createSequentialGroup()
                .addComponent(pnlEmployeeSubSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subCardEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mainCard.add(cardEmployee, "card2");

        cardSupplier.setBackground(new java.awt.Color(0, 0, 0));

        pnlSupplierSubSelection.setBackground(new java.awt.Color(20, 20, 20));

        jLabel114.setBackground(new java.awt.Color(60, 60, 60));
        jLabel114.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel114.setForeground(new java.awt.Color(255, 255, 255));
        jLabel114.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel114.setText("New Supplier");
        jLabel114.setOpaque(true);
        jLabel114.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel114MouseClicked(evt);
            }
        });

        jLabel123.setBackground(new java.awt.Color(35, 35, 35));
        jLabel123.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel123.setForeground(new java.awt.Color(255, 255, 255));
        jLabel123.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel123.setText("Manage Supplier");
        jLabel123.setOpaque(true);
        jLabel123.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel123MouseClicked(evt);
            }
        });

        jLabel124.setBackground(new java.awt.Color(35, 35, 35));
        jLabel124.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel124.setForeground(new java.awt.Color(255, 255, 255));
        jLabel124.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel124.setText("Manage Items");
        jLabel124.setOpaque(true);
        jLabel124.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel124MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlSupplierSubSelectionLayout = new javax.swing.GroupLayout(pnlSupplierSubSelection);
        pnlSupplierSubSelection.setLayout(pnlSupplierSubSelectionLayout);
        pnlSupplierSubSelectionLayout.setHorizontalGroup(
            pnlSupplierSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSupplierSubSelectionLayout.createSequentialGroup()
                .addComponent(jLabel114, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jLabel123, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jLabel124, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlSupplierSubSelectionLayout.setVerticalGroup(
            pnlSupplierSubSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel114, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
            .addComponent(jLabel123, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel124, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        subCardSupplier.setBackground(new java.awt.Color(20, 20, 20));
        subCardSupplier.setLayout(new java.awt.CardLayout());

        card10.setBackground(new java.awt.Color(0, 0, 0));

        lblSupplierAddressValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSupplierAddressValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSupplierUserameValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSupplierUserameValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSupplierEmailValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSupplierEmailValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSupplierPhoneNoValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSupplierPhoneNoValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSupplierCompanyValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSupplierCompanyValidation.setForeground(new java.awt.Color(255, 0, 51));

        btnAddSupplier.setBackground(new java.awt.Color(40, 40, 40));
        btnAddSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAddSupplier.setForeground(new java.awt.Color(255, 255, 255));
        btnAddSupplier.setText("Add New Supplier");
        btnAddSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSupplierActionPerformed(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Company");

        txtSupplierCompany.setBackground(new java.awt.Color(18, 18, 18));
        txtSupplierCompany.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSupplierCompany.setForeground(new java.awt.Color(255, 255, 255));
        txtSupplierCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSupplierCompanyActionPerformed(evt);
            }
        });
        txtSupplierCompany.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSupplierCompanyKeyPressed(evt);
            }
        });

        txtSupplierPhoneNo.setBackground(new java.awt.Color(18, 18, 18));
        txtSupplierPhoneNo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSupplierPhoneNo.setForeground(new java.awt.Color(255, 255, 255));
        txtSupplierPhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSupplierPhoneNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSupplierPhoneNoKeyTyped(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Phone No.");

        jLabel38.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("E-mail");

        txtSupplierEmail.setBackground(new java.awt.Color(18, 18, 18));
        txtSupplierEmail.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSupplierEmail.setForeground(new java.awt.Color(255, 255, 255));
        txtSupplierEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSupplierEmailKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSupplierEmailKeyTyped(evt);
            }
        });

        txtSupplierAddress.setBackground(new java.awt.Color(18, 18, 18));
        txtSupplierAddress.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSupplierAddress.setForeground(new java.awt.Color(255, 255, 255));
        txtSupplierAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSupplierAddressKeyPressed(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Address");

        jLabel43.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Name");

        txtSupplierUsername.setBackground(new java.awt.Color(18, 18, 18));
        txtSupplierUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSupplierUsername.setForeground(new java.awt.Color(255, 255, 255));
        txtSupplierUsername.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSupplierUsernameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSupplierUsernameKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout card10Layout = new javax.swing.GroupLayout(card10);
        card10.setLayout(card10Layout);
        card10Layout.setHorizontalGroup(
            card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card10Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddSupplier)
                    .addGroup(card10Layout.createSequentialGroup()
                        .addGroup(card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSupplierPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSupplierAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSupplierEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSupplierUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSupplierCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSupplierAddressValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSupplierUserameValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSupplierEmailValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSupplierPhoneNoValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSupplierCompanyValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        card10Layout.setVerticalGroup(
            card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card10Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card10Layout.createSequentialGroup()
                        .addGroup(card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSupplierUsername)
                            .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSupplierAddress)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSupplierEmail))
                        .addGap(18, 18, 18)
                        .addGroup(card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSupplierPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(card10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSupplierCompany)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addComponent(btnAddSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(card10Layout.createSequentialGroup()
                        .addComponent(lblSupplierUserameValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSupplierAddressValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSupplierEmailValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSupplierPhoneNoValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSupplierCompanyValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(190, Short.MAX_VALUE))
        );

        subCardSupplier.add(card10, "card2");

        card11.setBackground(new java.awt.Color(20, 20, 20));

        jTabbedPaneSupplier.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPaneSupplier.setFocusable(false);
        jTabbedPaneSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPaneSupplier.setOpaque(true);

        jPanel29.setBackground(new java.awt.Color(0, 0, 0));

        tblSupplier.setBackground(new java.awt.Color(0, 0, 0));
        tblSupplier.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblSupplier.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                " Id", "Name", "Address", "Email", "Phone No.", "Company"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSupplier.setGridColor(new java.awt.Color(70, 70, 70));
        tblSupplier.setRequestFocusEnabled(false);
        tblSupplier.setRowHeight(25);
        tblSupplier.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblSupplier.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSupplierMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(tblSupplier);

        btnEditSupplier.setBackground(new java.awt.Color(40, 40, 40));
        btnEditSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEditSupplier.setForeground(new java.awt.Color(255, 255, 255));
        btnEditSupplier.setText("Edit");
        btnEditSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditSupplierActionPerformed(evt);
            }
        });

        btnDeleteSupplier.setBackground(new java.awt.Color(40, 40, 40));
        btnDeleteSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeleteSupplier.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteSupplier.setText("Delete");
        btnDeleteSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSupplierActionPerformed(evt);
            }
        });

        btnPrintAllSupplier.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintAllSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintAllSupplier.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintAllSupplier.setText("Print All");
        btnPrintAllSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintAllSupplierActionPerformed(evt);
            }
        });

        txtSearchSupplier.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchSupplier.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchSupplierKeyReleased(evt);
            }
        });

        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        btnRefreshSupplier.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshSupplier.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshSupplier.setText("Refresh");
        btnRefreshSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshSupplierActionPerformed(evt);
            }
        });

        cmbSortSupplier.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortSupplier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id", "Name", "Address", "E-mail", "Phone No.", "Company" }));
        cmbSortSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortSupplierActionPerformed(evt);
            }
        });

        lblSupplierCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSupplierCount.setForeground(new java.awt.Color(153, 153, 153));
        lblSupplierCount.setText("Supplier Count : 0");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(txtSearchSupplier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel41))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                        .addComponent(lblSupplierCount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 390, Short.MAX_VALUE)
                        .addComponent(btnRefreshSupplier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditSupplier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteSupplier)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrintAllSupplier))
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(20, 20, 20))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41)
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbSortSupplier, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtSearchSupplier, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrintAllSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefreshSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSupplierCount))
                .addGap(20, 20, 20))
        );

        jTabbedPaneSupplier.addTab("View Supplier", jPanel29);

        jPanel32.setBackground(new java.awt.Color(0, 0, 0));

        lblSelectedSupplierPhoneNoValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedSupplierPhoneNoValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedSupplierCompanyValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedSupplierCompanyValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedSupplierUsernameValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedSupplierUsernameValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedSupplierIdValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedSupplierIdValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedSupplierAddressValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedSupplierAddressValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedSupplierEmailValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedSupplierEmailValidation.setForeground(new java.awt.Color(255, 0, 51));

        btnGoBackToViewSupplier.setBackground(new java.awt.Color(40, 40, 40));
        btnGoBackToViewSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnGoBackToViewSupplier.setForeground(new java.awt.Color(255, 255, 255));
        btnGoBackToViewSupplier.setText("Go Back");
        btnGoBackToViewSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackToViewSupplierActionPerformed(evt);
            }
        });

        btnUpdateSelectedSupplier.setBackground(new java.awt.Color(40, 40, 40));
        btnUpdateSelectedSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnUpdateSelectedSupplier.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateSelectedSupplier.setText("Update");
        btnUpdateSelectedSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateSelectedSupplierActionPerformed(evt);
            }
        });

        btnDeleteSelectedSupplier.setBackground(new java.awt.Color(40, 40, 40));
        btnDeleteSelectedSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeleteSelectedSupplier.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteSelectedSupplier.setText("Delete");
        btnDeleteSelectedSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSelectedSupplierActionPerformed(evt);
            }
        });

        btnPrintSelectedSupplier.setBackground(new java.awt.Color(40, 40, 40));
        btnPrintSelectedSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnPrintSelectedSupplier.setForeground(new java.awt.Color(255, 255, 255));
        btnPrintSelectedSupplier.setText("Print");
        btnPrintSelectedSupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintSelectedSupplierActionPerformed(evt);
            }
        });

        txtSelectedSupplierCompany.setBackground(new java.awt.Color(18, 18, 18));
        txtSelectedSupplierCompany.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedSupplierCompany.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedSupplierCompany.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedSupplierCompanyKeyPressed(evt);
            }
        });

        jLabel140.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel140.setForeground(new java.awt.Color(255, 255, 255));
        jLabel140.setText("Company");

        jLabel139.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel139.setForeground(new java.awt.Color(255, 255, 255));
        jLabel139.setText("Phone No.");

        jLabel138.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel138.setForeground(new java.awt.Color(255, 255, 255));
        jLabel138.setText("E-mail");

        jLabel137.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel137.setForeground(new java.awt.Color(255, 255, 255));
        jLabel137.setText("Address");

        jLabel158.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel158.setForeground(new java.awt.Color(255, 255, 255));
        jLabel158.setText("Name");

        jLabel157.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel157.setForeground(new java.awt.Color(255, 255, 255));
        jLabel157.setText("Id");

        txtSelectedSupplierId.setEditable(false);
        txtSelectedSupplierId.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedSupplierId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtSelectedSupplierUsername.setEditable(false);
        txtSelectedSupplierUsername.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedSupplierUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        txtSelectedSupplierAddress.setBackground(new java.awt.Color(18, 18, 18));
        txtSelectedSupplierAddress.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedSupplierAddress.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedSupplierAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedSupplierAddressKeyPressed(evt);
            }
        });

        txtSelectedSupplierEmail.setBackground(new java.awt.Color(18, 18, 18));
        txtSelectedSupplierEmail.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedSupplierEmail.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedSupplierEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedSupplierEmailKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedSupplierEmailKeyTyped(evt);
            }
        });

        txtSelectedSupplierPhoneNo.setBackground(new java.awt.Color(18, 18, 18));
        txtSelectedSupplierPhoneNo.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedSupplierPhoneNo.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedSupplierPhoneNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedSupplierPhoneNoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSelectedSupplierPhoneNoKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGoBackToViewSupplier)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel140, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel139, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel138, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel137, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel157, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel158, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel32Layout.createSequentialGroup()
                                .addComponent(btnUpdateSelectedSupplier)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDeleteSelectedSupplier)))
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtSelectedSupplierUsername, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                                .addComponent(txtSelectedSupplierAddress)
                                .addComponent(txtSelectedSupplierEmail)
                                .addComponent(txtSelectedSupplierPhoneNo)
                                .addComponent(txtSelectedSupplierCompany)
                                .addComponent(txtSelectedSupplierId))
                            .addGroup(jPanel32Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnPrintSelectedSupplier)))))
                .addGap(30, 30, 30)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSelectedSupplierCompanyValidation, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(lblSelectedSupplierUsernameValidation, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(lblSelectedSupplierIdValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(lblSelectedSupplierAddressValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(lblSelectedSupplierPhoneNoValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSelectedSupplierEmailValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addComponent(lblSelectedSupplierIdValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedSupplierUsernameValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedSupplierAddressValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedSupplierEmailValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedSupplierPhoneNoValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedSupplierCompanyValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSelectedSupplierId)
                            .addComponent(jLabel157, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSelectedSupplierUsername)
                            .addComponent(jLabel158, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSelectedSupplierAddress)
                            .addComponent(jLabel137, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel138, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSelectedSupplierEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel139, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSelectedSupplierPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSelectedSupplierCompany)
                            .addComponent(jLabel140, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUpdateSelectedSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeleteSelectedSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPrintSelectedSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGoBackToViewSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPaneSupplier.addTab("Edit Supplier", jPanel32);

        javax.swing.GroupLayout card11Layout = new javax.swing.GroupLayout(card11);
        card11.setLayout(card11Layout);
        card11Layout.setHorizontalGroup(
            card11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneSupplier)
        );
        card11Layout.setVerticalGroup(
            card11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneSupplier)
        );

        subCardSupplier.add(card11, "card3");

        card12.setBackground(new java.awt.Color(20, 20, 20));

        jTabbedPaneSupplierItem.setBackground(new java.awt.Color(0, 0, 0));
        jTabbedPaneSupplierItem.setFocusable(false);
        jTabbedPaneSupplierItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jTabbedPaneSupplierItem.setOpaque(true);

        jPanel16.setBackground(new java.awt.Color(0, 0, 0));

        lblItemNameValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblItemNameValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblItemCodeValidation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblItemCodeValidation.setForeground(new java.awt.Color(255, 0, 51));

        lblPOItemQuantityValidation1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPOItemQuantityValidation1.setForeground(new java.awt.Color(255, 0, 51));

        btnSaveItem.setBackground(new java.awt.Color(40, 40, 40));
        btnSaveItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSaveItem.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveItem.setText("Add New Item");
        btnSaveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveItemActionPerformed(evt);
            }
        });

        jLabel169.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel169.setForeground(new java.awt.Color(255, 255, 255));
        jLabel169.setText("Supplier");

        cmbItemSupplier.setBackground(new java.awt.Color(18, 18, 18));
        cmbItemSupplier.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbItemSupplier.setForeground(new java.awt.Color(255, 255, 255));
        cmbItemSupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbItemSupplierKeyPressed(evt);
            }
        });

        cmbItemCategory.setBackground(new java.awt.Color(18, 18, 18));
        cmbItemCategory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbItemCategory.setForeground(new java.awt.Color(255, 255, 255));
        cmbItemCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Computers", "Displays", "Home", "Phones", "Tablets", "Watches", "Other" }));
        cmbItemCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbItemCategoryKeyPressed(evt);
            }
        });

        jLabel171.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel171.setForeground(new java.awt.Color(255, 255, 255));
        jLabel171.setText("Item Category");

        jLabel168.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel168.setForeground(new java.awt.Color(255, 255, 255));
        jLabel168.setText("Item Code");

        txtItemCode.setBackground(new java.awt.Color(18, 18, 18));
        txtItemCode.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtItemCode.setForeground(new java.awt.Color(255, 255, 255));
        txtItemCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtItemCodeKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtItemCodeKeyTyped(evt);
            }
        });

        txtItemName.setBackground(new java.awt.Color(18, 18, 18));
        txtItemName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtItemName.setForeground(new java.awt.Color(255, 255, 255));
        txtItemName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtItemNameKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtItemNameKeyTyped(evt);
            }
        });

        jLabel165.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel165.setForeground(new java.awt.Color(255, 255, 255));
        jLabel165.setText("Item Name");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel169, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(cmbItemSupplier, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnSaveItem)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel165, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel168, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel171, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtItemName)
                            .addComponent(txtItemCode)
                            .addComponent(cmbItemCategory, 0, 301, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblItemNameValidation, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                    .addComponent(lblItemCodeValidation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
                    .addComponent(lblPOItemQuantityValidation1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(lblItemNameValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblItemCodeValidation, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblPOItemQuantityValidation1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel165, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtItemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel168, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel171, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbItemCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel169, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbItemSupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addComponent(btnSaveItem, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(213, Short.MAX_VALUE))
        );

        jTabbedPaneSupplierItem.addTab("New Item", jPanel16);

        jPanel41.setBackground(new java.awt.Color(0, 0, 0));

        tblSupplierItem.setBackground(new java.awt.Color(0, 0, 0));
        tblSupplierItem.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblSupplierItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier Id", "Supplier Name", "Item Name", "Item Code", "Item Category", "Availability"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSupplierItem.setGridColor(new java.awt.Color(70, 70, 70));
        tblSupplierItem.setRequestFocusEnabled(false);
        tblSupplierItem.setRowHeight(25);
        tblSupplierItem.setSelectionBackground(new java.awt.Color(0, 59, 94));
        tblSupplierItem.getTableHeader().setReorderingAllowed(false);
        tblSupplierItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSupplierItemMouseClicked(evt);
            }
        });
        jScrollPane18.setViewportView(tblSupplierItem);

        btnDeleteSupplierItem.setBackground(new java.awt.Color(40, 40, 40));
        btnDeleteSupplierItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeleteSupplierItem.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteSupplierItem.setText("Delete");
        btnDeleteSupplierItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSupplierItemActionPerformed(evt);
            }
        });

        btnEditSupplierItem.setBackground(new java.awt.Color(40, 40, 40));
        btnEditSupplierItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEditSupplierItem.setForeground(new java.awt.Color(255, 255, 255));
        btnEditSupplierItem.setText("Edit");
        btnEditSupplierItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditSupplierItemActionPerformed(evt);
            }
        });

        txtSearchSupplierItem.setBackground(new java.awt.Color(0, 0, 0));
        txtSearchSupplierItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSearchSupplierItem.setForeground(new java.awt.Color(255, 255, 255));
        txtSearchSupplierItem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchSupplierItemKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSearchSupplierItemKeyTyped(evt);
            }
        });

        jLabel54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/search.icon.png"))); // NOI18N

        cmbSortSupplierItem.setBackground(new java.awt.Color(0, 0, 0));
        cmbSortSupplierItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSortSupplierItem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Supplier Id", "Supplier Name", "Item Name", "Item Code", "Item Category", "Available", "Unavailable", "Whole Items" }));
        cmbSortSupplierItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbSortSupplierItemActionPerformed(evt);
            }
        });

        lblItemCount.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblItemCount.setForeground(new java.awt.Color(153, 153, 153));
        lblItemCount.setText("Item Count : *");

        btnRefreshSupplierItem.setBackground(new java.awt.Color(40, 40, 40));
        btnRefreshSupplierItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnRefreshSupplierItem.setForeground(new java.awt.Color(255, 255, 255));
        btnRefreshSupplierItem.setText("Refresh");
        btnRefreshSupplierItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshSupplierItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel41Layout.createSequentialGroup()
                        .addComponent(txtSearchSupplierItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbSortSupplierItem, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel54))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel41Layout.createSequentialGroup()
                        .addComponent(lblItemCount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRefreshSupplierItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteSupplierItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditSupplierItem))
                    .addComponent(jScrollPane18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel54)
                    .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cmbSortSupplierItem, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtSearchSupplierItem, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane18, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditSupplierItem, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteSupplierItem, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblItemCount)
                    .addComponent(btnRefreshSupplierItem, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jTabbedPaneSupplierItem.addTab("View Item", jPanel41);

        jPanel44.setBackground(new java.awt.Color(0, 0, 0));

        lblSelectedSupplierPhoneNoValidation2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedSupplierPhoneNoValidation2.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedSupplierCompanyValidation2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedSupplierCompanyValidation2.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedSupplierUsernameValidation2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedSupplierUsernameValidation2.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedSupplierIdValidation2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedSupplierIdValidation2.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedSupplierAddressValidation2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedSupplierAddressValidation2.setForeground(new java.awt.Color(255, 0, 51));

        lblSelectedSupplierEmailValidation2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedSupplierEmailValidation2.setForeground(new java.awt.Color(255, 0, 51));

        jLabel172.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel172.setForeground(new java.awt.Color(255, 255, 255));
        jLabel172.setText("Supplier Id");

        txtSelectedItemSupplierId.setEditable(false);
        txtSelectedItemSupplierId.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedItemSupplierId.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel173.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel173.setForeground(new java.awt.Color(255, 255, 255));
        jLabel173.setText("Supplier Name");

        txtSelectedItemSupplierUsername.setEditable(false);
        txtSelectedItemSupplierUsername.setBackground(new java.awt.Color(45, 45, 45));
        txtSelectedItemSupplierUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jLabel153.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel153.setForeground(new java.awt.Color(255, 255, 255));
        jLabel153.setText("Item Name");

        txtSelectedItemSupplierItemName.setBackground(new java.awt.Color(18, 18, 18));
        txtSelectedItemSupplierItemName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedItemSupplierItemName.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedItemSupplierItemName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedItemSupplierItemNameKeyPressed(evt);
            }
        });

        jLabel162.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel162.setForeground(new java.awt.Color(255, 255, 255));
        jLabel162.setText("Item Code");

        txtSelectedItemSupplierItemCode.setBackground(new java.awt.Color(18, 18, 18));
        txtSelectedItemSupplierItemCode.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSelectedItemSupplierItemCode.setForeground(new java.awt.Color(255, 255, 255));
        txtSelectedItemSupplierItemCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSelectedItemSupplierItemCodeKeyPressed(evt);
            }
        });

        jLabel167.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel167.setForeground(new java.awt.Color(255, 255, 255));
        jLabel167.setText("Item Category");

        cmbSelectedItemSupplierItemCategory.setBackground(new java.awt.Color(18, 18, 18));
        cmbSelectedItemSupplierItemCategory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cmbSelectedItemSupplierItemCategory.setForeground(new java.awt.Color(255, 255, 255));
        cmbSelectedItemSupplierItemCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Computers", "Displays", "Home", "Phones", "Tablets", "Watches", "Other" }));
        cmbSelectedItemSupplierItemCategory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbSelectedItemSupplierItemCategoryKeyPressed(evt);
            }
        });

        btnDeleteSelectedSupplierItem.setBackground(new java.awt.Color(40, 40, 40));
        btnDeleteSelectedSupplierItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnDeleteSelectedSupplierItem.setForeground(new java.awt.Color(255, 255, 255));
        btnDeleteSelectedSupplierItem.setText("Delete");
        btnDeleteSelectedSupplierItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteSelectedSupplierItemActionPerformed(evt);
            }
        });

        btnUpdateSelectedSupplierItem.setBackground(new java.awt.Color(40, 40, 40));
        btnUpdateSelectedSupplierItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnUpdateSelectedSupplierItem.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateSelectedSupplierItem.setText("Update");
        btnUpdateSelectedSupplierItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateSelectedSupplierItemActionPerformed(evt);
            }
        });

        btnGoBackToViewItem.setBackground(new java.awt.Color(40, 40, 40));
        btnGoBackToViewItem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnGoBackToViewItem.setForeground(new java.awt.Color(255, 255, 255));
        btnGoBackToViewItem.setText("Go Back");
        btnGoBackToViewItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoBackToViewItemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGoBackToViewItem)
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addComponent(btnUpdateSelectedSupplierItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDeleteSelectedSupplierItem))
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel167, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel162, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel153, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel172, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel173, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSelectedItemSupplierId)
                            .addComponent(txtSelectedItemSupplierUsername)
                            .addComponent(txtSelectedItemSupplierItemName)
                            .addComponent(txtSelectedItemSupplierItemCode)
                            .addComponent(cmbSelectedItemSupplierItemCategory, 0, 301, Short.MAX_VALUE))))
                .addGap(28, 28, 28)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSelectedSupplierCompanyValidation2, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                    .addComponent(lblSelectedSupplierUsernameValidation2, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                    .addComponent(lblSelectedSupplierIdValidation2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                    .addComponent(lblSelectedSupplierAddressValidation2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                    .addComponent(lblSelectedSupplierPhoneNoValidation2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblSelectedSupplierEmailValidation2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addComponent(lblSelectedSupplierIdValidation2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedSupplierUsernameValidation2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedSupplierAddressValidation2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedSupplierEmailValidation2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedSupplierPhoneNoValidation2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblSelectedSupplierCompanyValidation2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSelectedItemSupplierId)
                            .addComponent(jLabel172, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSelectedItemSupplierUsername)
                            .addComponent(jLabel173, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSelectedItemSupplierItemName)
                            .addComponent(jLabel153, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel162, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSelectedItemSupplierItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel167, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbSelectedItemSupplierItemCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUpdateSelectedSupplierItem, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDeleteSelectedSupplierItem, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGoBackToViewItem, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPaneSupplierItem.addTab("Edit Item", jPanel44);

        javax.swing.GroupLayout card12Layout = new javax.swing.GroupLayout(card12);
        card12.setLayout(card12Layout);
        card12Layout.setHorizontalGroup(
            card12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneSupplierItem)
        );
        card12Layout.setVerticalGroup(
            card12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneSupplierItem)
        );

        subCardSupplier.add(card12, "card3");

        javax.swing.GroupLayout cardSupplierLayout = new javax.swing.GroupLayout(cardSupplier);
        cardSupplier.setLayout(cardSupplierLayout);
        cardSupplierLayout.setHorizontalGroup(
            cardSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlSupplierSubSelection, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(subCardSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cardSupplierLayout.setVerticalGroup(
            cardSupplierLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardSupplierLayout.createSequentialGroup()
                .addComponent(pnlSupplierSubSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subCardSupplier, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainCard.add(cardSupplier, "card2");

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

        card13.setBackground(new java.awt.Color(0, 0, 0));

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

        javax.swing.GroupLayout card13Layout = new javax.swing.GroupLayout(card13);
        card13.setLayout(card13Layout);
        card13Layout.setHorizontalGroup(
            card13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card13Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(card13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(card13Layout.createSequentialGroup()
                        .addComponent(btnBrowseBackupPath)
                        .addGap(18, 18, 18)
                        .addComponent(btnBackup))
                    .addComponent(txtBackupLocation))
                .addContainerGap(467, Short.MAX_VALUE))
        );
        card13Layout.setVerticalGroup(
            card13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card13Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(txtBackupLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(card13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBrowseBackupPath, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBackup, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(332, Short.MAX_VALUE))
        );

        subCardSettings.add(card13, "card2");

        card14.setBackground(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Restore Database");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(150, 150, 150));
        jLabel11.setText("Open Location");

        txtRestoreLocation.setBackground(new java.awt.Color(18, 18, 18));
        txtRestoreLocation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtRestoreLocation.setForeground(new java.awt.Color(255, 255, 255));

        btnBrowseRestoreLocation.setBackground(new java.awt.Color(40, 40, 40));
        btnBrowseRestoreLocation.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnBrowseRestoreLocation.setForeground(new java.awt.Color(255, 255, 255));
        btnBrowseRestoreLocation.setText("Browse Path");
        btnBrowseRestoreLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseRestoreLocationActionPerformed(evt);
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

        javax.swing.GroupLayout card14Layout = new javax.swing.GroupLayout(card14);
        card14.setLayout(card14Layout);
        card14Layout.setHorizontalGroup(
            card14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card14Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(card14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(card14Layout.createSequentialGroup()
                            .addComponent(btnBrowseRestoreLocation)
                            .addGap(18, 18, 18)
                            .addComponent(btnRestore)))
                    .addComponent(txtRestoreLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(467, Short.MAX_VALUE))
        );
        card14Layout.setVerticalGroup(
            card14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card14Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(txtRestoreLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(card14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBrowseRestoreLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRestore, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(332, Short.MAX_VALUE))
        );

        subCardSettings.add(card14, "card3");

        card15.setBackground(new java.awt.Color(0, 0, 0));

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

        javax.swing.GroupLayout card15Layout = new javax.swing.GroupLayout(card15);
        card15.setLayout(card15Layout);
        card15Layout.setHorizontalGroup(
            card15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card15Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(card15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47)
                    .addComponent(jLabel18)
                    .addComponent(version)
                    .addGroup(card15Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(card15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(card15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(card15Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel15)
                    .addComponent(jLabel48))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        card15Layout.setVerticalGroup(
            card15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card15Layout.createSequentialGroup()
                .addGroup(card15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card15Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addGroup(card15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(card15Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(card15Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(card15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(card15Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(card15Layout.createSequentialGroup()
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
                .addContainerGap(170, Short.MAX_VALUE))
        );

        subCardSettings.add(card15, "card4");

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
                    .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, 941, Short.MAX_VALUE)
                    .addComponent(mainCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlParentLayout.setVerticalGroup(
            pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParentLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlAdminDashboardLayout = new javax.swing.GroupLayout(pnlAdminDashboard);
        pnlAdminDashboard.setLayout(pnlAdminDashboardLayout);
        pnlAdminDashboardLayout.setHorizontalGroup(
            pnlAdminDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(pnlTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlAdminDashboardLayout.createSequentialGroup()
                .addComponent(pnlSidebar, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlParent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlAdminDashboardLayout.setVerticalGroup(
            pnlAdminDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdminDashboardLayout.createSequentialGroup()
                .addComponent(pnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlAdminDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlParent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSidebar, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)))
        );

        pnlFrames.add(pnlAdminDashboard, "card2");

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
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                .addGap(50, 50, 50)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE))
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlFrames, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        setSize(new java.awt.Dimension(1200, 690));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pnlTopMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTopMousePressed
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            if (AdminDashboard.this.getExtendedState() == MAXIMIZED_BOTH) {
                AdminDashboard.this.setExtendedState(JFrame.NORMAL);
                maximizeView.setVisible(false);
                restoreDownView.setVisible(true);
            } else {
                AdminDashboard.this.setExtendedState(MAXIMIZED_BOTH);
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

        if (AdminDashboard.this.getExtendedState() != MAXIMIZED_BOTH) {
            this.setLocation(locX - mousepX, locY - mousepY);
            setOpacity((float) (0.97));
        }

    }//GEN-LAST:event_pnlTopMouseDragged

    private void homeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeMouseClicked
        setPnlColor(pnlHome);
        resetPnlColor(pnlStock);
        resetPnlColor(pnlSales);
        resetPnlColor(pnlEmployee);
        resetPnlColor(pnlSupplier);
        resetPnlColor(pnlSettings);

        setPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorStock);
        resetPnlIndicatorColor(pnlIndicatorSales);
        resetPnlIndicatorColor(pnlIndicatorEmployee);
        resetPnlIndicatorColor(pnlIndicatorSupplier);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Home");

        cardHome.setVisible(true);
        cardStock.setVisible(false);
        cardSales.setVisible(false);
        cardEmployee.setVisible(false);
        cardSupplier.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_homeMouseClicked

    private void stockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stockMouseClicked
        resetPnlColor(pnlHome);
        setPnlColor(pnlStock);
        resetPnlColor(pnlSales);
        resetPnlColor(pnlEmployee);
        resetPnlColor(pnlSupplier);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        setPnlIndicatorColor(pnlIndicatorStock);
        resetPnlIndicatorColor(pnlIndicatorSales);
        resetPnlIndicatorColor(pnlIndicatorEmployee);
        resetPnlIndicatorColor(pnlIndicatorSupplier);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Stock");

        cardHome.setVisible(false);
        cardStock.setVisible(true);
        cardSales.setVisible(false);
        cardEmployee.setVisible(false);
        cardSupplier.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_stockMouseClicked

    private void salesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlStock);
        setPnlColor(pnlSales);
        resetPnlColor(pnlEmployee);
        resetPnlColor(pnlSupplier);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorStock);
        setPnlIndicatorColor(pnlIndicatorSales);
        resetPnlIndicatorColor(pnlIndicatorEmployee);
        resetPnlIndicatorColor(pnlIndicatorSupplier);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Sales");

        cardHome.setVisible(false);
        cardStock.setVisible(false);
        cardSales.setVisible(true);
        cardEmployee.setVisible(false);
        cardSupplier.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_salesMouseClicked

    private void employeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeeMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlStock);
        resetPnlColor(pnlSales);
        setPnlColor(pnlEmployee);
        resetPnlColor(pnlSupplier);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorStock);
        resetPnlIndicatorColor(pnlIndicatorSales);
        setPnlIndicatorColor(pnlIndicatorEmployee);
        resetPnlIndicatorColor(pnlIndicatorSupplier);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Employee");

        cardHome.setVisible(false);
        cardStock.setVisible(false);
        cardSales.setVisible(false);
        cardEmployee.setVisible(true);
        cardSupplier.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_employeeMouseClicked

    private void supplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_supplierMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlStock);
        resetPnlColor(pnlSales);
        resetPnlColor(pnlEmployee);
        setPnlColor(pnlSupplier);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorStock);
        resetPnlIndicatorColor(pnlIndicatorSales);
        resetPnlIndicatorColor(pnlIndicatorEmployee);
        setPnlIndicatorColor(pnlIndicatorSupplier);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Supplier");

        cardHome.setVisible(false);
        cardStock.setVisible(false);
        cardSales.setVisible(false);
        cardEmployee.setVisible(false);
        cardSupplier.setVisible(true);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_supplierMouseClicked

    private void reportsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportsMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlStock);
        resetPnlColor(pnlSales);
        resetPnlColor(pnlEmployee);
        resetPnlColor(pnlSupplier);
        setPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorStock);
        resetPnlIndicatorColor(pnlIndicatorSales);
        resetPnlIndicatorColor(pnlIndicatorEmployee);
        resetPnlIndicatorColor(pnlIndicatorSupplier);
        setPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Settings");

        cardHome.setVisible(false);
        cardStock.setVisible(false);
        cardSales.setVisible(false);
        cardEmployee.setVisible(false);
        cardSupplier.setVisible(false);
        cardSettings.setVisible(true);
    }//GEN-LAST:event_reportsMouseClicked

    private void lblLogoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLogoutMouseClicked

        int choose = JOptionPane.showConfirmDialog(this,
                "Do you want to Logout the application?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (choose == JOptionPane.YES_OPTION) {

            String logoutActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username= '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Admin Logout', '" + lblAdminUsername.getText() + " logged out' , 'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            pnlLogoutLoader.setVisible(true);
            pnlAdminDashboard.setVisible(false);

            new java.util.Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    for (float i = 0.985f; i > 0; i -= 0.000001) {
                        AdminDashboard.this.setOpacity(i);

                    }
                    AdminDashboard.this.dispose();

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
        resetPnlColor(pnlStock);
        resetPnlColor(pnlSales);
        resetPnlColor(pnlEmployee);
        resetPnlColor(pnlSupplier);
        resetPnlColor(pnlSettings);

        setPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorStock);
        resetPnlIndicatorColor(pnlIndicatorSales);
        resetPnlIndicatorColor(pnlIndicatorEmployee);
        resetPnlIndicatorColor(pnlIndicatorSupplier);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Home");

        cardHome.setVisible(true);
        cardStock.setVisible(false);
        cardSales.setVisible(false);
        cardEmployee.setVisible(false);
        cardSupplier.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_pnlHomeMouseClicked

    private void pnlStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlStockMouseClicked
        resetPnlColor(pnlHome);
        setPnlColor(pnlStock);
        resetPnlColor(pnlSales);
        resetPnlColor(pnlEmployee);
        resetPnlColor(pnlSupplier);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        setPnlIndicatorColor(pnlIndicatorStock);
        resetPnlIndicatorColor(pnlIndicatorSales);
        resetPnlIndicatorColor(pnlIndicatorEmployee);
        resetPnlIndicatorColor(pnlIndicatorSupplier);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Stock");

        cardHome.setVisible(false);
        cardStock.setVisible(true);
        cardSales.setVisible(false);
        cardEmployee.setVisible(false);
        cardSupplier.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_pnlStockMouseClicked

    private void pnlSalesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlSalesMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlStock);
        setPnlColor(pnlSales);
        resetPnlColor(pnlEmployee);
        resetPnlColor(pnlSupplier);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorStock);
        setPnlIndicatorColor(pnlIndicatorSales);
        resetPnlIndicatorColor(pnlIndicatorEmployee);
        resetPnlIndicatorColor(pnlIndicatorSupplier);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Sales");

        cardHome.setVisible(false);
        cardStock.setVisible(false);
        cardSales.setVisible(true);
        cardEmployee.setVisible(false);
        cardSupplier.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_pnlSalesMouseClicked

    private void pnlEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlEmployeeMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlStock);
        resetPnlColor(pnlSales);
        setPnlColor(pnlEmployee);
        resetPnlColor(pnlSupplier);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorStock);
        resetPnlIndicatorColor(pnlIndicatorSales);
        setPnlIndicatorColor(pnlIndicatorEmployee);
        resetPnlIndicatorColor(pnlIndicatorSupplier);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Employee");

        cardHome.setVisible(false);
        cardStock.setVisible(false);
        cardSales.setVisible(false);
        cardEmployee.setVisible(true);
        cardSupplier.setVisible(false);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_pnlEmployeeMouseClicked

    private void pnlSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlSupplierMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlStock);
        resetPnlColor(pnlSales);
        resetPnlColor(pnlEmployee);
        setPnlColor(pnlSupplier);
        resetPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorStock);
        resetPnlIndicatorColor(pnlIndicatorSales);
        resetPnlIndicatorColor(pnlIndicatorEmployee);
        setPnlIndicatorColor(pnlIndicatorSupplier);
        resetPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Supplier");

        cardHome.setVisible(false);
        cardStock.setVisible(false);
        cardSales.setVisible(false);
        cardEmployee.setVisible(false);
        cardSupplier.setVisible(true);
        cardSettings.setVisible(false);
    }//GEN-LAST:event_pnlSupplierMouseClicked

    private void pnlSettingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlSettingsMouseClicked
        resetPnlColor(pnlHome);
        resetPnlColor(pnlStock);
        resetPnlColor(pnlSales);
        resetPnlColor(pnlEmployee);
        resetPnlColor(pnlSupplier);
        setPnlColor(pnlSettings);

        resetPnlIndicatorColor(pnlIndicatorHome);
        resetPnlIndicatorColor(pnlIndicatorStock);
        resetPnlIndicatorColor(pnlIndicatorSales);
        resetPnlIndicatorColor(pnlIndicatorEmployee);
        resetPnlIndicatorColor(pnlIndicatorSupplier);
        setPnlIndicatorColor(pnlIndicatorSettings);

        cardName.setText("Settings");

        cardHome.setVisible(false);
        cardStock.setVisible(false);
        cardSales.setVisible(false);
        cardEmployee.setVisible(false);
        cardSupplier.setVisible(false);
        cardSettings.setVisible(true);
    }//GEN-LAST:event_pnlSettingsMouseClicked

    private void minimize1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minimize1MouseClicked
        AdminDashboard.this.setState(JFrame.ICONIFIED);
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
            AdminDashboard.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            maximizeView.setVisible(true);
            restoreDownView.setVisible(false);
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            AdminDashboard.this.setMaximizedBounds(env.getMaximumWindowBounds());
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

            String logoutActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username= '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Admin System Exit', '" + lblAdminUsername.getText() + " system exited' , 'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            for (float i = 1f; i > 0; i -= 0.000001) {
                AdminDashboard.this.setOpacity(i);
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
        AdminDashboard.this.setState(JFrame.ICONIFIED);
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
            AdminDashboard.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            maximizeView.setVisible(true);
            restoreDownView.setVisible(false);
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            AdminDashboard.this.setMaximizedBounds(env.getMaximumWindowBounds());
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

            String logoutActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username= '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Admin System Exit', '" + lblAdminUsername.getText() + " system exited' , 'SUCCESS')";
            try {
                DB.DB.putData(logoutActivityLog);
            } catch (Exception e) {
            }

            for (float i = 1f; i > 0; i -= 0.000001) {
                AdminDashboard.this.setOpacity(i);
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

    private void lblAdminUsernameMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAdminUsernameMouseEntered
        lblAdminUsername.setBackground(new Color(35, 35, 35));
    }//GEN-LAST:event_lblAdminUsernameMouseEntered

    private void lblAdminUsernameMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAdminUsernameMouseExited
        lblAdminUsername.setBackground(new Color(0, 0, 0));
    }//GEN-LAST:event_lblAdminUsernameMouseExited


    private void pnlTopMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlTopMouseReleased
        setOpacity((float) (0.985));
    }//GEN-LAST:event_pnlTopMouseReleased

    private void btnRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestoreActionPerformed
        int option = JOptionPane.showConfirmDialog(this, "Do you want to Restore previous Database?", "Restore Confirmation", JOptionPane.YES_NO_OPTION);
        if (option == 0) {
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
                    txtRestoreLocation.setText(null);

                    String restoreBackupActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Restore Backup','Admin restored new backup from file location : " + txtRestoreLocation.getText() + "','SUCCESS')";
                    DB.DB.putData(restoreBackupActivityLog);

                } else {
                    JOptionPane.showMessageDialog(this, "Restore Failed!", "Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
            }
        }
        card14.setVisible(false);
        card14.setVisible(true);
    }//GEN-LAST:event_btnRestoreActionPerformed

    private void btnBrowseRestoreLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseRestoreLocationActionPerformed
        JFileChooser fcrestore = new JFileChooser();
        fcrestore.showOpenDialog(this);
        try {
            File f = fcrestore.getSelectedFile();
            path = f.getAbsolutePath();
            path = path.replace('\\', '/');
            txtRestoreLocation.setText(path);
        } catch (Exception e) {
        }
        card14.setVisible(false);
        card14.setVisible(true);
    }//GEN-LAST:event_btnBrowseRestoreLocationActionPerformed

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
                card13.setVisible(false);
                card13.setVisible(true);

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

                        String createBackupActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username = '" + lblAdminUsername.getText() + "'),'Admin','" + lblAdminUsername.getText() + "','Create Backup','Admin created new backup on file location : " + txtBackupLocation.getText() + "','SUCCESS')";
                        DB.DB.putData(createBackupActivityLog);

                    } else {
                        JOptionPane.showMessageDialog(this, "Backup Creation Failed!", "Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                }
            }
        }

        card13.setVisible(false);
        card13.setVisible(true);
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
        card13.setVisible(false);
        card13.setVisible(true);
    }//GEN-LAST:event_btnBrowseBackupPathActionPerformed

    private void jLabel42MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel42MouseClicked
        card13.setVisible(false);
        card14.setVisible(false);
        card15.setVisible(true);

        resetLblColor(jLabel17);
        resetLblColor(jLabel34);
        setLblColor(jLabel42);
    }//GEN-LAST:event_jLabel42MouseClicked

    private void jLabel34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel34MouseClicked
        card13.setVisible(false);
        card14.setVisible(true);
        card15.setVisible(false);

        resetLblColor(jLabel17);
        setLblColor(jLabel34);
        resetLblColor(jLabel42);
    }//GEN-LAST:event_jLabel34MouseClicked

    private void jLabel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseClicked
        card13.setVisible(true);
        card14.setVisible(false);
        card15.setVisible(false);

        setLblColor(jLabel17);
        resetLblColor(jLabel34);
        resetLblColor(jLabel42);
    }//GEN-LAST:event_jLabel17MouseClicked

    private void txtSelectedItemSupplierItemCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedItemSupplierItemCodeKeyPressed
        cmbSelectedItemSupplierItemCategory.grabFocus();
        cmbSelectedItemSupplierItemCategory.setPopupVisible(true);
    }//GEN-LAST:event_txtSelectedItemSupplierItemCodeKeyPressed

    private void btnDeleteSelectedSupplierItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSelectedSupplierItemActionPerformed
        if (!(tblSupplierItem.getSelectedRowCount() == 0)) {
            deleteSupplierItem();
            jPanel44.setVisible(false);
            jPanel44.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No Item Selected!");
            jTabbedPaneSupplierItem.setSelectedIndex(1);
        }
    }//GEN-LAST:event_btnDeleteSelectedSupplierItemActionPerformed

    private void btnGoBackToViewItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackToViewItemActionPerformed
        jTabbedPaneSupplierItem.setSelectedIndex(1);
    }//GEN-LAST:event_btnGoBackToViewItemActionPerformed

    private void btnUpdateSelectedSupplierItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateSelectedSupplierItemActionPerformed
        if (tblSupplierItem.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Item Selected!");
            jTabbedPaneSupplierItem.setSelectedIndex(1);

        } else {
            updateSupplierItem();

            jPanel44.setVisible(false);
            jPanel44.setVisible(true);
        }
    }//GEN-LAST:event_btnUpdateSelectedSupplierItemActionPerformed

    private void txtSelectedItemSupplierItemNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedItemSupplierItemNameKeyPressed
        txtSelectedItemSupplierItemCode.grabFocus();
    }//GEN-LAST:event_txtSelectedItemSupplierItemNameKeyPressed

    private void btnRefreshSupplierItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshSupplierItemActionPerformed
        refreshSupplierItemTable();
        txtSearchSupplierItem.setText(null);
        jPanel41.setVisible(false);
        jPanel41.setVisible(true);

        txtSelectedItemSupplierId.setText(null);
        txtSelectedItemSupplierUsername.setText(null);
        txtSelectedItemSupplierItemName.setText(null);
        txtSelectedItemSupplierItemCode.setText(null);
        cmbSelectedItemSupplierItemCategory.setSelectedIndex(0);
    }//GEN-LAST:event_btnRefreshSupplierItemActionPerformed

    private void cmbSortSupplierItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortSupplierItemActionPerformed
        searchSupplierItem();
    }//GEN-LAST:event_cmbSortSupplierItemActionPerformed

    private void txtSearchSupplierItemKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSupplierItemKeyTyped
        switch (cmbSortSupplierItem.getSelectedIndex()) {
            case 0:

                if (txtSearchSupplierItem.getText().isEmpty()) {
                    lblItemCount.setText("Item Count : *");
                } else {
                    calculateSupplierItemsCount();
                }
                break;
            default:
                lblItemCount.setText("Item Count : *");
        }
    }//GEN-LAST:event_txtSearchSupplierItemKeyTyped

    private void txtSearchSupplierItemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSupplierItemKeyReleased
        searchSupplierItem();
    }//GEN-LAST:event_txtSearchSupplierItemKeyReleased

    private void btnEditSupplierItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditSupplierItemActionPerformed
        if (tblSupplierItem.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Item Selected!");
            jPanel41.setVisible(false);
            jPanel41.setVisible(true);

        } else {
            jTabbedPaneSupplierItem.setSelectedIndex(2);
        }
    }//GEN-LAST:event_btnEditSupplierItemActionPerformed

    private void btnDeleteSupplierItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSupplierItemActionPerformed
        if (!(tblSupplierItem.getSelectedRowCount() == 0)) {
            deleteSupplierItem();
            jPanel41.setVisible(false);
            jPanel41.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No Item Selected!");
            jPanel41.setVisible(false);
            jPanel41.setVisible(true);
        }
    }//GEN-LAST:event_btnDeleteSupplierItemActionPerformed

    private void tblSupplierItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSupplierItemMouseClicked
        txtSelectedItemSupplierId.setText((String) tblSupplierItem.getValueAt(tblSupplierItem.getSelectedRow(), 0));
        txtSelectedItemSupplierUsername.setText((String) tblSupplierItem.getValueAt(tblSupplierItem.getSelectedRow(), 1));
        txtSelectedItemSupplierItemName.setText((String) tblSupplierItem.getValueAt(tblSupplierItem.getSelectedRow(), 2));
        txtSelectedItemSupplierItemCode.setText((String) tblSupplierItem.getValueAt(tblSupplierItem.getSelectedRow(), 3));
        cmbSelectedItemSupplierItemCategory.setSelectedItem((String) tblSupplierItem.getValueAt(tblSupplierItem.getSelectedRow(), 4));

        try {
            ResultSet rs = DB.DB.getData("SELECT id FROM item WHERE item_name = '" + txtSelectedItemSupplierItemName.getText() + "'");
            if (rs.next()) {
                String i = rs.getString(1);
                itemId = (i);
            }
        } catch (Exception e) {
        }

    }//GEN-LAST:event_tblSupplierItemMouseClicked

    private void cmbItemCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbItemCategoryKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtItemCode.getText().isEmpty()) {
                lblItemCodeValidation.setText("Please Fill this Field!");
                txtItemCode.setBorder(new LineBorder(Color.red));
                txtItemCode.setMinimumSize(new Dimension(64, 31));
            } else {
                lblItemCodeValidation.setText(null);
                txtItemCode.setBorder(new FlatBorder());
            }

            if (txtItemName.getText().isEmpty()) {
                lblItemNameValidation.setText("Please Fill this Field!");
                txtItemName.setBorder(new LineBorder(Color.red));
                txtItemName.setMinimumSize(new Dimension(64, 31));
            } else {
                lblItemNameValidation.setText(null);
                txtItemName.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE item_name = '" + txtItemName.getText() + "'");

                if (rs.next()) {
                    lblItemNameValidation.setText("Item Name Already Exists!");
                    txtItemName.setBorder(new LineBorder(Color.red));
                    txtItemName.setMinimumSize(new Dimension(64, 31));
                } else {
                    lblItemNameValidation.setText(null);
                    txtItemName.setBorder(new FlatBorder());
                }
            } catch (Exception e) {
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE item_code = '" + txtItemCode.getText() + "'");

                if (rs.next()) {
                    lblItemCodeValidation.setText("Item Code Already Exists!");
                    txtItemCode.setBorder(new LineBorder(Color.red));
                    txtItemCode.setMinimumSize(new Dimension(64, 31));
                } else {
                    lblItemCodeValidation.setText(null);
                    txtItemCode.setBorder(new FlatBorder());
                }
            } catch (Exception e) {

            }
            cmbItemSupplier.setPopupVisible(true);
            cmbItemSupplier.grabFocus();
        }
    }//GEN-LAST:event_cmbItemCategoryKeyPressed

    private void cmbItemSupplierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbItemSupplierKeyPressed
        if (evt.getKeyCode() == 10) {
            addItem();
            jPanel16.setVisible(false);
            jPanel16.setVisible(true);
        }
    }//GEN-LAST:event_cmbItemSupplierKeyPressed

    private void txtItemCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemCodeKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c)) || (Character.isLetter(c)) || (Character.isISOControl(c)))) {
            evt.consume();
        }

        try {
            ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE item_code = '" + txtItemCode.getText() + "'");

            if (rs.next()) {
                lblItemCodeValidation.setText("Item Code Already Exists!");
                txtItemCode.setBorder(new LineBorder(Color.red));
                txtItemCode.setMinimumSize(new Dimension(64, 31));
            } else {
                lblItemCodeValidation.setText(null);
                txtItemCode.setBorder(new FlatBorder());
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_txtItemCodeKeyTyped

    private void txtItemCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemCodeKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtItemCode.getText().isEmpty()) {
                lblItemCodeValidation.setText("Please Fill this Field!");
                txtItemCode.setBorder(new LineBorder(Color.red));
                txtItemCode.setMinimumSize(new Dimension(64, 31));
            } else {
                lblItemCodeValidation.setText(null);
                txtItemCode.setBorder(new FlatBorder());
            }

            if (txtItemName.getText().isEmpty()) {
                lblItemNameValidation.setText("Please Fill this Field!");
                txtItemName.setBorder(new LineBorder(Color.red));
                txtItemName.setMinimumSize(new Dimension(64, 31));
            } else {
                lblItemNameValidation.setText(null);
                txtItemName.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE item_name = '" + txtItemName.getText() + "'");

                if (rs.next()) {
                    lblItemNameValidation.setText("Item Name Already Exists!");
                    txtItemName.setBorder(new LineBorder(Color.red));
                    txtItemName.setMinimumSize(new Dimension(64, 31));
                } else {
                    lblItemNameValidation.setText(null);
                    txtItemName.setBorder(new FlatBorder());
                }
            } catch (Exception e) {
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM purchase_order WHERE item_code = '" + txtItemCode.getText() + "'");

                if (rs.next()) {
                    lblItemCodeValidation.setText("Item Code Already Exists!");
                    txtItemCode.setBorder(new LineBorder(Color.red));
                    txtItemCode.setMinimumSize(new Dimension(64, 31));
                } else {
                    lblItemCodeValidation.setText(null);
                    txtItemCode.setBorder(new FlatBorder());
                }
            } catch (Exception e) {

            }
            cmbItemCategory.setPopupVisible(true);
            cmbItemCategory.grabFocus();
        }
    }//GEN-LAST:event_txtItemCodeKeyPressed

    private void btnSaveItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveItemActionPerformed
        addItem();
        jPanel16.setVisible(false);
        jPanel16.setVisible(true);
    }//GEN-LAST:event_btnSaveItemActionPerformed

    private void txtItemNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c)) || (Character.isLetter(c)) || (Character.isISOControl(c)))) {
            evt.consume();
        }

        try {
            ResultSet rs = DB.DB.getData("SELECT * FROM item WHERE item_name = '" + txtItemName.getText() + "'");

            if (rs.next()) {
                lblItemNameValidation.setText("Item Name Already Exists!");
                txtItemName.setBorder(new LineBorder(Color.red));
                txtItemName.setMinimumSize(new Dimension(64, 31));
            } else {
                lblItemNameValidation.setText(null);
                txtItemName.setBorder(new FlatBorder());
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_txtItemNameKeyTyped

    private void txtItemNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtItemNameKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtItemName.getText().isEmpty()) {
                lblItemNameValidation.setText("Please Fill this Field!");
                txtItemName.setBorder(new LineBorder(Color.red));
                txtItemName.setMinimumSize(new Dimension(64, 31));
            } else {
                lblItemNameValidation.setText(null);
                txtItemName.setBorder(new FlatBorder());
            }
            if (txtItemName.getText().isEmpty()) {
                lblItemNameValidation.setText("Please Fill this Field!");
                txtItemName.setBorder(new LineBorder(Color.red));
                txtItemName.setMinimumSize(new Dimension(64, 31));
            } else {
                lblItemNameValidation.setText(null);
                txtItemName.setBorder(new FlatBorder());
            }
            txtItemCode.grabFocus();
        }
    }//GEN-LAST:event_txtItemNameKeyPressed

    private void txtSelectedSupplierEmailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedSupplierEmailKeyTyped
        if (!Pattern.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", txtSelectedSupplierEmail.getText())) {
            lblSelectedSupplierEmailValidation.setText("Please Input a Valid E-mail!");
            txtSelectedSupplierEmail.setBorder(new LineBorder(Color.red));
            txtSelectedSupplierEmail.setMinimumSize(new Dimension(64, 31));

        } else {
            lblSelectedSupplierEmailValidation.setText(null);
            txtSelectedSupplierEmail.setBorder(new FlatBorder());
        }
    }//GEN-LAST:event_txtSelectedSupplierEmailKeyTyped

    private void txtSelectedSupplierEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedSupplierEmailKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSelectedSupplierEmail.getText().isEmpty()) {
                lblSelectedSupplierEmailValidation.setText("Please Fill this Field!");
                txtSelectedSupplierEmail.setBorder(new LineBorder(Color.red));
                txtSelectedSupplierEmail.setMinimumSize(new Dimension(64, 31));

            } else {
                lblSelectedSupplierEmailValidation.setText(null);
                txtSelectedSupplierEmail.setBorder(new FlatBorder());
            }
            txtSelectedSupplierPhoneNo.grabFocus();
        }
    }//GEN-LAST:event_txtSelectedSupplierEmailKeyPressed

    private void btnPrintSelectedSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintSelectedSupplierActionPerformed
        printSelectedSupplier();
        jPanel32.setVisible(false);
        jPanel32.setVisible(true);
    }//GEN-LAST:event_btnPrintSelectedSupplierActionPerformed

    private void btnDeleteSelectedSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSelectedSupplierActionPerformed
        if (tblSupplier.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Supplier Selected!");
            jTabbedPaneSupplier.setSelectedIndex(0);

        } else {

            deleteSupplier();
            jPanel32.setVisible(false);
            jPanel32.setVisible(true);
        }
    }//GEN-LAST:event_btnDeleteSelectedSupplierActionPerformed

    private void btnGoBackToViewSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackToViewSupplierActionPerformed
        jTabbedPaneSupplier.setSelectedIndex(0);
    }//GEN-LAST:event_btnGoBackToViewSupplierActionPerformed

    private void btnUpdateSelectedSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateSelectedSupplierActionPerformed
        if (tblSupplier.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Supplier Selected!");
            jTabbedPaneSupplier.setSelectedIndex(0);

        } else {
            updateSupplier();

            jPanel32.setVisible(false);
            jPanel32.setVisible(true);
        }
    }//GEN-LAST:event_btnUpdateSelectedSupplierActionPerformed

    private void txtSelectedSupplierPhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedSupplierPhoneNoKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();
        }
        String phoneNo = txtSelectedSupplierPhoneNo.getText();
        int length = phoneNo.length();
        if (length >= 10) {
            evt.consume();
        }
        if (length < 10) {
            lblSelectedSupplierPhoneNoValidation.setText("Please Input a Valid Phone Number!");
            txtSelectedSupplierPhoneNo.setBorder(new LineBorder(Color.red));
            txtSelectedSupplierPhoneNo.setMinimumSize(new Dimension(64, 31));
        } else {
            lblSelectedSupplierPhoneNoValidation.setText(null);
            txtSelectedSupplierPhoneNo.setBorder(new FlatBorder());

        }
    }//GEN-LAST:event_txtSelectedSupplierPhoneNoKeyTyped

    private void txtSelectedSupplierPhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedSupplierPhoneNoKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSelectedSupplierPhoneNo.getText().isEmpty()) {
                lblSelectedSupplierPhoneNoValidation.setText("Please Fill this Field!");
                txtSelectedSupplierPhoneNo.setBorder(new LineBorder(Color.red));
                txtSelectedSupplierPhoneNo.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSelectedSupplierPhoneNoValidation.setText(null);
                lblSelectedEmployeeIdValidation.setText(null);
                lblSelectedSupplierUsernameValidation.setText(null);
                txtSelectedSupplierPhoneNo.setBorder(new FlatBorder());
            }
            txtSelectedSupplierCompany.grabFocus();
        }
    }//GEN-LAST:event_txtSelectedSupplierPhoneNoKeyPressed

    private void txtSelectedSupplierCompanyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedSupplierCompanyKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSelectedSupplierCompany.getText().isEmpty()) {
                lblSelectedSupplierCompanyValidation.setText("Please Fill this Field!");
                txtSelectedSupplierCompany.setBorder(new LineBorder(Color.red));
                txtSelectedSupplierCompany.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSelectedSupplierCompanyValidation.setText(null);
                lblSelectedEmployeeIdValidation.setText(null);
                lblSelectedEmployeeUsernameValidation.setText(null);
                txtSelectedSupplierCompany.setBorder(new FlatBorder());
            }
            updateSupplier();
            jPanel32.setVisible(false);
            jPanel32.setVisible(true);
        }
    }//GEN-LAST:event_txtSelectedSupplierCompanyKeyPressed

    private void txtSelectedSupplierAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedSupplierAddressKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSelectedSupplierAddress.getText().isEmpty()) {
                lblSelectedSupplierAddressValidation.setText("Please Fill this Field!");
                txtSelectedSupplierAddress.setBorder(new LineBorder(Color.red));
                txtSelectedSupplierAddress.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSelectedSupplierAddressValidation.setText(null);
                lblSelectedEmployeeIdValidation.setText(null);
                lblSelectedSupplierUsernameValidation.setText(null);
                txtSelectedSupplierAddress.setBorder(new FlatBorder());
            }
            txtSelectedSupplierEmail.grabFocus();
        }
    }//GEN-LAST:event_txtSelectedSupplierAddressKeyPressed

    private void cmbSortSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortSupplierActionPerformed
        searchSupplier();
    }//GEN-LAST:event_cmbSortSupplierActionPerformed

    private void btnRefreshSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshSupplierActionPerformed
        txtSearchSupplier.setText(null);
        txtSelectedSupplierId.setText(null);
        txtSelectedSupplierUsername.setText(null);
        txtSelectedSupplierAddress.setText(null);
        txtSelectedSupplierEmail.setText(null);
        txtSelectedSupplierPhoneNo.setText(null);
        txtSelectedSupplierCompany.setText(null);
        refreshSupplierTable();
        cmbSortSupplier.setSelectedIndex(0);
        jPanel29.setVisible(false);
        jPanel29.setVisible(true);
    }//GEN-LAST:event_btnRefreshSupplierActionPerformed

    private void txtSearchSupplierKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSupplierKeyReleased
        searchSupplier();
    }//GEN-LAST:event_txtSearchSupplierKeyReleased

    private void btnPrintAllSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintAllSupplierActionPerformed
        printAllSupplier();
        jPanel29.setVisible(false);
        jPanel29.setVisible(true);
    }//GEN-LAST:event_btnPrintAllSupplierActionPerformed

    private void btnDeleteSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSupplierActionPerformed
        if (tblSupplier.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Supplier Selected!");
            jPanel29.setVisible(false);
            jPanel29.setVisible(true);

        } else {

            deleteSupplier();

            jPanel29.setVisible(false);
            jPanel29.setVisible(true);
        }
    }//GEN-LAST:event_btnDeleteSupplierActionPerformed

    private void btnEditSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditSupplierActionPerformed
        if (tblSupplier.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Supplier Selected!");
            jPanel29.setVisible(false);
            jPanel29.setVisible(true);

        } else {
            jTabbedPaneSupplier.setSelectedIndex(1);
        }
    }//GEN-LAST:event_btnEditSupplierActionPerformed

    private void tblSupplierMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSupplierMouseClicked
        txtSelectedSupplierId.setText((String) tblSupplier.getValueAt(tblSupplier.getSelectedRow(), 0));
        txtSelectedSupplierUsername.setText((String) tblSupplier.getValueAt(tblSupplier.getSelectedRow(), 1));
        txtSelectedSupplierAddress.setText((String) tblSupplier.getValueAt(tblSupplier.getSelectedRow(), 2));
        txtSelectedSupplierEmail.setText((String) tblSupplier.getValueAt(tblSupplier.getSelectedRow(), 3));
        txtSelectedSupplierPhoneNo.setText((String) tblSupplier.getValueAt(tblSupplier.getSelectedRow(), 4));
        txtSelectedSupplierCompany.setText((String) tblSupplier.getValueAt(tblSupplier.getSelectedRow(), 5));

        lblSelectedSupplierIdValidation.setText(null);
        lblSelectedSupplierUsernameValidation.setText(null);
        lblSelectedSupplierAddressValidation.setText(null);
        lblSelectedSupplierEmailValidation.setText(null);
        lblSelectedSupplierPhoneNoValidation.setText(null);
        lblSelectedSupplierCompanyValidation.setText(null);

        txtSelectedSupplierId.setBorder(new FlatBorder());
        txtSelectedSupplierUsername.setBorder(new FlatBorder());
        txtSelectedSupplierAddress.setBorder(new FlatBorder());
        txtSelectedSupplierEmail.setBorder(new FlatBorder());
        txtSelectedSupplierPhoneNo.setBorder(new FlatBorder());
        txtSelectedSupplierCompany.setBorder(new FlatBorder());
    }//GEN-LAST:event_tblSupplierMouseClicked

    private void txtSupplierUsernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierUsernameKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c)) || (Character.isLetter(c)) || (Character.isISOControl(c)))) {
            evt.consume();
        }

        try {
            ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE username like '" + txtSupplierUsername.getText() + "'");

            if (rs.next()) {
                lblSupplierUserameValidation.setText("Username Already Exists!");
                txtSupplierUsername.setBorder(new LineBorder(Color.red));
                txtSupplierUsername.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSupplierUserameValidation.setText(null);
                txtSupplierUsername.setBorder(new FlatBorder());
            }

        } catch (Exception e) {
        }
    }//GEN-LAST:event_txtSupplierUsernameKeyTyped

    private void txtSupplierUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierUsernameKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSupplierUsername.getText().isEmpty()) {
                lblSupplierUserameValidation.setText("Please Fill this Field!");
                txtSupplierUsername.setBorder(new LineBorder(Color.red));
                txtSupplierUsername.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSupplierUserameValidation.setText(null);
                txtSupplierUsername.setBorder(new FlatBorder());
            }

            txtSupplierAddress.grabFocus();
        }
    }//GEN-LAST:event_txtSupplierUsernameKeyPressed

    private void txtSupplierAddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierAddressKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSupplierAddress.getText().isEmpty()) {
                lblSupplierAddressValidation.setText("Please Fill this Field!");
                txtSupplierAddress.setBorder(new LineBorder(Color.red));
                txtSupplierAddress.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSupplierAddressValidation.setText(null);
                txtSupplierAddress.setBorder(new FlatBorder());
            }
            if (txtSupplierUsername.getText().isEmpty()) {
                lblSupplierUserameValidation.setText("Please Fill this Field!");
                txtSupplierUsername.setBorder(new LineBorder(Color.red));
                txtSupplierUsername.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSupplierUserameValidation.setText(null);
                txtSupplierUsername.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE username like '" + txtSupplierUsername.getText() + "'");

                if (rs.next()) {
                    lblSupplierUserameValidation.setText("Username Already Exists!");
                    txtSupplierUsername.setBorder(new LineBorder(Color.red));
                    txtSupplierUsername.setMinimumSize(new Dimension(64, 31));
                } else {
                    lblSupplierUserameValidation.setText(null);
                    txtSupplierUsername.setBorder(new FlatBorder());
                }

            } catch (Exception e) {
            }
            txtSupplierEmail.grabFocus();
        }
    }//GEN-LAST:event_txtSupplierAddressKeyPressed

    private void txtSupplierEmailKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierEmailKeyTyped
        if (!Pattern.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$", txtSupplierEmail.getText())) {
            lblSupplierEmailValidation.setText("Please Input a Valid E-mail!");
            txtSupplierEmail.setBorder(new LineBorder(Color.red));
            txtSupplierEmail.setMinimumSize(new Dimension(64, 31));

        } else {
            lblSupplierEmailValidation.setText(null);
            txtSupplierEmail.setBorder(new FlatBorder());
        }
    }//GEN-LAST:event_txtSupplierEmailKeyTyped

    private void txtSupplierEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierEmailKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSupplierEmail.getText().isEmpty()) {
                lblSupplierEmailValidation.setText("Please Fill this Field!");
                txtSupplierEmail.setBorder(new LineBorder(Color.red));
                txtSupplierEmail.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSupplierEmailValidation.setText(null);
                txtSupplierEmail.setBorder(new FlatBorder());
            }
            if (txtSupplierUsername.getText().isEmpty()) {
                lblSupplierUserameValidation.setText("Please Fill this Field!");
                txtSupplierUsername.setBorder(new LineBorder(Color.red));
                txtSupplierUsername.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSupplierUserameValidation.setText(null);
                txtSupplierUsername.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE username like '" + txtSupplierUsername.getText() + "'");

                if (rs.next()) {
                    lblSupplierUserameValidation.setText("Username Already Exists!");
                    txtSupplierUsername.setBorder(new LineBorder(Color.red));
                    txtSupplierUsername.setMinimumSize(new Dimension(64, 31));
                } else {
                    lblSupplierUserameValidation.setText(null);
                    txtSupplierUsername.setBorder(new FlatBorder());
                }

            } catch (Exception e) {
            }
            txtSupplierPhoneNo.grabFocus();
        }
    }//GEN-LAST:event_txtSupplierEmailKeyPressed

    private void txtSupplierPhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierPhoneNoKeyTyped
        char c = evt.getKeyChar();

        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String phoneNo = txtSupplierPhoneNo.getText();
        int length = phoneNo.length();

        if (length >= 10) {
            evt.consume();
        }
        if (length < 9) {
            lblSupplierPhoneNoValidation.setText("Please Input a Valid Phone Number!");
            txtSupplierPhoneNo.setBorder(new LineBorder(Color.red));
            txtSupplierPhoneNo.setMinimumSize(new Dimension(64, 31));
        } else {
            lblSupplierPhoneNoValidation.setText(null);
            txtSupplierPhoneNo.setBorder(new FlatBorder());
        }
    }//GEN-LAST:event_txtSupplierPhoneNoKeyTyped

    private void txtSupplierPhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierPhoneNoKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSupplierPhoneNo.getText().isEmpty()) {
                lblSupplierPhoneNoValidation.setText("Please Fill this Field!");
                txtSupplierPhoneNo.setBorder(new LineBorder(Color.red));
                txtSupplierPhoneNo.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSupplierPhoneNoValidation.setText(null);
                txtSupplierPhoneNo.setBorder(new FlatBorder());
            }

            if (txtSupplierUsername.getText().isEmpty()) {
                lblSupplierUserameValidation.setText("Please Fill this Field!");
                txtSupplierUsername.setBorder(new LineBorder(Color.red));
                txtSupplierUsername.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSupplierUserameValidation.setText(null);
                txtSupplierUsername.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE username like '" + txtSupplierUsername.getText() + "'");

                if (rs.next()) {
                    lblSupplierUserameValidation.setText("Username Already Exists!");
                    txtSupplierUsername.setBorder(new LineBorder(Color.red));
                    txtSupplierUsername.setMinimumSize(new Dimension(64, 31));
                } else {
                    lblSupplierUserameValidation.setText(null);
                    txtSupplierUsername.setBorder(new FlatBorder());
                }

            } catch (Exception e) {
            }
            txtSupplierCompany.grabFocus();
        }
    }//GEN-LAST:event_txtSupplierPhoneNoKeyPressed

    private void txtSupplierCompanyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSupplierCompanyKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSupplierCompany.getText().isEmpty()) {
                lblSupplierCompanyValidation.setText("Please Fill this Field!");
                txtSupplierCompany.setBorder(new LineBorder(Color.red));
                txtSupplierCompany.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSupplierCompanyValidation.setText(null);
                txtSupplierCompany.setBorder(new FlatBorder());
            }

            if (txtSupplierUsername.getText().isEmpty()) {
                lblSupplierUserameValidation.setText("Please Fill this Field!");
                txtSupplierUsername.setBorder(new LineBorder(Color.red));
                txtSupplierUsername.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSupplierUserameValidation.setText(null);
                txtSupplierUsername.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM supplier WHERE username like '" + txtSupplierUsername.getText() + "'");

                if (rs.next()) {
                    lblSupplierUserameValidation.setText("Username Already Exists!");
                    txtSupplierUsername.setBorder(new LineBorder(Color.red));
                    txtSupplierUsername.setMinimumSize(new Dimension(64, 31));
                } else {
                    lblSupplierUserameValidation.setText(null);
                    txtSupplierUsername.setBorder(new FlatBorder());
                }

            } catch (Exception e) {
            }
            txtSupplierCompany.grabFocus();
        }
    }//GEN-LAST:event_txtSupplierCompanyKeyPressed

    private void txtSupplierCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSupplierCompanyActionPerformed
        addNewSupplier();
        card10.setVisible(false);
        card10.setVisible(true);
    }//GEN-LAST:event_txtSupplierCompanyActionPerformed

    private void btnAddSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSupplierActionPerformed
        addNewSupplier();
        card10.setVisible(false);
        card10.setVisible(true);
    }//GEN-LAST:event_btnAddSupplierActionPerformed

    private void jLabel124MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel124MouseClicked
        card10.setVisible(false);
        card11.setVisible(false);
        card12.setVisible(true);

        resetLblColor(jLabel114);
        resetLblColor(jLabel123);
        setLblColor(jLabel124);
    }//GEN-LAST:event_jLabel124MouseClicked

    private void jLabel123MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel123MouseClicked
        card10.setVisible(false);
        card11.setVisible(true);
        card12.setVisible(false);

        resetLblColor(jLabel114);
        setLblColor(jLabel123);
        resetLblColor(jLabel124);
    }//GEN-LAST:event_jLabel123MouseClicked

    private void jLabel114MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel114MouseClicked
        card10.setVisible(true);
        card11.setVisible(false);
        card12.setVisible(false);

        setLblColor(jLabel114);
        resetLblColor(jLabel123);
        resetLblColor(jLabel124);
    }//GEN-LAST:event_jLabel114MouseClicked

    private void cmbSortEmployeePayrollHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortEmployeePayrollHistoryActionPerformed
        searchPaymentHistory();
    }//GEN-LAST:event_cmbSortEmployeePayrollHistoryActionPerformed

    private void txtSearchPayrollHistoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchPayrollHistoryKeyReleased
        searchPaymentHistory();
    }//GEN-LAST:event_txtSearchPayrollHistoryKeyReleased

    private void btnRefreshPayrollHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshPayrollHistoryActionPerformed
        refreshEmployeePayrollHistoryTable();
        txtSearchPayrollHistory.setText(null);
        jPanel23.setVisible(false);
        jPanel23.setVisible(true);
    }//GEN-LAST:event_btnRefreshPayrollHistoryActionPerformed

    private void txtSelectedPayrollEmployeePaymentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedPayrollEmployeePaymentKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSelectedPayrollEmployeePayment.getText().isEmpty()) {
                lblSelectedEmployeePaymentValidation.setText("Please Fill this Field!");
                txtSelectedPayrollEmployeePayment.setBorder(new LineBorder(Color.red));
                txtSelectedPayrollEmployeePayment.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSelectedEmployeePaymentValidation.setText(null);
                txtSelectedPayrollEmployeePayment.setBorder(new FlatBorder());

            }
            payEmployee();
            jPanel22.setVisible(false);
            jPanel22.setVisible(true);
        }
    }//GEN-LAST:event_txtSelectedPayrollEmployeePaymentKeyPressed

    private void txtSelectedPayrollEmployeeExtraSalaryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedPayrollEmployeeExtraSalaryKeyTyped
        char c = evt.getKeyChar();

        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String extraSalary = txtSelectedPayrollEmployeeExtraSalary.getText();
        int length = extraSalary.length();
        if (length >= 6) {
            evt.consume();
        }
    }//GEN-LAST:event_txtSelectedPayrollEmployeeExtraSalaryKeyTyped

    private void txtSelectedPayrollEmployeeExtraSalaryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedPayrollEmployeeExtraSalaryKeyPressed
        if (evt.getKeyCode() == 10) {

            if (txtSelectedPayrollEmployeeExtraSalary.getText().isEmpty()) {
                lblSelectedEmployeeExtraSalaryValidation.setText("Please Fill this Field!");
                txtSelectedPayrollEmployeeExtraSalary.setBorder(new LineBorder(Color.red));
                txtSelectedPayrollEmployeeExtraSalary.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSelectedEmployeeExtraSalaryValidation.setText(null);
                txtSelectedPayrollEmployeeExtraSalary.setBorder(new FlatBorder());

                try {
                    String payment;
                    salary = Integer.parseInt(txtSelectedPayrollEmployeeSalary.getText());
                    extra = Integer.parseInt(txtSelectedPayrollEmployeeExtraSalary.getText());

                    result = salary + extra;

                    payment = String.format("%.2f", result);
                    txtSelectedPayrollEmployeePayment.setText(payment);

                } catch (NumberFormatException nf) {
                }
                txtSelectedPayrollEmployeePayment.grabFocus();

            }
        }
    }//GEN-LAST:event_txtSelectedPayrollEmployeeExtraSalaryKeyPressed

    private void btnPrintSelectedEmployeePayrollBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintSelectedEmployeePayrollBillActionPerformed
        printSelectedEmployeePaymentBill();
        jPanel22.setVisible(false);
        jPanel22.setVisible(true);
    }//GEN-LAST:event_btnPrintSelectedEmployeePayrollBillActionPerformed

    private void btnGoBackToPayrollSummaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackToPayrollSummaryActionPerformed
        jTabbedPane3.setSelectedIndex(0);
    }//GEN-LAST:event_btnGoBackToPayrollSummaryActionPerformed

    private void btnPaySelectedEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPaySelectedEmployeeActionPerformed

        if (tblEmployeePayroll.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Employee Selected!");
            jTabbedPane3.setSelectedIndex(0);

        } else {
            payEmployee();
            jPanel22.setVisible(false);
            jPanel22.setVisible(true);
        }
    }//GEN-LAST:event_btnPaySelectedEmployeeActionPerformed

    private void cmbSortEmployeePayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortEmployeePayrollActionPerformed
        searchPaymentSummary();
    }//GEN-LAST:event_cmbSortEmployeePayrollActionPerformed

    private void txtSearchPayrollSummaryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchPayrollSummaryKeyReleased
        searchPaymentSummary();
    }//GEN-LAST:event_txtSearchPayrollSummaryKeyReleased

    private void btnPayEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayEmployeeActionPerformed
        String selectedEmployeePayrollStatus = (txtSelectedPayrollEmployeeStatus.getText());

        if (selectedEmployeePayrollStatus.equals("PAID")) {
            JOptionPane.showMessageDialog(this, "Payment Already Settled!");
            jPanel21.setVisible(false);
            jPanel21.setVisible(true);
        } else {

            if ((tblEmployeePayroll.getSelectedRowCount() == 0)) {
                JOptionPane.showMessageDialog(this, "No Employee Selected!");
                jPanel21.setVisible(false);
                jPanel21.setVisible(true);

            } else {
                jTabbedPane3.setSelectedIndex(1);
                txtSelectedPayrollEmployeeExtraSalary.grabFocus();
            }
        }
    }//GEN-LAST:event_btnPayEmployeeActionPerformed

    private void btnRefreshEmployeePayrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshEmployeePayrollActionPerformed
        txtSearchPayrollSummary.setText(null);
        txtSelectedPayrollEmployeeId.setText(null);
        txtSelectedPayrollEmployeeUsername.setText(null);
        txtSelectedPayrollEmployeeMonth.setText(null);
        txtSelectedPayrollEmployeeStatus.setText(null);
        txtSelectedPayrollEmployeeAttendance.setText(null);
        txtSelectedPayrollEmployeeSalary.setText(null);
        txtSelectedPayrollEmployeeExtraSalary.setText(null);
        txtSelectedPayrollEmployeeExtraSalary.setText(null);
        txtSelectedPayrollEmployeePayment.setText(null);
        refreshEmployeePayrollTable();
        jPanel21.setVisible(false);
        jPanel21.setVisible(true);
    }//GEN-LAST:event_btnRefreshEmployeePayrollActionPerformed

    private void tblEmployeePayrollMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmployeePayrollMouseClicked
        Date DateToday = new Date();
        SimpleDateFormat toToday = new SimpleDateFormat("yyyy-MM-dd");
        today = "" + toToday.format(DateToday);

        txtSelectedPayrollEmployeeId.setText((String) tblEmployeePayroll.getValueAt(tblEmployeePayroll.getSelectedRow(), 0));
        txtSelectedPayrollEmployeeUsername.setText((String) tblEmployeePayroll.getValueAt(tblEmployeePayroll.getSelectedRow(), 1));
        txtSelectedPayrollEmployeeMonth.setText((String) tblEmployeePayroll.getValueAt(tblEmployeePayroll.getSelectedRow(), 2));
        txtSelectedPayrollEmployeeStatus.setText((String) tblEmployeePayroll.getValueAt(tblEmployeePayroll.getSelectedRow(), 7));
        txtSelectedPayrollEmployeeAttendance.setText((String) tblEmployeePayroll.getValueAt(tblEmployeePayroll.getSelectedRow(), 5));
        txtSelectedPayrollEmployeeSalary.setText((String) tblEmployeePayroll.getValueAt(tblEmployeePayroll.getSelectedRow(), 6));
        txtSelectedPayrollEmployeeExtraSalary.setText("0");
        txtSelectedPayrollEmployeePayment.setText((String) tblEmployeePayroll.getValueAt(tblEmployeePayroll.getSelectedRow(), 6));
        txtSelectedPayrollEmployeeExtraSalary.setBorder(new FlatBorder());
        txtSelectedPayrollEmployeePayment.setBorder(new FlatBorder());
        lblSelectedEmployeeExtraSalaryValidation.setText(null);
        lblSelectedEmployeePaymentValidation.setText(null);
    }//GEN-LAST:event_tblEmployeePayrollMouseClicked

    private void cmbSortMonthlyEmployeeAttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortMonthlyEmployeeAttendanceActionPerformed
        searchMonthlyEmployeeAttendance();
    }//GEN-LAST:event_cmbSortMonthlyEmployeeAttendanceActionPerformed

    private void txtSearchMonthlyEmployeeAttendanceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchMonthlyEmployeeAttendanceKeyReleased
        searchMonthlyEmployeeAttendance();
    }//GEN-LAST:event_txtSearchMonthlyEmployeeAttendanceKeyReleased

    private void btnRefreshMonthlyEmployeeAttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshMonthlyEmployeeAttendanceActionPerformed
        txtSearchMonthlyEmployeeAttendance.setText(null);
        refreshEmployeeMonthlyAttendanceTable();
        cmbSortMonthlyEmployeeAttendance.setSelectedIndex(0);
        jPanel14.setVisible(false);
        jPanel14.setVisible(true);
    }//GEN-LAST:event_btnRefreshMonthlyEmployeeAttendanceActionPerformed

    private void jXDatePicker1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jXDatePicker1ActionPerformed
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date pickedDate = jXDatePicker1.getDate();
        String dateString = dateFormat.format(pickedDate);

        try {
            DefaultTableModel dtm = (DefaultTableModel) tblEmployeeAttendance.getModel();
            ResultSet rs = DB.DB.getData("SELECT * FROM employee_attendance WHERE date = '" + dateString + "' AND id like '" + txtSearchEmployeeAttendance.getText() + "%' order by id ");
            dtm.setRowCount(0);

            while (rs.next()) {
                String a = (rs.getString(1));
                String b = (rs.getString(2));
                String c = (rs.getString(3));
                String d = (rs.getString(4));
                String e = (rs.getString(5));
                String f = (rs.getString(6));
                dtm.addRow(new Object[]{a, b, c, d, e, f});
            }
            String AttendanceCount = ("Attendance Count : " + Integer.toString(tblEmployeeAttendance.getRowCount()));

            int AttCount = tblEmployeeAttendance.getRowCount();
            lblAttendanceCount.setText(AttendanceCount);

            int EmployeeCount = tblEmployee.getRowCount();
            int AbsCount = (EmployeeCount - AttCount);
            lblAbsenceCount.setText("Absence Count : " + AbsCount);

        } catch (Exception e) {
        }
        txtSearchEmployeeAttendance.setText(null);
    }//GEN-LAST:event_jXDatePicker1ActionPerformed

    private void cmbSortEmployeeAttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortEmployeeAttendanceActionPerformed
        searchEmployeeAttendance();
    }//GEN-LAST:event_cmbSortEmployeeAttendanceActionPerformed

    private void btnRefreshEmployeeAttendanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshEmployeeAttendanceActionPerformed
        txtSearchEmployeeAttendance.setText(null);
        jXDatePicker1.setDate(null);
        refreshEmployeeAttendanceTable();
        cmbSortEmployeeAttendance.setSelectedIndex(0);
        jPanel13.setVisible(false);
        jPanel13.setVisible(true);
    }//GEN-LAST:event_btnRefreshEmployeeAttendanceActionPerformed

    private void txtSearchEmployeeAttendanceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchEmployeeAttendanceKeyReleased
        searchEmployeeAttendance();
    }//GEN-LAST:event_txtSearchEmployeeAttendanceKeyReleased

    private void txtSelectedEmployeeNICKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeeNICKeyTyped
        if (!(Character.isDigit(evt.getKeyChar()))) {
            evt.consume();

        }
        String nic = txtSelectedEmployeeNIC.getText();
        int length = nic.length();
        if (length >= 12) {
            evt.consume();
        }

        if (length < 8) {
            lblSelectedEmployeeNICValidation.setText("Please Input a Valid NIC!");
            txtSelectedEmployeeNIC.setBorder(new LineBorder(Color.red));
            txtSelectedEmployeeNIC.setMinimumSize(new Dimension(64, 31));
        } else {
            lblSelectedEmployeeNICValidation.setText(null);
            txtSelectedEmployeeNIC.setBorder(new FlatBorder());

        }
    }//GEN-LAST:event_txtSelectedEmployeeNICKeyTyped

    private void txtSelectedEmployeeNICKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeeNICKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSelectedEmployeeNIC.getText().isEmpty()) {
                lblSelectedEmployeeNICValidation.setText("Please Fill this Field!");
                txtSelectedEmployeeNIC.setBorder(new LineBorder(Color.red));
                txtSelectedEmployeeNIC.setSize(new Dimension(64, 31));

            } else {
                lblSelectedEmployeeNICValidation.setText(null);
                txtSelectedEmployeeNIC.setBorder(new FlatBorder());
            }
            txtSelectedEmployeePhoneNo.grabFocus();
        }
    }//GEN-LAST:event_txtSelectedEmployeeNICKeyPressed

    private void btnPrintSelectedEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintSelectedEmployeeActionPerformed
        printSelectedEmployee();
        jPanel11.setVisible(false);
        jPanel11.setVisible(true);

    }//GEN-LAST:event_btnPrintSelectedEmployeeActionPerformed

    private void btnDeleteSelectedEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteSelectedEmployeeActionPerformed
        if (tblEmployee.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Employee Selected!");
            jTabbedPaneEmployee.setSelectedIndex(0);

        } else {

            deleteEmployee();
            jPanel11.setVisible(false);
            jPanel11.setVisible(true);
        }
    }//GEN-LAST:event_btnDeleteSelectedEmployeeActionPerformed

    private void txtSelectedEmployeeUsernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeeUsernameKeyTyped
        lblSelectedEmployeeUsernameValidation.setText("Employee Username can't be changed!");
    }//GEN-LAST:event_txtSelectedEmployeeUsernameKeyTyped

    private void txtSelectedEmployeeUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeeUsernameKeyPressed
        if (evt.getKeyCode() == 10) {
            txtSelectedEmployeePosition.grabFocus();
            lblSelectedEmployeeIdValidation.setText(null);
            lblSelectedEmployeeUsernameValidation.setText(null);
        }
    }//GEN-LAST:event_txtSelectedEmployeeUsernameKeyPressed

    private void btnGoBackToViewEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoBackToViewEmployeeActionPerformed
        jTabbedPaneEmployee.setSelectedIndex(0);
    }//GEN-LAST:event_btnGoBackToViewEmployeeActionPerformed

    private void btnUpdateSelectedEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateSelectedEmployeeActionPerformed

        if (tblEmployee.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Employee Selected!");
            jTabbedPaneEmployee.setSelectedIndex(0);

        } else {
            updateEmployee();

            jPanel11.setVisible(false);
            jPanel11.setVisible(true);
        }
    }//GEN-LAST:event_btnUpdateSelectedEmployeeActionPerformed

    private void txtSelectedEmployeeIdKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeeIdKeyTyped
        lblSelectedEmployeeIdValidation.setText("Employee Id can't be changed!");
    }//GEN-LAST:event_txtSelectedEmployeeIdKeyTyped

    private void txtSelectedEmployeeIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeeIdKeyPressed
        if (evt.getKeyCode() == 10) {
            txtSelectedEmployeeUsername.grabFocus();
            lblSelectedEmployeeIdValidation.setText(null);
            lblSelectedEmployeeUsernameValidation.setText(null);
        }
    }//GEN-LAST:event_txtSelectedEmployeeIdKeyPressed

    private void txtSelectedEmployeePhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeePhoneNoKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String phoneNo = txtSelectedEmployeePhoneNo.getText();
        int length = phoneNo.length();
        if (length >= 10) {
            evt.consume();
        }

        if (length < 10) {
            lblSelectedEmployeePhoneNoValidation.setText("Please Input a Valid Phone Number!");
            txtSelectedEmployeePhoneNo.setBorder(new LineBorder(Color.red));
            txtSelectedEmployeePhoneNo.setMinimumSize(new Dimension(64, 31));
        } else {
            lblSelectedEmployeePhoneNoValidation.setText(null);
            txtSelectedEmployeePhoneNo.setBorder(new FlatBorder());

        }
    }//GEN-LAST:event_txtSelectedEmployeePhoneNoKeyTyped

    private void txtSelectedEmployeePhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeePhoneNoKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSelectedEmployeePhoneNo.getText().isEmpty()) {
                lblSelectedEmployeePhoneNoValidation.setText("Please Fill this Field!");
                txtSelectedEmployeePhoneNo.setBorder(new LineBorder(Color.red));
                txtSelectedEmployeePhoneNo.setMinimumSize(new Dimension(64, 31));

            } else {
                lblSelectedEmployeePhoneNoValidation.setText(null);
                lblSelectedEmployeeIdValidation.setText(null);
                lblSelectedEmployeeUsernameValidation.setText(null);
                txtSelectedEmployeePhoneNo.setBorder(new FlatBorder());
            }
            txtSelectedEmployeeSalary.grabFocus();
        }
    }//GEN-LAST:event_txtSelectedEmployeePhoneNoKeyPressed

    private void txtSelectedEmployeeSalaryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeeSalaryKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String phoneNo = txtSelectedEmployeeSalary.getText();
        int length = phoneNo.length();
        if (length >= 6) {
            evt.consume();
        }

        if (!txtSelectedEmployeeSalary.getText().isEmpty()) {
            lblSelectedEmployeeSalaryValidation.setText(null);
            txtSelectedEmployeeSalary.setBorder(new FlatBorder());
            try {
                Integer.parseInt(txtSelectedEmployeeSalary.getText());
                lblSelectedEmployeeSalaryValidation.setText(null);
            } catch (NumberFormatException e) {
                lblSelectedEmployeeSalaryValidation.setText("Please Input a Valid Salary!");
                txtSelectedEmployeeSalary.setBorder(new LineBorder(Color.red));
                txtSelectedEmployeeSalary.setMinimumSize(new Dimension(64, 31));

            }
        }
    }//GEN-LAST:event_txtSelectedEmployeeSalaryKeyTyped

    private void txtSelectedEmployeeSalaryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeeSalaryKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSelectedEmployeeSalary.getText().isEmpty()) {
                lblSelectedEmployeeSalaryValidation.setText("Please Fill this Field!");
                txtSelectedEmployeeSalary.setBorder(new LineBorder(Color.red));
                txtSelectedEmployeeSalary.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSelectedEmployeeSalaryValidation.setText(null);
                lblSelectedEmployeeIdValidation.setText(null);
                lblSelectedEmployeeUsernameValidation.setText(null);
                txtSelectedEmployeeSalary.setBorder(new FlatBorder());
            }
            updateEmployee();
            jPanel11.setVisible(false);
            jPanel11.setVisible(true);
        }
    }//GEN-LAST:event_txtSelectedEmployeeSalaryKeyPressed

    private void txtSelectedEmployeePositionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSelectedEmployeePositionKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtSelectedEmployeePosition.getText().isEmpty()) {
                lblSelectedEmployeePositionValidation.setText("Please Fill this Field!");
                txtSelectedEmployeePosition.setBorder(new LineBorder(Color.red));
                txtSelectedEmployeePosition.setMinimumSize(new Dimension(64, 31));
            } else {
                lblSelectedEmployeePositionValidation.setText(null);
                lblSelectedEmployeeIdValidation.setText(null);
                lblSelectedEmployeeUsernameValidation.setText(null);
                txtSelectedEmployeePosition.setBorder(new FlatBorder());
            }
            txtSelectedEmployeeNIC.grabFocus();
        }
    }//GEN-LAST:event_txtSelectedEmployeePositionKeyPressed

    private void cmbSortEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortEmployeeActionPerformed
        searchEmployee();
    }//GEN-LAST:event_cmbSortEmployeeActionPerformed

    private void btnRefreshEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshEmployeeActionPerformed
        txtSearchEmployee.setText(null);
        txtSelectedEmployeeId.setText(null);
        txtSelectedEmployeeUsername.setText(null);
        txtSelectedEmployeePosition.setText(null);
        txtSelectedEmployeeNIC.setText(null);
        txtSelectedEmployeePhoneNo.setText(null);
        txtSelectedEmployeeSalary.setText(null);
        refreshEmployeeTable();
        cmbSortEmployee.setSelectedIndex(0);
        jPanel10.setVisible(false);
        jPanel10.setVisible(true);
    }//GEN-LAST:event_btnRefreshEmployeeActionPerformed

    private void txtSearchEmployeeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchEmployeeKeyReleased
        searchEmployee();
    }//GEN-LAST:event_txtSearchEmployeeKeyReleased

    private void btnPrintAllEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintAllEmployeeActionPerformed
        printAllEmployee();
        jPanel10.setVisible(false);
        jPanel10.setVisible(true);
    }//GEN-LAST:event_btnPrintAllEmployeeActionPerformed

    private void btnDeleteEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteEmployeeActionPerformed
        if (tblEmployee.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Employee Selected!");
            jPanel10.setVisible(false);
            jPanel10.setVisible(true);

        } else {

            deleteEmployee();

            jPanel10.setVisible(false);
            jPanel10.setVisible(true);
        }
    }//GEN-LAST:event_btnDeleteEmployeeActionPerformed

    private void btnEditEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditEmployeeActionPerformed
        if (tblEmployee.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Employee Selected!");
            jPanel10.setVisible(false);
            jPanel10.setVisible(true);

        } else {
            jTabbedPaneEmployee.setSelectedIndex(1);
        }
    }//GEN-LAST:event_btnEditEmployeeActionPerformed

    private void tblEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmployeeMouseClicked
        txtSelectedEmployeeId.setText((String) tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 0));
        txtSelectedEmployeeUsername.setText((String) tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 1));
        txtSelectedEmployeePosition.setText((String) tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 2));
        txtSelectedEmployeeNIC.setText((String) tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 3));
        txtSelectedEmployeePhoneNo.setText((String) tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 4));
        txtSelectedEmployeeSalary.setText((String) tblEmployee.getValueAt(tblEmployee.getSelectedRow(), 5));

        lblSelectedEmployeeIdValidation.setText(null);
        lblSelectedEmployeeUsernameValidation.setText(null);
        lblSelectedEmployeePositionValidation.setText(null);
        lblSelectedEmployeePositionValidation.setText(null);
        lblSelectedEmployeePhoneNoValidation.setText(null);
        lblSelectedEmployeeSalaryValidation.setText(null);

        txtSelectedEmployeeId.setBorder(new FlatBorder());
        txtSelectedEmployeeUsername.setBorder(new FlatBorder());
        txtSelectedEmployeePosition.setBorder(new FlatBorder());
        txtSelectedEmployeePhoneNo.setBorder(new FlatBorder());
        txtSelectedEmployeeSalary.setBorder(new FlatBorder());
    }//GEN-LAST:event_tblEmployeeMouseClicked

    private void txtEmployeeUsernameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeUsernameKeyTyped
        char c = evt.getKeyChar();
        if (!((Character.isDigit(c)) || (Character.isLetter(c)) || (Character.isISOControl(c)))) {
            evt.consume();
        }

        try {
            ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE username like '" + txtEmployeeUsername.getText() + "' ");

            if (rs.next()) {
                lblEmployeeUserameValidation.setText("Username Already Exists!");
                txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeeUserameValidation.setText(null);
                txtEmployeeUsername.setBorder(new FlatBorder());
            }

        } catch (Exception e) {
        }
    }//GEN-LAST:event_txtEmployeeUsernameKeyTyped

    private void txtEmployeeUsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeUsernameKeyPressed
        if (evt.getKeyCode() == 10) {

            if (txtEmployeeUsername.getText().isEmpty()) {
                lblEmployeeUserameValidation.setText("Please Fill this Field!");
                txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));
            } else {
                lblEmployeeUserameValidation.setText(null);
                txtEmployeeUsername.setBorder(new FlatBorder());
            }
            try {

                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE username like '" + txtEmployeeUsername.getText() + "' ");

                if (rs.next()) {
                    lblEmployeeUserameValidation.setText("Username Already Exists!");
                    txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                    txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));

                } else {
                    lblEmployeeUserameValidation.setText(null);
                    txtEmployeeUsername.setBorder(new FlatBorder());
                }

            } catch (Exception e) {
            }

            txtEmployeePosition.grabFocus();

        }
    }//GEN-LAST:event_txtEmployeeUsernameKeyPressed

    private void txtEmployeePositionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeePositionKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtEmployeePosition.getText().isEmpty()) {
                lblEmployeePositionValidation.setText("Please Fill this Field!");
                txtEmployeePosition.setBorder(new LineBorder(Color.red));
                txtEmployeePosition.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeePositionValidation.setText(null);
                txtEmployeePosition.setBorder(new FlatBorder());
            }

            if (txtEmployeeUsername.getText().isEmpty()) {
                lblEmployeeUserameValidation.setText("Please Fill this Field!");
                txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));
            } else {
                lblEmployeeUserameValidation.setText(null);
                txtEmployeeUsername.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE username like '" + txtEmployeeUsername.getText() + "'");

                if (rs.next()) {
                    lblEmployeeUserameValidation.setText("Username Already Exists!");
                    txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                    txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));
                } else {
                    lblEmployeeUserameValidation.setText(null);
                    txtEmployeeUsername.setBorder(new FlatBorder());
                }
            } catch (Exception e) {
            }
            txtEmployeeNIC.grabFocus();
        }
    }//GEN-LAST:event_txtEmployeePositionKeyPressed

    private void txtEmployeeNICKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeNICKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String nic = txtEmployeeNIC.getText();
        int length = nic.length();

        if (length >= 12) {
            evt.consume();
        }

        if (length < 8) {
            lblEmployeeNICValidation.setText("Please Input a Valid NIC!");
            txtEmployeeNIC.setBorder(new LineBorder(Color.red));
            txtEmployeeNIC.setMinimumSize(new Dimension(64, 31));

        } else {
            lblEmployeeNICValidation.setText(null);
            txtEmployeeNIC.setBorder(new FlatBorder());
        }
    }//GEN-LAST:event_txtEmployeeNICKeyTyped

    private void txtEmployeeNICKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeNICKeyPressed
        if (evt.getKeyCode() == 10) {

            if (txtEmployeeNIC.getText().isEmpty()) {
                lblEmployeeNICValidation.setText("Please Fill this Field!");
                txtEmployeeNIC.setBorder(new LineBorder(Color.red));
                txtEmployeeNIC.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeeNICValidation.setText(null);
                txtEmployeeNIC.setBorder(new FlatBorder());
            }

            if (txtEmployeeUsername.getText().isEmpty()) {
                lblEmployeeUserameValidation.setText("Please Fill this Field!");
                txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeeUserameValidation.setText(null);
                txtEmployeeUsername.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE username like '" + txtEmployeeUsername.getText() + "'");

                if (rs.next()) {
                    lblEmployeeUserameValidation.setText("Username Already Exists!");
                    txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                    txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));

                } else {
                    lblEmployeeUserameValidation.setText(null);
                    txtEmployeeUsername.setBorder(new FlatBorder());
                }
            } catch (Exception e) {
            }
            txtEmployeePhoneNo.grabFocus();
        }
    }//GEN-LAST:event_txtEmployeeNICKeyPressed

    private void txtEmployeePhoneNoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeePhoneNoKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String phoneNo = txtEmployeePhoneNo.getText();
        int length = phoneNo.length();
        if (length >= 10) {
            evt.consume();
        }

        if (length < 9) {
            lblEmployeePhoneNoValidation.setText("Please Input a Valid Phone Number!");
            txtEmployeePhoneNo.setBorder(new LineBorder(Color.red));
            txtEmployeePhoneNo.setMinimumSize(new Dimension(64, 31));

        } else {
            lblEmployeePhoneNoValidation.setText(null);
            txtEmployeePhoneNo.setBorder(new FlatBorder());

        }
    }//GEN-LAST:event_txtEmployeePhoneNoKeyTyped

    private void txtEmployeePhoneNoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeePhoneNoKeyPressed
        if (evt.getKeyCode() == 10) {
            String phoneNo = txtEmployeePhoneNo.getText();
            int length = phoneNo.length();

            if (phoneNo.isEmpty() || (length < 10)) {
                lblEmployeePhoneNoValidation.setText("Please Fill this Field!");
                txtEmployeePhoneNo.setBorder(new LineBorder(Color.red));
                txtEmployeePhoneNo.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeePhoneNoValidation.setText(null);
                txtEmployeePhoneNo.setBorder(new FlatBorder());
            }

            if (txtEmployeeUsername.getText().isEmpty()) {
                lblEmployeeUserameValidation.setText("Please Fill this Field!");
                txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeeUserameValidation.setText(null);
                txtEmployeeUsername.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE username like '" + txtEmployeeUsername.getText() + "'");

                if (rs.next()) {
                    lblEmployeeUserameValidation.setText("Username Already Exists!");
                    txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                    txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));

                } else {
                    lblEmployeeUserameValidation.setText(null);
                    txtEmployeeUsername.setBorder(new FlatBorder());
                }

            } catch (Exception e) {
            }
            txtEmployeeSalary.grabFocus();
        }
    }//GEN-LAST:event_txtEmployeePhoneNoKeyPressed

    private void txtEmployeeSalaryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeSalaryKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String phoneNo = txtEmployeeSalary.getText();
        int length = phoneNo.length();

        if (length >= 6) {
            evt.consume();
        }

        if (!txtEmployeeSalary.getText().isEmpty()) {
            lblEmployeeSalaryValidation.setText(null);
            txtEmployeeSalary.setBorder(new FlatBorder());

            try {
                Integer.parseInt(txtEmployeeSalary.getText());
                lblEmployeeSalaryValidation.setText(null);

            } catch (NumberFormatException e) {
                lblEmployeeSalaryValidation.setText("Please Input a Valid Salary!");
                txtEmployeeSalary.setBorder(new LineBorder(Color.red));
                txtEmployeeSalary.setMinimumSize(new Dimension(64, 31));

            }
        }
    }//GEN-LAST:event_txtEmployeeSalaryKeyTyped

    private void txtEmployeeSalaryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmployeeSalaryKeyPressed
        if (evt.getKeyCode() == 10) {
            String selectedSalary = txtEmployeeSalary.getText();
            int length = selectedSalary.length();
            if (selectedSalary.isEmpty() || (length < 6)) {

                if (selectedSalary.isEmpty()) {
                    lblEmployeeSalaryValidation.setText("Please Fill this Field!");
                    txtEmployeeSalary.setBorder(new LineBorder(Color.red));
                    txtEmployeeSalary.setMinimumSize(new Dimension(64, 31));

                } else {
                    lblEmployeeSalaryValidation.setText(null);
                    txtEmployeeSalary.setBorder(new FlatBorder());
                }
            }

            if (txtEmployeeUsername.getText().isEmpty()) {
                lblEmployeeUserameValidation.setText("Please Fill this Field!");
                txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeeUserameValidation.setText(null);
                txtEmployeeUsername.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE username like '" + txtEmployeeUsername.getText() + "'");

                if (rs.next()) {
                    lblEmployeeUserameValidation.setText("Username Already Exists!");
                    txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                    txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));

                } else {
                    lblEmployeeUserameValidation.setText(null);
                    txtEmployeeUsername.setBorder(new FlatBorder());
                }
            } catch (Exception e) {
            }
            pwdEmployeePassword.grabFocus();
        }
    }//GEN-LAST:event_txtEmployeeSalaryKeyPressed

    private void pwdEmployeePasswordKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdEmployeePasswordKeyTyped
        String password = (String.valueOf(pwdEmployeePassword.getPassword()));
        int length = password.length();

        if (length < 3) {
            lblEmployeePasswordValidation.setText("Password is Too Short!");
            pwdEmployeePassword.setBorder(new LineBorder(Color.red));
            pwdEmployeePassword.setMinimumSize(new Dimension(64, 31));

        } else {
            lblEmployeePasswordValidation.setText(null);
            pwdEmployeePassword.setBorder(new FlatBorder());
            pwdRepeatEmployeePassword.setBorder(new FlatBorder());

        }
    }//GEN-LAST:event_pwdEmployeePasswordKeyTyped

    private void pwdEmployeePasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdEmployeePasswordKeyPressed
        if (evt.getKeyCode() == 10) {
            if (String.valueOf(pwdEmployeePassword.getPassword()).isEmpty()) {
                lblEmployeePasswordValidation.setText("Please Fill this Field!");
                pwdEmployeePassword.setBorder(new LineBorder(Color.red));
                pwdEmployeePassword.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeePasswordValidation.setText(null);
                pwdEmployeePassword.setBorder(new FlatBorder());
            }

            if (txtEmployeeUsername.getText().isEmpty()) {
                lblEmployeeUserameValidation.setText("Please Fill this Field!");
                txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeeUserameValidation.setText(null);
                txtEmployeeUsername.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE username like '" + txtEmployeeUsername.getText() + "'");

                if (rs.next()) {
                    lblEmployeeUserameValidation.setText("Username Already Exists!");
                    txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                    txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));

                } else {
                    lblEmployeeUserameValidation.setText(null);
                    txtEmployeeUsername.setBorder(new FlatBorder());
                }

            } catch (Exception e) {
            }
            pwdRepeatEmployeePassword.grabFocus();
        }
    }//GEN-LAST:event_pwdEmployeePasswordKeyPressed

    private void btnAddEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddEmployeeActionPerformed
        addNewEmployee();
        card7.setVisible(false);
        card7.setVisible(true);
    }//GEN-LAST:event_btnAddEmployeeActionPerformed

    private void pwdRepeatEmployeePasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwdRepeatEmployeePasswordKeyPressed
        if (evt.getKeyCode() == 10) {

            if (String.valueOf(pwdRepeatEmployeePassword.getPassword()).isEmpty()) {
                lblEmployeeRepeatPasswordValidation.setText("Please Fill this Field!");
                pwdRepeatEmployeePassword.setBorder(new LineBorder(Color.red));
                pwdRepeatEmployeePassword.setMinimumSize(new Dimension(64, 31));
                pwdEmployeePassword.grabFocus();

            } else {
                lblEmployeeRepeatPasswordValidation.setText(null);
                pwdRepeatEmployeePassword.setBorder(new FlatBorder());
            }

            if (txtEmployeeUsername.getText().isEmpty()) {
                lblEmployeeUserameValidation.setText("Please Fill this Field!");
                txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));

            } else {
                lblEmployeeUserameValidation.setText(null);
                txtEmployeeUsername.setBorder(new FlatBorder());
            }

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE username like '" + txtEmployeeUsername.getText() + "'");

                if (rs.next()) {
                    lblEmployeeUserameValidation.setText("Username Already Exists!");
                    txtEmployeeUsername.setBorder(new LineBorder(Color.red));
                    txtEmployeeUsername.setMinimumSize(new Dimension(64, 31));

                } else {
                    lblEmployeeUserameValidation.setText(null);
                    txtEmployeeUsername.setBorder(new FlatBorder());
                }

            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_pwdRepeatEmployeePasswordKeyPressed

    private void pwdRepeatEmployeePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwdRepeatEmployeePasswordActionPerformed
        addNewEmployee();
        card7.setVisible(false);
        card7.setVisible(true);
    }//GEN-LAST:event_pwdRepeatEmployeePasswordActionPerformed

    private void jLabel31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel31MouseClicked
        card7.setVisible(false);
        card8.setVisible(false);
        card9.setVisible(true);

        resetLblColor(jLabel8);
        resetLblColor(jLabel21);
        setLblColor(jLabel31);
    }//GEN-LAST:event_jLabel31MouseClicked

    private void jLabel21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel21MouseClicked
        card7.setVisible(false);
        card8.setVisible(true);
        card9.setVisible(false);

        resetLblColor(jLabel8);
        setLblColor(jLabel21);
        resetLblColor(jLabel31);
    }//GEN-LAST:event_jLabel21MouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        card7.setVisible(true);
        card8.setVisible(false);
        card9.setVisible(false);

        setLblColor(jLabel8);
        resetLblColor(jLabel21);
        resetLblColor(jLabel31);
        txtEmployeeUsername.grabFocus();

    }//GEN-LAST:event_jLabel8MouseClicked

    private void txtGRNItemLowStock1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemLowStock1KeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();
        }

        String sellingPrice = txtGRNItemLowStock1.getText();
        int length = sellingPrice.length();

        if (length >= 6) {
            evt.consume();
        }
    }//GEN-LAST:event_txtGRNItemLowStock1KeyTyped

    private void txtGRNItemLowStock1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemLowStock1KeyPressed
        if (evt.getKeyCode() == 10) {

            if (txtGRNItemLowStock1.getText().isEmpty()) {
                lblPOItemCodeValidation3.setText("Please Fill this Field!");
                txtGRNItemLowStock1.setBorder(new LineBorder(Color.red));
                txtGRNItemLowStock1.setMinimumSize(new Dimension(64, 31));

            } else {
                lblPOItemCodeValidation3.setText(null);
                txtGRNItemLowStock1.setBorder(new FlatBorder());
            }

            if ((tblGRN1.getSelectedRowCount() == 0)) {
                JOptionPane.showMessageDialog(this, "No GRN Item Selected!");
                jTabbedPaneAddExistingItems.setSelectedIndex(2);

            } else {
                addReOrdersToStock();
                jPanel50.setVisible(false);
                jPanel50.setVisible(true);
            }
        }
    }//GEN-LAST:event_txtGRNItemLowStock1KeyPressed

    private void btnViewGRN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewGRN1ActionPerformed
        jTabbedPaneAddExistingItems.setSelectedIndex(2);
    }//GEN-LAST:event_btnViewGRN1ActionPerformed

    private void txtGRNItemSellingPrice1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemSellingPrice1KeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();
        }

        String sellingPrice = txtGRNItemSellingPrice1.getText();
        int length = sellingPrice.length();

        if (length >= 7) {
            evt.consume();
        }
    }//GEN-LAST:event_txtGRNItemSellingPrice1KeyTyped

    private void txtGRNItemSellingPrice1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemSellingPrice1KeyPressed
        if (evt.getKeyCode() == 10) {

            if (txtGRNItemSellingPrice1.getText().isEmpty()) {
                lblPOItemCodeValidation2.setText("Please Fill this Field!");
                txtGRNItemSellingPrice1.setBorder(new LineBorder(Color.red));
                txtGRNItemSellingPrice1.setMinimumSize(new Dimension(64, 31));

            } else {
                lblPOItemCodeValidation2.setText(null);
                txtGRNItemSellingPrice1.setBorder(new FlatBorder());
            }

            txtGRNItemLowStock1.grabFocus();
        }
    }//GEN-LAST:event_txtGRNItemSellingPrice1KeyPressed

    private void btnCheckAndAddToStock1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckAndAddToStock1ActionPerformed
        if ((tblGRN1.getSelectedRowCount() == 0)) {
            JOptionPane.showMessageDialog(this, "No GRN Item Selected!");
            jTabbedPaneAddExistingItems.setSelectedIndex(2);

        } else {
            addReOrdersToStock();
            jPanel50.setVisible(false);
            jPanel50.setVisible(true);
        }
    }//GEN-LAST:event_btnCheckAndAddToStock1ActionPerformed

    private void btnMarkAsBadOrder1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarkAsBadOrder1ActionPerformed

        if (tblGRN1.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No GRN Item Selected!");
            jPanel49.setVisible(false);
            jPanel49.setVisible(true);

        } else {
            markAsBadOrder1();
            jPanel49.setVisible(false);
            jPanel49.setVisible(true);
        }
    }//GEN-LAST:event_btnMarkAsBadOrder1ActionPerformed

    private void btnRefreshGRN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshGRN1ActionPerformed
        refreshGRNTable1();
        txtSearchGRNItem1.setText(txtPOId1.getText());
        cmbSortGRNItem1.setSelectedIndex(0);
        lblGRNId1.setText(txtPOId1.getText());
        calculateGRNTotal1();
        jPanel49.setVisible(false);
        jPanel49.setVisible(true);

    }//GEN-LAST:event_btnRefreshGRN1ActionPerformed

    private void btnPrintGRN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintGRN1ActionPerformed
        printSelectedGRNBill1();
        jPanel49.setVisible(false);
        jPanel49.setVisible(true);
    }//GEN-LAST:event_btnPrintGRN1ActionPerformed

    private void cmbSortGRNItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortGRNItem1ActionPerformed
        searchGRNItem1();
    }//GEN-LAST:event_cmbSortGRNItem1ActionPerformed

    private void txtSearchGRNItem1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchGRNItem1KeyTyped
        switch (cmbSortGRNItem1.getSelectedIndex()) {
            case 0:

                if (txtSearchGRNItem1.getText().isEmpty()) {
                    lblGRNId1.setText(" * ");
                } else {

                    lblGRNId1.setText(txtSearchGRNItem1.getText());
                }
                break;
            case 1:

                if (txtSearchGRNItem1.getText().isEmpty()) {
                    lblGRNId1.setText(" * ");
                } else {

                    lblGRNId1.setText(txtSearchGRNItem1.getText());

                }
                break;
            default:
                lblGRNId1.setText(" * ");
        }
    }//GEN-LAST:event_txtSearchGRNItem1KeyTyped

    private void txtSearchGRNItem1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchGRNItem1KeyReleased
        searchGRNItem1();

        switch (cmbSortGRNItem1.getSelectedIndex()) {
            case 0:

                if (txtSearchGRNItem1.getText().isEmpty()) {
                    lblGRNId1.setText(" * ");
                } else {
                    lblGRNId1.setText(txtSearchGRNItem1.getText());
                }
                break;
            case 1:

                if (txtSearchGRNItem1.getText().isEmpty()) {
                    lblGRNId1.setText(" * ");
                } else {
                    lblGRNId1.setText(txtSearchGRNItem1.getText());
                }
                break;
            default:
                lblGRNId1.setText(" * ");
        }
    }//GEN-LAST:event_txtSearchGRNItem1KeyReleased

    private void btnCheckGRNItems1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckGRNItems1ActionPerformed
        String selectedGRNItemStatus = (txtGRNStatus1.getText());

        switch (selectedGRNItemStatus) {
            case "Checked":
                JOptionPane.showMessageDialog(this, "Item Already Added to Stock!");
                jPanel49.setVisible(false);
                jPanel49.setVisible(true);
                break;
            case "Bad Order":
                JOptionPane.showMessageDialog(this, "Bad Order Items cannot be Added to Stock!");
                jPanel49.setVisible(false);
                jPanel49.setVisible(true);
                break;
            default:
                if (tblGRN1.getSelectedRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "No GRN Item Selected!");
                    jPanel49.setVisible(false);
                    jPanel49.setVisible(true);

                } else {
                    jTabbedPaneAddExistingItems.setSelectedIndex(3);
                    txtGRNItemSellingPrice1.grabFocus();
                }
                break;
        }
    }//GEN-LAST:event_btnCheckGRNItems1ActionPerformed

    private void tblGRN1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGRN1MouseClicked
        txtGRNItemName1.setText((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 1));
        txtGRNItemQuantity1.setText((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 3));
        txtGRNItemPrice1.setText((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 4));
        txtGRNStatus1.setText((String) tblGRN1.getValueAt(tblGRN1.getSelectedRow(), 6));

        try {
            String selectedGRNItemName = txtGRNItemName1.getText();
            ResultSet rs = DB.DB.getData("SELECT low_stock FROM stock WHERE item_name = '" + selectedGRNItemName + "' ");

            if (rs.next()) {
                txtGRNItemLowStock1.setText(rs.getString(1));
            }
            DB.DB.con.close();

            ResultSet rs1 = DB.DB.getData("SELECT stock_count FROM stock WHERE item_name = '" + selectedGRNItemName + "' ");

            if (rs1.next()) {
                txtGRNItemStockCount1.setText(rs1.getString(1));
            }
            DB.DB.con.close();

            ResultSet rs2 = DB.DB.getData("SELECT selling_price FROM stock WHERE item_name = '" + selectedGRNItemName + "' ");

            if (rs2.next()) {
                txtGRNItemSellingPrice1.setText(rs2.getString(1));
            }
            DB.DB.con.close();

        } catch (Exception e) {
        }
    }//GEN-LAST:event_tblGRN1MouseClicked

    private void btnViewAddToPOList1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAddToPOList1ActionPerformed
        jTabbedPaneAddExistingItems.setSelectedIndex(0);
    }//GEN-LAST:event_btnViewAddToPOList1ActionPerformed

    private void btnRefreshPOItems1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshPOItems1ActionPerformed
        refreshPOTable1();
        txtSearchPOItem1.setText(txtPOId1.getText());
        cmbSortPOItem1.setSelectedIndex(0);
        lblPOId2.setText(txtPOId1.getText());
        calculatePOTotal();
        jPanel46.setVisible(false);
        jPanel46.setVisible(true);
    }//GEN-LAST:event_btnRefreshPOItems1ActionPerformed

    private void btnDeletePOItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletePOItem1ActionPerformed
        if (!(tblPOItem1.getSelectedRowCount() == 0)) {
            deletePOItem1();
            jPanel46.setVisible(false);
            jPanel46.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No Item Selected!");
            jPanel46.setVisible(false);
            jPanel46.setVisible(true);
        }
    }//GEN-LAST:event_btnDeletePOItem1ActionPerformed

    private void btnPrintPOBill1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintPOBill1ActionPerformed
        printSelectedPOBill1();
        jPanel46.setVisible(false);
        jPanel46.setVisible(true);
    }//GEN-LAST:event_btnPrintPOBill1ActionPerformed

    private void cmbSortPOItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortPOItem1ActionPerformed
        searchPOItem1();
    }//GEN-LAST:event_cmbSortPOItem1ActionPerformed

    private void txtSearchPOItem1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchPOItem1KeyTyped
        switch (cmbSortPOItem1.getSelectedIndex()) {
            case 0:

                if (txtSearchPOItem1.getText().isEmpty()) {
                    lblPOId2.setText(" * ");
                    lblTotal3.setText("$ *");
                } else {

                    lblPOId2.setText(txtSearchPOItem1.getText());
                    calculatePOTotal();

                }
                break;
            default:
                lblPOId2.setText(" * ");
                lblTotal3.setText("$ *");
        }
    }//GEN-LAST:event_txtSearchPOItem1KeyTyped

    private void txtSearchPOItem1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchPOItem1KeyReleased
        searchPOItem1();

        switch (cmbSortPOItem1.getSelectedIndex()) {
            case 0:

                if (txtSearchPOItem1.getText().isEmpty()) {
                    lblPOId2.setText(" * ");
                } else {
                    lblPOId2.setText(txtSearchPOItem1.getText());
                }
                break;
            default:
                lblPOId2.setText(" * ");
        }
    }//GEN-LAST:event_txtSearchPOItem1KeyReleased

    private void btnIssuePOBill1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIssuePOBill1ActionPerformed
        if (!(tblPOItem1.getRowCount() == 0)) {
            purchaseOrder1();
            jPanel46.setVisible(false);
            jPanel46.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No Items Found on PO Bill!");
            jPanel46.setVisible(false);
            jPanel46.setVisible(true);
        }
    }//GEN-LAST:event_btnIssuePOBill1ActionPerformed

    private void tblPOItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPOItem1MouseClicked
        selectedPOItemId1 = ((String) tblPOItem1.getValueAt(tblPOItem1.getSelectedRow(), 0));
    }//GEN-LAST:event_tblPOItem1MouseClicked

    private void cmbPOItemName1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOItemName1KeyPressed
        if (evt.getKeyCode() == 10) {
            txtPOItemQuantity1.grabFocus();
        }
    }//GEN-LAST:event_cmbPOItemName1KeyPressed

    private void cmbPOLeadTime1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOLeadTime1KeyPressed
        if (evt.getKeyCode() == 10) {
            addPOItem1();
            jPanel36.setVisible(false);
            jPanel36.setVisible(true);
        }
    }//GEN-LAST:event_cmbPOLeadTime1KeyPressed

    private void cmbPOItemSupplier1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOItemSupplier1KeyPressed
        if (evt.getKeyCode() == 10) {
            try {
                String selectedSupplier = cmbPOItemSupplier1.getSelectedItem().toString();

                cmbPOItemName1.removeAllItems();

                ResultSet rs = DB.DB.getData("SELECT item_name FROM item WHERE item_status = 'Available' AND supplier = '" + selectedSupplier + "'");

                while (rs.next()) {
                    cmbPOItemName1.addItem(rs.getString(1));
                }
                DB.DB.con.close();
            } catch (Exception e) {
            }

            cmbPOItemName1.setPopupVisible(true);
            cmbPOItemName1.grabFocus();
        }
    }//GEN-LAST:event_cmbPOItemSupplier1KeyPressed

    private void txtPOItemPrice1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemPrice1KeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String price = txtPOItemPrice1.getText();
        int length = price.length();
        if (length >= 7) {
            evt.consume();
        }

        if (!txtPOItemPrice1.getText().isEmpty()) {
            lblPOItemCodeValidation2.setText(null);
            txtPOItemPrice1.setBorder(new FlatBorder());
            try {
                Integer.parseInt(txtPOItemPrice1.getText());
                lblPOItemCodeValidation2.setText(null);
            } catch (NumberFormatException e) {
                lblPOItemCodeValidation2.setText("Please Input a Valid Quantity!");
                txtPOItemPrice1.setBorder(new LineBorder(Color.red));
                txtPOItemPrice1.setMinimumSize(new Dimension(64, 31));

            }
        }
    }//GEN-LAST:event_txtPOItemPrice1KeyTyped

    private void txtPOItemPrice1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemPrice1KeyPressed
        if (evt.getKeyCode() == 10) {

            if (txtPOItemPrice1.getText().isEmpty()) {
                lblPOItemCodeValidation1.setText("Please Fill this Field!");
                txtPOItemPrice1.setBorder(new LineBorder(Color.red));
                txtPOItemPrice1.setMinimumSize(new Dimension(64, 31));
            } else {
                lblPOItemCodeValidation1.setText(null);
                txtPOItemPrice1.setBorder(new FlatBorder());
            }

            cmbPOLeadTime1.setPopupVisible(true);
            cmbPOLeadTime1.grabFocus();
        }
    }//GEN-LAST:event_txtPOItemPrice1KeyPressed

    private void btnViewPOItems1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewPOItems1ActionPerformed
        jTabbedPaneAddExistingItems.setSelectedIndex(1);
        txtSearchPOItem1.setText(txtPOId1.getText());
        lblPOId2.setText(txtPOId1.getText());
        calculatePOTotal();
        searchPOItem1();
        searchPOItem();
    }//GEN-LAST:event_btnViewPOItems1ActionPerformed

    private void btnClearPOFields1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearPOFields1ActionPerformed
        cmbPOItemSupplier1.setSelectedIndex(0);
        cmbPOItemName1.setSelectedItem(null);
        txtPOItemQuantity1.setText(null);
        txtPOItemPrice1.setText(null);
        cmbPOLeadTime1.setSelectedIndex(0);
        jPanel37.setVisible(false);
        jPanel37.setVisible(true);
        cmbPOItemName1.grabFocus();
    }//GEN-LAST:event_btnClearPOFields1ActionPerformed

    private void btnAddToPOList1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToPOList1ActionPerformed
        addPOItem1();
        jPanel37.setVisible(false);
        jPanel37.setVisible(true);
    }//GEN-LAST:event_btnAddToPOList1ActionPerformed

    private void txtPOItemQuantity1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemQuantity1KeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String quantity = txtPOItemQuantity1.getText();
        int length = quantity.length();
        if (length >= 6) {
            evt.consume();
        }

        if (!txtPOItemQuantity1.getText().isEmpty()) {
            lblPOItemQuantityValidation1.setText(null);
            txtPOItemQuantity1.setBorder(new FlatBorder());
            try {
                Integer.parseInt(txtPOItemQuantity1.getText());
                lblPOItemQuantityValidation1.setText(null);
            } catch (NumberFormatException e) {
                lblPOItemQuantityValidation1.setText("Please Input a Valid Quantity!");
                txtPOItemQuantity1.setBorder(new LineBorder(Color.red));
                txtPOItemQuantity1.setMinimumSize(new Dimension(64, 31));

            }
        }
    }//GEN-LAST:event_txtPOItemQuantity1KeyTyped

    private void txtPOItemQuantity1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemQuantity1KeyPressed
        if (evt.getKeyCode() == 10) {

            if (txtPOItemQuantity1.getText().isEmpty()) {
                lblPOItemNameValidation1.setText("Please Fill this Field!");
                txtPOItemQuantity1.setBorder(new LineBorder(Color.red));
                txtPOItemQuantity1.setMinimumSize(new Dimension(64, 31));
            } else {
                lblPOItemNameValidation1.setText(null);
                txtPOItemQuantity1.setBorder(new FlatBorder());
            }

            txtPOItemPrice1.grabFocus();
        }
    }//GEN-LAST:event_txtPOItemQuantity1KeyPressed

    private void btnReOrder1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReOrder1ActionPerformed
        if (tblLowStock.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Re-order Item Selected!");
        } else {
            card1.setVisible(false);
            card3.setVisible(true);
            card2.setVisible(false);

            jTabbedPaneAddExistingItems.setSelectedIndex(0);
            txtPOItemQuantity1.grabFocus();

            resetLblColor(jLabel44);
            setLblColor(jLabel45);
            resetLblColor(jLabel46);
        }
        jPanel8.setVisible(false);
        jPanel8.setVisible(true);
    }//GEN-LAST:event_btnReOrder1ActionPerformed

    private void btnPrintLowStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintLowStockActionPerformed
        printLowStock();
        jPanel8.setVisible(false);
        jPanel8.setVisible(true);
    }//GEN-LAST:event_btnPrintLowStockActionPerformed

    private void tblLowStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLowStockMouseClicked
        try {

            String reorderSupplier = ((String) tblLowStock.getValueAt(tblLowStock.getSelectedRow(), 5));
            cmbPOItemSupplier1.setSelectedItem(reorderSupplier);

            String reOrderItemName = ((String) tblLowStock.getValueAt(tblLowStock.getSelectedRow(), 2));

            ResultSet rs = DB.DB.getData("SELECT item_price FROM stock WHERE item_name = '" + reOrderItemName + "' ");

            if (rs.next()) {
                cmbPOItemName1.setSelectedItem(reOrderItemName);
                txtPOItemPrice1.setText(rs.getString(1));

            }
            DB.DB.con.close();

        } catch (Exception e) {
        }
    }//GEN-LAST:event_tblLowStockMouseClicked

    private void cmbSortLowStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortLowStockActionPerformed
        searchLowStock();
    }//GEN-LAST:event_cmbSortLowStockActionPerformed

    private void txtSearchLowStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchLowStockKeyTyped
        calculateLowStockCount();
    }//GEN-LAST:event_txtSearchLowStockKeyTyped

    private void txtSearchLowStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchLowStockKeyReleased
        searchLowStock();
    }//GEN-LAST:event_txtSearchLowStockKeyReleased

    private void btnPrintBarcodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintBarcodesActionPerformed
        printBarcodes();
        jPanel20.setVisible(false);
        jPanel20.setVisible(true);
    }//GEN-LAST:event_btnPrintBarcodesActionPerformed

    private void cmbSortStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortStockActionPerformed
        searchStock();
    }//GEN-LAST:event_cmbSortStockActionPerformed

    private void txtSearchStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStockKeyTyped
        calculateStockCount();
    }//GEN-LAST:event_txtSearchStockKeyTyped

    private void txtSearchStockKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchStockKeyReleased
        searchStock();
    }//GEN-LAST:event_txtSearchStockKeyReleased

    private void btnPrintAllStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintAllStockActionPerformed
        printStock();
        jPanel20.setVisible(false);
        jPanel20.setVisible(true);
    }//GEN-LAST:event_btnPrintAllStockActionPerformed

    private void btnReOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReOrderActionPerformed
        if (tblStock.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No Stock Item Selected!");
        } else {

            card1.setVisible(false);
            card3.setVisible(true);
            card2.setVisible(false);

            jTabbedPaneAddExistingItems.setSelectedIndex(0);
            txtPOItemQuantity1.grabFocus();

            resetLblColor(jLabel44);
            setLblColor(jLabel45);
            resetLblColor(jLabel46);

        }
        jPanel20.setVisible(false);
        jPanel20.setVisible(true);
    }//GEN-LAST:event_btnReOrderActionPerformed

    private void btnRefreshStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshStockActionPerformed
        refreshStockTable();
        jPanel20.setVisible(false);
        jPanel20.setVisible(true);
    }//GEN-LAST:event_btnRefreshStockActionPerformed

    private void tblStockMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblStockMouseClicked
        try {

            String reorderSupplier = ((String) tblStock.getValueAt(tblStock.getSelectedRow(), 5));
            cmbPOItemSupplier1.setSelectedItem(reorderSupplier);
            String reOrderItemName = ((String) tblStock.getValueAt(tblStock.getSelectedRow(), 2));
            ResultSet rs = DB.DB.getData("SELECT item_price FROM stock WHERE item_name = '" + reOrderItemName + "' ");

            if (rs.next()) {
                cmbPOItemName1.setSelectedItem(reOrderItemName);
                txtPOItemPrice1.setText(rs.getString(1));

            }
            DB.DB.con.close();

        } catch (Exception e) {
        }
    }//GEN-LAST:event_tblStockMouseClicked

    private void btnViewGRNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewGRNActionPerformed
        jTabbedPaneAddNewItems.setSelectedIndex(2);
    }//GEN-LAST:event_btnViewGRNActionPerformed

    private void btnCheckAndAddToStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckAndAddToStockActionPerformed

        if ((tblGRN.getSelectedRowCount() == 0)) {
            JOptionPane.showMessageDialog(this, "No GRN Item Selected!");
            jTabbedPaneAddNewItems.setSelectedIndex(2);

        } else {
            addToStock();
            jPanel43.setVisible(false);
            jPanel43.setVisible(true);
        }
    }//GEN-LAST:event_btnCheckAndAddToStockActionPerformed

    private void txtGRNItemLowStockKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemLowStockKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String lowStock = txtGRNItemLowStock.getText();
        int length = lowStock.length();
        if (length >= 5) {
            evt.consume();
        }
    }//GEN-LAST:event_txtGRNItemLowStockKeyTyped

    private void txtGRNItemLowStockKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemLowStockKeyPressed
        if (evt.getKeyCode() == 10) {

            if (txtGRNItemLowStock.getText().isEmpty()) {
                lblPOItemNameValidation3.setText("Please Fill this Field!");
                txtGRNItemLowStock.setBorder(new LineBorder(Color.red));
                txtGRNItemLowStock.setMinimumSize(new Dimension(64, 31));

            } else {
                lblPOItemNameValidation3.setText(null);
                lblSelectedEmployeeIdValidation.setText(null);
                txtGRNItemLowStock.setBorder(new FlatBorder());
            }

            if ((tblGRN.getSelectedRowCount() == 0)) {
                JOptionPane.showMessageDialog(this, "No GRN Item Selected!");
                jTabbedPaneAddNewItems.setSelectedIndex(2);

            } else {
                addToStock();
                jPanel43.setVisible(false);
                jPanel43.setVisible(true);
            }
        }
    }//GEN-LAST:event_txtGRNItemLowStockKeyPressed

    private void txtGRNItemSellingPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemSellingPriceKeyTyped
        char c = evt.getKeyChar();

        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String sellingPrice = txtGRNItemSellingPrice.getText();
        int length = sellingPrice.length();

        if (length >= 7) {
            evt.consume();
        }
    }//GEN-LAST:event_txtGRNItemSellingPriceKeyTyped

    private void txtGRNItemSellingPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRNItemSellingPriceKeyPressed
        if (evt.getKeyCode() == 10) {

            if (txtGRNItemSellingPrice.getText().isEmpty()) {
                lblPOItemNameValidation2.setText("Please Fill this Field!");
                txtGRNItemSellingPrice.setBorder(new LineBorder(Color.red));
                txtGRNItemSellingPrice.setMinimumSize(new Dimension(64, 31));
            } else {
                lblPOItemNameValidation2.setText(null);
                txtGRNItemSellingPrice.setBorder(new FlatBorder());
            }

            txtGRNItemLowStock.grabFocus();
        }
    }//GEN-LAST:event_txtGRNItemSellingPriceKeyPressed

    private void btnMarkAsBadOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarkAsBadOrderActionPerformed

        if (tblGRN.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No GRN Item Selected!");
            jPanel42.setVisible(false);
            jPanel42.setVisible(true);

        } else {
            markAsBadOrder();
            jPanel42.setVisible(false);
            jPanel42.setVisible(true);
        }
    }//GEN-LAST:event_btnMarkAsBadOrderActionPerformed

    private void btnRefreshGRNItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshGRNItemsActionPerformed
        refreshGRNTable();
        txtSearchGRNItem.setText(txtPOId.getText());
        cmbSortGRNItem.setSelectedIndex(0);
        lblGRNId.setText(txtPOId.getText());
        calculateGRNTotal();
        jPanel42.setVisible(false);
        jPanel42.setVisible(true);
    }//GEN-LAST:event_btnRefreshGRNItemsActionPerformed

    private void btnPrintGRNBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintGRNBillActionPerformed
        printSelectedGRNBill();
        jPanel42.setVisible(false);
        jPanel42.setVisible(true);
    }//GEN-LAST:event_btnPrintGRNBillActionPerformed

    private void cmbSortGRNItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortGRNItemActionPerformed
        searchGRNItem();
    }//GEN-LAST:event_cmbSortGRNItemActionPerformed

    private void txtSearchGRNItemKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchGRNItemKeyTyped
        switch (cmbSortGRNItem.getSelectedIndex()) {
            case 0:

                if (txtSearchGRNItem.getText().isEmpty()) {
                    lblGRNId.setText(" * ");

                } else {
                    lblGRNId.setText(txtSearchGRNItem.getText());
                }
                break;
            case 1:

                if (txtSearchGRNItem.getText().isEmpty()) {
                    lblGRNId.setText(" * ");
                } else {

                    lblGRNId.setText(txtSearchGRNItem.getText());

                }
                break;
            default:
                lblGRNId.setText(" * ");
        }
    }//GEN-LAST:event_txtSearchGRNItemKeyTyped

    private void txtSearchGRNItemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchGRNItemKeyReleased
        searchGRNItem();

        switch (cmbSortGRNItem.getSelectedIndex()) {
            case 0:

                if (txtSearchGRNItem.getText().isEmpty()) {
                    lblGRNId.setText(" * ");

                } else {
                    lblGRNId.setText(txtSearchGRNItem.getText());
                }
                break;
            case 1:

                if (txtSearchGRNItem.getText().isEmpty()) {
                    lblGRNId.setText(" * ");

                } else {
                    lblGRNId.setText(txtSearchGRNItem.getText());
                }
                break;
            default:
                lblGRNId.setText(" * ");
        }
    }//GEN-LAST:event_txtSearchGRNItemKeyReleased

    private void btnCheckGRNItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckGRNItemsActionPerformed
        String selectedGRNItemStatus = (txtGRNStatus.getText());

        switch (selectedGRNItemStatus) {
            case "Checked":
                JOptionPane.showMessageDialog(this, "Item Already Added to Stock!");
                jPanel42.setVisible(false);
                jPanel42.setVisible(true);
                break;
            case "Bad Order":
                JOptionPane.showMessageDialog(this, "Bad Order Items cannot be Added to Stock!");
                jPanel42.setVisible(false);
                jPanel42.setVisible(true);
                break;
            default:
                if (tblGRN.getSelectedRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "No GRN Item Selected!");
                    jPanel42.setVisible(false);
                    jPanel42.setVisible(true);

                } else {
                    jTabbedPaneAddNewItems.setSelectedIndex(3);
                    txtGRNItemSellingPrice.grabFocus();
                }
                break;
        }
    }//GEN-LAST:event_btnCheckGRNItemsActionPerformed

    private void tblGRNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGRNMouseClicked
        txtGRNItemName.setText((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 1));
        txtGRNItemQuantity.setText((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 3));
        txtGRNItemPrice.setText((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 4));
        txtGRNStatus.setText((String) tblGRN.getValueAt(tblGRN.getSelectedRow(), 6));
    }//GEN-LAST:event_tblGRNMouseClicked

    private void btnViewAddToPOListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAddToPOListActionPerformed
        jTabbedPaneAddNewItems.setSelectedIndex(0);
    }//GEN-LAST:event_btnViewAddToPOListActionPerformed

    private void btnRefreshPOItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshPOItemsActionPerformed
        refreshPOTable();
        txtSearchPOItem.setText(txtPOId.getText());
        cmbSortPOItem.setSelectedIndex(0);
        lblPOId.setText(txtPOId.getText());
        calculatePOTotal();
        jPanel40.setVisible(false);
        jPanel40.setVisible(true);
    }//GEN-LAST:event_btnRefreshPOItemsActionPerformed

    private void btnDeletePOItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeletePOItemActionPerformed
        if (!(tblPOItem.getSelectedRowCount() == 0)) {
            deletePOItem();
            jPanel40.setVisible(false);
            jPanel40.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "No Item Selected!");
            jPanel40.setVisible(false);
            jPanel40.setVisible(true);
        }
    }//GEN-LAST:event_btnDeletePOItemActionPerformed

    private void btnPrintPOBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintPOBillActionPerformed
        printSelectedPOBill();
        jPanel40.setVisible(false);
        jPanel40.setVisible(true);
    }//GEN-LAST:event_btnPrintPOBillActionPerformed

    private void cmbSortPOItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortPOItemActionPerformed
        searchPOItem();
    }//GEN-LAST:event_cmbSortPOItemActionPerformed

    private void txtSearchPOItemKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchPOItemKeyTyped

        switch (cmbSortPOItem.getSelectedIndex()) {
            case 0:

                if (txtSearchPOItem.getText().isEmpty()) {
                    lblPOId.setText(" * ");
                    lblTotal.setText("$ *");

                } else {
                    lblPOId.setText(txtSearchPOItem.getText());
                    calculatePOTotal();

                }
                break;
            default:
                lblPOId.setText(" * ");
                lblTotal.setText("$ *");
        }
    }//GEN-LAST:event_txtSearchPOItemKeyTyped

    private void txtSearchPOItemKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchPOItemKeyReleased
        searchPOItem();

        switch (cmbSortPOItem.getSelectedIndex()) {
            case 0:

                if (txtSearchPOItem.getText().isEmpty()) {
                    lblPOId.setText(" * ");
                } else {
                    lblPOId.setText(txtSearchPOItem.getText());
                }
                break;
            default:
                lblPOId.setText(" * ");
        }

    }//GEN-LAST:event_txtSearchPOItemKeyReleased

    private void btnIssuePOBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIssuePOBillActionPerformed
        if (!(tblPOItem.getRowCount() == 0)) {
            purchaseOrder();
            jPanel40.setVisible(false);
            jPanel40.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No Items Found on PO Bill!");
            jPanel40.setVisible(false);
            jPanel40.setVisible(true);
        }

    }//GEN-LAST:event_btnIssuePOBillActionPerformed

    private void tblPOItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPOItemMouseClicked
        selectedPOItemId = ((String) tblPOItem.getValueAt(tblPOItem.getSelectedRow(), 0));
    }//GEN-LAST:event_tblPOItemMouseClicked

    private void cmbPOItemNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOItemNameKeyPressed
        if (evt.getKeyCode() == 10) {
            txtPOItemQuantity.grabFocus();
        }
    }//GEN-LAST:event_cmbPOItemNameKeyPressed

    private void cmbPOLeadTimeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOLeadTimeKeyPressed
        if (evt.getKeyCode() == 10) {
            addPOItem();
            jPanel36.setVisible(false);
            jPanel36.setVisible(true);
        }
    }//GEN-LAST:event_cmbPOLeadTimeKeyPressed

    private void txtPOItemPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemPriceKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String price = txtPOItemPrice.getText();
        int length = price.length();
        if (length >= 7) {
            evt.consume();
        }

        if (!txtPOItemPrice.getText().isEmpty()) {
            lblPOItemNameValidation.setText(null);
            txtPOItemPrice.setBorder(new FlatBorder());
            try {
                Integer.parseInt(txtPOItemPrice.getText());
                lblPOItemNameValidation.setText(null);
            } catch (NumberFormatException e) {
                lblPOItemNameValidation.setText("Please Input a Valid Quantity!");
                txtPOItemPrice.setBorder(new LineBorder(Color.red));
                txtPOItemPrice.setMinimumSize(new Dimension(64, 31));

            }
        }
    }//GEN-LAST:event_txtPOItemPriceKeyTyped

    private void txtPOItemPriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemPriceKeyPressed
        if (evt.getKeyCode() == 10) {

            if (txtPOItemPrice.getText().isEmpty()) {
                lblPOItemNameValidation.setText("Please Fill this Field!");
                txtPOItemPrice.setBorder(new LineBorder(Color.red));
                txtPOItemPrice.setMinimumSize(new Dimension(64, 31));

            } else {
                lblPOItemCodeValidation.setText(null);
                lblPOItemNameValidation.setText(null);
                txtPOItemPrice.setBorder(new FlatBorder());
            }

            cmbPOLeadTime.setPopupVisible(true);
            cmbPOLeadTime.grabFocus();
        }
    }//GEN-LAST:event_txtPOItemPriceKeyPressed

    private void btnViewPOItemsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewPOItemsActionPerformed
        jTabbedPaneAddNewItems.setSelectedIndex(1);
        txtSearchPOItem.setText(txtPOId.getText());
        lblPOId.setText(txtPOId.getText());
        calculatePOTotal();
        searchPOItem();
        searchPOItem1();
    }//GEN-LAST:event_btnViewPOItemsActionPerformed

    private void btnClearPOFieldsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearPOFieldsActionPerformed
        cmbPOItemSupplier.setSelectedIndex(0);
        cmbPOItemName.setSelectedItem(null);
        txtPOItemQuantity.setText(null);
        txtPOItemPrice.setText(null);
        cmbPOLeadTime.setSelectedIndex(0);
        jPanel36.setVisible(false);
        jPanel36.setVisible(true);
        cmbPOItemName.grabFocus();
    }//GEN-LAST:event_btnClearPOFieldsActionPerformed

    private void btnAddToPOListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddToPOListActionPerformed
        addPOItem();
        jPanel36.setVisible(false);
        jPanel36.setVisible(true);
    }//GEN-LAST:event_btnAddToPOListActionPerformed

    private void txtPOItemQuantityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemQuantityKeyTyped
        char c = evt.getKeyChar();
        if (!(Character.isDigit(c))) {
            evt.consume();

        }
        String quantity = txtPOItemQuantity.getText();
        int length = quantity.length();
        if (length >= 6) {
            evt.consume();
        }

        if (!txtPOItemQuantity.getText().isEmpty()) {
            lblPOItemCodeValidation.setText(null);
            txtPOItemQuantity.setBorder(new FlatBorder());
            try {
                Integer.parseInt(txtPOItemQuantity.getText());
                lblPOItemCodeValidation.setText(null);
            } catch (NumberFormatException e) {
                lblPOItemCodeValidation.setText("Please Input a Valid Quantity!");
                txtPOItemQuantity.setBorder(new LineBorder(Color.red));
                txtPOItemQuantity.setMinimumSize(new Dimension(64, 31));

            }
        }
    }//GEN-LAST:event_txtPOItemQuantityKeyTyped

    private void txtPOItemQuantityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOItemQuantityKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtPOItemQuantity.getText().isEmpty()) {
                lblPOItemCodeValidation.setText("Please Fill this Field!");
                txtPOItemQuantity.setBorder(new LineBorder(Color.red));
                txtPOItemQuantity.setMinimumSize(new Dimension(64, 31));

            } else {
                lblPOItemCodeValidation.setText(null);
                txtPOItemQuantity.setBorder(new FlatBorder());
            }
            txtPOItemPrice.grabFocus();
        }
    }//GEN-LAST:event_txtPOItemQuantityKeyPressed

    private void jLabel46MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel46MouseClicked
        card1.setVisible(false);
        card3.setVisible(false);
        card2.setVisible(true);

        resetLblColor(jLabel44);
        resetLblColor(jLabel45);
        setLblColor(jLabel46);
    }//GEN-LAST:event_jLabel46MouseClicked

    private void jLabel45MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel45MouseClicked
        card1.setVisible(false);
        card3.setVisible(true);
        card2.setVisible(false);

        resetLblColor(jLabel44);
        setLblColor(jLabel45);
        resetLblColor(jLabel46);
    }//GEN-LAST:event_jLabel45MouseClicked

    private void jLabel44MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel44MouseClicked
        card1.setVisible(true);
        card3.setVisible(false);
        card2.setVisible(false);

        setLblColor(jLabel44);
        resetLblColor(jLabel45);
        resetLblColor(jLabel46);
    }//GEN-LAST:event_jLabel44MouseClicked

    private void btn_addUsers1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_addUsers1MouseClicked
        card4.setVisible(false);
        card5.setVisible(false);
        card6.setVisible(true);

        resetLblColor(btn_addUsers3);
        resetLblColor(btn_addUsers2);
        setLblColor(btn_addUsers1);
    }//GEN-LAST:event_btn_addUsers1MouseClicked

    private void btn_addUsers2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_addUsers2MouseClicked
        card4.setVisible(false);
        card5.setVisible(true);
        card6.setVisible(false);

        resetLblColor(btn_addUsers3);
        setLblColor(btn_addUsers2);
        resetLblColor(btn_addUsers1);
    }//GEN-LAST:event_btn_addUsers2MouseClicked

    private void btn_addUsers3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_addUsers3MouseClicked
        card4.setVisible(true);
        card5.setVisible(false);
        card6.setVisible(false);

        setLblColor(btn_addUsers3);
        resetLblColor(btn_addUsers2);
        resetLblColor(btn_addUsers1);
    }//GEN-LAST:event_btn_addUsers3MouseClicked

    private void txtSearchTodaySalesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchTodaySalesKeyReleased
        searchTodaySalesTable();
    }//GEN-LAST:event_txtSearchTodaySalesKeyReleased

    private void cmbSortTodaySalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortTodaySalesActionPerformed
        searchTodaySalesTable();
    }//GEN-LAST:event_cmbSortTodaySalesActionPerformed

    private void btnRefreshTodaySalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshTodaySalesActionPerformed
        refreshSalesTable();

        jPanel26.setVisible(false);
        jPanel26.setVisible(true);
    }//GEN-LAST:event_btnRefreshTodaySalesActionPerformed

    private void txtSearchSalesHistoryKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchSalesHistoryKeyReleased
        searchSalesHistoryTable();
    }//GEN-LAST:event_txtSearchSalesHistoryKeyReleased

    private void cmbSortSalesHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbSortSalesHistoryActionPerformed
        searchSalesHistoryTable();
    }//GEN-LAST:event_cmbSortSalesHistoryActionPerformed

    private void btnRefreshSalesHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshSalesHistoryActionPerformed
        refreshSalesHistoryTable();

        jPanel19.setVisible(false);
        jPanel19.setVisible(true);
    }//GEN-LAST:event_btnRefreshSalesHistoryActionPerformed

    private void cmbPOItemSupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPOItemSupplierActionPerformed
        try {
            String selectedSupplier = cmbPOItemSupplier.getSelectedItem().toString();

            cmbPOItemName.removeAllItems();

            ResultSet rs = DB.DB.getData("SELECT item_name FROM item WHERE item_status = 'Unavailable' AND supplier = '" + selectedSupplier + "'");

            while (rs.next()) {
                cmbPOItemName.addItem(rs.getString(1));
            }
            DB.DB.con.close();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_cmbPOItemSupplierActionPerformed

    private void cmbPOItemSupplier1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPOItemSupplier1ActionPerformed
        try {
            String selectedSupplier = cmbPOItemSupplier1.getSelectedItem().toString();

            cmbPOItemName1.removeAllItems();

            ResultSet rs = DB.DB.getData("SELECT item_name FROM item WHERE item_status = 'Available' AND supplier = '" + selectedSupplier + "'");

            while (rs.next()) {
                cmbPOItemName1.addItem(rs.getString(1));
            }
            DB.DB.con.close();
        } catch (Exception e) {
        }

    }//GEN-LAST:event_cmbPOItemSupplier1ActionPerformed

    private void cmbPOItemSupplierKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbPOItemSupplierKeyPressed
        if (evt.getKeyCode() == 10) {
            try {
                String selectedSupplier = cmbPOItemSupplier.getSelectedItem().toString();

                cmbPOItemName.removeAllItems();

                ResultSet rs = DB.DB.getData("SELECT item_name FROM item WHERE item_status = 'Unavailable' AND supplier = '" + selectedSupplier + "'");

                while (rs.next()) {
                    cmbPOItemName.addItem(rs.getString(1));
                }
                DB.DB.con.close();
            } catch (Exception e) {
            }
            cmbPOItemName.setPopupVisible(true);
            cmbPOItemName.grabFocus();
        }
    }//GEN-LAST:event_cmbPOItemSupplierKeyPressed

    private void btnPrintTodaySalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintTodaySalesActionPerformed
        printTodaySalesReport();
        jPanel26.setVisible(false);
        jPanel26.setVisible(true);
    }//GEN-LAST:event_btnPrintTodaySalesActionPerformed

    private void btnPrintSalesHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintSalesHistoryActionPerformed
        printSalesReport();
        jPanel19.setVisible(false);
        jPanel19.setVisible(true);
    }//GEN-LAST:event_btnPrintSalesHistoryActionPerformed

    private void cmbSelectedItemSupplierItemCategoryKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbSelectedItemSupplierItemCategoryKeyPressed
        if (evt.getKeyCode() == 10) {
            if (tblSupplierItem.getSelectedRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No Item Selected!");
                jTabbedPaneSupplierItem.setSelectedIndex(1);

            } else {
                updateSupplierItem();

                jPanel44.setVisible(false);
                jPanel44.setVisible(true);
            }
        }
    }//GEN-LAST:event_cmbSelectedItemSupplierItemCategoryKeyPressed

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
                new AdminDashboard().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/xeon_icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel adminName;
    private javax.swing.JButton btnAddEmployee;
    private javax.swing.JButton btnAddSupplier;
    private javax.swing.JButton btnAddToPOList;
    private javax.swing.JButton btnAddToPOList1;
    private javax.swing.JButton btnBackup;
    private javax.swing.JButton btnBrowseBackupPath;
    private javax.swing.JButton btnBrowseRestoreLocation;
    private javax.swing.JButton btnCheckAndAddToStock;
    private javax.swing.JButton btnCheckAndAddToStock1;
    private javax.swing.JButton btnCheckGRNItems;
    private javax.swing.JButton btnCheckGRNItems1;
    private javax.swing.JButton btnClearPOFields;
    private javax.swing.JButton btnClearPOFields1;
    private javax.swing.JButton btnDeleteEmployee;
    private javax.swing.JButton btnDeletePOItem;
    private javax.swing.JButton btnDeletePOItem1;
    private javax.swing.JButton btnDeleteSelectedEmployee;
    private javax.swing.JButton btnDeleteSelectedSupplier;
    private javax.swing.JButton btnDeleteSelectedSupplierItem;
    private javax.swing.JButton btnDeleteSupplier;
    private javax.swing.JButton btnDeleteSupplierItem;
    private javax.swing.JButton btnEditEmployee;
    private javax.swing.JButton btnEditSupplier;
    private javax.swing.JButton btnEditSupplierItem;
    private javax.swing.JButton btnGoBackToPayrollSummary;
    private javax.swing.JButton btnGoBackToViewEmployee;
    private javax.swing.JButton btnGoBackToViewItem;
    private javax.swing.JButton btnGoBackToViewSupplier;
    private javax.swing.JButton btnIssuePOBill;
    private javax.swing.JButton btnIssuePOBill1;
    private javax.swing.JButton btnMarkAsBadOrder;
    private javax.swing.JButton btnMarkAsBadOrder1;
    private javax.swing.JButton btnPayEmployee;
    private javax.swing.JButton btnPaySelectedEmployee;
    private javax.swing.JButton btnPrintAllEmployee;
    private javax.swing.JButton btnPrintAllStock;
    private javax.swing.JButton btnPrintAllSupplier;
    private javax.swing.JButton btnPrintBarcodes;
    private javax.swing.JButton btnPrintGRN1;
    private javax.swing.JButton btnPrintGRNBill;
    private javax.swing.JButton btnPrintLowStock;
    private javax.swing.JButton btnPrintPOBill;
    private javax.swing.JButton btnPrintPOBill1;
    private javax.swing.JButton btnPrintSalesHistory;
    private javax.swing.JButton btnPrintSelectedEmployee;
    private javax.swing.JButton btnPrintSelectedEmployeePayrollBill;
    private javax.swing.JButton btnPrintSelectedSupplier;
    private javax.swing.JButton btnPrintTodaySales;
    private javax.swing.JButton btnReOrder;
    private javax.swing.JButton btnReOrder1;
    private javax.swing.JButton btnRefreshEmployee;
    private javax.swing.JButton btnRefreshEmployeeAttendance;
    private javax.swing.JButton btnRefreshEmployeePayroll;
    private javax.swing.JButton btnRefreshGRN1;
    private javax.swing.JButton btnRefreshGRNItems;
    private javax.swing.JButton btnRefreshMonthlyEmployeeAttendance;
    private javax.swing.JButton btnRefreshPOItems;
    private javax.swing.JButton btnRefreshPOItems1;
    private javax.swing.JButton btnRefreshPayrollHistory;
    private javax.swing.JButton btnRefreshSalesHistory;
    private javax.swing.JButton btnRefreshStock;
    private javax.swing.JButton btnRefreshSupplier;
    private javax.swing.JButton btnRefreshSupplierItem;
    private javax.swing.JButton btnRefreshTodaySales;
    private javax.swing.JButton btnRestore;
    private javax.swing.JButton btnSaveItem;
    private javax.swing.JButton btnUpdateSelectedEmployee;
    private javax.swing.JButton btnUpdateSelectedSupplier;
    private javax.swing.JButton btnUpdateSelectedSupplierItem;
    private javax.swing.JButton btnViewAddToPOList;
    private javax.swing.JButton btnViewAddToPOList1;
    private javax.swing.JButton btnViewGRN;
    private javax.swing.JButton btnViewGRN1;
    private javax.swing.JButton btnViewPOItems;
    private javax.swing.JButton btnViewPOItems1;
    private javax.swing.JLabel btn_addUsers1;
    private javax.swing.JLabel btn_addUsers2;
    private javax.swing.JLabel btn_addUsers3;
    private javax.swing.JPanel card1;
    private javax.swing.JPanel card10;
    private javax.swing.JPanel card11;
    private javax.swing.JPanel card12;
    private javax.swing.JPanel card13;
    private javax.swing.JPanel card14;
    private javax.swing.JPanel card15;
    private javax.swing.JPanel card2;
    private javax.swing.JPanel card3;
    private javax.swing.JPanel card4;
    private javax.swing.JPanel card5;
    private javax.swing.JPanel card6;
    private javax.swing.JPanel card7;
    private javax.swing.JPanel card8;
    private javax.swing.JPanel card9;
    private javax.swing.JPanel cardEmployee;
    private javax.swing.JPanel cardHome;
    private javax.swing.JLabel cardName;
    private javax.swing.JPanel cardSales;
    private javax.swing.JPanel cardSettings;
    private javax.swing.JPanel cardStock;
    private javax.swing.JPanel cardSupplier;
    private javax.swing.JLabel close1;
    private javax.swing.JLabel close2;
    private javax.swing.JComboBox<String> cmbItemCategory;
    private javax.swing.JComboBox<String> cmbItemSupplier;
    private javax.swing.JComboBox<String> cmbPOItemName;
    private javax.swing.JComboBox<String> cmbPOItemName1;
    private javax.swing.JComboBox<String> cmbPOItemSupplier;
    private javax.swing.JComboBox<String> cmbPOItemSupplier1;
    private javax.swing.JComboBox<String> cmbPOLeadTime;
    private javax.swing.JComboBox<String> cmbPOLeadTime1;
    private javax.swing.JComboBox<String> cmbSelectedItemSupplierItemCategory;
    private javax.swing.JComboBox<String> cmbSortEmployee;
    private javax.swing.JComboBox<String> cmbSortEmployeeAttendance;
    private javax.swing.JComboBox<String> cmbSortEmployeePayroll;
    private javax.swing.JComboBox<String> cmbSortEmployeePayrollHistory;
    private javax.swing.JComboBox<String> cmbSortGRNItem;
    private javax.swing.JComboBox<String> cmbSortGRNItem1;
    private javax.swing.JComboBox<String> cmbSortLowStock;
    private javax.swing.JComboBox<String> cmbSortMonthlyEmployeeAttendance;
    private javax.swing.JComboBox<String> cmbSortPOItem;
    private javax.swing.JComboBox<String> cmbSortPOItem1;
    private javax.swing.JComboBox<String> cmbSortSalesHistory;
    private javax.swing.JComboBox<String> cmbSortStock;
    private javax.swing.JComboBox<String> cmbSortSupplier;
    private javax.swing.JComboBox<String> cmbSortSupplierItem;
    private javax.swing.JComboBox<String> cmbSortTodaySales;
    private javax.swing.JLabel employee;
    private javax.swing.JLabel home;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel140;
    private javax.swing.JLabel jLabel141;
    private javax.swing.JLabel jLabel142;
    private javax.swing.JLabel jLabel143;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel166;
    private javax.swing.JLabel jLabel167;
    private javax.swing.JLabel jLabel168;
    private javax.swing.JLabel jLabel169;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel170;
    private javax.swing.JLabel jLabel171;
    private javax.swing.JLabel jLabel172;
    private javax.swing.JLabel jLabel173;
    private javax.swing.JLabel jLabel174;
    private javax.swing.JLabel jLabel175;
    private javax.swing.JLabel jLabel176;
    private javax.swing.JLabel jLabel177;
    private javax.swing.JLabel jLabel178;
    private javax.swing.JLabel jLabel179;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel180;
    private javax.swing.JLabel jLabel181;
    private javax.swing.JLabel jLabel182;
    private javax.swing.JLabel jLabel183;
    private javax.swing.JLabel jLabel184;
    private javax.swing.JLabel jLabel185;
    private javax.swing.JLabel jLabel186;
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
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane21;
    private javax.swing.JScrollPane jScrollPane22;
    private javax.swing.JScrollPane jScrollPane23;
    private javax.swing.JScrollPane jScrollPane24;
    private javax.swing.JScrollPane jScrollPane26;
    private javax.swing.JScrollPane jScrollPane27;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPaneAddExistingItems;
    private javax.swing.JTabbedPane jTabbedPaneAddNewItems;
    private javax.swing.JTabbedPane jTabbedPaneEmployee;
    private javax.swing.JTabbedPane jTabbedPaneStock;
    private javax.swing.JTabbedPane jTabbedPaneSupplier;
    private javax.swing.JTabbedPane jTabbedPaneSupplierItem;
    private org.jdesktop.swingx.JXDatePicker jXDatePicker1;
    private javax.swing.JLabel lblAbsenceCount;
    public static final javax.swing.JLabel lblAdminUsername = new javax.swing.JLabel();
    private javax.swing.JLabel lblAttendanceCount;
    private javax.swing.JLabel lblAttendanceCount3;
    private javax.swing.JLabel lblAttendanceCount4;
    private javax.swing.JLabel lblAttendanceCount6;
    private javax.swing.JLabel lblAttendanceCount7;
    private javax.swing.JLabel lblDate;
    private javax.swing.JLabel lblEmployeeCount;
    private javax.swing.JLabel lblEmployeeNICValidation;
    private javax.swing.JLabel lblEmployeePasswordValidation;
    private javax.swing.JLabel lblEmployeePhoneNoValidation;
    private javax.swing.JLabel lblEmployeePositionValidation;
    private javax.swing.JLabel lblEmployeeRepeatPasswordValidation;
    private javax.swing.JLabel lblEmployeeSalaryValidation;
    private javax.swing.JLabel lblEmployeeUserameValidation;
    private javax.swing.JLabel lblGRNId;
    private javax.swing.JLabel lblGRNId1;
    private javax.swing.JLabel lblGRNTotal;
    private javax.swing.JLabel lblGRNTotal1;
    private javax.swing.JLabel lblHomeEmployeeAttendanceAbsent;
    private javax.swing.JLabel lblHomeEmployeeAttendancePresent;
    private javax.swing.JLabel lblHomeEmployeeCount;
    private javax.swing.JLabel lblHomeLowStockCount;
    private javax.swing.JLabel lblHomePayrollPaid;
    private javax.swing.JLabel lblHomePayrollPaidAmount;
    private javax.swing.JLabel lblHomePayrollUnpaid;
    private javax.swing.JLabel lblHomePayrollUnpaidAmount;
    private javax.swing.JLabel lblHomePendingPOCount;
    private javax.swing.JLabel lblHomeStockCount;
    private javax.swing.JLabel lblHomeSupplierCount;
    private javax.swing.JLabel lblItemCodeValidation;
    private javax.swing.JLabel lblItemCount;
    private javax.swing.JLabel lblItemNameValidation;
    private javax.swing.JLabel lblLogout;
    private javax.swing.JLabel lblLowStockCount;
    private javax.swing.JLabel lblPOId;
    private javax.swing.JLabel lblPOId1;
    private javax.swing.JLabel lblPOId2;
    private javax.swing.JLabel lblPOId3;
    private javax.swing.JLabel lblPOItemCodeValidation;
    private javax.swing.JLabel lblPOItemCodeValidation1;
    private javax.swing.JLabel lblPOItemCodeValidation2;
    private javax.swing.JLabel lblPOItemCodeValidation3;
    private javax.swing.JLabel lblPOItemNameValidation;
    private javax.swing.JLabel lblPOItemNameValidation1;
    private javax.swing.JLabel lblPOItemNameValidation2;
    private javax.swing.JLabel lblPOItemNameValidation3;
    private javax.swing.JLabel lblPOItemQuantityValidation1;
    private javax.swing.JLabel lblPaidCount;
    private javax.swing.JLabel lblSalesEarning;
    private javax.swing.JLabel lblSelectedEmployeeExtraSalaryValidation;
    private javax.swing.JLabel lblSelectedEmployeeIdValidation;
    private javax.swing.JLabel lblSelectedEmployeeNICValidation;
    private javax.swing.JLabel lblSelectedEmployeePaymentValidation;
    private javax.swing.JLabel lblSelectedEmployeePhoneNoValidation;
    private javax.swing.JLabel lblSelectedEmployeePositionValidation;
    private javax.swing.JLabel lblSelectedEmployeeSalaryValidation;
    private javax.swing.JLabel lblSelectedEmployeeUsernameValidation;
    private javax.swing.JLabel lblSelectedSupplierAddressValidation;
    private javax.swing.JLabel lblSelectedSupplierAddressValidation2;
    private javax.swing.JLabel lblSelectedSupplierCompanyValidation;
    private javax.swing.JLabel lblSelectedSupplierCompanyValidation2;
    private javax.swing.JLabel lblSelectedSupplierEmailValidation;
    private javax.swing.JLabel lblSelectedSupplierEmailValidation2;
    private javax.swing.JLabel lblSelectedSupplierIdValidation;
    private javax.swing.JLabel lblSelectedSupplierIdValidation2;
    private javax.swing.JLabel lblSelectedSupplierPhoneNoValidation;
    private javax.swing.JLabel lblSelectedSupplierPhoneNoValidation2;
    private javax.swing.JLabel lblSelectedSupplierUsernameValidation;
    private javax.swing.JLabel lblSelectedSupplierUsernameValidation2;
    private javax.swing.JLabel lblStockCount;
    private javax.swing.JLabel lblSupplierAddressValidation;
    private javax.swing.JLabel lblSupplierCompanyValidation;
    private javax.swing.JLabel lblSupplierCount;
    private javax.swing.JLabel lblSupplierEmailValidation;
    private javax.swing.JLabel lblSupplierPhoneNoValidation;
    private javax.swing.JLabel lblSupplierUserameValidation;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTodaySalesCount;
    private javax.swing.JLabel lblTodaySalesCount1;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotal1;
    private javax.swing.JLabel lblTotal2;
    private javax.swing.JLabel lblTotal3;
    private javax.swing.JLabel lblTotal4;
    private javax.swing.JLabel lblTotal5;
    private javax.swing.JLabel lblUnpaidCount;
    private javax.swing.JLabel loginWish;
    private javax.swing.JPanel mainCard;
    private javax.swing.JLabel maximize1;
    private javax.swing.JLabel maximize2;
    private javax.swing.JPanel maximizeView;
    private javax.swing.JLabel minimize1;
    private javax.swing.JLabel minimize2;
    private javax.swing.JPanel pnlAdminDashboard;
    private javax.swing.JPanel pnlBranding;
    private javax.swing.JPanel pnlDateTime;
    private javax.swing.JPanel pnlEmployee;
    private javax.swing.JPanel pnlEmployeeSubSelection;
    private javax.swing.JPanel pnlFrames;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlHome;
    private javax.swing.JPanel pnlHomeSubSelection;
    private javax.swing.JPanel pnlIndicatorEmployee;
    private javax.swing.JPanel pnlIndicatorHome;
    private javax.swing.JPanel pnlIndicatorSales;
    private javax.swing.JPanel pnlIndicatorSettings;
    private javax.swing.JPanel pnlIndicatorStock;
    private javax.swing.JPanel pnlIndicatorSupplier;
    private javax.swing.JPanel pnlLogout;
    private javax.swing.JPanel pnlLogoutLoader;
    private javax.swing.JPanel pnlLogoutLoaderBody;
    private javax.swing.JPanel pnlParent;
    private javax.swing.JPanel pnlSales;
    private javax.swing.JPanel pnlSalesSubSelection;
    private javax.swing.JPanel pnlSettings;
    private javax.swing.JPanel pnlSettingsSubSelection;
    private javax.swing.JPanel pnlSidebar;
    private javax.swing.JPanel pnlStock;
    private javax.swing.JPanel pnlStockSubSelection;
    private javax.swing.JPanel pnlSupplier;
    private javax.swing.JPanel pnlSupplierSubSelection;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JPanel pnlTopIcons;
    private javax.swing.JPasswordField pwdEmployeePassword;
    private javax.swing.JPasswordField pwdRepeatEmployeePassword;
    private javax.swing.JLabel reports;
    private javax.swing.JPanel restoreDownView;
    private javax.swing.JLabel sales;
    private javax.swing.JLabel stock;
    private javax.swing.JPanel subCardEmployee;
    private javax.swing.JPanel subCardSales;
    private javax.swing.JPanel subCardSettings;
    private javax.swing.JPanel subCardStock;
    private javax.swing.JPanel subCardSupplier;
    private javax.swing.JLabel supplier;
    private javax.swing.JTable tblEmployee;
    private javax.swing.JTable tblEmployeeAttendance;
    private javax.swing.JTable tblEmployeeMonthlyAttendance;
    private javax.swing.JTable tblEmployeePayroll;
    private javax.swing.JTable tblEmployeePayrollHistory;
    private javax.swing.JTable tblGRN;
    private javax.swing.JTable tblGRN1;
    private javax.swing.JTable tblLowStock;
    private javax.swing.JTable tblPOItem;
    private javax.swing.JTable tblPOItem1;
    private javax.swing.JTable tblSalesHistory;
    private javax.swing.JTable tblStock;
    private javax.swing.JTable tblSupplier;
    private javax.swing.JTable tblSupplierItem;
    private javax.swing.JTable tblTodaySales;
    private javax.swing.JLabel title;
    private javax.swing.JTextField txtBackupLocation;
    private javax.swing.JTextField txtEmployeeNIC;
    private javax.swing.JTextField txtEmployeePhoneNo;
    private javax.swing.JTextField txtEmployeePosition;
    private javax.swing.JTextField txtEmployeeSalary;
    private javax.swing.JTextField txtEmployeeUsername;
    private javax.swing.JTextField txtGRNItemLowStock;
    private javax.swing.JTextField txtGRNItemLowStock1;
    private javax.swing.JTextField txtGRNItemName;
    private javax.swing.JTextField txtGRNItemName1;
    private javax.swing.JTextField txtGRNItemPrice;
    private javax.swing.JTextField txtGRNItemPrice1;
    private javax.swing.JTextField txtGRNItemQuantity;
    private javax.swing.JTextField txtGRNItemQuantity1;
    private javax.swing.JTextField txtGRNItemSellingPrice;
    private javax.swing.JTextField txtGRNItemSellingPrice1;
    private javax.swing.JTextField txtGRNItemStockCount;
    private javax.swing.JTextField txtGRNItemStockCount1;
    private javax.swing.JTextField txtGRNStatus;
    private javax.swing.JTextField txtGRNStatus1;
    private javax.swing.JTextField txtItemCode;
    private javax.swing.JTextField txtItemName;
    private javax.swing.JTextField txtPOId;
    private javax.swing.JTextField txtPOId1;
    private javax.swing.JTextField txtPOItemPrice;
    private javax.swing.JTextField txtPOItemPrice1;
    private javax.swing.JTextField txtPOItemQuantity;
    private javax.swing.JTextField txtPOItemQuantity1;
    private javax.swing.JTextField txtRestoreLocation;
    private javax.swing.JTextField txtSearchEmployee;
    private javax.swing.JTextField txtSearchEmployeeAttendance;
    private javax.swing.JTextField txtSearchGRNItem;
    private javax.swing.JTextField txtSearchGRNItem1;
    private javax.swing.JTextField txtSearchLowStock;
    private javax.swing.JTextField txtSearchMonthlyEmployeeAttendance;
    private javax.swing.JTextField txtSearchPOItem;
    private javax.swing.JTextField txtSearchPOItem1;
    private javax.swing.JTextField txtSearchPayrollHistory;
    private javax.swing.JTextField txtSearchPayrollSummary;
    private javax.swing.JTextField txtSearchSalesHistory;
    private javax.swing.JTextField txtSearchStock;
    private javax.swing.JTextField txtSearchSupplier;
    private javax.swing.JTextField txtSearchSupplierItem;
    private javax.swing.JTextField txtSearchTodaySales;
    private javax.swing.JTextField txtSelectedEmployeeId;
    private javax.swing.JTextField txtSelectedEmployeeNIC;
    private javax.swing.JTextField txtSelectedEmployeePhoneNo;
    private javax.swing.JTextField txtSelectedEmployeePosition;
    private javax.swing.JTextField txtSelectedEmployeeSalary;
    private javax.swing.JTextField txtSelectedEmployeeUsername;
    private javax.swing.JTextField txtSelectedItemSupplierId;
    private javax.swing.JTextField txtSelectedItemSupplierItemCode;
    private javax.swing.JTextField txtSelectedItemSupplierItemName;
    private javax.swing.JTextField txtSelectedItemSupplierUsername;
    private javax.swing.JTextField txtSelectedPayrollEmployeeAttendance;
    private javax.swing.JTextField txtSelectedPayrollEmployeeExtraSalary;
    private javax.swing.JTextField txtSelectedPayrollEmployeeId;
    private javax.swing.JTextField txtSelectedPayrollEmployeeMonth;
    private javax.swing.JTextField txtSelectedPayrollEmployeePayment;
    private javax.swing.JTextField txtSelectedPayrollEmployeeSalary;
    private javax.swing.JTextField txtSelectedPayrollEmployeeStatus;
    private javax.swing.JTextField txtSelectedPayrollEmployeeUsername;
    private javax.swing.JTextField txtSelectedSupplierAddress;
    private javax.swing.JTextField txtSelectedSupplierCompany;
    private javax.swing.JTextField txtSelectedSupplierEmail;
    private javax.swing.JTextField txtSelectedSupplierId;
    private javax.swing.JTextField txtSelectedSupplierPhoneNo;
    private javax.swing.JTextField txtSelectedSupplierUsername;
    private javax.swing.JTextField txtSupplierAddress;
    private javax.swing.JTextField txtSupplierCompany;
    private javax.swing.JTextField txtSupplierEmail;
    private javax.swing.JTextField txtSupplierPhoneNo;
    private javax.swing.JTextField txtSupplierUsername;
    private javax.swing.JLabel version;
    private javax.swing.JLabel watermark;
    // End of variables declaration//GEN-END:variables
}
