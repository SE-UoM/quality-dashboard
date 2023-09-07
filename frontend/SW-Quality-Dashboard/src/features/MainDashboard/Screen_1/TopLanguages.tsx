import { Flex, NumberInputStepperProps, chakra } from "@chakra-ui/react";
import { useGetTopLanguagesQuery } from "../../api/screen1Api";

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
  let height = 20 - rank * 5;
  let colorOfBar = getColorOfRank(rank);
  return (
    <Flex direction={"column"} alignItems={"center"}>
      {/* the icon will go here */}
      <chakra.span fontWeight={"semibold"}>{langname}</chakra.span>
      <chakra.div
        bg={colorOfBar}
        height={`${height}rem`}
        width="5rem"
        borderRadius={"0.25rem"}
      ></chakra.div>
    </Flex>
  );
}

function TopLanguages() {
  const { data, isLoading } = useGetTopLanguagesQuery("10");
  console.log("The data for Top Languages is: ", data);
  const firstLanguage = "javascript";
  const secondLanguage = "java";
  const thirdLanguage = "C++";

  return (
    <Flex
      direction={"column"}
      p={4}
      borderRadius={"0.5rem"}
      height={"100%"}
      width={"100%"}
    >
      <chakra.h2 textAlign={"center"} fontSize={"4xl"} fontWeight={"semibold"}>
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
