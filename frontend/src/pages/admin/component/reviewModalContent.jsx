import {Button, TextField} from "@mui/material";
import * as React from "react";
import {useState} from "react";
import axios from "axios";

function ReviewModalContent(inputValue) {
    const [value, setValue] = useState(inputValue);
    return <div>
        <TextField
            id="filled-read-only-input"
            label="ID"
            defaultValue= {inputValue.id}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            id="filled-read-only-input"
            label="PORTFOLIOID"
            defaultValue= {inputValue.portfolioId}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            id="filled-read-only-input"
            label="USERNAME"
            defaultValue= {inputValue.username}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            id="filled-read-only-input"
            label="TITLE"
            defaultValue= {inputValue.title}
            variant="filled"
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, title: value};
                });
            }}
        />
        <TextField
            id="filled-read-only-input"
            label="DESCRIPTION"
            defaultValue= {inputValue.description}
            variant="filled"
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, description: value};
                });
            }}
        />
        <TextField
            id="filled-read-only-input"
            label="RATE"
            defaultValue= {inputValue.rate}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            id="filled-read-only-input"
            label="CREATEDAT"
            defaultValue= {inputValue.createdAt}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            id="filled-read-only-input"
            label="UPDATEDAT"
            defaultValue= {inputValue.updatedAt}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <Button onClick={async (event) => {
            const response =
                await axios.patch(`/api/review/admin`, {
                    id: value.id,
                    title : value.title,
                    description : value.description,
                }).catch(response => console.log(response));
            console.log("role", value.role);
            console.log("value",value);
            console.log("event", event);
        }}>
            수정
        </Button>
    </div>
}

export default ReviewModalContent;