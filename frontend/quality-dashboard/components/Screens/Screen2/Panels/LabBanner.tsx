import Image from "next/image";

export default function LabBanner() {
  return (
    <div className="col-span-3 border-[1px] border-black/5 shadow-sm bg-[#ececec] relative flex items-center justify-center rounded-2xl overflow-hidden px-6 py-6">
      <Image
        src="/static/images/sde-banner.png"
        width={1094}
        height={278}
        alt="SDE UoM Banner"
        className="h-[80%] w-full absolute object-contain"
      />
    </div>
  );
}
