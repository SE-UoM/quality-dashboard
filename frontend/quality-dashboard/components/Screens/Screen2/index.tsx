import CodeSmells from "./Panels/CodeSmells";
import FactorsBanners from "./Panels/FactorsBanners";
import LabBanner from "./Panels/LabBanner";
import QuickInfoPanel from "./Panels/QuickInfoPanel";
import { motion } from "framer-motion";

export default function Screen1() {
  return (
    <motion.div
      initial={{
        opacity: 0,
      }}
      animate={{
        opacity: 1,
      }}
      exit={{
        opacity: 0,
      }}
      transition={{
        duration: 0.2,
      }}
      className="w-full h-full grid grid-cols-6 grid-rows-4 gap-4 "
    >
      <CodeSmells />
      <LabBanner />
      <QuickInfoPanel
        value="10/05/2023"
        text="Since last analysis"
        icon={
          <svg
            width="100%"
            viewBox="0 0 18 18"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M1 5H17M2 17H16C16.5523 17 17 16.5523 17 16V2C17 1.44772 16.5523 1 16 1H2C1.44772 1 1 1.44772 1 2V16C1 16.5523 1.44772 17 2 17Z"
              stroke="currentColor"
              strokeWidth="1.5"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
            <path
              d="M6.25 9C6.25 9.69036 5.69036 10.25 5 10.25C4.30964 10.25 3.75 9.69036 3.75 9C3.75 8.30964 4.30964 7.75 5 7.75C5.69036 7.75 6.25 8.30964 6.25 9Z"
              fill="currentColor"
            />
            <path
              d="M6.25 13C6.25 13.6904 5.69036 14.25 5 14.25C4.30964 14.25 3.75 13.6904 3.75 13C3.75 12.3096 4.30964 11.75 5 11.75C5.69036 11.75 6.25 12.3096 6.25 13Z"
              fill="currentColor"
            />
            <path
              d="M10.25 9C10.25 9.69036 9.69036 10.25 9 10.25C8.30964 10.25 7.75 9.69036 7.75 9C7.75 8.30964 8.30964 7.75 9 7.75C9.69036 7.75 10.25 8.30964 10.25 9Z"
              fill="currentColor"
            />
            <path
              d="M10.25 13C10.25 13.6904 9.69036 14.25 9 14.25C8.30964 14.25 7.75 13.6904 7.75 13C7.75 12.3096 8.30964 11.75 9 11.75C9.69036 11.75 10.25 12.3096 10.25 13Z"
              fill="currentColor"
            />
            <path
              d="M14.25 9C14.25 9.69036 13.6904 10.25 13 10.25C12.3096 10.25 11.75 9.69036 11.75 9C11.75 8.30964 12.3096 7.75 13 7.75C13.6904 7.75 14.25 8.30964 14.25 9Z"
              fill="currentColor"
            />
          </svg>
        }
      />
      <FactorsBanners />
    </motion.div>
  );
}
