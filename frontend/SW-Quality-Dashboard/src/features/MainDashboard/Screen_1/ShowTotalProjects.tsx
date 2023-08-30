import { Button, Flex, chakra } from '@chakra-ui/react'
import { useState } from 'react'
import AnimatedCount from '../../../components/AnimatedCount'
import SingleDataTile from '../../../components/SingleDataTile'
import {Icon} from "@chakra-ui/react"
function ShowTotalProjects() {
    
    return (
        <SingleDataTile label='Projects' num={50} />
    )
}


export default ShowTotalProjects