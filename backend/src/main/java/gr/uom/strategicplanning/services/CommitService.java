package gr.uom.strategicplanning.services;

import gr.uom.strategicplanning.models.domain.Commit;
import gr.uom.strategicplanning.models.domain.Project;
import org.springframework.stereotype.Service;

@Service
public class CommitService {

    public void populateCommit(Commit commit, Project project) {
        //TODO: Fetch commit data from Github API

        //TODO: Populate commit with Developers
    }
}
