import React, {useState, useRef} from 'react';
import Box from "@mui/material/Box";
import {Button} from "@mui/material";
import Stack from "@mui/material/Stack";
import Typography from "@mui/material/Typography";
import {DndProvider, useDrag, useDrop} from 'react-dnd';
import {HTML5Backend} from 'react-dnd-html5-backend';
//import style from "../../styles/_variables.scss";

const ItemTypes = {
    SOLUTION: 'solution',
};

const SolutionForm = () => {

    const [solutions, setSolutions] = useState([]);

    const addSolution = () => {
        setSolutions([...solutions, {title: '', description: '', price: ''}]);
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
            <Box sx={{padding: 1, textAlign: 'left'}}>
                <Button id="addSolution" onClick={addSolution}>+솔루션 추가</Button>
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
            </Box>
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
        <Box
            ref={ref}
            key={index}
            sx={{
                padding: 2,
                border: '1px solid #ccc',
                borderRadius: '4px',
                marginBottom: 2,
            }}
        >
            <Stack
                key={index}
                direction="column"
                spacing={2}
                sx={{marginTop: 2, alignItems: 'left'}}
            >
                <Typography variant="h7">{`${index + 1}번째 솔루션`}</Typography>
                <input
                    type="text"
                    name="title"
                    placeholder="title"
                    value={solution.title}
                    onChange={(event) => handleInputChange(index, event)}
                    style={{width: '100%', height: '30px', padding: '0px'}}
                ></input>
                <textarea
                    name="description"
                    placeholder="description"
                    value={solution.description}
                    onChange={(event) => handleInputChange(index, event)}
                    style={{width: '100%', height: '100px', padding: '0px'}}
                ></textarea>
                <Stack direction="row" spacing={1}>
                    <input
                        type="number"
                        name="price"
                        placeholder="price"
                        value={solution.price}
                        onChange={(event) => handleInputChange(index, event)}
                        style={{
                            width: '80%',
                            height: '30px',
                            padding: '0px',
                            border: '1px solid #ccc',
                            borderRadius: '4px'
                        }}
                    />
                    <select
                        value={solution.currency}
                        onChange={(event) => handleCurrencyChange(index, event)}
                        style={{
                            width: '20%',
                            height: '30px',
                            padding: '0px',
                            border: '1px solid #ccc',
                            borderRadius: '4px'
                        }}
                    >
                        <option value="KRW">원 (KRW)</option>
                    </select>
                </Stack>
                <div className={style['deleteBtn']}>
                    <Button
                        variant="outlined"
                        onClick={() => handleRemoveSolution(index)}
                    >삭제</Button>
                </div>

            </Stack>
        </Box>
    );

}
export default SolutionForm;