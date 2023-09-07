import { Flex, Avatar, chakra } from "@chakra-ui/react";
import GithubIcon from "../../../assets/icons/GithubIcon";
import { useGetMostActiveDevelopersQuery } from "../../api/screen4Api";

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
  console.log(data);
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

export default MostActiveDev;
