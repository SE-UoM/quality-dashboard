import { Box } from "@chakra-ui/react";
import SingleDataTile from "../../../components/SingleDataTile";
import LinesOfCode from "../../../assets/icons/components/LinesOfCodeIcon";

interface TotalBytesProps {
  bytes: number;
}
function ShowTotalBytes({ bytes }: TotalBytesProps) {
  return (
    <SingleDataTile
      Icon={<LinesOfCode height={60} width={60} />}
      label="Bytes of Code"
      num={bytes}
    />
  );
}

export default ShowTotalBytes;
