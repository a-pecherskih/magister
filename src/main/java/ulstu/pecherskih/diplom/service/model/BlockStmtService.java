package ulstu.pecherskih.diplom.service.model;

import com.github.javaparser.ast.body.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.model.BlockStmtNode;
import ulstu.pecherskih.diplom.model.MethodArgumentNode;
import ulstu.pecherskih.diplom.model.MethodNode;
import ulstu.pecherskih.diplom.modelDTO.BlockStmtDTO;
import ulstu.pecherskih.diplom.modelDTO.MethodArgumentDTO;
import ulstu.pecherskih.diplom.repository.BlockStmtRepository;
import ulstu.pecherskih.diplom.repository.MethodArgumentRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BlockStmtService {

    @Autowired
    BlockStmtRepository blockStmtRepository;

    public BlockStmtNode save(BlockStmtDTO blockStmtDTO, MethodNode parentMethod) {
        BlockStmtNode me = new BlockStmtNode();
//        me.setName(blockStmtDTO.getName());
        me.setType(blockStmtDTO.getType());
        me.setValue(blockStmtDTO.getValue());

        me.setMethod(parentMethod);

        me.setBlockStmt(this.saveChildren(blockStmtDTO));

        return blockStmtRepository.save(me);
    }

    private Set<BlockStmtNode> saveChildren(BlockStmtDTO blockStmtDTO) {
        Set<BlockStmtNode> children = new HashSet<>();

        if (blockStmtDTO.getBlockStmts().size() > 0) {
            for (BlockStmtDTO child : blockStmtDTO.getBlockStmts()) {
                BlockStmtNode blockStmtNode = new BlockStmtNode();
                blockStmtNode.setType(child.getType());

                if (child.getBlockStmts().size() > 0) {
                    blockStmtNode.setBlockStmt(this.saveChildren(child));
                }

                children.add(blockStmtNode);
            }
        }

        return children;
    }
}
