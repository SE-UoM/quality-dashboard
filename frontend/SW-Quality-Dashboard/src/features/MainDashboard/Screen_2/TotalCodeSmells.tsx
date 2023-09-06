import { Doughnut } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Box, Flex, chakra } from "@chakra-ui/react";

ChartJS.register(ArcElement, Tooltip, Legend);
const textCenter = {
  id: "textCenter",
  beforeDraw(chart, args, pluginOptions) {
    console.log("Hello THERE");
    const { ctx, data } = chart;
    ctx.save();
    console.log(data);
    ctx.font = "bold 30px sans-serif";
    ctx.fillStyle = "violet";
    ctx.textAlign = "center";
    ctx.textBaseLine = "middle";
    const metaDataset = chart.getDatasetMeta(0);
    console.log(metaDataset);

    ctx.fillText("Total Code Smells", 150, 150);
  },
};
const options = {
  maintainAspectRatio: false,
  cutout: "70%",
  plugins: {
    legend: {
      display: false,
    },

    title: {
      position: "top",
      display: true,
      text: "Total Code Smells",
      font: {
        size: 18,
        weight: "bold",
      }, // Set your title here
    },
  },
};

const data = {
  labels: ["Minimal", "Minor", "Moderate", "Critical", "Unknown", "Code Style"],
  datasets: [
    {
      label: "# of Votes",
      data: [12, 19, 3, 5, 2, 3],
      backgroundColor: ["green", "yellow", "orange", "red", "purple", "blue"],

      borderWidth: 1,
    },
  ],
};

function TotalCodeSmells() {
  return (
    <Flex
      justifyContent={"space-around"}
      width={"75%"}
      height={"75%"}
      padding="1rem"
      mt="2rem"
    >
      <Doughnut data={data} options={options} />

      <ChartLegend
        labels={data.labels}
        colors={data.datasets[0].backgroundColor}
      />
    </Flex>
  );
}

interface LegendProps {
  labels: string[];
  colors: string[];
}
function ChartLegend({ labels, colors }: LegendProps) {
  return (
    <Flex direction={"column"} rowGap="0.5rem" alignSelf={"center"}>
      {labels.map((label, index) => {
        return (
          <Flex columnGap={"2rem"} alignItems={"center"} key={label}>
            <Box
              bg={colors[index]}
              width={"2rem"}
              height={"2rem"}
              borderRadius={"1000000px"}
            ></Box>
            <chakra.span fontWeight={"medium"} fontSize={"xl"}>
              {label}
            </chakra.span>
          </Flex>
        );
      })}
    </Flex>
  );
}

export default TotalCodeSmells;
