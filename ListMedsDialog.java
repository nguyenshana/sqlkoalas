package test_dep;

import java.awt.*;
import java.awt.event.*;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

public class ListMedsDialog extends JDialog {
    private JTextField tfMed;
    private JLabel lbMed;
    private JButton btnShow;
    private JButton btnCancel;
    private JButton btnPopularMeds;
    
    public ListMedsDialog(Frame parent) {
        super(parent, "Medication List", true);
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbMed = new JLabel("Medication: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbMed, cs);
 
        tfMed = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfMed, cs);
 
        btnShow = new JButton("Show");
 
        btnShow.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
        		try {
        			Statement statement = null;
                    String queryDrop = "DROP PROCEDURE IF EXISTS getUsersTakingMed";
                    Statement stmtDrop = App.conn.createStatement();
                    stmtDrop.execute(queryDrop);
                    String createInParameterProcedure = "CREATE PROCEDURE getUsersTakingMed(\r\n"
                    		+ "IN genericDrugName_in VARCHAR(50) )\r\n"
                    		+ "BEGIN\r\n"
                    		+ "	SELECT * \r\n"
                    		+ "    FROM (SELECT uID, genericDrugName FROM Demographics D NATURAL JOIN Medications M) A1\r\n"
                    		+ "    WHERE A1.genericDrugName = genericDrugName_in;\r\n"
                    		+ "END\r\n"
                    		+ "";
                    statement = App.conn.createStatement();
                    statement.executeUpdate(createInParameterProcedure);
                    CallableStatement cs = App.conn.prepareCall("{CALL getUsersTakingMed(?)}");
                    cs.setString(1, getMed());
                    ResultSet rs = cs.executeQuery();
                    ArrayList<Integer> listOfIds = new ArrayList<>();
                    while (rs.next()) {
                        listOfIds.add(rs.getInt("uID")); 
                    }
                    String str = listOfIds.toString();
                    JOptionPane.showMessageDialog(ListMedsDialog.this,
                            "Here's the list: " + str,
                            "List of uIDs",
                            JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
            }
        });
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        btnPopularMeds = new JButton("List Common Meds");
        btnPopularMeds.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        		try {
        			Statement statement = null;
                    String query = "SELECT distinct genericDrugName\r\n"
                    		+ "FROM Medications M\r\n"
                    		+ "WHERE uID != ANY (SELECT uID FROM Medications where genericDrugName = M.genericDrugName);\r\n"
                    		+ "";
                    statement = App.conn.createStatement();
                    ResultSet rs = statement.executeQuery(query);
                    ArrayList<String> listOfMeds= new ArrayList<>();
                    while (rs.next()) {
                        listOfMeds.add(rs.getString("genericDrugName")); 
                    }
                    String str = listOfMeds.toString();
                    JOptionPane.showMessageDialog(ListMedsDialog.this,
                            "Here's the list: " + str,
                            "Popular Meds",
                            JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
            }
        });
        
        JPanel bp = new JPanel();
        bp.add(btnShow);
        bp.add(btnCancel);
        bp.add(btnPopularMeds);
        
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
    
    public String getMed() {
        return tfMed.getText().trim();
    }
   
}
