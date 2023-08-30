import { Grid } from "@chakra-ui/react";

const templateAreas= `
"projs projs submitted submitted submitted submitted bestdevs bestdevs"
"projs projs submitted submitted submitted submitted bestdevs bestdevs"
"projs projs submitted submitted submitted submitted bestdevs bestdevs"
"projs projs wordcloud wordcloud wordcloud wordcloud bestdevs bestdevs"
"projs projs wordcloud wordcloud wordcloud wordcloud bestdevs bestdevs"
"projs projs wordcloud wordcloud wordcloud wordcloud bestdevs bestdevs"
"projs projs wordcloud wordcloud wordcloud wordcloud bestdevs bestdevs"
"projs projs wordcloud wordcloud wordcloud wordcloud bestdevs bestdevs"
"logo logo logo space space logo2 logo2 logo2"
"logo logo logo space space logo2 logo2 logo2"
`

function Dashboard4(){
    return <Grid height={"92vh"}
    padding={"1rem"}
    templateAreas={templateAreas} 
    templateColumns={"repeat(8,1fr)"} 
    templateRows={"repeat(10,1fr)"}
    gap="1rem">

    </Grid>
}