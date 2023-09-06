import { Box, Divider, Flex, chakra } from "@chakra-ui/react";

function BestPractice() {
  return (
    <Flex
      direction={"column"}
      pt="1.5rem"
      border="solid 2px black"
      height="100%"
      width="100%"
    >
      <Flex width={"100%"} direction={"column"}>
        <chakra.span
          pb="1rem"
          fontSize={"3xl"}
          fontWeight={"semibold"}
          textAlign={"center"}
        >
          Best Practices
        </chakra.span>
        <chakra.hr
          width={"100%!important"}
          bg={"black"}
          height="2px"
        ></chakra.hr>
        <chakra.span
          py="1rem"
          fontSize={"4xl"}
          fontWeight={"semibold"}
          textAlign={"center"}
        >
          Avoid Magic Numbers
        </chakra.span>
        <chakra.p
          px="2rem"
          alignSelf="center"
          fontSize={"2xl"}
          fontWeight={"medium"}
          textAlign={"center"}
          width={"80%"}
        >
          Replace Plain Numbers with named constants or variables to improve
          code readability and make changes easier in the future
        </chakra.p>
      </Flex>
    </Flex>
  );
}

export default BestPractice;
