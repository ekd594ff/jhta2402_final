import {TextField} from "@mui/material";
import * as React from "react";

function PortfolioModalContent(inputValue) {
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
            label="COMPANY-NAME"
            defaultValue= {inputValue.companyName}
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
        <TextField
            id="filled-read-only-input"
            label="DELETED"
            defaultValue= {inputValue.deleted}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
        <TextField
            id="filled-read-only-input"
            label="ACTIATED"
            defaultValue= {inputValue.activated}
            variant="filled"
            slotProps={{
                input: {
                    readOnly: true,
                },
            }}
        />
    </div>
}

export default PortfolioModalContent;