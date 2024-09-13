import {Button, TextField} from "@mui/material";
import * as React from "react";
import {useState} from "react";
import axios from "axios";
import MenuItem from "@mui/material/MenuItem";

const quotationProgresses = [
    {
        value : 'PENDING',
        label : 'PENDING',
    },
    {
        value : 'USER_CANCELLED',
        label : 'USER_CANCELLED',
    },
    {
        value : 'SELLER_CANCELLED',
        label : 'SELLER_CANCELLED',
    },
    {
        value : 'ADMIN_CANCELLED',
        label : 'ADMIN_CANCELLED',
    },
    {
        value : 'APPROVED',
        label : 'APPROVED',
    },
]

function QuotationRequestModalContent(inputValue) {
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
            label="PORTFOLIO-ID"
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
            label="TITLE"
            defaultValue= {inputValue.title}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            id="filled-read-only-input"
            label="DESCRIPTION"
            defaultValue= {inputValue.description}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            id="filled-read-only-input"
            label="PROGRESS"
            defaultValue= {inputValue.progress}
            variant="filled"
            select
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, progress: value};
                });
            }}
        >
            {quotationProgresses.map((option) => (
                <MenuItem key={option.value} value={option.value}>
                    {option.label}
                </MenuItem>
            ))}
        </TextField>
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
                await axios.patch(`/api/quotationRequest/admin`, {
                    id: value.id,
                    progress : value.progress,
                }).catch(response => console.log(response));
            console.log("role", value.role);
            console.log("value",value);
            console.log("event", event);
        }}>
            수정
        </Button>
    </div>
}

export default QuotationRequestModalContent;