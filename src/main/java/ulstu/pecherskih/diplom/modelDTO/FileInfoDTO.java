package ulstu.pecherskih.diplom.modelDTO;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import ulstu.pecherskih.diplom.service.JavaParserService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FileInfoDTO {
    private File me;
    private FileInfoDTO parentFile;
    private ClassDTO classDTO;

    private List<FileInfoDTO> children;

    public FileInfoDTO(File me) {
        this.me = me;
        this.parentFile = null;
        this.children = new ArrayList<FileInfoDTO>();
    }

    public FileInfoDTO(File me, FileInfoDTO parentFile) throws FileNotFoundException {
        this.me = me;
        this.parentFile = parentFile;
        this.children = new ArrayList<FileInfoDTO>();
    }

    public File getMe() {
        return me;
    }

    public String getPath() {
        return this.me.getPath();
    }

    public void setClassDTO(ClassDTO classDTO) {
        this.classDTO = classDTO;
    }

    public String getName() {
        return this.me.getName();
    }

    public ClassDTO getClassDTO() {
        return this.classDTO;
    }

    public FileInfoDTO getParentFile() {
        return parentFile;
    }

    public void addChild(FileInfoDTO child) {
        this.children.add(child);
    }

    public List<FileInfoDTO> getChildren() {
        return children;
    }

    public boolean isDirectory() {
        return me.isDirectory();
    }

    public boolean isJavaFile() {
        return me.getPath().endsWith(".java");
    }
}
