import React, {useEffect} from 'react'
import {Link, useLocation} from "react-router-dom";
import logo from '../assets/dashboard_logo_transparent.png'
import axios from "axios";
import {Button} from "@chakra-ui/react";

function VerifyUserPage() {
    const location = useLocation()
    const [statusTextTitle, setStatusTextTitle] = React.useState<string>('User Verification in Progress')
    const [statusText, setStatusText] = React.useState<string>('Verifying your email address. Please wait...')
    const [verificationSuccess, setVerificationSuccess] = React.useState<boolean>(false)

    useEffect(() => {
        let params = new URLSearchParams(location.search)
        let token = params.get('token')
        let uid = params.get('uid')

        // URI Encode the token
        token = encodeURIComponent(token)
        let url = `http://localhost:8080/api/user/verify?token=${token}&uid=${uid}`

        // Wait a few seconds to make the user know something is happening
        setTimeout(() => {
            callAPI(token, uid)
        }  , 1500)
    }, []);

    async function callAPI(token, uid) {
        let url = `http://localhost:8080/api/user/verify?token=${token}&uid=${uid}`
        await axios.post(url)
            .then(response => {
                if (response.status === 200) {
                    setStatusTextTitle('User Verification Successful')
                    setStatusText('Your account has been verified successfully. You can now login to your account and start analyzing projects.')
                    setVerificationSuccess(true)
                }
            })
            .catch(error => {
                console.error('Error verifying email:', error);
                setStatusText('Error verifying email. Please contact your admin.'); // Set verification status to error message
                setVerificationSuccess(false)
            });
    }

    let pageStyle = {
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
        textAlign: 'center'

    }

    return (
        <>
            <div style={pageStyle}>
                <img src={logo} alt="logo" style={{width: '100px', height: '100px'}}/>
                <h1>
                    <i><b>{statusTextTitle}</b></i>
                </h1>

                <p
                    style={{
                        display: 'flex',
                        flexDirection: 'column',
                        justifyContent: 'center',
                    }}
                >
                    {statusText}
                    <a href="/"
                        style={{
                            marginTop: '1em',
                            fontStyle: 'italic',
                            color: '#015092',
                            textDecoration: 'underline',
                        }}
                    >
                        Go to Home page
                    </a>
                </p>

            </div>
        </>
    )
}

export default VerifyUserPage