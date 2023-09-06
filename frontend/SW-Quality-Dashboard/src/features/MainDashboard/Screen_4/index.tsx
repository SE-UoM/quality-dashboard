import { Grid, GridItem } from "@chakra-ui/react";
import MostActiveDev from "./MostActiveDev";
import MostActiveProject from "./MostActiveProject";
import CommitsGraph from "./CommitsGraph";
import MostStarredProject from "./MostStarredProject";
import MostForkedProject from "./MostForkedProject";
import SinceLastAnalysis from "../../../components/SinceLastAnalysis";

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
      templateRows={"repeat(11,1fr)"}
      gap="1rem"
    >
      <GridItem gridArea={"actived"} border={"solid 2px black"}>
        <MostActiveDev
          devName="GeorgeFkd"
          imgUrl="https://avatars.githubusercontent.com/u/69716466?v=4"
          numberOfContributions={150}
          numberOfProjects={20}
        />
      </GridItem>
      <GridItem gridArea="cgraph" border={"solid 2px black"}>
        {/* <CommitsGraph /> */}
      </GridItem>
      <GridItem gridArea={"activep"} border={"solid 2px black"}>
        <MostActiveProject
          devName="GeorgeFkd"
          imgUrl="https://avatars.githubusercontent.com/u/69716466?v=4"
          numberOfContributions={150}
          numberOfProjects={20}
        />
      </GridItem>
      <GridItem gridArea={"starred"} border={"solid 2px black"}>
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
      <GridItem gridArea={"forked"} border={"solid 2px black"}>
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
      <GridItem gridArea={"devs"} border={"solid 2px black"}>
        Developers
      </GridItem>

      <GridItem gridArea={"logo"} border={"solid 2px black"}>
        Logo
      </GridItem>
      <GridItem gridArea={"space"} border={"solid 2px black"}>
        <SinceLastAnalysis since="16/08/2023" />
      </GridItem>
      <GridItem gridArea={"logo2"} border={"solid 2px black"}>
        Logo
      </GridItem>
    </Grid>
  );
}

export default Dashboard4;
