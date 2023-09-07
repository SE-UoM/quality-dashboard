import { Box, Flex, chakra } from "@chakra-ui/react";
import { useGetTechDebtStatsQuery } from "../../api/screen2Api";

function TDStats() {
  const { data } = useGetTechDebtStatsQuery("10");
  console.log("THE DATA", data);
  const {
    avgTechDebt,
    avgTechDebtPerLOC,
    minProjectTechDebt,
    maxProjectTechDebt,
  } = data
    ? data
    : {
        avgTechDebt: -1,
        avgTechDebtPerLOC: -1,
        minProjectTechDebt: -1,
        maxProjectTechDebt: -1,
      };

  return (
    <Flex direction={"column"} height="100%">
      {/* title: */}
      <chakra.span fontSize={"3xl"} pl="1rem" pt="0.25rem">
        Tech Debt Stats
      </chakra.span>
      {/* weird behavior around here */}
      <Box width="100%" bg="lightgrey" height="2px"></Box>

      <Flex direction={"column"} px="0.5rem" pt="0.5rem" rowGap={"0.5rem"}>
        <Flex>
          <chakra.span fontWeight={"bold"} fontSize={"lg"}>
            Average Project Tech Debt: &nbsp;
          </chakra.span>
          <chakra.span fontSize={"lg"}>
            {avgTechDebt}
            {"€"}
          </chakra.span>
        </Flex>
        <Flex>
          <chakra.span fontWeight={"bold"} fontSize={"lg"}>
            Min Project Tech Debt: &nbsp;
          </chakra.span>
          <chakra.span fontSize={"lg"}>
            {minProjectTechDebt}
            {"€"}
          </chakra.span>
        </Flex>
        <Flex>
          <chakra.span fontWeight={"bold"} fontSize={"lg"}>
            Max Project Tech Debt: &nbsp;
          </chakra.span>
          <chakra.span fontSize={"lg"}>
            {maxProjectTechDebt}
            {"€"}
          </chakra.span>
        </Flex>
        <Flex>
          <chakra.span fontWeight={"bold"} fontSize={"lg"}>
            Average Tech Debt per LoC &nbsp;
          </chakra.span>
          <chakra.span fontSize={"lg"}>
            {avgTechDebtPerLOC}
            {"€"}
          </chakra.span>
        </Flex>
      </Flex>
    </Flex>
  );
}

export default TDStats;
