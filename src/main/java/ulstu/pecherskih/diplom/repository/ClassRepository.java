package ulstu.pecherskih.diplom.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import ulstu.pecherskih.diplom.model.ClassNode;

import java.util.Collection;

public interface ClassRepository extends Neo4jRepository<ClassNode, Long> {

//    @Query("MATCH (u:MyClass)<-[r:RATED]-(m:MyMethod) RETURN u,r,m")
    @Query("MATCH (n:MyClass) RETURN n LIMIT 25")
    Collection<ClassNode> getAllMyClasses();
}

// MATCH (n) DETACH DELETE n - все удалить
// MATCH (n:MyClass) RETURN n LIMIT 25 - получить только MyClass
// CREATE (n:MyClass {name: 'FirstClass'}) - создать MyClass
// CREATE (MyMethod)-[:RATED {rating: 9}]->(MyClass) - отношение