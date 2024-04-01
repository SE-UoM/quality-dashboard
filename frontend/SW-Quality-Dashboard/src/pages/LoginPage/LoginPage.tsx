import { useState } from 'react'
import { Box, Flex, Button, FormControl, FormLabel, Input, Grid } from '@chakra-ui/react';
import LoginForm from "../../components/LoginForm/LoginForm.tsx";
import './LoginPage.css';

function LoginPage() {
    return (
        <div className={'login-page'}>
            <LoginForm />
        </div>
    )
}

export default LoginPage