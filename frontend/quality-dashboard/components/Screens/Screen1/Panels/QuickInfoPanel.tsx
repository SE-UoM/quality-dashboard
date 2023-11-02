export default function QuickInfoPanel({
  value,
  text,
  icon,
}: {
  value: string;
  text: string;
  icon: React.ReactNode;
}) {
  return (
    <div className="bg-white border-[1px] border-black/5 shadow-sm col-span-1 row-span-1 rounded-2xl flex flex-row items-center justify-center gap-8">
      <span className="w-16">{icon}</span>
      <div className="flex flex-col gap-1">
        <span className="text-black font-semibold text-4xl">{value}</span>
        <span className="text-black/60 font-medium text-2xl">{text}</span>
      </div>
    </div>
  );
}
