import { Flex, chakra } from "@chakra-ui/react";

interface MostForkedProjectProps {
  forks: number;
  files: number;
  codesmells: number;
  bytes: number;
  timeForBest: number;
  projectName: string;
  devName: string;
}

function MostForkedProject({
  bytes,
  codesmells,
  devName,
  files,
  projectName,
  forks,
  timeForBest,
}: MostForkedProjectProps) {
  return (
    <Flex direction={"column"}>
      <chakra.span fontWeight={"bold"} fontSize={"xl"}>
        Most Starred Project
      </chakra.span>
      <Flex direction={"row"} alignItems={"center"} px="1rem">
        <Flex direction={"column"}>
          Big Fork Icon
          <chakra.span fontSize={"xl"}>{forks}</chakra.span>
        </Flex>
        <Flex direction={"column"}>
          <chakra.span fontSize={"xl"} fontWeight={"bold"}>
            {projectName}
          </chakra.span>
          <chakra.span fontSize={"sm"} color="grey">
            By: {devName}
          </chakra.span>
          <chakra.span fontSize={"md"}>
            Total Number of Code Smells: {codesmells}
          </chakra.span>
          <Flex direction={"row"} columnGap={"0.75rem"}>
            File Icon
            <Flex direction={"column"}>
              <chakra.span>{files}</chakra.span>
              <chakra.span>Files</chakra.span>
            </Flex>
            Bytes Icon
            <Flex direction={"column"}>
              <chakra.span>{bytes}</chakra.span>
              <chakra.span>Files</chakra.span>
            </Flex>
            Effort Icon
            <Flex direction={"column"}>
              <chakra.span>{timeForBest} mins Required</chakra.span>
            </Flex>
          </Flex>
        </Flex>
      </Flex>
    </Flex>
  );
}

export default MostForkedProject;
