import { Flex, chakra } from "@chakra-ui/react";
import AnimatedCount from "../../../components/AnimatedCount";
function TotalTD() {
  return (
    <Flex direction={"column"} border={"solid 2px black"}>
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
          fontSize="9xl"
          fontWeight={"bold"}
          display={"inline"}
          lineHeight={"6.5rem"}
          // mb="-1.5rem"
        >
          <AnimatedCount count={12256} />
        </chakra.span>
      </Flex>
    </Flex>
  );
}

export default TotalTD;
