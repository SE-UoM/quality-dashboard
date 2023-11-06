import { Box } from "@chakra-ui/react";

interface PropsType {
  data: { dependencies: number };
}

function DependenciesTile({ data }: PropsType) {
  console.log("Data: ", data);

  return (
    <Box w="100%" h="full" border="solid 2px black">
      Dependencies
    </Box>
  );
}

export default DependenciesTile;
