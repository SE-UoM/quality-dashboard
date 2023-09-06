import { Flex, Avatar, chakra } from "@chakra-ui/react";

interface MostActiveDevProps {
  devName: string;
  numberOfProjects: number;
  numberOfContributions: number;
  imgUrl: string;
}

function MostActiveProject({
  devName,
  imgUrl,
  numberOfContributions,
  numberOfProjects,
}: MostActiveDevProps) {
  return (
    <Flex direction={"column"}>
      <chakra.span fontWeight={"bold"} fontSize={"xl"}>
        Most Active Project
      </chakra.span>
      <Flex
        direction={"row"}
        alignItems={"center"}
        height={"100%"}
        columnGap={"2rem"}
        px="1rem"
      >
        <Avatar name={devName} src={imgUrl} size="lg" />
        <Flex direction={"column"} width={"100%"}>
          <chakra.span fontSize={"xl"} fontWeight="bold" alignSelf={"center"}>
            {devName}
          </chakra.span>
          <Flex direction={"row"} columnGap={"1rem"} justifyContent={"center"}>
            <Flex direction={"column"}>
              <chakra.span fontWeight={"bold"}>{numberOfProjects}</chakra.span>
              <chakra.span>Projects</chakra.span>
              {/* <GithubIcon height="100" width="100" /> */}
            </Flex>
            <Flex direction={"column"}>
              <chakra.span fontWeight={"bold"}>
                {numberOfContributions}
              </chakra.span>
              <chakra.span>Contributions</chakra.span>
              {/* <GithubIcon height="100" width="100" /> */}
            </Flex>
            <Flex direction={"column"}>
              <chakra.span fontWeight={"bold"}>
                {numberOfContributions}
              </chakra.span>
              <chakra.span>Contributions</chakra.span>
              {/* <GithubIcon height="100" width="100" /> */}
            </Flex>
          </Flex>
        </Flex>
      </Flex>
    </Flex>
  );
}

export default MostActiveProject;
