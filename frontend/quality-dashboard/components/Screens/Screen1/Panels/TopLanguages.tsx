import { BarChart, Card, Subtitle, Title } from "@tremor/react";

const chartdata = [
  {
    name: "Java",
    "Lines of code": 1445,
  },
  {
    name: "HTML",
    "Lines of code": 2488,
  },
  {
    name: "CSS",
    "Lines of code": 743,
  },
];

const valueFormatter = (num: number) =>
  `$ ${new Intl.NumberFormat("us").format(num).toString()}`;

export default function TopLanguages() {
  return (
    <div className="col-span-2 border-[1px] border-black/5 shadow-sm row-span-3 bg-white h-full flex flex-col items-center rounded-2xl gap-12 justify-between pt-12 p-6">
      <div className="flex flex-col items-center">
        <span className="text-black tracking-tighter font-medium text-4xl">
          Top Languages
        </span>
        <p className="text-black/40 mt-2 font-medium">
          The most used languages by the organization
        </p>
      </div>
      <BarChart
        className="mt-6 basis-[80%]"
        data={chartdata}
        showAnimation={true}
        index="name"
        showTooltip={false}
        showLegend={false}
        categories={["Lines of code"]}
        colors={["indigo", "blue", "purple"]}
        valueFormatter={valueFormatter}
        yAxisWidth={0}
        color="purple"
        showYAxis={false}
      />
    </div>
  );
}
