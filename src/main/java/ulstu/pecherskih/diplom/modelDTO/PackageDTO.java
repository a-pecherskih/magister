package ulstu.pecherskih.diplom.modelDTO;

import java.util.ArrayList;
import java.util.List;

public class PackageDTO {
    private String name;

    private List<PackageDTO> packages;

    private List<ClassDTO> classes;

    public PackageDTO(String name) {
        this.name = name;

        this.packages = new ArrayList<>();
        this.classes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ClassDTO> getClasses() {
        return classes;
    }

    public void addClass(ClassDTO classDTO) {
        this.classes.add(classDTO);
    }

    public List<PackageDTO> getPackages() {
        return packages;
    }

    public void addPackage(PackageDTO packageDTO) {
        this.packages.add(packageDTO);
    }
}
