package ulstu.pecherskih.diplom.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ulstu.pecherskih.diplom.HashDTO.CommonNodeDTO;
import ulstu.pecherskih.diplom.HashDTO.HashDTO;
import ulstu.pecherskih.diplom.HashDTO.NodeDiffDTO;
import ulstu.pecherskih.diplom.HashDTO.ResultDTO;
import ulstu.pecherskih.diplom.repository.CustomRepository;
import ulstu.pecherskih.diplom.repository.PackageRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Map<String, List<HashDTO>> allHashes = new HashMap<String, List<HashDTO>>();

        if (this.existFile("idsHashes.json")) {
            ObjectMapper mapper = new ObjectMapper();
            allHashes = mapper.readValue(Paths.get("idsHashes.json").toFile(), new TypeReference<Map<String, List<HashDTO>>>(){});

            if (allHashes != null && allHashes.size() > 0) {
                this.countHashes(allHashes, id);
            }
            return;
        }

        this.saveHashesToFile(allHashes, id);
    }

    private void saveHashesToFile(Map<String, List<HashDTO>> allHashes, Long id) throws IOException {
        Collection<ResultDTO> hashesById = customRepository.getPathHashFromRoot(id);

        for (ResultDTO resultDTO : hashesById) {
            List<HashDTO> nodesByHash = allHashes.get(resultDTO.getHash());

            if (nodesByHash == null) {
                nodesByHash = new ArrayList<>();
            } else {
                HashDTO findHashByIdInFile = nodesByHash.stream()
                        .filter(item -> item.getRootId().equals(this.longToInteger(id)))
                        .findAny()
                        .orElse(null);

                if (findHashByIdInFile != null) continue;
            }

            HashDTO newNodeHashInFile = new HashDTO();
            newNodeHashInFile.setRootId(this.longToInteger(id));
            newNodeHashInFile.setPathIds(resultDTO.getPathIds());

            nodesByHash.add(newNodeHashInFile);
            allHashes.put(resultDTO.getHash(), nodesByHash);
        }

        ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        objectMapper.writeValue(Paths.get("idsHashes.json").toFile(), allHashes);

        this.countHashes(allHashes, id);
    }

    private Integer longToInteger(Long num) {
        return num.intValue();
    }

    private void countHashes(Map<String, List<HashDTO>> allHashes, Long id) throws IOException {
        if (!this.existInFile(allHashes, id)) {
            this.saveHashesToFile(allHashes, id);
        }

        Map<Integer, NodeDiffDTO> counter = new HashMap<>();

        for (Map.Entry<String, List<HashDTO>> entry : allHashes.entrySet()) {
            List<HashDTO> nodesByHash = entry.getValue();

            HashDTO findNodeByHash = nodesByHash.stream()
                    .filter(item -> item.getRootId().equals(this.longToInteger(id)))
                    .findAny()
                    .orElse(null);
            boolean isFind = findNodeByHash != null;

            for (HashDTO nodeByHash : nodesByHash) {
                if (nodeByHash.getRootId().equals(this.longToInteger(id))) continue;

                NodeDiffDTO nodeDiffDTO = new NodeDiffDTO();

                NodeDiffDTO nodeDiffDTOExist = counter.get(nodeByHash.getRootId());
                if (nodeDiffDTOExist != null) {
                    nodeDiffDTO = nodeDiffDTOExist;
                }

                nodeDiffDTO.setRootId(nodeByHash.getRootId());

                String hash = entry.getKey();
                List<Integer> paths =  nodeByHash.getPathIds();

                if (isFind) {
                    nodeDiffDTO.addEqualsPath(hash, paths);
                } else {
                    nodeDiffDTO.addDiffPath(hash, paths);
                }

                counter.put(nodeDiffDTO.getRootId(), nodeDiffDTO);
            }
        }

        this.writeResult2(counter, id);
    }

    private boolean existInFile(Map<String, List<HashDTO>> allHashes, Long id) {
        boolean hasHashInFile = false;

        for (Map.Entry<String, List<HashDTO>> entry : allHashes.entrySet()) {
            List<HashDTO> nodesByHash = entry.getValue();

            HashDTO findNodeByHash = nodesByHash.stream()
                    .filter(item -> item.getRootId().equals(longToInteger(id)))
                    .findAny().orElse(null);

            if (findNodeByHash != null) {
                hasHashInFile = true;
                break;
            }
        }

        return hasHashInFile;
    }

    private void writeResult(Map<Integer, Integer> counter, Long id) throws IOException {
        Map<Integer, Map<Integer, Integer>> result = new HashMap<>();

        if (this.existFile("result.json")) {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.readValue(Paths.get("result.json").toFile(), new TypeReference<Map<Integer, Map<Integer, Integer>>>(){});
        }

        Map<Integer, Integer> countCommonNodes = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : counter.entrySet()) {
            countCommonNodes.put(entry.getKey(), entry.getValue());
        }

        result.put(this.longToInteger(id), countCommonNodes);

        ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        objectMapper.writeValue(Paths.get("result.json").toFile(), result);
    }

    private void writeResult2(Map<Integer, NodeDiffDTO> counter, Long id) throws IOException {
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

        for (Map.Entry<Integer, NodeDiffDTO> entry : counter.entrySet()) {
            myNode.addCommonNodeDTO(entry.getValue());
        }

        result.add(myNode);

        ObjectWriter objectMapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        objectMapper.writeValue(Paths.get("result.json").toFile(), result);
    }

    private Integer calcPecent(Integer a, Integer b) {
        return Math.round(((float)a) / b * 100);
    }
}
