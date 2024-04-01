import { useEffect, useState } from 'react';
import axios from 'axios';

const useAuthenticationCheck = (accessToken: string | null): [boolean, React.Dispatch<React.SetStateAction<boolean>>] => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean>(false);

    useEffect(() => {
        const fetchData = async () => {
            try {
                if (!accessToken) {
                    setIsAuthenticated(false);
                    return;
                }

                const headers = {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                };

                const response = await axios.get('http://localhost:8080/api/organizations', { headers });

                if (response.status === 200) {
                    setIsAuthenticated(true);
                } else {
                    setIsAuthenticated(false);
                }
            } catch (error) {
                setIsAuthenticated(false);
                console.error('Error:', error);
            }
        };

        fetchData();
    }, [accessToken]);

    return [isAuthenticated, setIsAuthenticated];
};

export default useAuthenticationCheck;
