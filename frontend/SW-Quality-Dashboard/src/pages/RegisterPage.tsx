import { useState, useEffect } from 'react';
import { Box, Flex, Text, Button, FormControl, FormLabel, Input, FormErrorMessage, Select } from '@chakra-ui/react';
import Navbar from '../components/Navbar';
import { useGetOrganizationsQuery } from '../features/api/organizationApi';
import { useRegisterMutation } from '../features/api/registerApi';

interface Organization {
    id: string;
    name: string;
}

function RegisterPage() {
    const [email, setEmail] = useState('');
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [passwordError, setPasswordError] = useState(false);
    const [selectedOrganization, setSelectedOrganization] = useState("");
    const [register, { data: regData, status, isLoading }] = useRegisterMutation();
    const { data, isLoading: orgLoading } = useGetOrganizationsQuery();
    console.log("data", regData, status)

    const handleSignUp = () => {
        if (password.length < 6) {
            setPasswordError(true);
            return;
        }

        // Placeholder function for sign-up logic
        register({ name, email, password })

    };

    return (
        <Flex direction={"column"}>
            <Navbar />
            <Box p={4}>
                <Text fontSize="xl" fontWeight="bold">
                    Welcome to SW-UoM Dashboard
                </Text>

                <Flex justify="center" align="center" height="70vh">
                    <Box width="60%">
                        <FormControl>

                            <FormLabel>
                                Public Name
                            </FormLabel>
                            <Input placeholder="This name will be shown to other users"
                                value={name}

                                onChange={(e) => setName(e.target.value)} />

                            <FormLabel>Username</FormLabel>
                            <Input
                                placeholder="Enter your username"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />

                            <FormLabel>Password</FormLabel>
                            <Input
                                type="password"
                                placeholder="Enter your password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                isInvalid={passwordError}
                            />
                            {passwordError && (
                                <FormErrorMessage>Password should be at least 6 characters long.</FormErrorMessage>
                            )}

                            <FormLabel>Organization</FormLabel>
                            <Select
                                placeholder="Select organization"
                                value={selectedOrganization}
                                onChange={(e) => setSelectedOrganization(e.target.value)}

                            >

                                {data && data.map((org) => (
                                    <option key={org.id} value={org.id}>
                                        {org.organizationName}
                                    </option>
                                ))}
                            </Select>
                            <Flex mt={4} justify="center">


                                <Button
                                    colorScheme="teal"
                                    size="lg"
                                    my={"6"}
                                    width="65%"
                                    onClick={handleSignUp}
                                    isLoading={isLoading}
                                >
                                    Sign Up
                                </Button>
                            </Flex>

                        </FormControl>
                    </Box>
                </Flex>
            </Box>
        </Flex>
    );
}

export default RegisterPage;
