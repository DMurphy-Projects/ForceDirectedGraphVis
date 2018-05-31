package Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.shell.ShellException;
import org.neo4j.shell.util.json.JSONArray;
import org.neo4j.shell.util.json.JSONException;
import org.neo4j.shell.util.json.JSONObject;
import org.neo4j.shell.util.json.JSONParser;

public class JsonParser {

    private Map<String, Node> ids;

    private static enum RelTypes implements RelationshipType {
	KNOWS
    }

    @SuppressWarnings("static-access")
    public JsonParser(String path) {
	JSONParser parser = new JSONParser();
	String total = "";

	ids = new HashMap<String, Node>();

	int count = 0;

	GraphDatabaseFactory fac = new GraphDatabaseFactory();
	GraphDatabaseService graphDb = fac.newEmbeddedDatabase(path + "db");

	try (Transaction tx = graphDb.beginTx()) {
	    try {
		total = new String(Files.readAllBytes(Paths.get(path)));
	    } catch (IOException e1) {
		e1.printStackTrace();
	    }
	    try {
		@SuppressWarnings("unchecked")
		Map<String, Object> jsonMap = (Map<String, Object>) parser
			.parse(total);
		String key;
		Object value;

		// create ndoes
		for (Entry<String, Object> entry : jsonMap.entrySet()) {
		    key = entry.getKey();
		    value = entry.getValue();
		    // System.out.println(key);
		    JSONArray i;
		    if (key.equals("nodes")) {
			// System.out.println("lol");
			i = (JSONArray) value;
			for (Object m : i.toList()) {
			    JSONObject nodeItem = (JSONObject) m;
			    Node n = graphDb.createNode();
			    String nodeKey = "id";
			    // System.out.println(nodeItem + " " +
			    // nodeItem.get(nodeKey) + " "
			    // + count++);
			    ids.put(nodeItem.get(nodeKey).toString(), n);
			    // n.setProperty(nodeKey.toString(),
			}
		    }
		}
		// create rels
		for (Entry<String, Object> entry : jsonMap.entrySet()) {
		    key = entry.getKey();
		    value = entry.getValue();
		    // System.out.println(key);
		    JSONArray i;
		    if (key.equals("nodes")) {
			i = (JSONArray) value;
			for (Object m : i.toList()) {
			    JSONObject nodeItem = (JSONObject) m;
			    // System.out.println(nodeItem.get("id").toString());
			    Node n1 = ids.get(nodeItem.get("id").toString());

			    JSONArray sas = nodeItem.getJSONArray("out");
			    for (Object jo : sas.toList()) {
				Node n2 = ids.get(jo.toString());
				try {
				    n1.createRelationshipTo(n2, RelTypes.KNOWS);
				} catch (IllegalArgumentException e) {

				}
			    }
			}
		    }
		}
	    } catch (ShellException | JSONException e) {
		e.printStackTrace();
	    }
	    tx.success();
	    System.out.println("done");
	}

    }

}
