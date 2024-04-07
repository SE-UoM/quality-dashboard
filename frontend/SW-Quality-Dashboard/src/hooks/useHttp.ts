import { useState, useEffect } from 'react';
import axios from 'axios';

const useHttp = (method, url, headers = {}) => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            try {
                const response = await axios({
                    method,
                    url,
                    headers,
                });
                setData(response.data);
                setLoading(false);
            } catch (error) {
                setError(error);
                setLoading(false);
            }
        };

        fetchData();

        // Clean up function
        return () => {
            // Cancel any ongoing requests or clear any timeouts/intervals if necessary
        };
    }, [method, url, headers]);

    return { data, loading, error };
};

export const useGet = (url, headers = {}) => {
    return useHttp('GET', url, headers);
};

export const usePost = (url, headers = {}) => {
    const [response, setResponse] = useState(null);

    const postData = async (requestData) => {
        try {
            const response = await axios.post(url, requestData, { headers });
            setResponse(response.data);
        } catch (error) {
            throw new Error(error);
        }
    };

    return { response, postData };
};

export const usePut = (url, headers = {}) => {
    const [response, setResponse] = useState(null);

    const putData = async (requestData) => {
        try {
            const response = await axios.put(url, requestData, { headers });
            setResponse(response.data);
        } catch (error) {
            throw new Error(error);
        }
    };

    return { response, putData };
};

export const useDelete = (url, headers = {}) => {
    const [response, setResponse] = useState(null);

    const deleteData = async () => {
        try {
            const response = await axios.delete(url, { headers });
            setResponse(response.data);
        } catch (error) {
            throw new Error(error);
        }
    };

    return { response, deleteData };
};

export default {useHttp, useDelete, useGet, usePost, usePut};