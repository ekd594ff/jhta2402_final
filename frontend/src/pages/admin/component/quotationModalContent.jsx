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

function QuotationModalContent(inputValue) {
    const [value, setValue] = useState(inputValue);
    return <div>
        <TextField
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
            label="TOTAL-TRANSACTION-AMOUNT"
            defaultValue= {inputValue.totalTransactionAmount}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            label="PROGRESS"
            defaultValue= {inputValue.progress}
            select
            variant="filled"
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
                await axios.patch(`/api/quotation/admin`, {
                    id: value.id,
                    progress: value.progress,
                }).catch(response => console.log(response));
            console.log("role", value.role);
            console.log("value",value);
            console.log("event", event);
        }}>
            수정
        </Button>
    </div>
}

export default QuotationModalContent;