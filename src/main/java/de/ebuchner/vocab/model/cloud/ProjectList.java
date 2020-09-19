package de.ebuchner.vocab.model.cloud;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ProjectList extends CloudList {

    private List<ProjectItem> projectItems = new ArrayList<ProjectItem>();

    public ProjectList(String cloudServerResponse) {
        StringTokenizer lines = new StringTokenizer(cloudServerResponse, "\n");
        while (lines.hasMoreElements()) {
            String line = lines.nextToken();

            StringTokenizer fields = new StringTokenizer(line, ";");
            ProjectItem projectItem = new ProjectItem();
            projectItems.add(projectItem);
            for (int i = 0; fields.hasMoreTokens(); i++) {
                String field = fields.nextToken();
                switch (i) {
                    case 0:
                        projectItem.setProjectName(extract(field));
                        break;
                    case 1:
                        projectItem.setProjectLocale(extract(field));
                        break;
                    case 2:
                        projectItem.setNumberOfFiles(Long.parseLong(field));
                        break;
                }
            }

        }
    }

    public List<ProjectItem> getProjectItems() {
        return projectItems;
    }

    public boolean containsProjectName(String name) {
        for (ProjectItem projectItem : projectItems) {
            if (projectItem.getProjectName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }
}
