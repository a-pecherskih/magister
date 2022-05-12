package ulstu.pecherskih.diplom.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("MethodNode")
public class MethodNode {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String returnType;

    @Relationship(type = "HAS_METHOD", direction = Relationship.Direction.OUTGOING)
    private ClassNode classNode;

    public MethodNode() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClass(ClassNode classNode) {
        this.classNode = classNode;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
}
