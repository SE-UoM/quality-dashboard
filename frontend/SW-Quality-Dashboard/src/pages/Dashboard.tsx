import Navbar from "../components/Navbar";
import { Button, Flex, IconButton } from "@chakra-ui/react";
import { register } from "swiper/element";
import Dashboard1 from "../features/MainDashboard/Screen_1";
import Dashboard2 from "../features/MainDashboard/Screen_2";
import Dashboard3 from "../features/MainDashboard/Screen_3";
import Dashboard4 from "../features/MainDashboard/Screen_4";
import { useState } from "react";
import DevelopersIcon from "../assets/icons/components/DevelopersIcon";
import FilesIcon from "../assets/icons/components/FilesIcon";
import FirstMedal from "../assets/icons/components/FirstMedal";
import GithubIcon from "../assets/icons/components/GithubIcon";
import GithubStars from "../assets/icons/components/GithubStars";
import LanguageIcon from "../assets/icons/components/LanguageIcon";
import LinesOfCode from "../assets/icons/components/LinesOfCodeIcon";
import ProjectsIcon from "../assets/icons/components/ProjectsIcon";
import SecondMedal from "../assets/icons/components/SecondMedal";
import SimpleMedal from "../assets/icons/components/SimpleMedal";
import ThirdMedal from "../assets/icons/components/ThirdMedal";
import TimerIcon from "../assets/icons/components/TimerIcon";

import { ArrowRightIcon } from "@chakra-ui/icons";
import ContributionsIcon from "../assets/icons/components/ContributionsIcon";
import DateIcon from "../assets/icons/components/DateIcon";
// import { Swiper, SwiperSlide } from "swiper/react";
// import "react-multi-carousel/lib/styles.css";
// import "swiper/css";
function RotatingLine() {
  return <Flex>Rotating Line</Flex>;
}

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
      <Flex direction="column" height={"100vh"} bgColor="gray.200">
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
        {/* <ContributionsIcon height={65} width={65} />
        <DateIcon height={65} width={65} />
        <DevelopersIcon height={65} width={65} />
        <FilesIcon height={65} width={65} />
        <FirstMedal height={65} width={65} />
        <GithubIcon height={65} width={65} />
        <GithubStars height={65} width={65} />
        <LanguageIcon height={65} width={65} />
        <LinesOfCode height={65} width={65} />
        <ProjectsIcon height={65} width={65} />
        <SecondMedal height={65} width={65} />
        <SimpleMedal height={65} width={65} />
        <ThirdMedal height={65} width={65} />
        <TimerIcon height={65} width={65} /> */}
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
