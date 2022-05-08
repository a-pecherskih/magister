package ulstu.pecherskih.diplom.service.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.model.*;
import ulstu.pecherskih.diplom.modelDTO.ClassFieldDTO;
import ulstu.pecherskih.diplom.repository.VariableRepository;

@Service
public class VariableService {

    @Autowired
    VariableRepository variableRepository;

    public VariableNode save(ClassFieldDTO classFieldDTO, ClassNode parentClass) {
        VariableNode me = new VariableNode();
        me.setName(classFieldDTO.getName());
        me.setType(classFieldDTO.getType());

        me.setClass(parentClass);

        return variableRepository.save(me);
    }
}
