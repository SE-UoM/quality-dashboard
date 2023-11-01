import { useRouter } from "next/router";
import Screen1 from "./Screen1";
import Screen2 from "./Screen2";
import Screen3 from "./Screen3";
import Screen4 from "./Screen4";
import Screen5 from "./Screen5";

export default function ScreenWrapper() {
  const router = useRouter();
  const { screen } = router.query;
  const screenString = screen as string;

  const sanitazedScreenVal = screenString ? parseInt(screenString) : 1;

  return (
    <div className="screen-wrapper px-8 h-full">
      {sanitazedScreenVal === 1 && <Screen1 />}
      {sanitazedScreenVal === 2 && <Screen2 />}
      {sanitazedScreenVal === 3 && <Screen3 />}
      {sanitazedScreenVal === 4 && <Screen4 />}
      {sanitazedScreenVal === 5 && <Screen5 />}
    </div>
  );
}
