import TopLanguages from "./Panels/TopLanguages";
import WordCloud from "./Panels/WordCloud";

export default function Screen1() {
  return (
    <div className="w-full h-full grid grid-cols-6 grid-rows-3 gap-4">
      <TopLanguages />
      <WordCloud />
    </div>
  );
}
