import api.APIDatabase;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import git.GitOperator;
import model.ApiObject;
import model.ClassObject;
import model.FileObject;
import model.MethodObject;
import neo4j.GitRelationships;
import neo4j.Neo4jFuncs;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.neo4j.graphdb.Node;

import java.util.List;

/**
 * Created by Carol on 2018/11/27.
 */
public class Analyser {
    private String project;
    private String filter;
    private GitOperator gitOperator;
    private List<Ref> branches;
    private Neo4jFuncs neo4j;

    public Analyser(String project, String filter){
        this.project = project;
        gitOperator = new GitOperator(project, filter);
        neo4j = new Neo4jFuncs();
    }

    public void run(){
        APIDatabase apiDatabase = new APIDatabase();
        apiDatabase.load_api("assets/current.txt");
        apiDatabase.load_api("assets/system-current.txt");

        Node branch_node, last_commit = null, api_node, now_commit, file_node, class_node, method_node;
        Boolean file_flag, class_flag, method_flag;
        branches = gitOperator.getBranches();
        for(Ref branch : branches){
            if(branch.getName().equals("HEAD")){
                continue;
            }
            branch_node = neo4j.createBranchNode(branch.getName());

            List<RevCommit> commits = gitOperator.getBranchCommits(branch);
            for(RevCommit commit : commits){
                now_commit = neo4j.createCommitNode(commit.getName(), commit.getAuthorIdent().toString(),
                        commit.getCommitTime(), commit.getFullMessage());
                neo4j.createRelationship(branch_node, now_commit, GitRelationships.BranchtoCommit);
                if(last_commit != null){
                    neo4j.createRelationship(last_commit, now_commit, GitRelationships.CommittoCommit);
                }

                List<FileObject> files = gitOperator.getCommitFiles(commit);

                for(FileObject fileObject : files) {
                    file_flag = false;
                    file_node = neo4j.matchFileMD5(fileObject);
                    if(file_node == null){ //new
                        file_flag = true;
                        file_node = neo4j.createFileNode(fileObject, commit.getName());
                    }
                    neo4j.createRelationship(now_commit, file_node, GitRelationships.CommittoFile);
                    if (file_flag && fileObject.getType().equals("java")) {
                        Parser parser = new Parser(fileObject.getPath(), fileObject.getFiledata());
                        //System.out.println(fileObject.getPath());

                        for (ClassOrInterfaceDeclaration clazz : parser.getClasses()){
                            class_flag = false;
                            ClassObject co = parser.resolveClass(clazz, apiDatabase);
                            class_node = neo4j.matchClassMD5(co);
                            if(class_node == null){
                                class_flag = true;
                                class_node = neo4j.createClassNode(co, commit.getName());
                            }
                            neo4j.createRelationship(file_node, class_node, GitRelationships.FiletoClass);
                            if(class_flag){
                                for(MethodObject mo : co.getMethods()){
                                    method_flag = false;
                                    method_node = neo4j.matchMethodMD5(mo);
                                    if(method_node == null){
                                        method_flag = true;
                                        method_node = neo4j.createMethodNode(mo, commit.getName());
                                    }
                                    neo4j.createRelationship(class_node, method_node, GitRelationships.ClasstoMethod);
                                    if(method_flag){
                                        for(ApiObject ao : mo.getApis()){
                                            api_node = neo4j.matchAPI(ao);
                                            if(api_node == null){
                                                api_node = neo4j.createAPINode(ao);
                                            }
                                            neo4j.createRelationship(method_node, api_node, GitRelationships.MethodtoAPI);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                last_commit = now_commit;
            }
            last_commit = null;
        }
        neo4j.db.shutdown();
    }
}
