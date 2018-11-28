/**
 * Created by Carol on 2018/11/28.
 */
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Transaction;
import static org.neo4j.driver.v1.Values.parameters;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;

import java.io.File;

public class Neo4jFuncs {

        private final Driver driver;

        public Neo4jFuncs( String uri, String user, String password )
        {
            driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
        }

        public void close() throws Exception
        {
            driver.close();
        }

        public void createNode() {
            try (Transaction tx = db.beginTx()) {

                // 创建节点
                Node User1 = db.createNode(Tutorials.User);
                User1.setProperty("name", "梁川川1");
                User1.setProperty("sex", "男");
                User1.setProperty("age", "25");

                // 创建节点
                Node User2 = db.createNode(Tutorials.User);
                User2.setProperty("name", "谢静静1");
                User2.setProperty("sex", "女");
                User2.setProperty("age", "25");

                // 梁川川 喜欢 谢静静  的关系
                Relationship relationship = User1.createRelationshipTo(User2,TutorialRelationships.LOVE);
                // 设置关系属性  梁川川 喜欢谢静静 多久了   开始时间
                relationship.setProperty("time","1年");
                relationship.setProperty("startTime","2017-06-01");


                // 创建节点
                Node Phone = db.createNode(Tutorials.Phone);
                User1.setProperty("brand", "魅族");
                User1.setProperty("money", "1000");
                User1.setProperty("time", "2017-06-01");



                // 谢静静 有一部魅族手机  的关系
                Relationship phoneRelationship = User2.createRelationshipTo(Phone,TutorialRelationships.HIVE);
                // 设置关系属性
                phoneRelationship.setProperty("time","1年");
                phoneRelationship.setProperty("startTime","2017-06-01");


                tx.success();
            }
            // 关闭非常重要
            db.shutdown();
            System.out.println("Done successfully");
        }
    }

}
