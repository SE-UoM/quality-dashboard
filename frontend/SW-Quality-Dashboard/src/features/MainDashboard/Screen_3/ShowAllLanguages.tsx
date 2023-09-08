import { Doughnut } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import { Box, Flex, chakra } from "@chakra-ui/react";
import { useGetTopLanguagesQuery } from "../../api/screen3Api";

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
  cutout: "60%",
  plugins: {
    legend: {
      display: false,
    },
    textCenter: textCenter,
    title: {
      position: "top",
      display: true,
      text: "Total Languages",
      font: {
        size: 18,
        weight: "bold",
      }, // Set your title here
    },
  },
};

const data = {
  labels: ["Java", "C++", "C", "Python", "Haskell", "Rust"],
  datasets: [
    {
      label: "# of Votes",
      data: [12, 19, 3, 5, 2, 3],
      backgroundColor: ["green", "yellow", "orange", "red", "purple", "blue"],

      borderWidth: 1,
    },
  ],
};

function getRandomColor() {
  const randomLetters = "23456789abcde".split("");
  const letter1 =
    randomLetters[Math.floor(Math.random() * randomLetters.length)];
  const letter2 =
    randomLetters[Math.floor(Math.random() * randomLetters.length)];
  const letter3 =
    randomLetters[Math.floor(Math.random() * randomLetters.length)];
  const letter4 =
    randomLetters[Math.floor(Math.random() * randomLetters.length)];
  const letter5 =
    randomLetters[Math.floor(Math.random() * randomLetters.length)];
  const letter6 =
    randomLetters[Math.floor(Math.random() * randomLetters.length)];
  return "#" + letter1 + letter2 + letter3 + letter4 + letter5 + letter6;
}

function extendColorArray(colors: string[], size: number) {
  if (colors.length > size) return colors.slice(0, size);
  if (colors.length === size) return colors;

  const extendBy = size - colors.length;
  let arr = [];
  for (let i = 0; i < extendBy; i++) {
    let randColor = getRandomColor();
    console.log("Got random Color", randColor);
    if (randColor === "#ffffff" || randColor === "#000000")
      randColor = getRandomColor();
    arr.push(randColor);
  }
  return colors.concat(arr);
}

function getLabelName(name: string) {
  if (name === "web") return "HTML";
  return name;
}

function ShowAllLanguages() {
  const { data: theData } = useGetTopLanguagesQuery("10");
  console.log("Languages: ", theData);
  //languageDistribution:{name,linesofCode}
  const totalLanguages = theData ? theData.totalLanguages : -1;
  const extraRems = 4 - Math.abs(totalLanguages).toString().length;
  const langData = theData ? theData.languageDistribution : [];
  const labels = langData.map((lang) => lang.name);
  const linesOfCodePerLang = langData.map((lang) => lang.linesOfCode);
  const backgroundColors = [
    "#b5da54",
    "#91d2fb",
    "#d998cb",
    "#5bb9e6",
    "#fbc16e",
  ];
  const theColors = extendColorArray(backgroundColors, totalLanguages);

  const theActualData = {
    labels,
    datasets: [
      {
        label: "Total Languages",
        data: linesOfCodePerLang,
        backgroundColor: theColors,
      },
    ],
  };
  return (
    <Flex
      width={"100%"}
      height={"100%"}
      py="1rem"
      justifyContent={"space-around"}
      position={"relative"}
    >
      <Doughnut data={theActualData} options={options} />
      <Flex
        direction={"column"}
        justifyItems={"center"}
        position={"absolute"}
        bottom={"30%"}
        left="22%"
      >
        <chakra.span w="9rem" fontSize={"xl"}>
          <chakra.span
            fontWeight={"bold"}
            fontSize={"7xl"}
            textAlign={"center"}
            pl={extraRems + "rem"}
          >
            {totalLanguages}

            <br />
          </chakra.span>
        </chakra.span>
      </Flex>
      <ChartLegend
        labels={theActualData.labels}
        colors={theActualData.datasets[0].backgroundColor}
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

export default ShowAllLanguages;
