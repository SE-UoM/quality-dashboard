import { Flex, Grid, GridItem } from "@chakra-ui/react";
import { Children } from "react";


function LogoBar() {
    return (<div>Logo Bar</div>)
}

interface PositionInGrid {
    rowSpan: number;
    colSpan: number;
    rowStart: number;
    colStart: number;
}

interface DashboardGridProps {
    layoutConfig:PositionInGrid[],
    children:React.ReactNode[]
    //mporw na valw kai logo Bar Edw
}

function DashboardGrid({layoutConfig,children}:DashboardGridProps){

    const arrayChildren = Children.toArray(children);
    return (<><Flex direction="column" height="100vh">
    <Grid p={4} h="90vh" gap="4" templateColumns={"repeat(12, 1fr)"} templateRows={"repeat(12, 1fr)"} >
        {layoutConfig.map(({ rowSpan, rowStart, colSpan, colStart },index) => {
            const child = arrayChildren[index];
            console.log(child,"Row Span: ",rowSpan)
            return (
            <GridItem rowSpan={rowSpan} colSpan={colSpan} rowStart={rowStart} colStart={colStart}>
                 {child}
            </GridItem>
        )})}

    </Grid>
    <LogoBar />
</Flex>
</>)
}

export default DashboardGrid;