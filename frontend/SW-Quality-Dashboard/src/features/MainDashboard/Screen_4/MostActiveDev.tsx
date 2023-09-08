import { Flex, Avatar, chakra } from "@chakra-ui/react";
import GithubIcon from "../../../assets/icons/GithubIcon";
import { useGetMostActiveDevelopersQuery } from "../../api/screen4Api";
import ContributionsIcon from "../../../assets/icons/components/ContributionsIcon";

interface MostActiveDevProps {
  devName: string;
  numberOfProjects: number;
  numberOfContributions: number;
  imgUrl: string;
}

function MostActiveDev({
  devName,
  imgUrl,
  numberOfContributions,
  numberOfProjects,
}: MostActiveDevProps) {
  const { data } = useGetMostActiveDevelopersQuery("10");
  //totalCommits,totalIssues,avatarUrl,name,issuesPerContribution
  console.log(data, "Most active developers");
  const { totalCommits, totalIssues, avatarUrl, name, issuesPerContribution } =
    data
      ? data
      : {
          totalCommits: -1,
          totalIssues: -1,
          avatarUrl: "",
          name: "",
          issuesPerContribution: -1,
        };
  return (
    <Flex direction={"column"}>
      <chakra.span fontWeight={"bold"} fontSize={"xl"}>
        Most Active Developer
      </chakra.span>
      <Flex
        direction={"row"}
        alignItems={"center"}
        height={"100%"}
        columnGap={"2rem"}
        px="1rem"
      >
        <Avatar name={devName} src={avatarUrl} size="lg" />
        <Flex direction={"column"} width={"100%"}>
          <chakra.span fontSize={"xl"} fontWeight="bold" alignSelf={"center"}>
            {name}
          </chakra.span>
          <Flex
            direction={"row"}
            columnGap={"1rem"}
            justifyContent={"space-around"}
            alignItems={"center"}
          >
            <ContributionsIcon height={65} width={65} />
            <Flex direction={"column"}>
              <chakra.span
                fontWeight={"bold"}
                fontSize={"xl"}
                textAlign={"center"}
              >
                {totalCommits}
              </chakra.span>
              <chakra.span fontSize={"xl"}>Contributions</chakra.span>
            </Flex>
          </Flex>
        </Flex>
      </Flex>
    </Flex>
  );
}

export default MostActiveDev;
