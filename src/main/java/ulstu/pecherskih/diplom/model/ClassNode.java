package ulstu.pecherskih.diplom.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

import java.util.HashSet;
import java.util.Set;

@Node("ClassNode")
public class ClassNode {
    @Id @GeneratedValue
    private Long id;

    private String name;

    @Relationship(type = "HAS_FIELD", direction = Direction.INCOMING)
    private Set<ClassFieldNode> fields;

    @Relationship(type = "HAS_METHOD", direction = Direction.INCOMING)
    private Set<MethodNode> methods;

    @Relationship(type = "HAS_CLASS", direction = Direction.OUTGOING)
    private PackageNode packageNode;

    public ClassNode() {
    }

    public Set<MethodNode> getMethods() {
        return methods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMethod(MethodNode methodNode) {
        if (this.methods == null) {
            this.methods = new HashSet<>();
        }

        this.methods.add(methodNode);
    }

    public void setPackage(PackageNode packageNode) {
        this.packageNode = packageNode;
    }
}
