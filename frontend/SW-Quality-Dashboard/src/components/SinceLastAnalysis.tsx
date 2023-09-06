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
      justifyContent={"space-around"}
      height={"100%"}
      borderRadius={"0.5rem"}
    >
      {/* Render Icon here */}
      <chakra.span>
        <DateIcon height={60} width={60} />
      </chakra.span>
      <Flex direction={"column"}>
        <chakra.span fontSize={"3xl"} fontWeight="semibold">
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
