package com._4dconcept.evaluation.projects;

import com._4dconcept.evaluation.BusinessException;
import com._4dconcept.evaluation.model.Projects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class ProjectsFileHelper {

    private static Logger LOG = LoggerFactory.getLogger(ProjectsFileHelper.class);

    public static Projects loadProjects(String projectFilePath) throws BusinessException {
        try {
            FileInputStream fileInputStream = new FileInputStream(projectFilePath);

            StringWriter o = new StringWriter();
            InputStreamReader input = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            char[] b = new char[1024];
            for (int l = 0; (l = input.read(b)) != -1; ) {
                o.write(b, 0, l);
            }
            o.close();

            String content = o.toString();

            System.out.println(content);

            Unmarshaller unmarshaller = newUnmarshaller();
            Projects unmarshal = (Projects) unmarshaller.unmarshal(new StringReader(content));

            return unmarshal;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        } catch (JAXBException e) {
            LOG.error("Not parseable file");
            return null;
        }
    }

    public static Unmarshaller newUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Projects.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller;
    }

}
