import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import { BrowserRouter, Route, Routes } from "react-router-dom"
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
          <Route path="/" element={<HomePage />} />
          {/* <Route path="/admin-login" element={<RegisterPage />} /> */}
          <Route path="/register-organisation" element={<RegisterOrganisationPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/admin-panel" element={<AdminPanel />} />
          <Route path="/about" element={<AboutPage />} />
          <Route path="/submit-project" element={<SubmitProjectPage />} />
        </Routes>
      </BrowserRouter>
    </ChakraProvider>
  </React.StrictMode>,
)
