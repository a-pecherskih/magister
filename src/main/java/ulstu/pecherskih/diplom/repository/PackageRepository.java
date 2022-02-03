package ulstu.pecherskih.diplom.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import ulstu.pecherskih.diplom.model.PackageNode;

public interface PackageRepository extends Neo4jRepository<PackageNode, Long> {

}