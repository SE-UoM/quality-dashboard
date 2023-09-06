import { Flex, chakra } from "@chakra-ui/react";
import React from "react";
import AnimatedCount from "./AnimatedCount";

interface SingleDataTileProps {
  Icon: JSX.Element;
  num: number;
  label: string;
}

function SingleDataTile({ Icon, label, num }: SingleDataTileProps) {
  return (
    <Flex
      direction={"row"}
      height="100%"
      borderRadius={"0.25rem"}
      columnGap={"1rem"}
      alignItems={"center"}
      justifyContent={"space-around"}
      border={"solid 2px black"}
    >
      <chakra.span>{Icon}</chakra.span>
      <Flex direction={"column"} rowGap={"0.25rem"}>
        <chakra.span fontSize={"5xl"} fontWeight={"semibold"}>
          <AnimatedCount count={num} />
        </chakra.span>
        <chakra.span fontSize={"xl"}>{label}</chakra.span>
      </Flex>
    </Flex>
  );
}

export default SingleDataTile;
