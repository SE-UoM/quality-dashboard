import { Flex } from "@chakra-ui/react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Line } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend
);

const options = {
  maintainAspectRatio: false,
  responsive: true,
  interaction: {
    mode: "index" as const,
    intersect: false,
  },
  stacked: false,
  plugins: {
    title: {
      display: true,
      text: "Monthly Commit Activity of Projects",
    },
  },
  scales: {
    y: {
      type: "linear" as const,
      display: true,
      position: "left" as const,
    },
    y1: {
      type: "linear" as const,
      display: true,
      position: "right" as const,
      grid: {
        drawOnChartArea: false,
      },
    },
  },
};

// Sample data with commits, you can replace this with your actual data
const commitsData = [
  {
    date: "2021-12-17T16:46:21.000+00:00",
    sha: "a2a8fd0c2de7887e7f73326a705709f38614affe",
  },
  {
    date: "2021-12-17T16:37:57.000+00:00",
    sha: "2f92b8b9e4a0c5a4c9a93246955b9ccf9f51dcd0",
  },
  {
    date: "2021-12-17T16:36:11.000+00:00",
    sha: "e498f88b25fa6ffa53194d2c0cc47ab8cf78e86a",
  },
  {
    date: "2021-12-17T16:34:31.000+00:00",
    sha: "b128d22352f1741cd5b1792ef0933697db4e2818",
  },
  {
    date: "2021-12-17T16:29:57.000+00:00",
    sha: "17cf2e3885e30326b29806ec10853e173256aa63",
  },
  {
    date: "2021-12-17T16:27:34.000+00:00",
    sha: "3fa52eaaee86434062633d4c4bcf487a0fa0ce3c",
  },
  {
    date: "2021-12-17T16:23:48.000+00:00",
    sha: "0900f245976da5e83d8a431eed79d7d22457fac6",
  },
  {
    date: "2021-12-17T16:46:21.000+00:00",
    sha: "a2a8fd0c2de7887e7f73326a705709f38614affe",
  },
  // Add more commits here
];

// Function to count commits for each month
function countCommitsByMonth(commitsData) {
  const commitsByMonth = new Array(12).fill(0); // Initialize an array with zeros for each month

  commitsData.forEach((commit) => {
    const date = new Date(commit.date);
    const month = date.getMonth();
    commitsByMonth[month]++;
  });

  return commitsByMonth;
}

const labels = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];

const commitsByMonth = countCommitsByMonth(commitsData);

const data = {
  labels,
  datasets: [
    {
      label: "Commits",
      data: commitsByMonth,
      backgroundColor: "blue",
    },
  ],
};

function CommitsGraph({}) {
  return (
    <Flex width={"100%"} height={"100%"}>
      <Line options={options} data={data} width={"900"} height="500" />
    </Flex>
  );
}

export default CommitsGraph;
