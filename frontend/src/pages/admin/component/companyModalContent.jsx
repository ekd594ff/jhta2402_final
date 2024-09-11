import {Button, TextField} from "@mui/material";
import * as React from "react";
import {useState} from "react";
import axios from "axios";
import MenuItem from "@mui/material/MenuItem";


const isApplied = [
    {
        value : true,
        label : 'true',
    },
    {
        value : false,
        label : 'false',
    },
]
function CompanyModalContent(inputValue) {
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
            label="COMPANY-NAME"
            defaultValue= {inputValue.companyName}
            variant="filled"
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, companyName: value};
                });
            }}
        />
        <TextField
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
            label="PHONE"
            defaultValue= {inputValue.phone}
            variant="filled"
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, phone: value};
                });
            }}
        />
        <TextField
            label="ADDRESS"
            defaultValue= {inputValue.address}
            variant="filled"
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, address: value};
                });
            }}
        />
        <TextField
            label="DETAIL-ADDRESS"
            defaultValue= {inputValue.detatilAddress}
            variant="filled"
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, detatilAddress: value};
                });
            }}
        />
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
            label="APPLIED"
            defaultValue= {inputValue.applied}
            select
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, applied: value};
                });
            }}
        >
            {isApplied.map((option) => (
                <MenuItem key={option.value} value={option.value}>
                    {option.label}
                </MenuItem>
            ))}
        </TextField>
        <Button onClick={async (event) => {
            const response =
                await axios.patch(`/api/company/admin`, {
                    id: value.id,
                    companyName : value.companyName,
                    description : value.description,
                    phone : value.phone,
                    address : value.address,
                    detailAddress : value.detailAddress,
                    isApplied : value.applied,
                }).catch(response => console.log(response));
        }}>
            수정
        </Button>
    </div>
}

export default CompanyModalContent;