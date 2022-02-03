package ulstu.pecherskih.diplom.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import ulstu.pecherskih.diplom.model.ClassFieldNode;
import ulstu.pecherskih.diplom.model.MethodArgumentNode;

public interface ClassFieldRepository extends Neo4jRepository<ClassFieldNode, Long> {

}