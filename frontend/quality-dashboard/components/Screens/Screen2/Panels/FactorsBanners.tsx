import Image from "next/image";
import Link from "next/link";

export default function FactorsBanners() {
  return (
    <div className="col-span-2 border-[1px] border-black/5 shadow-sm bg-white relative flex items-center justify-center rounded-2xl overflow-hidden px-6 py-6">
      <div className="absolute w-full h-full flex items-center">
        <Link
          href="https://www.uom.gr/"
          target="_blank"
          className="basis-1/2 h-[50%] relative"
        >
          <Image
            src="/static/images/UoMLogo.png"
            width={500}
            height={500}
            alt="UoM Logo"
            className="w-full h-full object-contain"
          />
        </Link>
        <Link
          href="https://opensource.uom.gr/"
          target="_blank"
          className="basis-1/2 h-[50%] relative"
        >
          <Image
            src="/static/images/osLogo.png"
            width={500}
            height={500}
            alt="OpenSource UoM Logo"
            className="w-full h-full object-contain"
          />
        </Link>
      </div>
    </div>
  );
}
