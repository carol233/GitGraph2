/**
 * Created by Carol on 2018/11/27.
 */
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import jdk.nashorn.internal.runtime.ParserException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Parser {
    private String filedata = "";
    public List<String> classes;
    public List<String> methods;
    public List<String> APIs;


    public Parser(String filedata){
        this.filedata = filedata;
    }

    public void listMethodCalls() {
        try {
            new VoidVisitorAdapter<Object>() {
                @Override
                public void visit(MethodCallExpr n, Object arg) {
                    super.visit(n, arg);

                    System.out.println(n);
                }
            }.visit(JavaParser.parse(filedata), null);
            System.out.println(); // empty line
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }

    public void listClasses () {
        try {
            new VoidVisitorAdapter<Object>() {
                @Override
                public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                    super.visit(n, arg);

                    System.out.println("* " + n.getName());
                }
            }.visit(JavaParser.parse(filedata), null);
            System.out.println(); // empty line
        } catch (ParserException e) {
            e.printStackTrace();
        }
    }


}



