import { Grid, GridItem } from "@chakra-ui/react";
import ActivityStats from "./ActivityStats";
import BestPractice from "./BestPractice";
import MinDebtLanguage from "./MinDebtLanguage";
import TDStats from "./TDStats";
import TotalCodeSmells from "./TotalCodeSmells";
import TotalTD from "./TotalTD";

const templateAreas = `
"smells smells smells tdstats tdstats activ activ activ"
"smells smells smells tdstats tdstats activ activ activ" 
"smells smells smells practice practice practice practice practice"
"smells smells smells practice practice practice practice practice"
"totaltd totaltd totaltd practice practice practice practice practice"
"totaltd totaltd totaltd practice practice practice practice practice"
"logo logo logo space space logo2 logo2 logo2"
`;

function Dashboard2() {
  return (
    <Grid
      height={"92vh"}
      padding={"1rem"}
      templateAreas={templateAreas}
      templateColumns={"repeat(8,1fr)"}
      templateRows={"repeat(7,1fr)"}
      gap="1rem"
    >
      <GridItem gridArea={"smells"} border={"solid 2px black"}>
        <TotalCodeSmells />
      </GridItem>
      <GridItem gridArea={"practice"} width={"100%"} height={"100%"}>
        <BestPractice />
      </GridItem>
      <GridItem gridArea="totaltd" width={"100%"} height={"100%"}>
        <TotalTD />
      </GridItem>
      <GridItem gridArea="tdstats" width={"100%"} height={"100%"}>
        <TDStats />
      </GridItem>
      <GridItem gridArea={"activ"} width={"100%"} height={"100%"}>
        <ActivityStats />
      </GridItem>
      <GridItem
        gridArea={"logo"}
        border={"solid 2px black"}
        width={"100%"}
        height={"100%"}
      >
        Logo
      </GridItem>
      <GridItem
        gridArea={"space"}
        border={"solid 2px black"}
        width={"100%"}
        height={"100%"}
      >
        Extra
      </GridItem>
      <GridItem
        gridArea={"logo2"}
        border={"solid 2px black"}
        width={"100%"}
        height={"100%"}
      >
        Logo2
      </GridItem>
    </Grid>
  );
}

export default Dashboard2;
