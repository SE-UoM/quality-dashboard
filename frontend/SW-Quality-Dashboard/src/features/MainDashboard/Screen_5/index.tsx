import { Flex, Grid, GridItem } from "@chakra-ui/react";
import DashboardLogo from "../../../components/DashboardLogo";
import UoMLogo from "../../../components/UoMLogo";
import CommitTypesChart from "./CommitTypesChart";
import OpenSourceUomLogo from "../../../components/OpenSourceUomLogo";
import CoverageTile from "./CoverageTile";
import HotspotsChart from "./HotspotsChart";
import DependenciesTile from "./DependenciesTile";
import SinceLastAnalysis from "../../../components/SinceLastAnalysis";
import {
  useGetCodeCoverageQuery,
  useGetCommitTypesQuery,
  useGetDependenciesQuery,
  useGetHotspotsQuery,
} from "../../api/screen5Api";

const templateAreas = `
"ctypes ctypes hspot hspot hspot"
"ctypes ctypes hspot hspot hspot"
"cover cover depend depend depend"
"logo logo space logo2 logo2"
`;

const resultForCommitTypes = new Map();
resultForCommitTypes.set("excellent", 50);
resultForCommitTypes.set("good", 20);
resultForCommitTypes.set("fair", 15);
resultForCommitTypes.set("poor", 15);

const resultForHotspots = new Map();
resultForHotspots.set("low", 100);
resultForHotspots.set("high", 40);
resultForHotspots.set("medium", 60);
resultForHotspots.set("normal", 80);

const resultForCoverage = {
  coverage: 100,
};

const resultForDependencies = {
  dependencies: 100,
};

function Dashboard5() {
  const dependenciesData = useGetDependenciesQuery("10");
  const codeCoverageData = useGetCodeCoverageQuery("10");
  const hotspotsData = useGetHotspotsQuery("10");
  const commitTypesData = useGetCommitTypesQuery("10");
  return (
    <Grid
      templateAreas={templateAreas}
      m="1rem"
      templateColumns={"repeat(5,1fr)"}
      templateRows={"repeat(4,1fr)"}
      gap="1rem"
    >
      <GridItem gridArea={"ctypes"}>
        <CommitTypesChart data={commitTypesData.data || resultForCommitTypes} />
      </GridItem>
      <GridItem gridArea={"cover"}>
        <CoverageTile data={codeCoverageData.data || resultForCommitTypes} />
      </GridItem>
      <GridItem gridArea={"hspot"}>
        <HotspotsChart data={hotspotsData.data || resultForHotspots} />
      </GridItem>
      <GridItem gridArea={"depend"}>
        <DependenciesTile
          data={dependenciesData.data || resultForDependencies}
        />
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
      {/* <GridItem gridArea={"blank"}>Commit Types</GridItem> */}
    </Grid>
  );
}

export default Dashboard5;
