package ulstu.pecherskih.diplom.modelDTO;

import java.util.ArrayList;
import java.util.List;

public class MethodDTO {

    private String name;

    private String returnType;

    private List<MethodArgumentDTO> arguments;
    private List<BlockStmtDTO> blockStmts;

    public MethodDTO(String name, String returnType) {
        this.name = name;
        this.returnType = returnType;

        this.arguments = new ArrayList<>();
        this.blockStmts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addArgument(MethodArgumentDTO argument) {
        this.arguments.add(argument);
    }

    public void setArguments(List<MethodArgumentDTO> arguments) {
        this.arguments = arguments;
    }

    public void setBlockStmts(List<BlockStmtDTO> blockStmts) {
        this.blockStmts = blockStmts;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<MethodArgumentDTO> getArguments() {
        return arguments;
    }

    public List<BlockStmtDTO> getBlockStmts() {
        return blockStmts;
    }
}
