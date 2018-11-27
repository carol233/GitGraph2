import org.apache.commons.cli.*;

/**
 * Created by Carol on 2018/11/27.
 */

public class Main {
    public static void main(String[] argv){
        String project = "";
        String filter = "";
        for (int i = 0; i < argv.length; i++){
            if(argv[i].equals("-p")){
                project = argv[i+1];
            }
            else if(argv[i].equals("-f")){
                filter = argv[i+1];
            }
        }
        new Analyser(project, filter).run();
    }
}
