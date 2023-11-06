import { Box } from "@chakra-ui/react";

interface PropsType {
  data: {
    coverage: number;
  };
}

function CoverageTile({ data }: PropsType) {
  console.log("Data: ", data);

  return (
    <Box w="100%" h="full" border="solid 2px black">
      Coverage Tile
    </Box>
  );
}

export default CoverageTile;
