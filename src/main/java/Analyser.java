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
    public String project;
    public String filter;
    private GitOperator gitOperator;
    private List<Ref> branches;

    public Analyser(String project, String filter){
        this.project = project;
        gitOperator = new GitOperator(project, filter);
    }

    public void run(){
        branches = gitOperator.getBranches();
        for(Ref branch : branches){
            System.out.println(branch.getName());
            List<RevCommit> commits = gitOperator.getBranchCommits(branch);
            for(RevCommit commit : commits){
                System.out.println(commit.getName());
                List<FileObject> files = gitOperator.getCommitFiles(commit);
                for(FileObject fileObject : files) {
                    System.out.println(fileObject.getPath());
                    System.out.println(fileObject.getName());
                    //PathFilter.create();
                    //System.out.println(gitOperator.getFilename(id));
//                    ObjectLoader loader = gitOperator.getFileLoader(id);
//                    try {
//                        System.out.println(new String(loader.getBytes(), "UTF-8"));
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }
    }
}
