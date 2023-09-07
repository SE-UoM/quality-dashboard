import { Image } from "@chakra-ui/react";
import imageUom from "../assets/pictures/University_of_Macedonia_logo.png";
// import imageSde from "../assets/pictures/sde-banner.png";
import imageOsUom from "../assets/pictures/osuom.png";
import imageSE from "./sde-banner.png";
const imageMap: Record<string, string> = {
  uom: imageUom,
  sde: "",
  osuom: imageOsUom,
};

interface ImageNameProps {
  imageName: string;
  width: string;
  height: string;
}

function DashboardLogo({ width, height }: ImageNameProps) {
  const imageUrl =
    "https://sde.uom.gr/wp-content/uploads/2016/10/sde-banner.png";

  return (
    <Image
      p="0.25rem"
      src={imageUrl}
      alt={"SE-UoM Logo"}
      width={width}
      height={height}
    />
  );
}

export default DashboardLogo;
