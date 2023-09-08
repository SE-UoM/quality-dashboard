import DevelopersIcon from "../../../assets/icons/components/DevelopersIcon";
import SingleDataTile from "../../../components/SingleDataTile";
interface TotalDevsProps {
  devs: number;
}
function ShowTotalDevs({ devs }: TotalDevsProps) {
  return (
    <SingleDataTile
      Icon={<DevelopersIcon height={60} width={60} />}
      label="Developers"
      num={devs}
    />
  );
}

export default ShowTotalDevs;
