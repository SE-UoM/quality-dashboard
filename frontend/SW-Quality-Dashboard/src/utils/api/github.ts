import apiUrls from "../../assets/data/api_urls.json";
import {jwtDecode} from "jwt-decode";
import axios from "axios";

const apiURL = "https://api.github.com";
const GH_TOKEN = import.meta.env.VITE_GITHUB_TOKEN;

export default function getRepoFromGithubAPI (owner, repo) {
    let url = apiURL + `/repos/${owner}/${repo}`;

    let headers = {
        'Authorization': 'Bearer ' + GH_TOKEN,
        'Content-Type': 'application/json'
    }

    return axios.get(url, { headers: headers });
};