import { Grid, GridItem } from "@chakra-ui/react";
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
  // const { data: generalStats } = useGetGeneralStatsQuery("10");
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
  console.log("General Stats: ", generalStats);

  let devs = getValueOf(generalStats?.totalDevs);
  let commits = getValueOf(generalStats?.totalCommits);
  let files = getValueOf(generalStats?.totalFiles);
  let LoC = getValueOf(generalStats?.totalLinesOfCode);
  let langs = getValueOf(generalStats?.totalLanguages);
  let projects = getValueOf(generalStats?.totalProjects);
  return (
    <Grid
      height={"92vh"}
      padding={"1rem"}
      templateAreas={templateAreas}
      templateColumns={"repeat(6,1fr)"}
      templateRows={"repeat(4,1fr)"}
      gap="1rem"
      borderRadius={"1rem"}
    >
      <GridItem gridArea={"toplangs"} bg="white">
        <ShowTopLanguages />
      </GridItem>
      <GridItem gridArea={"wordcloud"} border={"solid 2px black"} bg="white">
        <WordCloud />
      </GridItem>
      <GridItem gridArea={"proj"} bg="white">
        <ShowTotalProjects projects={projects} />
      </GridItem>
      <GridItem gridArea={"langs"} bg="white">
        <LanguagesCount langs={langs} />
      </GridItem>
      <GridItem gridArea={"devs"} bg="white">
        <ShowTotalDevs devs={devs} />
      </GridItem>
      <GridItem gridArea={"files"} bg="white">
        <ShowTotalFiles files={files} />
      </GridItem>
      <GridItem gridArea={"commits"} bg="white">
        <ShowTotalCommits commits={commits} />
      </GridItem>
      <GridItem gridArea={"loc"} bg="white">
        <ShowTotalBytes bytes={LoC} />
      </GridItem>

      <GridItem gridArea={"logo"} border={"solid 2px black"} bg="white">
        <OrganisationLogo />
      </GridItem>
      <GridItem gridArea={"sincelast"} border={"solid 2px black"} bg="white">
        <SinceLastAnalysis since="16/08/2023" />
      </GridItem>
      <GridItem gridArea={"logo2"} border={"solid 2px black"} bg="white">
        <OrganisationLogo />
      </GridItem>
    </Grid>
  );
}

export default Dashboard1;
