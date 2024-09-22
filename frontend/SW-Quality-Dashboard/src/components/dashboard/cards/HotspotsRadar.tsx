import {TiRadar} from "react-icons/ti";
import ReactApexChart from "react-apexcharts";
import SimpleDashboardCard from "./SimpleDashboardCard.tsx";
import React from "react";
import useLocalStorage from "../../../hooks/useLocalStorage.ts";
import apiUrls from "../../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";
import useAxiosGet from "../../../hooks/useAxios.ts";

const baseApiUrl = import.meta.env.VITE_API_BASE_URL

const state = {
    options: {
        chart: {
            type: 'radar',
            toolbar: {show: false},
        },
        yaxis: {
            stepSize: 20,
            labels: { show: false }
        },
        xaxis: {
            categories: ['HIGH', 'MEDIUM', 'NORMAL', 'UNKNOWN', 'LOW']
        }
    },


};

function HotspotsRadar({gridArea, data}) {
    return (
        <>
            <SimpleDashboardCard
                className=""
                id={"radarChart"}
                style={{height: "100%", gridArea: gridArea}}
            >
                <h4 style={{
                    width: "100%",
                    display: "flex",
                    flexDirection: "row",
                    alignItems: "center",
                    gap: "0.5em"
                }}>
                    <TiRadar style={{height: "100%", justifySelf: "center"}}/> Hotspots Priority Radar
                </h4>
                {data && <ReactApexChart options={state.options} series={data} type="radar" height={
                    // calculate height to be 10% of the screen height
                    Math.round(window.innerHeight * 0.3)
                }/>}
            </SimpleDashboardCard>
        </>
    );
}

export default HotspotsRadar;