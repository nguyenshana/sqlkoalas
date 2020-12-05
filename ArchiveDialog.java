package test_dep;

import java.awt.*;
import java.awt.event.*;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.*;

public class ArchiveDialog extends JDialog {
    private JTextField tfTimestamp;
    private JLabel lbTimestamp;
    private JButton btnArchive;
    private JButton btnCancel;
    private JButton btnClearArchive;
    private boolean succeeded;
 
    public ArchiveDialog(Frame parent) {
        super(parent, "Archive Accounts", true);
        //
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
 
        cs.fill = GridBagConstraints.HORIZONTAL;
 
        lbTimestamp = new JLabel("Timestamp: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(lbTimestamp, cs);
 
        tfTimestamp = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(tfTimestamp, cs);
 
        btnArchive = new JButton("Archive");
 
        btnArchive.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
        		try {
                    CallableStatement cs = App.conn.prepareCall("{CALL archiveOld(?)}");
                    cs.setString(1, getTime());
                    int i = cs.executeUpdate();
                    if (i > 0) {
                        JOptionPane.showMessageDialog(ArchiveDialog.this,
                                "Successfully updated",
                                "What", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else   JOptionPane.showMessageDialog(ArchiveDialog.this,
                            "No users last updated before specified time",
                            "Archive",
                            JOptionPane.ERROR_MESSAGE);
                	
                    
            		
            	
					
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
        
        btnClearArchive = new JButton("Clear Archive");
        btnClearArchive.addActionListener(new ActionListener() {
        	 
            public void actionPerformed(ActionEvent e) {
            	try {
            		Statement statement = null;
                    String queryDrop = "DROP PROCEDURE IF EXISTS deleteArchive";
                    Statement stmtDrop = App.conn.createStatement();
                    stmtDrop.execute(queryDrop);
                    String createInParameterProcedure = "CREATE PROCEDURE deleteArchive()\r\n"
                    		+ "BEGIN\r\n"
                    		+ "	DELETE FROM Archive;\r\n"
                    		+ "END \r\n"
                    		+ "";
                    statement = App.conn.createStatement();
                    statement.executeUpdate(createInParameterProcedure);
                    CallableStatement cs = App.conn.prepareCall("{CALL deleteArchive()}");
                    int i = cs.executeUpdate();
                    if (i > 0) {
                    	succeeded = true;
                        JOptionPane.showMessageDialog(ArchiveDialog.this,
                                "Successfully emptied archive.",
                                "Archive", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                    else JOptionPane.showMessageDialog(ArchiveDialog.this,
                            "Archive already empty!",
                            "Archive",
                            JOptionPane.ERROR_MESSAGE);
                 }
            	
            	catch (SQLException se) {
            		se.printStackTrace();
            	}
        }
        });
        
        JPanel bp = new JPanel();
        bp.add(btnArchive);
        bp.add(btnCancel);
        bp.add(btnClearArchive);
 
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
    
    public String getTime() {
        return tfTimestamp.getText().trim();
    }
   
    public boolean isSucceeded() {
        return succeeded;
    }
}
