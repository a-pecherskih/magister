package ulstu.pecherskih.diplom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ulstu.pecherskih.diplom.model.ClassNode;
import ulstu.pecherskih.diplom.model.PackageNode;
import ulstu.pecherskih.diplom.modelDTO.FileInfoDTO;
import ulstu.pecherskih.diplom.modelDTO.PackageDTO;
import ulstu.pecherskih.diplom.service.GraphService;
import ulstu.pecherskih.diplom.service.JavaParserService;
import ulstu.pecherskih.diplom.service.ParserService;
import ulstu.pecherskih.diplom.service.model.ClassService;
import ulstu.pecherskih.diplom.service.HashService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

@RestController
public class ApiController {

    @Autowired
    ClassService classService;
    @Autowired
    ParserService parserService;
    @Autowired
    GraphService graphService;
    @Autowired
    JavaParserService javaParserService;
    @Autowired
    HashService hashService;

    @RequestMapping("/classes")
    public Collection<ClassNode> getAll() {
        return classService.getAll();
    }

    @RequestMapping("/delete")
    public void delete() throws IOException {
        this.graphService.deleteAll();//удаляем все из базы и файлы
    }

    @RequestMapping("/pars")
    public void pars() throws IOException {
        Long id = this.parseProject("D:\\0_Магистратура\\курсач\\test3");

        this.hashService.checkGraph(id);
    }

    @RequestMapping("/pars2")
    public void pars2() throws IOException {
        Long id = this.parseProject("D:\\0_Магистратура\\курсач\\test4");

        this.hashService.checkGraph(id);
    }

    @RequestMapping("/pars3")
    public void pars3() throws IOException {
        Long id = this.parseProject("D:\\0_Магистратура\\курсач\\test2");

        this.hashService.checkGraph(id);
    }

    @RequestMapping("/compare")
    public void compare() throws IOException {
        Collection<PackageNode> packages = this.graphService.getRootPackages();

        for(PackageNode packageNode: packages) {
            this.hashService.compareProjects(packageNode.getId());
        }
    }

    private Long parseProject(String path) throws FileNotFoundException {
        FileInfoDTO project = this.parserService.parser(path);

        PackageDTO packageDTO = javaParserService.fillProject(project);

        if (packageDTO != null) {
            return this.graphService.buildGraph(packageDTO);
        }

        return 0L;
    }
}
