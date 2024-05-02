import React, { useEffect, useState } from 'react';
import { PieChart, useDrawingArea } from '@mui/x-charts';
import axios from "axios";
import ErrorModal from "../../../../modals/ErrorModal/ErrorModal.tsx";
import { jwtDecode } from "jwt-decode";
import { styled } from '@mui/material/styles';

const colors = [
    "#b5da54ff",
    "#91d2fbff",
    "#d998cbff",
    "#5bb9e6",
    "#f7a35cff"
];

function formatText(txt) {
    if (!isNaN(txt) && parseInt(txt) > 1000) {
        const num = parseInt(txt);
        const roundedNum = Math.round(num / 100) / 10;
        return parseInt(roundedNum) + "k";
    }
    return txt;
}

const StyledText = styled('text')(({ theme }) => ({
    textAnchor: 'middle',
    dominantBaseline: 'central',
    fontWeight: "bold",
    padding: "1em",
    color: "#404852 !important"
}));

function PieCenterLabel({ children }) {
    const { width, height, left, top } = useDrawingArea();
    return (
        <StyledText className="lang-count" x={left + width / 2} y={top + height / 2}>
            {children}
        </StyledText>
    );
}

function getRelativeSizeOfScreenWidthInPx(percentage) {
    const width = window.innerWidth;
    return (width * percentage) / 100;
}

function getRelativeSizeOfScreenHeightInPx(percentage) {
    const height = window.innerHeight;
    return (height * percentage) / 100;
}

function CustomPieChart({ data, centerText }) {
    const sizingProps = {
        width: getRelativeSizeOfScreenWidthInPx(35),
        height: getRelativeSizeOfScreenHeightInPx(45)
    };

    return (
        <div className="pie-chart-container">
            <PieChart
                series={[{
                    data: data,
                    innerRadius: 104,
                    outerRadius: 144,
                    paddingAngle: 1,
                    cornerRadius: 5,
                    startAngle: -90,
                    highlightScope: { faded: 'global', highlighted: 'item' }
                }]}
                {...sizingProps}
            >
                <PieCenterLabel>{formatText(centerText)}</PieCenterLabel>
            </PieChart>
        </div>
    );
}

export default CustomPieChart;
