import React from 'react'
import { Box, Flex, Text, Button } from '@chakra-ui/react';
import { Link } from 'react-router-dom';
function Navbar() {
    return (
        <Box p={4} borderBottom={"solid black 2px"} width="100vw">
            <Flex justifyContent="space-between" alignItems="center">

                <Link to="/">
                    <Button variant="ghost">
                        <Text fontSize="xl" fontWeight="bold">SW-Dashboard</Text>
                    </Button>
                </Link>

                <Flex gap={4}>
                    <Link to="/register">
                        <Button variant="ghost">Sign Up</Button>
                    </Link>
                    <Link to="/login">
                        <Button variant="ghost">Login</Button>
                    </Link>
                    <Link to="/submit-project">
                        <Button variant="ghost">Submit Project</Button>
                    </Link>
                    <Link to="/register-organisation">
                        <Button variant="ghost">Register Organisation</Button>
                    </Link>
                    {/* <Link to="/admin-panel">
                        <Button variant="ghost">Admin Panel</Button>
                    </Link> */}


                </Flex>
            </Flex>
        </Box>
    )
}

export default Navbar