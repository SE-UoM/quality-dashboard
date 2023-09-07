import Navbar from "../components/Navbar";
import { Flex, IconButton } from "@chakra-ui/react";

import Dashboard1 from "../features/MainDashboard/Screen_1";
import Dashboard2 from "../features/MainDashboard/Screen_2";
import Dashboard3 from "../features/MainDashboard/Screen_3";
import Dashboard4 from "../features/MainDashboard/Screen_4";
import { useState } from "react";
import RotatingLine from "../components/RotatingLine";

import { ArrowRightIcon } from "@chakra-ui/icons";
// import { Swiper, SwiperSlide } from "swiper/react";
// import "react-multi-carousel/lib/styles.css";
// import "swiper/css";

const dashboards = [Dashboard1, Dashboard2, Dashboard3, Dashboard4];

const getNextIndex = (arr: any[], currentIndex: number) => {
  if (currentIndex === arr.length - 1) return 0;
  else return currentIndex + 1;
};

function HomePage() {
  const [index, setIndex] = useState(0);
  const DisplayedDashboard = dashboards[index];
  return (
    <>
      <Flex direction="column" height={"100vh"} bgColor="#dededeff">
        {/* wrap this in a swiper instance that doesnot autoscroll */}
        <DisplayedDashboard />
        <IconButton
          colorScheme="teal"
          aria-label="View Next Dashboard"
          position={"fixed"}
          bottom={"5rem"}
          right="2rem"
          icon={<ArrowRightIcon />}
          onClick={() => setIndex((prev) => getNextIndex(dashboards, prev))}
        />

        <RotatingLine />
      </Flex>
      <Navbar />
    </>
  );
}

function LogoBar() {
  return <>Logo Bar</>;
}

export default HomePage;
