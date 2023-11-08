package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.ProjectCodeSmellDistribution;
import gr.uom.strategicplanning.repositories.CodeSmellDistributionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CodeSmellDistributionService {
    @Autowired
    private CodeSmellDistributionRepository codeSmellDistributionRepository;

    public void save(ProjectCodeSmellDistribution projectCodeSmellDistribution) {
        codeSmellDistributionRepository.save(projectCodeSmellDistribution);
    }

    public ProjectCodeSmellDistribution saveOrUpdate(ProjectCodeSmellDistribution projectCodeSmellDistribution) {
        Optional<ProjectCodeSmellDistribution> codeSmellDistributionOptional = codeSmellDistributionRepository.findByCodeSmell(projectCodeSmellDistribution.getCodeSmell());

        ProjectCodeSmellDistribution projectCodeSmellDistributionToSave = projectCodeSmellDistribution;

        if (codeSmellDistributionOptional.isPresent()) {
            projectCodeSmellDistributionToSave = codeSmellDistributionOptional.get();
        }

        return codeSmellDistributionRepository.save(projectCodeSmellDistributionToSave);
    }

    public void saveCollectionOfCodeSmellDistribution(Collection<ProjectCodeSmellDistribution> projectCodeSmellDistributions) {
        for (ProjectCodeSmellDistribution projectCodeSmellDistribution : projectCodeSmellDistributions) {
            save(projectCodeSmellDistribution);
        }
    }
}
