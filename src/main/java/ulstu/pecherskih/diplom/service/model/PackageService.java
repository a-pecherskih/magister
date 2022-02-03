package ulstu.pecherskih.diplom.service.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.model.PackageNode;
import ulstu.pecherskih.diplom.modelDTO.PackageDTO;
import ulstu.pecherskih.diplom.repository.PackageRepository;

@Service
public class PackageService {

    @Autowired
    PackageRepository packageRepository;

    public PackageNode save(PackageDTO packageDTO) {
        PackageNode me = new PackageNode(packageDTO.getName());

        return this.packageRepository.save(me);
    }

    public PackageNode save(PackageDTO packageDTO, PackageNode parentPackageNode) {
        PackageNode me = new PackageNode(packageDTO.getName());

        me.setParentPackage(parentPackageNode);

        return this.packageRepository.save(me);
    }
}
