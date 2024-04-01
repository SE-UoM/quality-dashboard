import { useState, useEffect } from 'react';

const useLocalStorage = <T>(key: string, initialValue: T): [T, React.Dispatch<React.SetStateAction<T>>] => {
    const [value, setValue] = useState<T>(() => {
        // Retrieve value from local storage
        const storedValue = localStorage.getItem(key);
        console.log("Retrieved value from local storage:", storedValue);
        return storedValue;
    });

    useEffect(() => {
        // Save value to local storage
        localStorage.setItem(key, value);
    }, [key, value]);

    return [value, setValue];
};

export default useLocalStorage;
