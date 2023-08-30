import { Button, Flex, chakra } from '@chakra-ui/react'


function getIconOfLanguage(lang:string){

}


interface LangBarProps{
    langname:string,
    color:string,
    rank:number,
}

function LanguageBar({color,langname,rank}:LangBarProps){
    let height = 20-rank*5;
    
    return <Flex direction={"column"} alignItems={"center"}>
        {/* the icon will go here */}
    <chakra.span fontWeight={"semibold"}>{langname}</chakra.span>
    <chakra.div bg={color} height={`${height}rem`} width="5rem" borderRadius={"0.25rem"}>

    </chakra.div>
</Flex>
}



function ShowTopLanguages() {
    const firstLanguage = "javascript"
    const secondLanguage = "java"
    const thirdLanguage = "C++"
    
    return (
        <Flex direction={"column"} p={4} border={"solid 2px black"} height={"100%"} width={"100%"}>
            <chakra.h2 textAlign={"center"} fontSize={"4xl"} fontWeight={"semibold"}>
                Top Languages
            </chakra.h2>
            <Flex direction={"row"} width={"100%"} mt="auto" alignItems={"flex-end"} justifyContent={"space-around"} columnGap={"1rem"}>
               <LanguageBar langname={secondLanguage} rank={2} color='blue' />
               <LanguageBar langname={firstLanguage} rank={1} color='yellow' />
               <LanguageBar langname={thirdLanguage} rank={3} color='red' />
            </Flex>
        </Flex>
    )
}


export default ShowTopLanguages