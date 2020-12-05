package test_dep;

import java.awt.*;
import java.awt.event.*;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.*;

public class AddDialog extends JDialog {
    private JTextField tfDrug;
    private JTextField userID;
    private JTextField tfDesc;
    private JLabel lbName;
    private JLabel lbUserID;
    private JLabel lbDesc;
    private JButton btnCommit;
    private JButton btnCancel;
    private JButton btnDelete;
    private boolean succeeded;
 
    public AddDialog(Frame parent, int lastLoggedInAs) {
        super(parent, "Add/Delete Medications", true);
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
        
        lbUserID = new JLabel("User to Add/Delete Drugs for: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbUserID, cs);
 
        lbName = new JLabel("Drug Name: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbName, cs);
        
        userID = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(userID, cs);
        
        tfDrug = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(tfDrug, cs);
 
        lbDesc = new JLabel("Drug Desc: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbDesc, cs);
 
        tfDesc = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(tfDesc, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        btnCommit = new JButton("Insert");
 
        btnCommit.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
            	try {
            		Statement statement = null;
                    String queryDrop = "DROP PROCEDURE IF EXISTS addMedications";
                    Statement stmtDrop = App.conn.createStatement();
                    stmtDrop.execute(queryDrop);
                    String createInParameterProcedure = "CREATE PROCEDURE addMedications(\r\n"
                    		+ "IN uID_in int,\r\n"
                    		+ "IN genericDrugName_in varchar(50),\r\n"
                    		+ "IN description_in varchar(50) )\r\n"
                    		+ "BEGIN\r\n"
                    		+ "	INSERT INTO Medications(uID, genericDrugName, description) VALUES (uID_in, genericDrugName_in, description_in);\r\n"
                    		+ "END;\r\n"
                    		+ "";
                    statement = App.conn.createStatement();
                    statement.executeUpdate(createInParameterProcedure);
                    CallableStatement cs = App.conn.prepareCall("{CALL addMedications(?, ?, ?)}");
                    cs.setString(1, getUserID());
                    cs.setString(2, getDrugName());
                    cs.setString(3, getDrugDesc());
                    int i = cs.executeUpdate();
                    if (i > 0) {
                    	succeeded = true;
                        JOptionPane.showMessageDialog(AddDialog.this,
                                "Successfully updated",
                                "Add Meds", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                    else   JOptionPane.showMessageDialog(AddDialog.this,
                            "User does not exist or invalid drug/description",
                            "Add Meds",
                            JOptionPane.ERROR_MESSAGE);
                	
                    
            		
            	} 
            	catch (SQLException se) {
            		JOptionPane.showMessageDialog(AddDialog.this,
                            "User does not exist",
                            "Add Meds",
                            JOptionPane.ERROR_MESSAGE);
            		se.printStackTrace();
            	}
            }
        });
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(new ActionListener() {
        	 
            public void actionPerformed(ActionEvent e) {
            	try {
            		Statement statement = null;
                    String queryDrop = "DROP PROCEDURE IF EXISTS deleteMedications";
                    Statement stmtDrop = App.conn.createStatement();
                    stmtDrop.execute(queryDrop);
                    String createInParameterProcedure = "CREATE PROCEDURE deleteMedications(\r\n"
                    		+ "IN uID_in int,\r\n"
                    		+ "IN genericDrugName_in varchar(50) )\r\n"
                    		+ "BEGIN\r\n"
                    		+ "	DELETE FROM Medications WHERE uID = uID_in and genericDrugName = genericDrugName_in;\r\n"
                    		+ "END;\r\n"
                    		+ "";
                    statement = App.conn.createStatement();
                    statement.executeUpdate(createInParameterProcedure);
                    CallableStatement cs = App.conn.prepareCall("{CALL deleteMedications(?, ?)}");
                    cs.setString(1, getUserID());
                    cs.setString(2, getDrugName());
                    int i = cs.executeUpdate();
                    if (i > 0) {
                    	succeeded = true;
                        JOptionPane.showMessageDialog(AddDialog.this,
                                "Successfully deleted",
                                "Delete Meds", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                    else   JOptionPane.showMessageDialog(AddDialog.this,
                            "Invalid existing drug name or no medications are currrent prescribed to you",
                            "Delete Meds",
                            JOptionPane.ERROR_MESSAGE);
                	
                    
            		
            	} 
            	catch (SQLException se) {
            		se.printStackTrace();
            	}
            }
        });
        
        JPanel bp = new JPanel();
        bp.add(btnCommit);
        bp.add(btnCancel);
        bp.add(btnDelete);
        
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
 
    public String getDrugName() {
        return tfDrug.getText().trim();
    }
    
    public String getUserID() {
        return userID.getText().trim();
    }
    
    public String getDrugDesc() {
        return new String(tfDesc.getText());
    }
 
    public boolean isSucceeded() {
        return succeeded;
    }
}
