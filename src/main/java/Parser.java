/**
 * Created by Carol on 2018/11/27.
 */
import api.APIDatabase;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import helper.Utils;
import model.ApiObject;
import model.ClassObject;
import model.MethodObject;


import java.util.ArrayList;
import java.util.List;

public class Parser {
    private String filepath;
    private CompilationUnit compilationUnit;

    public Parser(String filepath, String filedata){
        this.filepath = filepath;
        this.compilationUnit = JavaParser.parse(filedata);
    }

    public List<ClassOrInterfaceDeclaration> getClasses(){
        List<ClassOrInterfaceDeclaration> classes = new ArrayList<>();
        for (Node node : compilationUnit.getChildNodes()){
            if (node instanceof ClassOrInterfaceDeclaration){
                classes.add((ClassOrInterfaceDeclaration) node);
            }
        }
        return classes;
    }

    public ClassObject resolveClass(ClassOrInterfaceDeclaration clazz, APIDatabase apiDatabase) {
        List<MethodObject> methods = new ArrayList<>();
        NodeList<BodyDeclaration<?>> member = clazz.getMembers();
        NodeList<ImportDeclaration> importDeclarations;
        List<String> imports = new ArrayList<>();
        importDeclarations = compilationUnit.getImports();
        for (int i = 0; i < importDeclarations.size(); i++) {
            imports.add(importDeclarations.get(i).getName().toString());
        }

        ClassObject co = new ClassObject();
        for (int i = 0; i < member.size(); i++) {
            if (member.get(i) instanceof MethodDeclaration) {
                MethodDeclaration m = (MethodDeclaration) member.get(i);
                MethodObject mo = new MethodObject();
                mo.setName(m.getDeclarationAsString());
                mo.setFile(this.filepath);
                mo.setBody(m.toString());
                mo.setClazz(clazz.getNameAsString());

                List<String> calls = Utils.extractCalls(mo.getBody());
                List<ApiObject> apis = new ArrayList<>();
                for (String call : calls) {
                    if (apiDatabase != null) {
                        ApiObject api = apiDatabase.isApi(imports, call);
                        if (api != null) {
                            apis.add(api);
                        }
                    }
                }
                mo.setApis(apis);
                methods.add(mo);
            }
        }
        co.setMethods(methods);
        co.setName(clazz.getNameAsString());
        co.setBody(clazz.toString());
        co.setFile(filepath);
        return co;
    }
}



