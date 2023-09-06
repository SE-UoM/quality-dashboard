import ProjectsIcon from "../../../assets/icons/components/ProjectsIcon";
import SingleDataTile from "../../../components/SingleDataTile";
interface ShowTotalProjectProps {
  projects: number;
}
function ShowTotalProjects({ projects }: ShowTotalProjectProps) {
  return (
    <SingleDataTile
      Icon={<ProjectsIcon height={60} width={60} />}
      label="Projects"
      num={projects}
    />
  );
}

export default ShowTotalProjects;
