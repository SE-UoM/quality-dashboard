import { BarChart, Card, Subtitle, Title } from "@tremor/react";

const chartdata = [
  {
    name: "Birds",
    "Number of threatened species": 1445,
  },
  {
    name: "Amphibians",
    "Number of threatened species": 2488,
  },
  {
    name: "Crustaceans",
    "Number of threatened species": 743,
  },
];

const valueFormatter = (num: number) =>
  `$ ${new Intl.NumberFormat("us").format(num).toString()}`;

export default function TopLanguages() {
  return (
    <div className="col-span-2 bg-white h-full flex flex-col items-center rounded-2xl gap-12 justify-between pt-12 p-6">
      <span className="text-black tracking-tighter font-medium text-3xl">
        Top Languages
      </span>
      <BarChart
        className="mt-6 basis-[80%]"
        data={chartdata}
        showAnimation={true}
        index="name"
        showTooltip={false}
        showLegend={false}
        categories={["Number of threatened species"]}
        colors={["blue", "red", "green"]}
        valueFormatter={valueFormatter}
        yAxisWidth={80}
        showYAxis={false}
      />
    </div>
  );
}
