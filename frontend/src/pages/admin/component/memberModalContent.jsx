import {Button, TextField} from "@mui/material";
import * as React from "react";
import MenuItem from "@mui/material/MenuItem";
import {useState} from "react";
import axios from "axios";

const roles = [
    {
        value: 'ROLE_USER',
        label: 'ROLE_USER',
    },
    {
        value: 'ROLE_ADMIN',
        label: 'ROLE_ADMIN',
    },
    {
        value: 'ROLE_SELLER',
        label: 'ROLE_SELLER',
    },
];

const isDeleted = [
    {
        value : true,
        label : 'true',
    },
    {
        value : false,
        label : 'false',
    },
]

function MemberModalContent(inputValue) {
    const [value, setValue] = useState(inputValue);
    return <div>
        <TextField
            label="ID"
            defaultValue={inputValue.id}
            variant="filled"
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, id: value};
                });
            }}
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            label="EMAIL"
            defaultValue={inputValue.email}
            variant="filled"
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, email: value};
                });
            }}
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            label="ROLE"
            defaultValue={inputValue.role}
            select
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, role: value};
                });
            }}
        >
            {roles.map((option) => (
                <MenuItem key={option.value} value={option.value}>
                    {option.label}
                </MenuItem>
            ))}
        </TextField>
        <TextField
            label="USERNAME"
            defaultValue={inputValue.username}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            label="PLATFORM"
            defaultValue={inputValue.platform}
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
            onChange={(event) => {
                const value = event.target.value;
                setValue(prev => {
                    return {...prev, role: value};
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
                await axios.patch(`/api/member/admin`, {
                    id: value.id,
                    role : value.role,
                    isDeleted : value.deleted,
                }).catch(response => console.log(response));
            console.log("role", value.role);
            console.log("value",value);
            console.log("event", event);
        }}>
           수정
        </Button>
    </div>
}

export default MemberModalContent;