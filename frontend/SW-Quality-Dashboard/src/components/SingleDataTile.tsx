
import { Flex,chakra } from '@chakra-ui/react'
import React from 'react'
import AnimatedCount from './AnimatedCount'

interface SingleDataTileProps{
    Icon?:React.FC,
    num:number,
    label:string,
}

function SingleDataTile({Icon,label,num}:SingleDataTileProps) {
  return (
    <Flex direction={"row"} height="100%" columnGap={"1rem"} alignItems={"center"} justifyContent={"space-around"} border={"solid 2px black"}>
        <chakra.span>Ey</chakra.span>
        <Flex direction={"column"} rowGap={"0.25rem"}>

            <AnimatedCount count={num} />
            <chakra.span>{label}</chakra.span>
        </Flex>
        
    </Flex>
  )
}

export default SingleDataTile
