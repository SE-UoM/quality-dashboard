import { Flex, Grid, GridItem } from "@chakra-ui/react";
import MostActiveDev from "./MostActiveDev";
import MostActiveProject from "./MostActiveProject";
import CommitsGraph from "./CommitsGraph";
import MostStarredProject from "./MostStarredProject";
import MostForkedProject from "./MostForkedProject";
import SinceLastAnalysis from "../../../components/SinceLastAnalysis";
import DashboardLogo from "../../../components/DashboardLogo";
import ShowDeveloper from "./ShowDeveloper";
import UoMLogo from "../../../components/UoMLogo";
import OpenSourceUomLogo from "../../../components/OpenSourceUomLogo";

const templateAreas = `
"actived actived actived actived cgraph cgraph cgraph cgraph cgraph cgraph cgraph"
"actived actived actived actived cgraph cgraph cgraph cgraph cgraph cgraph cgraph"
"actived actived actived actived cgraph cgraph cgraph cgraph cgraph cgraph cgraph"
"activep activep activep activep cgraph cgraph cgraph cgraph cgraph cgraph cgraph"
"activep activep activep activep cgraph cgraph cgraph cgraph cgraph cgraph cgraph"
"activep activep activep activep cgraph cgraph cgraph cgraph cgraph cgraph cgraph"
"starred starred starred starred forked forked forked forked devs devs devs"
"starred starred starred starred forked forked forked forked devs devs devs"
"starred starred starred starred forked forked forked forked devs devs devs" 
"logo logo logo logo space space space logo2 logo2 logo2 logo2"
"logo logo logo logo space space space logo2 logo2 logo2 logo2"
`;

function Dashboard4() {
  return (
    <Grid
      height={"92vh"}
      padding={"1rem"}
      templateAreas={templateAreas}
      templateColumns={"repeat(11,1fr)"}
      templateRows={"1fr 1fr 1fr 1fr 1fr 1fr 1fr 1fr 10rem"}
      gap="1rem"
      color="txt"
    >
      <GridItem gridArea={"actived"} borderRadius={"0.5rem"} bg="white">
        <MostActiveDev
          devName="GeorgeFkd"
          imgUrl="https://avatars.githubusercontent.com/u/69716466?v=4"
          numberOfContributions={150}
          numberOfProjects={20}
        />
      </GridItem>
      <GridItem gridArea="cgraph" borderRadius={"0.5rem"} bg="white">
        <CommitsGraph />
      </GridItem>
      <GridItem gridArea={"activep"} borderRadius={"0.5rem"} bg="white">
        <MostActiveProject
          devName="GeorgeFkd"
          imgUrl="https://avatars.githubusercontent.com/u/69716466?v=4"
          numberOfContributions={150}
          numberOfProjects={20}
        />
      </GridItem>
      <GridItem gridArea={"starred"} borderRadius={"0.5rem"} bg="white">
        <MostStarredProject
          bytes={1230}
          files={23}
          stars={226}
          timeForBest={10}
          projectName="Camunda Plugin"
          devName="GeorgeFkd"
          codesmells={10}
        />
      </GridItem>
      <GridItem gridArea={"forked"} borderRadius={"0.5rem"} bg="white">
        <MostForkedProject
          bytes={1230}
          files={23}
          forks={226}
          timeForBest={10}
          projectName="Camunda Plugin"
          devName="GeorgeFkd"
          codesmells={10}
        />
      </GridItem>
      <GridItem gridArea={"devs"} borderRadius={"0.5rem"} bg="white">
        <ShowDeveloper />
      </GridItem>

      <GridItem gridArea={"logo"} borderRadius={"0.5rem"} bg="white">
        <DashboardLogo imageName="uom" width={"100%"} height={"100%"} />
      </GridItem>
      <GridItem gridArea={"space"} borderRadius={"0.5rem"} bg="white">
        <SinceLastAnalysis since="16/08/2023" />
      </GridItem>
      <GridItem gridArea={"logo2"} borderRadius={"0.5rem"} bg="white">
        <Flex
          alignItems={"center"}
          justifyContent={"center"}
          mt="0.5rem"
          columnGap={"1rem"}
        >
          <UoMLogo />
          <OpenSourceUomLogo />
        </Flex>
      </GridItem>
    </Grid>
  );
}

export default Dashboard4;
