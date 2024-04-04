import React from 'react'
import ReactDOM from 'react-dom'
import App from './App'
import {createTheme, ThemeProvider} from "@mui/material";
import {BrowserRouter} from "react-router-dom";

const theme = createTheme({
    palette: {
        // Define your palette colors here
    },
    // Other theme properties
});

ReactDOM.render(
    <ThemeProvider theme={theme}>
        <BrowserRouter>
            <App />
        </BrowserRouter>
    </ThemeProvider>,
    document.getElementById('root')
)
