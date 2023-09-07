import React, { useState, useEffect } from "react";
import { Flex, Text } from "@chakra-ui/react";

function RotatingLine() {
  const projectArray = [
    "PyAssess",
    "CodeInspector",
    "BPMN Plugin",
    "myUoM",
    "project4",
    "project4",
    "project4",
    "project4",
    "project4",
    "project4",
    "project4",
    "project4",
    "project4",
  ];
  const [currentProjectIndex, setCurrentProjectIndex] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentProjectIndex(
        (prevIndex) => (prevIndex + 1) % (projectArray.length + 1)
      );
    }, 3000); // Change project every 3 seconds

    return () => clearInterval(interval);
  }, [projectArray]);

  return (
    <Flex bgColor="black" width="100%" height="8%" overflow="hidden">
      <style>
        {`
          @keyframes slide {
            0% {
              transform: translateX(100%);
            }
            100% {
              transform: translateX(-100%);
            }
          }
        `}
      </style>
      <Text
        fontWeight="bold"
        fontSize="xl"
        pt="1.3rem"
        alignSelf="center"
        animation="slide 15s linear infinite"
        color="white"
        width="100%"
        height="100%"
        whiteSpace="nowrap"
        overflow="hidden"
      >
        {projectArray.map((project, index) => (
          <span key={project}>
            {index !== 0 && "  |  "} <strong>{project}</strong>
          </span>
        ))}
        {currentProjectIndex === 0 && <span>{"  |  "}</span>}
      </Text>
    </Flex>
  );
}

export default RotatingLine;
