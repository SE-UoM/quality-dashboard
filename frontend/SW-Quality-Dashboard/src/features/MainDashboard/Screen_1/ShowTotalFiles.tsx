import { Button, Flex, chakra } from '@chakra-ui/react'
import { useState } from 'react'
import AnimatedCount from '../../../components/AnimatedCount'
import SingleDataTile from '../../../components/SingleDataTile'
function ShowTotalFiles() {
    return (
        <SingleDataTile label='Projects' num={50} />
    )
}


export default ShowTotalFiles