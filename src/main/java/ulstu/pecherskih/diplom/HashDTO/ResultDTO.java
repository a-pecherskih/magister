package ulstu.pecherskih.diplom.HashDTO;


import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.List;

@Node("ResultDTO")
public class ResultDTO {

    @Id
    @GeneratedValue
    private Long id;
    private String hash;
    private List<String> names;
    private List<Integer> rootIds;
    private Integer countNodes;

    public ResultDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public List<Integer> getRootIds() {
        return rootIds;
    }

    public void setRootIds(List<Integer> rootIds) {
        this.rootIds = rootIds;
    }

    public Integer getCountNodes() {
        return countNodes;
    }

    public void setCountNodes(Integer countNodes) {
        this.countNodes = countNodes;
    }
}
