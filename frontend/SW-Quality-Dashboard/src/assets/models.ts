export enum UserRoles {
    ORG_ADMIN = "admin",
    SUPER_USER = "superuser",
    SIMPLE_USER = "user"
}

export interface Developer {
    name: string;
    githubUrl: string,
    totalCommits: number,
    project: Project
}
export enum ProjectStatus {
    ANALYSIS_IN_PROGRESS,
    ANALYSIS_COMPLETED,
    ANALYSIS_FAILED,
    ANALYSIS_SKIPPED,
    ANALYSIS_TO_BE_REVIEWED,
    ANALYSIS_NOT_STARTED
}
export interface Project {
    organization: Organization,
    repoUrl: string,
    forks: number,
    stars: number,
    commits: Commit[],
    totalDevelopers: number,
    developers: Set<Developer>,
    totalLanguages: number,
    totalCommits: number,
    status: ProjectStatus,
}

export interface Organization {
    name: string,
    users: User[],
    projects: Project[],
    OrganizationAnalysis: OrganizationAnalysis | null,
}

export interface OrganizationAnalysis {
    orgName: string,
    analysisDate: Date,
    generalStats: GeneralStats,
    techDebtStats: TechDebtStats,
    activityStats: ActivityStats,
    mostStarredProject: Project,
    mostForkedProject: Project,
}

export interface GeneralStats {
    totalProjects: number,
    totalLanguages: number,
    languages: string[],
    totalCommits: number,
    totalFiles: number,
    totalLinesOfCode: number,
    totalDevelopers: number,
}

export interface ActivityStats {
    commitsPerDay: number,
    locAddedPerDay: number,
    filesAddedPerDay: number,
    projectsAddedPerDay: number,
    averageLoc: number,
}
export interface TechDebtStats {
    id: number;
    totalTechDebt: number;
    averageTechDebt: number;
    ProjectWithMinTechDebt: Project; // Replace 'Project' with the actual class/interface for Project
    ProjectWithMaxTechDebt: Project; // Replace 'Project' with the actual class/interface for Project
    averageTechDebtPerLoC: number;
    minDebtLanguage: Language; // Replace 'Language' with the actual class/interface for Language
    bestTechDebtProjects: Project[]; // Replace 'Project' with the actual class/interface for Project
    bestCodeSmellProjects: Project[]; // Replace 'Project' with the actual class/interface for Project
    totalCodeSmells: number;
    codeSmells: CodeSmell[]; // Replace 'CodeSmell' with the actual class/interface for CodeSmell
}

export interface Commit {
    id: number;
    hash: string;
    developer: Developer; // Replace 'Developer' with the actual class/interface for Developer
    commitDate: Date;
    codeSmells: CodeSmell[]; // Replace 'CodeSmell' with the actual class/interface for CodeSmell
    technicalDebt: number;
    totalFiles: number;
    totalLoC: number;
    totalCodeSmells: number;
    techDebtPerLoC: number;
    languages: Language[]; // Replace 'Language' with the actual class/interface for Language
    totalLanguages: number;
    project: Project; // Replace 'Project' with the actual class/interface for Project
}
export interface CodeSmell {
    id: number;
    name: string;
    severityLevel: string;
    line: number;
    remediationTime: number;
    description: string;
}


export interface User {
    id: number;
    name: string;
    email: string;
    roles: string[];
    verified: boolean;
    organization?: Organization; // Use the actual interface for Organization or omit it if not needed on the frontend
}

export interface Language {
    id: number;
    name: string;
    version: string;
}

export interface ProjectStats {
    id: number;
    totalLoC: number;
    totalFiles: number;
    totalCodeSmells: number;
    techDebt: number;
    techDebtPerLoC: number;
    totalLanguages: number;
}


const mockOrganization: Organization = {
    name: "Acme Corp",
    users: [], // Assuming you have the mockUser object defined
    projects: [], // Assuming you have the mockProject object defined
    OrganizationAnalysis: null, // Assuming you have the mockOrganizationAnalysis object defined
};

const mockLanguage: Language = {
    id: 1,
    name: "JavaScript",
    version: "ES6",
};
const mockProject: Project = {
    organization: mockOrganization, // Assuming you have the mockOrganization object defined
    repoUrl: "https://github.com/example/mock-project",
    forks: 100,
    stars: 500,
    commits: [], // Assuming you have the mockCommit object defined
    totalDevelopers: 10,
    developers: new Set<Developer>([]), // Assuming you have the mockDeveloper object defined
    totalLanguages: 3,
    totalCommits: 1000,
    status: ProjectStatus.ANALYSIS_COMPLETED,
};
const mockTechDebtStats: TechDebtStats = {
    id: 1,
    totalTechDebt: 2500,
    averageTechDebt: 500,
    ProjectWithMinTechDebt: mockProject,
    ProjectWithMaxTechDebt: mockProject,
    averageTechDebtPerLoC: 0.25,
    minDebtLanguage: mockLanguage,
    bestTechDebtProjects: [mockProject],
    bestCodeSmellProjects: [mockProject],
    totalCodeSmells: 50,
    codeSmells: [],
};



const mockGeneralStats: GeneralStats = {
    totalProjects: 10,
    totalLanguages: 5,
    languages: ["JavaScript", "TypeScript", "Python", "Java", "C#"],
    totalCommits: 5000,
    totalFiles: 1000,
    totalLinesOfCode: 50000,
    totalDevelopers: 50,
};
const mockActivityStats: ActivityStats = {
    commitsPerDay: 50,
    locAddedPerDay: 1000,
    filesAddedPerDay: 20,
    projectsAddedPerDay: 2,
    averageLoc: 500,
};
const mockOrganizationAnalysis: OrganizationAnalysis = {
    orgName: "Acme Corp",
    analysisDate: new Date("2023-07-26"),
    generalStats: mockGeneralStats, // Assuming you have the mockGeneralStats object defined
    techDebtStats: mockTechDebtStats, // Assuming you have the mockTechDebtStats object defined
    activityStats: mockActivityStats, // Assuming you have the mockActivityStats object defined
    mostStarredProject: mockProject, // Assuming you have the mockProject object defined
    mostForkedProject: mockProject, // Assuming you have the mockProject object defined
};
const mockUser: User = {
    id: 1,
    name: "John Doe",
    email: "john.doe@example.com",
    roles: ["user"],
    verified: true,
    organization: mockOrganization,
};

const mockCodeSmell: CodeSmell = {
    id: 1,
    name: "LongMethod",
    severityLevel: "Medium",
    line: 42,
    remediationTime: 30,
    description: "This method is too long and needs refactoring.",
};
const mockDeveloper: Developer = {
    name: "Jane Smith",
    githubUrl: "jane.smith@example.com",
    totalCommits: 50,
    project: mockProject, // Assuming you have the mockProject object defined
};
const mockCommit: Commit = {
    id: 1,
    hash: "a3d2c4f1b2e0",
    developer: mockDeveloper,
    commitDate: new Date(),
    codeSmells: [mockCodeSmell],
    technicalDebt: 150,
    totalFiles: 10,
    totalLoC: 1000,
    totalCodeSmells: 5,
    techDebtPerLoC: 0.15,
    languages: [mockLanguage],
    totalLanguages: 1,
    project: mockProject,
};



mockOrganization.users.push(mockUser);
mockOrganization.projects.push(mockProject);
mockOrganization.OrganizationAnalysis = mockOrganizationAnalysis;

mockProject.commits.push(mockCommit);
mockProject.developers.add(mockDeveloper);

mockTechDebtStats.codeSmells.push(mockCodeSmell);

export interface LanguageStats {
    id: number;
    linesOfCode: number;
    project: Project;
    language: Language;
}

export {
    mockActivityStats, mockCodeSmell, mockCommit, mockDeveloper, mockGeneralStats, mockLanguage, mockOrganization, mockOrganizationAnalysis, mockProject, mockTechDebtStats,
    mockUser
}


