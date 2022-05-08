package ulstu.pecherskih.diplom.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node("BlockStmtNode")
public class BlockStmtNode {

    @Id @GeneratedValue
    private Long id;

//    private String name;
    private String type;
    private String value;

    @Relationship(type = "BLOCK-METHOD", direction = Relationship.Direction.OUTGOING)
    private MethodNode methodNode;

    @Relationship(type = "BLOCK-BODY", direction = Relationship.Direction.INCOMING)
    private Set<BlockStmtNode> blockStmts;

    public BlockStmtNode() {
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setMethod(MethodNode methodNode) {
        this.methodNode = methodNode;
    }

    public void addBlockStmt(BlockStmtNode blockStmtNode) {
        if (this.blockStmts == null) {
            this.blockStmts = new HashSet<>();
        }

        this.blockStmts.add(blockStmtNode);
    }

    public void setBlockStmt(Set<BlockStmtNode> blockStmtNodes) {
        this.blockStmts = blockStmtNodes;
    }
}
