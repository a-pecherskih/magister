package ulstu.pecherskih.diplom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.modelDTO.FileInfoDTO;
import ulstu.pecherskih.diplom.modelDTO.PackageDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ParserService {

    @Autowired
    JavaParserService javaParserService;
    @Autowired
    GraphService graphService;

    public ParserService(JavaParserService javaParserService, GraphService graphService) {
        this.javaParserService = javaParserService;
        this.graphService = graphService;
    }

    public Long parseProject(String path) throws FileNotFoundException {
        FileInfoDTO project = this.parser(path);

        PackageDTO packageDTO = this.javaParserService.fillProject(project);

        if (packageDTO != null) {
            return this.graphService.buildGraph(packageDTO);
        }

        return 0L;
    }

    private FileInfoDTO parser(String path) throws FileNotFoundException {
        File dir = new File(path);

        /**
         * @TODO ошибку что директории с проектом нет
         */
        if (!dir.exists()) {
            System.out.println("Проект не существует по заданному пути!");
            return null;
        }

        //находим директорию src - отсюда начнется поиск директории с .java файлом
        File src = this.findSrc(dir);
        /**
         * @TODO ошибка, что src директории нет в проекте
         */
        if (src == null) {
            System.out.println("Директория src не найдена");
            return null;
        }
        File startDirectory = this.findStartDir(src);

        /**
         * @TODO ошибка, что корневой директории нет
         */
        if (startDirectory == null) {
            System.out.println("Корневая директория не найдена");
            return null;
        }

        File startDirectoryFile = new File(startDirectory.getPath());
        FileInfoDTO startDirectoryInfo = new FileInfoDTO(startDirectoryFile);
        this.explore(startDirectory, startDirectoryInfo);

        return startDirectoryInfo;
    }

    /**
     * Поиск директории src
     * @param file
     * @return
     */
    private File findSrc(File file) {
        String find = "src";

        if (file.getName().equals(find)) {
            return file;
        }

        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                File src = this.findSrc(f);
                if (src != null)
                    return src;
            }
        }

        return null;
    }

    /**
     * Есть ли в диреткории java файл
     * @param file
     * @return
     */
    private boolean existJavaClass(File file) {
        for (File f : file.listFiles()) {
            if (f.getPath().endsWith(".java")) {
                return true;
            }
        }

        return false;
    }

    /**
     * Поиск корневой директории
     * @param parent
     * @return
     */
    private File findStartDir(File parent) {
        List<File> childrenExistJava = new ArrayList<File>(); //список директорий с java файлом
        List<File> children = new ArrayList<File>(); //список всех директорий

        for (File child : parent.listFiles()) {
            //пропускаем директории test | resources
            if (child.getName().equals("test") || child.getName().equals("resources")) {
                continue;
            }

            if (child.isDirectory()) {
                children.add(child);

                if (this.existJavaClass(child)) {
                    childrenExistJava.add(child);
                }
            }
        }

        if (childrenExistJava.size() == 1) {
            return childrenExistJava.get(0);
        } else if (childrenExistJava.size() > 1) {
            return parent;
        } else if (children.size() > 0) {
            return this.findStartDir(children.get(0));
        }

        return null;
    }


    /**
     *
     * @param file
     * @param parent
     * @throws FileNotFoundException
     */
    private void explore(File file, FileInfoDTO parent) throws FileNotFoundException {
        FileInfoDTO current;

        if (file.getPath().equals(parent.getMe().getPath())) {
            current = parent;
        } else {
            current = new FileInfoDTO(file, parent);
        }

        if (file.isDirectory() && !file.isHidden()) {
            if (!file.getPath().equals(parent.getMe().getPath())) {
                parent.addChild(current);
            }
            for (File child : file.listFiles()) {
                explore(child, current);
            }
        } else if (current.isJavaFile()) {
            if (file != parent.getMe()) {
                parent.addChild(current);
            }
        }
    }
}

