import Header from "./Header";

export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <div className="layout-wrapper h-screen w-screen bg-gray-100 flex flex-col pb-8">
      <Header />
      {children}
    </div>
  );
}
