package ulstu.pecherskih.diplom.modelDTO;

public class MethodArgumentDTO {

    private String name;

    private String type;

    public MethodArgumentDTO(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
