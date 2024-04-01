import './App.css'
import {BrowserRouter, Outlet, Route, Routes, useLocation} from "react-router-dom"
import Dashboard from './pages/Dashboard.tsx'
import { ChakraProvider } from '@chakra-ui/react'
import RegisterPage from './pages/RegisterPage/RegisterPage.tsx'
import AdminPanel from './pages/AdminPanel.tsx'
import RegisterOrganisationPage from './pages/RegisterOrganisationPage.tsx'
import AboutPage from './pages/AboutPage.tsx'
import SubmitProjectPage from './pages/SubmitProjectPage/SubmitProjectPage.tsx'
import LoginPage from './pages/LoginPage/LoginPage.tsx'
import VerifyUserPage from "./pages/VerifyUserPage.tsx"
import HomePage from "./pages/HomePage/HomePage.tsx";
import DashboardNavbar from "./components/ui/DashboardNavbar/DashboardNavbar.tsx";
import Footer from "./components/ui/Footer/Footer.tsx";
import useLocalStorage from "./hooks/useLocalStorage.ts";
import useAuthenticationCheck from "./hooks/useAuthenticationCheck.ts";
import {useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";
import DecodedToken from "./interfaces/DecodedToken.ts";
import NotFoundPage from "./pages/NotFoundPage/NotFoundPage.tsx";

function App() {
    const isVerifyPage = window.location.pathname.includes('verify');
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
        console.log(isAdmin)
    }, [isAuthenticated, accessToken, isAdmin])


    return (
        <>
            <ChakraProvider>
                {!isVerifyPage &&
                    <DashboardNavbar
                        isAuthenticated={isAuthenticated}
                        isAdmin={isAdmin}
                    />
                }
                <BrowserRouter>
                    <Routes>
                        {/* Public Routes */}
                        <Route path="/">
                            <Route index element={<HomePage />} />
                            <Route path="dashboard" element={<Dashboard />} />
                            <Route path="about" element={<AboutPage />} />
                            <Route path="register" element={<RegisterPage />} />
                            <Route path="login" element={<LoginPage />} />
                            <Route path="verify" element={<VerifyUserPage />} />
                            <Route path="submit-project" element={<SubmitProjectPage />} />
                            <Route path='*' element={<NotFoundPage />} />

                            {/* Protected Routes */}
                            {isAuthenticated && (
                                <>
                                    <Route path="register-organisation" element={<RegisterOrganisationPage />} />
                                    <Route path="admin-panel" element={<AdminPanel />} />

                                </>
                            )}
                        </Route>
                    </Routes>
                </BrowserRouter>

                {!isVerifyPage && <Footer />}
            </ChakraProvider>
        </>
    )
}

export default App
