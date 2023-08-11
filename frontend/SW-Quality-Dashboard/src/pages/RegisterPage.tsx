import React, { useState, useEffect } from 'react';
import { Box, Flex, Text, Button, FormControl, FormLabel, Input, FormErrorMessage, Select } from '@chakra-ui/react';

interface Organization {
    id: string;
    name: string;
}

function RegisterPage() {
    const [isAdmin, setIsAdmin] = useState(false);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [passwordError, setPasswordError] = useState(false);
    const [organizations, setOrganizations] = useState<Organization[]>([]);
    const [selectedOrganization, setSelectedOrganization] = useState("");

    useEffect(() => {
        // Fetch organizations from the backend API
        // const fetchOrganizations = async () => {
        //     try {
        //         const response = await fetch('your-backend-endpoint');
        //         const data = await response.json();
        //         setOrganizations(data);
        //     } catch (error) {
        //         console.error('Error fetching organizations:', error);
        //     }
        // };

        // fetchOrganizations();
        setOrganizations([
            { id: '1', name: 'Organization 1' },
            { id: '2', name: 'Organization 2' },
            { id: '3', name: 'Organization 3' },
        ])
    }, []);

    const handleRoleToggle = () => {
        setIsAdmin((prevIsAdmin) => !prevIsAdmin);
    };

    const handleLogin = () => {
        // Placeholder function for login logic
        console.log('Login');
        console.log('Username:', username);
        console.log('Password:', password);
        console.log('Selected Organization:', selectedOrganization);
    };

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
        <Box p={4}>
            <Text fontSize="xl" fontWeight="bold" mb={4}>
                Welcome, {isAdmin ? 'Admin' : 'User'}!
            </Text>

            <Flex justify="center" align="center" height="70vh">
                <Box width="60%">
                    <FormControl>
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
                            isDisabled={isAdmin}
                        >
                            {organizations.map((org) => (
                                <option key={org.id} value={org.id}>
                                    {org.name}
                                </option>
                            ))}
                        </Select>

                        <FormLabel>Role</FormLabel>
                        <Input type="text" value={isAdmin ? 'Admin' : 'User'} disabled />

                        <Flex mt={4} justify="space-between">
                            <Button colorScheme="teal" size="lg" width="48%" onClick={handleLogin}>
                                Login
                            </Button>

                            <Button
                                colorScheme="teal"
                                size="lg"
                                width="48%"
                                onClick={handleSignUp}
                                isDisabled={isAdmin}
                            >
                                Sign Up
                            </Button>
                        </Flex>

                        <Button mt={2} variant="outline" size="sm" onClick={handleRoleToggle}>
                            {isAdmin ? 'Switch to User' : 'Switch to Admin'}
                        </Button>
                    </FormControl>
                </Box>
            </Flex>
        </Box>
    );
}

export default RegisterPage;
