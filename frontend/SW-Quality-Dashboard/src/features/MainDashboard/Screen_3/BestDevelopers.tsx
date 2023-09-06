import { Box, Flex, chakra } from "@chakra-ui/react";
import FirstMedal from "../../../assets/icons/components/FirstMedal";
import SecondMedal from "../../../assets/icons/components/SecondMedal";
import ThirdMedal from "../../../assets/icons/components/ThirdMedal";
import SimpleMedal from "../../../assets/icons/components/SimpleMedal";

function BestDevelopers() {
  const bestDevelopers = [
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd1",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd2",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd3",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd4",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd5",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd6",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd7",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd8",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd9",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd10",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd11",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd12",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd13",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd14",
      codeSmells: 100,
    },
    {
      projectName: "Camunda Plugin",
      devName: "Georgefkd15",
      codeSmells: 100,
    },
  ];
  return (
    <Flex direction={"column"} overflow={"scroll"}>
      <Flex
        alignItems={"center"}
        my="0.5rem"
        justifyContent={"center"}
        columnGap={"1rem"}
      >
        <chakra.span fontWeight={"semibold"}>Top Developers</chakra.span>
        <chakra.span fontWeight={"semibold"}>(Code Smells)</chakra.span>
      </Flex>
      <Box width="100%" bg="lightgrey" height="2px"></Box>

      <Flex direction={"column"}>
        {bestDevelopers.map((item, index) => (
          <Flex
            key={index}
            w={"100%"}
            direction={"row"}
            columnGap={"0.5rem"}
            alignItems={"center"}
            borderBottom="solid 2px black"
            py="0.25rem"
          >
            <>{console.log("Rank is: ", index)}</>
            <>{console.log("Name is: ", item?.devName)}</>
            <chakra.span fontSize={"sm"} ml="1rem">
              {index === 0 && <FirstMedal height={30} width={30} />}
              {index === 1 && <SecondMedal height={30} width={30} />}
              {index === 2 && <ThirdMedal height={30} width={30} />}
              {index > 2 && (
                <SimpleMedal height={30} width={30} rank={index + 1} />
              )}
            </chakra.span>
            <Flex direction="column" ml="0.5rem">
              <chakra.span fontWeight={"bold"}>{item?.projectName}</chakra.span>
              <chakra.span fontSize="0.65rem" color="grey">
                By: {item?.devName}
              </chakra.span>
            </Flex>
            <chakra.span ml="auto" mr="2rem">
              ({item?.codeSmells})
            </chakra.span>
          </Flex>
        ))}
      </Flex>
    </Flex>
  );
}

export default BestDevelopers;
