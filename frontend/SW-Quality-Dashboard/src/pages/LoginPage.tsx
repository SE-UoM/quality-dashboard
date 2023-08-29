import React, { useState, useEffect } from 'react'
import { Box, Flex, chakra, Text, Button, FormControl, FormLabel, Input, FormErrorMessage, Select, Grid, useToast } from '@chakra-ui/react';
import Navbar from '../components/Navbar';
import { useLoginMutation } from '../features/api/registerApi';
import { setCredentials } from '../features/api/authSlice';
import { useAppDispatch, useAppSelector } from '../features/api/hooks';
import { useNavigate } from 'react-router-dom';


function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [passwordError, setPasswordError] = useState(false);
    const [login, { data, isLoading, isSuccess }] = useLoginMutation();
    const loginData = useAppSelector(state => state.auth);
    const dispatch = useAppDispatch();
    const toast = useToast();
    const navigate = useNavigate();
    // this is needed to prevent the toast from showing up twice
    // because there are two different state updates that occur
    const [canShowToast, setCanShowToast] = useState(false);
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
            setCanShowToast(true)
        }
    }, [data])

    // console.log("data i got back: ", data)
    // console.log(loginData)

    if (canShowToast && isSuccess) {
        // κάπως να μπορούσα να δω αν αυτός που έκανε login είναι ο admin
        toast({
            title: "Login was succesful",
            duration: 4.5 * 1000,
            render: ({ onClose }) => (
                <Box color="white" p={3} bg="green.500" rounded={20} height="125px" width="450px">
                    <Flex direction="column" justifyItems={"center"} rowGap={"0.5rem"}>
                        <Text fontSize="xl" fontWeight="bold" mr={2}>Login was succesful</Text>
                        <Flex columnGap="1rem">
                            <chakra.a href="#" onClick={(e) => {
                                e.preventDefault();
                                navigate("/")
                                onClose()
                            }}><Button variant="outline">
                                    Go to Dashboard
                                </Button>
                            </chakra.a>
                            <chakra.a href="#" onClick={(e) => {
                                e.preventDefault();
                                navigate("/submit-project")
                                onClose()
                            }}><Button variant="outline">
                                    Submit a Project
                                </Button></chakra.a>
                        </Flex>
                    </Flex>
                </Box>
            )

        })
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