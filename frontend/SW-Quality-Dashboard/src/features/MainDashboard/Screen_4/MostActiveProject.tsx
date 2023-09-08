import { Flex, Avatar, chakra } from "@chakra-ui/react";
import { useGetMostActiveProjectQuery } from "../../api/screen4Api";
import ContributionsIcon from "../../../assets/icons/components/ContributionsIcon";
import GithubIcon from "../../../assets/icons/components/GithubIcon";

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
  } = data
    ? data
    : {
        files: -1,
        loc: -1,
        totalCommits: -1,
        owner: "null",
        name: "null",
        techDebt: -1,
        totalCodeSmells: -1,
        totalForks: -1,
        stars: -1,
      };
  // files, loc,totalCommits, owner,name,techDebt,totalCodeSmells,totalForks,stars
  return (
    <Flex direction={"column"} w={"100%"}>
      <chakra.span fontWeight={"bold"} fontSize={"xl"} pl="1rem">
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
        <GithubIcon height={100} width={100} />
        <Flex direction={"column"}>
          <chakra.span fontSize={"xl"} fontWeight="bold" alignSelf={"center"}>
            {name}
          </chakra.span>
          <Flex
            direction={"row"}
            width={"100%"}
            columnGap={"1rem"}
            justifyContent={"space-around"}
            alignItems={"center"}
            mx="2rem"
          >
            <ContributionsIcon height={60} width={60} />
            <Flex direction={"column"}>
              <chakra.span
                fontWeight={"bold"}
                fontSize={"xl"}
                textAlign={"center"}
              >
                {totalCommits}
              </chakra.span>
              <chakra.span fontSize={"xl"}>Contributions</chakra.span>
              {/* <GithubIcon height="100" width="100" /> */}
            </Flex>
          </Flex>
        </Flex>
      </Flex>
    </Flex>
  );
}

export default MostActiveProject;
