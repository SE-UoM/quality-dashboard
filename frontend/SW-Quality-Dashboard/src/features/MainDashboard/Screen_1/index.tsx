import { Flex, Grid, GridItem } from "@chakra-ui/react";
import LanguagesCount from "./LanguagesCount";
import ShowTopLanguages from "./TopLanguages";
import ShowTotalCommits from "./TotalCommits";
import ShowTotalDevs from "./TotalDevs";
import ShowTotalFiles from "./TotalFiles";
import ShowTotalProjects from "./TotalProjects";
import WordCloud from "./WordCloud";
import OrganisationLogo from "./OrganisationLogo";
import SinceLastAnalysis from "../../../components/SinceLastAnalysis";
import {
  useGetTopLanguagesQuery,
  useGetDateSinceLastAnalysisQuery,
  useGetGeneralStatsQuery,
  useGetLanguageNamesQuery,
} from "../../api/screen1Api";
import ShowTotalBytes from "./TotalBytes";
import { GeneralStats } from "../../../assets/models";
import DashboardLogo from "../../../components/DashboardLogo";
import OpenSourceUomLogo from "../../../components/OpenSourceUomLogo";
import UoMLogo from "../../../components/UoMLogo";

const templateAreas = `
"toplangs toplangs wordcloud wordcloud wordcloud proj"
"toplangs toplangs wordcloud wordcloud wordcloud langs"
"toplangs toplangs loc commits files devs"
"logo logo logo sincelast logo2 logo2"
`;

interface OverviewDashboardProps {
  // topLanguages:
  // totalLanguages:
  // loc:
  // commits:
  // files:
  // devs:
  // projects:
  // lastanalysis:
}

const getValueOf = (thing: number | null | undefined) => {
  if (thing == 0) {
    return 0;
  }
  if (thing == null || thing == undefined) return -1;
  return thing;
};

function Dashboard1() {
  // const { data } = useGetTopLanguagesQuery("10");
  const { data: generalStatsFetched } = useGetGeneralStatsQuery(10);
  const generalStats: GeneralStats = {
    totalCommits: 5,
    totalDevs: 10,
    totalFiles: 1,
    totalLanguages: 4,
    totalLinesOfCode: 1230,
    id: 1235,
    languages: ["Java"],
    totalProjects: 45,
  };
  // console.log("The data is here: ", data);
  console.log("General Stats: ", generalStatsFetched);

  let devs = getValueOf(generalStatsFetched?.totalDevs);
  let commits = getValueOf(generalStatsFetched?.totalCommits);
  let files = getValueOf(generalStatsFetched?.totalFiles);
  let LoC = getValueOf(generalStatsFetched?.totalLinesOfCode);
  let langs = getValueOf(generalStatsFetched?.totalLanguages);
  let projects = getValueOf(generalStatsFetched?.totalProjects);
  return (
    <Grid
      height={"92vh"}
      padding={"1rem"}
      templateAreas={templateAreas}
      templateColumns={"repeat(6,1fr)"}
      templateRows={"1fr 1fr 1fr 9rem"}
      gap="1rem"
      borderRadius={"1rem"}
      color="txt"
    >
      <GridItem gridArea={"toplangs"} bg="white" borderRadius={"0.5rem"}>
        <ShowTopLanguages />
      </GridItem>
      <GridItem gridArea={"wordcloud"} bg="white" borderRadius={"0.5rem"}>
        <WordCloud />
      </GridItem>
      <GridItem gridArea={"proj"} borderRadius={"0.5rem"} bg="white">
        <ShowTotalProjects projects={projects} />
      </GridItem>
      <GridItem gridArea={"langs"} bg="white" borderRadius={"0.5rem"}>
        <LanguagesCount langs={langs} />
      </GridItem>
      <GridItem gridArea={"devs"} bg="white" borderRadius={"0.5rem"}>
        <ShowTotalDevs devs={devs} />
      </GridItem>
      <GridItem gridArea={"files"} bg="white" borderRadius={"0.5rem"}>
        <ShowTotalFiles files={files} />
      </GridItem>
      <GridItem gridArea={"commits"} bg="white" borderRadius={"0.5rem"}>
        <ShowTotalCommits commits={commits} />
      </GridItem>
      <GridItem gridArea={"loc"} bg="white" borderRadius={"0.5rem"}>
        <ShowTotalBytes bytes={LoC} />
      </GridItem>

      <GridItem gridArea={"logo"} bg="white" borderRadius={"0.5rem"}>
        <DashboardLogo imageName="uom" width={"100%"} height={"100%"} />
      </GridItem>
      <GridItem gridArea={"sincelast"} bg="white" borderRadius={"0.5rem"}>
        <SinceLastAnalysis since="16/08/2023" />
      </GridItem>
      <GridItem gridArea={"logo2"} bg="white" borderRadius={"0.5rem"}>
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

export default Dashboard1;
