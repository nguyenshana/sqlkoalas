package test_dep;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.jdbc.JDBCCategoryDataset;
import org.jfree.data.jdbc.JDBCPieDataset;
import org.jfree.data.jdbc.JDBCXYDataset;

import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App extends JFrame {
	
	public static final int WIDTH = 1280;
	public static final int HEIGHT = WIDTH/16 * 9; //720
	private static Connection conn = null;
	
	public App() {
		super("Healthy Koalas");
		setSize(WIDTH, HEIGHT);
		setLayout(null);
		setVisible(true);
		JLabel l1 = new JLabel("Welcome to Healthy Koalas. Choose a statistic to graph.");
		l1.setBounds(WIDTH/3, 0, WIDTH, 100);
		add(l1);
		JButton b = new JButton("Graph 1");
		b.setBounds(50, 100, 200, 50);
		add(b);
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        //SamplePieChart demo = new SamplePieChart("Comparison", "Which operating system are you using?");
		        //demo.pack();
		        //demo.setVisible(true);
				Statement stmt = null;
				//ResultSet rs = null;		
				JDBCPieDataset jds = createPieDataset("select age_range, total from (\r\n"
						+ "select \r\n"
						+ " CASE\r\n"
						+ "    WHEN age < 20 THEN 1\r\n"
						+ "    WHEN age BETWEEN 20 and 29 THEN 2\r\n"
						+ "    WHEN age BETWEEN 30 and 39 THEN 3\r\n"
						+ "    WHEN age BETWEEN 40 and 49 THEN 4\r\n"
						+ "    WHEN age BETWEEN 50 and 59 THEN 5\r\n"
						+ "    WHEN age BETWEEN 60 and 69 THEN 6\r\n"
						+ "    WHEN age BETWEEN 70 and 79 THEN 7\r\n"
						+ "    WHEN age >= 80 THEN 8\r\n"
						+ "    WHEN age IS NULL THEN 9\r\n"
						+ "END as ordinal,\r\n"
						+ "\r\n"
						+ "case\r\n"
						+ "	WHEN age < 20 THEN 'Under 20'\r\n"
						+ "    WHEN age BETWEEN 20 and 29 THEN '20 - 29'\r\n"
						+ "    WHEN age BETWEEN 30 and 39 THEN '30 - 39'\r\n"
						+ "    WHEN age BETWEEN 40 and 49 THEN '40 - 49'\r\n"
						+ "    WHEN age BETWEEN 50 and 59 THEN '50 - 59'\r\n"
						+ "    WHEN age BETWEEN 60 and 69 THEN '60 - 69'\r\n"
						+ "    WHEN age BETWEEN 70 and 79 THEN '70 - 79'\r\n"
						+ "    WHEN age >= 80 THEN 'Over 80'\r\n"
						+ "    WHEN age IS NULL THEN 'N/A'\r\n"
						+ "    \r\n"
						+ "end as age_range,\r\n"
						+ "\r\n"
						+ "COUNT(*) AS total\r\n"
						+ "\r\n"
						+ "\r\n"
						+ "from Demographics\r\n"
						+ "group by age_range\r\n"
						+ "order by ordinal) as subquery");
				
				// age distribution
				
				JFreeChart chart = ChartFactory.createPieChart("Age distribution", jds, true, true, false);
				//XYPlot plot = chart.getXYPlot();
				//plot.setDomainAxis(new DateAxis("Date"));
				ChartFrame res = new ChartFrame("Graph 1", chart);
				res.setSize(WIDTH, HEIGHT);
				res.setVisible(true);
			}
		});
		// Use this to test queries!!
		JButton b2 = new JButton("Graph 2");
		b2.setBounds(300, 100, 200, 50);
		add(b2);
		b2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//try {
					/*
					int id = 101;
					String query = "select * from Movie, Rating where Movie.mID = Rating.mID and Movie.mID = " + id;
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(query);
					while(rs.next()){
							String result = "Movie =" + rs.getString("title") + ", Stars" + rs.getInt("stars");
				          System.out.println(result);
				     }
				     */
					JDBCPieDataset jds = createPieDataset("select\r\n"
							+ "case\r\n"
							+ "	when gender = 1 then \"Male\"\r\n"
							+ "    when gender = 2 then \"Female\"\r\n"
							+ "end as sex,\r\n"
							+ "\r\n"
							+ "count(genericDrugName) as total_drug_used\r\n"
							+ " \r\n"
							+ "from Demographics D\r\n"
							+ "inner join Medications M ON D.uID = M.uID\r\n"
							+ "group by gender");
					JFreeChart chart = ChartFactory.createPieChart("Male vs. female ratio in terms of medications taken", jds, true, true, false);
					ChartFrame res = new ChartFrame("Graph 2", chart);
					res.setSize(WIDTH, HEIGHT);
					res.setVisible(true);
					
				//}
				/*
				catch (SQLException se) {
					se.printStackTrace();
				}
				catch (Exception e2) {
					e2.printStackTrace();
				}
				*/
			}
		});
		
		JButton b3 = new JButton("Graph 3");
		b3.setBounds(550, 100, 200, 50);
		add(b3);
		b3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					JDBCXYDataset jds = createXYDataset("SELECT totalFat, bloodPressure\r\n"
							+ "FROM Diet, Examination \r\n"
							+ "WHERE Diet.uID = Examination.uID and bloodPressure is not null and totalFat is not null");
					JFreeChart chart = ChartFactory.createScatterPlot("Total fat intake vs. blood pressure",
					           "totalFat", "bloodPressure", jds);
					ChartFrame res = new ChartFrame("Graph 3", chart);
					res.setSize(WIDTH, HEIGHT);
					res.setVisible(true);
					
				//}
				/*
				catch (SQLException se) {
					se.printStackTrace();
				}
				catch (Exception e2) {
					e2.printStackTrace();
				}
				*/
			}
		});
	}
	
	private static JDBCXYDataset createXYDataset(String query) {
		try {
			JDBCXYDataset jds = new JDBCXYDataset(conn);
			jds.executeQuery(query);
			return jds;
		}
		catch (SQLException se){
			se.printStackTrace();
		}
		return null;
	}
	
	private static JDBCCategoryDataset createCategoricalDataset(String query) {
		try {
			JDBCCategoryDataset jds = new JDBCCategoryDataset(conn);
			jds.executeQuery(query);
			return jds;
		}
		catch (SQLException se){
			se.printStackTrace();
		}
		return null;
	}
	
	private static JDBCPieDataset createPieDataset(String query) {
		try {
			JDBCPieDataset jds = new JDBCPieDataset(conn);
			jds.executeQuery(query);
			return jds;
		}
		catch (SQLException se){
			se.printStackTrace();
		}
		return null;
	}
    public static void main(String[] args) {
    	new App();
    	// JDBC
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/healthykoalas?serverTimezone=UTC","root", "bronyderp123");
     	

        } catch (SQLException e) {
             System.out.println("Connection Failed! Check output console");
             e.printStackTrace();
             return;
        }
        if (conn != null) {
            System.out.println("You made it, take control of your database now!");
    } else {
            System.out.println("Failed to make connection!");
    }
    }
}
