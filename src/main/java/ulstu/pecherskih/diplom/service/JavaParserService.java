package ulstu.pecherskih.diplom.service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import org.checkerframework.checker.nullness.Opt;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.modelDTO.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JavaParserService {

    public PackageDTO fillProject(FileInfoDTO project) throws FileNotFoundException {
        PackageDTO mainPackage = new PackageDTO(project.getName());

        this.parsePackage(project, mainPackage);

        return mainPackage;
    }

    private void parsePackage(FileInfoDTO packageFile, PackageDTO parentPackage) throws FileNotFoundException {
        for (FileInfoDTO fileInfo : packageFile.getChildren()) {
            if (fileInfo.isDirectory()) {
                PackageDTO packageDTO = new PackageDTO(fileInfo.getName());
                parentPackage.addPackage(packageDTO);

                this.parsePackage(fileInfo, packageDTO);
            } else if (fileInfo.isJavaFile()) {
                parentPackage.addClass(this.parseClass(fileInfo));
            }
        }
    }

    public ClassDTO parseClass(FileInfoDTO fileInfo) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(fileInfo.getPath()));

        ClassDTO classDTO = new ClassDTO(fileInfo.getName());
        classDTO.setFields(this.parseFields(cu));
        classDTO.setMethods(this.parseMethods(cu));

        return classDTO;
    }

    /**
     * Парсинг полей класса
     * @param cu
     * @return
     */
    private List<ClassFieldDTO> parseFields(CompilationUnit cu) {
        List<FieldDeclaration> fieldDeclarations = cu.findAll(FieldDeclaration.class);
        List<ClassFieldDTO> classFieldDTOs = new ArrayList<>();

        for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
            String modifier = "private";
            if (fieldDeclaration.getModifiers().size() > 0) {
                modifier = fieldDeclaration.getModifiers().get(0).getKeyword().asString();
            }
            String name = fieldDeclaration.getVariables().get(0).getNameAsString();
            String type = fieldDeclaration.getVariables().get(0).getTypeAsString();

            ClassFieldDTO classFieldDTO = new ClassFieldDTO(name, type, modifier);
            classFieldDTOs.add(classFieldDTO);
        }

        return classFieldDTOs;
    }

    /**
     * Парсинг полей класса
     * @param cu
     * @return
     */
    private List<MethodDTO> parseMethods(CompilationUnit cu) {
        List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class);
        List<MethodDTO> methodDTOs = new ArrayList<>();

        for (MethodDeclaration method : methodDeclarations) {
            String name = method.getNameAsString();
            String returnType = method.getType().toString();
//            boolean isStatic = ModifierSet.hasModifier(method.getModifiers(), ModifierSet.STATIC);

            MethodDTO methodDTO = new MethodDTO(name, returnType);
            methodDTO.setArguments(this.parseArgumets(method));
            methodDTO.setBlockStmts(this.parseMethodBlock(method));

            methodDTOs.add(methodDTO);
        }

        return methodDTOs;
    }

    /**
     * Параметры метода
     *
     * @param method
     * @return List<String>
     */
    public List<MethodArgumentDTO> parseArgumets(MethodDeclaration method) {
        List<MethodArgumentDTO> args = new ArrayList<>();

        for (Parameter p : method.getParameters()) {
            MethodArgumentDTO methodArgument = new MethodArgumentDTO(p.getName().toString(), p.getTypeAsString());

            args.add(methodArgument);
        }

        return args;
    }

    /**
     * Параметры метода
     *
     * @param method
     * @return List<String>
     */
    public List<BlockStmtDTO> parseMethodBlock(MethodDeclaration method) {
        NodeList<Statement> statements = method.getBody().orElse(new BlockStmt()).getStatements();
        List<BlockStmtDTO> blockStmtDTOs = new ArrayList<>();

        for (Statement s : statements) {
            List<Node> children = s.getChildNodes();

            String name = "";
            String type = "";
            String value = "";

            boolean hasMethodCall = false;
            boolean hasVariable = false;
            boolean hasExpr = false;

            for (Node child : children) {
                /**
                 * Переменная
                 */
                if (child instanceof VariableDeclarationExpr) {
                    hasVariable = true;
                    name = ((VariableDeclarationExpr) child).getVariables().get(0).getNameAsString();

                    Expression var = ((VariableDeclarationExpr) child).getVariables().get(0).getInitializer().orElse(null);
                    if (var instanceof MethodCallExpr) {
                        value = ((MethodCallExpr) var).getNameAsString();

                        Expression e = ((MethodCallExpr) var).getScope().orElse(null);
                        if (e instanceof NameExpr) {
                            value = ((NameExpr) e).getNameAsString() + "." + value;
                        }
                    }

                    if (var instanceof StringLiteralExpr) {
                        value = ((StringLiteralExpr) var).getValue();
                    }
                    if (var instanceof IntegerLiteralExpr) {
                        value = ((IntegerLiteralExpr) var).getValue();
                    }
                    if (var instanceof ObjectCreationExpr) {
                        value = ((ObjectCreationExpr) var).getTypeAsString();
                    }

                    /**
                     * Метод
                     */
                } else if (child instanceof MethodCallExpr) {
                    hasMethodCall = true;
                    name = ((MethodCallExpr) child).getNameAsString();

                    Expression e = ((MethodCallExpr) child).getScope().orElse(null);
                    if (e instanceof NameExpr) {
                        name = ((NameExpr) e).getNameAsString() + "." + name;
                    }
                    if (e instanceof FieldAccessExpr) {
                        name = ((FieldAccessExpr) e).getNameAsString() + "." + name;

                        Expression scope = ((FieldAccessExpr) e).getScope();
                        if (scope instanceof NameExpr) {
                            name = ((NameExpr) scope).getNameAsString() + "." + name;
                        }
                    }

                    String args = "";
                    for (Expression arg : ((MethodCallExpr) child).getArguments()) {
                        Expression argScope = ((MethodCallExpr) child).getScope().orElse(null);
                        if (argScope instanceof NameExpr) {
                            args = ((NameExpr) argScope).getNameAsString() + ".";
                        }

                        if (arg instanceof MethodCallExpr) {
                            args = args + ((MethodCallExpr) arg).getNameAsString();
                        }
                        if (arg instanceof VariableDeclarationExpr) {
                            args = args + ((VariableDeclarationExpr) arg).getVariables().get(0).getNameAsString();
                        }
                        if (arg instanceof ObjectCreationExpr) {
                            args = args + ((ObjectCreationExpr) arg).getTypeAsString();
                        }
                        if (arg instanceof StringLiteralExpr) {
                            args = args + ((StringLiteralExpr) arg).getValue();
                        }
                        if (arg instanceof IntegerLiteralExpr) {
                            args = args + ((IntegerLiteralExpr) arg).getValue();
                        }
                    }

                    value = args;

                    /**
                     * Цикл
                     */
                } else if (child instanceof BinaryExpr) {
                    hasExpr = true; // значит есть VariableDeclarator
                } else {
//                    name = child.getClass().getName();
//                    value = child.getClass().getName();
                }
            }

            if (hasExpr) {
                type = "цикл";
            } else if (hasMethodCall) {
                type = "вызов функции";
            } else if (hasVariable) {
                type = "присвоение переменной";
            } else {
                type = "что-то другое";
            }

            BlockStmtDTO blockStmtDTO = new BlockStmtDTO(type, name, value);

            blockStmtDTOs.add(blockStmtDTO);
        }

        return blockStmtDTOs;
    }
}
