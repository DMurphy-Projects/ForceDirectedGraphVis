package Controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.impl.cache.WeakCacheProvider;
import org.neo4j.tooling.GlobalGraphOperations;

import Model.DatabaseConnection;
import Model.ForceNodeGrouper;
import Model.Graph;
import Model.LoadScreenModel;
import Model.NodePrePositioner;
import Model.Force.ForceNode;
import Model.Force.ForceRelationship;

public class Neo4jDatabaseHandler {

    private List<DatabaseConnection> mCachedDatabases;

    private String mDirectory = "C:\\Users\\Dean\\Downloads\\RandomDb\\";

    private GraphDatabaseFactory mFac = new GraphDatabaseFactory();

    private static enum RelTypes implements RelationshipType {
	KNOWS
    }

    public Neo4jDatabaseHandler(String path) {
	mCachedDatabases = new ArrayList<DatabaseConnection>();
	mDirectory = path;
    }

    private GraphDatabaseService makeService(String dbPath) {
	LoadScreenModel.addMessage("Getting Service");
	GraphDatabaseService graphDb = null;
	for (DatabaseConnection con : mCachedDatabases) {
	    if (dbPath.equals(con.getPath())) {
		graphDb = con.getService();
		break;
	    }
	}
	if (graphDb == null) {
	    // graphDb = fac.newEmbeddedDatabase(dbPath);

	    // reduces memory usage, but increases CPU usage
	    boolean msg = false;
	    while (true) {
		try {
		    graphDb = mFac
			    .newEmbeddedDatabaseBuilder(dbPath)
			    .setConfig(GraphDatabaseSettings.cache_type,
				    WeakCacheProvider.NAME)
			    .setConfig(GraphDatabaseSettings.pagecache_memory,
				    "16384")// 16384//32768
			    .newGraphDatabase();
		    break;
		} catch (RuntimeException e) {
		    if (!msg) {
			LoadScreenModel
				.addMessage("DataBase already in use, one moment");
			msg = true;
		    }
		}
	    }
	    mCachedDatabases.add(new DatabaseConnection(dbPath, graphDb));
	}
	return graphDb;
    }

    private ForceNode findNode(long id, ForceNode[] mNodes) {
  	for (ForceNode fNode : mNodes) {
  	    if (fNode.NodeInfo().getId() == id) {
  		return fNode;
  	    }
  	}
  	return null;
      }

    private Graph popGraph(GraphDatabaseService graphDb) {
	LoadScreenModel.addMessage("Populating Graph");
	ForceNodeGrouper grouper;
	NodePrePositioner positioner = new NodePrePositioner();
	// node creation
	try (Transaction tx = graphDb.beginTx()) {
	    Iterable<Node> listOfNodes = GlobalGraphOperations.at(graphDb)
		    .getAllNodes();
	    Iterable<Relationship> listOfRels = GlobalGraphOperations.at(
		    graphDb).getAllRelationships();

	    // count nodes
	    int nodeSize = 0;
	    for (Node n : listOfNodes) {
		nodeSize++;
	    }
	    // count relations
	    int relSize = 0;
	    for (Relationship r : listOfRels) {
		relSize++;
	    }

	    ForceNode[] nodes = new ForceNode[nodeSize];
	    int nodeIndex = 0;
	    ForceRelationship[] relations = new ForceRelationship[relSize];
	    int relIndex = 0;

	    // g = new Graph(graphDb, nodeSize, relSize);

	    // convert relationship Iterable to array - may not be needed
	    // anymore
	    LoadScreenModel.addMessage("Arraying Relations");
	    ArrayList<Relationship> rels;
	    Relationship[] relArr;
	    Point p;
	    for (Node n : listOfNodes) {
		rels = new ArrayList<Relationship>();
		for (Relationship r : n.getRelationships()) {
		    rels.add(r);
		}
		relArr = new Relationship[rels.size()];
		p = positioner.getPosition();
		nodes[nodeIndex++] = new ForceNode(p.x, p.y, n,
			rels.toArray(relArr));
	    }

	    // add force relations to graph
	    long id1;
	    long id2;

	    ForceNode n1;
	    ForceNode n2;

	    ForceRelationship fRel;

	    for (Relationship r : listOfRels) {
		id1 = r.getStartNode().getId();
		id2 = r.getEndNode().getId();

		// cross reference Node Id to get ForceNode
		n1 = findNode(id1, nodes);
		n2 = findNode(id2, nodes);

		// setup relation
		fRel = new ForceRelationship();
		fRel.addForceNode(n1);
		fRel.addForceNode(n2);

		relations[relIndex++] = fRel;
	    }
	    grouper = new ForceNodeGrouper(nodes, relations);
	    tx.success();
	}
	LoadScreenModel.addMessage("Grouping Graph");
	Graph g = new Graph(graphDb, grouper);
	return g;
    }
    
    private List<Node> toList(Iterable<Node> nodes) {
	List<Node> nodeList = new ArrayList<Node>();
	for (Node n : nodes) {
	    nodeList.add(n);
	}
	return nodeList;
    }

    public Graph newRandomGraph(int numOfNodes) {
	String path = mDirectory + UUID.randomUUID();
	GraphDatabaseService graphDb = makeService(path);

	try (Transaction tx = graphDb.beginTx()) {
	    for (int i = 0; i < numOfNodes; i++) {
		graphDb.createNode();
	    }
	    List<Node> nodes = toList(GlobalGraphOperations.at(graphDb)
		    .getAllNodes());
	    Random rGen = new Random();
	    LoadScreenModel.addMessage("Linking Nodes");
	    int type = rGen.nextInt(3);
	    type = 2;
	    switch (type) {
	    case 0:
		oneLink(nodes);
		break;
	    case 1:
		everythingToEverything(nodes);
		break;
	    case 2:
		ring(nodes);
		break;
	    case 3:
		mesh(nodes);
		break;
	    }

	    tx.success();
	}

	Graph g = popGraph(graphDb);
	return g;
    }

    private void mesh(List<Node> nodes) {
	int root = (int) Math.sqrt(nodes.size());
	Node list[][] = new Node[root][root];
	int w = 0, h = 0;
	for (Node n : nodes) {
	    list[w][h] = n;
	    w++;
	    if (w >= root) {
		w = 0;
		h++;
	    }
	}
	for (int i = 0; i < root; i++) {
	    for (int ii = 0; ii < root; ii++) {
		if (i > 0) {
		    // System.out.println(i + " " + ii + " => " + (i - 1) + " "
		    // + ii);
		    list[i][ii].createRelationshipTo(list[i - 1][ii],
			    RelTypes.KNOWS);
		}
		if (ii > 0) {
		    // System.out.println(i + " " + ii + " => " + i + " "
		    // + (ii - 1));
		    list[i][ii].createRelationshipTo(list[i][ii - 1],
			    RelTypes.KNOWS);
		}
	    }
	}
    }

    private void oneLink(List<Node> nodes) {
	Node node1;
	Node node2;
	int num1 = 0;
	int num2 = 0;
	for (int i = 0; i < nodes.size() - 1; i++) {
	    // if (rGen.nextInt(1000) != 0) {
	    num1 = i;
	    num2 = i + 1;
	    node1 = nodes.get(num1);
	    node2 = nodes.get(num2);
	    node1.createRelationshipTo(node2, RelTypes.KNOWS);
	    // }
	}
	node1 = nodes.get(num1);
	node2 = nodes.get(0);
	node1.createRelationshipTo(node2, RelTypes.KNOWS);
    }

    private void everythingToEverything(List<Node> nodes) {
	Node node1;
	Node node2;
	for (int i = 0; i < nodes.size(); i++) {
	    node1 = nodes.get(i);
	    for (int ii = 0; ii < nodes.size(); ii++) {
		// dont connect node to itself
		if (i == ii)
		    continue;
		node2 = nodes.get(ii);
		boolean dontRel = false;
		// dont do duplicate relationships
		for (Relationship r : node1.getRelationships()) {
		    for (Node n : r.getNodes()) {
			if (n.getId() == node2.getId()) {
			    dontRel = true;
			    break;
			}
		    }
		    if (dontRel)
			break;
		}
		if (!dontRel) {
		    node1.createRelationshipTo(node2, RelTypes.KNOWS);
		}
	    }
	}
    }

    private void ring(List<Node> nodes) {
	Node node1;
	Node node2;
	for (int i = 0; i < nodes.size() - 1; i++) {
	    node1 = nodes.get(i);
	    int ahead1 = i + 1;
	    int ahead2 = i + 2;
	    if (ahead1 > nodes.size())
		ahead1 -= nodes.size();
	    if (ahead2 > nodes.size() - 1)
		ahead2 -= nodes.size();
	    node2 = nodes.get(ahead1);

	    node1.createRelationshipTo(node2, RelTypes.KNOWS);
	}
	node1 = nodes.get(0);
	node2 = nodes.get(nodes.size() - 1);

	node1.createRelationshipTo(node2, RelTypes.KNOWS);
    }

    public Graph parseGraph(String dbPath) {
	GraphDatabaseService graphDb = makeService(dbPath);
	LoadScreenModel.addMessage("Service Obtained");
	Graph g = popGraph(graphDb);
	LoadScreenModel.addMessage("Graph Populated");
	return g;
    }
}
