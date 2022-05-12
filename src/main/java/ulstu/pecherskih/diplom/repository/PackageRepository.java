package ulstu.pecherskih.diplom.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import ulstu.pecherskih.diplom.model.PackageNode;

import java.util.*;

public interface PackageRepository extends Neo4jRepository<PackageNode, Long> {

    @Query("MATCH (p:PackageNode {name:'root'}) RETURN p")
    Collection<PackageNode> getRootPackages();

    @Query("MATCH p=(root{name:\"root\"})-[r*]-(other)\n" +
            "WHERE ID(root)={0}\n" +
            "DETACH DELETE root, other")
    void deletePackage(Long id);

    @Query("MATCH (n) DETACH DELETE n")
    void deleteAll();
}