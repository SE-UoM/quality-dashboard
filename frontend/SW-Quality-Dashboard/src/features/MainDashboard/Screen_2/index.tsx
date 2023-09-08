import { Flex, Grid, GridItem } from "@chakra-ui/react";
import ActivityStats from "./ActivityStats";
import BestPractice from "./BestPractice";
import MinDebtLanguage from "./MinDebtLanguage";
import TDStats from "./TDStats";
import TotalCodeSmells from "./TotalCodeSmells";
import TotalTD from "./TotalTD";
import DashboardLogo from "../../../components/DashboardLogo";
import OpenSourceUomLogo from "../../../components/OpenSourceUomLogo";
import UoMLogo from "../../../components/UoMLogo";
import SinceLastAnalysis from "../../../components/SinceLastAnalysis";

const templateAreas = `
"smells smells smells tdstats tdstats tdstats tdstats tdstats"
"smells smells smells tdstats tdstats tdstats tdstats tdstats" 
"smells smells smells tdstats tdstats tdstats tdstats tdstats" 
"smells smells smells tdstats tdstats tdstats tdstats tdstats" 
"smells smells smells practice practice practice practice practice"
"smells smells smells practice practice practice practice practice"
"totaltd totaltd totaltd practice practice practice practice practice"
"totaltd totaltd totaltd practice practice practice practice practice"
"logo logo logo space space logo2 logo2 logo2"
"logo logo logo space space logo2 logo2 logo2"
`;

function Dashboard2() {
  return (
    <Grid
      height={"92vh"}
      padding={"1rem"}
      templateAreas={templateAreas}
      templateColumns={"repeat(8,1fr)"}
      templateRows={"1fr 1fr 1fr 1fr 1fr 1fr 8rem"}
      borderRadius={"0.5rem"}
      gap="1rem"
      color="txt"
    >
      <GridItem gridArea={"smells"} bg="white" borderRadius={"0.5rem"}>
        <TotalCodeSmells />
      </GridItem>

      <GridItem
        gridArea={"practice"}
        width={"100%"}
        height={"100%"}
        bg="white"
        borderRadius={"0.5rem"}
      >
        <BestPractice />
      </GridItem>

      <GridItem
        gridArea="totaltd"
        width={"100%"}
        height={"100%"}
        bg="white"
        borderRadius={"0.5rem"}
      >
        <TotalTD />
      </GridItem>

      <GridItem
        gridArea="tdstats"
        width={"100%"}
        height={"100%"}
        bg="white"
        borderRadius={"0.5rem"}
      >
        <TDStats />
      </GridItem>
      {/* <GridItem gridArea={"activ"} width={"100%"} height={"100%"} bg="white">
        <ActivityStats />
      </GridItem> */}
      <GridItem
        gridArea={"logo"}
        borderRadius={"0.5rem"}
        width={"100%"}
        height={"100%"}
        bg="white"
      >
        <DashboardLogo imageName="uom" width={"100%"} height={"100%"} />
      </GridItem>
      <GridItem
        gridArea={"space"}
        borderRadius={"0.5rem"}
        width={"100%"}
        height={"100%"}
        bg="white"
      >
        <SinceLastAnalysis since="20" />
      </GridItem>
      <GridItem
        gridArea={"logo2"}
        borderRadius={"0.5rem"}
        width={"100%"}
        height={"100%"}
        bg="white"
      >
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

export default Dashboard2;
