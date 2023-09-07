import { Flex, Box, chakra } from "@chakra-ui/react";
import FirstMedal from "../../../assets/icons/components/FirstMedal";
import SecondMedal from "../../../assets/icons/components/SecondMedal";
import ThirdMedal from "../../../assets/icons/components/ThirdMedal";
import SimpleMedal from "../../../assets/icons/components/SimpleMedal";
import { useGetTopProjectsQuery } from "../../api/screen3Api";
function BestProjects() {
  const { data } = useGetTopProjectsQuery("10");
  console.log("Best projects: ", data);

  return (
    <Flex direction={"column"} overflow={"scroll"}>
      <Flex
        alignItems={"center"}
        my="0.5rem"
        justifyContent={"center"}
        columnGap={"1rem"}
      >
        <chakra.span fontWeight={"semibold"}>Best Projects</chakra.span>
        <chakra.span fontWeight={"semibold"}>(TD per LoC)</chakra.span>
      </Flex>
      <Box width="100%" bg="lightgrey" height="2px"></Box>

      <Flex direction={"column"}>
        {data &&
          data.map((item, index) => (
            <Flex
              w={"100%"}
              direction={"row"}
              columnGap={"0.5rem"}
              alignItems={"center"}
              borderBottom="solid 2px black"
              py="0.25rem"
            >
              <chakra.span fontSize={"sm"} ml="1rem">
                {index === 0 && <FirstMedal height={30} width={30} />}
                {index === 1 && <SecondMedal height={30} width={30} />}
                {index === 2 && <ThirdMedal height={30} width={30} />}
                {index > 2 && (
                  <SimpleMedal height={30} width={30} rank={index + 1} />
                )}
              </chakra.span>
              <Flex direction="column" ml="0.5rem">
                <chakra.span fontWeight={"bold"}>{item.name}</chakra.span>
                <chakra.span fontSize="0.65rem" color="grey">
                  By: {item.owner}
                </chakra.span>
              </Flex>
              <chakra.span ml="auto" mr="2rem">
                ({item.techDebtPerLoc}){"€"}
              </chakra.span>
            </Flex>
          ))}
      </Flex>
    </Flex>
  );
}

export default BestProjects;
