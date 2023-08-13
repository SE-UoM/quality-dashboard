import React, { useState, useEffect } from 'react'
import { Box, Flex, Text, Button, FormControl, FormLabel, Input, FormErrorMessage, Select, Grid } from '@chakra-ui/react';
import Navbar from '../components/Navbar';
import { useLoginMutation } from '../features/api/registerApi';
import { useDispatch, useSelector, useStore } from 'react-redux';
import { setCredentials } from '../features/api/authSlice';

function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [passwordError, setPasswordError] = useState(false);
    const [login, { data, isLoading }] = useLoginMutation();
    const loginData = useSelector(state => state.auth);
    const dispatch = useDispatch();
    const handleLogin = () => {
        // Placeholder function for login logic
        console.log('Login');
        console.log('Username:', email);
        login({ email, password })
    }
    useEffect(() => {
        if (data && !isLoading) {
            console.log("data", data)
            dispatch(setCredentials(data))
        }
    }, [data])

    console.log("data i got back: ", data)
    console.log(loginData)
    return (
        <Flex direction={"column"}>
            <Navbar />
            <Grid placeItems={"center"} height="65vh" width="100vw">
                <Box width="65%">
                    <FormControl>
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