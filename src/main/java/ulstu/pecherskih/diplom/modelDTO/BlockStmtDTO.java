package ulstu.pecherskih.diplom.modelDTO;

public class BlockStmtDTO {
    String type;

    String name;

    String value;

    public BlockStmtDTO(String type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
