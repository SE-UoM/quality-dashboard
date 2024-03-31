import './App.css'
import {BrowserRouter, Outlet, Route, Routes, useLocation} from "react-router-dom"
import Dashboard from './pages/Dashboard.tsx'
import { ChakraProvider } from '@chakra-ui/react'
import RegisterPage from './pages/RegisterPage/RegisterPage.tsx'
import AdminPanel from './pages/AdminPanel.tsx'
import RegisterOrganisationPage from './pages/RegisterOrganisationPage.tsx'
import AboutPage from './pages/AboutPage.tsx'
import SubmitProjectPage from './pages/SubmitProjectPage.tsx'
import LoginPage from './pages/LoginPage/LoginPage.tsx'
import VerifyUserPage from "./pages/VerifyUserPage.tsx"
import HomePage from "./pages/HomePage/HomePage.tsx";
import DashboardNavbar from "./components/DashboardNavbar/DashboardNavbar.tsx";
import Footer from "./components/Footer/Footer.tsx";

function App() {
    const isVerifyPage = window.location.pathname.includes('verify');

    console.log(isVerifyPage)

    return (
        <>
            <ChakraProvider>
                {!isVerifyPage && <DashboardNavbar />}
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
                        </Route>

                        {/* Protected Routes */}
                        <Route path="/" element={<RequireAuth />}>
                            <Route path="register-organisation" element={<RegisterOrganisationPage />} />
                            <Route path="admin-panel" element={<AdminPanel />} />
                            <Route path="submit-project" element={<SubmitProjectPage />} />
                        </Route>
                        {/* <Route path="/admin-login" element={<RegisterPage />} /> */}

                    </Routes>
                </BrowserRouter>

                {!isVerifyPage && <Footer />}
            </ChakraProvider>
        </>
    )
}

function RequireAuth() {
    return (
        <div>
            <Outlet />
        </div>
    )
}

export default App
