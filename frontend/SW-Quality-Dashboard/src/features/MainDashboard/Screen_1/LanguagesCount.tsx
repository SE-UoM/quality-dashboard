import { Button, Flex, chakra } from "@chakra-ui/react";
import React from "react";
import AnimatedCount from "../../../components/AnimatedCount";
import SingleDataTile from "../../../components/SingleDataTile";
import LanguageIcon from "../../../assets/icons/components/LanguageIcon";
interface LanguagesCountProps {
  langs: number;
}
function LanguagesCount({ langs }: LanguagesCountProps) {
  return (
    <SingleDataTile
      Icon={<LanguageIcon height={60} width={60} />}
      label="Languages"
      num={langs}
    />
  );
}

export default LanguagesCount;
