import { Box, Flex, chakra } from "@chakra-ui/react";

function ActivityStats() {
  const avgProjectTechDebt = 123.58;
  const minProjectTechDebt = 123.58;
  const maxProjectTechDebt = 123.58;
  const avgTechDebtPerLoc = 123.58;
  return (
    <Flex direction={"column"} height="100%">
      {/* title: */}
      <chakra.header fontSize={"2xl"} pl="1rem" pt="0.25rem">
        Activity Stats
      </chakra.header>
      {/* it works here and not in TDStats */}
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
export default ActivityStats;
