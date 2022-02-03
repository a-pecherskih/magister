package ulstu.pecherskih.diplom.service.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.model.ClassFieldNode;
import ulstu.pecherskih.diplom.model.ClassNode;
import ulstu.pecherskih.diplom.model.MethodArgumentNode;
import ulstu.pecherskih.diplom.model.MethodNode;
import ulstu.pecherskih.diplom.modelDTO.ClassFieldDTO;
import ulstu.pecherskih.diplom.repository.ClassFieldRepository;
import ulstu.pecherskih.diplom.repository.MethodArgumentRepository;

@Service
public class ClassFieldService {

    @Autowired
    ClassFieldRepository classFieldRepository;

    public ClassFieldNode save(ClassFieldDTO classFieldDTO, ClassNode parentClass) {
        ClassFieldNode me = new ClassFieldNode();
        me.setName(classFieldDTO.getName());
        me.setType(classFieldDTO.getType());
        me.setModifier(classFieldDTO.getModifier());

        me.setClass(parentClass);

        return classFieldRepository.save(me);
    }
}
