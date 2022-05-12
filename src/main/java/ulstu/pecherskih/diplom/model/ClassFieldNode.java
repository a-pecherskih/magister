package ulstu.pecherskih.diplom.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("ClassFieldNode")
public class ClassFieldNode {


    @Id @GeneratedValue
    private Long id;

    private String name;
    private String type;
    private String modifier;

    @Relationship(type = "HAS_FIELD", direction = Relationship.Direction.OUTGOING)
    private ClassNode classNode;

    public ClassFieldNode() {
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

    public void setType(String type) {
        this.type = type;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
}
