package ulstu.pecherskih.diplom.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import ulstu.pecherskih.diplom.model.MethodNode;

public interface MethodRepository extends Neo4jRepository<MethodNode, Long> {

}