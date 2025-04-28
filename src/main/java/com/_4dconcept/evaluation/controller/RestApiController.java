package com._4dconcept.evaluation.controller;

import com._4dconcept.evaluation.Constants;
import com._4dconcept.evaluation.dto.DeveloperDTO;
import com._4dconcept.evaluation.mapper.DeveloperMapper;
import com._4dconcept.evaluation.model.Developer;
import com._4dconcept.evaluation.repository.DeveloperRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class RestApiController {

    static final Logger LOG = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    private DeveloperRepository DeveloperRepository;

    @Autowired
    private DeveloperMapper developerMapper;

    /**
     * @param showAllDeveloper - Get parameter from URL to know if it has to return the list of active or inactive developer
     * @return ResponseEntity - return an HTTP response containing the list of developer active or inactive depending on the get parameter
     */
    @PostMapping("developers/list")
    public ResponseEntity<?> listDevelopers( @RequestParam(value = "all", required = false, defaultValue = "true") boolean showAllDeveloper) {
        try {
            List<Developer> listDevelopers = showAllDeveloper
                ? DeveloperRepository.findAll()
                : DeveloperRepository.findAllByProjectIdNotNull();

            List<DeveloperDTO> listDeveloperDTO = developerMapper.toDtoList(listDevelopers);

            return ResponseEntity.ok(listDeveloperDTO);
        } catch(Exception e) {
            LOG.error("Error listing developers : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error listing developers");
        }
    }

    /**
     *
     * @param developer - the developer to create
     * @return ResponseEntity - return an HTTP response to confirm the creation was made successfully
     */
    @PostMapping("developers/create")
    public ResponseEntity createDevelopers(@RequestBody DeveloperDTO developer) {
        try {
            Developer developerToCreate = new Developer();
            developerToCreate.setName(developer.getName());
            developerToCreate.setProjectId(developer.getProjectId());
            developerToCreate.setStatus(Constants.DEVELOPER_STATUS_ACTIVE);

            DeveloperRepository.save(developerToCreate);

           return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create");
        }
    }
}
