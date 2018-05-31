package Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.QueryExecutionException;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

public class CypherParser {

    public CypherParser(String path) {
	GraphDatabaseFactory fac = new GraphDatabaseFactory();
	File f = new File(path+"db");
	if (!f.exists()) f.mkdirs();

	System.out.println("Cleaning "+path);
	for(File file: f.listFiles())
		if (!file.isDirectory())
			file.delete();
	System.out.println("Parsing Cypher");
	GraphDatabaseService graphDb = fac.newEmbeddedDatabase(path + "db");
	try (Transaction tx = graphDb.beginTx()) {
		graphDb.execute(new String(Files.readAllBytes(Paths.get(path))));
		tx.success();
	} catch (QueryExecutionException | IOException e) {
		e.printStackTrace();
	}
	finally {
		graphDb.shutdown();
	}
	System.out.println("Done");
    }
}
