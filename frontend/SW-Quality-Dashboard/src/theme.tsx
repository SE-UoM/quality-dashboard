import { extendTheme } from "@chakra-ui/react";

const theme = extendTheme({
  colors: {
    footer: "#3737ff",
    tiles: "white",
    bgprimary: "#dedede",
    lang1: "#f0ba3d",
    lang2: "#8aa4bd",
    lang3: "#a35247",
    txt: "#404852",
    level: {
      minor: "#67b279",
      major: "#fdd835",
      critical: "#ff7f50",
      blocker: "#ff5252",
      info: "#58bbfb",
    },
  },
  fonts: {
    body: `'Roboto', sans-serif`,
  },
});

export { theme };
