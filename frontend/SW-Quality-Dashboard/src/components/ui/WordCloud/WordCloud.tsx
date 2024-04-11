import React, { useState, useEffect } from 'react';
import './WordCloud.css';
import {Spinner} from "react-bootstrap";


const WordCloud = ({ words, loading }) => {
    const [wordStyles, setWordStyles] = useState([]);

    // Function to calculate the size of the word based on its frequency
    const calculateSize = (frequency) => {
        return frequency * 10; // Adjust the scaling factor as needed
    };

    // Function to generate random color
    const getRandomColor = () => {
        const letters = '0123456789ABCDEF';
        let color = '#';
        for (let i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    };

    // Function to generate random rotation angle
    const getRandomRotation = () => {
        return Math.floor(Math.random() * 90) - 45; // Random angle between -45deg and 45deg
    };

    // Function to generate random font family
    const getRandomFont = () => {
        const fonts = ['Arial', 'Helvetica', 'Times New Roman', 'Courier New', 'Verdana'];
        return fonts[Math.floor(Math.random() * fonts.length)];
    };

    // Function to get random floating point number between 1 and 5
    const getRandomFontSize = () => {
        return (Math.random() * 3) + 1; // Random number between 1 and 5
    }

    // Generate a random subset of words to be bold
    const boldWords = words.filter(() => Math.random() < 0.5); // Adjust the probability as needed

    useEffect(() => {
        const interval = setInterval(() => {
            // Update styles for each word
            const updatedWordStyles = words.map((word) => ({
                fontSize: `${getRandomFontSize()}em`,
                color: getRandomColor(),
                display: 'inline-block',
                margin: '5px',
                transform: `rotate(${getRandomRotation()}deg)`,
                fontWeight: boldWords.includes(word) ? 'bold' : 'normal',
                fontFamily: getRandomFont(),
            }));

            setWordStyles(updatedWordStyles);
        }, 3000); // Adjust the interval as needed

        return () => clearInterval(interval); // Cleanup function to clear interval
    }, [words, boldWords]);

    return (
        <div className="word-cloud" style={{ overflow: 'hidden' }} id={loading ? 'loading' : ''}>

            {loading &&
                <Spinner animation="border" role="status">
                    <span className="sr-only">Loading...</span>
                </Spinner>
            }
            {words.map((word, index) => (
                <span
                    key={index}
                    className="word"
                    style={wordStyles[index]}
                >
                    {word}
                </span>
            ))}
        </div>
    );
};

export default WordCloud;
