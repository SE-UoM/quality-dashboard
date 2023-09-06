import ContributionsIcon from "../../../assets/icons/components/ContributionsIcon";
import SingleDataTile from "../../../components/SingleDataTile";
interface TotalCommitsProps {
  commits: number;
}
function ShowTotalCommits({ commits }: TotalCommitsProps) {
  return (
    <SingleDataTile
      Icon={<ContributionsIcon height={60} width={60} />}
      label="Contributions"
      num={commits}
    />
  );
}

export default ShowTotalCommits;
