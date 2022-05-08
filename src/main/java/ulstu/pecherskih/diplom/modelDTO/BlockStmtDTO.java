package ulstu.pecherskih.diplom.modelDTO;

import java.util.ArrayList;
import java.util.List;

public class BlockStmtDTO {
    String type;

    String name;

    String value;

    private List<BlockStmtDTO> blockStmts;

    public BlockStmtDTO() {
    }

    public BlockStmtDTO(String type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;

        this.blockStmts = new ArrayList<>();
    }

    public List<BlockStmtDTO> getBlockStmts() {
        return blockStmts;
    }

    public void setBlockStmts(List<BlockStmtDTO> blockStmts) {
        this.blockStmts = blockStmts;
    }

    public void addBlockStmt(BlockStmtDTO blockStmt) {
        this.blockStmts.add(blockStmt);
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
