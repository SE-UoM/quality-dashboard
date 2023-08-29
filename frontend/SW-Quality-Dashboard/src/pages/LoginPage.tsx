import React, { useState } from 'react'
import { Box, Flex, Text, Button, FormControl, FormLabel, Input, FormErrorMessage, Select, Grid } from '@chakra-ui/react';
import Navbar from '../components/Navbar';

function LoginPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [passwordError, setPasswordError] = useState(false);

    const handleLogin = () => {
        // Placeholder function for login logic
        console.log('Login');
        console.log('Username:', username);
    }

    return (
        <Flex direction={"column"}>
            <Navbar />
            <Grid placeItems={"center"} height="65vh" width="100vw">
                <Box width="65%">
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


                        <Flex mt={4} justify="center" my={8}>
                            <Button colorScheme="teal" size="lg" width="68%" onClick={handleLogin}>
                                Login
                            </Button>

                        </Flex>
                    </FormControl>
                </Box>


            </Grid >
        </Flex>


    )
}

export default LoginPage