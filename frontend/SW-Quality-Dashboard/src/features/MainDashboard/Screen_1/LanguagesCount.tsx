import { Button, Flex,chakra } from '@chakra-ui/react'
import React from 'react'
import AnimatedCount from '../../../components/AnimatedCount'
import SingleDataTile from '../../../components/SingleDataTile'

function LanguagesCount() {
  return (
    <SingleDataTile label='Projects' num={50} />
  )
}

export default LanguagesCount
