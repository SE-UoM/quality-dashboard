import { useState } from 'react'
import { Box, Flex, Button, FormControl, FormLabel, Input, Grid } from '@chakra-ui/react';
import LoginForm from "../../components/LoginForm/LoginForm.tsx";
import './LoginPage.css';

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
        <div className={'login-page'}>
            <LoginForm />
        </div>
    )
}

export default LoginPage