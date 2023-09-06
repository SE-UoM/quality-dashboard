import imageUom from "../assets/pictures/University_of_Macedonia_logo.png";
import imageSde from "../assets/pictures/sde-banner.png";
import imageOsUom from "../assets/pictures/osuom.png";

const imageMap: Record<string, string> = {
  uom: imageUom,
  sde: imageSde,
  osuom: imageOsUom,
};

interface ImageNameProps {
  imageName: string;
  width: number;
  height: number;
}

function DashboardLogo({ imageName, width, height }: ImageNameProps) {
  const imageUrl = imageMap[imageName];

  return (
    <div>
      <img src={imageUrl} alt={imageName} width={width} height={height} />
    </div>
  );
}

export default DashboardLogo;
