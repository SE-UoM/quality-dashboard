import { Flex, chakra } from "@chakra-ui/react";
import TimerIcon from "../../../assets/icons/components/TimerIcon";
import LinesOfCode from "../../../assets/icons/components/LinesOfCodeIcon";
import FilesIcon from "../../../assets/icons/components/FilesIcon";
import GithubStar from "../../../assets/icons/components/GithubStars";
import ContributionsIcon from "../../../assets/icons/components/ContributionsIcon";

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
    <Flex direction={"column"} height={"100%"} justifyContent={"center"}>
      <chakra.span
        mb="auto"
        fontWeight={"bold"}
        fontSize={"3xl"}
        px="0.75rem"
        pt="0.15rem"
      >
        Most Forked Project
      </chakra.span>
      <Flex
        mb="auto"
        justifyContent={"center"}
        direction={"row"}
        alignItems={"center"}
        px="1rem"
        columnGap={"1rem"}
      >
        <Flex direction={"column"} alignItems={"center"} rowGap={"1rem"}>
          <chakra.span justifySelf={"center"}>
            <ContributionsIcon height={85} width={85} />
          </chakra.span>
          <chakra.span fontSize={"2xl"} fontWeight={"bold"}>
            {forks}
          </chakra.span>
        </Flex>
        <Flex direction={"column"} rowGap={"0.5rem"}>
          <chakra.span fontSize={"3xl"} fontWeight={"bold"}>
            {projectName}
          </chakra.span>
          <chakra.span fontSize={"lg"} color="grey" mt="-0.5rem">
            By: <chakra.span fontWeight={"bold"}>{devName}</chakra.span>
          </chakra.span>
          <chakra.span fontSize={"xl"}>
            Total Number of Code Smells:{" "}
            <chakra.span fontWeight={"bold"}>{codesmells} </chakra.span>
          </chakra.span>
          <Flex direction={"row"} columnGap={"1rem"}>
            <FilesIcon height={45} width={45} />
            <Flex direction={"column"}>
              <chakra.span fontWeight={"bold"}>{files}</chakra.span>
              <chakra.span>Files</chakra.span>
            </Flex>
            <LinesOfCode height={45} width={45} />
            <Flex direction={"column"}>
              <chakra.span fontWeight={"bold"}>{bytes}</chakra.span>
              <chakra.span>Lines</chakra.span>
            </Flex>
            <TimerIcon height={45} width={45} />
            <Flex direction={"column"}>
              <chakra.span fontWeight={"bold"}>{timeForBest} mins</chakra.span>

              <chakra.span>Required</chakra.span>
            </Flex>
          </Flex>
        </Flex>
      </Flex>
    </Flex>
  );
}

export default MostForkedProject;
