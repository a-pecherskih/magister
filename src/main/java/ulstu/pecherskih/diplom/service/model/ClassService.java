package ulstu.pecherskih.diplom.service.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.model.ClassNode;
import ulstu.pecherskih.diplom.model.PackageNode;
import ulstu.pecherskih.diplom.modelDTO.ClassDTO;
import ulstu.pecherskih.diplom.modelDTO.PackageDTO;
import ulstu.pecherskih.diplom.repository.ClassRepository;

import java.util.Collection;

@Service
public class ClassService {

    @Autowired
    ClassRepository classRepository;

    @Autowired
    PackageService packageService;

    public Collection<ClassNode> getAll() {
        return classRepository.getAllMyClasses();
    }

    public ClassNode save(ClassDTO classDTO, PackageNode parentPackageNode) {
        ClassNode classNode = new ClassNode();
        classNode.setName(classDTO.getName());
        classNode.setPackage(parentPackageNode);

        return classRepository.save(classNode);
    }
}
