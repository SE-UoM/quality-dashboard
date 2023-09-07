import { Flex, chakra } from "@chakra-ui/react";
import React from "react";
import DateIcon from "../assets/icons/components/DateIcon";

interface SinceLastAnalysisProps {
  since: string;
}

function SinceLastAnalysis({ since }: SinceLastAnalysisProps) {
  return (
    <Flex
      direction={"row"}
      columnGap="0.5rem"
      alignItems={"center"}
      justifyContent={"center"}
      height={"100%"}
      borderRadius={"0.5rem"}
    >
      {/* Render Icon here */}
      <chakra.span>
        <DateIcon height={40} width={40} />
      </chakra.span>
      <Flex direction={"column"}>
        <chakra.span fontSize={"2xl"} fontWeight="semibold">
          {since}
        </chakra.span>
        <chakra.span fontSize="md" fontWeight={"hairline"} color="gray">
          since last analysis
        </chakra.span>
      </Flex>
    </Flex>
  );
}

export default SinceLastAnalysis;
