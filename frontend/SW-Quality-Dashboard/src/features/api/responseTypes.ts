import { Project, GeneralStats, TechDebtStats, ActivityStats, Language, LanguageStats, ProjectStatus, ProjectStats, Developer, CodeSmell } from "../../assets/models"

export interface OrganizationResponse {
    id: number;
    organizationName: string;
    projects: ProjectResponse[];
    organizationAnalysis: OrganizationAnalysisResponse;
}


export interface ProjectResponse {
    id: number;
    name: string;
    organizationName: string;
    organizationId: number;
    repoUrl: string;
    forks: number;
    stars: number;
    totalDevelopers: number;
    totalCommits: number;
    totalLanguages: number;
    status: ProjectStatus;
    developers: DeveloperResponse[];
    commits: CommitResponse[];
    languages: LanguageResponse[];
    projectStats: ProjectStats;
}

export interface CommitResponse {
    id: number;
    hash: string;
    developer: Developer;
    commitDate: Date;
    codeSmells: CodeSmell[];
    technicalDebt: number;
    totalFiles: number;
    totalLoC: number;
    totalCodeSmells: number;
    techDebtPerLoC: number;
    languages: LanguageStats[];
    totalLanguages: number;
    projectId: number;
}

export interface DeveloperResponse {
    id: number;
    name: string;
    githubUrl: string;
    totalCommits: number;
    projectId: number;
}

export interface LanguageResponse {
    id: number;
    name: string;
    imageUrl: string;
    linesOfCode: number;
}

export interface OrganizationAnalysisResponse {
    id: number;
    orgName: string;
    analysisDate: Date;
    generalStats: GeneralStats;
    techDebtStats: TechDebtStats;
    activityStats: ActivityStats;
    mostStarredProject: ProjectResponse;
    mostForkedProject: ProjectResponse;
    languages: LanguageResponse[] | null;
}





