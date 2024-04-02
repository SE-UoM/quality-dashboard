import { Flex, Grid, GridItem } from '@chakra-ui/react'
import ShowTotalCommits from '../../features/MainDashboard/ShowTotalCommits.tsx'
import ShowTotalFiles from '../../features/MainDashboard/ShowTotalFiles.tsx'
import ShowTotalProjects from '../../features/MainDashboard/ShowTotalProjects.tsx'
import ShowTotalDevs from '../../features/MainDashboard/ShowTotalDevs.tsx'
import ShowTotalLoC from '../../features/MainDashboard/ShowTotalLoC.tsx'
import ShowTopLanguages from '../../features/MainDashboard/ShowTopLanguages.tsx'
import ShowAllLanguages from '../../features/MainDashboard/ShowAllLanguages.tsx'
import './DashboardPage.css'

const layoutConfig = [
    {
        rowSpan: 1,
        colSpan: 1,
        component: ShowTotalCommits,
        rowStart: 1,
        colStart: 1,
        bg: "red"
    },
    {
        rowSpan: 1,
        colSpan: 1,
        component: ShowTotalFiles,
        rowStart: 2,
        colStart: 1,
        bg: "blue"
    }
    , {
        rowSpan: 2,
        colSpan: 3,
        component: ShowAllLanguages,
        rowStart: 1,
        colStart: 2,
        bg: "green"

    },
    {
        rowSpan: 1,
        colSpan: 2,
        component: ShowTotalProjects,
        rowStart: 3,
        colStart: 1,
        bg: "yellow"
    },
    {
        rowSpan: 1,
        colSpan: 2,
        component: ShowTotalLoC,
        rowStart: 3,
        colStart: 3,
        bg: "purple"

    }, {
        rowSpan: 1,
        colSpan: 2,
        component: ShowTotalDevs,
        rowStart: 3,
        colStart: 5,
        bg: "pink"
    },
    {
        rowSpan: 2,
        colSpan: 2,
        component: ShowTopLanguages,
        rowStart: 1,
        colStart: 5,
        bg: "violet"

    }
]


function HomePage() {
    return (
        <>
            <Flex direction="column" height="100vh">
                <Grid p={4} h="85vh" gap="4" templateColumns={"repeat(6, 1fr)"} templateRows={"repeat(3, 1fr)"} >
                    {layoutConfig.map(({ component: Component, rowSpan, rowStart, bg, colSpan, colStart }) => (
                        <GridItem key={bg} rowSpan={rowSpan} colSpan={colSpan} bg={bg} rowStart={rowStart} colStart={colStart}>
                            <Component />
                        </GridItem>
                    ))}

                </Grid>
                <LogoBar />
            </Flex>
        </>
    )
}

function LogoBar() {
    return (<>Logo Bar</>)
}

export default HomePage