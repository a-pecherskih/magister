package ulstu.pecherskih.diplom.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("MethodArgumentNode")
public class MethodArgumentNode {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String type;

    @Relationship(type = "HAS_ARG", direction = Relationship.Direction.OUTGOING)
    private MethodNode methodNode;

    public MethodArgumentNode() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMethod(MethodNode methodNode) {
        this.methodNode = methodNode;
    }
}
