/**
 * Created by Carol on 2018/11/27.
 */
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import jdk.nashorn.internal.runtime.ParserException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private String filedata = null;
    private String filepath = null;
    public List<ClassOrInterfaceDeclaration> classes = new ArrayList<>();

    public Parser(String filepath, String filedata){
        this.filedata = filedata;
        this.filepath = filepath;
        try {
            new VoidVisitorAdapter<Object>() {
                @Override
                public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                    super.visit(n, arg);
                    classes.add(n);
                }
            }.visit(JavaParser.parse(filedata), null);
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    public ClassObject getClass(ClassOrInterfaceDeclaration clazz){
        List<MethodObject> methods = new ArrayList<>();
        NodeList<BodyDeclaration<?>> member = clazz.getMembers();
        ClassObject co = new ClassObject();
        for (int i=0; i<member.size(); i++){
            if (member.get(i) instanceof MethodDeclaration){
                MethodDeclaration m = (MethodDeclaration) member.get(i);
                MethodObject mo = new MethodObject();
                mo.setName(m.getDeclarationAsString());
                mo.setFile(this.filepath);
                mo.setBody(m.toString());
                mo.setClazz(clazz.getNameAsString());
                methods.add(mo);
            }
        }
        co.setMethods(methods);
        co.setName(clazz.getNameAsString());
        co.setBody(clazz.toString());
        co.setFile(filepath);
        return co;
    }

    public List<ClassOrInterfaceDeclaration> getClasses() {
        return classes;
    }
}



