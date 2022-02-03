package ulstu.pecherskih.diplom.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import ulstu.pecherskih.diplom.model.MethodArgumentNode;

public interface MethodArgumentRepository extends Neo4jRepository<MethodArgumentNode, Long> {

}