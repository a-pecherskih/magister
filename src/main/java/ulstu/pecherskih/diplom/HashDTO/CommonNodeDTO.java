package ulstu.pecherskih.diplom.HashDTO;

import java.util.ArrayList;
import java.util.List;

public class CommonNodeDTO {
    Integer id;
    Integer numberOfMatches;
    Integer countNodes;
    List<NodeDiffDTO> commonNodeDTOs;
    Integer percent;

    public CommonNodeDTO() {
        commonNodeDTOs = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumberOfMatches() {
        return numberOfMatches;
    }

    public void setNumberOfMatches(Integer numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

    public List<NodeDiffDTO> getCommonNodeDTOs() {
        return commonNodeDTOs;
    }

    public void setCommonNodeDTOs(List<NodeDiffDTO> commonNodeDTOs) {
        this.commonNodeDTOs = commonNodeDTOs;
    }

    public void addCommonNodeDTO(NodeDiffDTO commonNodeDTO) {
        this.commonNodeDTOs.add(commonNodeDTO);
    }

    public Integer getCountNodes() {
        return countNodes;
    }

    public void setCountNodes(Integer countNodes) {
        this.countNodes = countNodes;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }
}
