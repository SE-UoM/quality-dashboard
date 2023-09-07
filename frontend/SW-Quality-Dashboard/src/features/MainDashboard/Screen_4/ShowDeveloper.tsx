import { Avatar, Flex, chakra } from "@chakra-ui/react";
import { useEffect, useState } from "react";
import { motion, useAnimation } from "framer-motion";
import { useGetDevelopersInfoQuery } from "../../api/screen4Api";

const interval = 3 * 1000;
const fadeOutDuration = 5 * 1000;
const fadeInDuration = 200;

const developersArr = [];

function ShowDeveloper() {
  const [isVisible, setIsVisible] = useState(false);
  const { data } = useGetDevelopersInfoQuery("10");
  console.log("Data: ", data);
  const avatarUrl = "https://avatars.githubusercontent.com/u/69716466?v=4";
  const controls = useAnimation();

  useEffect(() => {
    // Function to handle fading in and out
    const handleFade = async () => {
      await controls.start({ opacity: 1 });
      await new Promise((resolve) => setTimeout(resolve, fadeOutDuration));
      await controls.start({ opacity: 0 });
      await new Promise((resolve) => setTimeout(resolve, fadeInDuration));
      setIsVisible(!isVisible);
    };

    // Initially trigger the fade
    handleFade();

    // Set interval to trigger the fade every 'interval' milliseconds
    const fadeInterval = setInterval(() => {
      handleFade();
    }, interval);

    // Clean up the interval when the component unmounts
    return () => clearInterval(fadeInterval);
  }, [isVisible, controls]);

  return (
    <Flex direction={"column"} px="1rem" pt="0.5rem" h="100%">
      <chakra.span fontWeight={"semibold"} fontSize={"2xl"}>
        Developers
      </chakra.span>
      <motion.div
        animate={controls}
        transition={{
          duration: fadeInDuration / 1000, // Convert milliseconds to seconds
        }}
      >
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
          >
            <Avatar src={avatarUrl} size={"2xl"} />
            <chakra.span
              mt="auto"
              mb="2rem"
              alignSelf={"center"}
              fontWeight={"bold"}
              fontSize={"xl"}
            >
              George David Apostolidis
            </chakra.span>
          </Flex>
        )}
      </motion.div>
    </Flex>
  );
}

export default ShowDeveloper;
