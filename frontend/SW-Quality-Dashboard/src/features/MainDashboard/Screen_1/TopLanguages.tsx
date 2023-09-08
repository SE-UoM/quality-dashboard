import { Flex, chakra } from "@chakra-ui/react";
import { useGetTopLanguagesQuery } from "../../api/screen1Api";
import FirstMedal from "../../../assets/icons/components/FirstMedal";
import SecondMedal from "../../../assets/icons/components/SecondMedal";
import ThirdMedal from "../../../assets/icons/components/ThirdMedal";

function getIconOfLanguage(lang: string) {}

interface LangBarProps {
  langname: string;
  color: string;
  rank: number;
}

function getColorOfRank(rank: number) {
  return "lang" + rank;
}

function LanguageBar({ langname, rank }: LangBarProps) {
  let height = 25 - rank * 5;
  let colorOfBar = getColorOfRank(rank);
  return (
    <Flex direction={"column"} alignItems={"center"}>
      {/* the icon will go here */}
      {rank === 1 && <FirstMedal height={70} width={70} />}
      {rank === 2 && <SecondMedal height={70} width={70} />}
      {rank === 3 && <ThirdMedal height={70} width={70} />}
      <chakra.span fontWeight={"semibold"}>{langname}</chakra.span>
      <chakra.div
        bg={colorOfBar}
        height={`${height}rem`}
        width="5.5rem"
        borderRadius={"0.25rem"}
      ></chakra.div>
    </Flex>
  );
}

function makeNameMoreUnderstandable(name: string) {
  if (name === "web") return "HTML";
  return name;
}

function TopLanguages() {
  const { data, isLoading } = useGetTopLanguagesQuery("10");
  console.log("The data for Top Languages is: ", data);
  // console.log("The keys are: ", Object.keys(data));
  // console.log("First:", data["1"]);
  let thedata = data
    ? data
    : {
        "1": {
          name: "null",
        },
        "2": {
          name: "null",
        },
        "3": {
          name: "null",
        },
      };
  const map = new Map(Object.entries(thedata));
  console.log("The map is: ", map.get("1"));
  const firstLanguage = makeNameMoreUnderstandable(map.get("1")?.name);
  const secondLanguage = makeNameMoreUnderstandable(map.get("2")?.name);
  const thirdLanguage = makeNameMoreUnderstandable(map.get("3")?.name);
  // const firstLanguage = "javascript";
  // const secondLanguage = "javascript";
  // const thirdLanguage = "javascript";

  return (
    <Flex
      direction={"column"}
      p={4}
      borderRadius={"0.5rem"}
      height={"100%"}
      width={"100%"}
    >
      <chakra.h2
        color="txt"
        textAlign={"center"}
        fontSize={"4xl"}
        fontWeight={"semibold"}
      >
        Top Languages
      </chakra.h2>
      <Flex
        direction={"row"}
        width={"100%"}
        mt="auto"
        alignItems={"flex-end"}
        justifyContent={"space-around"}
        columnGap={"1rem"}
      >
        <LanguageBar langname={secondLanguage} rank={2} color="blue" />
        <LanguageBar langname={firstLanguage} rank={1} color="yellow" />
        <LanguageBar langname={thirdLanguage} rank={3} color="red" />
      </Flex>
    </Flex>
  );
}

export default TopLanguages;
