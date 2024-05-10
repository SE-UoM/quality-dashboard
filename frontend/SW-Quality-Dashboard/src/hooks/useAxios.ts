import { useState, useEffect } from 'react';
import axios from 'axios';

function useAxiosGet(apiURL: string, token: string) {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [errorMessage, setErrorMessage] = useState(null);

    useEffect(() => {
        async function fetchData() {
            setLoading(true);

            let headers = {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }

            try {
                const response = await axios.get(apiURL, {headers: headers});
                setData(response.data);

                // Wait 1 second to simulate loading
                setTimeout(() => {
                    setLoading(false);
                }, 1000);
            } catch (error) {
                setError(error);
                setErrorMessage(error.message);
                setLoading(false);
            }
        }

        fetchData();

    }, [apiURL, token]);



    return { data, loading, error, errorMessage };
}

export default useAxiosGet;