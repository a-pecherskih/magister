package ulstu.pecherskih.diplom.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("VariableNode")
public class VariableNode {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String type;
    private String value;

    @Relationship(type = "VAR", direction = Relationship.Direction.OUTGOING)
    private ClassNode classNode;
    @Relationship(type = "VAR", direction = Relationship.Direction.OUTGOING)
    private MethodNode methodNode;

    public VariableNode() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setMethod(MethodNode methodNode) {
        this.methodNode = methodNode;
    }

    public void setClass(ClassNode classNode) {
        this.classNode = classNode;
    }
}
