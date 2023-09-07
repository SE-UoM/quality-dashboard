import { Doughnut } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Box, Flex, chakra } from "@chakra-ui/react";
import { useGetCodeSmellsQuery } from "../../api/screen2Api";

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
      text: "Code Smells",
      font: {
        size: 18,
        weight: "bold",
      }, // Set your title here
    },
  },
};

const data = {
  labels: ["Minor", "Major", "Critical", "Blocker", "Info"],
  datasets: [
    {
      label: "# of Votes",
      data: [12, 19, 3, 5, 2, 3],
      backgroundColor: ["#67b279", "#fdd835", "#ff7f50", "#ff5252", "#58bbfb"],

      borderWidth: 1,
    },
  ],
};

function TotalCodeSmells() {
  const { data: codesmells } = useGetCodeSmellsQuery("10");
  const distribution = codesmells
    ? codesmells.codeSmellsDistribution
    : [{ severity: "Null", count: "-1" }];
  console.log("Hey", distribution);
  const labels = distribution.map(
    (severityWithCount: any) => severityWithCount.severity
  );
  const thedata = labels.map(
    (label: string) =>
      distribution.find(
        (severityWithCount: any) => severityWithCount.severity === label
      )?.count
  );
  const bgColors = ["#67b279", "#fdd835", "#ff7f50", "#ff5252", "#58bbfb"];
  const datalabel = "# of Code Smells";

  const allData = {
    labels,
    datasets: [
      {
        label: datalabel,
        data: thedata,
        backgroundColor: bgColors,
      },
    ],
  };
  console.log(thedata, "HELLLOOOOOO");
  return (
    <Flex
      justifyContent={"space-around"}
      width={"75%"}
      height={"75%"}
      ml="3rem"
      mt="2rem"
      position={"relative"}
    >
      <Flex alignItems={"center"}>
        <chakra.span
          position={"absolute"}
          bottom={"40%"}
          right="45%"
          w="9rem"
          fontSize={"xl"}
        >
          Total Code Smells:
          <chakra.span fontWeight={"bold"}>
            {codesmells ? codesmells.totalCodeSmells : -1}
          </chakra.span>
        </chakra.span>
      </Flex>
      <Doughnut data={allData} options={options} />

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
  const colorsOfChart = ["minor", "major", "critical", "blocker", "info"];

  return (
    <Flex direction={"column"} rowGap="0.5rem" alignSelf={"center"}>
      {labels.map((label, index) => {
        return (
          <Flex columnGap={"2rem"} alignItems={"center"} key={label}>
            <Box
              bg={"level." + colorsOfChart[index]}
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
