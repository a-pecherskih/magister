package ulstu.pecherskih.diplom.model;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("PackageNode")
public class PackageNode {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @Relationship(type = "RATED", direction = Relationship.Direction.OUTGOING)
    private PackageNode packageParent;

    public PackageNode() {
    }

    public PackageNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentPackage(PackageNode packageNode) {
        this.packageParent = packageNode;
    }
}
