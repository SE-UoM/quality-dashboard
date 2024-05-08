export default function getRelativeScreenHeightInPixels(heightPercentage: number): number {
    return window.innerHeight * (heightPercentage / 100);
}

export function getRelativeScreenWidthInPixels(widthPercentage: number): number {
    return window.innerWidth * (widthPercentage / 100);
}