package ulstu.pecherskih.diplom.service.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.model.MethodNode;
import ulstu.pecherskih.diplom.model.MethodArgumentNode;
import ulstu.pecherskih.diplom.modelDTO.MethodArgumentDTO;
import ulstu.pecherskih.diplom.repository.MethodArgumentRepository;

@Service
public class MethodArgumentService {

    @Autowired
    MethodArgumentRepository methodArgumentRepository;

    public MethodArgumentNode save(MethodArgumentDTO methodArgumentDTO, MethodNode parentMethod) {
        MethodArgumentNode me = new MethodArgumentNode();
        me.setName(methodArgumentDTO.getName());
        me.setType(methodArgumentDTO.getType());

        me.setMethod(parentMethod);

        return methodArgumentRepository.save(me);
    }
}
