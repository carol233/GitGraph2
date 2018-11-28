import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Carol on 2018/11/27.
 */
public class Analyser {
    private String project;
    private String filter;
    private GitOperator gitOperator;
    private Neo4jFuncs greeter;
    private List<Ref> branches;

    public Analyser(String project, String filter){
        this.project = project;
        gitOperator = new GitOperator(project, filter);
        greeter = new Neo4jFuncs( "bolt://localhost:7687", "neo4j", "2333" );
    }

    public void run(){
        branches = gitOperator.getBranches();
        System.out.println(branches);
        for(Ref branch : branches){
            if(branch.getName().equals("HEAD")){
                continue;
            }
            System.out.println(branch.getName());
            List<RevCommit> commits = gitOperator.getBranchCommits(branch);
            for(RevCommit commit : commits){
                System.out.println(commit.getName());
                List<FileObject> files = gitOperator.getCommitFiles(commit);
                //System.out.println(files);
                for(FileObject fileObject : files) {
                    System.out.println(fileObject.getPath());
                    System.out.println(fileObject.getName());
                    //System.out.println(fileObject.getFiledata());
                    Parser parser = new Parser(fileObject.getFiledata());
                    parser.listClasses();
                    parser.listMethodCalls();

                }
            }
        }
    }
}
