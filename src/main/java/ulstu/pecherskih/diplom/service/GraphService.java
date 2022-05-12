package ulstu.pecherskih.diplom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.model.BlockStmtNode;
import ulstu.pecherskih.diplom.model.ClassNode;
import ulstu.pecherskih.diplom.model.MethodNode;
import ulstu.pecherskih.diplom.model.PackageNode;
import ulstu.pecherskih.diplom.modelDTO.*;
import ulstu.pecherskih.diplom.service.model.*;

import java.nio.file.Paths;
import java.util.Collection;

@Service
public class GraphService {

    @Autowired
    PackageService packageService;
    @Autowired
    ClassService classService;
    @Autowired
    VariableService classFieldService;
    @Autowired
    MethodService methodService;
    @Autowired
    MethodArgumentService methodArgumentService;
    @Autowired
    BlockStmtService blockStmtService;


    public Long buildGraph(PackageDTO mainPackageDTO) {
        PackageNode packageNode = packageService.save(mainPackageDTO);

        this.saveChildren(mainPackageDTO, packageNode);

        return packageNode.getId();
    }

    private void saveChildren(PackageDTO currentPackageDTO, PackageNode parentPackageNode) {
        for (PackageDTO childPackageDTO : currentPackageDTO.getPackages()) {
            PackageNode packageNode = packageService.save(childPackageDTO, parentPackageNode);

            this.saveChildren(childPackageDTO, packageNode);
        }
        for (ClassDTO childClassDTO : currentPackageDTO.getClasses()) {
            ClassNode classNode = classService.save(childClassDTO, parentPackageNode);

            for (MethodDTO methodDTO : childClassDTO.getMethods()) {
                MethodNode methodNode = methodService.save(methodDTO, classNode);

                for (MethodArgumentDTO methodArgumentDTO : methodDTO.getArguments()) {
                    methodArgumentService.save(methodArgumentDTO, methodNode);
                }
                for (BlockStmtDTO blockStmtDTO : methodDTO.getBlockStmts()) {
                    BlockStmtNode blockStmtNode = blockStmtService.save(blockStmtDTO, methodNode);
                }
            }
            for (ClassFieldDTO classFieldDTO : childClassDTO.getFields()) {
                classFieldService.save(classFieldDTO, classNode);
            }
        }
    }

    public Collection<PackageNode> getRootPackages() {
        return packageService.getRootPackages();
    }


    public void deleteAll() {
        this.packageService.deleteAll();
        Paths.get("rootHashes.json").toFile().delete();
        Paths.get("idsHashes.json").toFile().delete();
        Paths.get("result.json").toFile().delete();
    }
}
