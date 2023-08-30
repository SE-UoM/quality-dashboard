import { useRef, useEffect, useState } from 'react'
import { useSpring, animated } from 'react-spring'
import {chakra} from "@chakra-ui/react"
function AnimatedCount({ count }: { count: number }) {
    const [prevValue, setPrevValue] = useState(count);
    const prevValueRef = useRef<number>();

    useEffect(() => {
        prevValueRef.current = prevValue;
        setPrevValue(count)
    }, [count]);
    const { number } = useSpring({
        from: { number: 0 },
        number: count,
        delay: 200,
        config: { mass: 5, tension: 500, friction: 100 }
    })

    console.log("Prev Value: ", prevValueRef.current)
    console.log("Current Value: ", count)
    return <chakra.span fontSize={"5xl"} fontWeight={"semibold"}><animated.div>{number.to((num) => num.toFixed(0))}</animated.div></chakra.span>

}
export default AnimatedCount