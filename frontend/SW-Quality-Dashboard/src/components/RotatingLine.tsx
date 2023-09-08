import React, { useState, useEffect } from "react";
import { Flex, Text, useInterval, chakra } from "@chakra-ui/react";
import { useGetProjectNamesQuery } from "../features/api/screen1Api";

function RotatingLine() {
  const { data } = useGetProjectNamesQuery("10");
  console.log("Project Names: ", data);
  const projectArray = data
    ? data.map((project) => project.name)
    : ["PyAssess", "CodeInspector", "BPMN Plugin", "myUoM"];

  return (
    <Flex bgColor="black" width="100%" height="8%">
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
      >
        {projectArray
          .filter((name) => Boolean(name))
          .map((project, index) => (
            <span key={project} style={{}}>
              {index !== 0 && "  |  "}
              {index === 0 && "Organization Projects: "}
              <strong>{project}</strong>
            </span>
          ))}
      </Text>
    </Flex>
  );
}

export default RotatingLine;
