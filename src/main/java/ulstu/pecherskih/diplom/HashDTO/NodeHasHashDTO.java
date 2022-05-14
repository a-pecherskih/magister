package ulstu.pecherskih.diplom.HashDTO;

import java.util.List;

public class NodeHasHashDTO {
    Integer id;
    Integer countEquals;
    List<Integer> ids;

    public NodeHasHashDTO() {
        this.countEquals = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public Integer getCountEquals() {
        return countEquals;
    }

    public void setCountEquals(Integer countEquals) {
        this.countEquals = countEquals;
    }
}
