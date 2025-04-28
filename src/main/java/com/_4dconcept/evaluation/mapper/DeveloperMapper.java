package com._4dconcept.evaluation.mapper;

import com._4dconcept.evaluation.dto.DeveloperDTO;
import com._4dconcept.evaluation.model.Developer;

import java.util.List;
import java.util.stream.Collectors;

import com._4dconcept.evaluation.model.Projects;
import com._4dconcept.evaluation.projects.ProjectsFileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DeveloperMapper {

    static final Logger LOG = LoggerFactory.getLogger(DeveloperMapper.class);

    @Value("${project.file.path}")
    private String projectFilePath;

    public DeveloperDTO toDto(Developer developer) {
        if(developer == null) {
            LOG.warn("Attempted to map null developer");
            return null;
        }

        DeveloperDTO developerDTO = new DeveloperDTO(developer.getId(), developer.getName());

        if (developer.getProjectId() != null) {
            try {
                Projects projects = ProjectsFileHelper.loadProjects(projectFilePath);
                if (projects != null && projects.getProjects() != null) {
                    projects.getProjects().stream()
                            .filter(project -> project != null && developer.getProjectId().equals(project.getId()))
                            .findFirst()
                            .ifPresent(project -> {
                                developerDTO.setProjectId(developer.getProjectId());
                                developerDTO.setProjectName(project.getName());
                            });
                } else {
                    LOG.error("No projects found in file : {}", projectFilePath);
                }
            } catch (Exception e) {
                LOG.error("Impossible to map Developer to DeveloperView {}: ", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return developerDTO;
    }

    public List<DeveloperDTO> toDtoList(List<Developer> developers) {
        return developers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

