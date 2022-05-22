package ulstu.pecherskih.diplom.HashDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeDiffDTO {
    Integer rootId;
    Map<String, List<Integer>> equalsPaths;
    Map<String, List<Integer>> diffPaths;

    public NodeDiffDTO() {
        this.equalsPaths = new HashMap<>();
        this.diffPaths = new HashMap<>();
    }

    public Integer getRootId() {
        return rootId;
    }

    public void setRootId(Integer rootId) {
        this.rootId = rootId;
    }

    public Map<String, List<Integer>> addEqualsPath(String hash, List<Integer> pathIds) {
        return (Map<String, List<Integer>>) equalsPaths.put(hash, pathIds);
    }

    public Map<String, List<Integer>> getEqualsPaths() {
        return equalsPaths;
    }

    public void setEqualsPaths(Map<String, List<Integer>> equalsPaths) {
        this.equalsPaths = equalsPaths;
    }

    public Map<String, List<Integer>> addDiffPath(String hash, List<Integer> pathIds) {
        return (Map<String, List<Integer>>) diffPaths.put(hash, pathIds);
    }

    public Map<String, List<Integer>> getDiffPaths() {
        return diffPaths;
    }

    public void setDiffPaths(Map<String, List<Integer>> diffPaths) {
        this.diffPaths = diffPaths;
    }
}
