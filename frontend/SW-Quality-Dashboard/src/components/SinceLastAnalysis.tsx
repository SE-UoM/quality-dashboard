import { Flex,chakra } from '@chakra-ui/react'
import React from 'react'

interface SinceLastAnalysisProps{
    since:string
}

function SinceLastAnalysis({since}:SinceLastAnalysisProps) {
  return (
    <Flex direction={"row"} columnGap="0.5rem" alignItems={"center"} justifyContent={"space-around"} height={"100%"}>
        {/* Render Icon here */}
        <chakra.span>Icon</chakra.span>
        <Flex direction={"column"}>

        <chakra.span fontSize={"xl"} fontWeight="semibold">{since}</chakra.span>
        <chakra.span fontSize="md" fontWeight={"hairline"} color="gray">since last analysis</chakra.span>
        </Flex>
      
    </Flex>
  )
}

export default SinceLastAnalysis
