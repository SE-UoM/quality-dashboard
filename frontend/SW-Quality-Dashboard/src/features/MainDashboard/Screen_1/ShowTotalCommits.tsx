import { Button, Flex, chakra } from '@chakra-ui/react'
import { useState } from 'react'
import AnimatedCount from '../../../components/AnimatedCount'
import SingleDataTile from '../../../components/SingleDataTile'
function ShowTotalCommits() {
    const [totalCommits, setTotalCommits] = useState(0)
    return (
        <SingleDataTile label='Projects' num={50} />
    )
}


export default ShowTotalCommits