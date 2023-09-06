import FilesIcon from "../../../assets/icons/components/FilesIcon";
import SingleDataTile from "../../../components/SingleDataTile";
interface TotalFilesProps {
  files: number;
}
function ShowTotalFiles({ files }: TotalFilesProps) {
  return (
    <SingleDataTile
      Icon={<FilesIcon height={60} width={60} />}
      label="Files"
      num={files}
    />
  );
}

export default ShowTotalFiles;
