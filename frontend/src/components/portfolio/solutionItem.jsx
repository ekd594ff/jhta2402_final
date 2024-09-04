import {Button, Card, Grid2, InputLabel, Select} from "@mui/material";
import Stack from "@mui/material/Stack";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";


export const SolutionItem = ({
                                 solution,
                                 index,
                                 moveSolution,
                                 handleInputChange,
                                 handleCurrencyChange,
                                 handleRemoveSolution
                             }) => {

    return (
        <Card variant="outlined" style={{padding: "16px"}}>
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
                    placeholder="title"
                    value={solution.title}
                    onChange={(event) => handleInputChange(index, event)}
                ></TextField>
                <TextField
                    variant="outlined"
                    name="description"
                    placeholder="description"
                    value={solution.description}
                    onChange={(event) => handleInputChange(index, event)}
                    style={{width: '100%', height: '100px', padding: '0px'}}
                ></TextField>
                <Grid2 container spacing={1}>
                    <Grid2 size={8}>
                        <TextField
                            variant="outlined"
                            type="number"
                            name="price"
                            placeholder="price"
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
export default SolutionItem;