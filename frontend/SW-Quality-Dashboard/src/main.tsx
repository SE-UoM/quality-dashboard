import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter, Outlet, Route, Routes } from "react-router-dom"
import HomePage from './pages/HomePage.tsx'
import { ChakraProvider } from '@chakra-ui/react'
import Navbar from './components/Navbar.tsx'
import RegisterPage from './pages/RegisterPage.tsx'
import AdminPanel from './pages/AdminPanel.tsx'
import RegisterOrganisationPage from './pages/RegisterOrganisationPage.tsx'
import AboutPage from './pages/AboutPage.tsx'
import SubmitProjectPage from './pages/SubmitProjectPage.tsx'

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <ChakraProvider>

      <BrowserRouter>
        <Navbar />
        <Routes>
          {/* Public Routes */}
          <Route path="/">
            <Route index element={<HomePage />} />
            <Route path="/about" element={<AboutPage />} />

          </Route>

          {/* Protected Routes */}
          <Route path="/" element={<RequireAuth />}>
            <Route path="/register-organisation" element={<RegisterOrganisationPage />} />
            <Route path="/register" element={<RegisterPage />} />
            <Route path="/admin-panel" element={<AdminPanel />} />
            <Route path="/submit-project" element={<SubmitProjectPage />} />
          </Route>
          {/* <Route path="/admin-login" element={<RegisterPage />} /> */}

        </Routes>
      </BrowserRouter>
    </ChakraProvider>
  </React.StrictMode>,
)


function RequireAuth() {
  return (<div>
    Protected Route
    <Outlet />
  </div>)
}
