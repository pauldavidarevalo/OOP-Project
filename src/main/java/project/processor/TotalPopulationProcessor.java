package project.processor;

import project.common.ProjectData;
import java.util.Map;

public class TotalPopulationProcessor {
    ProjectData projectData;

    public TotalPopulationProcessor(ProjectData pd) {
        this.projectData = pd;
    }
    public int run() {
        Map<String, Integer> zipPopulationMap = projectData.getZipPopulation();
        int result = 0;
        for (int v : zipPopulationMap.values()) {
            result += v;
        }
        return result;
    }
}
