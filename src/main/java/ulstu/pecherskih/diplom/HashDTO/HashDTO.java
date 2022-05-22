package ulstu.pecherskih.diplom.HashDTO;

import java.util.List;

public class HashDTO {
    Integer rootId;
    String hash;
    List<Integer> pathIds;
    Integer countEquals;

    public Integer getRootId() {
        return rootId;
    }

    public void setRootId(Integer rootId) {
        this.rootId = rootId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public List<Integer> getPathIds() {
        return pathIds;
    }

    public void setPathIds(List<Integer> pathIds) {
        this.pathIds = pathIds;
    }

    public Integer getCountEquals() {
        return countEquals;
    }

    public void addCountEquals() {
        this.countEquals  += 1;
    }
}
