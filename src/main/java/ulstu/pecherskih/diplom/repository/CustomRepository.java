package ulstu.pecherskih.diplom.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import ulstu.pecherskih.diplom.HashDTO.ResultDTO;

import java.util.Collection;

public interface CustomRepository extends Neo4jRepository<ResultDTO, Long> {

    @Query("MATCH p=(o{name:\"root\"})-[r*]-()\n" +
            "WHERE ID(o)={0}\n" +
            "WITH [x in nodes(p) | CASE WHEN EXISTS(x.name) THEN x.name ELSE x.type END] as names\n" +
            "WITH names,  apoc.util.md5(names) as hash\n" +
            "WITH names, hash\n" +
            "WITH {hash: hash} as ResultDTO\n" +
            "RETURN ResultDTO")
    Collection<ResultDTO> getPathHashFromRoot(Long id);

    @Query("MATCH p=(o{name:\"root\"})-[r*]-()\n" +
            "WHERE ID(o)={0}\n" +
            "WITH [x in nodes(p) | CASE WHEN EXISTS(x.name) THEN x.name ELSE x.type END] as names\n" +
            "WITH apoc.util.md5(names) as hash ORDER BY hash \n" +
            "WITH apoc.util.md5(collect(hash)) as hash\n" +
            "WITH {hash: hash} as ResultDTO\n" +
            "RETURN ResultDTO")
    ResultDTO getHashGraph(Long id);

    @Query("MATCH p=(o{name:\"root\"})-[r*]-()\n" +
            "WHERE ID(o)={0}\n" +
            "WITH [x in nodes(p) | CASE WHEN EXISTS(x.name) THEN x.name ELSE x.type END] as names\n" +
            "WITH count(names) as cnt\n" +
            "WITH {countNodes: cnt} as ResultDTO\n" +
            "RETURN ResultDTO")
    ResultDTO getCountNodes(Long id);
}