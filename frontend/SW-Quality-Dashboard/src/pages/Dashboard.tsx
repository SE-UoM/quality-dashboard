import Navbar from '../components/Navbar'
import { Flex } from '@chakra-ui/react'
import Dashboard1 from '../features/MainDashboard/Screen_1'
import Dashboard2 from '../features/MainDashboard/Screen_2'
import Dashboard3 from '../features/MainDashboard/Screen_3'


function RotatingLine(){
    return <Flex>Rotating Line</Flex>
}

function HomePage() {
    return (
        <>
            <Flex direction="column" height={"100vh"}>
                {/* wrap this in a swiper instance that doesnot autoscroll */}
                <Dashboard1 />
                <RotatingLine />
            </Flex>
            <Navbar />
        </>
    )
}

function LogoBar() {
    return (<>Logo Bar</>)
}

export default HomePage