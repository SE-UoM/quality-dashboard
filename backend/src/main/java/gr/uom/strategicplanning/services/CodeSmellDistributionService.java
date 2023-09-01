package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.CodeSmellDistribution;
import gr.uom.strategicplanning.repositories.CodeSmellDistributionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CodeSmellDistributionService {
    @Autowired
    private CodeSmellDistributionRepository codeSmellDistributionRepository;

    public void save(CodeSmellDistribution codeSmellDistribution) {
        codeSmellDistributionRepository.save(codeSmellDistribution);
    }

    public CodeSmellDistribution saveOrUpdate(CodeSmellDistribution codeSmellDistribution) {
        Optional<CodeSmellDistribution> codeSmellDistributionOptional = codeSmellDistributionRepository.findByCodeSmell(codeSmellDistribution.getCodeSmell());

        CodeSmellDistribution codeSmellDistributionToSave = codeSmellDistribution;

        if (codeSmellDistributionOptional.isPresent()) {
            codeSmellDistributionToSave = codeSmellDistributionOptional.get();
        }

        return codeSmellDistributionRepository.save(codeSmellDistributionToSave);
    }

    public void saveCollectionOfCodeSmellDistribution(Collection<CodeSmellDistribution> codeSmellDistributions) {
        for (CodeSmellDistribution codeSmellDistribution : codeSmellDistributions) {
            saveOrUpdate(codeSmellDistribution);
        }
    }
}
