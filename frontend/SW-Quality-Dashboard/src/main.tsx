import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter, Outlet, Route, Routes } from "react-router-dom"
import Dashboard from './pages/Dashboard.tsx'
import { ChakraProvider } from '@chakra-ui/react'
import { Provider as ReduxProvider } from 'react-redux'
import { store } from './features/api/store'
import RegisterPage from './pages/RegisterPage.tsx'
import AdminPanel from './pages/AdminPanel.tsx'
import RegisterOrganisationPage from './pages/RegisterOrganisationPage.tsx'
import AboutPage from './pages/AboutPage.tsx'
import SubmitProjectPage from './pages/SubmitProjectPage.tsx'
import LoginPage from './pages/LoginPage.tsx'

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <ReduxProvider store={store}>
      <ChakraProvider>
        <BrowserRouter>
          <Routes>
            {/* Public Routes */}
            <Route path="/">
              <Route path="/" element={<Dashboard />} />
              <Route path="/about" element={<AboutPage />} />
              <Route path="/register" element={<RegisterPage />} />
              <Route path="/login" element={<LoginPage />} />
            </Route>

            {/* Protected Routes */}
            <Route path="/" element={<RequireAuth />}>
              <Route path="/register-organisation" element={<RegisterOrganisationPage />} />

              <Route path="/admin-panel" element={<AdminPanel />} />
              <Route path="/submit-project" element={<SubmitProjectPage />} />
            </Route>
            {/* <Route path="/admin-login" element={<RegisterPage />} /> */}

          </Routes>
        </BrowserRouter>
      </ChakraProvider>
    </ReduxProvider>
  </React.StrictMode>,
)


function RequireAuth() {
  return (<div>
    <Outlet />
  </div>)
}
