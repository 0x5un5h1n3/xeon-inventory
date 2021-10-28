/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

/**
 *
 * @author 0x5un5h1n3

 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form login
     */
    int mousepX;
    int mousepY;
    Timer timer;
    String month;
    String year;
    String date;
    String time;
    String today;
    int counter;

    public Login() {
        setIcon();
        initComponents();
        Login.this.getRootPane().setBorder(new LineBorder(new Color(1, 1, 1, 1)));

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DateFormat wishBasedOnTime = new SimpleDateFormat("HH");
                Date date4 = new Date();
                String wishTime = wishBasedOnTime.format(date4);
                int now = Integer.parseInt(wishTime);

                if (now >= 6 && now < 12) {
                    loginWish.setText("Good morning");
                } else if (now >= 12 && now < 17) {
                    loginWish.setText("Good afternoon");
                } else if (now >= 17 && now < 20) {
                    loginWish.setText("Good evening");
                } else {
                    loginWish.setText("Good night");
                }

            }
        };
        timer = new Timer(1000, actionListener);

        timer.setInitialDelay(0);
        timer.start();

    }

    public void setPnlColor(JPanel pnl) {
        pnl.setBackground(new Color(35, 35, 35));
    }

    public void resetPnlColor(JPanel pnl) {
        pnl.setBackground(new Color(20, 20, 20));
    }

    private void employeeLogin() {
        String pwd = String.valueOf(pwdEmployeePassword.getPassword());
        counter++;
        if (counter > 3) {
            JOptionPane.showMessageDialog(this, "Intruder Login Detected, System will be Exited!", "Access Denied", JOptionPane.ERROR_MESSAGE);
            String IntruderloginActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM employee WHERE username= '" + txtEmployeeUsername.getText() + "'),'Employee' ,'" + txtEmployeeUsername.getText() + "','Intruder Login','" + txtEmployeeUsername.getText() + " has been detected as an Intruder' , 'FAILED')";
            try {
                DB.DB.putData(IntruderloginActivityLog);
            } catch (Exception ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.exit(0);

        } else {

            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM employee WHERE account_status = 'Active' AND username = '" + txtEmployeeUsername.getText() + "' AND password =  '" + pwd + "' ");
                if (rs.next()) {
                    String loginActivitySuccessLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM employee WHERE username= '" + txtEmployeeUsername.getText() + "'),'Employee','" + txtEmployeeUsername.getText() + "','Employee Login','" + txtEmployeeUsername.getText() + " logged in' , 'SUCCESS')";
                    DB.DB.putData(loginActivitySuccessLog);

                    pnlLogin.setVisible(false);
                    pnlLoginLoader.setVisible(true);
                    username.setText(txtEmployeeUsername.getText());

                    new java.util.Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {

                            for (float i = 0.985f; i > 0; i -= 0.000001) {
                                Login.this.setOpacity(i);
                            }

                            Login.this.dispose();

                            EmployeeDashboard ed = new EmployeeDashboard();
                            EmployeeDashboard.lblEmployeeUsername.setText(txtEmployeeUsername.getText());

                            try {

                                Date DateMonth = new Date();
                                SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
                                month = "" + toMonth.format(DateMonth);

                                Date DateYear = new Date();
                                SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
                                year = "" + toYear.format(DateYear);

                                ResultSet rs = DB.DB.getData("SELECT count(id) FROM employee_attendance WHERE month = '" + month + "' AND year = '" + year + "' AND username = '" + txtEmployeeUsername.getText() + "' ");
                                if (rs.next()) {

                                    int count = Integer.parseInt(rs.getString(1));

                                    EmployeeDashboard.lblHomeEmployeeAttendanceThisMonth.setText("This Month : " + count);
                                }

                                ResultSet empSalary = DB.DB.getData("SELECT salary FROM employee WHERE username  = '" + txtEmployeeUsername.getText() + "'  ");
                                ResultSet empSalaryStatus = DB.DB.getData("SELECT status FROM payroll WHERE username = '" + txtEmployeeUsername.getText() + "' AND month = '" + month + "' AND year = '" + year + "' ");
                                if (empSalary.next()) {
                                    int j = empSalary.getInt(1);

                                    EmployeeDashboard.jLabel72.setText("Salary : " + j);
                                }
                                if (empSalaryStatus.next()) {
                                    String k = empSalaryStatus.getString(1);

                                    if (!"PAID".equals(k)) {
                                        EmployeeDashboard.jLabel68.setForeground(new Color(255, 0, 51));
                                    } else {
                                        EmployeeDashboard.jLabel68.setForeground(new Color(180, 180, 180));
                                    }
                                    EmployeeDashboard.jLabel68.setText("Status : " + k);
                                }

                            } catch (Exception e) {
                            }

                            ed.setVisible(true);

                            for (float j = 0f; j < 0.985; j += 0.000001) {
                                ed.setOpacity(j);
                            }

                        }
                    }, 1000 * 1);

                    Date DateMonth = new Date();
                    SimpleDateFormat toMonth = new SimpleDateFormat("MMMM");
                    month = "" + toMonth.format(DateMonth);

                    Date DateYear = new Date();
                    SimpleDateFormat toYear = new SimpleDateFormat("yyyy");
                    year = "" + toYear.format(DateYear);

                    Date DateDate = new Date();
                    SimpleDateFormat toDate = new SimpleDateFormat("yyyy-MM-dd");
                    date = "" + toDate.format(DateDate);

                    Date DateTime = new Date();
                    SimpleDateFormat toTime = new SimpleDateFormat("HH:mm:ss");
                    time = "" + toTime.format(DateTime);

                    Date DateToday = new Date();
                    SimpleDateFormat toToday = new SimpleDateFormat("yyyy-MM-dd");
                    today = "" + toToday.format(DateToday);

                    ResultSet at = DB.DB.getData("SELECT date FROM employee_attendance WHERE id =(SELECT id FROM employee WHERE username = '" + txtEmployeeUsername.getText() + "') AND  date='" + date + "' ");

                    if (!at.isBeforeFirst()) {
                        String employeeAttendance = "INSERT INTO employee_attendance (id, username, month, year, date, time) VALUES ((SELECT id FROM employee WHERE username = '" + txtEmployeeUsername.getText() + "'), '" + txtEmployeeUsername.getText() + "', '" + month + "', '" + year + "','" + today + "', '" + time + "');";

                        DB.DB.putData(employeeAttendance);
                    }

                    ResultSet payroll = DB.DB.getData("SELECT month FROM payroll WHERE id =(SELECT DISTINCT id FROM employee_attendance WHERE username = '" + txtEmployeeUsername.getText() + "' ) AND month = '" + month + "' AND year = '" + year + "'");
                    DB.DB.putData("UPDATE payroll SET attendance = (SELECT COUNT(id) FROM employee_attendance WHERE username = '" + txtEmployeeUsername.getText() + "' AND month = '" + month + "' AND year = '" + year + "') WHERE username = '" + txtEmployeeUsername.getText() + "' AND month = '" + month + "' AND year = '" + year + "'");

                    if (!payroll.isBeforeFirst()) {

                        String fillDataToPayroll = "INSERT INTO payroll(id, username, month, year, date, attendance, payment, status) VALUES ((SELECT id FROM employee WHERE username = '" + txtEmployeeUsername.getText() + "'),'" + txtEmployeeUsername.getText() + "','" + month + "','" + year + "','" + date + "',(SELECT COUNT(id) FROM employee_attendance WHERE username = '" + txtEmployeeUsername.getText() + "' AND month = '" + month + "' AND year = '" + year + "'),(SELECT salary FROM employee WHERE username = '" + txtEmployeeUsername.getText() + "'),'UNPAID')";
                        DB.DB.putData(fillDataToPayroll);
                    }

                } else {
                    String loginActivityFailedLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM employee WHERE username= '" + txtEmployeeUsername.getText() + "'),'Employee' ,'" + txtEmployeeUsername.getText() + "','Employee Login','" + txtEmployeeUsername.getText() + " login failed' , 'FAILED')";
                    DB.DB.putData(loginActivityFailedLog);

                    JOptionPane.showMessageDialog(this, "Access Denied", "Access Denied", JOptionPane.WARNING_MESSAGE); //invalid entry message

                    txtEmployeeUsername.setText(null);
                    pwdEmployeePassword.setText(null);
                    txtEmployeeUsername.grabFocus();

                }

            } catch (Exception ex) {
            }
        }

    }

    private void adminLogin() {
        String pwd = String.valueOf(pwdAdminPassword.getPassword());
        counter++;
        if (counter > 3) {
            JOptionPane.showMessageDialog(this, "Intruder Login Detected, System will be Exited!", "Access Denied", JOptionPane.ERROR_MESSAGE);

            String IntruderloginActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username= '" + txtAdminUsername.getText() + "'),'Admin' ,'" + txtAdminUsername.getText() + "','Intruder Login','" + txtAdminUsername.getText() + " has been detected as an Intruder' , 'FAILED')";
            try {
                DB.DB.putData(IntruderloginActivityLog);
            } catch (Exception ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.exit(0);

        } else {
            try {
                ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE type ='admin' AND account_status = 'Active' AND username = '" + txtAdminUsername.getText() + "' AND password =  '" + pwd + "' ");
                if (rs.next()) {
                    String loginActivitySuccessLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username= '" + txtAdminUsername.getText() + "'),'Admin','" + txtAdminUsername.getText() + "','Admin Login','" + txtAdminUsername.getText() + " logged in' , 'SUCCESS')";
                    DB.DB.putData(loginActivitySuccessLog);

                    pnlLogin.setVisible(false);
                    pnlLoginLoader.setVisible(true);
                    username.setText(txtAdminUsername.getText());

                    new java.util.Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {

                            for (float i = 0.985f; i > 0; i -= 0.000001) {
                                Login.this.setOpacity(i);

                            }
                            Login.this.dispose();

                            AdminDashboard ad = new AdminDashboard();

                            AdminDashboard.lblAdminUsername.setText(txtAdminUsername.getText());
                            ad.setVisible(true);

                            for (float j = 0f; j < 0.985; j += 0.000001) {
                                ad.setOpacity(j);
                            }

                        }
                    }, 1000 * 1);

                } else {
                    String loginActivityFailedLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username= '" + txtAdminUsername.getText() + "'),'Admin' ,'" + txtAdminUsername.getText() + "','Admin Login','" + txtAdminUsername.getText() + " login failed' , 'FAILED')";
                    DB.DB.putData(loginActivityFailedLog);
                    JOptionPane.showMessageDialog(this, "Access Denied", "Access Denied", JOptionPane.WARNING_MESSAGE); //invalid entry message

                    txtAdminUsername.setText(null);
                    pwdAdminPassword.setText(null);
                    txtAdminUsername.grabFocus();

                }

            } catch (Exception e) {
            }
        }
    }

    private void emergencyLogin() {
        String pwd = String.valueOf(pwdEmergencyPassword.getPassword());
        counter++;
        if (counter > 3) {
            JOptionPane.showMessageDialog(this, "Intruder Login Detected, System will be Exited!", "Access Denied", JOptionPane.ERROR_MESSAGE);

            String IntruderloginActivityLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username= '" + txtEmergencyUsername.getText() + "'),'Super User' ,'" + txtEmergencyUsername.getText() + "','Intruder Login','" + txtEmergencyUsername.getText() + " has been detected as an Intruder' , 'FAILED')";
            try {
                DB.DB.putData(IntruderloginActivityLog);
            } catch (Exception ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.exit(0);
        } else {
            try {

                ResultSet rs = DB.DB.getData("SELECT * FROM account WHERE type = 'super' || type = 'developer'  AND account_status = 'Active' AND (username = '" + txtEmergencyUsername.getText() + "' ) AND (password = '" + pwd + "') ");
                if (rs.next()) {
                    String loginActivitySuccessLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username= '" + txtEmergencyUsername.getText() + "'),'Super User','" + txtEmergencyUsername.getText() + "','Super User Login','" + txtEmergencyUsername.getText() + " logged in' , 'SUCCESS')";

                    DB.DB.putData(loginActivitySuccessLog);
                    pnlLogin.setVisible(false);
                    pnlLoginLoader.setVisible(true);
                    username.setText(txtEmergencyUsername.getText());

                    new java.util.Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            for (float i = 0.985f; i > 0; i -= 0.000001) {
                                Login.this.setOpacity(i);
                            }
                            Login.this.dispose();
                            EmergencyDashboard ed = new EmergencyDashboard();
                            EmergencyDashboard.lblSuperUserUsername.setText(txtEmergencyUsername.getText());
                            ed.setVisible(true);

                            for (float j = 0f; j < 0.985; j += 0.000001) {
                                ed.setOpacity(j);
                            }
                        }
                    }, 1000 * 1);

                } else {
                    String loginActivityFailedLog = "INSERT INTO log(id, user, username, activity, description, state) VALUES ((SELECT id FROM account WHERE username= '" + txtEmergencyUsername.getText() + "'),'Super User' ,'" + txtEmergencyUsername.getText() + "','Super User Login','" + txtEmergencyUsername.getText() + " login failed' , 'FAILED')";
                    DB.DB.putData(loginActivityFailedLog);
                    JOptionPane.showMessageDialog(this, "Access Denied", "Access Denied", JOptionPane.WARNING_MESSAGE); //invalid entry message

                    txtEmergencyUsername.setText(null);
                    pwdEmergencyPassword.setText(null);
                    txtEmergencyUsername.grabFocus();

                }

            } catch (Exception e) {
            }
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
        pnlLogin = new javax.swing.JPanel();
        btn_close = new javax.swing.JLabel();
        pnlLeft = new javax.swing.JPanel();
        userTypeImg = new javax.swing.JLabel();
        loginWish = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        accountType = new javax.swing.JLabel();
        employee = new javax.swing.JPanel();
        employeeSelection = new javax.swing.JLabel();
        admin = new javax.swing.JPanel();
        adminSelection = new javax.swing.JLabel();
        emergency = new javax.swing.JPanel();
        emergencySelection = new javax.swing.JLabel();
        mainCard = new javax.swing.JPanel();
        pnlEmployee = new javax.swing.JPanel();
        cardMember = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtEmployeeUsername = new javax.swing.JTextField();
        pwdEmployeePassword = new javax.swing.JPasswordField();
        pnlEmployeeLogin = new javax.swing.JPanel();
        btnEmployeeLogin = new javax.swing.JButton();
        pnlAdmin = new javax.swing.JPanel();
        cardAdmin = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtAdminUsername = new javax.swing.JTextField();
        pwdAdminPassword = new javax.swing.JPasswordField();
        pnlAdminLogin = new javax.swing.JPanel();
        btnAdminLogin = new javax.swing.JButton();
        pnlEmergency = new javax.swing.JPanel();
        cardEmergency = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtEmergencyUsername = new javax.swing.JTextField();
        pwdEmergencyPassword = new javax.swing.JPasswordField();
        pnlSystemLogin = new javax.swing.JPanel();
        btnSystemLogin = new javax.swing.JButton();
        pnlLoginLoader = new javax.swing.JPanel();
        pnlParent = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        wrelcome = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        username = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Xeon Inventory | Sign In");
        setUndecorated(true);
        setOpacity(0.985F);
        setResizable(false);

        pnlFrames.setLayout(new java.awt.CardLayout());

        pnlLogin.setBackground(new java.awt.Color(35, 35, 35));
        pnlLogin.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlLoginMouseDragged(evt);
            }
        });
        pnlLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlLoginMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlLoginMouseReleased(evt);
            }
        });
        pnlLogin.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btn_close.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_close.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/transparent_titlebar_close.png"))); // NOI18N
        btn_close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_closeMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn_closeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_closeMouseExited(evt);
            }
        });
        pnlLogin.add(btn_close, new org.netbeans.lib.awtextra.AbsoluteConstraints(854, 0, -1, -1));

        pnlLeft.setBackground(new java.awt.Color(0, 0, 0));
        pnlLeft.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlLeftMouseDragged(evt);
            }
        });
        pnlLeft.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlLeftMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                pnlLeftMouseReleased(evt);
            }
        });

        userTypeImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userTypeImg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/member.png"))); // NOI18N

        loginWish.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        loginWish.setForeground(new java.awt.Color(255, 255, 255));
        loginWish.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        loginWish.setText("Good morning");
        loginWish.setToolTipText("");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Log in to Xeon Inventory");

        accountType.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        accountType.setForeground(new java.awt.Color(255, 255, 255));
        accountType.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        accountType.setText("Employee");

        javax.swing.GroupLayout pnlLeftLayout = new javax.swing.GroupLayout(pnlLeft);
        pnlLeft.setLayout(pnlLeftLayout);
        pnlLeftLayout.setHorizontalGroup(
            pnlLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLeftLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(userTypeImg, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLeftLayout.createSequentialGroup()
                .addComponent(loginWish, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(accountType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlLeftLayout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlLeftLayout.setVerticalGroup(
            pnlLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLeftLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(userTypeImg)
                .addGap(29, 29, 29)
                .addGroup(pnlLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loginWish)
                    .addComponent(accountType))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        pnlLogin.add(pnlLeft, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 390, 550));

        employee.setBackground(new java.awt.Color(35, 35, 35));

        employeeSelection.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        employeeSelection.setForeground(new java.awt.Color(255, 255, 255));
        employeeSelection.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        employeeSelection.setText("Employee");
        employeeSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                employeeSelectionMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout employeeLayout = new javax.swing.GroupLayout(employee);
        employee.setLayout(employeeLayout);
        employeeLayout.setHorizontalGroup(
            employeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(employeeSelection, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
        );
        employeeLayout.setVerticalGroup(
            employeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(employeeSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pnlLogin.add(employee, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 0, 152, 32));

        admin.setBackground(new java.awt.Color(20, 20, 20));

        adminSelection.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        adminSelection.setForeground(new java.awt.Color(255, 255, 255));
        adminSelection.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        adminSelection.setText("Admin");
        adminSelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                adminSelectionMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout adminLayout = new javax.swing.GroupLayout(admin);
        admin.setLayout(adminLayout);
        adminLayout.setHorizontalGroup(
            adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(adminSelection, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                .addContainerGap())
        );
        adminLayout.setVerticalGroup(
            adminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(adminSelection, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
        );

        pnlLogin.add(admin, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 0, -1, -1));

        emergency.setBackground(new java.awt.Color(20, 20, 20));
        emergency.setPreferredSize(new java.awt.Dimension(152, 32));

        emergencySelection.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        emergencySelection.setForeground(new java.awt.Color(255, 255, 255));
        emergencySelection.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        emergencySelection.setText("Emergency");
        emergencySelection.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                emergencySelectionMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout emergencyLayout = new javax.swing.GroupLayout(emergency);
        emergency.setLayout(emergencyLayout);
        emergencyLayout.setHorizontalGroup(
            emergencyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(emergencySelection, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
        );
        emergencyLayout.setVerticalGroup(
            emergencyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(emergencySelection, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
        );

        pnlLogin.add(emergency, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 0, -1, -1));

        mainCard.setBackground(new java.awt.Color(35, 35, 35));
        mainCard.setLayout(new java.awt.CardLayout());

        pnlEmployee.setBackground(new java.awt.Color(35, 35, 35));

        cardMember.setBackground(new java.awt.Color(35, 35, 35));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(250, 250, 250));
        jLabel6.setText("Username");

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(250, 250, 250));
        jLabel9.setText("Password");

        txtEmployeeUsername.setBackground(new java.awt.Color(70, 70, 70));
        txtEmployeeUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtEmployeeUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmployeeUsernameActionPerformed(evt);
            }
        });

        pwdEmployeePassword.setBackground(new java.awt.Color(70, 70, 70));
        pwdEmployeePassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pwdEmployeePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pwdEmployeePasswordActionPerformed(evt);
            }
        });

        pnlEmployeeLogin.setBackground(new java.awt.Color(20, 20, 20));

        btnEmployeeLogin.setBackground(new java.awt.Color(20, 20, 20));
        btnEmployeeLogin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnEmployeeLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnEmployeeLogin.setText("Employee Login");
        btnEmployeeLogin.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));
        btnEmployeeLogin.setBorderPainted(false);
        btnEmployeeLogin.setContentAreaFilled(false);
        btnEmployeeLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEmployeeLogin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnEmployeeLogin.setPreferredSize(new java.awt.Dimension(50, 27));
        btnEmployeeLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEmployeeLoginMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEmployeeLoginMouseExited(evt);
            }
        });
        btnEmployeeLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmployeeLoginActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlEmployeeLoginLayout = new javax.swing.GroupLayout(pnlEmployeeLogin);
        pnlEmployeeLogin.setLayout(pnlEmployeeLoginLayout);
        pnlEmployeeLoginLayout.setHorizontalGroup(
            pnlEmployeeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnEmployeeLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlEmployeeLoginLayout.setVerticalGroup(
            pnlEmployeeLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlEmployeeLoginLayout.createSequentialGroup()
                .addComponent(btnEmployeeLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout cardMemberLayout = new javax.swing.GroupLayout(cardMember);
        cardMember.setLayout(cardMemberLayout);
        cardMemberLayout.setHorizontalGroup(
            cardMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardMemberLayout.createSequentialGroup()
                .addContainerGap(74, Short.MAX_VALUE)
                .addGroup(cardMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pwdEmployeePassword)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .addComponent(txtEmployeeUsername, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlEmployeeLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(66, 66, 66))
        );
        cardMemberLayout.setVerticalGroup(
            cardMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardMemberLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmployeeUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pwdEmployeePassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(pnlEmployeeLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(121, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlEmployeeLayout = new javax.swing.GroupLayout(pnlEmployee);
        pnlEmployee.setLayout(pnlEmployeeLayout);
        pnlEmployeeLayout.setHorizontalGroup(
            pnlEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmployeeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cardMember, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlEmployeeLayout.setVerticalGroup(
            pnlEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmployeeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cardMember, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainCard.add(pnlEmployee, "card2");

        pnlAdmin.setBackground(new java.awt.Color(35, 35, 35));

        cardAdmin.setBackground(new java.awt.Color(35, 35, 35));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(250, 250, 250));
        jLabel10.setText("Username");

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(250, 250, 250));
        jLabel11.setText("Password");

        txtAdminUsername.setBackground(new java.awt.Color(70, 70, 70));
        txtAdminUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtAdminUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAdminUsernameActionPerformed(evt);
            }
        });

        pwdAdminPassword.setBackground(new java.awt.Color(70, 70, 70));
        pwdAdminPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pwdAdminPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pwdAdminPasswordActionPerformed(evt);
            }
        });

        pnlAdminLogin.setBackground(new java.awt.Color(20, 20, 20));

        btnAdminLogin.setBackground(new java.awt.Color(20, 20, 20));
        btnAdminLogin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAdminLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnAdminLogin.setText("Admin Login");
        btnAdminLogin.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));
        btnAdminLogin.setBorderPainted(false);
        btnAdminLogin.setContentAreaFilled(false);
        btnAdminLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAdminLogin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAdminLogin.setPreferredSize(new java.awt.Dimension(50, 27));
        btnAdminLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAdminLoginMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAdminLoginMouseExited(evt);
            }
        });
        btnAdminLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdminLoginActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlAdminLoginLayout = new javax.swing.GroupLayout(pnlAdminLogin);
        pnlAdminLogin.setLayout(pnlAdminLoginLayout);
        pnlAdminLoginLayout.setHorizontalGroup(
            pnlAdminLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnAdminLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlAdminLoginLayout.setVerticalGroup(
            pnlAdminLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAdminLoginLayout.createSequentialGroup()
                .addComponent(btnAdminLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout cardAdminLayout = new javax.swing.GroupLayout(cardAdmin);
        cardAdmin.setLayout(cardAdminLayout);
        cardAdminLayout.setHorizontalGroup(
            cardAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardAdminLayout.createSequentialGroup()
                .addContainerGap(74, Short.MAX_VALUE)
                .addGroup(cardAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pwdAdminPassword)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .addComponent(txtAdminUsername, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlAdminLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(66, 66, 66))
        );
        cardAdminLayout.setVerticalGroup(
            cardAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardAdminLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAdminUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pwdAdminPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(pnlAdminLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(121, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlAdminLayout = new javax.swing.GroupLayout(pnlAdmin);
        pnlAdmin.setLayout(pnlAdminLayout);
        pnlAdminLayout.setHorizontalGroup(
            pnlAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdminLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cardAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlAdminLayout.setVerticalGroup(
            pnlAdminLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdminLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cardAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainCard.add(pnlAdmin, "card3");

        pnlEmergency.setBackground(new java.awt.Color(35, 35, 35));

        cardEmergency.setBackground(new java.awt.Color(35, 35, 35));

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(250, 250, 250));
        jLabel12.setText("Username");

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(250, 250, 250));
        jLabel13.setText("Password");

        txtEmergencyUsername.setBackground(new java.awt.Color(70, 70, 70));
        txtEmergencyUsername.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtEmergencyUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmergencyUsernameActionPerformed(evt);
            }
        });

        pwdEmergencyPassword.setBackground(new java.awt.Color(70, 70, 70));
        pwdEmergencyPassword.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pwdEmergencyPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pwdEmergencyPasswordActionPerformed(evt);
            }
        });

        pnlSystemLogin.setBackground(new java.awt.Color(20, 20, 20));

        btnSystemLogin.setBackground(new java.awt.Color(20, 20, 20));
        btnSystemLogin.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSystemLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnSystemLogin.setText("Emergency Login");
        btnSystemLogin.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));
        btnSystemLogin.setBorderPainted(false);
        btnSystemLogin.setContentAreaFilled(false);
        btnSystemLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnSystemLogin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSystemLogin.setPreferredSize(new java.awt.Dimension(50, 27));
        btnSystemLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSystemLoginMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSystemLoginMouseExited(evt);
            }
        });
        btnSystemLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSystemLoginActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSystemLoginLayout = new javax.swing.GroupLayout(pnlSystemLogin);
        pnlSystemLogin.setLayout(pnlSystemLoginLayout);
        pnlSystemLoginLayout.setHorizontalGroup(
            pnlSystemLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnSystemLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlSystemLoginLayout.setVerticalGroup(
            pnlSystemLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSystemLoginLayout.createSequentialGroup()
                .addComponent(btnSystemLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout cardEmergencyLayout = new javax.swing.GroupLayout(cardEmergency);
        cardEmergency.setLayout(cardEmergencyLayout);
        cardEmergencyLayout.setHorizontalGroup(
            cardEmergencyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cardEmergencyLayout.createSequentialGroup()
                .addContainerGap(74, Short.MAX_VALUE)
                .addGroup(cardEmergencyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pwdEmergencyPassword)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .addComponent(txtEmergencyUsername, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlSystemLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(66, 66, 66))
        );
        cardEmergencyLayout.setVerticalGroup(
            cardEmergencyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cardEmergencyLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEmergencyUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pwdEmergencyPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(pnlSystemLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(121, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlEmergencyLayout = new javax.swing.GroupLayout(pnlEmergency);
        pnlEmergency.setLayout(pnlEmergencyLayout);
        pnlEmergencyLayout.setHorizontalGroup(
            pnlEmergencyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmergencyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cardEmergency, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlEmergencyLayout.setVerticalGroup(
            pnlEmergencyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEmergencyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cardEmergency, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainCard.add(pnlEmergency, "card4");

        pnlLogin.add(mainCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 38, 504, 510));

        pnlFrames.add(pnlLogin, "card2");

        pnlParent.setBackground(new java.awt.Color(35, 35, 35));
        pnlParent.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                pnlParentMouseDragged(evt);
            }
        });
        pnlParent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnlParentMousePressed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/loading.gif"))); // NOI18N

        wrelcome.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        wrelcome.setForeground(new java.awt.Color(255, 255, 255));
        wrelcome.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        wrelcome.setText("Welcome");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/frames/img/user_welcome.png"))); // NOI18N

        username.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        username.setForeground(new java.awt.Color(255, 255, 255));
        username.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        username.setText("User");

        javax.swing.GroupLayout pnlParentLayout = new javax.swing.GroupLayout(pnlParent);
        pnlParent.setLayout(pnlParentLayout);
        pnlParentLayout.setHorizontalGroup(
            pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
            .addGroup(pnlParentLayout.createSequentialGroup()
                .addComponent(wrelcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(username, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlParentLayout.setVerticalGroup(
            pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlParentLayout.createSequentialGroup()
                .addContainerGap(64, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(35, 35, 35)
                .addGroup(pnlParentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wrelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(66, 66, 66))
        );

        javax.swing.GroupLayout pnlLoginLoaderLayout = new javax.swing.GroupLayout(pnlLoginLoader);
        pnlLoginLoader.setLayout(pnlLoginLoaderLayout);
        pnlLoginLoaderLayout.setHorizontalGroup(
            pnlLoginLoaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlParent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlLoginLoaderLayout.setVerticalGroup(
            pnlLoginLoaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlParent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlFrames.add(pnlLoginLoader, "card3");

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

        setSize(new java.awt.Dimension(900, 547));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_closeMouseClicked
        for (float i = 1f; i > 0; i -= 0.000001) {
            Login.this.setOpacity(i);
        }
        System.exit(0);
    }//GEN-LAST:event_btn_closeMouseClicked

    private void btn_closeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_closeMouseEntered
        ImageIcon ii = new ImageIcon(getClass().getResource("img/titlebar_close_hover.png"));
        btn_close.setIcon(ii);
    }//GEN-LAST:event_btn_closeMouseEntered

    private void btn_closeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_closeMouseExited
        ImageIcon ii = new ImageIcon(getClass().getResource("img/transparent_titlebar_close.png"));
        btn_close.setIcon(ii);
    }//GEN-LAST:event_btn_closeMouseExited

    private void pnlLeftMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLeftMousePressed
        mousepX = evt.getX();
        mousepY = evt.getY();
    }//GEN-LAST:event_pnlLeftMousePressed

    private void pnlLeftMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLeftMouseDragged
        int locX = evt.getXOnScreen();
        int locY = evt.getYOnScreen();

        this.setLocation(locX - mousepX, locY - mousepY);
        setOpacity((float) (0.97));
    }//GEN-LAST:event_pnlLeftMouseDragged

    private void pnlLoginMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLoginMousePressed
        mousepX = evt.getX();
        mousepY = evt.getY();
    }//GEN-LAST:event_pnlLoginMousePressed

    private void pnlLoginMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLoginMouseDragged
        int locX = evt.getXOnScreen();
        int locY = evt.getYOnScreen();

        this.setLocation(locX - mousepX, locY - mousepY);
        setOpacity((float) (0.97));
    }//GEN-LAST:event_pnlLoginMouseDragged

    private void employeeSelectionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeeSelectionMouseClicked
        ImageIcon ii = new ImageIcon(getClass().getResource("img/member.png"));
        userTypeImg.setIcon(ii);

        setPnlColor(employee);
        resetPnlColor(admin);
        resetPnlColor(emergency);

        pnlEmployee.setVisible(true);
        pnlAdmin.setVisible(false);
        pnlEmergency.setVisible(false);

        accountType.setText("Employee");
        txtEmployeeUsername.grabFocus();
    }//GEN-LAST:event_employeeSelectionMouseClicked

    private void adminSelectionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_adminSelectionMouseClicked
        ImageIcon ii = new ImageIcon(getClass().getResource("img/admin.png"));
        userTypeImg.setIcon(ii);

        resetPnlColor(employee);
        setPnlColor(admin);
        resetPnlColor(emergency);

        pnlEmployee.setVisible(false);
        pnlAdmin.setVisible(true);
        pnlEmergency.setVisible(false);

        accountType.setText("Admin");
        txtAdminUsername.grabFocus();
    }//GEN-LAST:event_adminSelectionMouseClicked

    private void emergencySelectionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emergencySelectionMouseClicked
        ImageIcon ii = new ImageIcon(getClass().getResource("img/system.png"));
        userTypeImg.setIcon(ii);

        resetPnlColor(employee);
        resetPnlColor(admin);
        setPnlColor(emergency);

        pnlEmployee.setVisible(false);
        pnlAdmin.setVisible(false);
        pnlEmergency.setVisible(true);

        accountType.setText("Super User");
        txtEmergencyUsername.grabFocus();
    }//GEN-LAST:event_emergencySelectionMouseClicked

    private void btnEmployeeLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmployeeLoginActionPerformed
        employeeLogin();
    }//GEN-LAST:event_btnEmployeeLoginActionPerformed

    private void btnAdminLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdminLoginActionPerformed
        adminLogin();

    }//GEN-LAST:event_btnAdminLoginActionPerformed

    private void btnSystemLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSystemLoginActionPerformed
        emergencyLogin();
    }//GEN-LAST:event_btnSystemLoginActionPerformed

    private void pnlParentMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlParentMouseDragged
        int locX = evt.getXOnScreen();
        int locY = evt.getYOnScreen();

        this.setLocation(locX - mousepX, locY - mousepY);
    }//GEN-LAST:event_pnlParentMouseDragged

    private void pnlParentMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlParentMousePressed
        mousepX = evt.getX();
        mousepY = evt.getY();
    }//GEN-LAST:event_pnlParentMousePressed

    private void txtEmployeeUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeeUsernameActionPerformed
        pwdEmployeePassword.grabFocus();
    }//GEN-LAST:event_txtEmployeeUsernameActionPerformed

    private void txtAdminUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAdminUsernameActionPerformed
        pwdAdminPassword.grabFocus();
    }//GEN-LAST:event_txtAdminUsernameActionPerformed

    private void txtEmergencyUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmergencyUsernameActionPerformed
        pwdEmergencyPassword.grabFocus();
    }//GEN-LAST:event_txtEmergencyUsernameActionPerformed

    private void pwdAdminPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwdAdminPasswordActionPerformed
        adminLogin();
    }//GEN-LAST:event_pwdAdminPasswordActionPerformed

    private void pwdEmployeePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwdEmployeePasswordActionPerformed
        employeeLogin();
    }//GEN-LAST:event_pwdEmployeePasswordActionPerformed

    private void pwdEmergencyPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pwdEmergencyPasswordActionPerformed
        emergencyLogin();
    }//GEN-LAST:event_pwdEmergencyPasswordActionPerformed

    private void btnEmployeeLoginMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEmployeeLoginMouseEntered
        pnlEmployeeLogin.setBackground(new Color(70, 70, 70));
    }//GEN-LAST:event_btnEmployeeLoginMouseEntered

    private void btnEmployeeLoginMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEmployeeLoginMouseExited
        pnlEmployeeLogin.setBackground(new Color(20, 20, 20));
    }//GEN-LAST:event_btnEmployeeLoginMouseExited

    private void btnAdminLoginMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAdminLoginMouseEntered
        pnlAdminLogin.setBackground(new Color(70, 70, 70));
    }//GEN-LAST:event_btnAdminLoginMouseEntered

    private void btnAdminLoginMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAdminLoginMouseExited
        pnlAdminLogin.setBackground(new Color(20, 20, 20));
    }//GEN-LAST:event_btnAdminLoginMouseExited

    private void btnSystemLoginMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSystemLoginMouseEntered
        pnlSystemLogin.setBackground(new Color(70, 70, 70));
    }//GEN-LAST:event_btnSystemLoginMouseEntered

    private void btnSystemLoginMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSystemLoginMouseExited
        pnlSystemLogin.setBackground(new Color(20, 20, 20));
    }//GEN-LAST:event_btnSystemLoginMouseExited

    private void pnlLeftMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLeftMouseReleased
        setOpacity((float) (0.985));
    }//GEN-LAST:event_pnlLeftMouseReleased

    private void pnlLoginMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlLoginMouseReleased
        setOpacity((float) (0.985));
    }//GEN-LAST:event_pnlLoginMouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println(ex + "Failed to initialize LaF");
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/xeon_icon.png")));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accountType;
    private javax.swing.JPanel admin;
    private javax.swing.JLabel adminSelection;
    private javax.swing.JButton btnAdminLogin;
    private javax.swing.JButton btnEmployeeLogin;
    private javax.swing.JButton btnSystemLogin;
    private javax.swing.JLabel btn_close;
    private javax.swing.JPanel cardAdmin;
    private javax.swing.JPanel cardEmergency;
    private javax.swing.JPanel cardMember;
    private javax.swing.JPanel emergency;
    private javax.swing.JLabel emergencySelection;
    private javax.swing.JPanel employee;
    private javax.swing.JLabel employeeSelection;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel loginWish;
    private javax.swing.JPanel mainCard;
    private javax.swing.JPanel pnlAdmin;
    private javax.swing.JPanel pnlAdminLogin;
    private javax.swing.JPanel pnlEmergency;
    private javax.swing.JPanel pnlEmployee;
    private javax.swing.JPanel pnlEmployeeLogin;
    private javax.swing.JPanel pnlFrames;
    private javax.swing.JPanel pnlLeft;
    private javax.swing.JPanel pnlLogin;
    private javax.swing.JPanel pnlLoginLoader;
    private javax.swing.JPanel pnlParent;
    private javax.swing.JPanel pnlSystemLogin;
    private javax.swing.JPasswordField pwdAdminPassword;
    private javax.swing.JPasswordField pwdEmergencyPassword;
    private javax.swing.JPasswordField pwdEmployeePassword;
    private javax.swing.JTextField txtAdminUsername;
    private javax.swing.JTextField txtEmergencyUsername;
    private javax.swing.JTextField txtEmployeeUsername;
    private javax.swing.JLabel userTypeImg;
    private javax.swing.JLabel username;
    private javax.swing.JLabel wrelcome;
    // End of variables declaration//GEN-END:variables
}
