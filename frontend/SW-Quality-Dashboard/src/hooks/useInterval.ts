import { useEffect } from "react";

function useInterval(interval: number, fn: () => any) {
  useEffect(() => {
    const intervalId = setInterval(fn, interval);
    return () => clearInterval(intervalId);
  }, [fn]);
}
export default useInterval;
