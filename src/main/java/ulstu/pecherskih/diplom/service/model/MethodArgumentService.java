package ulstu.pecherskih.diplom.service.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.model.MethodNode;
import ulstu.pecherskih.diplom.model.MethodArgumentNode;
import ulstu.pecherskih.diplom.model.VariableNode;
import ulstu.pecherskih.diplom.modelDTO.MethodArgumentDTO;
import ulstu.pecherskih.diplom.repository.MethodArgumentRepository;
import ulstu.pecherskih.diplom.repository.VariableRepository;

@Service
public class MethodArgumentService {

    @Autowired
    VariableRepository variableRepository;

    public VariableNode save(MethodArgumentDTO methodArgumentDTO, MethodNode parentMethod) {
        VariableNode me = new VariableNode();
        me.setName(methodArgumentDTO.getName());
        me.setType(methodArgumentDTO.getType());

        me.setMethod(parentMethod);

        return variableRepository.save(me);
    }
}
