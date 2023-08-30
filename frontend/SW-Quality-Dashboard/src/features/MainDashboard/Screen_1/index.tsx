import { Grid, GridItem } from "@chakra-ui/react";
import LanguagesCount from "./LanguagesCount";
import ShowTopLanguages from "./ShowTopLanguages";
import ShowTotalCommits from "./ShowTotalCommits";
import ShowTotalDevs from "./ShowTotalDevs";
import ShowTotalFiles from "./ShowTotalFiles";
import ShowTotalLoC from "./ShowTotalBytes";
import ShowTotalProjects from "./ShowTotalProjects";
import WordCloud from "./WordCloud";
import OrganisationLogo from "./OrganisationLogo";
import SinceLastAnalysis from "../../../components/SinceLastAnalysis";

const templateAreas = `
"toplangs toplangs wordcloud wordcloud wordcloud proj"
"toplangs toplangs wordcloud wordcloud wordcloud langs"
"toplangs toplangs loc commits files devs"
"logo logo logo sincelast logo2 logo2"
`


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



function Dashboard1(){

    return <Grid height={"92vh"}
    padding={"1rem"}
    templateAreas={templateAreas} 
    templateColumns={"repeat(6,1fr)"} 
    templateRows={"repeat(4,1fr)"}
    gap="1rem">
        
        <GridItem gridArea={"toplangs"}>


        <ShowTopLanguages />
        </GridItem>
        <GridItem gridArea={"wordcloud"} border={"solid 2px black"}>

        <WordCloud />
        </GridItem>
        <GridItem gridArea={"proj"}>

        <ShowTotalProjects/>
        </GridItem>
        <GridItem gridArea={"langs"}>

        <LanguagesCount />
        </GridItem>
        <GridItem gridArea={"devs"}>

        <ShowTotalDevs/>
        </GridItem>
        <GridItem gridArea={"files"}>

        <ShowTotalFiles />
        </GridItem>
        <GridItem gridArea={"commits"}>

        <ShowTotalCommits/>
        </GridItem>
        <GridItem gridArea={"loc"}>

        <ShowTotalLoC/>
        </GridItem>

        <GridItem gridArea={"logo"} border={"solid 2px black"}>
            <OrganisationLogo />
        </GridItem>
        <GridItem gridArea={"sincelast"} border={"solid 2px black"}>
            <SinceLastAnalysis since="16/08/2023" />
        </GridItem>
        <GridItem gridArea={"logo2"} border={"solid 2px black"}>
        <OrganisationLogo />

        </GridItem>
        </Grid>
}

export default Dashboard1;

