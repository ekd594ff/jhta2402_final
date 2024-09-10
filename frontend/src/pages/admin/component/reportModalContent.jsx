import {TextField} from "@mui/material";
import * as React from "react";

function ReportModalContent(inputValue) {
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
            label="REF-TITLE"
            defaultValue= {inputValue.refTitle}
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
            label="SORT"
            defaultValue= {inputValue.sort}
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
            label="COMMENT"
            defaultValue= {inputValue.comment}
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
export default ReportModalContent;