package test_dep;

import java.awt.*;
import java.awt.event.*;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.*;

public class EditDialog extends JDialog {
    private JTextField tfDrug;
    private JTextField tfOldDrug;
    private JPasswordField tfDesc;
    private JLabel lbName;
    private JLabel lbOldName;
    private JLabel lbDesc;
    private JButton btnCommit;
    private JButton btnCancel;
    private boolean succeeded;
 
    public EditDialog(Frame parent, int lastLoggedInAs) {
        super(parent, "Edit Medications", true);
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
        
        lbOldName = new JLabel("Old Drug Name: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbOldName, cs);
 
        lbName = new JLabel("New Drug Name: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(lbName, cs);
        
        tfOldDrug = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfOldDrug, cs);
        
        tfDrug = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(tfDrug, cs);
 
        lbDesc = new JLabel("New Drug Desc: ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        panel.add(lbDesc, cs);
 
        tfDesc = new JPasswordField(20);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 2;
        panel.add(tfDesc, cs);
        panel.setBorder(new LineBorder(Color.GRAY));
 
        btnCommit = new JButton("Commit");
 
        btnCommit.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
            	try {
            		Statement statement = null;
                    String queryDrop = "DROP PROCEDURE IF EXISTS editMedications";
                    Statement stmtDrop = App.conn.createStatement();
                    stmtDrop.execute(queryDrop);
                    String createInParameterProcedure = "CREATE PROCEDURE editMedications(\r\n"
                    		+ "IN uID_in int,\r\n"
                    		+ "IN oldDrug_in varchar(50),\r\n"
                    		+ "IN genericDrugName_in varchar(50),\r\n"
                    		+ "IN description_in varchar(50) )\r\n"
                    		+ "\r\n"
                    		+ "BEGIN\r\n"
                    		+ "	UPDATE Medications\r\n"
                    		+ "		SET genericDrugName = genericDrugName_in,\r\n"
                    		+ "            description = description_in\r\n"
                    		+ "	WHERE uID = uID_in and genericDrugName = oldDrug_in;\r\n"
                    		+ "END;\r\n"
                    		+ "";
                    statement = App.conn.createStatement();
                    statement.executeUpdate(createInParameterProcedure);
                    CallableStatement cs = App.conn.prepareCall("{CALL editMedications(?, ?, ?, ?)}");
                    cs.setInt(1, lastLoggedInAs);
                    cs.setString(2, getOldDrug());
                    cs.setString(3, getDrugName());
                    cs.setString(4, getDrugDesc());
                    int i = cs.executeUpdate();
                    if (i > 0) {
                    	succeeded = true;
                        JOptionPane.showMessageDialog(EditDialog.this,
                                "Successfully updated",
                                "Edit Medications", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                    else   JOptionPane.showMessageDialog(EditDialog.this,
                            "Invalid existing drug name or no medications are currrent prescribed to you",
                            "Edit Medications",
                            JOptionPane.ERROR_MESSAGE);
                	
                    
            		
            	} 
            	catch (SQLException se) {
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
        JPanel bp = new JPanel();
        bp.add(btnCommit);
        bp.add(btnCancel);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
 
    public String getDrugName() {
        return tfDrug.getText().trim();
    }
    
    public String getOldDrug() {
        return tfOldDrug.getText().trim();
    }
    
    public String getDrugDesc() {
        return new String(tfDesc.getPassword());
    }
 
    public boolean isSucceeded() {
        return succeeded;
    }
}
