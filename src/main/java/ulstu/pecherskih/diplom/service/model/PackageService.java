package ulstu.pecherskih.diplom.service.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.model.PackageNode;
import ulstu.pecherskih.diplom.modelDTO.PackageDTO;
import ulstu.pecherskih.diplom.repository.PackageRepository;

import java.util.Collection;

@Service
public class PackageService {

    @Autowired
    PackageRepository packageRepository;

    /**
     * Save root package
     *
     * @param packageDTO
     * @return
     */
    public PackageNode save(PackageDTO packageDTO) {
        PackageNode me = new PackageNode("root");

        return this.packageRepository.save(me);
    }

    /**
     * Save package
     *
     * @param packageDTO
     * @param parentPackageNode
     * @return
     */
    public PackageNode save(PackageDTO packageDTO, PackageNode parentPackageNode) {
        PackageNode me = new PackageNode(packageDTO.getName());

        me.setParentPackage(parentPackageNode);

        return this.packageRepository.save(me);
    }

    public Collection<PackageNode> getRootPackages() {
        return packageRepository.getRootPackages();
    }

    public void deleteAll() {
        packageRepository.deleteAll();
    }
}
