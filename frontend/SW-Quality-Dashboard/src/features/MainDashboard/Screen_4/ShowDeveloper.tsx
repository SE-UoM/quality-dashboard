import { Avatar, Flex, chakra, useInterval } from "@chakra-ui/react";
import { useEffect, useState } from "react";
import { motion, useAnimation } from "framer-motion";
import { useGetDevelopersInfoQuery } from "../../api/screen4Api";
import getNextIndex from "../../../utils/getNextIndex";

const interval = 3 * 1000;
const fadeOutDuration = 5 * 1000;
const fadeInDuration = 200;

const developersArr = [];

function ShowDeveloper() {
  const [isVisible, setIsVisible] = useState(true);
  const { data } = useGetDevelopersInfoQuery("10");
  const [currentDevIndex, setCurrentDevIndex] = useState(0);
  useInterval(() => {
    setCurrentDevIndex((prev) => getNextIndex(data, prev));
  }, 5 * 1000);

  console.log("Developers: ", data);
  const { avatarUrl, name } = data
    ? data[currentDevIndex]
    : {
        avatarUrl: "",
        name: "",
      };
  // const controls = useAnimation();

  // useEffect(() => {
  //   // Function to handle fading in and out
  //   const handleFade = async () => {
  //     await controls.start({ opacity: 1 });
  //     await new Promise((resolve) => setTimeout(resolve, fadeOutDuration));
  //     await controls.start({ opacity: 0 });
  //     await new Promise((resolve) => setTimeout(resolve, fadeInDuration));
  //     setIsVisible(!isVisible);
  //   };

  //   // Initially trigger the fade
  //   handleFade();

  //   // Set interval to trigger the fade every 'interval' milliseconds
  //   const fadeInterval = setInterval(() => {
  //     handleFade();
  //   }, interval);

  //   // Clean up the interval when the component unmounts
  //   return () => clearInterval(fadeInterval);
  // }, [isVisible, controls]);

  return (
    <Flex direction={"column"} px="1rem" h="100%">
      <chakra.span fontWeight={"semibold"} fontSize={"xl"}>
        Developers
      </chakra.span>
      <>
        <style>
          {`/* Define the text and its initial state */
       

        /* Define the animation keyframes */
        @keyframes fadeInOut {
            0% {
                opacity: 1;

            }
            50% {
              opacity:0;
            }
            100% {
                opacity: 1;
            }
        }`}
        </style>
        {isVisible && (
          <Flex
            direction={"column"}
            mt="1rem"
            alignItems={"center"}
            justifyContent={"center"}
            w="100%"
            px="1rem"
            mx="auto"
            h="100%"
            animation="fadeInOut 5s linear infinite"
          >
            <Avatar src={avatarUrl} size={"2xl"} />
            <chakra.span
              mt="auto"
              mb="2rem"
              alignSelf={"center"}
              fontWeight={"bold"}
              fontSize={"xl"}
            >
              {name}
            </chakra.span>
          </Flex>
        )}
      </>
    </Flex>
  );
}

export default ShowDeveloper;
