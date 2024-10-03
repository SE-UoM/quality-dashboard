package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.analysis.refactoringminer.RefactoringMinerAnalysis;
import gr.uom.strategicplanning.models.analyses.OrganizationAnalysis;
import gr.uom.strategicplanning.models.domain.Organization;
import gr.uom.strategicplanning.models.domain.Project;
import gr.uom.strategicplanning.models.domain.RefactoringModel;
import gr.uom.strategicplanning.models.stats.RefactoringMinerStats;
import gr.uom.strategicplanning.repositories.RefactoringModelRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RefactoringMinerService {

    @Autowired
    private RefactoringModelRepository refactoringModelRepository;

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private CommitService commitService;

    public void saveRefactoring(RefactoringModel ref) {
        refactoringModelRepository.save(ref);
    }

    public int analyzeProject(Project project) throws Exception {
        String defaultBranch = project.getDefaultBranchName();
        String repoUrl = project.getRepoUrl();
        String projectName = project.getName();

        // Analyze the project using RefactoringMiner to get the all types of refactorings
        RefactoringMinerAnalysis refactoringMinerAnalysis = new RefactoringMinerAnalysis(commitService);
        Map<String, Object> results = refactoringMinerAnalysis.getTotalNumberOfRefactorings(repoUrl, projectName, defaultBranch);

        // Save the refactorings to the database
        for (RefactoringModel refactoringModel : (Iterable<RefactoringModel>) results.get("refactorings")) {
            saveRefactoring(refactoringModel);
        }

        // Update the Organization Statistics for RefactoringMiner
        Organization organization = project.getOrganization();
        OrganizationAnalysis organizationAnalysis = organization.getOrganizationAnalysis();
        RefactoringMinerStats refactoringMinerStats = organizationAnalysis.getRefactoringMinerStats();

        int totalDifferentRefactorings = (int) results.get("totalRefactorings");

        Collection<RefactoringModel> allRefactorings = (Collection<RefactoringModel>) results.get("refactorings");

        // Using streams to collect distinct refactoringType values into a Set
        Set<String> distinctRefactoringTypes = allRefactorings.stream()
                .map(RefactoringModel::getRefactoringType)  // Extract refactoringType
                .collect(Collectors.toSet());  // Collect distinct values into a Set

        // Use streams to collect distinct refactoringName values into a Set
        Set<String> distinctRefactoringNames = allRefactorings.stream()
                .map(RefactoringModel::getName)  // Extract refactoringName
                .collect(Collectors.toSet());  // Collect distinct values into a Set

        // Update the RefactoringMinerStats object with the distinct refactoring types and names
        refactoringMinerStats.incrementTotalDifferentRefactoringsBy(totalDifferentRefactorings);
        refactoringMinerStats.addMultipleRefactoringTypes(distinctRefactoringTypes);
        refactoringMinerStats.addMultipleRefactoringNames(distinctRefactoringNames);

        organizationAnalysis.setRefactoringMinerStats(refactoringMinerStats);
        organization.setOrganizationAnalysis(organizationAnalysis);

        organizationService.saveOrganization(organization);

        return 1;
    }
}
