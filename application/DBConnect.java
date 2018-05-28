package application;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * The data base connection class.
 * 
 * @author robert ringel
 * @version 1.0 May 2017
 *
 */
public class DBConnect {

	/** reference to the connection instance */
	private DBActions dbc = null;
	
	/** the data base driver string */
	private String pdriver = null;
	
	/** the data base connection string */
	private String pconn = null;

	
    /**
     * Construction for the data base connection class.
     * It will read the configuration file for connection data.
     * The constructor will right open the data base connection.
     * @param configFile the configuration file
     */
    public DBConnect(String configFile) {
		Properties properties = new Properties();
		// read the data base driver and the connect string from the config file
		try {
			properties.load(new FileInputStream(configFile));	
			pdriver = properties.getProperty("jdbcdriver");
			pconn = properties.getProperty("jdbcconn");
		} catch (IOException e) {
			System.out.println("***** Exception: " + e.getMessage());
			System.exit(1);
		} 
    	connect(pdriver, pconn);
	}
	
    /**
     * The default constructor - it does nothing.
     * connection must be opened by calling the connect method.
     */
    public DBConnect()
    {};
    
    /**
     * This method opens the data base connection using the given parameters
     * @param driver the data base driver string
     * @param connect the data base connect string
     */
    public void connect(String driver, String connect) {
		dbc = new DBActions();
		dbc.initialize(driver, connect);
    }

    /**
     * This method closes the data base connection.
     */
    public void disconnect() {
    	dbc.close();
    }

    /**
     * This method executes a given SQL SELECT statement
     * @param queryStmt the SQL statement string
     * @return the ResultSet for the query
     * @throws SQLException
     */
    public ResultSet executeQuery(String queryStmt) throws SQLException {
        ResultSet result = null;
        result = dbc.performQuery(queryStmt);
        return result;
    }
    
    /**
     * This method executes a given SQL UPDATE statement
     * @param queryStmt the SQL statement string
     * @return OK in case of successor the error message in case of error
     * @throws SQLException
     */
    public String executeUpdate(String queryStmt) throws SQLException {
        
        String result = dbc.update(queryStmt);
        return result;
    }
    /**
     * A special method to evaluate user credential from the Cinderella entities table
     * @param user the user ID
     * @param pw the related pw phrase
     * @return OK in case of success - or "Invalid login" in case of failure
     */
    public String checkLoginData(String user, String pw)
    {
    	dbc.read("SELECT entity, note FROM entities where entity ='"+user+"'");
    	String result = dbc.getRow();    	
    	if (result.equalsIgnoreCase(user+"|"+pw+"|"))
    		return "OK";
    	
    	return "Invalid Login";
    }

	/**
	 * This method checks the data base functionality with help of a very basic statement.
	 * @return true in case of success - otherwise false
	 */
	public boolean checkConnection() {
		try {
			ResultSet rs = dbc.performQuery("SELECT NOW();");
		} catch (SQLException e) {
			return false;
		}
		return true;
	}
    
}
