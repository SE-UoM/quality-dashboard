const getNextIndex = (arr: any[], currentIndex: number) => {
  if (currentIndex === arr.length - 1) return 0;
  else return currentIndex + 1;
};

export default getNextIndex;
