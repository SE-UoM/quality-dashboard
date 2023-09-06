import { Box, Flex, chakra } from "@chakra-ui/react";
function TDStats() {
  const avgProjectTechDebt = 123.58;
  const minProjectTechDebt = 123.58;
  const maxProjectTechDebt = 123.58;
  const avgTechDebtPerLoc = 123.58;
  return (
    <Flex direction={"column"} border="solid 2px black" height="100%">
      {/* title: */}
      <chakra.span fontSize={"2xl"} pl="1rem" pt="0.25rem">
        Tech Debt Stats
      </chakra.span>
      {/* weird behavior around here */}
      <Box width="100%" bg="lightgrey" height="2px"></Box>

      <Box px="0.5rem" pt="0.5rem">
        <Flex>
          <chakra.span fontWeight={"bold"}>
            Average Project Tech Debt: &nbsp;
          </chakra.span>
          <chakra.span>
            {avgProjectTechDebt}
            {"€"}
          </chakra.span>
        </Flex>
        <Flex>
          <chakra.span fontWeight={"bold"}>
            Min Project Tech Debt: &nbsp;
          </chakra.span>
          <chakra.span>
            {minProjectTechDebt}
            {"€"}
          </chakra.span>
        </Flex>
        <Flex>
          <chakra.span fontWeight={"bold"}>
            Max Project Tech Debt: &nbsp;
          </chakra.span>
          <chakra.span>
            {maxProjectTechDebt}
            {"€"}
          </chakra.span>
        </Flex>
        <Flex>
          <chakra.span fontWeight={"bold"}>
            Average Tech Debt per Line of Code: &nbsp;
          </chakra.span>
          <chakra.span>
            {avgTechDebtPerLoc}
            {"€"}
          </chakra.span>
        </Flex>
      </Box>
    </Flex>
  );
}

export default TDStats;
