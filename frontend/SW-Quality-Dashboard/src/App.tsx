import './App.css'
import {BrowserRouter, Outlet, Route, Routes, useLocation} from "react-router-dom"
import Dashboard from './pages/general/DashboardPage/DashboardPage.tsx'
import RegisterPage from './pages/auth/RegisterPage/RegisterPage.tsx'
import AdminPanel from './pages/admin/AdminPanel/AdminPanel.tsx'
import RegisterOrganisationPage from './pages/admin/RegisterOrganizationPage/RegisterOrganisationPage.tsx'
import SubmitProjectPage from './pages/general/SubmitProjectPage/SubmitProjectPage.tsx'
import AuthPage from './pages/auth/LoginPage/AuthPage.tsx'
import VerifyUserPage from "./pages/auth/VerifyUserPage/VerifyUserPage.tsx"
import HomePage from "./pages/general/HomePage/HomePage.tsx";
import DashboardNavbar from "./components/ui/DashboardNavbar/DashboardNavbar.tsx";
import Footer from "./components/ui/Footer/Footer.tsx";
import useLocalStorage from "./hooks/useLocalStorage.ts";
import useAuthenticationCheck from "./hooks/useAuthenticationCheck.ts";
import {useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";
import DecodedToken from "./interfaces/DecodedToken.ts";
import NotFoundPage from "./pages/general/NotFoundPage/NotFoundPage.tsx";
import PasswordResetPage from "./pages/auth/PasswordResetPage/PasswordResetPage.tsx";
import ForgotPasswordPage from "./pages/auth/ForgotPasswordPage/ForgotPasswordPage.tsx";
import LoadingPage from "./pages/general/LoadingPage/LoadingPage.tsx";
import SimpleUserPanel from "./pages/simpleUser/SimpleUserPanel/SimpleUserPanel.tsx";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

function App() {
    const isVerifyPage = useLocation().pathname.includes('verify');
    const isDashboardPage = useLocation().pathname.includes('dashboard');
    const isResetPasswordPage = useLocation().pathname.includes('reset-password');
    const isForgotPasswordPage = useLocation().pathname.includes('forgot-password');
    const isAdminPage = useLocation().pathname.includes('admin');
    const isAuthPage = useLocation().pathname.includes('login') || useLocation().pathname.includes('register');

    const [accessToken] = useLocalStorage<string>('accessToken', '');
    const [isAuthenticated] = useAuthenticationCheck(accessToken)
    const [isAdmin, setIsAdmin] = useState<boolean>(false)

    const [loading, setLoading] = useState<boolean>(false)

    console.log(isAuthenticated)

    // Make sure the isAuthenticated state is true. If not remove the tokens from the local storage
    // useEffect(() => {
    //     if (!isAuthenticated) {
    //         localStorage.removeItem('accessToken')
    //         localStorage.removeItem('refreshToken')
    //     }
    // }, [accessToken, isAuthenticated])

    // Decode the token to check if the user is an admin
    useEffect(() => {
        if (!isAuthenticated) {
            setIsAdmin(false)
            return
        }

        let decoded : DecodedToken = jwtDecode(accessToken)

        if (!decoded) {
            setIsAdmin(false)
            return
        }

        let isAdmin = decoded.roles.includes('PRIVILEGED')
        setIsAdmin(isAdmin)
    }, [isAuthenticated, accessToken, isAdmin])

    setTimeout(() => {
        setLoading(false);
    }, 1500);

    return (
        <>
            {loading && !isDashboardPage ? (
                <LoadingPage />
            ) : (
                <>
                    {
                        !isVerifyPage &&
                        !isDashboardPage &&
                        !isResetPasswordPage &&
                        !isForgotPasswordPage &&
                        !isAuthPage &&
                        <DashboardNavbar
                            isAuthenticated={isAuthenticated}
                            isAdmin={isAdmin}
                        />
                    }

                    <Routes>
                        <Route path="/">
                            {/* Public Routes */}
                            <Route index element={<HomePage />} />
                            <Route path="verify" element={<VerifyUserPage />} />
                            <Route path="forgot-password" element={<ForgotPasswordPage />} />
                            <Route path="reset-password" element={<PasswordResetPage />} />
                            <Route path="admin" element={<AdminPanel />} />

                            <Route path="submit-project" element={<SubmitProjectPage />} />
                            <Route path="dashboard" element={<Dashboard isAdmin={isAdmin} isAuthenticated={isAuthenticated} />} />
                            <Route path='*' element={<NotFoundPage />} />

                            {/* Non Logged in Routes */}
                            {!isAuthenticated && (
                                <>
                                    <Route path="/login" element={<AuthPage isAuth={isAuthenticated} />} />
                                    <Route path="/register" element={<AuthPage isAuth={isAuthenticated} />} />
                                </>
                            )}
                        </Route>
                    </Routes>

                    {
                        !isVerifyPage &&
                        !isDashboardPage &&
                        !isResetPasswordPage &&
                        !isForgotPasswordPage &&
                        !isAdminPage &&
                        !isAuthPage &&
                        <Footer />
                    }
                </>
            )}
        </>
    )
}

export default App
