import { Grid, GridItem } from "@chakra-ui/react"
import ActivityStats from "./ActivityStats"
import BestPractice from "./BestPractice"
import MinDebtLanguage from "./MinDebtLanguage"
import TDStats from "./TDStats"
import TotalCodeSmells from "./TotalCodeSmells"
import TotalTD from "./TotalTD"


const templateAreas= `
"smells smells smells practice practice practice practice practice"
"smells smells smells practice practice practice practice practice"
"smells smells smells practice practice practice practice practice"
"smells smells smells practice practice practice practice practice"
"totaltd totaltd totaltd tdstats tdstats activ activ mindebtlang"
"totaltd totaltd totaltd tdstats tdstats activ activ mindebtlang"
"logo logo logo logo space space logo2 logo2"
`




function Dashboard2(){

    return <Grid height={"92vh"}
    padding={"1rem"}
    templateAreas={templateAreas} 
    templateColumns={"repeat(8,1fr)"} 
    templateRows={"repeat(7,1fr)"}
    gap="1rem">
        <GridItem gridArea={"smells"}>

        <TotalCodeSmells />
        </GridItem>
        <GridItem gridArea={"practice"}>

        <BestPractice />
        </GridItem>
        <GridItem gridArea="totaltd">

        <TotalTD />
        </GridItem>
        <GridItem gridArea="tdstats">

        <TDStats />
        </GridItem>
        <GridItem gridArea={"activ"}>

        <ActivityStats />
        </GridItem>
        <GridItem gridArea={"mindebtlang"}>
        <MinDebtLanguage />
        </GridItem>
        <GridItem gridArea={"logo"} border={"solid 2px black"}>
            Logo
        </GridItem>
        <GridItem gridArea={"space"} border={"solid 2px black"}>
            Extra
        </GridItem>
        <GridItem gridArea={"logo2"} border={"solid 2px black"}>
            Logo2
        </GridItem>

    </Grid>


}

export default Dashboard2;