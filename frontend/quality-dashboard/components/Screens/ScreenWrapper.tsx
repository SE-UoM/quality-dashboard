import { useRouter } from "next/router";
import Screen1 from "./Screen1";
import Screen2 from "./Screen2";
import Screen3 from "./Screen3";
import Screen4 from "./Screen4";
import Screen5 from "./Screen5";
import { AnimatePresence } from "framer-motion";

export default function ScreenWrapper() {
  const router = useRouter();
  const { screen } = router.query;
  const screenString = screen as string;

  const sanitazedScreenVal = screenString
    ? parseInt(screenString) >= 1 && parseInt(screenString) <= 5
      ? parseInt(screenString)
      : 1
    : 1;

  return (
    <div className="screen-wrapper px-8 h-full">
      <AnimatePresence mode="wait">
        {sanitazedScreenVal === 1 && <Screen1 key="screen1" />}
        {sanitazedScreenVal === 2 && <Screen2 key="screen2" />}
        {sanitazedScreenVal === 3 && <Screen3 key="screen3" />}
        {sanitazedScreenVal === 4 && <Screen4 key="screen4" />}
        {sanitazedScreenVal === 5 && <Screen5 key="screen5" />}
      </AnimatePresence>
    </div>
  );
}
