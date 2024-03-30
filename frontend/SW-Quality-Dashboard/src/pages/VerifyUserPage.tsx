import React, {useEffect} from 'react'
import {useLocation} from "react-router-dom";
import logo from '../assets/dashboard_logo_transparent.png'
import axios from "axios";

function VerifyUserPage() {
    const location = useLocation()
    const [statusTextTitle, setStatusTextTitle] = React.useState<string>('User Verification in Progress')
    const [statusText, setStatusText] = React.useState<string>('Please wait while we verify your account')

    useEffect(() => {
        let params = new URLSearchParams(location.search)
        console.log(params)
        let token = params.get('token')
        console.log(token)

        let uid = params.get('uid')
        console.log(uid)

        let url = `http://localhost:8080/api/user/verify?token=${token}&uid=${uid}`
        console.log(url)

        axios.post(url)
            .then(response => {
                if (response.status === 200) {
                    setStatusTextTitle('User Verification Successful')
                    setStatusText('Your account has been verified successfully. You can now login to your account and start analyzing projects.')
                }
            })
            .catch(error => {
                console.error('Error verifying email:', error);
                setStatusText('Error verifying email. Please contact your admin.'); // Set verification status to error message
            });


    }, []);

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

                <p>
                    {statusText}
                </p>

            </div>
        </>
    )
}

export default VerifyUserPage