package ulstu.pecherskih.diplom.service.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.model.BlockStmtNode;
import ulstu.pecherskih.diplom.model.MethodArgumentNode;
import ulstu.pecherskih.diplom.model.MethodNode;
import ulstu.pecherskih.diplom.modelDTO.BlockStmtDTO;
import ulstu.pecherskih.diplom.modelDTO.MethodArgumentDTO;
import ulstu.pecherskih.diplom.repository.BlockStmtRepository;
import ulstu.pecherskih.diplom.repository.MethodArgumentRepository;

@Service
public class BlockStmtService {

    @Autowired
    BlockStmtRepository blockStmtRepository;

    public BlockStmtNode save(BlockStmtDTO blockStmtDTO, MethodNode parentMethod) {
        BlockStmtNode me = new BlockStmtNode();
        me.setName(blockStmtDTO.getName());
        me.setType(blockStmtDTO.getType());
        me.setValue(blockStmtDTO.getValue());

        me.setMethod(parentMethod);

        return blockStmtRepository.save(me);
    }
}
