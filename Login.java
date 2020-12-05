package test_dep;

import java.sql.*;

public class Login {
	private static Statement statement = null;
	
    public static boolean createAccount(String username, String password) throws SQLException {
        // hardcoded username and password
    	/*
        if (username.equals("bob") && password.equals("secret")) {
            return true;
        }
        return false;
        */

        String queryDrop = "DROP PROCEDURE IF EXISTS createAccount";
        Statement stmtDrop = App.conn.createStatement();
        stmtDrop.execute(queryDrop);
        String createInParameterProcedure = "CREATE PROCEDURE createAccount(\r\n"
        		+ "IN uID_in int,\r\n"
        		+ "IN password_in varchar(50))\r\n"
        		+ "\r\n"
        		+ "BEGIN\r\n"
        		+ "	IF (SELECT uID FROM Accounts WHERE uID = uID_in and password = 'password') THEN\r\n"
        		+ "		UPDATE Accounts SET password = password_in WHERE uID = uID_in;\r\n"
        		+ "    END IF;\r\n"
        		+ "END\r\n"
        		+ "";
        //System.out.println(createInParameterProcedure);
        statement = App.conn.createStatement();
        statement.executeUpdate(createInParameterProcedure);
        
        // Call procedures
        
        CallableStatement cs = App.conn.prepareCall("{CALL createAccount(?, ?)}");
        cs.setString(1, username);
        cs.setString(2, password);
        int i = cs.executeUpdate();
        if (i > 0) {
        	return true;
        }
        else return false;

    }
    
    public static boolean authenticate(String username, String password) throws SQLException {
        // hardcoded username and password
    	/*
        if (username.equals("bob") && password.equals("secret")) {
            return true;
        }
        return false;
        */
        String queryDrop = "DROP PROCEDURE IF EXISTS signIn";
        Statement stmtDrop = App.conn.createStatement();
        stmtDrop.execute(queryDrop);
        String createInParameterProcedure = "CREATE PROCEDURE signIn(\r\n"
        		+ "IN uID_in int,\r\n"
        		+ "IN password_in varchar(50))\r\n"
        		+ "\r\n"
        		+ "BEGIN\r\n"
        		+ "	SELECT * FROM Accounts WHERE uID = uID_in and password = password_in;	\r\n"
        		+ "END\r\n"
        		+ "";
        //System.out.println(createInParameterProcedure);
        statement = App.conn.createStatement();
        statement.executeUpdate(createInParameterProcedure);
        
        // Call procedures
        
        CallableStatement cs = App.conn.prepareCall("{CALL signIn(?, ?)}");
        cs.setString(1, username);
        cs.setString(2, password);
        ResultSet rs = cs.executeQuery();
        if (rs.next()) return true;
        else return false;
        //printResultSet(rs);
    }
    
    private static void printResultSet(ResultSet rs) throws SQLException
    {
       while(rs.next())
       {
          int id = rs.getInt("uID"); 
          String pass = rs.getString("password"); 
          System.out.println("uID:" + id + "password:" + pass); 
       }
    }
}
