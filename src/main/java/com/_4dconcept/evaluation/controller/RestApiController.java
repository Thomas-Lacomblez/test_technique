package com._4dconcept.evaluation.controller;

import com._4dconcept.evaluation.Constants;
import com._4dconcept.evaluation.Utils;
import com._4dconcept.evaluation.model.Developer;
import com._4dconcept.evaluation.model.Project;
import com._4dconcept.evaluation.model.Projects;
import com._4dconcept.evaluation.repository.DeveloperRepository;
import com._4dconcept.evaluation.BusinessException;
import com._4dconcept.evaluation.projects.ProjectsFileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class RestApiController {

    static final Logger LOG = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    private DeveloperRepository DeveloperRepository;

    @Value("${project.file.path}")
    private String projectFilePath;

    /**
     *
     * @return
     */
    @PostMapping("developers/list")
    public ResponseEntity<?> listDevelopers(HttpServletRequest request){
        String allDevelopers = request.getParameter("all");
        boolean allDevelopers2 = false;
        if(! Utils.isStringNullOrEmptyOrBlank(allDevelopers)){
            allDevelopers2 = Boolean.parseBoolean(allDevelopers);
            if(!allDevelopers2){
                LOG.warn("Should return only active developer");
            }
        }
        try {
            List<Developer> developers = DeveloperRepository.findAll();

            ArrayList<DeveloperView> ds = new ArrayList<>();
            for(int i = 0; i < developers.size(); i++) {
                Developer developer = developers.get(i);

                DeveloperView d = new DeveloperView(developer.getId(), developer.getName());

                Projects projects = ProjectsFileHelper.loadProjects(projectFilePath);
                if(developer.getProjectId() != null) {
                    for (int j = 0; j < projects.getProjects().size(); j++) {
                        if (developer.getProjectId().equals(projects.getProjects().get(j).getId())) {
                            d.setProjectName(projects.getProjects().get(j).getName());
                        }
                    }
                }

                if (Constants.DEVELOPER_STATUS_ACTIVE.equals(developer.getStatus())) {
                    ds.add(d);
                } else {
                    LOG.error("The developer is inactive");
                }
            }

            return ResponseEntity.ok(ds);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(200).body("Error listing developers");
        }
    }

    /**
     *
     * @param developer - the developer to create
     * @return
     */
    @PostMapping("developers/create")
    public ResponseEntity ceateDevelopers(@RequestBody DeveloperView developer){

        try {
            Developer developerToCreate = new Developer();
            developerToCreate.setName(developer.getName());
            developerToCreate.setProjectId(developer.getProjectId());
            developerToCreate.setStatus(Constants.DEVELOPER_STATUS_ACTIVE);

            DeveloperRepository.save(developerToCreate);

           return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Failed to create");
        }
    }
}
