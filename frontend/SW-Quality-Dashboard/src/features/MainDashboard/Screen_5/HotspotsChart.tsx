import { Box } from "@chakra-ui/react";

interface PropsType {
  data: Map<string, number>;
}

function HotspotsChart({ data }: PropsType) {
  console.log("Data: ", data);

  return (
    <Box w="100%" h="full" border="solid 2px black">
      Hotspots Chart
    </Box>
  );
}

export default HotspotsChart;
