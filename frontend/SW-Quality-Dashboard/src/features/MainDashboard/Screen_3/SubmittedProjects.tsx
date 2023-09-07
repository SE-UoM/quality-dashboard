import { Box, Flex, chakra, useInterval } from "@chakra-ui/react";
import GithubStar from "../../../assets/icons/components/GithubStars";
import ContributionsIcon from "../../../assets/icons/components/ContributionsIcon";
import { useGetProjectsInfoQuery } from "../../api/screen3Api";
import { useState } from "react";
import getNextIndex from "../../../utils/getNextIndex";
//all of this will be swippable probably

interface DisplayProjectProps {
  devName: string;
  repoName: string;
  stars: number;
  forks: number;
  contributions: number;
}

function DisplayProject({
  devName,
  forks,
  repoName,
  stars,
  contributions,
}: DisplayProjectProps) {
  return (
    <Flex direction={"column"} mx="auto">
      <Box>
        <chakra.span fontSize={"2xl"}>{devName}/</chakra.span>
        <chakra.span fontSize={"2xl"} fontWeight={"bold"}>
          {repoName}
        </chakra.span>
      </Box>
      <Flex direction={"row"} columnGap={"2rem"}>
        <Flex columnGap={"1rem"} alignItems={"center"}>
          <GithubStar width={50} height={50} />
          <Flex direction={"column"}>
            <chakra.span fontWeight={"semibold"}>{stars}</chakra.span>
            <chakra.span fontWeight={"semibold"}>stars</chakra.span>
          </Flex>
        </Flex>

        <Flex columnGap={"1rem"} alignItems={"center"}>
          <ContributionsIcon height={50} width={50} />
          <Flex direction={"column"}>
            <chakra.span fontWeight={"semibold"}>{forks}</chakra.span>
            <chakra.span fontWeight={"semibold"}>forks</chakra.span>
          </Flex>
        </Flex>
        <Flex columnGap={"1rem"} alignItems={"center"}>
          <ContributionsIcon height={50} width={50} />
          <Flex direction={"column"}>
            <chakra.span fontWeight={"semibold"}>{contributions}</chakra.span>
            <chakra.span fontWeight={"semibold"}>Contributions</chakra.span>
          </Flex>
        </Flex>
      </Flex>
    </Flex>
  );
}

function SubmittedProjects() {
  const { data } = useGetProjectsInfoQuery("10");
  const [currentIndexOfProject, setCurrentProjectIndex] = useState(0);
  useInterval(() => {
    setCurrentProjectIndex(getNextIndex(data, currentIndexOfProject));
  }, 5 * 1000);
  console.log("Submitted Projects:", data);
  // owner forks name starts
  const currentProject = data ? data[currentIndexOfProject] : {};
  return (
    <Flex direction={"column"} h="100%">
      <chakra.span fontSize="xl" fontWeight={"medium"} mx="auto" py="0.25rem">
        Submitted Projects
      </chakra.span>
      <Box width="100%" bg="black" height="1.5px"></Box>
      <Flex
        direction={"row"}
        justifyContent={"center"}
        alignItems="center"
        px="1rem"
        height={"100%"}
        columnGap="1rem"
      >
        <GithubIcon />
        <DisplayProject
          devName={currentProject.owner}
          repoName={currentProject.name}
          forks={currentProject.forks}
          stars={currentProject.stars}
          contributions={currentProject.totalContributions}
        />
      </Flex>
    </Flex>
  );
}

function GithubIcon() {
  return (
    <svg width="98" height="96" xmlns="http://www.w3.org/2000/svg">
      <path
        fillRule="evenodd"
        clipRule="evenodd"
        d="M48.854 0C21.839 0 0 22 0 49.217c0 21.756 13.993 40.172 33.405 46.69 2.427.49 3.316-1.059 3.316-2.362 0-1.141-.08-5.052-.08-9.127-13.59 2.934-16.42-5.867-16.42-5.867-2.184-5.704-5.42-7.17-5.42-7.17-4.448-3.015.324-3.015.324-3.015 4.934.326 7.523 5.052 7.523 5.052 4.367 7.496 11.404 5.378 14.235 4.074.404-3.178 1.699-5.378 3.074-6.6-10.839-1.141-22.243-5.378-22.243-24.283 0-5.378 1.94-9.778 5.014-13.2-.485-1.222-2.184-6.275.486-13.038 0 0 4.125-1.304 13.426 5.052a46.97 46.97 0 0 1 12.214-1.63c4.125 0 8.33.571 12.213 1.63 9.302-6.356 13.427-5.052 13.427-5.052 2.67 6.763.97 11.816.485 13.038 3.155 3.422 5.015 7.822 5.015 13.2 0 18.905-11.404 23.06-22.324 24.283 1.78 1.548 3.316 4.481 3.316 9.126 0 6.6-.08 11.897-.08 13.526 0 1.304.89 2.853 3.316 2.364 19.412-6.52 33.405-24.935 33.405-46.691C97.707 22 75.788 0 48.854 0z"
        fill="#24292f"
      />
    </svg>
  );
}

export default SubmittedProjects;
