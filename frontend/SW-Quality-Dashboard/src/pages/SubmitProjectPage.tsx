import React from 'react';
import { Box, Flex, FormControl, FormLabel, Input, Select, Button, Text } from '@chakra-ui/react';

const industries = ['Technology', 'Healthcare', 'Finance', 'Education', 'Retail'];

function SubmitProjectForm() {
    return (
        <Flex align="center" justify="center" py={4}>
            <Box p={4} width="50%" bg="gray.100" borderRadius="md">
                <Text fontSize="xl" fontWeight="bold" mb={4} textAlign="center">
                    Submit Your Project
                </Text>
                <FormControl id="githubUrl" mb={4}>
                    <FormLabel>GitHub URL</FormLabel>
                    <Input type="url" placeholder="Enter GitHub URL" />
                </FormControl>
                <form>
                    <FormControl id="industry" mb={4}>
                        <FormLabel>Industry</FormLabel>
                        <Select placeholder="Select industry">
                            {industries.map((industry) => (
                                <option key={industry} value={industry}>
                                    {industry}
                                </option>
                            ))}
                        </Select>
                    </FormControl>
                    <FormControl id="teamSize" mb={4}>
                        <FormLabel>Team Size</FormLabel>
                        <Input type="number" placeholder="Enter team size" />
                    </FormControl>
                    <FormControl id="description" mb={4}>
                        <FormLabel>Description</FormLabel>
                        <Input type="text" placeholder="Enter project description" />
                    </FormControl>
                    <FormControl id="website" mb={4}>
                        <FormLabel>Website</FormLabel>
                        <Input type="url" placeholder="Enter project website" />
                    </FormControl>
                    <Button colorScheme="teal" size="lg" w="100%">
                        Submit
                    </Button>
                </form>
            </Box>
        </Flex>
    );
};

export default SubmitProjectForm;
