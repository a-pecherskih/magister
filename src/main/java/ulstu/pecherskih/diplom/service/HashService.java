package ulstu.pecherskih.diplom.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.HashDTO.CommonNodeDTO;
import ulstu.pecherskih.diplom.HashDTO.ResultDTO;
import ulstu.pecherskih.diplom.repository.CustomRepository;
import ulstu.pecherskih.diplom.repository.PackageRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class HashService {

    @Autowired
    PackageRepository packageRepository;
    @Autowired
    CustomRepository customRepository;

    public void checkGraph(Long id) throws IOException {
        ResultDTO hashGraph = customRepository.getHashGraph(id);

        if (this.issetSameProject(hashGraph.getHash())) {
            packageRepository.deletePackage(id);
        }
    }

    public void compareProjects(Long id) throws IOException {
        this.saveHashes(id);
    }

    private boolean issetSameProject(String hash) throws IOException {
        List<String> hashes = new ArrayList<>();

        if (this.existFile("rootHashes.json")) {
            ObjectMapper mapper = new ObjectMapper();
            hashes = mapper.readValue(Paths.get("rootHashes.json").toFile(), List.class);

            if (hashes != null && hashes.contains(hash)) {
                return true;
            }
        }

        hashes.add(hash);

        ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        objectMapper.writeValue(Paths.get("rootHashes.json").toFile(), hashes);

        return false;
    }

    private boolean existFile(String path) {
        Path file = new File(path).toPath();

        return Files.exists(file);
    }

    public void saveHashes(Long id) throws IOException {
        Map<String, List<Integer>> allHashes = new HashMap<String, List<Integer>>();

        if (this.existFile("idsHashes.json")) {
            ObjectMapper mapper = new ObjectMapper();
            allHashes = mapper.readValue(Paths.get("idsHashes.json").toFile(), Map.class);

            if (allHashes != null && allHashes.size() > 0) {
                this.compareHashes(allHashes, id);
            }
            return;
        }

        this.saveHashesById(allHashes, id);
    }

    private void saveHashesById(Map<String, List<Integer>> allHashes, Long id) throws IOException {
        Collection<ResultDTO> hashesById = customRepository.getPathHashFromRoot(id);

        for (ResultDTO resultDTO : hashesById) {
            List<Integer> ids = allHashes.get(resultDTO.getHash());
            if (ids == null) {
                ids = new ArrayList<>();
            } else {
                if (ids.contains(this.longToInteger(id))) continue;
            }

            ids.add(this.longToInteger(id));
            allHashes.put(resultDTO.getHash(), ids);
        }

        ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        objectMapper.writeValue(Paths.get("idsHashes.json").toFile(), allHashes);
    }

    private Integer longToInteger(Long num) {
        return num.intValue();
    }

    private void compareHashes(Map<String, List<Integer>> allHashes, Long id) throws IOException {
        Map<Integer, Integer> counter = new HashMap<>();

        boolean hasHashInFile = false;
        for (Map.Entry<String, List<Integer>> entry : allHashes.entrySet()) {
            List<Integer> ids = entry.getValue();
            if (entry.getValue().contains(this.longToInteger(id))) {
                hasHashInFile = true;

                for (Integer otherId : ids) {
                    if (!otherId.equals(this.longToInteger(id))) {
                        Integer count = counter.get(otherId);
                        if (count == null) {
                            counter.put(otherId, 0);
                        } else {
                            counter.put(otherId, count + 1);
                        }
                    }
                }
            }
        }

        if (!hasHashInFile) {
            this.saveHashesById(allHashes, id);
            this.compareHashes(allHashes, id);
        } else if (counter.size() > 0) {
            this.writeResult2(counter, id);
        }
    }

    private void writeResult(Map<Integer, Integer> counter, Long id) throws IOException {
        Map<Integer, Map<Integer, Integer>> result = new HashMap<>();

        if (this.existFile("result.json")) {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(Paths.get("result.json").toFile(), Map.class);
        }

        Map<Integer, Integer> countCommonNodes = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : counter.entrySet()) {
            countCommonNodes.put(entry.getKey(), entry.getValue());
        }

        result.put(this.longToInteger(id), countCommonNodes);

        ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        objectMapper.writeValue(Paths.get("result.json").toFile(), result);
    }

    private void writeResult2(Map<Integer, Integer> counter, Long id) throws IOException {
        Integer countNodesById = customRepository.getCountNodes(id).getCountNodes();
        List<CommonNodeDTO> result = new ArrayList<>();

        CommonNodeDTO myNode = new CommonNodeDTO();
        myNode.setId(this.longToInteger(id));
        myNode.setCountNodes(countNodesById);

        if (this.existFile("result.json")) {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(Paths.get("result.json").toFile(), new TypeReference<List<CommonNodeDTO>>(){});

            if (result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).getId().equals(this.longToInteger(id))) {
                        result.remove(i);
                        break;
                    }
                }
            }
        }

        for (Map.Entry<Integer, Integer> entry : counter.entrySet()) {
            CommonNodeDTO relationNode = new CommonNodeDTO();
            relationNode.setId(entry.getKey());
            relationNode.setNumberOfMatches(entry.getValue());

            relationNode.setPercent(this.calcPecent(relationNode.getNumberOfMatches(), myNode.getCountNodes()));

            myNode.addCommonNodeDTO(relationNode);
        }

        result.add(myNode);

        ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        objectMapper.writeValue(Paths.get("result.json").toFile(), result);
    }

    private Integer calcPecent(Integer a, Integer b) {
        return Math.round(((float)a) / b * 100);
    }
}
