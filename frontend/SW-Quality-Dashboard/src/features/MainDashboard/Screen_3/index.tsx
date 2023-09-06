import BestDevelopers from "./BestDevelopers";
import BestProjects from "./BestProjects";
import SubmittedProjects from "./SubmittedProjects";
import ShowAllLanguages from "./ShowAllLanguages";
import { Grid, GridItem } from "@chakra-ui/react";
import SinceLastAnalysis from "../../../components/SinceLastAnalysis";
import DashboardLogo from "../../../components/DashboardLogo";

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
      <GridItem gridArea={"projs"} border={"solid 2px black"} overflow={"auto"}>
        <BestProjects />
      </GridItem>
      <GridItem gridArea={"submitted"} border={"solid 2px black"}>
        <SubmittedProjects />
      </GridItem>
      <GridItem
        gridArea={"bestdevs"}
        border={"solid 2px black"}
        overflow={"auto"}
      >
        <BestDevelopers />
      </GridItem>
      <GridItem gridArea={"toplangs"} border={"solid 2px black"}>
        <ShowAllLanguages />
      </GridItem>
      <GridItem gridArea={"logo"} border={"solid 2px black"}>
        <DashboardLogo imageName="uom" width={100} height={100} />
      </GridItem>
      <GridItem gridArea={"space"} border={"solid 2px black"}>
        <SinceLastAnalysis since="16/08/2023" />
      </GridItem>
      <GridItem gridArea={"logo2"} border={"solid 2px black"}>
        <DashboardLogo imageName="uom" width={100} height={100} />
      </GridItem>
    </Grid>
  );
}

export default Dashboard3;
