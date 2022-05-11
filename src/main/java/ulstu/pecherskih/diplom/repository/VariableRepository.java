package ulstu.pecherskih.diplom.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import ulstu.pecherskih.diplom.model.VariableNode;

public interface VariableRepository extends Neo4jRepository<VariableNode, Long> {

}