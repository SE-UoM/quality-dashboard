import './App.css'
import {BrowserRouter, Outlet, Route, Routes, useLocation} from "react-router-dom"
import Dashboard from './pages/DashboardPage/DashboardPage.tsx'
import { ChakraProvider } from '@chakra-ui/react'
import RegisterPage from './pages/RegisterPage/RegisterPage.tsx'
import AdminPanel from './pages/AdminPanel/AdminPanel.tsx'
import RegisterOrganisationPage from './pages/RegisterOrganizationPage/RegisterOrganisationPage.tsx'
import AboutPage from './pages/AboutPage/AboutPage.tsx'
import SubmitProjectPage from './pages/SubmitProjectPage/SubmitProjectPage.tsx'
import LoginPage from './pages/LoginPage/LoginPage.tsx'
import VerifyUserPage from "./pages/VerifyUserPage/VerifyUserPage.tsx"
import HomePage from "./pages/HomePage/HomePage.tsx";
import DashboardNavbar from "./components/ui/DashboardNavbar/DashboardNavbar.tsx";
import Footer from "./components/ui/Footer/Footer.tsx";
import useLocalStorage from "./hooks/useLocalStorage.ts";
import useAuthenticationCheck from "./hooks/useAuthenticationCheck.ts";
import {useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";
import DecodedToken from "./interfaces/DecodedToken.ts";
import NotFoundPage from "./pages/NotFoundPage/NotFoundPage.tsx";
import PasswordResetPage from "./pages/PasswordResetPage/PasswordResetPage.tsx";
import ForgotPasswordPage from "./pages/ForgotPasswordPage/ForgotPasswordPage.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function App() {
    const isVerifyPage = useLocation().pathname.includes('verify');
    const isDashboardPage = useLocation().pathname.includes('dashboard');
    const isResetPasswordPage = useLocation().pathname.includes('reset-password');
    const isForgotPasswordPage = useLocation().pathname.includes('forgot-password');

    const [accessToken] = useLocalStorage<string>('accessToken', '');
    const [isAuthenticated] = useAuthenticationCheck(accessToken)
    const [isAdmin, setIsAdmin] = useState<boolean>(false)

    // Decode the token to check if the user is an admin
    useEffect(() => {
        if (!isAuthenticated)
            return

        let decoded : DecodedToken = jwtDecode(accessToken)

        if (!decoded) {
            setIsAdmin(false)
            return
        }

        let isAdmin = decoded.roles.includes('PRIVILEGED')
        setIsAdmin(isAdmin)
    }, [isAuthenticated, accessToken, isAdmin])


    return (
        <>
                {!isVerifyPage && !isDashboardPage && !isResetPasswordPage && !isForgotPasswordPage &&
                    <DashboardNavbar
                        isAuthenticated={isAuthenticated}
                        isAdmin={isAdmin}
                    />
                }


                    <Routes>
                        {/* Public Routes */}
                        <Route path="/">
                            <Route index element={<HomePage />} />
                            <Route path="dashboard" element={<Dashboard isAdmin={isAdmin} isAuthenticated={isAuthenticated} />} />
                            <Route path="about" element={<AboutPage />} />
                            <Route path="verify" element={<VerifyUserPage />} />
                            <Route path="submit-project" element={<SubmitProjectPage />} />
                            <Route path="forgot-password" element={<ForgotPasswordPage />} />
                            <Route path="reset-password" element={<PasswordResetPage />} />
                            <Route path="admin" element={<AdminPanel />} />
                            <Route path='*' element={<NotFoundPage />} />

                            {/* Non Logged in Routes */}
                            {!isAuthenticated && (
                                <>
                                    <Route path="login" element={<LoginPage />} />
                                    <Route path="register" element={<RegisterPage />} />
                                </>
                            )}
                        </Route>
                    </Routes>

                    {!isVerifyPage && !isDashboardPage && !isResetPasswordPage && !isForgotPasswordPage &&
                        <Footer />
                    }
        </>
    )
}

export default App
