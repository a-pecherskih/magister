package ulstu.pecherskih.diplom.modelDTO;

import java.util.ArrayList;
import java.util.List;

public class ClassDTO {
    private String name;

    private List<ClassFieldDTO> fields;

    private List<MethodDTO> methods;

    public ClassDTO(String name) {
        this.name = name;

        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<ClassFieldDTO> getFields() {
        return fields;
    }

    public List<MethodDTO> getMethods() {
        return methods;
    }

    public void addMethod(MethodDTO method) {
        this.methods.add(method);
    }

    public void addField(ClassFieldDTO field) {
        this.fields.add(field);
    }

    public void setFields(List<ClassFieldDTO> fields) {
        this.fields = fields;
    }

    public void setMethods(List<MethodDTO> methods) {
        this.methods = methods;
    }
}
