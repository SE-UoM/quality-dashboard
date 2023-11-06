import { Box } from "@chakra-ui/react";

interface PropsType {
  data: Map<string, number>;
}

function CommitTypesChart({ data }: PropsType) {
  console.log("Data: ", data);
  return (
    <Box w="100%" h="full" border="solid 2px black">
      Commit Types Chart
    </Box>
  );
}

export default CommitTypesChart;
