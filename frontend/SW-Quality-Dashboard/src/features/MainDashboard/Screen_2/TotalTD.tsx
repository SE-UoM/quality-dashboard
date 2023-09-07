import { Flex, chakra } from "@chakra-ui/react";
import AnimatedCount from "../../../components/AnimatedCount";
import { useGetTotalTechDebtQuery } from "../../api/screen2Api";
function TotalTD() {
  const { data } = useGetTotalTechDebtQuery("10");
  const { totalTechDebtCost } = data
    ? data
    : {
        totalTechDebtCost: -1,
      };
  console.log(data);
  return (
    <Flex direction={"column"}>
      <chakra.span
        fontSize={"2xl"}
        mb="1rem"
        fontWeight={"thin"}
        alignSelf={"center"}
      >
        Monthly Total Tech Debt
      </chakra.span>
      <Flex alignSelf={"center"} justifySelf={"center"}>
        <chakra.span fontSize={"4xl"}>€</chakra.span>
        <chakra.span
          fontSize="8xl"
          fontWeight={"bold"}
          display={"inline"}
          lineHeight={"6.5rem"}
          // mb="-1.5rem"
        >
          <AnimatedCount count={totalTechDebtCost} />
        </chakra.span>
      </Flex>
    </Flex>
  );
}

export default TotalTD;
