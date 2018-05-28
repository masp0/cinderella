package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

	
	/**
	 * This class provides the functionality to access a data base to perform SELECT statements.
	 * 
	 * @author Robert Ringel
	 * @version 0.05 - March 2016
	 * 
	 */
	public class DBActions {

		/** reference to the data base connection */
		private Connection connect = null;
		
		/** the data base statement to be performed */
		private Statement statement = null;
		
		/** the result set of the executed statement */
		private ResultSet resultSet = null;
		
		/**
		 * This method initializes the data base connection.
		 * @param sqlDriver the SQL driver to be used
		 * @param sqlConnection the URL of the SQL connection
		 */
		public void initialize(String sqlDriver, String sqlConnection) {
			try {
//				Class.forName(sqlDriver);
				Class.forName("org.sqlite.JDBC");
			} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		    try {
		    	String url = "jdbc:sqlite:Resources/Cinderella_TestDB.db";
//	            connect = DriverManager.getConnection("jdbc:sqlite:Resources/Min.db");
		    	//connect = DriverManager.getConnection("jdbc:sqlite::resource:"+getClass().getResource("Min2.db")); 
		    	connect = DriverManager.getConnection(url);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

		    try {
				statement = connect.createStatement();
			} catch (SQLException e) {
				System.out.println("Can not create SQL statement. " + e.getMessage());
				e.printStackTrace();
			}  
		      
		  }
		    
		/**
		 * This method reads data from the data base. 
		 * It should perform SELECT statements.
		 * @param sqlSelect the SQL SELECT statement to access the data base
		 */
		public void read(String sqlSelect){
			  try {
		      statement = connect.createStatement();
		      resultSet = statement.executeQuery(sqlSelect);
			  } catch (Exception e) {
				  System.out.println("There was an exception: "+e.getMessage());
			      //e.printStackTrace();
			  }      
		  }	
		
		/**
		 * This method provides one row of data out of the SQL result data set.
		 * It will return an empty string in case there is no more data available.
		 * @return one line of SQL result data
		 */
		public String getRow() 
		{
			StringBuffer strRow = new StringBuffer();
			if (resultSet != null)
			{
				try 
				{
					int colCount = resultSet.getMetaData().getColumnCount();
					if (resultSet.next()) 
					{
						for (int index=1; index<=colCount; index++)
							strRow.append(resultSet.getString(index)+"|");
					}
				} catch(Exception e) {
					System.out.println("Reading SQL result failed. " + e.getMessage());
					System.out.println(e.getMessage());
			    }
			}
			return strRow.toString();
		}
		
		/**
		 * This method provides the sql header row showing the result column names.
		 * @return a string containing the sql result column names
		 */
		public String getHeader() 
		{
			StringBuffer strHeader = new StringBuffer();
			if (resultSet != null)
			{
				try 
				{
					int colCount = resultSet.getMetaData().getColumnCount();
					for (int index=1; index<=colCount; index++)
						strHeader.append(resultSet.getMetaData().getColumnName(index)+"|");
				} catch(Exception e) {
					System.out.println("Reading SQL header failed. " + e.getMessage());
					e.printStackTrace();
			    }
			}
			return strHeader.toString();
		}
		
		/**
		 * This method can be used to perform updates or inserts.
		 * @param sql the SQL statement to be performed here
		 * @return OK in case of success or the error message in case of error
		 */
		public String update(String sql)
		{
			
			String result = "OK";
			try {
				connect.setAutoCommit(true);
				long rows = this.statement.executeUpdate(sql);
//				System.out.println(sql);
//				connect.commit();
				statement.close();
			} catch (Exception e) {
				System.out.println("Execution of  SQL operation failed. " + sql);
				System.out.println(e.getMessage());
				result = e.getMessage();
			}
			return result;
		}
		
		/**
		 * This method closes any open internal instances.
		 */
		public void close() {
			try {
				if (resultSet != null) {
			        resultSet.close();
			    }

			    if (statement != null) {
			    	statement.close();
			    }

			    if (connect != null) {
			        connect.close();
			    }
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				}
		}

		public ResultSet performQuery(String queryStmt) throws SQLException {
			ResultSet resSet = null;
			statement = connect.createStatement();
			resSet = statement.executeQuery(queryStmt);
			return resSet;
		}

}
