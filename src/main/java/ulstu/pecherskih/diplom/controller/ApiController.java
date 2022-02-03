package ulstu.pecherskih.diplom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ulstu.pecherskih.diplom.model.ClassNode;
import ulstu.pecherskih.diplom.modelDTO.FileInfoDTO;
import ulstu.pecherskih.diplom.modelDTO.PackageDTO;
import ulstu.pecherskih.diplom.service.GraphService;
import ulstu.pecherskih.diplom.service.JavaParserService;
import ulstu.pecherskih.diplom.service.model.ClassService;
import ulstu.pecherskih.diplom.service.ParserService;

import java.io.FileNotFoundException;
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

    @RequestMapping("/classes")
    public Collection<ClassNode> getAll() {
        return classService.getAll();
    }

    @RequestMapping("/pars")
    public void pars() throws FileNotFoundException {
        this.classService.deleteAll();//удаляем все из базы

        FileInfoDTO project = this.parserService.parser("D:/0_Магистратура/курсач/parscodemag");

        PackageDTO packageDTO = javaParserService.fillProject(project);

        if (packageDTO != null) {
            this.graphService.buildGraph(packageDTO);
        }
    }

//    @GetMapping("/getInfo/{name}")
//    public Customer getInfo(@PathVariable String name) {
//        return repository.findByName(name);
//    }

}
