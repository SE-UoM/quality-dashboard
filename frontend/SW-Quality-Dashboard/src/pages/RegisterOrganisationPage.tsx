import React, { useState } from 'react';
import {
    Box,
    Flex,
    FormControl,
    FormLabel,
    Input,
    Select,
    Button,
    Text,
} from '@chakra-ui/react';

const locations = ['Athens', 'Thessaloniki', 'Heraklion', 'Patras', 'Larissa'];

const categories = ['Student Club', 'University', 'Organisation'];

function OrganisationForm() {
    const [selectedFile, setSelectedFile] = useState<File | null>(null);

    const handleFileChange = (event: any) => {
        const file = event.target.files[0];
        setSelectedFile(file);
    };

    return (
        <Box p={8} bg="gray.100" flex="1">
            <Text fontWeight="bold" fontSize="xl" mb={4}>
                Organisation Data (Required)
            </Text>
            <form>
                <FormControl id="name" mb={4} outline={"black"}>
                    <FormLabel>Name</FormLabel>
                    <Input type="text" placeholder="Enter name" />
                </FormControl>

                <FormControl id="location" mb={4}>
                    <FormLabel>Location</FormLabel>
                    <Select placeholder="Select location">
                        {locations.map((location) => (
                            <option key={location} value={location}>
                                {location}
                            </option>
                        ))}
                    </Select>
                </FormControl>

                <FormControl id="category" mb={4}>
                    <FormLabel>Category</FormLabel>
                    <Select placeholder="Select category">
                        {categories.map((category) => (
                            <option key={category} value={category}>
                                {category}
                            </option>
                        ))}
                    </Select>
                </FormControl>

                <FormControl id="icon" mb={4}>
                    <FormLabel>Icon (JPG or PNG)</FormLabel>
                    <Box
                        border="2px"
                        borderRadius="md"
                        borderColor="gray.300"
                        bg="white"
                        p={4}
                        cursor="pointer"

                    >
                        {selectedFile ? (
                            <Text>{selectedFile.name}</Text>
                        ) : (
                            <Text color="gray.500">Drag and drop your file here, or click to select a file</Text>
                        )}
                    </Box>
                    <Input
                        type="file"
                        accept="image/jpeg,image/png"

                        onChange={handleFileChange}
                        style={{ display: 'none' }}
                    />
                </FormControl>
                <Button colorScheme="teal" size="lg" mt={4} w="65%" alignSelf={"center"}>
                    Submit
                </Button>

            </form>
        </Box>
    );
}

function RightColumn() {
    return (<Box flex="1" p={8} bg="gray.200">
        <Text fontWeight="bold" fontSize="xl" mb={4}>
            Organisation MetaData (Optional)
        </Text>
    </Box>);
}


export default function App() {
    return (
        <Flex direction={"column"}>
            <Flex>

                <OrganisationForm />

                {/* The right column will be implemented later */}
                {/* <RightColumn /> */}
            </Flex>

        </Flex>
    );
}
