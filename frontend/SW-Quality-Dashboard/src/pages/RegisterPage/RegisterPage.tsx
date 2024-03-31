import React, { useState, useEffect } from 'react';
import { Box, Flex, Text, Button, FormControl, FormLabel, Input, FormErrorMessage, Select } from '@chakra-ui/react';
import Navbar from '../components/DashboardNavbar/DashboardNavbar.tsx';
import axios from "axios";

interface Organization {
    id: string;
    name: string;
}

function RegisterPage() {
    const [username, setUsername] = useState('');
    const [name, setName] = useState('');
    const [password, setPassword] = useState('');
    const [passwordError, setPasswordError] = useState(false);
    const [organizations, setOrganizations] = useState<Organization[]>([]);
    const [selectedOrganization, setSelectedOrganization] = useState("");

    useEffect(() => {
        // Call API to get organizations
        let url = 'http://localhost:8080/api/organizations/names';

        axios.get(url)
            .then((response) => {
                setOrganizations(response.data);
            })
            .catch((error) => {
                console.log(error);
            });
    }, []);
    const handleSignUp = () => {
        if (password.length < 6) {
            setPasswordError(true);
            return;
        }

        // Placeholder function for sign-up logic
        console.log('Sign Up');
        console.log('Username:', username);
        console.log('Password:', password);
        console.log('Selected Organization:', selectedOrganization);
    };

    return (
        <Flex direction={"column"}>
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
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
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
                                {organizations.map((org) => (
                                    <option key={org.id} value={org.id}>
                                        {org.name}
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
