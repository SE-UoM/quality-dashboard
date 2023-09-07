import { Flex, Grid, GridItem, chakra } from "@chakra-ui/react";
import { useGetLanguageNamesQuery } from "../../api/screen1Api";

function spreadArrOnGrid(items: string[]) {
  if (items.length > 16) return items.slice(0, 16);
  if (items.length === 16) return items;
  //Spread the items in 16 blocks
  let newArr = [];
  for (let i = 0; i < 16; i++) {
    const randIndex = Math.floor(Math.random() * items.length);
    console.log("Iteration: ", i, randIndex);
    const randomLang = items[randIndex];
    newArr.push(randomLang);
  }
  return newArr;
}

const colorsToPickFrom = [
  "blue",
  "cornflowerblue",
  "violet",
  "green",
  "orange",
];

function WordCloud() {
  // const languages = ["Java", "Javascript", "Go", "Rust", "C++"];
  const { data } = useGetLanguageNamesQuery("10");
  const languages = data ? data : ["Java", "Javascript", "Go", "Rust", "C++"];
  console.log(data, "The language");
  const spreadLangs = spreadArrOnGrid(languages);
  const colorsInGrid = spreadArrOnGrid(colorsToPickFrom);
  return (
    <Grid
      templateRows={"repeat(4,1fr)"}
      templateColumns={"repeat(4,1fr)"}
      width={"100%"}
      height={"100%"}
    >
      {spreadLangs.map((lang, index) => (
        <LanguageBlock language={lang} color={colorsInGrid[index]} />
      ))}
    </Grid>
  );
}

interface LangBlockProps {
  language: string;
  color: string;
}
function LanguageBlock({ language, color }: LangBlockProps) {
  const randomRotation = Math.floor(Math.random() * 45);
  const randomDirection = Math.random() > 0.5 ? 1 : 0;
  const deg = randomRotation - randomDirection * 60;
  return (
    <GridItem colSpan={1} rowSpan={1} w="100%" h="100%">
      <Flex justifyContent={"center"} alignItems={"center"} h="100%" w="100%">
        <style>
          {`/* Define the text and its initial state */
        .fade-text {
            opacity: 0;
            font-size: 24px;
            text-align: center;
            margin-top: 20px;
            animation: fadeInOut 3s infinite alternate; /* Apply the animation */
        }

        /* Define the animation keyframes */
        @keyframes fadeInOut {
            0% {
                opacity: 1;

            }
            50% {
              opacity:0;
            }
            100% {
                opacity: 1;
            }
        }`}
        </style>
        <chakra.span
          fontSize={"xl"}
          opacity={"0"}
          color={color}
          animation="fadeInOut 15s linear infinite"
          transform={`rotate(${deg}deg)`}
        >
          {language}
        </chakra.span>
      </Flex>
    </GridItem>
  );
}
export default WordCloud;
