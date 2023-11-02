import FactorsBanners from "./Panels/FactorsBanners";
import LabBanner from "./Panels/LabBanner";
import QuickInfoPanel from "./Panels/QuickInfoPanel";
import TopLanguages from "./Panels/TopLanguages";
import WordCloud from "./Panels/WordCloud";
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
      <TopLanguages />
      <WordCloud />
      <QuickInfoPanel
        value="16"
        text="Projects"
        icon={
          <svg
            width="100%"
            viewBox="0 0 22 18"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M2.75 2.75C2.75 1.64543 3.64543 0.75 4.75 0.75H17.25C18.3546 0.75 19.25 1.64543 19.25 2.75V13.75H2.75V2.75Z"
              stroke="currentColor"
              strokeWidth="1.5"
              strokeLinecap="square"
              strokeLinejoin="round"
            />
            <path
              d="M0.75 13.75H21.25V15.25C21.25 16.3546 20.3546 17.25 19.25 17.25H2.75C1.64543 17.25 0.75 16.3546 0.75 15.25V13.75Z"
              stroke="currentColor"
              strokeWidth="1.5"
              strokeLinecap="square"
              strokeLinejoin="round"
            />
          </svg>
        }
      />
      <QuickInfoPanel
        value="7"
        text="Languages"
        icon={
          <svg
            width="100%"
            viewBox="0 0 22 18"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M9 17L13 1M17 5.00004L21 9.00004L17 13M5 13L1 9.00004L5 5.00004"
              stroke="currentColor"
              strokeWidth="1.5"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        }
      />
      <QuickInfoPanel
        value="12836"
        text="Lines of Code"
        icon={
          <svg
            width="100%"
            viewBox="0 0 16 20"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M5 5H11M5 9H11M5 13H7M1 1H15V19H1V1Z"
              stroke="currentColor"
              strokeWidth="1"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        }
      />
      <QuickInfoPanel
        value="198"
        text="Contributions"
        icon={
          <svg
            width="100%"
            viewBox="0 0 18 20"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M3.5 6C4.88071 6 6 4.88071 6 3.5C6 2.11929 4.88071 1 3.5 1C2.11929 1 1 2.11929 1 3.5C1 4.88071 2.11929 6 3.5 6ZM3.5 6V14M3.5 14C2.11929 14 1 15.1193 1 16.5C1 17.8807 2.11929 19 3.5 19C4.88071 19 6 17.8807 6 16.5C6 15.1193 4.88071 14 3.5 14ZM14.5 14C13.1193 14 12 15.1193 12 16.5C12 17.8807 13.1193 19 14.5 19C15.8807 19 17 17.8807 17 16.5C17 15.1193 15.8807 14 14.5 14ZM14.5 14V3.5H9M9 3.5L11.5 1M9 3.5L11.5 6"
              stroke="currentColor"
              stroke-width="1.5"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        }
      />
      <QuickInfoPanel
        value="291"
        text="Files"
        icon={
          <svg
            width="100%"
            viewBox="0 0 18 20"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M5 19H1V1H15V8.5M5 5H11M5 9H7M11.75 19C9.95507 19 8.5 17.5449 8.5 15.75C8.5 13.9551 9.95507 12.5 11.75 12.5C12.9015 12.5 13.9116 13.0983 14.4888 14L14.5 14C15.8807 14 17 15.1193 17 16.5C17 17.8807 15.8807 19 14.5 19H11.75Z"
              stroke="currentColor"
              strokeWidth="1.5"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        }
      />
      <QuickInfoPanel
        value="14"
        text="Developers"
        icon={
          <svg
            width="100%"
            viewBox="0 0 24 19"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M15 1C16.933 1 18.5 2.567 18.5 4.5C18.5 6.433 16.933 8 15 8M20.5 18H23C23 15.0623 20.9318 12.5472 18 11.5088M9 8C7.067 8 5.5 6.433 5.5 4.5C5.5 2.567 7.067 1 9 1C10.933 1 12.5 2.567 12.5 4.5C12.5 6.433 10.933 8 9 8ZM1 18C1 14.134 4.58172 11 9 11C13.4183 11 17 14.134 17 18H1Z"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        }
      />
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
