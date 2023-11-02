import { DonutChart, Legend } from "@tremor/react";

const smells = [
  {
    name: "Minor",
    occurencies: 3,
  },
  {
    name: "Major",
    occurencies: 25,
  },
  {
    name: "Critical",
    occurencies: 6,
  },
  {
    name: "Blocker",
    occurencies: 30,
  },
  {
    name: "Info",
    occurencies: 70,
  },
];

const total = smells.reduce((acc, curr) => acc + curr.occurencies, 0);
const categories = smells.map((smell) => smell.name);

export default function CodeSmells() {
  return (
    <div className="col-span-2 row-span-2 bg-white border-[1px] border-black/5 shadow-sm flex items-center justify-center">
      <div className="flex flex-row items-center gap-12">
        <div className="flex flex-col items-center">
          <span className="text-3xl font-semibold">Total Code Smells</span>
          <div className="relative flex items-center justify-center">
            <span className="absolute text-4xl font-bold text-black">
              {total}
            </span>
            <DonutChart
              className="mt-6 w-[15vw] h-[15vw]"
              showAnimation={true}
              data={smells}
              showLabel={false}
              category="occurencies"
              showTooltip={false}
              index="name"
              // valueFormatter={valueFormatter}
              colors={["slate", "violet", "indigo", "rose", "cyan", "amber"]}
            />
          </div>
        </div>
        <Legend
          className="mt-3 flex-col"
          categories={categories}
          colors={["slate", "violet", "indigo", "rose", "cyan", "amber"]}
        />
      </div>
    </div>
  );
}
