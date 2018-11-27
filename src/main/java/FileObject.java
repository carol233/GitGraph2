import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;

/**
 * Created by Carol on 2018/11/27.
 */
public class FileObject {
    private String path = null;
    private String type = null;
    private String name = null;
    private ObjectLoader loader = null;
    private ObjectId id = null;
    private String filedata = null;

    public String getPath() {
        return path;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getFiledata() {
        return filedata;
    }

    public ObjectLoader getLoader() {
        return loader;
    }

    public ObjectId getId() {
        return id;
    }


    public FileObject(String path, String name, ObjectId id){
        this.path = path;
        this.name = name;
        this.id = id;
        if (name.lastIndexOf(".") != -1){
            this.type = name.substring(name.lastIndexOf(".") + 1);
        }else{
            this.type = null;
        }
    }
    public void setLoader(ObjectLoader loader){
        this.loader = loader;
    }

    public void setFiledata(String filedata) {
        this.filedata = filedata;
    }
}
