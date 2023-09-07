import { Flex, Avatar, chakra } from "@chakra-ui/react";
import { useGetMostActiveProjectQuery } from "../../api/screen4Api";

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
  const { data } = useGetMostActiveProjectQuery("10");
  console.log(data, "ACTIVE P");
  const {
    files,
    loc,
    totalCommits,
    owner,
    name,
    techDebt,
    totalCodeSmells,
    totalForks,
    stars,
  } = data;
  // files, loc,totalCommits, owner,name,techDebt,totalCodeSmells,totalForks,stars
  return (
    <Flex direction={"column"} w={"100%"}>
      <chakra.span fontWeight={"bold"} fontSize={"xl"}>
        Most Active Project
      </chakra.span>
      <Flex
        direction={"row"}
        alignItems={"center"}
        height={"100%"}
        w="100%"
        columnGap={"2rem"}
        px="1rem"
      >
        <Avatar name={devName} src={""} size="lg" />
        <Flex direction={"column"} width={"100%"}>
          <chakra.span fontSize={"xl"} fontWeight="bold" alignSelf={"center"}>
            {owner}
          </chakra.span>
          <Flex
            direction={"row"}
            width={"100%"}
            columnGap={"1rem"}
            justifyContent={"center"}
          >
            <Flex direction={"column"}>
              <chakra.span fontWeight={"bold"}>{totalCommits}</chakra.span>
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
