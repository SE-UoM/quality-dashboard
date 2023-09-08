import { Box, Flex, chakra } from "@chakra-ui/react";
import FirstMedal from "../../../assets/icons/components/FirstMedal";
import SecondMedal from "../../../assets/icons/components/SecondMedal";
import ThirdMedal from "../../../assets/icons/components/ThirdMedal";
import SimpleMedal from "../../../assets/icons/components/SimpleMedal";
import { useGetTopDevelopersQuery } from "../../api/screen3Api";

function BestDevelopers() {
  const { data: bestDevs } = useGetTopDevelopersQuery("10");
  console.log("data devs:", bestDevs);
  //name,githuburl,//totalCommits,totalCodeSmells
  return (
    <Flex direction={"column"} overflow={"scroll"}>
      <Flex
        alignItems={"center"}
        my="0.5rem"
        justifyContent={"center"}
        columnGap={"1rem"}
      >
        <chakra.span fontWeight={"semibold"}>Top Developers</chakra.span>
        <chakra.span fontWeight={"semibold"}>(Contributions)</chakra.span>
      </Flex>
      <Box width="100%" bg="lightgrey" height="2px"></Box>

      <Flex direction={"column"}>
        {bestDevs &&
          bestDevs
            .filter((item: any) => Boolean(item.name))
            .map((item, index) => (
              <Flex
                key={index}
                w={"100%"}
                direction={"row"}
                columnGap={"0.5rem"}
                alignItems={"center"}
                borderBottom="solid 2px black"
                py="0.25rem"
              >
                <>{console.log("Rank is: ", index)}</>
                <>{console.log("Name is: ", item?.name)}</>
                <chakra.span fontSize={"sm"} ml="1rem">
                  {index === 0 && <FirstMedal height={30} width={30} />}
                  {index === 1 && <SecondMedal height={30} width={30} />}
                  {index === 2 && <ThirdMedal height={30} width={30} />}
                  {index > 2 && (
                    <SimpleMedal height={30} width={30} rank={index + 1} />
                  )}
                </chakra.span>
                <Flex direction="column" ml="0.5rem">
                  <chakra.span fontWeight={"bold"}>
                    {item?.name || "<blank>"}
                  </chakra.span>
                </Flex>
                <chakra.span ml="auto" mr="2rem">
                  ({item?.totalCommits})
                </chakra.span>
              </Flex>
            ))}
      </Flex>
    </Flex>
  );
}

export default BestDevelopers;
