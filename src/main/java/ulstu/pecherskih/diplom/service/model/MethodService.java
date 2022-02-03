package ulstu.pecherskih.diplom.service.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.model.ClassNode;
import ulstu.pecherskih.diplom.model.MethodNode;
import ulstu.pecherskih.diplom.modelDTO.MethodDTO;
import ulstu.pecherskih.diplom.repository.MethodRepository;

@Service
public class MethodService {

    @Autowired
    MethodRepository methodRepository;

    public MethodNode save(MethodDTO methodDTO, ClassNode parentClass) {
        MethodNode me = new MethodNode();
        me.setName(methodDTO.getName());
        me.setReturnType(methodDTO.getReturnType());

        me.setClass(parentClass);

        return methodRepository.save(me);
    }
}
