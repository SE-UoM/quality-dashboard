import BestDevelopers from "./BestDevelopers";
import BestProjects from "./BestProjects";
import SubmittedProjects from "./SubmittedProjects";
import ShowAllLanguages from "./ShowAllLanguages";
import { Flex, Grid, GridItem } from "@chakra-ui/react";
import SinceLastAnalysis from "../../../components/SinceLastAnalysis";
import DashboardLogo from "../../../components/DashboardLogo";
import UoMLogo from "../../../components/UoMLogo";
import OpenSourceUomLogo from "../../../components/OpenSourceUomLogo";

const templateAreas = `
"projs projs toplangs toplangs toplangs toplangs bestdevs bestdevs"
"projs projs toplangs toplangs toplangs toplangs bestdevs bestdevs"
"projs projs toplangs toplangs toplangs toplangs bestdevs bestdevs"
"projs projs toplangs toplangs toplangs toplangs bestdevs bestdevs"
"projs projs toplangs toplangs toplangs toplangs bestdevs bestdevs"
"projs projs submitted submitted submitted submitted bestdevs bestdevs"
"projs projs submitted submitted submitted submitted bestdevs bestdevs"
"projs projs submitted submitted submitted submitted bestdevs bestdevs"
"logo logo logo space space logo2 logo2 logo2"
"logo logo logo space space logo2 logo2 logo2"
`;

interface DashboardOfBestProps {}

function Dashboard3({}: DashboardOfBestProps) {
  return (
    <Grid
      height={"92vh"}
      padding={"1rem"}
      templateAreas={templateAreas}
      templateColumns={"repeat(8,1fr)"}
      templateRows={"repeat(10,1fr)"}
      gap="1rem"
    >
      <GridItem
        gridArea={"projs"}
        borderRadius={"0.5rem"}
        overflow={"auto"}
        bg="white"
      >
        <BestProjects />
      </GridItem>
      <GridItem gridArea={"submitted"} borderRadius={"0.5rem"} bg="white">
        <SubmittedProjects />
      </GridItem>
      <GridItem
        gridArea={"bestdevs"}
        borderRadius={"0.5rem"}
        overflow={"auto"}
        bg="white"
      >
        <BestDevelopers />
      </GridItem>
      <GridItem gridArea={"toplangs"} borderRadius={"0.5rem"} bg="white">
        <ShowAllLanguages />
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

export default Dashboard3;
