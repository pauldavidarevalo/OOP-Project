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
        if (zipPopulationMap == null) return 0;
        int result = 0;
        for (Integer v : zipPopulationMap.values()) {
            if (v != null) {result += v;}
        }
        return result;
    }
}
