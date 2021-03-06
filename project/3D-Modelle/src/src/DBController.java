/**
 * 
 */
package src;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * @author Lennard Flegel
 *
 * Singleton-Class for DB-access. Singleton NOT threadsafe.
 *
 */
public class DBController {
	//Members
	private static DBController instance;
	private final File directory;
	private static GraphDatabaseService graphDb;
	
	//Constructors
	/**
	 * Constructor
	 * 
	 * @param directory Target directory to initialize DB
	 */
	private DBController(File directory) {
		this.directory = directory;
	}
	
	//Methods
	/**
	 * Method to realize Singleton-Pattern
	 * 
	 * @param directory  Target directory to initialize DB
	 * @return instance of DBController to access DB
	 */
	 public static DBController getInstance (File directory) {
		    if (DBController.instance == null) {
		    	DBController.instance = new DBController (directory);
		    }
		    return DBController.instance;
		 }
	  
  	/**
 	 * Registers a shutdown hook for the Neo4j instance so that it
	 * shuts down nicely when the VM exits (even if you "Ctrl-C" the
	 * running application).
	 * 
	 * @param GraphDatabaseService GDBService to link the hook to
	 */
	private static void registerShutdownHook( final GraphDatabaseService graphDb )
	{
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	        }
	    } );
	}
	
	/**
	 * Instantiate a GraphDatabaseService to
	 * create a new database or open an existing one 
	 * 
	 * @param directory Target directory to initialize DB
	 */
	public static void initializeDB(File directory){
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( directory );
		registerShutdownHook( graphDb );
	}

	/**
	 * Method that executes a Cypher Query Language query
	 * 
	 * @param cypher CQL code to execute
	 * @return query result as Neo4J {@code Result} type
	 */
	public static Result executeQuery(String cypher){
		Result result;
		 try ( Transaction tx = graphDb.beginTx() )
		 {
		     result= graphDb.execute(cypher);
		     
		     tx.success();
		 }
		return result;
	}

	/**
	 * Removes all nodes with given Object-ID from DB
	 * @param id 
	 */
	public static void removeByOBJ_ID(float id){
		//TODO foreach (node n: nodes) if n.id==id delete n;
	}
	
	/**
	 * TODO
	 */
	public static void writeToDB(){
		//TODO
	}
	
	/**
	 * Shuts down the DB. Make sure to call this once 
	 * you no longer plan on using transactions.
	 * 
	 */
 	public static void shutdownDB(){
		graphDb.shutdown();
		}
	
	//Getters
	/**
	 * Just a getter
	 * @return DB-directory
	 */
	public File getDirectory() {
		return directory;
	}

	
}
