import {Button, TextField} from "@mui/material";
import * as React from "react";
import axios from "axios";
import {useState} from "react";
import MenuItem from "@mui/material/MenuItem";


const isDeleted = [
    {
        value : true,
        label : 'true',
    },
    {
        value : false,
        label : 'false',
    },
];

function PortfolioModalContent(inputValue) {
    const [value, setValue] = useState(inputValue);
    return <div>
        <TextField
            label="ID"
            defaultValue={inputValue.id}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            label="TITLE"
            defaultValue={inputValue.title}
            variant="filled"
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, title: value};
                });
            }}
        />
        <TextField
            label="DESCRIPTION"
            defaultValue={inputValue.description}
            variant="filled"
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, description: value};
                });
            }}
        />
        <TextField
            label="COMPANY-NAME"
            defaultValue={inputValue.companyName}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            label="CREATEDAT"
            defaultValue={inputValue.createdAt}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            label="UPDATEDAT"
            defaultValue={inputValue.updatedAt}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            label="DELETED"
            defaultValue={inputValue.deleted}
            select
            variant="filled"
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, deleted: value};
                });
            }}
        >
            {isDeleted.map((option) => (
                <MenuItem key={option.value} value={option.value}>
                    {option.label}
                </MenuItem>
            ))}
        </TextField>
        <TextField
            label="ACTIVATED"
            defaultValue={inputValue.activated}
            variant="filled"
            select
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, deleted: value};
                });
            }}
        >
            {isDeleted.map((option) => (
                <MenuItem key={option.value} value={option.value}>
                    {option.label}
                </MenuItem>
            ))}
        </TextField>
        <Button onClick={async (event) => {
            const response =
                await axios.patch(`/api/portfolio/admin`, {
                    id: value.id,
                    title: value.title,
                    description: value.description,
                    isDeleted: value.deleted,
                    isActivated: value.activated,
                }).catch(response => console.log(response));
            console.log("value", value);
        }}>
            수정
        </Button>
    </div>;
}

export default PortfolioModalContent;