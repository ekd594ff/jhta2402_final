import React from 'react';
import {DndProvider, useDrag, useDrop} from "react-dnd";
import Button from "@mui/material/Button";
import Stack from "@mui/material/Stack";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import {Card, Grid2, Select} from "@mui/material";
import {HTML5Backend} from "react-dnd-html5-backend";

const ItemTypes = {
    SOLUTION: 'solution',
};

function SolutionForm({solutions, setSolutions}) {

    const addSolution = () => {

        setSolutions([...solutions, {id: '', title: '', description: '', price: ''}]);
    };

    const moveSolution = (dragIndex, hoverIndex) => {
        const newSolutions = [...solutions];
        const [draggedSolution] = newSolutions.splice(dragIndex, 1);
        newSolutions.splice(hoverIndex, 0, draggedSolution);
        setSolutions(newSolutions);
    };

    const handleInputChange = (index, event) => {
        const newSolutions = [...solutions];
        newSolutions[index][event.target.name] = event.target.value;
        setSolutions(newSolutions);
    };

    const handleCurrencyChange = (index, event) => {
        const newSolutions = [...solutions];
        newSolutions[index].currency = event.target.value;
        setSolutions(newSolutions);
    };

    const handleRemoveSolution = (index) => {
        const newSolutions = solutions.filter((_, i) => i !== index);
        setSolutions(newSolutions);
    };


    return (
        <DndProvider backend={HTML5Backend}>
            <Button id="addSolution" onClick={addSolution}
                    variant="outlined"
                    style={{
                        borderColor: '#FA4D56',
                        color: '#FA4D56',
                        margin: '32px 16px'
                    }}
            >솔루션 추가</Button>
            <div>
                {solutions.map((solution, index) => (
                    <SolutionItem
                        key={index}
                        index={index}
                        solution={solution}
                        moveSolution={moveSolution}
                        handleInputChange={handleInputChange}
                        handleCurrencyChange={handleCurrencyChange}
                        handleRemoveSolution={handleRemoveSolution}
                    />
                ))}
            </div>
        </DndProvider>
    );
}

const SolutionItem = ({
                          solution,
                          index,
                          moveSolution,
                          handleInputChange,
                          handleCurrencyChange,
                          handleRemoveSolution
                      }) => {

    const ref = React.useRef(null);

    const [, drop] = useDrop({
        accept: ItemTypes.SOLUTION,
        hover(item) {
            if (!ref.current) {
                return;
            }
            const dragIndex = item.index;
            const hoverIndex = index;

            if (dragIndex === hoverIndex) {
                return;
            }

            moveSolution(dragIndex, hoverIndex);
            item.index = hoverIndex;
        },
    });

    const [{isDragging}, drag] = useDrag({
        type: ItemTypes.SOLUTION,
        item: {index},
        collect: (monitor) => ({
            isDragging: monitor.isDragging(),
        }),
    });

    drag(drop(ref));

    return (
        <Card ref={ref} variant="outlined" style={{padding: "16px", margin: "24px 0"}}>
            <Stack
                key={index}
                direction="column"
                spacing={2}
                sx={{marginTop: 2, alignItems: 'left'}}
            >
                <Typography variant="h6">{`${index + 1}번째 솔루션`}</Typography>
                <TextField
                    variant="outlined"
                    type="text"
                    name="title"
                    placeholder="솔루션 제목"
                    value={solution.title}
                    onChange={(event) => handleInputChange(index, event)}
                ></TextField>
                <TextField
                    variant="outlined" name="description"
                    placeholder="솔루션 설명"
                    value={solution.description}
                    onChange={(event) => handleInputChange(index, event)}
                    style={{width: '100%'}}
                ></TextField>
                <Grid2 container spacing={1}>
                    <Grid2 size={8}>
                        <TextField
                            variant="outlined"
                            type="number"
                            name="price"
                            placeholder="솔루션 최소 가격"
                            value={solution.price}
                            onChange={(event) => handleInputChange(index, event)}
                            style={{width: '100%'}}
                        />
                    </Grid2>
                    <Grid2 size={4}>
                        <Select
                            value={solution.currency}
                            defaultValue="KRW"
                            onChange={(event) => handleCurrencyChange(index, event)}
                            style={{width: '100%'}}
                            variant="outlined">
                            <option value="KRW">원 (KRW)</option>
                        </Select>
                    </Grid2>
                </Grid2>
                <div>
                    <Button
                        variant="outlined"
                        onClick={() => handleRemoveSolution(index)}
                        style={{borderColor: '#FA4D56', color: '#FA4D56'}}
                    >삭제</Button>
                </div>

            </Stack>
        </Card>
    );
}

export default SolutionForm;