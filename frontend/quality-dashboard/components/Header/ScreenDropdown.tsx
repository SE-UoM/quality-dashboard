import {
  Button,
  DropdownItem,
  DropdownTrigger,
  Dropdown,
  DropdownMenu,
} from "@nextui-org/react";
import { useRouter } from "next/router";

export default function ScreenDropdown() {
  const router = useRouter();

  return (
    <Dropdown>
      <DropdownTrigger>
        <Button
          disableRipple
          className="p-0 text-xl font-medium tracking-tight bg-white data-[hover=true]:bg-white py-6 px-12"
          radius="md"
        >
          Screens
        </Button>
      </DropdownTrigger>
      <DropdownMenu
        aria-label="Screens"
        className="w-[340px]"
        itemClasses={{
          base: "gap-4 data-[hover=true]:bg-gray-200/70 py-3",
        }}
      >
        <DropdownItem
          key="autoscaling"
          onClick={() =>
            router.query.screen !== "1" && router.push("/?screen=1")
          }
          description="A quick overview of your organization's most important metrics."
          startContent={
            <span className="px-3 text-3xl text-black font-bold">1</span>
          }
        >
          <span className="font-medium">Organization Overview</span>
        </DropdownItem>
        <DropdownItem
          key="autoscaling"
          onClick={() =>
            router.query.screen !== "2" && router.push("/?screen=2")
          }
          description="A quick overview of your organization's most important metrics."
          startContent={
            <span className="px-3 text-3xl text-black font-bold">2</span>
          }
        >
          <span className="font-medium">Organization Overview</span>
        </DropdownItem>
        <DropdownItem
          key="autoscaling"
          description="A quick overview of your organization's most important metrics."
          startContent={
            <span className="px-3 text-3xl text-black font-bold">3</span>
          }
        >
          <span className="font-medium">Organization Overview</span>
        </DropdownItem>
        <DropdownItem
          key="autoscaling"
          description="A quick overview of your organization's most important metrics."
          startContent={
            <span className="px-3 text-3xl text-black font-bold">4</span>
          }
        >
          <span className="font-medium">Organization Overview</span>
        </DropdownItem>
        <DropdownItem
          key="autoscaling"
          description="A quick overview of your organization's most important metrics."
          startContent={
            <span className="px-3 text-3xl text-black font-bold">5</span>
          }
        >
          <span className="font-medium">Organization Overview</span>
        </DropdownItem>
      </DropdownMenu>
    </Dropdown>
  );
}
