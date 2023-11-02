import ReactWordcloud from "react-wordcloud";
import { useEffect, useState } from "react";

const words = [
  {
    text: "CSS",
    value: 64,
  },
  {
    text: "Web",
    value: 11,
  },
  {
    text: "Java",
    value: 16,
  },
  {
    text: "XML",
    value: 17,
  },
  {
    text: "php",
    value: 44,
  },
  {
    text: "C++",
    value: 32,
  },
];

export default function WordCloud() {
  const [render, setRender] = useState(false);

  useEffect(() => {
    setRender(true);
  }, []);

  return (
    <div className="col-span-3 border-[1px] border-black/5 shadow-sm row-span-2 bg-white rounded-2xl flex items-center justify-center">
      <div className="wordcloud-wrapper">
        {render && (
          <ReactWordcloud
            words={words}
            size={[100, 100]}
            options={{
              colors: ["#000000", "#6D748C"],
              scale: "sqrt",
              spiral: "archimedean",
              fontSizes: [40, 120],
              // fontFamily: "Inter",
              rotations: 0,
              enableTooltip: false,
              deterministic: true,
            }}
          />
        )}
      </div>
    </div>
  );
}
