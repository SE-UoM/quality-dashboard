import Navigtaion from "./Navigation";

export default function Header() {
  return (
    <header className="w-screen flex flex-row items-center py-6 px-8 justify-between">
      <h1 className="font-medium text-2xl tracking-tighter">
        Quality Dashboard
      </h1>
      <Navigtaion />
    </header>
  );
}
