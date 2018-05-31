package Model;

import org.neo4j.graphdb.GraphDatabaseService;

public class DatabaseConnection {

	private GraphDatabaseService graphDb;

	private String path;// identifier

	public DatabaseConnection(String p, GraphDatabaseService db) {
		path = p;
		graphDb = db;
		registerShutdownHook(db);
	}

	public String getPath() {
		return path;
	}

	public GraphDatabaseService getService() {
		return graphDb;
	}

	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("Closing Database");
				graphDb.shutdown();
				System.out.println("Closed Database");
			}
		});
	}
}
