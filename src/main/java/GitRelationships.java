
import org.neo4j.graphdb.RelationshipType;

public enum GitRelationships implements RelationshipType {
    BranchtoCommit, CommittoCommit, CommittoFile, FiletoClass, ClasstoMethod
}