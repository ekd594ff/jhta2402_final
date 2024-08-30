import Box from "@mui/material/Box";
import {Button} from "@mui/material";
import {useState} from "react";
import Stack from "@mui/material/Stack";

const SolutionForm = () => {

    const [solutions, setSolutions ] = useState([]);

    const addSolution = () => {
        setSolutions([...solutions, { title: '', description: '', price: '' }]);
    };

    const handleInputChange = (index, event) => {
        const newSolutions = [...solutions];
        newSolutions[index][event.target.name] = event.target.value;
        setSolutions(newSolutions);
    };

    return (
        <div>
            <Box sx={{ padding: 1, textAlign: 'left' }}>
                <Button id="addSolution" onClick={addSolution}>+솔루션 추가</Button>
                <div>
                    {solutions.map((solution, index) => (
                        <Stack
                            key={index}
                            direction="column"
                            spacing={2}
                            sx={{ marginTop: 2, alignItems: 'left' }}
                        >
                            <textarea
                                name="title"
                                placeholder="title"
                                value={solution.title}
                                onChange={(event) => handleInputChange(index, event)}
                            ></textarea>
                            <textarea
                                name="description"
                                placeholder="description"
                                value={solution.description}
                                onChange={(event) => handleInputChange(index, event)}
                            ></textarea>
                            <textarea
                                name="price"
                                placeholder="price"
                                value={solution.price}
                                onChange={(event) => handleInputChange(index, event)}
                            ></textarea>
                            <Button>삭제</Button>
                        </Stack>
                    ))}
                </div>
            </Box>
        </div>
    );
}
export default SolutionForm;