package ulstu.pecherskih.diplom.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import ulstu.pecherskih.diplom.model.BlockStmtNode;
import ulstu.pecherskih.diplom.model.MethodArgumentNode;

public interface BlockStmtRepository extends Neo4jRepository<BlockStmtNode, Long> {

}