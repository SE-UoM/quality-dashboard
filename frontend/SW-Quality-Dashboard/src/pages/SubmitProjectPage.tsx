import React from 'react';
import { Box, Flex, FormControl, FormLabel, Input, Select, Button, Text, NumberInput, NumberInputField, NumberInputStepper, NumberDecrementStepper, NumberIncrementStepper } from '@chakra-ui/react';
import Navbar from '../components/Navbar';
import { useStartAnalysisMutation } from '../features/api/analysisApi';

const industries = ['Technology', 'Healthcare', 'Finance', 'Education', 'Retail', "other"];

function SubmitProjectForm() {
    const [githubUrl, setGithubUrl] = React.useState("");
    const [industry, setIndustry] = React.useState("");
    const [teamSize, setTeamSize] = React.useState(1);
    const [description, setDescription] = React.useState("");
    const [website, setWebsite] = React.useState("");
    const [startAnalysis, { isSuccess }] = useStartAnalysisMutation()

    const handleSubmit = () => {
        console.log("Submitted project");
        console.log("data of project: ", githubUrl, industry, teamSize, description, website)
        startAnalysis(githubUrl);
        setGithubUrl("");
        setIndustry("");
        setTeamSize(1);
        setDescription("");
    }


    return (
        <Flex direction={"column"}>
            <Navbar />
            <Flex align="center" justify="center" py={4}>
                <Box p={4} width="50%" bg="gray.100" borderRadius="md">
                    <Text fontSize="xl" fontWeight="bold" mb={4} textAlign="center">
                        Submit Your Project
                    </Text>
                    <FormControl id="githubUrl" mb={4}>
                        <FormLabel>GitHub URL</FormLabel>
                        <Input type="url" placeholder="Enter GitHub URL" value={githubUrl} onChange={e => setGithubUrl(e.target.value)} />
                    </FormControl>
                    <form>
                        <FormControl id="industry" mb={4}>
                            <FormLabel>Industry</FormLabel>
                            <Select placeholder="Select industry" value={industry} onChange={(e) => setIndustry(e.target.value)}>
                                {industries.map((industry) => (
                                    <option key={industry} value={industry}>
                                        {industry}
                                    </option>
                                ))}
                            </Select>
                        </FormControl>
                        <FormControl id="teamSize" mb={4}>
                            <FormLabel>Team Size</FormLabel>
                            <NumberInput defaultValue={1} value={teamSize} onChange={e => setTeamSize(Number(e))}>
                                <NumberInputField />
                                <NumberInputStepper>
                                    <NumberIncrementStepper />
                                    <NumberDecrementStepper />
                                </NumberInputStepper>
                            </NumberInput>
                        </FormControl>
                        <FormControl id="description" mb={4}>
                            <FormLabel>Description</FormLabel>
                            <Input type="text" placeholder="Enter project description" value={description} onChange={e => setDescription(e.target.value)} />
                        </FormControl>
                        <FormControl id="website" mb={4}>
                            <FormLabel>Website</FormLabel>
                            <Input type="url" placeholder="Enter project website" value={website} onChange={e => setWebsite(e.target.value)} />
                        </FormControl>
                        <Button colorScheme="teal" size="lg" w="100%" onClick={handleSubmit}>
                            Submit
                        </Button>
                    </form>
                </Box>
            </Flex>
        </Flex>
    );
};

export default SubmitProjectForm;
