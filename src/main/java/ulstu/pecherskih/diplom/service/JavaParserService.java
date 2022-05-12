package ulstu.pecherskih.diplom.service;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
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
     *
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
     *
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
            BlockStmtDTO blockStmtDTO = this.parseStatement(s);

            blockStmtDTOs.add(blockStmtDTO);
        }

        return blockStmtDTOs;
    }

    /**
     * @param cycleBlock
     * @return
     */
    private List<BlockStmtDTO> parseCycleBlock(Statement cycleBlock) {

        List<Node> children = cycleBlock.getChildNodes();
        List<BlockStmtDTO> result = new ArrayList<>();

        for (Node blockChild : children) {
            if (blockChild instanceof SwitchEntry) {
                return this.parseSwitch(cycleBlock);
            }
            if (!(blockChild instanceof BlockStmt)) continue;
            //blockChild instanceof catch учитывать ли?

            NodeList<Statement> statements = ((BlockStmt) blockChild).getStatements();

            for (Statement s : statements) {
                BlockStmtDTO childStmtDTO = this.parseStatement(s);

                result.add(childStmtDTO);
            }
        }


        return result;
    }

    private BlockStmtDTO parseStatement(Statement s) {
        String name = "";
        String type = "";
        String value = "";
        List<BlockStmtDTO> blockStmtsInBlockStmts = new ArrayList<>();

        if (s instanceof ExpressionStmt && s.getChildNodes().size() == 1) {
            Node child = s.getChildNodes().get(0);
            if (child instanceof VariableDeclarationExpr || child instanceof BooleanLiteralExpr || child instanceof UnaryExpr || child instanceof AssignExpr) {
                type = "VARIABLE";
            } else if (child instanceof MethodCallExpr) {
                type = "METHOD";
            } else if (child instanceof BinaryExpr || child instanceof SwitchStmt || child instanceof IfStmt) {
                type = "BINARY";
            } else {
                type = "other";
            }
        } else {
            if (s instanceof ThrowStmt) {
                type = "THROW";
            } else if (s instanceof ForStmt
                    || s instanceof WhileStmt
                    || s instanceof DoStmt
                    || s instanceof ForEachStmt
                    || s instanceof TryStmt) {
                type = "BLOCK";

                blockStmtsInBlockStmts = this.parseCycleBlock(s);
            } else if (s instanceof IfStmt) {
                type = "IF";

                blockStmtsInBlockStmts = this.parseIf(s);
            } else if (s instanceof SwitchStmt) {
                type = "SWITCH";

                blockStmtsInBlockStmts = this.parseSwitch(s);
            } else if (s instanceof ReturnStmt) {
                type = "RETURN";
            } else if (s instanceof BreakStmt) {
                type = "BREAK";
            } else if (s instanceof ContinueStmt) {
                type = "CONTINUE";
            } else { //emptySTMT
                type = "other 3";
            }

            value = s.toString();
        }

        BlockStmtDTO childStmtDTO = new BlockStmtDTO(type, name, value);
        childStmtDTO.setBlockStmts(blockStmtsInBlockStmts);

        return childStmtDTO;
    }

    private List<BlockStmtDTO> parseSwitch(Statement cycleBlock) {
        List<BlockStmtDTO> result = new ArrayList<>();

        for (Node blockChild : cycleBlock.getChildNodes()) {
            if (blockChild instanceof SwitchEntry) {
                BlockStmtDTO childStmtDTO = new BlockStmtDTO("BLOCK", "", "");
                List<BlockStmtDTO> blockStmtsInBlockStmts = new ArrayList<>();

                for (Statement s : ((SwitchEntry) blockChild).getStatements()) {
                    blockStmtsInBlockStmts.add(this.parseStatement(s));

                }

                childStmtDTO.setBlockStmts(blockStmtsInBlockStmts);

                result.add(childStmtDTO);
            }
        }

        return result;
    }

    private List<BlockStmtDTO> parseIf(Statement cycleBlock) {
        List<BlockStmtDTO> result = new ArrayList<>();

        for (Node blockChild : cycleBlock.getChildNodes()) {
            if (blockChild instanceof BlockStmt) {
                BlockStmtDTO ifBlock = new BlockStmtDTO("BLOCK", "", blockChild.toString());

                for (Statement s : ((BlockStmt) blockChild).getStatements()) {
                    ifBlock.addBlockStmt(this.parseStatement(s));
                }

                result.add(ifBlock);
            }
        }

        //если просто if {} else {}, то в result уже будет 2 блока
        if (result.size() < 2) {
            Optional<Statement> s = ((IfStmt) cycleBlock).getElseStmt();

            if (s.isPresent() && s.get() instanceof IfStmt) {
                List<BlockStmtDTO> elseIfBLocks = this.parseIf(s.get());

                result.addAll(elseIfBLocks);
            }
        }

        return result;
    }
}
