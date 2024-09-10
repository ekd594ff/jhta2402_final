import {TextField} from "@mui/material";
import * as React from "react";

function QuotationModalContent(inputValue) {
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
            id="filled-read-only-input"
            label="PROGRESS"
            defaultValue= {inputValue.progress}
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
    </div>
}

export default QuotationModalContent;