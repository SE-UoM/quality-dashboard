import { useEffect, useState } from 'react';
import { Box, Text, VStack, Divider, chakra, Button, Flex } from '@chakra-ui/react';
import { Organization, Project, Developer, mockDeveloper, mockProject } from '../../assets/models.ts';
import { mockOrganization } from '../../assets/models.ts';
import Navbar from '../../components/ui/DashboardNavbar/DashboardNavbar.tsx';
import './AdminPanel.css';

function OrganizationList({ organization }: { organization: Organization }) {
    return (

        <Box bg="gray.100" p={4} mb={4}>
            <Text fontWeight="normal" fontSize="lg" mb={2}>
                Organization name: {"  "}
                <chakra.span fontWeight={'bold'}>{organization.name}</chakra.span>
            </Text>

            {/* <Text fontWeight="bold" mb={2}>
                Users:
                </Text>
                <ul>
                {organization.users.map((user, index) => (
                    <li key={index}>{user.name}</li>
                    ))}
                </ul> */}

            <Text fontWeight="bold" mb={2}>
                Repo URLs:
            </Text>
            <ul>
                {organization.projects.map((project) => (
                    <li key={project.repoUrl}>{project.repoUrl}</li>
                ))}
            </ul>
        </Box>
    );
}


function DeveloperList({ developers, organisation }: { developers: Developer[], organisation: Organization }) {
    return (
        <Box bg="gray.100" p={4} mb={4}>
            <Text fontWeight="normal" fontSize="lg" mb={2}>
                Developers for {" "}
                <chakra.span textDecoration={"underline"} fontWeight={"bold"}>

                    {organisation.name}
                </chakra.span>
            </Text>
            {developers.map((developer, index) => (
                <Box key={index}>
                    {index > 0 && <Divider my={4} />}
                    <Text fontWeight="bold" fontSize="lg" mb={2}>
                        Name: {" "}
                        <chakra.span fontWeight={"normal"}>

                            {developer.name}
                        </chakra.span>
                    </Text>
                    <Text fontWeight="bold" fontSize="lg" mb={2}>
                        Github URL: {" "}
                        <chakra.span fontWeight={"normal"}>

                            {developer.githubUrl}
                        </chakra.span>
                    </Text>
                    <Text fontWeight="bold" fontSize="lg" mb={2}>
                        Total Commits: {" "}
                        <chakra.span fontWeight={"normal"}>

                            {developer.totalCommits}
                        </chakra.span>
                    </Text>
                </Box>
            ))}
        </Box>
    );
}

function PendingProjects({ projects }: { projects: Project[] }) {
    return <VStack align={"stretch"} >
        <Text fontWeight="semibold" fontSize="lg" mb={2}>
            Pending Projects
        </Text>
        {projects.map((project, index) => (
            <Box key={index} bg="gray.100" p={4} mb={4}>
                <VStack align={"stretch"}>

                    <Text fontSize={"lg"} textDecoration={"underline"}>
                        Organisation:
                    </Text>
                    <Text fontSize={"lg"}>
                        {project.organization.name}
                    </Text>
                    <Text fontSize={"lg"} textDecoration={"underline"}>
                        Project URL:
                    </Text>
                    <Text fontSize={"lg"}>
                        {project.repoUrl}
                        <Button ml="4" colorScheme={"green"}>Copy</Button>
                    </Text>
                    <Text fontSize={"lg"} textDecoration={"underline"}>
                        Submitted at:
                    </Text>
                    <Text fontSize={"lg"}>
                        {new Date().toString()}
                    </Text>
                    <Button colorScheme='green' width="60%">Approve</Button>
                </VStack>
            </Box>
        ))
        }
    </VStack>;
}

function AdminPanel() {
    const [organizations, setOrganizations] = useState<Organization[]>([]);
    const [projectsAwaitingReview, setProjectsAwaitingReview] = useState<Project[]>([]);
    useEffect(() => {
        // Dummy data for organizations

        setOrganizations([mockOrganization, mockOrganization]);
    }, []);

    return (
        <Flex direction={"column"}>

            <Navbar />


            <Box p={4}>
                <Text fontSize="xl" fontWeight="bold" mb={4}>
                    Admin Panel
                </Text>
                <PendingProjects projects={[mockProject, mockProject]} />
                {/* these should be separated under a see more page */}
                {organizations.map((organization) => (
                    <OrganizationList key={organization.name} organization={organization} />
                ))}

                <VStack align="stretch">
                    {organizations.map((organization) => (
                        <DeveloperList key={organization.name} developers={[mockDeveloper, mockDeveloper]} organisation={organization} />
                    ))}
                </VStack>
            </Box>
        </Flex>
    );
}

export default AdminPanel;
