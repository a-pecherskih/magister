package ulstu.pecherskih.diplom.modelDTO;

public class ClassFieldDTO {
    private String name;

    private String type;

    private String modifier;

    public ClassFieldDTO(String name, String type, String modifier) {
        this.name = name;
        this.type = type;
        this.modifier = modifier;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getModifier() {
        return modifier;
    }
}
