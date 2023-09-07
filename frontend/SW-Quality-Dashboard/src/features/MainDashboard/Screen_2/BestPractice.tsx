import { Box, Divider, Flex, chakra } from "@chakra-ui/react";
import { useGetRandomBestPracticeQuery } from "../../api/screen2Api";
import { useEffect, useState } from "react";

const durationOfBestPracticeInSeconds = 15;

function useInterval(interval: number, fn: () => any) {
  useEffect(() => {
    const intervalId = setInterval(fn, interval);
    return () => clearInterval(intervalId);
  }, [fn]);
}

const getNextIndex = (arr: any[], currentIndex: number) => {
  if (currentIndex === arr.length - 1) return 0;
  else return currentIndex + 1;
};

function BestPractice() {
  const { data } = useGetRandomBestPracticeQuery("10");
  const [currentBestPractice, setCurrentBestPractice] = useState(0);
  useInterval(durationOfBestPracticeInSeconds * 1000, () =>
    setCurrentBestPractice((prev) => getNextIndex(data, currentBestPractice))
  );
  const { title, explanation } = data
    ? data[currentBestPractice]
    : { title: "No more Best Practices", explanation: "You have seen enough" };

  //title,explanation
  console.log("BEST PRACTICE", data);
  return (
    <Flex direction={"column"} pt="1.5rem" height="100%" width="100%">
      <Flex width={"100%"} direction={"column"}>
        <chakra.span
          pb="1rem"
          fontSize={"4xl"}
          fontWeight={"semibold"}
          textAlign={"center"}
        >
          Best Practices
        </chakra.span>
        <chakra.hr
          width={"100%!important"}
          bg={"black"}
          height="2px"
        ></chakra.hr>
        <Flex direction={"column"} height={"100%"}>
          <chakra.span
            py="1rem"
            fontSize={"3xl"}
            fontWeight={"semibold"}
            textAlign={"center"}
            justifySelf={"center"}
          >
            {title}
          </chakra.span>
          <chakra.p
            my="auto"
            px="2rem"
            justifySelf={"center"}
            alignSelf="center"
            fontSize={"2xl"}
            fontWeight={"medium"}
            textAlign={"center"}
            width={"80%"}
          >
            {explanation}
          </chakra.p>
        </Flex>
      </Flex>
    </Flex>
  );
}

export default BestPractice;
